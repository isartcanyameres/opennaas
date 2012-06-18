package org.opennaas.core.queue;

import java.util.concurrent.Semaphore;

import junit.framework.Assert;

import org.junit.Test;
import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.mock.MockAction;

public class QueueTest {

	@Test
	public void submitQStoresQueueExecution() throws SubmitionException {
		IExtendedQueueCapability qCap = getQueueCapability();
		IAction actionA = new MockAction();
		qCap.add(actionA);

		ExecutionId execId = qCap.submit();
		IQueueExecutionRepository qExecRepo = getQueueExecutionRepository();
		ITransaction qExec = qExecRepo.get(execId);
		Assert.assertNotNull(qExec);
	}

	@Test
	public void executionContainsAllActionsInQ() throws SubmitionException {

		IExtendedQueueCapability qCap = getQueueCapability();
		IAction actionA = new MockAction();
		IAction actionB = new MockAction();
		IAction actionC = new MockAction();

		qCap.add(actionA);
		qCap.add(actionB);
		qCap.add(actionC);

		ExecutionId execId = qCap.submit();
		IQueueExecutionRepository qExecRepo = getQueueExecutionRepository();
		IQueueExecution qExec = qExecRepo.get(execId);

		Assert.assertEquals(3, qExec.getTransactionOperations().size());
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionA));
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionB));
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionC));
	}

	@Test
	public void submmitQIsProtectedWithMutex() throws SubmitionException, InterruptedException {

		IExtendedQueueCapability qCap = getQueueCapability();

		WaitingAction waitingAction = new WaitingAction();

		Semaphore semaphore = new Semaphore(1);
		semaphore.acquire();
		Thread t = new SubmitterThread(qCap, semaphore, waitingAction);
		t.start();

		semaphore.acquire();

		int eExecRepoSize = getQueueExecutionRepository().listIds().size();

		qCap.add(new MockAction());

		Exception e = null;
		try {
			qCap.submit();
		} catch (SubmitionException e1) {
			e = e1;
		}
		Assert.assertNotNull("Submitting a Q while another Q is executing should throw SubmitionException, but didn't", e);
		Assert.assertTrue("Failed submition has not modified qExecRepo", getQueueExecutionRepository().listIds().size() == eExecRepoSize);

		waitingAction.stopWaiting();
	}

	// TODO TEST queue lock/unlock works

	// TODO TEST user can access up-to-date Tx status from QExecutionRepository
	// TODO TEST an execution remains in QExecutionRepository after finishing

	private IQueueExecutionRepository getQueueExecutionRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	private IExtendedQueueCapability getQueueCapability() {
		// TODO Auto-generated method stub
		return null;
	}

	private class SubmitterThread extends Thread {

		Semaphore					semaphore;
		IExtendedQueueCapability	qCap;
		IAction						action;

		public SubmitterThread(IExtendedQueueCapability qCap, Semaphore semaphore, IAction action) {
			this.qCap = qCap;
			this.semaphore = semaphore;
			this.action = action;
		}

		public void run() {

			qCap.add(action);
			semaphore.release();
			try {
				ExecutionId txId1 = qCap.submit();
			} catch (SubmitionException e) {
			}
		}
	}

}

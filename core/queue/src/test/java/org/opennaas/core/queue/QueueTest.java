package org.opennaas.core.queue;

import junit.framework.Assert;

import org.junit.Test;
import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.TransactionId;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.mock.MockAction;

public class QueueTest {

	@Test
	public void submitQStoresQueueExecution() throws SubmitionException {
		IExtendedQueueCapability qCap = getQueueCapability();
		QueueId qId = qCap.createQueue();
		IAction actionA = new MockAction();
		qCap.add(qId, actionA);

		TransactionId execId = qCap.submit(qId);
		IQueueExecutionRepository qExecRepo = getQueueExecutionRepository();
		ITransaction qExec = qExecRepo.get(execId);
		Assert.assertNotNull(qExec);
	}

	@Test
	public void executionContainsAllActionsInQ() throws SubmitionException {

		IExtendedQueueCapability qCap = getQueueCapability();
		QueueId qId = qCap.createQueue();
		IAction actionA = new MockAction();
		IAction actionB = new MockAction();
		IAction actionC = new MockAction();

		qCap.add(qId, actionA);
		qCap.add(qId, actionB);
		qCap.add(qId, actionC);

		TransactionId execId = qCap.submit(qId);
		IQueueExecutionRepository qExecRepo = getQueueExecutionRepository();
		ITransaction qExec = qExecRepo.get(execId);

		Assert.assertEquals(3, qExec.getTransactionOperations().size());
		Assert.assertTrue(qExec.getTransactionOperations().containsAll(qCap.listActions(qId)));
	}

	@Test
	public void executionContainsAllActionsInQAfterDestroyQ() throws SubmitionException {

		IExtendedQueueCapability qCap = getQueueCapability();
		QueueId qId = qCap.createQueue();
		IAction actionA = new MockAction();
		IAction actionB = new MockAction();
		IAction actionC = new MockAction();

		qCap.add(qId, actionA);
		qCap.add(qId, actionB);
		qCap.add(qId, actionC);

		TransactionId execId = qCap.submit(qId);

		qCap.destroyQueue(qId);

		IQueueExecutionRepository qExecRepo = getQueueExecutionRepository();
		ITransaction qExec = qExecRepo.get(execId);

		Assert.assertEquals(3, qExec.getTransactionOperations().size());
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionA));
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionB));
		Assert.assertTrue(qExec.getTransactionOperations().contains(actionC));
	}

	@Test
	public void submmitQIsProtectedWithMutex() throws SubmitionException {

		IExtendedQueueCapability qCap = getQueueCapability();

		QueueId qId1 = qCap.createQueue();
		QueueId qId2 = qCap.createQueue();

		WaitingAction waitingAction = new WaitingAction();
		qCap.add(qId1, waitingAction);
		TransactionId txId1 = qCap.submit(qId1);

		int eExecRepoSize = getQueueExecutionRepository().listIds().size();

		qCap.add(qId2, new MockAction());

		Exception e = null;
		try {
			qCap.submit(qId2);
		} catch (SubmitionException e1) {
			e = e1;
		}
		Assert.assertNotNull("Submitting a Q while another Q is executing should throw SubmitionException, but didn't", e);
		Assert.assertTrue("Failed submition has not modified qExecRepo", getQueueExecutionRepository().listIds().size() == eExecRepoSize);

		waitingAction.stopWaiting();
	}

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

}

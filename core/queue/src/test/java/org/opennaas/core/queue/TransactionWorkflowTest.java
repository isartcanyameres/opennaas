package org.opennaas.core.queue;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opennaas.core.queue.impl.engine.ExecutionEngine;
import org.opennaas.core.queue.impl.transaction.ActionsTransaction;
import org.opennaas.core.queue.impl.transaction.TransactionCoordinator;
import org.opennaas.core.queue.old.EngineLockedException;
import org.opennaas.core.queue.old.ICompositeExecutionEngine;
import org.opennaas.core.queue.old.IExecutionEngine;
import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxStatus;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.mock.MockActionError;
import org.opennaas.core.resources.mock.MockActionExceptionOnExecute;
import org.opennaas.core.resources.mock.MockActionOk;
import org.opennaas.core.resources.queue.QueueConstants;

public class TransactionWorkflowTest {

	IExecutionEngine	engine;

	ActionSet			qActionSet;

	@BeforeClass
	public void initExecutionEngine() {
		engine = new ExecutionEngine();
	}

	@Before
	public void initActionSet() {
		qActionSet = new ActionSet();
		qActionSet.putAction(QueueConstants.PREPARE, MockActionOk.class);
		qActionSet.putAction(QueueConstants.VALIDATE, MockActionOk.class);
		qActionSet.putAction(QueueConstants.CONFIRM, MockActionOk.class);
		qActionSet.putAction(QueueConstants.RESTORE, MockActionOk.class);
		qActionSet.putAction(QueueConstants.REFRESH, MockActionOk.class);
	}

	@Test
	public void okTxStateTransitions() throws EngineLockedException {

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		// TODO check state transition follows this order:
		// TxStatus.CREATED;
		// TxStatus.WAITING;
		// TxStatus.BEGGINNING;
		// TxStatus.BEGIN_FINISHED;
		// TxStatus.WAITING;
		// TxStatus.COMMITING;
		// TxStatus.FINISHED;

	}

	@Test
	public void okTxStateTransitionsWithCoord() throws EngineLockedException {
		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		ITransactionCoordinator txCoordinator = new TransactionCoordinator();
		ITransactionWrapper txw = ((ICompositeExecutionEngine) engine).submitCoordinated(tx);
		txCoordinator.register(txw);

		Assert.assertEquals(TxStatus.WAITING, ((ITransactionWrapper) tx).getStatus());
		txCoordinator.begin();
		waitUntilTxHasFinishedBegin(tx);
		Assert.assertEquals(TxStatus.WAITING, ((ITransactionWrapper) tx).getStatus());
		txCoordinator.commit();

		waitUntilTxIsFinished(tx);

		// TODO check state transition follows this order:
		// TxStatus.CREATED;
		// TxStatus.WAITING;
		// TxStatus.BEGGINNING;
		// TxStatus.BEGIN_FINISHED;
		// TxStatus.WAITING;
		// TxStatus.COMMITING;
		// TxStatus.FINISHED;
	}

	@Test
	public void txResultsOnlyAvailableWhenSet() throws EngineLockedException {
		// beginResult is only available in status after BEGIN_FINISHED included
		// result is only available in status FINISHED

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		ITransactionCoordinator txCoordinator = new TransactionCoordinator();
		ITransactionWrapper txw = ((ICompositeExecutionEngine) engine).submitCoordinated(tx);
		txCoordinator.register(txw);

		txCoordinator.begin();
		txCoordinator.commit();

		// TODO subscribe to changes in txStatus and run checkResultsInStatus in each.

		checkResultsInStatus(txw.getStatus(), txw.getBeginResult(), txw.getResult());

		waitUntilTxIsFinished(tx);
	}

	private void checkResultsInStatus(TxStatus currentStatus, TxResult beginResult, TxResult result) {
		// beginResult is only available in status after BEGIN_FINISHED included
		// result is only available in status FINISHED

		if (currentStatus.equals(TxStatus.FINISHED)) {
			Assert.assertFalse(result.equals(TxResult.UNKNOWN));
		} else {
			Assert.assertEquals(TxResult.UNKNOWN, result);
		}

		if (currentStatus.ordinal() >= TxStatus.BEGIN_FINISHED.ordinal()) {
			Assert.assertFalse(beginResult.equals(TxResult.UNKNOWN));
		} else {
			Assert.assertEquals(TxResult.UNKNOWN, beginResult);
		}
	}

	@Test
	public void txSuccessIfAllIsOk() throws EngineLockedException {

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.OK, ((ITransactionWrapper) tx).getResult());
		Assert.assertEquals(TxStatus.FINISHED, ((ITransactionWrapper) tx).getStatus());
	}

	@Test
	public void errorInPrepareCausesTxAbort() throws EngineLockedException {
		qActionSet.putAction(QueueConstants.PREPARE, MockActionError.class);

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		qActionSet.putAction(QueueConstants.PREPARE, MockActionExceptionOnExecute.class);
		tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());
	}

	@Test
	public void errorInExecuteActionsCausesTxAbort() throws EngineLockedException {

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionError());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		tx = new ActionsTransaction();
		((ActionsTransaction) tx).setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionExceptionOnExecute());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());
	}

	@Test
	public void errorInValidateCausesTxAbort() throws EngineLockedException {
		qActionSet.putAction(QueueConstants.VALIDATE, MockActionError.class);

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		qActionSet.putAction(QueueConstants.VALIDATE, MockActionExceptionOnExecute.class);

		tx = new ActionsTransaction();
		((ActionsTransaction) tx).setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		engine.submit(tx);

		waitUntilTxIsFinished(tx);

		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());
	}

	@Test
	public void txAbortsWhenCoordinatorOrders() throws EngineLockedException {

		ActionsTransaction tx = new ActionsTransaction();
		tx.setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());

		ITransactionCoordinator txCoordinator = new TransactionCoordinator();
		ITransactionWrapper txw = ((ICompositeExecutionEngine) engine).submitCoordinated(tx);
		txCoordinator.register(txw);

		txCoordinator.abort(); // abort before begin
		waitUntilTxIsFinished(tx);
		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		// TODO check state transition follows this order:
		// TxStatus.CREATED;
		// TxStatus.WAITING;
		// TxStatus.ABORTING;
		// TxStatus.FINISHED;

		tx = new ActionsTransaction();
		((ActionsTransaction) tx).setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());
		txCoordinator = new TransactionCoordinator();
		txw = ((ICompositeExecutionEngine) engine).submitCoordinated(tx);
		txCoordinator.register(txw);

		txCoordinator.begin();
		waitUntilTxHasBegin(tx);
		txCoordinator.abort(); // abort after begin
		waitUntilTxIsFinished(tx);
		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		// TODO check state transition follows this order:
		// TxStatus.CREATED;
		// TxStatus.WAITING;
		// TxStatus.BEGGINNING;
		// TxStatus.BEGIN_FINISHED; ???
		// TODO It has to be decided if abort should interrupt begin, or wait for it to finish
		// TxStatus.ABORTING;
		// TxStatus.FINISHED;

		tx = new ActionsTransaction();
		((ActionsTransaction) tx).setqActionSet(qActionSet);
		tx.getTransactionOperations().add(new MockActionOk());
		txCoordinator = new TransactionCoordinator();
		txw = ((ICompositeExecutionEngine) engine).submitCoordinated(tx);
		txCoordinator.register(txw);
		txCoordinator.begin();
		waitUntilTxHasFinishedBegin(tx);
		txCoordinator.abort(); // abort after begin has finished
		waitUntilTxIsFinished(tx);
		Assert.assertEquals(TxResult.ABORTED, ((ITransactionWrapper) tx).getResult());

		// TODO check state transition follows this order:
		// TxStatus.CREATED;
		// TxStatus.WAITING;
		// TxStatus.BEGGINNING;
		// TxStatus.BEGIN_FINISHED;
		// TxStatus.WAITING;
		// TxStatus.ABORTING;
		// TxStatus.FINISHED;
	}

	// TODO test:
	// returning error in commit causes Tx to abort????
	// a failure in commit causes Tx to abort????

	// TxCoordinator has access to Tx status when tx finished begin
	// TxCoordinator has access to Tx status when tx finished

	// Tx is aborted if subTx aborts in begin()
	// Tx is aborted if subTx aborts in commit()????

	// what if txCoordinator never tells registered txs to commit/abort?
	// All engines involved in this composite tx will be locked for ever, waiting for a signal.

	private void waitUntilTxHasBegin(ITransaction tx) {
		// TODO Auto-generated method stub

	}

	private void waitUntilTxHasFinishedBegin(ITransaction tx) {
		// TODO Auto-generated method stub

	}

	private void waitUntilTxIsFinished(ITransaction tx) {
		// TODO Auto-generated method stub

	}
}

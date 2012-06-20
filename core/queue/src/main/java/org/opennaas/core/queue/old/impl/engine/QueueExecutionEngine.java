package org.opennaas.core.queue.old.impl.engine;

import java.util.List;

import org.opennaas.core.queue.impl.execution.QueueExecution;
import org.opennaas.core.queue.impl.transaction.ActionsTransaction;
import org.opennaas.core.queue.old.EngineLockedException;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.IQueueExecutionRepository;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.resources.action.IAction;

public class QueueExecutionEngine {

	IQueueExecutionRepository	execRepo;
	ExecutionEngine				txEngine;

	public ExecutionId submit(List<IAction> actions) throws EngineLockedException {

		lock();
		try {

			QueueExecution qExec = createQueueExecution(actions);
			ActionsTransaction tx = createActionsTransaction(qExec);
			execRepo.save(qExec);

			txEngine.submit(tx);

			return qExec.getId();

		} finally {
			unlock();
		}
	}

	public ExecutionId submitCoordinated(List<IAction> actions) throws EngineLockedException {

		lock();
		try {

			QueueExecution qExec = createQueueExecution(actions);
			ActionsTransaction tx = createActionsTransaction(qExec);
			execRepo.save(qExec);

			ITransactionWrapper txWrapper = txEngine.submitCoordinated(tx);
			qExec.setTx(txWrapper); // txWrapper can be used to control tx workflow

			return qExec.getId();

		} finally {
			// FIXME we cannot call unlock if txEngine.submitCoordinated(tx) returns before tx is finished
			unlock();
		}
	}

	private QueueExecution createQueueExecution(List<IAction> actions) {
		QueueExecution qExec = new QueueExecution();
		// TODO ASSIGN ID to qExec
		qExec.getActions().addAll(actions);
		return qExec;
	}

	private ActionsTransaction createActionsTransaction(QueueExecution qExec) {
		ActionsTransaction tx = new ActionsTransaction();
		tx.getTransactionOperations().addAll(qExec.getActions());
		qExec.setTx(tx); // link qExec with created tx
		return tx;
	}

	private void lock() {
		// TODO Auto-generated method stub

	}

	private void unlock() {
		// TODO Auto-generated method stub

	}
}

package org.opennaas.core.queue.old.impl.execution;

import java.util.List;

import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.ExecutionState;
import org.opennaas.core.queue.repository.IQueueExecution;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.resources.action.IAction;

public class QueueExecution implements IQueueExecution {

	ExecutionId			id;

	List<IAction>		submittedActions;

	ITransactionWrapper	tx;

	@Override
	public ExecutionId getId() {
		return id;
	}

	@Override
	public ExecutionResult getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAction> getActions() {
		return submittedActions;
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void abort() {
		// TODO Auto-generated method stub

	}

	public ITransactionWrapper getTx() {
		return tx;
	}

	public void setTx(ITransactionWrapper tx) {
		this.tx = tx;
	}
}

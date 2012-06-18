package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.resources.action.IAction;

public interface IQueueExecution extends ITransaction {

	public ExecutionResult getResult();

	public ExecutionState getState();

	public List<IAction> getActions();

}

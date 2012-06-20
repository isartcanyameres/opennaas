package org.opennaas.core.queue.repository;

import java.util.List;

import org.opennaas.core.resources.action.IAction;

public interface IQueueExecution {

	public ExecutionId getId();

	public ExecutionResult getResult();

	public List<IAction> getActions();

}

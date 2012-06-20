package org.opennaas.core.queue.repository;

import java.util.List;

public interface IQueueExecutionRepository {

	public IQueueExecution get(ExecutionId qExecId);

	public void put(IQueueExecution qExec);

	public List<ExecutionId> listIds();

}

package org.opennaas.core.queue;

import java.util.List;

public interface IQueueExecutionRepository {

	public IQueueExecution get(ExecutionId qExecId);

	public void save(IQueueExecution qExec);

	public List<ExecutionId> listIds();

}

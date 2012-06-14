package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.TransactionId;
import org.opennaas.core.resources.action.IAction;

public interface IQueueExecutionManager {

	/**
	 * Creates a QueueExecution from given actions, and associated with txCoordinator.
	 * 
	 * Created QueueExecution is submitted to ExecutionEngine's and stored in QueueExecutionRepository.
	 * 
	 * 
	 * @param actions
	 * @param txCoordinator
	 * @return id of created QueueExecution (qExecId). QueeuExecution can be retrieved calling IQueueExecutionRepository.get(qExecId)
	 * @throws SubmitionException
	 *             if failed to submit created QueueExecution.
	 * 
	 */
	public TransactionId submit(List<IAction> actions, ITransactionCoordinator txCoordinator) throws SubmitionException;

}

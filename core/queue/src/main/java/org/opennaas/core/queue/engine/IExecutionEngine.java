package org.opennaas.core.queue.engine;

import org.opennaas.core.queue.ExecutionId;
import org.opennaas.core.queue.transaction.ITransaction;

public interface IExecutionEngine {

	/**
	 * Runs TransactionLogic against given tx.
	 * 
	 * Executing this method locks the engine.
	 * 
	 * @param tx
	 * @throws EngineLockedException
	 *             if this engine is already locked.
	 */
	public ExecutionId submit(ITransaction tx) throws EngineLockedException;

}

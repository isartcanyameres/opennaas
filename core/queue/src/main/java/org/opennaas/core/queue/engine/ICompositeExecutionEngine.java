package org.opennaas.core.queue.engine;

import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

public interface ICompositeExecutionEngine {

	/**
	 * Runs CompositeTransactionLogic against given tx.
	 * 
	 * CompositeTransactionLogic waits for calls to begin, commit, abort in returned ITransactionWrapper for transaction to continue.
	 * 
	 * Executing this method locks the engine.
	 * 
	 * This method is not blocking. It creates Wrapper and returns immediately.
	 * 
	 * @param tx
	 * @throws EngineLockedException
	 *             if this engine is already locked.
	 */
	public ITransactionWrapper submitCoordinated(ITransaction tx) throws EngineLockedException;

}

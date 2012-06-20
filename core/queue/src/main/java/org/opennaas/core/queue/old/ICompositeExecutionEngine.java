package org.opennaas.core.queue.old;

import java.util.concurrent.Future;

import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

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
	 */
	public Future<TxResult> submitCoordinated(ITransactionWrapper tx);

	public Future<TxResult> begin();

	public Future<TxResult> commit();

	public Future<TxResult> abort();

}

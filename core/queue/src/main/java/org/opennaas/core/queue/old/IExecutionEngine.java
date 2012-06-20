package org.opennaas.core.queue.old;

import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

import com.google.common.util.concurrent.ListenableFuture;

public interface IExecutionEngine {

	/**
	 * Runs TransactionLogic against given tx.
	 * 
	 * Executing this method locks the engine.
	 * 
	 * @param tx
	 */
	public ListenableFuture<TxResult> submit(ITransactionWrapper tx);

}

package org.opennaas.core.queue.old.transaction;

import java.util.List;

import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

import com.google.common.util.concurrent.ListenableFuture;

public interface ITransactionCoordinator {

	public void register(ITransactionWrapper tx);

	public List<ITransactionWrapper> getRegisteredTransactions();

	/**
	 * Causes all registered transactions to commit, only if all of them have successfully finished begin. Otherwise, causes all registered
	 * transactions to abort. Waits for all registered transactions to finish begin, before deciding.
	 */
	public ListenableFuture<List<TxResult>> commit();

	/**
	 * Causes all registered transactions to abort
	 */
	public ListenableFuture<List<TxResult>> abort();

	/**
	 * Causes all registered transactions to begin
	 */
	public ListenableFuture<List<TxResult>> begin();

}

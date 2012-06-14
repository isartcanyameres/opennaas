package org.opennaas.core.queue.transaction;

import java.util.List;

public interface ITransactionCoordinator {

	public void register(ITransactionWrapper tx);

	public List<ITransactionWrapper> getRegisteredTransactions();

	/**
	 * Causes all registered transactions to commit, only if all of them have successfully finished begin. Otherwise, causes all registered
	 * transactions to abort. Waits for all registered transactions to finish begin, before deciding.
	 */
	public void commit();

	/**
	 * Causes all registered transactions to abort
	 */
	public void abort();

	/**
	 * Causes all registered transactions to begin
	 */
	public void begin();

}

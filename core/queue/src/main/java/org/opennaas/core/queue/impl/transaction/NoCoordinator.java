package org.opennaas.core.queue.impl.transaction;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

/**
 * A Coordinator not acting as such, hence not waiting for
 * 
 * It launches begin and commit in each registered ITransactionWrapper just after it is registered.
 * 
 * 
 * @author isart
 * 
 */
public class NoCoordinator implements ITransactionCoordinator {

	List<ITransactionWrapper>	registeredTransactions	= new ArrayList<ITransactionWrapper>();

	@Override
	public void register(ITransactionWrapper tx) {
		registeredTransactions.add(tx);
		tx.begin();
		tx.commit();
	}

	@Override
	public List<ITransactionWrapper> getRegisteredTransactions() {
		return registeredTransactions;
	}

	@Override
	public void begin() {
		// do nothing, we have already told registered tx to begin
	}

	@Override
	public void commit() {
		// do nothing, we have already told registered tx to commit
	}

	@Override
	public void abort() {
		for (ITransactionWrapper tx : getRegisteredTransactions()) {
			tx.abort();
		}
	}

}

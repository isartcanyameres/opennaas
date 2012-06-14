package org.opennaas.core.queue.impl.transaction;

import java.util.List;

import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

public class TransactionCoordinator implements ITransactionCoordinator {

	@Override
	public void register(ITransactionWrapper tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ITransactionWrapper> getRegisteredTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void abort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub

	}

}

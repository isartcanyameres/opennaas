package org.opennaas.core.queue.impl.engine.logic;

import java.util.concurrent.Callable;

import org.opennaas.core.queue.impl.transaction.TransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

public class TransactionLogic implements Callable {

	private ITransactionWrapper	tx;

	public TransactionLogic(ITransactionWrapper tx) {
		this.tx = tx;
	}

	@Override
	public Object call() throws Exception {
		ITransactionCoordinator coordinator = new TransactionCoordinator();
		coordinator.register(tx);
		tx.begin();
		coordinator.commit(); // coordinator will decide if commits or aborts tx
		return tx.getResult();
	}

}

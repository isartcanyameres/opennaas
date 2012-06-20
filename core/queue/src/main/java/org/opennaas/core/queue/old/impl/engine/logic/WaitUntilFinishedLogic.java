package org.opennaas.core.queue.old.impl.engine.logic;

import java.util.concurrent.Callable;

import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

public class WaitUntilFinishedLogic implements Callable<TxResult> {

	ITransactionWrapper	tx;

	public WaitUntilFinishedLogic(ITransactionWrapper tx) {
		this.tx = tx;
	}

	@Override
	public TxResult call() throws Exception {
		tx.waitUntilFinished();
		return tx.getResult();
	}

}

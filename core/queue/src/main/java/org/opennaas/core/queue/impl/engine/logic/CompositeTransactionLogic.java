package org.opennaas.core.queue.impl.engine.logic;

import java.util.concurrent.Callable;

import org.opennaas.core.queue.impl.transaction.TransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

/**
 * Differentiated from TransactionLogic because this does nor decide when to call next step, that decision is left to the caller of
 * ITransactionWrapper methods, probably a Coordinator.
 * 
 * @author isart
 * 
 */
public class CompositeTransactionLogic implements Callable, ITransactionWrapper {

	private ITransactionWrapper	tx;

	private final Object		signalLock				= new Object();

	private boolean				beginSignalReceived		= false;
	private boolean				commitSignalReceived	= false;
	private boolean				abortSignalReceived		= false;

	public CompositeTransactionLogic(ITransactionWrapper tx) {
		this.tx = tx;
	}

	@Override
	public Object call() throws Exception {
		ITransactionCoordinator coordinator = new TransactionCoordinator();
		coordinator.register(tx);

		waitForBeginSignal();
		if (!abortSignalReceived) {
			tx.begin();
			waitForCommmitSignal();
		}
		if (!abortSignalReceived)
			coordinator.commit(); // coordinator will decide if commits or aborts
		else
			coordinator.abort();

		return tx.getResult();
	}

	private synchronized void waitForSignal() {
		try {
			signalLock.wait();
		} catch (InterruptedException e) {
			abortSignalReceived = true;
		}
	}

	private synchronized void waitForBeginSignal() {
		while (!beginSignalReceived && !abortSignalReceived) {
			waitForSignal();
		}
	}

	private synchronized void waitForCommmitSignal() {
		while (!commitSignalReceived && !abortSignalReceived) {
			waitForSignal();
		}
	}

	@Override
	public synchronized void begin() {
		beginSignalReceived = true;
		signalLock.notify();
	}

	@Override
	public synchronized void commit() {
		commitSignalReceived = true;
		signalLock.notify();
	}

	@Override
	public synchronized void abort() {
		abortSignalReceived = true;
		signalLock.notify();
	}

	@Override
	public void waitUntilFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public TxStatus getStatus() {
		return tx.getStatus();
	}

	@Override
	public TxResult getResult() {
		return tx.getResult();
	}

	@Override
	public TxResult getBeginResult() {
		return tx.getBeginResult();
	}

}

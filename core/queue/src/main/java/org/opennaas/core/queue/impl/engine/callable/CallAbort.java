package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Callable;

import org.opennaas.core.queue.impl.engine.transaction.ActionsTransaction;
import org.opennaas.core.queue.repository.ExecutionResult;

public class CallAbort implements Callable<ExecutionResult> {

	private ActionsTransaction	tx;

	public CallAbort(ActionsTransaction tx) {
		this.tx = tx;
	}

	@Override
	public ExecutionResult call() throws Exception {
		ExecutionResult res = null;
		try {
			res = tx.abort();
		} catch (Exception e) {
			res = ExecutionResult.resultFromException(e);
		}
		return res;
	}

}

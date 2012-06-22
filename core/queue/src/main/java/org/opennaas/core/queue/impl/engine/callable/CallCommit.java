package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Callable;

import org.opennaas.core.queue.impl.engine.transaction.ActionsTransaction;
import org.opennaas.core.queue.repository.ExecutionResult;

public class CallCommit implements Callable<ExecutionResult> {

	private ActionsTransaction	tx;

	public CallCommit(ActionsTransaction tx) {
		this.tx = tx;
	}

	@Override
	public ExecutionResult call() throws Exception {
		ExecutionResult res = null;
		try {
			res = tx.commit();
		} catch (Exception e) {
			res = createResultFromException(e);
		}
		return res;
	}

	private ExecutionResult createResultFromException(Exception e) {
		// TODO Auto-generated method stub
		return null;
	}

}

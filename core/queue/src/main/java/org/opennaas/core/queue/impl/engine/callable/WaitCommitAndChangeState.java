package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Future;

import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.state.Error;
import org.opennaas.core.queue.impl.engine.state.Free;
import org.opennaas.core.queue.repository.ExecutionResult;

public class WaitCommitAndChangeState extends WaitFutureAndChangeState {

	public WaitCommitAndChangeState(Future<ExecutionResult> beginFuture, ExecutionResult result, QueueExecutionEngine engine) {
		super(beginFuture, result, engine);
	}

	@Override
	protected void changeState(Object futureResult) {
		if (isOk(futureResult)) {
			executionEngine.setEngineState(new Free());
		} else {
			executionEngine.setEngineState(new Error());
		}
	}

	private boolean isOk(Object futureResult) {
		// TODO Auto-generated method stub
		return false;
	}

}

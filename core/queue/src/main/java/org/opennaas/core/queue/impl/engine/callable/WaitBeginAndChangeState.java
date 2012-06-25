package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Future;

import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.state.BeginError;
import org.opennaas.core.queue.impl.engine.state.BeginOk;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.ExecutionResult.Status;

public class WaitBeginAndChangeState extends WaitFutureAndChangeState {

	private QueueExecutionEngine	executionEngine;

	public WaitBeginAndChangeState(Future<ExecutionResult> beginFuture, ExecutionResult result, QueueExecutionEngine engine) {
		super(beginFuture, result, engine);
	}

	@Override
	protected void changeState(ExecutionResult futureResult) {
		if (isOk(futureResult)) {
			executionEngine.setEngineState(new BeginOk());
		} else {
			executionEngine.setEngineState(new BeginError());
		}
	}

	private boolean isOk(ExecutionResult futureResult) {
		return !(futureResult.getResult().equals(Status.ERROR));
	}

}

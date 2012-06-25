package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Future;

import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.state.Error;
import org.opennaas.core.queue.impl.engine.state.Free;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.ExecutionResult.Status;

public class WaitCommitAndChangeState extends WaitFutureAndChangeState {

	public WaitCommitAndChangeState(Future<ExecutionResult> beginFuture, ExecutionResult result, QueueExecutionEngine engine) {
		super(beginFuture, result, engine);
	}

	@Override
	protected void changeState(ExecutionResult futureResult) {
		if (isOk(futureResult)) {
			executionEngine.setEngineState(new Free());
		} else {
			executionEngine.setEngineState(new Error());
		}
		sendExecutionFinishedEvent();
	}

	private boolean isOk(ExecutionResult futureResult) {
		return !(futureResult.getResult().equals(Status.ERROR));
	}

}

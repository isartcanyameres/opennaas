package org.opennaas.core.queue.impl.engine.callable;

import java.util.concurrent.Future;

import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.state.BeginError;
import org.opennaas.core.queue.impl.engine.state.BeginOk;
import org.opennaas.core.queue.repository.ExecutionResult;

public class WaitBeginAndChangeState extends WaitFutureAndChangeState {

	private QueueExecutionEngine	executionEngine;

	public WaitBeginAndChangeState(Future<ExecutionResult> beginFuture, ExecutionResult result, QueueExecutionEngine engine) {
		super(beginFuture, result, engine);
	}

	@Override
	protected void changeState(Object futureResult) {
		if (isOk(futureResult)) {
			executionEngine.setEngineState(new BeginOk());
		} else {
			executionEngine.setEngineState(new BeginError());
		}
	}

	private boolean isOk(Object result) {
		// TODO Auto-generated method stub
		return false;
	}

}

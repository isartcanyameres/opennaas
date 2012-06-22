package org.opennaas.core.queue.impl.engine.state;

import java.util.List;

import org.opennaas.core.queue.engine.EngineState;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.IAction;

public class Free extends AbstractEngineState {

	@Override
	public ExecutionId submit(List<IAction> actions) throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionId abort() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionResult waitUntilBeginFinishes() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionResult waitUntilAbortFinishes() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionResult waitUntilCommitFinishes() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public EngineState getState() {
		return EngineState.FREE;
	}

}

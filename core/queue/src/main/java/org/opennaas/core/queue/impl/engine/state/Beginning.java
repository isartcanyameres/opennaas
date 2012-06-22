package org.opennaas.core.queue.impl.engine.state;

import org.opennaas.core.queue.engine.EngineState;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;

public class Beginning extends AbstractEngineState {

	@Override
	public ExecutionId begin() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionResult waitUntilCommitFinishes() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public ExecutionResult waitUntilAbortFinishes() throws IllegalStateException {
		throw new IllegalStateException();
	}

	@Override
	public EngineState getState() {
		return EngineState.BEGINNING;
	}

}

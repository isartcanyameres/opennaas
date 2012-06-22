package org.opennaas.core.queue.impl.engine.state;

import java.util.List;

import org.opennaas.core.queue.engine.EngineState;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.IAction;

public interface IEngineState {

	public ExecutionId submit(List<IAction> actions) throws IllegalStateException;

	public ExecutionId begin() throws IllegalStateException;

	public ExecutionId commit() throws IllegalStateException;

	public ExecutionId abort() throws IllegalStateException;

	public ExecutionResult waitUntilBeginFinishes() throws IllegalStateException;

	public ExecutionResult waitUntilCommitFinishes() throws IllegalStateException;

	public ExecutionResult waitUntilAbortFinishes() throws IllegalStateException;

	public EngineState getState();

}

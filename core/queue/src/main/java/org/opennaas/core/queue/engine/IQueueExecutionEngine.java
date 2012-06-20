package org.opennaas.core.queue.engine;

import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;

/**
 * Uses a different thread than the caller to run non blocking transactional operations.
 * 
 * Blocking methods wait for this thread to end before returning.
 * 
 */
public interface IQueueExecutionEngine {

	public ExecutionId begin() throws IllegalStateException;

	public ExecutionId commit() throws IllegalStateException;

	public ExecutionId abort() throws IllegalStateException;

	public ExecutionResult blockingBegin() throws IllegalStateException;

	public ExecutionResult blockingCommit() throws IllegalStateException;

	public ExecutionResult blockingAbort() throws IllegalStateException;

	public EngineState getState();

}

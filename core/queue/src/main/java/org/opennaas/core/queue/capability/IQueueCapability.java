package org.opennaas.core.queue.capability;

import java.util.List;

import org.opennaas.core.queue.QueueState;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.capability.ICapability;

public interface IQueueCapability extends ICapability {

	public List<IAction> getActions();

	public IAction removeAction(IAction action) throws IllegalStateException, Exception;

	public void clear() throws IllegalStateException;

	public ExecutionId begin() throws IllegalStateException;

	public ExecutionId commit() throws IllegalStateException;

	public ExecutionId abort() throws IllegalStateException;

	public ExecutionResult blockingBegin() throws IllegalStateException;

	public ExecutionResult blockingCommit() throws IllegalStateException;

	public ExecutionResult blockingAbort() throws IllegalStateException;

	public QueueState getQueueState() throws IllegalStateException;

	public ExecutionResult execute();

}

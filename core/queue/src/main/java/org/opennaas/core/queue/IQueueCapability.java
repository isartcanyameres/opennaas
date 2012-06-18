package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.capability.ICapability;

public interface IQueueCapability extends ICapability {

	public ExecutionId submit() throws SubmitionException;

	public List<IAction> listActions();

	public IAction removeAction(IAction action);

	public void lock() throws AlreadyLockedException;

	public void unlock() throws NotLockedException;

}

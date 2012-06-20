package org.opennaas.core.queue.capability;

import org.opennaas.core.resources.action.IAction;

public interface IExtendedQueueCapability extends IQueueCapability {

	public void add(IAction action);

}

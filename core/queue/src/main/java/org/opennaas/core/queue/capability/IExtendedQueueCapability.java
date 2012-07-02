package org.opennaas.core.queue.capability;

import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.capability.CapabilityException;

public interface IExtendedQueueCapability extends IQueueCapability {

	public void add(IAction action) throws CapabilityException;

}

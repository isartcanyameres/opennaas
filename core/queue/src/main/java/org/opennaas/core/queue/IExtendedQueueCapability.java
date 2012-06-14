package org.opennaas.core.queue;

import org.opennaas.core.resources.action.IAction;

public interface IExtendedQueueCapability extends IQueueCapability {

	public void add(QueueId qid, IAction action);

}

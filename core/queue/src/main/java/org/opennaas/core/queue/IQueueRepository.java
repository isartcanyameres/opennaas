package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.resources.action.IAction;

public interface IQueueRepository {

	public QueueId createQueue();

	public List<IAction> getQueue(QueueId qid);

	public void destroyQueue(QueueId qid);

	public List<QueueId> listQueues();

}

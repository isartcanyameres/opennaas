package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.queue.transaction.TransactionId;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.capability.ICapability;

public interface IQueueCapability extends ICapability {

	public QueueId createQueue();

	public TransactionId submit(QueueId qid) throws SubmitionException;

	public void destroyQueue(QueueId qid);

	public List<IAction> listActions(QueueId qid);

	public IAction removeAction(QueueId qid, IAction action);

}

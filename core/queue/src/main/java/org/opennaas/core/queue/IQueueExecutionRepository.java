package org.opennaas.core.queue;

import java.util.List;

import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.TransactionId;

public interface IQueueExecutionRepository {

	public ITransaction get(TransactionId qExecId);

	public void save(ITransaction qExec);

	public List<TransactionId> listIds();

}

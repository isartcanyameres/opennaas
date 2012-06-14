package org.opennaas.core.queue.transaction;

import java.util.List;

import org.opennaas.core.resources.action.IAction;

public interface ITransaction {

	public TransactionId getId();

	public List<IAction> getTransactionOperations();

	public void begin();

	public void commit();

	public void abort();

	public ITransactionContext getContext();

}

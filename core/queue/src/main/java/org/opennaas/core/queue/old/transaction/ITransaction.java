package org.opennaas.core.queue.old.transaction;

public interface ITransaction {

	public void begin();

	public void commit();

	public void abort();

}

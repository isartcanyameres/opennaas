package org.opennaas.core.queue.old.transaction;

public interface ITransactionWrapper {

	public enum TxStatus {
		CREATED,
		BEGGINNING,
		BEGIN_FINISHED,
		WAITING,
		COMMITING,
		ABORTING,
		FINISHED;
	};

	public enum TxResult {
		UNKNOWN,
		OK,
		COMMITED,
		ABORTED,
		ERROR;
	};

	public void begin();

	public void commit();

	public void abort();

	public void waitUntilFinished();

	public TxStatus getStatus();

	public TxResult getResult();

	public TxResult getBeginResult();

}

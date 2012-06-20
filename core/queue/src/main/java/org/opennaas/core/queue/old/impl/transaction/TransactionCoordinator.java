package org.opennaas.core.queue.old.impl.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class TransactionCoordinator implements ITransactionCoordinator {

	private List<ITransactionWrapper>			registeredTxs;
	ExecutorService								executor;

	private ListenableFuture<List<TxResult>>	beginResult;

	public TransactionCoordinator() {
		registeredTxs = new ArrayList<ITransactionWrapper>();
	}

	@Override
	public void register(ITransactionWrapper tx) {
		registeredTxs.add(tx);
	}

	@Override
	public List<ITransactionWrapper> getRegisteredTransactions() {
		return registeredTxs;
	}

	@Override
	public ListenableFuture<List<TxResult>> abort() {
		// TODO do not abort if commit is taking place
		return doAbort();
	}

	@Override
	public ListenableFuture<List<TxResult>> begin() {
		beginResult = doBegin();
		return beginResult;
	}

	@Override
	public ListenableFuture<List<TxResult>> commit() {
		if (shouldCommit()) {
			return doCommit();
		}
		return abort();
	}

	private boolean shouldCommit() {
		// TODO what if begin has not been called??
		try {
			List<TxResult> beginResults = beginResult.get(); // wait for tx to finish begin
			if (allOk(beginResults)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean allOk(List<TxResult> results) {
		for (TxResult result : results) {
			if (!result.equals(TxResult.OK))
				return false;
		}
		return true;
	}

	private ListenableFuture<List<TxResult>> doAbort() {
		List<ListenableFuture<TxResult>> abortResults = new ArrayList<ListenableFuture<TxResult>>();
		for (ITransactionWrapper tx : getRegisteredTransactions()) {
			abortResults.add(executor.submit(tx.abort()));
		}
		return Futures.allAsList(abortResults);
	}

	private ListenableFuture<List<TxResult>> doBegin() {
		executor = Executors.newFixedThreadPool(getRegisteredTransactions().size());
		List<ListenableFuture<TxResult>> beginResults = new ArrayList<ListenableFuture<TxResult>>();

		for (ITransactionWrapper tx : getRegisteredTransactions()) {
			beginResults.add(executor.submit(tx.begin()));
		}

		return Futures.allAsList(beginResults);
	}

	private ListenableFuture<List<TxResult>> doCommit() {
		List<ListenableFuture<TxResult>> commitResults = new ArrayList<ListenableFuture<TxResult>>();
		for (ITransactionWrapper tx : getRegisteredTransactions()) {
			commitResults.add(executor.submit(tx.commit()));
		}
		return Futures.allAsList(commitResults);
	}
}

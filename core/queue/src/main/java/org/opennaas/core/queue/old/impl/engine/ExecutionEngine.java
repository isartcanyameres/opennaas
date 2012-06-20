package org.opennaas.core.queue.old.impl.engine;

import java.util.List;
import java.util.concurrent.Executors;

import org.opennaas.core.queue.impl.engine.logic.TransactionLogic;
import org.opennaas.core.queue.impl.engine.logic.WaitUntilFinishedLogic;
import org.opennaas.core.queue.impl.transaction.TransactionCoordinator;
import org.opennaas.core.queue.old.ICompositeExecutionEngine;
import org.opennaas.core.queue.old.IExecutionEngine;
import org.opennaas.core.queue.transaction.ITransactionCoordinator;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.queue.transaction.ITransactionWrapper.TxResult;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class ExecutionEngine implements IExecutionEngine, ICompositeExecutionEngine {

	private ListeningExecutorService	executor;
	private ITransactionCoordinator		coordinator;

	ListenableFuture<TxResult>			result	= null;

	@Override
	public ListenableFuture<TxResult> submit(ITransactionWrapper tx) {

		TransactionLogic logic = new TransactionLogic(tx);
		executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
		result = executor.submit(logic);
		executor.shutdown(); // do not accept more submitions
		return result;
	}

	@Override
	public ListenableFuture<TxResult> submitCoordinated(ITransactionWrapper tx) {

		coordinator = new TransactionCoordinator();
		coordinator.register(tx);

		WaitUntilFinishedLogic logic = new WaitUntilFinishedLogic(tx);

		executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(3)); // 3 thread at max: 1 per submit, 1 per begin/commit, 1 per
																						// abort
		result = executor.submit(logic);
		return result;
	}

	@Override
	public ListenableFuture<TxResult> begin() {
		Function<List<TxResult>, TxResult> isAllOkFunction =
				new Function<List<TxResult>, TxResult>() {
					@Override
					public TxResult apply(List<TxResult> input) {
						boolean ok = Iterables.all(input, Predicates.equalTo(TxResult.OK));
						if (ok) {
							return TxResult.OK;
						} else {
							return TxResult.ERROR;
						}
					}
				};

		return Futures.transform(coordinator.begin(), isAllOkFunction);
	}

	@Override
	public ListenableFuture<TxResult> commit() {
		return coordinator.commit();
	}

	@Override
	public ListenableFuture<TxResult> abort() {
		return coordinator.abort();
	}

	private void resetExecutor() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}
}

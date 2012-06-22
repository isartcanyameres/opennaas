package org.opennaas.core.queue.impl.engine.state;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.opennaas.core.queue.impl.engine.QueueExecution;
import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.callable.CallAbort;
import org.opennaas.core.queue.impl.engine.callable.CallBegin;
import org.opennaas.core.queue.impl.engine.callable.CallCommit;
import org.opennaas.core.queue.impl.engine.callable.WaitAbortAndChangeState;
import org.opennaas.core.queue.impl.engine.callable.WaitBeginAndChangeState;
import org.opennaas.core.queue.impl.engine.callable.WaitCommitAndChangeState;
import org.opennaas.core.queue.impl.engine.transaction.ActionsTransaction;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.IAction;

import com.google.common.util.concurrent.ListenableFuture;

public abstract class AbstractEngineState implements IEngineState {

	QueueExecutionEngine			engine;

	private Future<ExecutionResult>	beginDoneFuture;
	private Future<ExecutionResult>	commitDoneFuture;
	private Future<ExecutionResult>	abortDoneFuture;

	public ExecutionId submit(List<IAction> actions) throws IllegalStateException {
		// TODO check state

		ActionsTransaction tx = new ActionsTransaction();
		tx.getTransactionOperations().addAll(actions);
		// TODO set psm
		// TODO set prepare, validate, commit and abort actions

		QueueExecution qExec = new QueueExecution();
		// TODO ASSIGN ID to qExec
		qExec.getActions().addAll(actions);

		engine.setTx(tx);
		engine.setqExec(qExec);
		engine.getQueueExecutionRepository().put(qExec);

		return qExec.getId();
	}

	@Override
	public ExecutionId begin() throws IllegalStateException {

		// TODO check state

		ExecutionResult beginResult = new ExecutionResult();
		beginResult.setStartTime(new Date());
		engine.getQExec().setBeginResult(beginResult);

		// open a new thread calling tx.begin()
		ListenableFuture<ExecutionResult> beginFuture = engine.getExecutor().submit(new CallBegin(engine.getTx()));
		// open a new thread waiting tx.begin to finish and:
		// 1) save begin result in qExec
		// 2) change state depending on begin result
		beginDoneFuture = engine.getExecutor().submit(new WaitBeginAndChangeState(beginFuture, beginResult, engine));

		return engine.getQExec().getId();
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {

		// TODO check state

		ExecutionResult commitResult = new ExecutionResult();
		commitResult.setStartTime(new Date());
		engine.getQExec().setCommitResult(commitResult);

		// open a new thread calling tx.commit()
		ListenableFuture<ExecutionResult> commitFuture = engine.getExecutor().submit(new CallCommit(engine.getTx()));
		// open a new thread waiting tx.commit to finish and changing state
		commitDoneFuture = engine.getExecutor().submit(new WaitCommitAndChangeState(commitFuture, commitResult, engine));

		return engine.getQExec().getId();
	}

	@Override
	public ExecutionId abort() throws IllegalStateException {

		// TODO check state

		ExecutionResult abortResult = new ExecutionResult();
		abortResult.setStartTime(new Date());
		engine.getQExec().setAbortResult(abortResult);

		// open a new thread calling tx.abort()
		ListenableFuture<ExecutionResult> abortFuture = engine.getExecutor().submit(new CallAbort(engine.getTx()));
		// open a new thread waiting tx.abort() to finish and changing state
		abortDoneFuture = engine.getExecutor().submit(new WaitAbortAndChangeState(abortFuture, abortResult, engine));

		return engine.getQExec().getId();
	}

	public ExecutionResult waitUntilBeginFinishes() throws IllegalStateException {
		// TODO check state
		try {
			return waitUntilFutureFinishes(beginDoneFuture);
		} catch (InterruptedException e) {
			// stop waiting but let future keep on with its computation
			return createTryLaterResult(engine.getQExec().getBeginResult());
		}
	}

	public ExecutionResult waitUntilCommitFinishes() throws IllegalStateException {
		// TODO check state
		try {
			return waitUntilFutureFinishes(commitDoneFuture);
		} catch (InterruptedException e) {
			// stop waiting but let future keep on with its computation
			return createTryLaterResult(engine.getQExec().getCommitResult());
		}
	}

	public ExecutionResult waitUntilAbortFinishes() throws IllegalStateException {
		// TODO check state
		try {
			return waitUntilFutureFinishes(abortDoneFuture);
		} catch (InterruptedException e) {
			// stop waiting but let future keep on with its computation
			return createTryLaterResult(engine.getQExec().getAbortResult());
		}
	}

	protected ExecutionResult waitUntilFutureFinishes(Future<ExecutionResult> future) throws InterruptedException {
		try {
			return future.get();
		} catch (ExecutionException e) {
			// never happens
			// WaitFutureAndChangeState does not really throw ExecutionExceptions :)
			return null;
		}
	}

	protected ExecutionResult createTryLaterResult(ExecutionResult baseResult) {
		ExecutionResult result = new ExecutionResult();
		result.setStartTime(baseResult.getStartTime());
		result.setExecutedActions(baseResult.getExecutedActions());
		// TODO use real unknownTryLaterResult
		Object unknownTryLaterResult = new Object();
		result.setResult(unknownTryLaterResult);
		return result;
	}

}

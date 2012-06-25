package org.opennaas.core.queue.impl.engine;

import java.util.List;

import org.opennaas.core.events.IEventManager;
import org.opennaas.core.queue.engine.EngineState;
import org.opennaas.core.queue.engine.IQueueExecutionEngine;
import org.opennaas.core.queue.impl.engine.state.Free;
import org.opennaas.core.queue.impl.engine.state.IEngineState;
import org.opennaas.core.queue.impl.engine.transaction.ActionsTransaction;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.IQueueExecutionRepository;
import org.opennaas.core.resources.action.IAction;

import com.google.common.util.concurrent.ListeningExecutorService;

public class QueueExecutionEngine implements IQueueExecutionEngine {

	private IEngineState				engineState;

	private IQueueExecutionRepository	execRepo;

	private ListeningExecutorService	executor;

	private QueueExecution				qExec;
	private ActionsTransaction			tx;

	private IEventManager				eventManager;

	public QueueExecutionEngine() {
		setEngineState(new Free());
	}

	@Override
	public ExecutionId submit(List<IAction> actions) throws IllegalStateException {
		return engineState.submit(actions);
	}

	@Override
	public ExecutionId begin() throws IllegalStateException {
		return engineState.begin();
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {
		return engineState.commit();
	}

	@Override
	public ExecutionId abort() throws IllegalStateException {
		return engineState.abort();
	}

	@Override
	public ExecutionResult waitUntilBeginFinishes() throws IllegalStateException {
		return engineState.waitUntilBeginFinishes();
	}

	@Override
	public ExecutionResult waitUntilCommitFinishes() throws IllegalStateException {
		return engineState.waitUntilCommitFinishes();
	}

	@Override
	public ExecutionResult waitUntilAbortFinishes() throws IllegalStateException {
		return engineState.waitUntilAbortFinishes();
	}

	@Override
	public ExecutionResult blockingBegin() throws IllegalStateException {
		begin();
		return waitUntilBeginFinishes();
	}

	@Override
	public ExecutionResult blockingCommit() throws IllegalStateException {
		commit();
		return waitUntilCommitFinishes();
	}

	@Override
	public ExecutionResult blockingAbort() throws IllegalStateException {
		abort();
		return waitUntilAbortFinishes();
	}

	@Override
	public EngineState getState() {
		return getEngineState().getState();
	}

	public IEngineState getEngineState() {
		return engineState;
	}

	public void setEngineState(IEngineState newState) {
		this.engineState = newState;
	}

	public IQueueExecutionRepository getQueueExecutionRepository() {
		return execRepo;
	}

	public void setQueueExecutionRepository(IQueueExecutionRepository execRepo) {
		this.execRepo = execRepo;
	}

	public QueueExecution getQExec() {
		return qExec;
	}

	public QueueExecution getqExec() {
		return qExec;
	}

	public void setqExec(QueueExecution qExec) {
		this.qExec = qExec;
	}

	public ActionsTransaction getTx() {
		return tx;
	}

	public void setTx(ActionsTransaction tx) {
		this.tx = tx;
	}

	public ListeningExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ListeningExecutorService executor) {
		this.executor = executor;
	}

	public IEventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(IEventManager eventManager) {
		this.eventManager = eventManager;
	}
}

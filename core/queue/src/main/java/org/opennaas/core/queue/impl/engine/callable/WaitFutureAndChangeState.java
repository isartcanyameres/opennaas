package org.opennaas.core.queue.impl.engine.callable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.opennaas.core.events.IEventManager;
import org.opennaas.core.queue.events.QueueExecutionFinishedEvent;
import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.ExecutionResult.Status;
import org.osgi.service.event.Event;

public abstract class WaitFutureAndChangeState implements Callable<ExecutionResult> {

	protected QueueExecutionEngine		executionEngine;
	protected ExecutionResult			result;
	protected Future<ExecutionResult>	future;

	public WaitFutureAndChangeState(Future<ExecutionResult> future, ExecutionResult result, QueueExecutionEngine engine) {
		this.future = future;
		this.result = result;
		this.executionEngine = engine;
	}

	@Override
	public ExecutionResult call() throws Exception {
		try {

			ExecutionResult futureResult = future.get();
			result.setExecutedActions(futureResult.getExecutedActions());
			result.setExecutedActionsResults(futureResult.getExecutedActionsResults());
			result.setResult(futureResult.getResult());

		} catch (InterruptedException e) {
			// TODO should we cancel future???
			// if thats a begin yes, otherwise no
			// future.cancel(true);
			result.setResult(Status.ERROR);
			result.setInformation(e.getLocalizedMessage());
		} catch (Exception e) {
			result.setResult(Status.ERROR);
			result.setInformation(e.getLocalizedMessage());
		} finally {
			result.setEndTime(new Date());
			changeState(result);
		}

		return result;
	}

	protected void sendExecutionFinishedEvent() {

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("ExecutionId", executionEngine.getQExec().getId().toString());

		Event execFinishedEvent = new QueueExecutionFinishedEvent(properties);
		getEventManager().publishEvent(execFinishedEvent);
	}

	protected IEventManager getEventManager() {
		return executionEngine.getEventManager();
	}

	protected abstract void changeState(ExecutionResult futureResult);

}

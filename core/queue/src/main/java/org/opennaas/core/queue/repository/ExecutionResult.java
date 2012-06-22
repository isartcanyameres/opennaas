package org.opennaas.core.queue.repository;

import java.util.Date;
import java.util.List;

import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.action.IAction;

public class ExecutionResult {

	private Date					startTime;
	private Date					endTime;

	private List<IAction>			executedActions;
	private List<ActionResponse>	executedActionsResults;

	private Object					result;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<IAction> getExecutedActions() {
		return executedActions;
	}

	public void setExecutedActions(List<IAction> executedActions) {
		this.executedActions = executedActions;
	}

	public List<ActionResponse> getExecutedActionsResults() {
		return executedActionsResults;
	}

	public void setExecutedActionsResults(List<ActionResponse> executedActionsResults) {
		this.executedActionsResults = executedActionsResults;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}

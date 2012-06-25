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

	private Status					result;

	private String					information;

	public enum Status {
		PENDING, SKIPPED, OK, ERROR;
	}

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

	public Status getResult() {
		return result;
	}

	public void setResult(Status result) {
		this.result = result;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public static ExecutionResult resultFromException(Exception e) {
		ExecutionResult result = new ExecutionResult();
		result.setEndTime(new Date());
		result.setResult(Status.ERROR);
		result.setInformation(e.getLocalizedMessage());
		return result;
	}
}

package org.opennaas.core.queue.impl.engine;

import java.util.List;

import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.queue.repository.IQueueExecution;
import org.opennaas.core.resources.action.IAction;

public class QueueExecution implements IQueueExecution {

	private List<IAction>	userActions;
	private ExecutionResult	beginResult;
	private ExecutionResult	commitResult;
	private ExecutionResult	abortResult;

	@Override
	public ExecutionId getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAction> getActions() {
		return userActions;
	}

	@Override
	public ExecutionResult getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionResult getBeginResult() {
		return beginResult;
	}

	@Override
	public ExecutionResult getCommitResult() {
		return commitResult;
	}

	@Override
	public ExecutionResult getAbortResult() {
		return abortResult;
	}

	public void setActions(List<IAction> userActions) {
		this.userActions = userActions;
	}

	public void setBeginResult(ExecutionResult beginResult) {
		this.beginResult = beginResult;
	}

	public void setCommitResult(ExecutionResult commitResult) {
		this.commitResult = commitResult;
	}

	public void setAbortResult(ExecutionResult abortResult) {
		this.abortResult = abortResult;
	}

}

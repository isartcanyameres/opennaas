package org.opennaas.core.queue.impl.engine.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.protocol.IProtocolSessionManager;

/**
 * A Transaction whose operations are IActions.
 * 
 * @author isart
 * 
 */
public class ActionsTransaction {

	private List<IAction>			transactionOperations;
	private IProtocolSessionManager	psm;

	private IAction					prepareAction;
	private IAction					validateAction;
	private IAction					confirmAction;
	private IAction					abortAction;

	public ActionsTransaction() {
		transactionOperations = new ArrayList<IAction>();
	}

	public List<IAction> getTransactionOperations() {
		return transactionOperations;
	}

	public IProtocolSessionManager getProtocolSessionManager() {
		return psm;
	}

	public void setProtocolSessionManager(IProtocolSessionManager psm) {
		this.psm = psm;
	}

	public IAction getPrepareAction() {
		return prepareAction;
	}

	public IAction getConfirmAction() {
		return confirmAction;
	}

	public IAction getValidateAction() {
		return validateAction;
	}

	public IAction getAbortAction() {
		return abortAction;
	}

	public void setPrepareAction(IAction prepareAction) {
		this.prepareAction = prepareAction;
	}

	public void setValidateAction(IAction validateAction) {
		this.validateAction = validateAction;
	}

	public void setConfirmAction(IAction confirmAction) {
		this.confirmAction = confirmAction;
	}

	public void setAbortAction(IAction abortAction) {
		this.abortAction = abortAction;
	}

	public ExecutionResult begin() {
		ExecutionResult result = new ExecutionResult();
		result.setStartTime(new Date());

		result = runPrepare(result);
		if (isOk(result)) {
			result = runTransactionOperations(result); // Actions
			if (isOk(result)) {
				result = runValidate(result);
			}
		}

		result.setEndTime(new Date());

		return result;
	}

	public ExecutionResult commit() {
		ExecutionResult result = new ExecutionResult();
		result.setStartTime(new Date());

		result = runCommit(result);

		result.setEndTime(new Date());
		return result;
	}

	public ExecutionResult abort() {
		ExecutionResult result = new ExecutionResult();
		result.setStartTime(new Date());

		result = runAbort(result);

		result.setEndTime(new Date());
		return result;
	}

	private ExecutionResult runTransactionOperations(ExecutionResult result) {
		boolean failure = false;
		for (int i = 0; i < getTransactionOperations().size() && !failure; i++) {
			result = runAction(getTransactionOperations().get(i), result);
			if (!isOk(result)) {
				failure = true;
			}
		}
		return result;
	}

	private ExecutionResult runPrepare(ExecutionResult result) {
		return runAction(getPrepareAction(), result);
	}

	private ExecutionResult runValidate(ExecutionResult result) {
		return runAction(getValidateAction(), result);
	}

	private ExecutionResult runCommit(ExecutionResult result) {
		return runAction(getConfirmAction(), result);
	}

	private ExecutionResult runAbort(ExecutionResult result) {
		return runAction(getAbortAction(), result);
	}

	private ExecutionResult runAction(IAction action, ExecutionResult wrappingResult) {
		ActionResponse response = runAction(action);
		return updateResultWithActionResponse(wrappingResult, action, response);
	}

	private ExecutionResult updateResultWithActionResponse(ExecutionResult wrappingResult, IAction action, ActionResponse response) {
		wrappingResult.getExecutedActions().add(action);
		wrappingResult.getExecutedActionsResults().add(response);
		// TODO CHECK response and update result state

		return wrappingResult;
	}

	private ActionResponse runAction(IAction action) {
		ActionResponse response = null;
		try {
			response = action.execute(psm);
		} catch (ActionException e) {
			response = createActionResultFromException(action, e);
		} catch (Exception e) {
			response = createActionResultFromException(action, e);
		}
		return response;
	}

	private ActionResponse createActionResultFromException(IAction failedAction, Exception fail) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isOk(ExecutionResult result) {
		// TODO Auto-generated method stub
		return false;
	}

}

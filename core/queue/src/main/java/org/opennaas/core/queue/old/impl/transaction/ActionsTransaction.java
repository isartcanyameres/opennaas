package org.opennaas.core.queue.old.impl.transaction;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.ITransactionContext;
import org.opennaas.core.queue.transaction.ITransactionWrapper;
import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.protocol.IProtocolSessionManager;

/**
 * A Transaction whose operations are IActions.
 * 
 * @author isart
 * 
 */
public class ActionsTransaction implements ITransactionWrapper, ITransaction {

	private ITransactionContext		ctx;
	private TxResult				result;
	private TxResult				beginResult;
	private TxStatus				status;
	private IActionSet				qActionSet;

	private IProtocolSessionManager	psm;

	public List<IAction> getTransactionOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void begin() {
		status = TxStatus.BEGGINNING;
		TxResult prepareResult = runPrepare();
		// TODO check prepare result and abort if error
		List<ActionResponse> executionResults = runTransactionOperations(); // Actions
		// TODO check executionResults and abort if error
		beginResult = runValidate();
		status = TxStatus.BEGIN_FINISHED;
	}

	@Override
	public void commit() {
		status = TxStatus.COMMITING;
		result = runCommit();
		status = TxStatus.FINISHED;
	}

	@Override
	public void abort() {
		status = TxStatus.ABORTING;
		// TODO Auto-generated method stub
	}

	@Override
	public void waitUntilFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public TxStatus getStatus() {
		return status;
	}

	@Override
	public TxResult getResult() {
		return result;
	}

	private List<ActionResponse> runTransactionOperations() {
		ArrayList<ActionResponse> results = new ArrayList<ActionResponse>(getTransactionOperations().size());
		boolean failure = false;
		for (int i = 0; i < getTransactionOperations().size() && !failure; i++) {
			try {
				// getTransactionOperations().get(i).setTxContext(getContext());
				results.add(getTransactionOperations().get(i).execute(psm));
			} catch (Exception e) {
				results.add(i, createResultFromException(getTransactionOperations().get(i), e));
				failure = true;
			}
		}
		return results;
	}

	private ActionResponse createResultFromException(IAction failedAction, Exception fail) {
		// TODO Auto-generated method stub
		return null;
	}

	private TxResult runPrepare() {
		try {
			IAction prepareAction = getPrepareAction();
			ActionResponse response = prepareAction.execute(psm);
			return computeTxResultFromActionResponse(response);
		} catch (Exception e) {
			return TxResult.ERROR;
		}
	}

	private TxResult runValidate() {
		try {
			IAction validateAction = getValidateAction();
			ActionResponse response = validateAction.execute(psm);
			return computeTxResultFromActionResponse(response);
		} catch (Exception e) {
			return TxResult.ERROR;
		}
	}

	private TxResult runCommit() {
		try {
			IAction confirmAction = getConfirmAction();
			ActionResponse response = confirmAction.execute(psm);
			return computeTxResultFromActionResponse(response);
		} catch (Exception e) {
			return TxResult.ERROR;
		}
	}

	private TxResult computeTxResultFromActionResponse(ActionResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	private IAction getPrepareAction() {
		// TODO Auto-generated method stub
		return null;
	}

	private IAction getConfirmAction() {
		// TODO Auto-generated method stub
		return null;
	}

	private IAction getValidateAction() {
		// TODO Auto-generated method stub
		return null;
	}

	public ITransactionContext getContext() {
		return ctx;
	}

	public IActionSet getqActionSet() {
		return qActionSet;
	}

	public void setqActionSet(IActionSet qActionSet) {
		this.qActionSet = qActionSet;
	}

	@Override
	public TxResult getBeginResult() {
		// TODO Auto-generated method stub
		return null;
	}

}

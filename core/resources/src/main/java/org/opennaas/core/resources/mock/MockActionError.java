package org.opennaas.core.resources.mock;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.protocol.IProtocolSessionManager;

public class MockActionError extends Action {

	public MockActionError() {
		actionID = "MockActionError";
	}

	@Override
	public ActionResponse execute(IProtocolSessionManager protocolSessionManager)
			throws ActionException {

		return ActionResponse.errorResponse("MockActionError");
	}

	@Override
	public boolean checkParams(Object params) throws ActionException {
		return true;
	}

}

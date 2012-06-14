package org.opennaas.core.queue;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.protocol.IProtocolSessionManager;

public class WaitingAction extends Action {

	private Object	lock;

	@Override
	public ActionResponse execute(IProtocolSessionManager protocolSessionManager) throws ActionException {
		startWaiting();
		return ActionResponse.okResponse(actionID);
	}

	@Override
	public boolean checkParams(Object params) throws ActionException {
		return true;
	}

	public synchronized void stopWaiting() {
		lock.notifyAll();
	}

	public synchronized void startWaiting() {
		try {
			lock.wait();
		} catch (InterruptedException e) {
			// Just stop waiting
		}
	}

}

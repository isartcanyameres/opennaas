package org.opennaas.extensions.roadm.wonesys.actionsets.actions;

import java.util.HashMap;
import java.util.Properties;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.extensions.roadm.capability.connections.actionset.IConnectionsCapabilityActionFactory;
import org.opennaas.extensions.roadm.wonesys.actionsets.ActionConstants;
import org.opennaas.extensions.router.model.opticalSwitch.FiberConnection;

/**
 * This factory registers itself with OSGI
 */
public class ConnectionsCapabilityActionFactory implements IConnectionsCapabilityActionFactory {

	// TODO: actionParams pending to initialize.

	HashMap<String, Class<? extends Action>>	actions			= new HashMap<String, Class<? extends Action>>();
	HashMap<String, Properties>					actionParams	= new HashMap<String, Properties>();

	public ConnectionsCapabilityActionFactory() {
		actions.put(ActionConstants.MAKECONNECTION, MakeConnectionAction.class);
		actions.put(ActionConstants.REMOVECONNECTION, RemoveConnectionAction.class);
		actions.put(ActionConstants.REFRESHCONNECTIONS, RefreshModelConnectionsAction.class);

	}

	/**
	 * IConnectionsCapabActionFactory methods
	 * 
	 */
	@Override
	public IAction createMakeConnectionAction(FiberConnection p1) throws ActionException {

		IAction action = obtainAction(ActionConstants.MAKECONNECTION);
		action.setParams(p1);
		return action;
	}

	@Override
	public IAction createRemoveConectionAction(FiberConnection p1) throws ActionException {

		IAction action = obtainAction(ActionConstants.REMOVECONNECTION);
		action.setParams(p1);
		return action;
	}

	@Override
	public IAction createRefreshModelConnectionsAction() throws ActionException {

		return obtainAction(ActionConstants.REFRESHCONNECTIONS);
	}

	/**
	 * same behaviour as ActionSet at the moment
	 */
	public Action obtainAction(String actionId) throws ActionException {
		Class<? extends Action> actionClass = actions.get(actionId);
		Action action = null;
		if (actionClass != null) {
			try {
				action = actionClass.newInstance();

				if (action != null) {
					if (actionParams.get(actionId) != null) {
						action.setBehaviorParams(actionParams.get(actionId));
					}
				}
			} catch (InstantiationException e) {
				// TODO write ActionException message actionID
				throw new ActionException("", e);
			} catch (IllegalAccessException e) {
				// TODO write ActionException message actionID
				throw new ActionException("", e);
			}
		}
		return action;
	}
}

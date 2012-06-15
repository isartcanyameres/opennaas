package org.opennaas.core.resources.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@SuppressWarnings({})
public abstract class ActionSet implements IActionSet {
	public String								actionsetId		= null;

	HashMap<IActionId, Class<? extends Action>>	actions			= new HashMap<IActionId, Class<? extends Action>>();
	HashMap<IActionId, Properties>				actionParams	= new HashMap<IActionId, Properties>();

	protected List<IActionId>					refreshActions	= new ArrayList<IActionId>();
	protected Object							refreshParam	= null;

	public Class<? extends Action> putAction(IActionId key, Class<? extends Action> value) {
		return actions.put(key, value);
	}

	public Properties putActionParams(IActionId key, Properties value) {
		return actionParams.put(key, value);
	}

	public Class<? extends Action> getAction(IActionId actionId) {
		return actions.get(actionId);
	}

	public Properties getActionParams(IActionId actionId) {
		return actionParams.get(actionId);
	}

	public Action obtainAction(IActionId actionId) throws ActionException {
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

	// public List<String> getActionNames() {
	// ArrayList<String> names = new ArrayList<String>();
	// names.addAll(actions.keySet());
	// return names;
	// }

	public String getActionSetId() {
		return actionsetId;
	}

	public void setActionSetId(String actionSetId) {
		this.actionsetId = actionSetId;
	}

	public List<IActionId> getRefreshActionName() {
		return refreshActions;
	}

}

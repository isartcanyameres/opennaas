package org.opennaas.core.resources.action;

import java.util.List;

public interface IActionSet {

	public String getActionSetId();

	public void setActionSetId(String actionSetId);

	public IActionSetDefinition getDefinition();

	/**
	 * Returns the name of the action that is called to start up capabilities using actions in this actionset. This action should update the model
	 * allowing all actions in this actionSet to be operative. An action with this signature should be available through
	 * obtainAction(getRefreshActionName()), unless getStartUpRefreshActionName() returns null
	 */
	public List<IActionId> getRefreshActionName();

	/**
	 * 
	 * @param actionId
	 * @return Action with given actionId present in this actionSet, or null if there is no action with given Id
	 * @throws ActionException
	 *             if there was a problem instantiating the action
	 */
	public Action obtainAction(IActionId actionId) throws ActionException;
}

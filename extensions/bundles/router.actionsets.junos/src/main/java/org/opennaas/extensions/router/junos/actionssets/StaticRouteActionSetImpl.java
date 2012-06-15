/**
 * 
 */
package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.extensions.router.capability.staticroute.StaticRouteActionSet;
import org.opennaas.extensions.router.junos.actionssets.actions.staticroute.CreateStaticRouteAction;

/**
 * @author Jordi
 */
public class StaticRouteActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new StaticRouteActionSet();

	public StaticRouteActionSetImpl() {
		super.setActionSetId("staticRouteActionSet");

		this.putAction(StaticRouteActionSet.ActionId.STATIC_ROUTE_CREATE, CreateStaticRouteAction.class);
	}

	@Override
	public IActionSetDefinition getDefinition() {
		return definition;
	}

	@Override
	public Action obtainAction(IActionId actionId) throws ActionException {
		Action action = super.obtainAction(actionId);
		action.setActionID(actionId);
		return action;
	}
}

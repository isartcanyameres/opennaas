package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.extensions.router.capability.gretunnel.GRETunnelActionSet;
import org.opennaas.extensions.router.junos.actionssets.actions.GetConfigurationAction;
import org.opennaas.extensions.router.junos.actionssets.actions.gretunnel.CreateTunnelAction;
import org.opennaas.extensions.router.junos.actionssets.actions.gretunnel.DeleteTunnelAction;

public class GRETunnelActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new GRETunnelActionSet();

	public GRETunnelActionSetImpl() {
		super.setActionSetId("gretunnelActionSet");

		this.putAction(GRETunnelActionSet.ActionId.CREATETUNNEL, CreateTunnelAction.class);
		this.putAction(GRETunnelActionSet.ActionId.DELETETUNNEL, DeleteTunnelAction.class);
		// this.putAction(GRETunnelActionSet.ActionId.GETTUNNELCONFIG, GetTunnelConfigurationAction.class);
		this.putAction(GRETunnelActionSet.ActionId.REFRESH, GetConfigurationAction.class);

		/* add refresh actions */
		this.refreshActions.add(GRETunnelActionSet.ActionId.REFRESH);

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
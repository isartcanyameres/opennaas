package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.extensions.router.capability.ip.IPActionSet;
import org.opennaas.extensions.router.junos.actionssets.actions.GetConfigurationAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ipv4.SetIPv4Action;
import org.opennaas.extensions.router.junos.actionssets.actions.ipv4.SetInterfaceDescriptionAction;

public class IPActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new IPActionSet();

	public IPActionSetImpl() {
		super.setActionSetId("ipActionSet");
		this.putAction(IPActionSet.ActionId.SET_IPv4, SetIPv4Action.class);
		this.putAction(IPActionSet.ActionId.SET_INTERFACE_DESCRIPTION, SetInterfaceDescriptionAction.class);
		this.putAction(IPActionSet.ActionId.REFRESH, GetConfigurationAction.class);

		/* add refresh actions */
		this.refreshActions.add(IPActionSet.ActionId.REFRESH);
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

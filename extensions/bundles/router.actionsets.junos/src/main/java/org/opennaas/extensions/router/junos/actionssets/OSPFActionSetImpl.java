package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.extensions.router.capability.ospf.OSPFActionSet;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.AddOSPFInterfaceInAreaAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.ClearOSPFAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.ConfigureOSPFAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.ConfigureOSPFAreaAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.ConfigureOSPFInterfaceStatusAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.ConfigureOSPFStatusAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.GetOSPFConfigAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.RemoveOSPFAreaAction;
import org.opennaas.extensions.router.junos.actionssets.actions.ospf.RemoveOSPFInterfaceInAreaAction;

public class OSPFActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new OSPFActionSet();

	public OSPFActionSetImpl() {
		super.setActionSetId("OSPFActionSet");

		this.putAction(OSPFActionSet.ActionId.OSPF_GET_CONFIGURATION, GetOSPFConfigAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_CONFIGURE, ConfigureOSPFAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_CLEAR, ClearOSPFAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_ACTIVATE, ConfigureOSPFStatusAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_DEACTIVATE, ConfigureOSPFStatusAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_ENABLE_INTERFACE, ConfigureOSPFInterfaceStatusAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_DISABLE_INTERFACE, ConfigureOSPFInterfaceStatusAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_CONFIGURE_AREA, ConfigureOSPFAreaAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_REMOVE_AREA, RemoveOSPFAreaAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_ADD_INTERFACE_IN_AREA, AddOSPFInterfaceInAreaAction.class);
		this.putAction(OSPFActionSet.ActionId.OSPF_REMOVE_INTERFACE_IN_AREA, RemoveOSPFInterfaceInAreaAction.class);
		this.putAction(OSPFActionSet.ActionId.REFRESH, GetOSPFConfigAction.class);

		/* add refresh actions */
		this.refreshActions.add(OSPFActionSet.ActionId.REFRESH);
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
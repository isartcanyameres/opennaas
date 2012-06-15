package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.extensions.router.capability.chassis.ChassisActionSet;
import org.opennaas.extensions.router.junos.actionssets.actions.GetConfigurationAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.ConfigureStatusAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.ConfigureSubInterfaceAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.DeleteSubInterfaceAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.RemoveTaggedEthernetEncapsulationAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.SetTaggedEthernetEncapsulationAction;
import org.opennaas.extensions.router.junos.actionssets.actions.chassis.SetVlanIdAction;
import org.opennaas.extensions.router.junos.actionssets.actions.logicalrouters.AddInterfaceToLogicalRouterAction;
import org.opennaas.extensions.router.junos.actionssets.actions.logicalrouters.CreateLogicalRouterAction;
import org.opennaas.extensions.router.junos.actionssets.actions.logicalrouters.DeleteLogicalRouterAction;
import org.opennaas.extensions.router.junos.actionssets.actions.logicalrouters.RemoveInterfaceFromLogicalRouterAction;

@SuppressWarnings("serial")
public class ChassisActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new ChassisActionSet();

	public ChassisActionSetImpl() {
		super.setActionSetId("chassisActionSet");

		this.putAction(ChassisActionSet.ActionId.DELETESUBINTERFACE, DeleteSubInterfaceAction.class);
		this.putAction(ChassisActionSet.ActionId.CONFIGURESUBINTERFACE, ConfigureSubInterfaceAction.class);
		this.putAction(ChassisActionSet.ActionId.CONFIGURESTATUS, ConfigureStatusAction.class);
		this.putAction(ChassisActionSet.ActionId.SET_TAGGEDETHERNET_ENCAPSULATION, SetTaggedEthernetEncapsulationAction.class);
		this.putAction(ChassisActionSet.ActionId.REMOVE_TAGGEDETHERNET_ENCAPSULATION, RemoveTaggedEthernetEncapsulationAction.class);
		this.putAction(ChassisActionSet.ActionId.SET_VLANID, SetVlanIdAction.class);
		// this.putAction(ChassisActionSet.ActionId.SETINTERFACEDESCRIPTION, SetInterfaceDescriptionAction.class);
		this.putAction(ChassisActionSet.ActionId.CREATELOGICALROUTER, CreateLogicalRouterAction.class);
		this.putAction(ChassisActionSet.ActionId.DELETELOGICALROUTER, DeleteLogicalRouterAction.class);
		this.putAction(ChassisActionSet.ActionId.ADDINTERFACETOLOGICALROUTER, AddInterfaceToLogicalRouterAction.class);
		this.putAction(ChassisActionSet.ActionId.REMOVEINTERFACEFROMLOGICALROUTER, RemoveInterfaceFromLogicalRouterAction.class);
		this.putAction(ChassisActionSet.ActionId.REFRESH, GetConfigurationAction.class);

		/* add refresh actions */
		this.refreshActions.add(ChassisActionSet.ActionId.REFRESH);
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

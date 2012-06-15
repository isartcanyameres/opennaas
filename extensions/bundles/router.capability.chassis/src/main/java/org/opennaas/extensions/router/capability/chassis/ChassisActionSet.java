package org.opennaas.extensions.router.capability.chassis;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;

public class ChassisActionSet implements IActionSetDefinition {

	public enum ActionId implements IActionId {
		// Interfaces
		CONFIGURESTATUS("configureInterfaceStatus"),
		DELETESUBINTERFACE("deleteSubInterface"),
		CONFIGURESUBINTERFACE("configureSubInterface"),
		SET_TAGGEDETHERNET_ENCAPSULATION("setTaggedEthEncapsulation"),
		REMOVE_TAGGEDETHERNET_ENCAPSULATION("removeTaggedEthEncapsulation"),
		SET_VLANID("setVlanId"),

		// LogicalRouters
		DELETELOGICALROUTER("deleteLogicalRouter"),
		CREATELOGICALROUTER("createLogicalRouter"),
		ADDINTERFACETOLOGICALROUTER("addInterfaceToLogicalRouter"),
		REMOVEINTERFACEFROMLOGICALROUTER("removeInterfaceFromLogicalRouter"),

		REFRESH("refresh");

		private String	value;

		ActionId(String value) {
			this.value = value;
		}
	}

	@Override
	public List<IActionId> getActionIds() {
		ActionId[] idsA = ActionId.values();
		List<IActionId> ids = new ArrayList<IActionId>(idsA.length);
		for (int i = 0; i < idsA.length; i++) {
			ids.add(idsA[i]);
		}
		return ids;
	}

}

package org.opennaas.extensions.router.capability.ospf;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;

public class OSPFActionSet implements IActionSetDefinition {

	public enum ActionId implements IActionId {
		OSPF_CONFIGURE("configureOSPF"),
		OSPF_CLEAR("clearOSPF"),
		OSPF_GET_CONFIGURATION("getOSPFConfiguration"),
		OSPF_ACTIVATE("activateOSPF"),
		OSPF_DEACTIVATE("deactivateOSPF"),
		OSPF_ENABLE_INTERFACE("enableOSPFInInterface"),
		OSPF_DISABLE_INTERFACE("disableOSPFInInterface"),
		OSPF_CONFIGURE_AREA("configureOSPFArea"),
		OSPF_REMOVE_AREA("removeOSPFArea"),
		OSPF_ADD_INTERFACE_IN_AREA("addOSPFInterfaceInArea"),
		OSPF_REMOVE_INTERFACE_IN_AREA("removeOSPFInterfaceInArea"),
		REFRESH("refresh");

		private String	value;

		ActionId(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

		/**
		 * 
		 * @param value
		 * @return ActionId constant matching given value
		 * @throws IllegalArgumentException
		 *             if given value does not match any allowed value.
		 */
		public static ActionId getByValue(String value) {
			for (ActionId actionId : values()) {
				if (actionId.value.equals(value)) {
					return actionId;
				}
			}
			throw new IllegalArgumentException("No enum const " + ActionId.class.getName() + "." + value);
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

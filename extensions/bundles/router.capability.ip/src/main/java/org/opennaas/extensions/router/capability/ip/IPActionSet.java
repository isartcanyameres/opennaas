package org.opennaas.extensions.router.capability.ip;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;

public class IPActionSet implements IActionSetDefinition {

	public enum ActionId implements IActionId {
		SET_INTERFACE_DESCRIPTION("setInterfaceDescription"),
		SET_IPv4("setIPv4"),
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

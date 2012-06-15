package org.opennaas.extensions.router.capability.staticroute;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;

public class StaticRouteActionSet implements IActionSetDefinition {

	public enum ActionId implements IActionId {
		STATIC_ROUTE_CREATE("createStaticRoute");

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
	};

}

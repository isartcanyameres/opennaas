package org.opennaas.core.resources.queue;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.action.IActionId;
import org.opennaas.core.resources.action.IActionSetDefinition;

public class QueueConstants implements IActionSetDefinition {
	// public static final String CONFIRM = "confirm";
	// public static final String ISALIVE = "isAlive";
	// public static final String PREPARE = "prepare";
	// public static final String RESTORE = "restore";
	// public static final String REFRESH = "refresh";
	// public static final String EXECUTE = "execute";
	// public static final String GETQUEUE = "getQueue";
	// public static final String MODIFY = "modify";
	// public static final String DUMMYEXECUTE = "dummyExecute";
	// public static final String CLEAR = "clear";

	public enum ActionId implements IActionId {
		PREPARE("prepare"),
		EXECUTE("execute"),
		CONFIRM("confirm"),
		RESTORE("restore"),
		REFRESH("refresh"),
		ISALIVE("isAlive");

		// DUMMYEXECUTE("dummyExecute"),
		// GETQUEUE("getQueue"),
		// MODIFY("modify"),
		// CLEAR("clear");

		private String	value;

		ActionId(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
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

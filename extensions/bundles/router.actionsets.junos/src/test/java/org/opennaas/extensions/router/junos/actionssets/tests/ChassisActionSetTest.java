package org.opennaas.extensions.router.junos.actionssets.tests;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opennaas.core.resources.action.Action;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.IActionId;
import org.opennaas.extensions.router.capability.chassis.ChassisActionSet;
import org.opennaas.extensions.router.junos.actionssets.ChassisActionSetImpl;

public class ChassisActionSetTest {
	private static ChassisActionSetImpl				chassis;
	private static List<ChassisActionSet.ActionId>	actionIds;
	Log												log	= LogFactory.getLog(ChassisActionSetTest.class);

	@BeforeClass
	public static void testChassisActionSet() {
		chassis = new ChassisActionSetImpl();
		actionIds = Arrays.asList(ChassisActionSet.ActionId.values());
	}

	@Test
	public void implContainsAllRequiredActionIdsTest() {
		Assert.assertTrue(chassis.getDefinition().getActionIds()
				.containsAll(actionIds));
	}

	@Test
	public void getActionSetIdTest() {
		String actionSetId = chassis.getActionSetId();
		Assert.assertNotNull(actionSetId);
		Assert.assertTrue(actionSetId.equalsIgnoreCase("chassisActionSet"));
	}

	@Test
	public void implContainsAnActionForEachRequiredId() {
		for (IActionId id : actionIds) {
			Assert.assertNotNull(chassis.getAction(id));
		}
	}

	@Test
	public void getActionTest() throws ActionException {
		for (IActionId id : actionIds) {
			Action action = chassis.obtainAction(id);
			Assert.assertNotNull(action.getActionID());
			Assert.assertEquals(id, action.getActionID());
		}
	}

}

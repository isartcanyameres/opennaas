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
import org.opennaas.core.resources.queue.QueueConstants;
import org.opennaas.extensions.router.junos.actionssets.QueueActionSetImpl;

public class QueueActionSetTest {
	private static QueueActionSetImpl				queueActions;
	private static List<QueueConstants.ActionId>	actionIds;
	Log												log	= LogFactory.getLog(ChassisActionSetTest.class);

	@BeforeClass
	public static void testBasicActionSet() {
		queueActions = new QueueActionSetImpl();
		actionIds = Arrays.asList(QueueConstants.ActionId.values());
	}

	@Test
	public void implContainsAllRequiredActionIdsTest() {
		Assert.assertTrue(queueActions.getDefinition().getActionIds()
				.containsAll(actionIds));
	}

	@Test
	public void getActionSetIdTest() {
		String actionSetId = queueActions.getActionSetId();
		Assert.assertNotNull(actionSetId);
		Assert.assertTrue(actionSetId.equalsIgnoreCase("queueActionSet"));
	}

	@Test
	public void implContainsAnActionForEachRequiredId() {
		for (IActionId id : actionIds) {
			Assert.assertNotNull(queueActions.getAction(id));
		}
	}

	@Test
	public void getActionTest() throws ActionException {
		for (IActionId id : actionIds) {
			Action action = queueActions.obtainAction(id);
			Assert.assertNotNull(action.getActionID());
			Assert.assertEquals(id, action.getActionID());
		}
	}
}

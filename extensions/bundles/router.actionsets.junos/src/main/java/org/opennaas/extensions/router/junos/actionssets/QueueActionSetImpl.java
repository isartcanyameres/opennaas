package org.opennaas.extensions.router.junos.actionssets;

import org.opennaas.core.resources.action.ActionSet;
import org.opennaas.core.resources.action.IActionSetDefinition;
import org.opennaas.core.resources.queue.QueueConstants;
import org.opennaas.extensions.router.junos.actionssets.actions.queue.ConfirmAction;
import org.opennaas.extensions.router.junos.actionssets.actions.queue.IsAliveAction;
import org.opennaas.extensions.router.junos.actionssets.actions.queue.PrepareAction;
import org.opennaas.extensions.router.junos.actionssets.actions.queue.RestoreAction;

public class QueueActionSetImpl extends ActionSet {

	private static final IActionSetDefinition	definition	= new QueueConstants();

	public QueueActionSetImpl() {
		super.setActionSetId("queueActionSet");
		this.putAction(QueueConstants.ActionId.CONFIRM, ConfirmAction.class);
		this.putAction(QueueConstants.ActionId.ISALIVE, IsAliveAction.class);
		this.putAction(QueueConstants.ActionId.PREPARE, PrepareAction.class);
		this.putAction(QueueConstants.ActionId.RESTORE, RestoreAction.class);
	}

	@Override
	public IActionSetDefinition getDefinition() {
		return definition;
	}

}
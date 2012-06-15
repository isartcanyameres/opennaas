package org.opennaas.extensions.roadm.capability.connections.actionset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.resources.ActivatorException;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.core.resources.descriptor.ResourceDescriptorConstants;
import org.opennaas.extensions.roadm.capability.connections.Activator;
import org.opennaas.extensions.router.model.opticalSwitch.FiberConnection;

/**
 * 
 * This class acts as Proxy for the real ConnectionsCapabilityActionFactory in the driver package (it looks for an OSGI service)
 */
public class ConnectionsCapabilityActionFactory implements IConnectionsCapabilityActionFactory {

	static Log		log	= LogFactory.getLog(ConnectionsCapabilityActionFactory.class);

	private String	actionSetName;
	private String	actionSetVersion;
	private String	capabilityName;

	IActionSet		profileActionSet;

	public ConnectionsCapabilityActionFactory(CapabilityDescriptor descriptor) {
		this.actionSetName = descriptor.getPropertyValue(ResourceDescriptorConstants.ACTION_NAME);
		this.actionSetVersion = descriptor.getPropertyValue(ResourceDescriptorConstants.ACTION_VERSION);
		this.capabilityName = descriptor.getCapabilityInformation().getType();
	}

	@Override
	public IAction createMakeConnectionAction(FiberConnection p1) throws ActionException {
		try {
			return getImplementation().createMakeConnectionAction(p1);
		} catch (ActivatorException e) {
			throw new ActionException(e);
		}
	}

	@Override
	public IAction createRemoveConectionAction(FiberConnection p1) throws ActionException {
		try {
			return getImplementation().createRemoveConectionAction(p1);
		} catch (ActivatorException e) {
			throw new ActionException(e);
		}
	}

	@Override
	public IAction createRefreshModelConnectionsAction() throws ActionException {
		try {
			return getImplementation().createRefreshModelConnectionsAction();
		} catch (ActivatorException e) {
			throw new ActionException(e);
		}
	}

	private IConnectionsCapabilityActionFactory getImplementation() throws ActivatorException {
		log.debug("Calling getConnectionsActionFactoryService");
		return (IConnectionsCapabilityActionFactory) Activator.getActionSetService(capabilityName, actionSetName, actionSetVersion,
				IConnectionsCapabilityActionFactory.class.getName());
	}

	private IConnectionsCapabilityActionFactory getSuitableActionFactoryFromProfile(String actionId) {
		if (profileActionSet != null) {
			if (profileActionSet.getActionNames().contains(actionId)) {
				return (IConnectionsCapabilityActionFactory) profileActionSet.getActionsFactory();
			}
		}
		return null;
	}

}

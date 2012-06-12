package org.opennaas.extensions.roadm.capability.connections.actionset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.resources.ActivatorException;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.extensions.roadm.capability.connections.Activator;
import org.opennaas.extensions.router.model.opticalSwitch.FiberConnection;

/**
 * 
 * This class acts as Proxy for the real ConnectionsCapabilityActionFactory in the driver package (it looks for an OSGI service)
 */
public class ConnectionsCapabilityActionFactory implements IConnectionsCapabilityActionFactory {

	static Log	log	= LogFactory.getLog(ConnectionsCapabilityActionFactory.class);

	private static IConnectionsCapabilityActionFactory getConnectionsActionFactoryService() throws ActivatorException {

		try {
			log.debug("Calling getConnectionsActionFactorytService");

			return (IConnectionsCapabilityActionFactory) Activator.getConnectionsCapabilityActionFactory();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public IAction createMakeConnectionAction(FiberConnection p1) throws ActionException {

		try {

			return getConnectionsActionFactoryService().createMakeConnectionAction(p1);
		} catch (ActivatorException e) {
			throw new ActionException();
		}
	}

	@Override
	public IAction createRemoveConectionAction(FiberConnection p1) throws ActionException {
		try {
			return getConnectionsActionFactoryService().createRemoveConectionAction(p1);
		} catch (ActivatorException e) {
			throw new ActionException();
		}
	}

	@Override
	public IAction createRefreshModelConnectionsAction(FiberConnection p1) throws ActionException {
		try {
			return getConnectionsActionFactoryService().createRefreshModelConnectionsAction(p1);
		} catch (ActivatorException e) {
			throw new ActionException();
		}
	}

}

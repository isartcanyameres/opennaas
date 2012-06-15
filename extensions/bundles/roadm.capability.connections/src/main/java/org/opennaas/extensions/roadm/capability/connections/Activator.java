package org.opennaas.extensions.roadm.capability.connections;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.resources.AbstractActivator;
import org.opennaas.core.resources.ActivatorException;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.descriptor.ResourceDescriptorConstants;
import org.opennaas.extensions.queuemanager.IQueueManagerCapability;
import org.opennaas.extensions.roadm.capability.connections.actionset.IConnectionsCapabilityActionFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;

public class Activator extends AbstractActivator implements BundleActivator {
	private static BundleContext	context;
	static Log						log	= LogFactory.getLog(Activator.class);

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext context) throws Exception {
		this.context = context;
	}

	public void stop(BundleContext context) throws Exception {

	}

	// public static IResourceManager getResourceManagerService() throws Exception {
	// return (IResourceManager) getServiceFromRegistry(context, IResourceManager.class.getName());
	// }

	public static IQueueManagerCapability getQueueManagerService(String resourceId) throws ActivatorException {
		try {
			log.debug("Calling QueueManagerService");
			return (IQueueManagerCapability) getServiceFromRegistry(context, createFilterQueueManager(resourceId));
		} catch (InvalidSyntaxException e) {
			throw new ActivatorException(e);
		}
	}

	/*
	 * necessary to get some capability type
	 */
	protected static Filter createFilterQueueManager(String resourceId) throws InvalidSyntaxException {
		Properties properties = new Properties();
		properties.setProperty(ResourceDescriptorConstants.CAPABILITY, "queue");
		properties.setProperty(ResourceDescriptorConstants.CAPABILITY_NAME, resourceId);
		return createServiceFilter(IQueueManagerCapability.class.getName(), properties);
	}

	public static IActionSet getConnectionsActionSetService(String name, String version) throws ActivatorException {
		try {
			log.debug("Calling ConnectionsActionSetService");
			return (IActionSet) getServiceFromRegistry(context, createFilterConnectionsActionSet(name, version));
		} catch (InvalidSyntaxException e) {
			throw new ActivatorException(e);
		}
	}

	/*
	 * necessary to get some capability type
	 */
	private static Filter createFilterConnectionsActionSet(String name, String version) throws InvalidSyntaxException {
		Properties properties = new Properties();
		properties.setProperty(ResourceDescriptorConstants.ACTION_CAPABILITY, "connections");
		properties.setProperty(ResourceDescriptorConstants.ACTION_NAME, name);
		properties.setProperty(ResourceDescriptorConstants.ACTION_VERSION, version);
		return createServiceFilter(IActionSet.class.getName(), properties);
	}

	/**
	 * 
	 * @return the OSGI registered Factory for ConnectionsCapabilityAction
	 * @throws ActivatorException
	 */
	public static IConnectionsCapabilityActionFactory getConnectionsCapabilityActionFactory(String name, String version) throws ActivatorException {
		try {
			log.debug("Calling getConnectionsCapabilityActionFactory");
			return (IConnectionsCapabilityActionFactory) getServiceFromRegistry(context,
					createFilterConnectionsActionFactory(name, version));

		} catch (InvalidSyntaxException e) {
			throw new ActivatorException(e);
		}

	}

	private static Filter createFilterConnectionsActionFactory(String name, String version) throws InvalidSyntaxException {
		Properties properties = new Properties();
		properties.setProperty(ResourceDescriptorConstants.ACTION_CAPABILITY, "connections");
		properties.setProperty(ResourceDescriptorConstants.ACTION_NAME, name);
		properties.setProperty(ResourceDescriptorConstants.ACTION_VERSION, version);
		return createServiceFilter(IConnectionsCapabilityActionFactory.class.getName(), properties);
	}

	public static Object getActionSetService(String capability, String actionSetName, String actionSetVersion, String serviceInterface)
			throws ActivatorException {
		try {
			log.debug("Calling getActionSetService");

			Properties properties = new Properties();
			properties.setProperty(ResourceDescriptorConstants.ACTION_CAPABILITY, capability);
			properties.setProperty(ResourceDescriptorConstants.ACTION_NAME, actionSetName);
			properties.setProperty(ResourceDescriptorConstants.ACTION_VERSION, actionSetVersion);

			Filter serviceFilter = createServiceFilter(serviceInterface, properties);
			return getServiceFromRegistry(context, serviceFilter);

		} catch (InvalidSyntaxException e) {
			throw new ActivatorException(e);
		}
	}

}

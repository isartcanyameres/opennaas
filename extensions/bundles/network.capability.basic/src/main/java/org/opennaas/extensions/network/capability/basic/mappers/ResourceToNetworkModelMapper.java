package org.opennaas.extensions.network.capability.basic.mappers;

import java.util.List;

import org.opennaas.core.resources.IResource;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.extensions.model.ndl.topology.NetworkElement;
import org.opennaas.extensions.network.model.NetworkModel;

public class ResourceToNetworkModelMapper {

	/**
	 * Transforms given Resource to a network model representation and adds it to given networkModel.
	 * 
	 * @param resource
	 *            to add
	 * @param networkModel
	 *            where to add a representation of resource
	 * @param name
	 *            name for the resource to add
	 * @return NetworkElements created to represent given resource in networkModel.
	 * @throws CapabilityException
	 *             if given resource is not supported.
	 */
	public static List<NetworkElement> addResourceToNetworkModel(IResource resource, NetworkModel networkModel, String name)
			throws CapabilityException {

		// TODO add support for BoD resources at least
		if (isRouter(resource)) {
			return Cim2NdlMapper.addResourceToNetworkModel(resource, networkModel, name);
		} else {
			throw new CapabilityException("Cannot add resource " + name + ". Unsupported resource type");
		}

	}

	private static boolean isRouter(IResource resource) {
		return resource.getResourceDescriptor().getInformation().getType().equals("router");
	}
}

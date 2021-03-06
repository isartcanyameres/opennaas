package org.opennaas.extensions.power.capabilities.factories;

import org.opennaas.core.resources.IResource;
import org.opennaas.core.resources.capability.AbstractCapabilityFactory;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.core.resources.capability.ICapability;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.extensions.power.capabilities.PowerSupplyCapability;

public class PowerSupplyCapabilityFactory extends AbstractCapabilityFactory {

	@Override
	public ICapability create(IResource resource) throws CapabilityException {
		ICapability capability = this.create(resource.getResourceDescriptor().getCapabilityDescriptor(PowerSupplyCapability.CAPABILITY_TYPE),
				resource.getResourceDescriptor().getId());
		capability.setResource(resource);
		return capability;
	}

	@Override
	public ICapability createCapability(CapabilityDescriptor capabilityDescriptor, String resourceId) throws CapabilityException {
		return new PowerSupplyCapability(capabilityDescriptor, resourceId);
	}

}

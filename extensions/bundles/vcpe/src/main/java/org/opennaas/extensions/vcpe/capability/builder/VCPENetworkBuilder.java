package org.opennaas.extensions.vcpe.capability.builder;

import static com.google.common.collect.Iterables.filter;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.resources.ActivatorException;
import org.opennaas.core.resources.IResource;
import org.opennaas.core.resources.IResourceManager;
import org.opennaas.core.resources.ResourceException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.capability.AbstractCapability;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.core.resources.protocol.ProtocolException;
import org.opennaas.core.resources.queue.QueueResponse;
import org.opennaas.extensions.queuemanager.IQueueManagerCapability;
import org.opennaas.extensions.router.capability.ip.IIPCapability;
import org.opennaas.extensions.router.capability.vrrp.IVRRPCapability;
import org.opennaas.extensions.router.model.ComputerSystem;
import org.opennaas.extensions.router.model.IPProtocolEndpoint;
import org.opennaas.extensions.router.model.LogicalPort;
import org.opennaas.extensions.router.model.NetworkPort;
import org.opennaas.extensions.router.model.ProtocolEndpoint;
import org.opennaas.extensions.router.model.Service;
import org.opennaas.extensions.router.model.VRRPGroup;
import org.opennaas.extensions.router.model.VRRPProtocolEndpoint;
import org.opennaas.extensions.vcpe.Activator;
import org.opennaas.extensions.vcpe.capability.VCPEToRouterModelTranslator;
import org.opennaas.extensions.vcpe.manager.templates.basic.builder.BasicVCPEBuilder;
import org.opennaas.extensions.vcpe.manager.templates.basic.builder.BasicVCPEBuilderParameters;
import org.opennaas.extensions.vcpe.manager.templates.builder.VCPEBuilderResult;
import org.opennaas.extensions.vcpe.model.Interface;
import org.opennaas.extensions.vcpe.model.Router;
import org.opennaas.extensions.vcpe.model.VCPENetworkModel;
import org.opennaas.extensions.vcpe.model.VCPETemplate;
import org.opennaas.extensions.vcpe.model.helper.VCPENetworkModelHelper;

public class VCPENetworkBuilder extends AbstractCapability implements IVCPENetworkBuilder {

	Log							log				= LogFactory.getLog(VCPENetworkBuilder.class);

	public static final String	CAPABILITY_TYPE	= "vcpenet_builder";
	private String				resourceId;
	private BasicVCPEBuilder	builder;

	public VCPENetworkBuilder(CapabilityDescriptor descriptor, String resourceId) {
		super(descriptor);
		this.resourceId = resourceId;
		this.builder = new BasicVCPEBuilder();
	}

	@Override
	public void activate() throws CapabilityException {
		registerService(Activator.getContext(), CAPABILITY_TYPE, getResourceType(), getResourceName(), IVCPENetworkBuilder.class.getName());
		setState(State.ACTIVE);
	}

	@Override
	public void deactivate() throws CapabilityException {
		setState(State.INACTIVE);
		registration.unregister();
		super.deactivate();
	}

	@Override
	public String getCapabilityName() {
		return CAPABILITY_TYPE;
	}

	@Override
	public void queueAction(IAction action) throws CapabilityException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IActionSet getActionSet() throws CapabilityException {
		throw new UnsupportedOperationException();
	}

	// /////////////////////////////////////
	// IVCPENetworkBuilder implementation //
	// /////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extensions.vcpe.capability.builder.IVCPENetworkBuilder#buildVCPENetwork(org.opennaas.extensions.vcpe.model.VCPENetworkModel)
	 */
	@Override
	public VCPENetworkModel buildVCPENetwork(VCPENetworkModel desiredScenario) throws CapabilityException {

		if (((VCPENetworkModel) resource.getModel()).isCreated())
			throw new CapabilityException("VCPE already created");

		try {
			BasicVCPEBuilderParameters params = new BasicVCPEBuilderParameters();
			params.setModel(desiredScenario);
			VCPEBuilderResult result = builder.build(params);

			resource.setModel(result.getModel());

			return (VCPENetworkModel) resource.getModel();
		} catch (ResourceException e) {
			throw new CapabilityException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extensions.vcpe.capability.builder.IVCPENetworkBuilder#destroyVCPENetwork()
	 */
	@Override
	public void destroyVCPENetwork() throws CapabilityException {

		if (!((VCPENetworkModel) resource.getModel()).isCreated())
			throw new CapabilityException("VCPE has not been created");

		try {

			VCPEBuilderResult result = builder.unbuild((VCPENetworkModel) resource.getModel());
			resource.setModel(result.getModel());

		} catch (ResourceException e) {
			throw new CapabilityException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extensions.vcpe.capability.builder.IVCPENetworkBuilder#updateIps(org.opennaas.extensions.vcpe.model.VCPENetworkModel)
	 */
	@Override
	public void updateIps(VCPENetworkModel updatedModel) throws CapabilityException {

		if (!((VCPENetworkModel) resource.getModel()).isCreated())
			throw new CapabilityException("VCPE has not been created");

		VCPENetworkModel currentModel = (VCPENetworkModel) resource.getModel();

		// launch commands
		try {
			for (Router router : filter(updatedModel.getElements(), Router.class)) {
				for (Interface iface : router.getInterfaces()) {
					Interface outDatedIface = (Interface) VCPENetworkModelHelper.getElementByTemplateName(currentModel, iface.getTemplateName());
					if (!outDatedIface.getIpAddress().equals(iface.getIpAddress())) {
						setIP(router, outDatedIface, iface.getIpAddress(), currentModel);
					}
				}
			}
		} catch (ResourceException e) {
			throw new CapabilityException(e);
		}

		// execute queues
		try {
			executeLogicalRouters(currentModel);
			executePhysicalRouters(currentModel);
		} catch (Exception e) {
			throw new CapabilityException(e);
		}

		// update IP addresses in model
		for (Router router : filter(updatedModel.getElements(), Router.class)) {
			for (Interface iface : router.getInterfaces()) {
				Interface outDatedIface = (Interface) VCPENetworkModelHelper.getElementByTemplateName(currentModel, iface.getTemplateName());
				if (!outDatedIface.getIpAddress().equals(iface.getIpAddress())) {
					outDatedIface.setIpAddress(iface.getIpAddress());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opennaas.extensions.vcpe.capability.builder.IVCPENetworkBuilder#updateVRRPIp(org.opennaas.extensions.vcpe.model.VCPENetworkModel)
	 */
	@Override
	public void updateVRRPIp(VCPENetworkModel vcpeModel) throws CapabilityException {
		log.debug("Updating VRRP ip");
		try {
			Router lr1 = (Router) VCPENetworkModelHelper.getElementByTemplateName(vcpeModel, VCPETemplate.VCPE1_ROUTER);
			Router lr2 = (Router) VCPENetworkModelHelper.getElementByTemplateName(vcpeModel, VCPETemplate.VCPE2_ROUTER);

			IResource router1 = getResourceManager().getResource(
					getResourceManager().getIdentifierFromResourceName("router", lr1.getName()));
			IResource router2 = getResourceManager().getResource(
					getResourceManager().getIdentifierFromResourceName("router", lr2.getName()));

			IVRRPCapability capability1 = (IVRRPCapability) router1.getCapabilityByInterface(IVRRPCapability.class);
			IVRRPCapability capability2 = (IVRRPCapability) router2.getCapabilityByInterface(IVRRPCapability.class);

			VRRPProtocolEndpoint endpointRouter1 = getVRRPProtocolEndpointDeepCopy((ComputerSystem) router1.getModel());
			((VRRPGroup) endpointRouter1.getService()).setVirtualIPAddress(vcpeModel.getVrrp().getVirtualIPAddress());

			VRRPProtocolEndpoint endpointRouter2 = getVRRPProtocolEndpointDeepCopy((ComputerSystem) router2.getModel());
			((VRRPGroup) endpointRouter2.getService()).setVirtualIPAddress(vcpeModel.getVrrp().getVirtualIPAddress());

			capability1.updateVRRPVirtualIPAddress(endpointRouter1);
			capability2.updateVRRPVirtualIPAddress(endpointRouter2);

			execute(router1);
			execute(router2);
		} catch (Exception e) {
			throw new CapabilityException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opennaas.extensions.vcpe.capability.builder.IVCPENetworkBuilder#changeVRRPPriority(org.opennaas.extensions.vcpe.model.VCPENetworkModel)
	 */
	@Override
	public void changeVRRPPriority(VCPENetworkModel model) throws CapabilityException {
		log.debug("Change the priority VRRP");
		try {
			Router lr1 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.VCPE1_ROUTER);
			Router lr2 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.VCPE2_ROUTER);

			IResource router1 = getResourceManager().getResource(
					getResourceManager().getIdentifierFromResourceName("router", lr1.getName()));
			IResource router2 = getResourceManager().getResource(
					getResourceManager().getIdentifierFromResourceName("router", lr2.getName()));

			// get VRRPProtocolEndpoint deep copies
			VRRPProtocolEndpoint vrrpProtocolEndpoint1 = getVRRPProtocolEndpointDeepCopy((ComputerSystem) router1.getModel());
			VRRPProtocolEndpoint vrrpProtocolEndpoint2 = getVRRPProtocolEndpointDeepCopy((ComputerSystem) router2.getModel());
			// swap priorities
			int priority1 = vrrpProtocolEndpoint1.getPriority();
			int priority2 = vrrpProtocolEndpoint2.getPriority();
			vrrpProtocolEndpoint1.setPriority(priority2);
			vrrpProtocolEndpoint2.setPriority(priority1);

			// call capabilities
			IVRRPCapability capability1 = (IVRRPCapability) router1.getCapabilityByInterface(IVRRPCapability.class);
			capability1.updateVRRPPriority(vrrpProtocolEndpoint1);
			IVRRPCapability capability2 = (IVRRPCapability) router2.getCapabilityByInterface(IVRRPCapability.class);
			capability2.updateVRRPPriority(vrrpProtocolEndpoint2);

			// execute queues
			execute(router1);
			execute(router2);
		} catch (Exception e) {
			throw new CapabilityException(e);
		}
	}

	private void setIP(Router router, Interface iface, String ipAddress, VCPENetworkModel model) throws ResourceException {
		IResource routerResource = getResourceManager().getResource(
				getResourceManager().getIdentifierFromResourceName("router", router.getName()));

		IIPCapability capability = (IIPCapability) routerResource.getCapabilityByInterface(IIPCapability.class);

		LogicalPort port = VCPEToRouterModelTranslator.vCPEInterfaceToLogicalPort(iface, model);
		IPProtocolEndpoint ipPEP = VCPEToRouterModelTranslator.ipAddressToProtocolEndpoint(ipAddress);
		capability.setIPv4(port, ipPEP);
	}

	private void executePhysicalRouters(VCPENetworkModel model) throws ResourceException, ProtocolException {
		Router phy1 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.CPE1_PHY_ROUTER);
		Router phy2 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.CPE2_PHY_ROUTER);

		IResource phyResource1 = getResourceManager().getResource(
				getResourceManager().getIdentifierFromResourceName("router", phy1.getName()));
		IResource phyResource2 = getResourceManager().getResource(
				getResourceManager().getIdentifierFromResourceName("router", phy2.getName()));

		execute(phyResource1);
		execute(phyResource2);
	}

	private void executeLogicalRouters(VCPENetworkModel model) throws ResourceException, ProtocolException {
		Router lr1 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.VCPE1_ROUTER);
		Router lr2 = (Router) VCPENetworkModelHelper.getElementByTemplateName(model, VCPETemplate.VCPE2_ROUTER);

		IResource lrResource1 = getResourceManager().getResource(
				getResourceManager().getIdentifierFromResourceName("router", lr1.getName()));
		IResource lrResource2 = getResourceManager().getResource(
				getResourceManager().getIdentifierFromResourceName("router", lr2.getName()));

		execute(lrResource1);
		execute(lrResource2);
	}

	private void execute(IResource resource) throws ResourceException, ProtocolException {
		IQueueManagerCapability qCapability = (IQueueManagerCapability) resource.getCapabilityByInterface(IQueueManagerCapability.class);
		QueueResponse response = qCapability.execute();
		if (!response.isOk()) {
			String commitMsg = response.getConfirmResponse().getInformation();
			throw new ResourceException(
					"Failed to execute queue for resource " + resource.getResourceDescriptor().getInformation().getName() + ": " + commitMsg);
		}
	}

	private IResourceManager getResourceManager() throws ResourceException {
		try {
			return Activator.getResourceManagerService();
		} catch (ActivatorException e) {
			throw new ResourceException("Could not find ResourceManager", e);
		}
	}

	/**
	 * Get a deep copy of VRRPProtocolEndpoint with all necessary elements
	 * 
	 * @param originalRouter
	 *            original ComputerSystem where the VRRProtocolEndpoint is
	 * @return
	 * @throws Exception
	 */
	private static VRRPProtocolEndpoint getVRRPProtocolEndpointDeepCopy(ComputerSystem originalRouter) throws Exception {
		VRRPGroup vrrpGroup = null;
		// ComputerSystem copy
		ComputerSystem newRouter = new ComputerSystem();

		VRRPGroup newVRRPGroup = null;
		List<Service> services = originalRouter.getHostedService();
		for (Service service : services) {
			if (service instanceof VRRPGroup) {
				vrrpGroup = (VRRPGroup) service;
				// VRRPGroup copy
				newVRRPGroup = new VRRPGroup();
				// copy attributes (name & virtual IP address)
				newVRRPGroup.setVrrpName(vrrpGroup.getVrrpName());
				newVRRPGroup.setVirtualIPAddress(vrrpGroup.getVirtualIPAddress());
				// add Service to ComputerSystem
				newRouter.addHostedService(newVRRPGroup);
				break;
			}
		}

		VRRPProtocolEndpoint vrrpProtocolEndpoint = null;
		VRRPProtocolEndpoint newVRRPProtocolEndpoint = null;
		if (vrrpGroup != null) {
			List<ProtocolEndpoint> protocolEndpoints = vrrpGroup.getProtocolEndpoint();
			for (ProtocolEndpoint protocolEndpoint : protocolEndpoints) {
				if (((VRRPGroup) ((VRRPProtocolEndpoint) protocolEndpoint).getService()).getVrrpName() == vrrpGroup.getVrrpName()) {
					vrrpProtocolEndpoint = (VRRPProtocolEndpoint) protocolEndpoint;
					// VRRPProtocolEndpoint copy
					newVRRPProtocolEndpoint = new VRRPProtocolEndpoint();
					// copy attributes (priority)
					newVRRPProtocolEndpoint.setPriority(vrrpProtocolEndpoint.getPriority());
					// set VRRPGroup as Service of VRRPProtocolEndpoint
					newVRRPProtocolEndpoint.setService(newVRRPGroup);

					// IPProtocolEndpoint copy
					IPProtocolEndpoint ipProtocolEndpoint = (IPProtocolEndpoint) vrrpProtocolEndpoint.getBindedProtocolEndpoints().get(0);
					IPProtocolEndpoint newIPProtocolEndpoint = new IPProtocolEndpoint();
					// set attributes (IPv4 address & subnet mask)
					newIPProtocolEndpoint.setIPv4Address(ipProtocolEndpoint.getIPv4Address());
					newIPProtocolEndpoint.setSubnetMask(ipProtocolEndpoint.getSubnetMask());
					// bind ServiceAccesPoint (IPProtocolEndpoint) to VRRPProtocolEndpoint
					newVRRPProtocolEndpoint.bindServiceAccessPoint(newIPProtocolEndpoint);

					// NetworkPort copy
					NetworkPort networkPort = (NetworkPort) ipProtocolEndpoint.getLogicalPorts().get(0);
					NetworkPort newNetworkPort = new NetworkPort();
					// set attributes (name & port)
					newNetworkPort.setName(networkPort.getName());
					newNetworkPort.setPortNumber(networkPort.getPortNumber());
					// add ProtocolEndpoint (IPProtocolEndpoint) to NetworkPort
					newNetworkPort.addProtocolEndpoint(newIPProtocolEndpoint);

					// add LogicalDevice (NetworkPort) to ComputerSystem
					newRouter.addLogicalDevice(newNetworkPort);
				}
			}
			if (vrrpProtocolEndpoint == null) {
				throw new Exception("VRRPProtocolEndpoint not found");
			}
		} else {
			throw new Exception("VRRPGroup not found");
		}
		return newVRRPProtocolEndpoint;
	}
}

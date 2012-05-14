package org.opennaas.itests.ws;

import static org.openengsb.labs.paxexam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.opennaas.extensions.itests.helpers.OpennaasExamOptions.includeFeatures;
import static org.opennaas.extensions.itests.helpers.OpennaasExamOptions.includeTestHelper;
import static org.opennaas.extensions.itests.helpers.OpennaasExamOptions.includeWSClient;
import static org.opennaas.extensions.itests.helpers.OpennaasExamOptions.noConsole;
import static org.opennaas.extensions.itests.helpers.OpennaasExamOptions.opennaasDistributionConfiguration;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennaas.core.resources.IResource;
import org.opennaas.core.resources.IResourceManager;
import org.opennaas.core.resources.ResourceException;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.core.resources.helpers.ResourceHelper;
import org.opennaas.core.resources.protocol.IProtocolManager;
import org.opennaas.core.resources.protocol.ProtocolException;
import org.opennaas.itests.ws.utils.OpennaasClient;
import org.opennaas.ws.ActionException_Exception;
import org.opennaas.ws.CapabilityException_Exception;
import org.opennaas.ws.ComputerSystem;
import org.opennaas.ws.FiberConnection;
import org.opennaas.ws.GreTunnelService;
import org.opennaas.ws.IChassisCapabilityService;
import org.opennaas.ws.IConnectionsCapabilityService;
import org.opennaas.ws.IGRETunnelCapabilityService;
import org.opennaas.ws.IIPCapabilityService;
import org.opennaas.ws.IL2BoDCapabilityService;
import org.opennaas.ws.IMonitoringCapabilityService;
import org.opennaas.ws.INetOSPFCapabilityService;
import org.opennaas.ws.INetQueueCapabilityService;
import org.opennaas.ws.INetworkBasicCapabilityService;
import org.opennaas.ws.IOSPFCapabilityService;
import org.opennaas.ws.IProtocolSessionManagerService;
import org.opennaas.ws.IQueueManagerCapabilityService;
import org.opennaas.ws.IResourceManagerService;
import org.opennaas.ws.IStaticRouteCapabilityService;
import org.opennaas.ws.Interface;
import org.opennaas.ws.IpProtocolEndpoint;
import org.opennaas.ws.LogicalPort;
import org.opennaas.ws.ModifyParams;
import org.opennaas.ws.NetworkPort;
import org.opennaas.ws.OspfArea;
import org.opennaas.ws.OspfAreaConfiguration;
import org.opennaas.ws.OspfProtocolEndpoint;
import org.opennaas.ws.OspfService;
import org.opennaas.ws.ProtocolException_Exception;
import org.opennaas.ws.ProtocolSessionContext;
import org.opennaas.ws.RequestConnectionParameters;
import org.opennaas.ws.ResourceDescriptor;
import org.opennaas.ws.ResourceException_Exception;
import org.opennaas.ws.ResourceIdentifier;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.service.blueprint.container.BlueprintContainer;

@RunWith(JUnit4TestRunner.class)
public class WebServiceITest {

	@Inject
	protected IResourceManager				resourceManager;

	@Inject
	protected IProtocolManager				protocolManager;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.router.repository)")
	private BlueprintContainer				routerRepoService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.network.repository)")
	private BlueprintContainer				netRepoService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.roadm.repository)")
	private BlueprintContainer				roadmRepoService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.bod.repository)")
	private BlueprintContainer				bodRepoService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.protocols.netconf)")
	private BlueprintContainer				netconfService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.roadm.protocols.wonesys)")
	private BlueprintContainer				wonesysService;

	@Inject
	@Filter("(osgi.blueprint.container.symbolicname=org.opennaas.extensions.ws)")
	private BlueprintContainer				wsService;

	// web service endpoints
	private IResourceManagerService			rm;
	private IProtocolSessionManagerService	psm;
	private IChassisCapabilityService		chassis;
	private IIPCapabilityService			ip;
	private IOSPFCapabilityService			ospf;
	private IGRETunnelCapabilityService		gre;
	private IStaticRouteCapabilityService	sr;
	private IQueueManagerCapabilityService	qm;
	private IL2BoDCapabilityService			l2bod;
	private INetworkBasicCapabilityService	basic;
	private INetOSPFCapabilityService		netospf;
	private INetQueueCapabilityService		nqm;
	private IConnectionsCapabilityService	con;
	private IMonitoringCapabilityService	mon;

	// resource ids
	private ResourceIdentifier				routerId;
	private ResourceIdentifier				roadmId;
	private ResourceIdentifier				bodId;
	private ResourceIdentifier				netId;

	@Configuration
	public static Option[] configuration() {
		return options(opennaasDistributionConfiguration(),
				includeFeatures("opennaas"),
				includeWSClient(),
				includeTestHelper(),
				noConsole(),
				keepRuntimeFolder());
	}

	@Before
	public void init() throws ResourceException, ProtocolException {
		getWebServiceEndpoints();
		createAndInitResources();
	}

	@Test
	public void callAllMethods() throws ResourceException_Exception {

		ResourceDescriptor desc = new ResourceDescriptor();
		ResourceIdentifier id = new ResourceIdentifier();

		callRMMethodsIgnoringResourceException(desc, id);

		// TODO call All WS methods

	}

	// @Test
	// public void checkParameters() {
	// // TODO test parameters are like expected
	// }

	private void allRMMethods(ResourceDescriptor desc, ResourceIdentifier id) throws ResourceException_Exception {
		rm.createResource(desc);
		rm.modifyResource(id, desc);
		rm.exportResourceDescriptor(id, "sample.descriptor");
		rm.getIdentifierFromResourceName(desc.getInformation().getType(), desc.getInformation().getName());
		rm.getNameFromResourceID(id.getId());
		rm.listResources();
		rm.listResourcesByType(id.getType());
		rm.getResourceTypes();
		rm.getResourceDescriptor(id);
		rm.startResource(id);
		rm.stopResource(id);
		rm.startResource(id);
		rm.forceStopResource(id);
		rm.removeResource(id);
	}

	private void allPSMMethods(ResourceIdentifier id, ProtocolSessionContext ctx) throws ResourceException_Exception, ProtocolException_Exception {
		psm.registerContext(id.getId(), ctx);
		psm.getRegisteredContexts(id.getId());
		psm.unregisterContext(id.getId(), ctx);
		psm.registerContext(id.getId(), ctx);
		psm.unregisterContextByProtocol(id.getId(), "netconf");
	}

	private void allChassisMethods(String id, LogicalPort phyIface, NetworkPort subIface, String encapsulationType, String encapsulationLabel,
			ComputerSystem lrModel, List<LogicalPort> lrIfaces) throws CapabilityException_Exception {
		chassis.downPhysicalInterface(id, phyIface);
		chassis.upPhysicalInterface(id, phyIface);
		chassis.setEncapsulation(id, phyIface, encapsulationType);
		chassis.createSubInterface(id, subIface);
		chassis.setEncapsulationLabel(id, subIface, encapsulationLabel);
		chassis.createLogicalRouter(id, lrModel);
		chassis.addInterfacesToLogicalRouter(id, lrModel, lrIfaces);
		chassis.removeInterfacesFromLogicalRouter(id, lrModel, lrIfaces);
		chassis.deleteSubInterface(id, subIface);
		chassis.deleteLogicalRouter(id, lrModel);
	}

	private void allIPMethods(String id, LogicalPort iface, IpProtocolEndpoint ep, String desc) throws CapabilityException_Exception {
		ip.setIPv4(id, iface, ep);
		ip.setInterfaceDescription(id, iface); // TODO missing description in this call!!!
	}

	private void allOSPFMethods(String id, OspfService ospfConf, OspfAreaConfiguration ospfAreaConfig, OspfArea ospfArea, List<LogicalPort> ifaces,
			List<OspfProtocolEndpoint> ospfPeps) throws CapabilityException_Exception {
		ospf.configureOSPF(id, ospfConf);
		ospf.configureOSPFArea(id, ospfAreaConfig);
		ospf.addInterfacesInOSPFArea(id, ifaces, ospfArea);
		ospf.enableOSPFInterfaces(id, ospfPeps);
		ospf.activateOSPF(id);
		ospf.getOSPFConfiguration(id);
		ospf.deactivateOSPF(id);
		ospf.disableOSPFInterfaces(id, ospfPeps);
		ospf.removeInterfacesInOSPFArea(id, ifaces, ospfArea);
		ospf.removeOSPFArea(id, ospfAreaConfig);
		ospf.clearOSPFconfiguration(id, ospfConf);
	}

	private void allGreMethods(String id, GreTunnelService greService) throws CapabilityException_Exception {
		gre.createGRETunnel(id, greService);
		gre.showGRETunnelConfiguration(id);
		gre.deleteGRETunnel(id, greService);
	}

	private void allStaticRouteMethods(String id, String netIpRange, String netIPRangeMask, String nextHopIPAddress)
			throws CapabilityException_Exception {
		sr.createStaticRoute(id, netIpRange, netIPRangeMask, nextHopIPAddress);
	}

	private void allQManagerMethods(String id, ModifyParams modifyParams)
			throws CapabilityException_Exception, ActionException_Exception, ProtocolException_Exception {
		qm.getActions(id);
		qm.modify(id, modifyParams);
		qm.clear(id);
		qm.execute(id);
	}

	private void allL2BoDMethods(String id, RequestConnectionParameters requestConnectionParams, List<Interface> ifaces)
			throws CapabilityException_Exception {
		l2bod.requestConnection(id, requestConnectionParams);
		l2bod.shutDownConnection(id, ifaces);
	}

	private void allNetBasicMethods(String id, String routerId, Interface fromIface, Interface toIface) throws CapabilityException_Exception {
		basic.addResource(id, routerId);
		basic.l2Attach(id, fromIface, toIface);
		basic.l2Detach(id, fromIface, toIface);
		basic.removeResource(id, routerId);
	}

	private void allNetOSPFMethods(String id) throws CapabilityException_Exception {
		netospf.activateOSPF(id);
		netospf.deactivateOSPF(id);
	}

	private void allNetQMethods(String id) throws CapabilityException_Exception {
		nqm.execute(id);
	}

	private void allConnectionsMethods(String id, FiberConnection fiberConnection) throws CapabilityException_Exception {
		con.makeConnection(id, fiberConnection);
		con.removeConnection(id, fiberConnection);
	}

	private void allMonitoringMethods(String id) throws CapabilityException_Exception {
		mon.clearAlarms(id);
	}

	// SWALLOW EXCEPTIONS

	public void callRMMethodsIgnoringResourceException(ResourceDescriptor desc, ResourceIdentifier id) {
		try {
			rm.createResource(desc);
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.modifyResource(id, desc);
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.removeResource(id);
		} catch (ResourceException_Exception e) {
		}
		rm.listResources();
		rm.listResourcesByType(id.getType());
		rm.getResourceTypes();
		try {
			rm.getResourceDescriptor(id);
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.startResource(id);
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.stopResource(id);
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.exportResourceDescriptor(id, "sample.descriptor");
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.getIdentifierFromResourceName(desc.getInformation().getType(), desc.getInformation().getName());
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.getNameFromResourceID(id.getId());
		} catch (ResourceException_Exception e) {
		}
		try {
			rm.forceStopResource(id);
		} catch (ResourceException_Exception e) {
		}
	}

	private void getWebServiceEndpoints() {

		rm = OpennaasClient.getResourceManagerService();
		psm = OpennaasClient.getProtocolSessionManagerService();

		// Router capabilities
		chassis = OpennaasClient.getChassisCapabilityService();
		ip = OpennaasClient.getIPCapabilityService();
		ospf = OpennaasClient.getOSPFCapabilityService();
		gre = OpennaasClient.getGRETunnelCapabilityService();
		sr = OpennaasClient.getStaticRouteCapabilityService();
		qm = OpennaasClient.getQueueManagerCapabilityService();

		// BoD capabilities
		l2bod = OpennaasClient.getL2BoDCapabilityService();

		// Network capabilities
		basic = OpennaasClient.getNetworkBasicCapabilityService();
		netospf = OpennaasClient.getNetOSPFCapabilityService();
		nqm = OpennaasClient.getNetQueueCapabilityService();

		// ROADM capabilities
		con = OpennaasClient.getConnectionsCapabilityService();
		mon = OpennaasClient.getMonitoringCapabilityService();
	}

	private void createAndInitResources() throws ResourceException, ProtocolException {
		routerId = createAndInitRouter();
		roadmId = createAndInitRoadm();
		bodId = createAndInitBoD();
		netId = createAndInitNet();
	}

	private ResourceIdentifier createAndInitRouter() throws ResourceException, ProtocolException {
		List<CapabilityDescriptor> capabilityDescriptors = new ArrayList<CapabilityDescriptor>();
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "queue", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "chassis", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "ipv4", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "ospf", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "gre", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("junos", "10.10", "staticroute", ""));

		IResource resource = resourceManager.createResource(
				ResourceHelper.newResourceDescriptor(capabilityDescriptors, "router", "", "router1"));
		protocolManager.getProtocolSessionManager(resource.getResourceIdentifier().getId()).
				registerContext(ResourceHelper.newSessionContextNetconf());
		resourceManager.startResource(resource.getResourceIdentifier());

		ResourceIdentifier resourceId = new ResourceIdentifier();
		resourceId.setId(resource.getResourceIdentifier().getId());
		resourceId.setType(resource.getResourceIdentifier().getType());
		return resourceId;
	}

	private ResourceIdentifier createAndInitRoadm() throws ResourceException, ProtocolException {
		List<CapabilityDescriptor> capabilityDescriptors = new ArrayList<CapabilityDescriptor>();
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("wonesys", "1.0.0", "queue", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("wonesys", "1.0.0", "connections", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("wonesys", "1.0.0", "monitoring", ""));

		IResource resource = resourceManager.createResource(
				ResourceHelper.newResourceDescriptor(capabilityDescriptors, "roadm", "", "roadm1"));
		protocolManager.getProtocolSessionManager(resource.getResourceIdentifier().getId()).
				registerContext(ResourceHelper.newSessionContextWonesys());
		resourceManager.startResource(resource.getResourceIdentifier());

		ResourceIdentifier resourceId = new ResourceIdentifier();
		resourceId.setId(resource.getResourceIdentifier().getId());
		resourceId.setType(resource.getResourceIdentifier().getType());
		return resourceId;
	}

	private ResourceIdentifier createAndInitBoD() throws ResourceException, ProtocolException {
		List<CapabilityDescriptor> capabilityDescriptors = new ArrayList<CapabilityDescriptor>();
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("dummy", "1.0", "queue", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("dummy", "1.0", "l2bod", ""));

		IResource resource = resourceManager.createResource(
				ResourceHelper.newResourceDescriptor(capabilityDescriptors, "bod", "", "bod1"));
		resourceManager.startResource(resource.getResourceIdentifier());

		ResourceIdentifier resourceId = new ResourceIdentifier();
		resourceId.setId(resource.getResourceIdentifier().getId());
		resourceId.setType(resource.getResourceIdentifier().getType());
		return resourceId;
	}

	private ResourceIdentifier createAndInitNet() throws ResourceException, ProtocolException {
		List<CapabilityDescriptor> capabilityDescriptors = new ArrayList<CapabilityDescriptor>();
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("", "", "basicNetwork", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("", "", "netospf", ""));
		capabilityDescriptors.add(ResourceHelper.newCapabilityDescriptor("", "", "netqueue", ""));

		IResource resource = resourceManager.createResource(
				ResourceHelper.newResourceDescriptor(capabilityDescriptors, "network", "", "net1"));
		resourceManager.startResource(resource.getResourceIdentifier());

		ResourceIdentifier resourceId = new ResourceIdentifier();
		resourceId.setId(resource.getResourceIdentifier().getId());
		resourceId.setType(resource.getResourceIdentifier().getType());
		return resourceId;
	}
}

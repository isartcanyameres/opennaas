package net.i2cat.mantychore.actionsets.junos.actions.test.logicalrouters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import mock.MockEventManager;
import net.i2cat.mantychore.actionsets.junos.actions.logicalrouters.AddInterfaceToLogicalRouterAction;
import net.i2cat.mantychore.model.ComputerSystem;
import net.i2cat.mantychore.model.EthernetPort;
import net.i2cat.mantychore.protocols.netconf.NetconfProtocolSessionFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opennaas.core.protocols.sessionmanager.impl.ProtocolManager;
import org.opennaas.core.protocols.sessionmanager.impl.ProtocolSessionManager;
import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.ActionResponse;
import org.opennaas.core.resources.action.ActionResponse.STATUS;
import org.opennaas.core.resources.protocol.ProtocolException;
import org.opennaas.core.resources.protocol.ProtocolSessionContext;

public class AddInterfaceToLRTest {

	private static ProtocolManager						protocolManager;
	private static ProtocolSessionManager				protocolSessionManager;
	private static ProtocolSessionContext				netconfContext;
	private static String								resourceId	= "testResource";
	private static AddInterfaceToLogicalRouterAction	action;

	@BeforeClass
	public static void init() throws ProtocolException {
		action = new AddInterfaceToLogicalRouterAction();
		action.setModelToUpdate(new ComputerSystem());

		protocolManager = new ProtocolManager();
		protocolSessionManager = (ProtocolSessionManager) protocolManager.getProtocolSessionManager(resourceId);
		protocolSessionManager.setEventManager(new MockEventManager());
		netconfContext = newSessionContextNetconf();
		protocolManager.sessionFactoryAdded(new NetconfProtocolSessionFactory(), new HashMap<String, String>() {
			{
				put(ProtocolSessionContext.PROTOCOL, "netconf");
			}
		});
		protocolSessionManager.registerContext(netconfContext);
	}

	@Test
	public void testExecute() throws ActionException {
		action.setParams(createLRModelWithAnInterface());
		ActionResponse response = action.execute(protocolSessionManager);
		assertNotNull(response);
		assertEquals(STATUS.OK, response.getStatus());
	}

	private ComputerSystem createLRModelWithAnInterface() {
		ComputerSystem model = new ComputerSystem();
		model.setElementName("LS1");

		EthernetPort port = new EthernetPort();
		port.setName("fe-0/3/0");
		port.setPortNumber(0);
		model.addLogicalDevice(port);

		return model;
	}

	private static ProtocolSessionContext newSessionContextNetconf() {
		String uri = System.getProperty("protocol.uri");
		if (uri == null || uri.equals("${protocol.uri}")) {
			uri = "mock://user:pass@host.net:2212/mocksubsystem";
		}

		// FIXME CAUTION REMOVE BEFORE COMMIT!!!!
		// NOT COMMENT IT, REMOVE IT! IT CONTAINS SENSITIVE DATA!!!!
		// uri =

		ProtocolSessionContext protocolSessionContext = new ProtocolSessionContext();

		protocolSessionContext.addParameter(
				ProtocolSessionContext.PROTOCOL_URI, uri);
		protocolSessionContext.addParameter(ProtocolSessionContext.PROTOCOL,
				"netconf");
		return protocolSessionContext;
	}
}
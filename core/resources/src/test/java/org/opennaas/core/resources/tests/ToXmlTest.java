package org.opennaas.core.resources.tests;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.opennaas.core.resources.ILifecycle.State;
import org.opennaas.core.resources.IModel;
import org.opennaas.core.resources.IResource;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.core.resources.capability.ICapability;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.core.resources.mock.MockCapabilityFactory;
import org.opennaas.core.resources.mock.MockModel;
import org.opennaas.core.resources.mock.MockResource;
import org.opennaas.core.resources.mock.MockResourceBootstrapper;

public class ToXmlTest {

	private JAXBContext	jc;
	private IResource	resource;

	@Before
	public void init() throws JAXBException, CapabilityException {
		// init JaxbContext
		jc = JAXBContext.newInstance(IResource.class);

		resource = createResource();
	}

	private IResource createResource() throws CapabilityException {

		MockResource resource = new MockResource();
		CapabilityDescriptor chassisDesc = MockResource.createCapabilityDescriptor("chassis");
		CapabilityDescriptor queueDesc = MockResource.createCapabilityDescriptor("queue");
		CapabilityDescriptor ipDesc = MockResource.createCapabilityDescriptor("ipv4");

		List<CapabilityDescriptor> descs = new ArrayList<CapabilityDescriptor>();
		descs.add(chassisDesc);
		descs.add(queueDesc);
		descs.add(ipDesc);

		resource.getResourceDescriptor().setCapabilityDescriptors(descs);

		List<ICapability> capabs = new ArrayList<ICapability>();
		for (int i = 0; i < descs.size(); i++) {
			MockCapabilityFactory fact = new MockCapabilityFactory();
			capabs.add(fact.createCapability(descs.get(i), resource.getResourceIdentifier().getId()));
		}
		resource.setCapabilities(capabs);
		resource.setBootstrapper(new MockResourceBootstrapper());

		resource.setState(State.ACTIVE);

		IModel model = new MockModel();
		resource.setModel(model);

		return resource;
	}

	@Test
	public void testSampleModel2Xml2Java() throws Exception {

		// transform to XML
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter writer = new StringWriter();
		marshaller.marshal(resource, writer);

		// Load from XML
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(writer.toString());
		IResource loaded = (IResource) unmarshaller.unmarshal(reader);

		check(resource, loaded);
	}

	private void check(IResource resource, IResource loaded) throws Exception {
		// TODO Auto-generated method stub

	}

}

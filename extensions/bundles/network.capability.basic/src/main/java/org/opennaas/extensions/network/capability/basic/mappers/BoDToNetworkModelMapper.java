package org.opennaas.extensions.network.capability.basic.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennaas.core.resources.IResource;
import org.opennaas.extensions.model.ndl.domain.NetworkDomain;
import org.opennaas.extensions.model.ndl.layer.Layer;
import org.opennaas.extensions.model.ndl.topology.ConnectionPoint;
import org.opennaas.extensions.model.ndl.topology.Interface;
import org.opennaas.extensions.model.ndl.topology.Link;
import org.opennaas.extensions.model.ndl.topology.NetworkElement;
import org.opennaas.extensions.model.ndl.topology.TransportNetworkElement;
import org.opennaas.extensions.network.model.NetworkModel;
import org.opennaas.extensions.network.model.NetworkModelHelper;

public class BoDToNetworkModelMapper {

	/**
	 * Transforms given model to a NDL representation and adds it to networkModel.
	 * 
	 * @param model
	 * @param networkModel
	 * @param name
	 *            name for the model to add
	 * @return NetworkElements created to represent given model in networkModel.
	 */
	public static List<NetworkElement> addResourceToNetworkModel(IResource resource, NetworkModel networkModel, String name) {

		NetworkModel bodModel = (NetworkModel) resource.getModel();

		NetworkModel modelCopy = copyBoDModel(bodModel);

		// add domain having all interfaces in modelCopy
		NetworkDomain domain = new NetworkDomain();
		domain.setName(resource.getResourceDescriptor().getInformation().getType() + ":" + resource.getResourceDescriptor().getInformation()
				.getName());
		domain.setHasInterface(NetworkModelHelper.getConnectionPoints(modelCopy));
		modelCopy.getNetworkElements().add(domain);

		NetworkModel toJoin = replaceLayers(modelCopy, NetworkModelHelper.getLayers(networkModel));

		List<NetworkElement> newElements = toJoin.getNetworkElements();

		joinModels(toJoin, networkModel);

		return newElements;
	}

	private static NetworkModel copyBoDModel(NetworkModel bodModel) {

		List<NetworkElement> copiedElements = copyIsolatedNetworkElements(bodModel);

		// A BoD model has interfaces, links and layers. But not devices nor domains.

		// // create associations in domains
		// for (NetworkDomain domain : NetworkModelHelper.getDomains(bodModel.getNetworkElements())) {
		//
		// NetworkDomain domainCopy = (NetworkDomain) NetworkModelHelper.getNetworkElementByName(domain.getName(), copiedElements);
		//
		// for (ConnectionPoint iface : domain.getHasInterface()) {
		// ConnectionPoint ifaceCopy = (ConnectionPoint) NetworkModelHelper.getNetworkElementByName(iface.getName(), copiedElements);
		// domainCopy.getHasInterface().add(ifaceCopy);
		// }
		// }

		// create associations in interfaces
		for (ConnectionPoint iface : NetworkModelHelper.getConnectionPoints(bodModel)) {
			ConnectionPoint ifaceCopy = (ConnectionPoint) NetworkModelHelper.getNetworkElementByName(iface.getName(), copiedElements);

			// set client and server interfaces
			for (ConnectionPoint clientInterface : iface.getClientInterfaces()) {
				ifaceCopy.getClientInterfaces().add(
						(ConnectionPoint) NetworkModelHelper.getNetworkElementByName(clientInterface.getName(), copiedElements));
			}
			if (iface.getServerInterface() != null) {
				ifaceCopy.setServerInterface((ConnectionPoint) NetworkModelHelper.getNetworkElementByName(iface.getServerInterface().getName(),
						copiedElements));
			}

			// set Layer
			if (iface.getLayer() != null)
				ifaceCopy.setLayer((Layer) NetworkModelHelper.getNetworkElementByName(iface.getLayer().getName(), copiedElements));
		}

		// create associations in links
		for (Link link : NetworkModelHelper.getLinks(bodModel.getNetworkElements())) {
			Link linkCopy = (Link) NetworkModelHelper.getNetworkElementByName(link.getName(), copiedElements);

			Interface sourceCopy = (Interface) NetworkModelHelper.getNetworkElementByName(link.getSource().getName(), copiedElements);
			Interface sinkCopy = (Interface) NetworkModelHelper.getNetworkElementByName(link.getSink().getName(), copiedElements);

			linkCopy.setSource(sourceCopy);
			linkCopy.setSink(sinkCopy);

			sourceCopy.setLinkTo(linkCopy);
			if (linkCopy.isBidirectional())
				sinkCopy.setLinkTo(linkCopy);

			// set Layer
			if (link.getLayer() != null)
				linkCopy.setLayer((Layer) NetworkModelHelper.getNetworkElementByName(link.getLayer().getName(), copiedElements));
		}

		NetworkModel copy = new NetworkModel();
		copy.setNetworkElements(copiedElements);

		// TODO copy resource references (not required for BoDToNetworkModelMapper as a BoD model does not hold references)

		return copy;
	}

	/**
	 * Replaces layers in toUpdate with equivalent ones in given layersToUse. In case there's no equivalent layer in given layersToUse for a layer in
	 * toUpdate, then toUpdate layer is not replaced.
	 * 
	 * @param toUpdate
	 * @param layersToUse
	 * @return toUpdate model having layers replaced for equivalent ones in layersToUse.
	 */
	private static NetworkModel replaceLayers(NetworkModel toUpdate, List<Layer> layersToUse) {

		// get references for each layer
		Map<String, List<NetworkElement>> layerReferences = new HashMap<String, List<NetworkElement>>();
		for (NetworkElement element : toUpdate.getNetworkElements()) {
			if (element instanceof TransportNetworkElement) {
				if (((TransportNetworkElement) element).getLayer() != null) {
					if (layerReferences.containsKey(((TransportNetworkElement) element).getLayer().getName())) {
						layerReferences.get(((TransportNetworkElement) element).getLayer().getName()).add(element);
					} else {
						List<NetworkElement> list = new ArrayList<NetworkElement>();
						list.add(element);
						layerReferences.put(((TransportNetworkElement) element).getLayer().getName(), list);
					}
				}
			}
		}

		// replace layers
		Layer replacement;
		for (String layerName : layerReferences.keySet()) {
			if ((replacement = (Layer) NetworkModelHelper.getNetworkElementByName(layerName, layersToUse)) != null) {
				// replace references to a layer called layerName with references to replacement
				for (NetworkElement element : layerReferences.get(layerName)) {
					if (element instanceof TransportNetworkElement) {
						((TransportNetworkElement) element).setLayer(replacement);
					}
				}

				// remove replaced layer from networkElements list
				Layer replaced = (Layer) NetworkModelHelper.getNetworkElementByName(layerName, toUpdate.getNetworkElements());
				toUpdate.getNetworkElements().remove(replaced);
			}
		}

		return toUpdate;
	}

	/**
	 * Joins toJoin into networkModel.
	 * 
	 * @param toJoin
	 * @param networkModel
	 */
	private static NetworkModel joinModels(NetworkModel toJoin, NetworkModel networkModel) {
		networkModel.getNetworkElements().addAll(toJoin.getNetworkElements());
		return networkModel;
	}

	private static List<NetworkElement> copyIsolatedNetworkElements(NetworkModel bodModel) {

		List<NetworkElement> copies = new ArrayList<NetworkElement>();
		for (NetworkElement element : bodModel.getNetworkElements()) {
			copies.add(copyIsolatedNetworkElement(element));
		}
		return copies;
	}

	private static NetworkElement copyIsolatedNetworkElement(NetworkElement element) {
		NetworkElement copy = null;
		try {
			// // A BoD model has interfaces, links and layers. But not devices nor domains.
			// if (element instanceof Device) {
			// copy = element.getClass().newInstance();
			// copy.setName(element.getName());
			// copy.setLocatedAt(element.getLocatedAt());
			//
			// // nothing to copy
			// // references to connection points are not copied
			//
			// } else if (element instanceof NetworkDomain) {
			// copy = element.getClass().newInstance();
			// copy.setName(element.getName());
			// copy.setLocatedAt(element.getLocatedAt());
			//
			// // nothing to copy
			// // references to devices and interfaces are not copied
			//
			// } else
			if (element instanceof ConnectionPoint) {
				copy = element.getClass().newInstance();
				copy.setName(element.getName());
				copy.setLocatedAt(element.getLocatedAt());

				// nothing to copy
				// references to layers and client/server interfaces are not copied

			} else if (element instanceof Link) {
				copy = element.getClass().newInstance();
				copy.setName(element.getName());
				copy.setLocatedAt(element.getLocatedAt());

				((Link) copy).setBidirectional(((Link) element).isBidirectional());
				// nothing else to copy
				// references to layers and source/sink interfaces are not copied

			}
		} catch (InstantiationException e) {
			// ignored. we already know NetworkElements are beans with the default constructor, and that this constructor does not throws exceptions
		} catch (IllegalAccessException e) {
			// ignored. we already know NetrworkElements are accessible and have a public nullary constructor
		}
		return copy;

	}

}

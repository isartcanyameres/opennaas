package org.opennaas.extensions.vcpe.capability;

import org.opennaas.extensions.network.model.NetworkModel;
import org.opennaas.extensions.network.model.NetworkModelHelper;
import org.opennaas.extensions.vcpe.model.Interface;

public class VCPEToBoDModelTranslator {

	public static org.opennaas.extensions.model.ndl.topology.Interface
			vCPEInterfaceToBoDInterface(Interface iface, NetworkModel model) {

		org.opennaas.extensions.model.ndl.topology.Interface netIface =
				NetworkModelHelper.getInterfaceByName(model.getNetworkElements(), iface.getName());
		return netIface;
	}

}

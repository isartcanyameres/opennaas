package org.opennaas.extensions.vcpe.manager.templates.basic.builder;

import org.opennaas.extensions.vcpe.manager.templates.builder.VCPEBuilderParameters;
import org.opennaas.extensions.vcpe.model.VCPENetworkModel;

public class BasicVCPEBuilderParameters extends VCPEBuilderParameters {

	private VCPENetworkModel	model;

	public VCPENetworkModel getModel() {
		return model;
	}

	public void setModel(VCPENetworkModel model) {
		this.model = model;
	}

}

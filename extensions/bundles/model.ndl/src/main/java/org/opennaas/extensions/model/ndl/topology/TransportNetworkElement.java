package org.opennaas.extensions.model.ndl.topology;

import org.opennaas.extensions.model.ndl.layer.Layer;

public class TransportNetworkElement extends NetworkElement {

	Layer	layer;

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}
}

package org.opennaas.extensions.model.ndl.layer;

import org.opennaas.extensions.model.ndl.topology.NetworkElement;

/**
 * A specific encoding of data in a network connection.
 * 
 * @author isart
 * 
 */
public abstract class Layer extends NetworkElement {

	/**
	 * @return the name of the layer
	 */
	@Override
	public abstract String getName();
}

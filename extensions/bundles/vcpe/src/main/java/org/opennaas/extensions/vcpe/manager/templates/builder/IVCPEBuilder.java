package org.opennaas.extensions.vcpe.manager.templates.builder;

import org.opennaas.core.resources.ResourceException;
import org.opennaas.extensions.vcpe.model.VCPENetworkModel;

public interface IVCPEBuilder {

	public VCPEBuilderResult build(VCPEBuilderParameters parameters) throws ResourceException;

	public VCPEBuilderResult unbuild(VCPENetworkModel currentModel) throws ResourceException;

}

package org.opennaas.core.queue.capability;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.queue.QueueState;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.capability.AbstractCapability;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;

public class QueueCapability extends AbstractCapability implements IQueueCapability {

	public static final String		CAPABILITY_TYPE	= "newqueue";
	private String					resourceId		= "";
	private final Vector<IAction>	queue			= new Vector<IAction>();
	private final Log				log				= LogFactory.getLog(QueueCapability.class);

	public QueueCapability(CapabilityDescriptor descriptor) {
		super(descriptor);
	}

	public QueueCapability(CapabilityDescriptor capabilityDescriptor, String resourceId) {
		super(capabilityDescriptor);
		this.resourceId = resourceId;
		log.debug("Built new Queue Capability");
	}

	@Override
	public String getCapabilityName() {
		return this.CAPABILITY_TYPE;
	}

	@Override
	public List<IAction> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAction removeAction(IAction action) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() throws IllegalStateException {

		while (!queue.isEmpty())
			queue.remove(0);
	}

	@Override
	public ExecutionId begin() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionId abort() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionResult blockingBegin() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionResult blockingCommit() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutionResult blockingAbort() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActionSet getActionSet() throws CapabilityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void queueAction(IAction arg0) throws CapabilityException {
		// TODO Auto-generated method stub

	}

	@Override
	public QueueState getQueueState() {
		return null;
	}

}
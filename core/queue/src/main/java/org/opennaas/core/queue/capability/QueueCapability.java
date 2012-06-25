package org.opennaas.core.queue.capability;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opennaas.core.queue.Activator;
import org.opennaas.core.queue.QueueState;
import org.opennaas.core.queue.engine.EngineState;
import org.opennaas.core.queue.impl.engine.QueueExecutionEngine;
import org.opennaas.core.queue.repository.ExecutionId;
import org.opennaas.core.queue.repository.ExecutionResult;
import org.opennaas.core.resources.ActivatorException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.core.resources.action.IActionSet;
import org.opennaas.core.resources.capability.AbstractCapability;
import org.opennaas.core.resources.capability.CapabilityException;
import org.opennaas.core.resources.descriptor.CapabilityDescriptor;
import org.opennaas.core.resources.descriptor.ResourceDescriptorConstants;

public class QueueCapability extends AbstractCapability implements IQueueCapability, IExtendedQueueCapability {

	public static final String			CAPABILITY_TYPE	= "newqueue";
	private String						resourceId		= "";
	private ExecutionId					executionID		= null;
	private final ArrayList<IAction>	queue			= new ArrayList<IAction>();
	private QueueExecutionEngine		qExecEngine		= new QueueExecutionEngine();
	private final Log					log				= LogFactory.getLog(QueueCapability.class);
	private QueueState					queueState		= QueueState.EMPTY;

	public QueueCapability(CapabilityDescriptor descriptor) {
		super(descriptor);
	}

	public QueueCapability(CapabilityDescriptor capabilityDescriptor, String resourceId) {
		super(capabilityDescriptor);
		this.resourceId = resourceId;
		this.queueState = QueueState.EMPTY;
		log.debug("Built new Queue Capability");
	}

	private void registerExecutionEvents() {
		String topic = "/org/opennaas/core/queue/exec/FINISHED";

		// String properties = buildPropertiesFilter();
		// EventFilter filter = new EventFilter(topic, );
		// TODO FINISH
	}

	@Override
	public String getCapabilityName() {
		return this.CAPABILITY_TYPE;
	}

	@Override
	public List<IAction> getActions() {
		log.info("Start of getActions call");
		log.debug("Get actions");
		List<IAction> actions = new ArrayList<IAction>();
		for (IAction action : queue) {
			actions.add(action);
		}
		log.info("End of getActions call");
		return actions;
	}

	@Override
	public IAction removeAction(IAction action) throws IllegalStateException, Exception {

		log.debug("Trying to remove " + action.getActionID() + "action.");
		if (!getQueueState().equals(QueueState.FILLED))
			throw new IllegalStateException("Can't remove an action in the current queue state.");
		int index = 0;
		while (!queue.get(index).equals(action))
			index++;

		try {
			queue.remove(index);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new CapabilityException("Action couldn't be removed since it doesn't exist in que current queue.");

		}
		queueState = getQueueState();

		return action;
	}

	@Override
	public void clear() throws IllegalStateException {

		log.debug("Trying to clear the queue.");
		while (!queue.isEmpty())
			queue.remove(0);
		// reset executionID
		executionID = null;
		log.debug("Queue cleared.");
	}

	@Override
	public ExecutionId begin() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.FILLED))
			throw new IllegalStateException("Can't begin queue execution at the current state.");
		// TODO who is responsable of changing the Queu state?
		executionID = qExecEngine.submit(queue);
		registerExecutionEvents();
		qExecEngine.begin();

		return executionID;
	}

	@Override
	public ExecutionId commit() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.EXECUTING))
			throw new IllegalStateException("Can't commit queue execution at the current state.");
		qExecEngine.commit();
		return executionID;
	}

	@Override
	public ExecutionId abort() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.EXECUTING))
			throw new IllegalStateException("Can't commit queue execution at the current state.");
		qExecEngine.abort();
		return executionID;
	}

	@Override
	public ExecutionResult blockingBegin() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.FILLED))
			throw new IllegalStateException("Can't begin queue execution at the current state.");
		executionID = qExecEngine.submit(queue);
		ExecutionResult execResult = qExecEngine.blockingBegin();
		return execResult;
	}

	@Override
	public ExecutionResult blockingCommit() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.EXECUTING))
			throw new IllegalStateException("Can't commit queue execution at the current state.");
		executionID = qExecEngine.submit(queue);
		ExecutionResult execResult = qExecEngine.blockingBegin();
		return execResult;
	}

	@Override
	public ExecutionResult blockingAbort() throws IllegalStateException {
		if (!getQueueState().equals(QueueState.EXECUTING))
			throw new IllegalStateException("Can't commit queue execution at the current state.");
		executionID = qExecEngine.submit(queue);
		ExecutionResult execResult = qExecEngine.blockingAbort();
		return execResult;

	}

	@Override
	public IActionSet getActionSet() throws CapabilityException {
		String name = this.descriptor
				.getPropertyValue(ResourceDescriptorConstants.ACTION_NAME);
		String protocol = this.descriptor
				.getPropertyValue(ResourceDescriptorConstants.ACTION_PROTOCOL);
		String version = this.descriptor
				.getPropertyValue(ResourceDescriptorConstants.ACTION_VERSION);

		try {
			return Activator.getQueueActionSet(name, version, protocol);
		} catch (ActivatorException e) {
			throw new CapabilityException(e.getLocalizedMessage());
		}
	}

	@Override
	public void queueAction(IAction newAction) throws CapabilityException {

		log.info("Start of queueAction call");
		log.debug("Queue new action");
		queue.add(newAction);
		log.info("End of queueAction call");

	}

	@Override
	public QueueState getQueueState() {
		EngineState engineState = qExecEngine.getState();
		if (engineState.equals(EngineState.FREE)) {
			if (queue.isEmpty())
				return QueueState.EMPTY;
			else
				return QueueState.FILLED;
		}
		else
			return QueueState.EXECUTING;
	}

	@Override
	public ExecutionResult execute() {
		ExecutionResult execResult = new ExecutionResult();
		try {
			execResult = blockingBegin();
			execResult = blockingCommit();
			execResult = blockingAbort();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return execResult;
	}

	@Override
	public void add(IAction action) throws CapabilityException, IllegalStateException {

		if (getQueueState().equals(QueueState.EXECUTING))
			throw new IllegalStateException("Can't add an action while executing the queue.");

		// TODO where to check params ?

		queueAction(action);

		queueState = getQueueState();
	}
}
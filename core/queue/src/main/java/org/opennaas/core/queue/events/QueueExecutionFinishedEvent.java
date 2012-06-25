package org.opennaas.core.queue.events;

import java.util.Map;

import org.osgi.service.event.Event;

public class QueueExecutionFinishedEvent extends Event {

	private static final String	TOPIC	= "org/opennaas/core/queue/exec/FINISHED";

	public QueueExecutionFinishedEvent(Map<String, String> properties) {
		super(TOPIC, properties);
	}

}

package org.opennaas.core.queue.engine;

public enum EngineState {
	FREE,
	BEGINNING,
	PREPARING,
	RUNNING_ACTIONS,
	VALIDATING,
	BEGIN_ERROR,
	BEGIN_OK,
	COMMITING,
	ABORTING,
	ERROR;
}

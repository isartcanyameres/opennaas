package org.opennaas.core.queue;

public enum QueueState {
	EMPTY,
	FILLED,
	EXECUTING,
	// BEGGINING,
	// BEGIN_ERROR,
	// BEGIN_OK,
	// COMMITING,

	// COMMIT_ERROR,
	// COMMIT_OK, --> EMPTY
	// ABORTING,
	// ABORT_ERROR;
	// ABORT_OK --> FILLED
}

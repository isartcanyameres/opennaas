package org.opennaas.core.queue.impl.engine;

import org.opennaas.core.queue.engine.EngineLockedException;
import org.opennaas.core.queue.engine.ICompositeExecutionEngine;
import org.opennaas.core.queue.engine.IExecutionEngine;
import org.opennaas.core.queue.transaction.ITransaction;
import org.opennaas.core.queue.transaction.ITransactionWrapper;

public class ExecutionEngine implements IExecutionEngine, ICompositeExecutionEngine {

	@Override
	public ITransactionWrapper submitCoordinated(ITransaction tx) throws EngineLockedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void submit(ITransaction tx) throws EngineLockedException {
		// TODO Auto-generated method stub

	}

}

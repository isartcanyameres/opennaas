package org.opennaas.extensions.roadm.capability.connections.actionset;

import org.opennaas.core.resources.action.ActionException;
import org.opennaas.core.resources.action.IAction;
import org.opennaas.extensions.router.model.opticalSwitch.FiberConnection;

/**
 * 
 * This interface aims to simplify the creation of actions since the methods (signature) of the drivers are already known top level usage IAction =
 * getDriver().getConnectionsCapabActionFactory().createAction1();
 * 
 */
public interface IConnectionsCapabilityActionFactory {

	IAction createMakeConnectionAction(FiberConnection p1) throws ActionException;

	IAction createRemoveConectionAction(FiberConnection p1) throws ActionException;

	IAction createRefreshModelConnectionsAction(FiberConnection p1) throws ActionException;

}

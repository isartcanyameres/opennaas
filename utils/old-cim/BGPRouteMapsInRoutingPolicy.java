/**
 * This file was auto-generated by mofcomp -j version 1.0.0 on Wed Jan 12
 * 09:21:06 CET 2011.
 */

package org.opennaas.extensions.router.model;

import java.io.*;
import java.lang.Exception;

/**
 * This Class contains accessor and mutator methods for all properties defined in the CIM class BGPRouteMapsInRoutingPolicy as well as methods
 * comparable to the invokeMethods defined for this class. This Class implements the BGPRouteMapsInRoutingPolicyBean Interface. The CIM class
 * BGPRouteMapsInRoutingPolicy is described as follows:
 * 
 * This aggregation defines the BGPRouteMaps that are used by a particular RoutingPolicy object. Multiple instances of the same RouteMap may be used
 * in the same RoutingPolicy instance. If this is desired, then the Sequence attribute of this aggregation can be used to disambiguate them.
 */
public class BGPRouteMapsInRoutingPolicy extends Component implements
		Serializable {

	/**
	 * This constructor creates a BGPRouteMapsInRoutingPolicyBeanImpl Class which implements the BGPRouteMapsInRoutingPolicyBean Interface, and
	 * encapsulates the CIM class BGPRouteMapsInRoutingPolicy in a Java Bean. The CIM class BGPRouteMapsInRoutingPolicy is described as follows:
	 * 
	 * This aggregation defines the BGPRouteMaps that are used by a particular RoutingPolicy object. Multiple instances of the same RouteMap may be
	 * used in the same RoutingPolicy instance. If this is desired, then the Sequence attribute of this aggregation can be used to disambiguate them.
	 */
	public BGPRouteMapsInRoutingPolicy() {
	};

	/**
	 * This method create an Association of the type BGPRouteMapsInRoutingPolicy between one RoutingPolicy object and BGPRouteMap object
	 */
	public static BGPRouteMapsInRoutingPolicy link(RoutingPolicy
			groupComponent, BGPRouteMap partComponent) {

		return (BGPRouteMapsInRoutingPolicy) Association.link(BGPRouteMapsInRoutingPolicy.class, groupComponent, partComponent);
	}// link

	/**
	 * The following constants are defined for use with the ValueMap/Values qualified property mapSequence.
	 */
	private int	mapSequence;

	/**
	 * This method returns the BGPRouteMapsInRoutingPolicy.mapSequence property value. This property is described as follows:
	 * 
	 * This defines the position of this RouteMap instance relative to all other instances of the same RouteMap.
	 * 
	 * @return int current mapSequence property value
	 * @exception Exception
	 */
	public int getMapSequence() {

		return this.mapSequence;
	} // getMapSequence

	/**
	 * This method sets the BGPRouteMapsInRoutingPolicy.mapSequence property value. This property is described as follows:
	 * 
	 * This defines the position of this RouteMap instance relative to all other instances of the same RouteMap.
	 * 
	 * @param int new mapSequence property value
	 * @exception Exception
	 */
	public void setMapSequence(int mapSequence) {

		this.mapSequence = mapSequence;
	} // setMapSequence

} // Class BGPRouteMapsInRoutingPolicy

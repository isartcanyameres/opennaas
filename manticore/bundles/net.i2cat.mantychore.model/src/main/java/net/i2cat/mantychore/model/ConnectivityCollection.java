/**
 * This file was auto-generated by mofcomp -j version 1.0.0 on Wed Jan 12
 * 09:21:06 CET 2011.
 */

package net.i2cat.mantychore.model;

import java.io.*;
import java.lang.Exception;

/**
 * This Class contains accessor and mutator methods for all properties defined
 * in the CIM class ConnectivityCollection as well as methods comparable to
 * the invokeMethods defined for this class. This Class implements the
 * ConnectivityCollectionBean Interface. The CIM class ConnectivityCollection
 * is described as follows:
 *
 * A ConnectivityCollection groups together a set of ProtocolEndpoints of the
 * same 'type' (i.e., class) which are able to communicate with each other.
 * It may also group related Systems, users or other ManagedElements. A
 * ConnectivityCollection represents the ability to send and/or receive data
 * over a set of ProtocolEndpoints. The collection is defined in the context
 * of an AdminDomain or scoping ComputerSystem. This is mandated by the
 * Hosted Collection association on the SystemSpecificCollection superclass.
 * Note that the entities aggregated into the Collection are specified using
 * the association, MemberOfCollection.
 */
public class ConnectivityCollection extends SystemSpecificCollection
    implements Serializable {

    /**
     * This constructor creates a ConnectivityCollectionBeanImpl Class which
     * implements the ConnectivityCollectionBean Interface, and encapsulates
     * the CIM class ConnectivityCollection in a Java Bean. The CIM class
     * ConnectivityCollection is described as follows:
     *
     * A ConnectivityCollection groups together a set of ProtocolEndpoints of
     * the same 'type' (i.e., class) which are able to communicate with each
     * other. It may also group related Systems, users or other
     * ManagedElements. A ConnectivityCollection represents the ability to
     * send and/or receive data over a set of ProtocolEndpoints. The
     * collection is defined in the context of an AdminDomain or scoping
     * ComputerSystem. This is mandated by the Hosted Collection association
     * on the SystemSpecificCollection superclass. Note that the entities
     * aggregated into the Collection are specified using the association,
     * MemberOfCollection.
     */
    public ConnectivityCollection(){};
    /**
     * The following constants are defined for use with the ValueMap/Values
     * qualified property ConnectivityStatus.
     */

    public enum ConnectivityStatus{
    UNKNOWN,
    CONNECTIVITY_UP,
    NO_CONNECTIVITY_DOWN,
    PARTITIONED
    }
    private ConnectivityStatus connectivityStatus;
    /**
     * This method returns the ConnectivityCollection.connectivityStatus
     * property value. This property is described as follows:
     *
     * An enumeration describing the current or potential connectivity between
     * endpoints in this collection. Connectivity may be provided or not, or
     * may be in a degraded/partitioned state if one or more endpoints or
     * links have failed. The latter would prevent full connectivity between
     * all elements in the Collection, but would permit connectivity between
     * subsets.
     *
     * @return	int	current connectivityStatus property value
     * @exception	Exception
     */
    public ConnectivityStatus getConnectivityStatus(){

    return this.connectivityStatus;
    } // getConnectivityStatus

    /**
     * This method sets the ConnectivityCollection.connectivityStatus property
     * value. This property is described as follows:
     *
     * An enumeration describing the current or potential connectivity between
     * endpoints in this collection. Connectivity may be provided or not, or
     * may be in a degraded/partitioned state if one or more endpoints or
     * links have failed. The latter would prevent full connectivity between
     * all elements in the Collection, but would permit connectivity between
     * subsets.
     *
     * @param	int	new connectivityStatus property value
     * @exception	Exception
     */
    public void setConnectivityStatus(ConnectivityStatus
	connectivityStatus){

    this.connectivityStatus = connectivityStatus;
    } // setConnectivityStatus



} // Class ConnectivityCollection
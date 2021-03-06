/**
 * This file was auto-generated by mofcomp -j version 1.0.0 on Wed Jan 12
 * 09:21:06 CET 2011.
 */

package org.opennaas.extensions.router.model;

import java.io.*;

/**
 * This Class contains accessor and mutator methods for all properties defined in the CIM class CollectedCollections as well as methods comparable to
 * the invokeMethods defined for this class. This Class implements the CollectedCollectionsBean Interface. The CIM class CollectedCollections is
 * described as follows:
 * 
 * CIM_CollectedCollections is an aggregation association representing that a CollectionOfMSEs can itself be contained in a CollectionOfMSEs.
 */
public class CollectedCollections extends Association implements Serializable
{

	/**
	 * This constructor creates a CollectedCollectionsBeanImpl Class which implements the CollectedCollectionsBean Interface, and encapsulates the CIM
	 * class CollectedCollections in a Java Bean. The CIM class CollectedCollections is described as follows:
	 * 
	 * CIM_CollectedCollections is an aggregation association representing that a CollectionOfMSEs can itself be contained in a CollectionOfMSEs.
	 */
	public CollectedCollections() {
	};

	/**
	 * This method create an Association of the type CollectedCollections between one CollectionOfMSEs object and CollectionOfMSEs object
	 */
	public static CollectedCollections link(CollectionOfMSEs
			collection, CollectionOfMSEs collectionInCollection) {

		return (CollectedCollections) Association.link(CollectedCollections.class, collection, collectionInCollection);
	}// link

} // Class CollectedCollections

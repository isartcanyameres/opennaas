/**
 * This file was auto-generated by mofcomp -j version 1.0.0 on Wed Jan 12
 * 09:21:06 CET 2011.
 */

package org.opennaas.extensions.router.model;

import java.io.*;

/**
 * This Class contains accessor and mutator methods for all properties defined in the CIM class FilterOfSecurityAssociation as well as methods
 * comparable to the invokeMethods defined for this class. This Class implements the FilterOfSecurityAssociationBean Interface. The CIM class
 * FilterOfSecurityAssociation is described as follows:
 * 
 * FilterOfSecurityAssociation associates a network traffic specification (i.e., a FilterList) with a SecurityAssociation Endpoint, to which the
 * filter list applies.
 */
public class FilterOfSecurityAssociation extends Dependency implements
		Serializable {

	/**
	 * This constructor creates a FilterOfSecurityAssociationBeanImpl Class which implements the FilterOfSecurityAssociationBean Interface, and
	 * encapsulates the CIM class FilterOfSecurityAssociation in a Java Bean. The CIM class FilterOfSecurityAssociation is described as follows:
	 * 
	 * FilterOfSecurityAssociation associates a network traffic specification (i.e., a FilterList) with a SecurityAssociation Endpoint, to which the
	 * filter list applies.
	 */
	public FilterOfSecurityAssociation() {
	};

	/**
	 * This method create an Association of the type FilterOfSecurityAssociation between one FilterList object and SecurityAssociationEndpoint object
	 */
	public static FilterOfSecurityAssociation link(FilterList
			antecedent, SecurityAssociationEndpoint dependent) {

		return (FilterOfSecurityAssociation) Association.link(FilterOfSecurityAssociation.class, antecedent, dependent);
	}// link

} // Class FilterOfSecurityAssociation
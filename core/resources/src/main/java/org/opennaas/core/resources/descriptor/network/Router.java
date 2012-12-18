package org.opennaas.core.resources.descriptor.network;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlSeeAlso;

@Entity
@XmlSeeAlso(LogicalRouter.class)
public class Router extends Device {

}

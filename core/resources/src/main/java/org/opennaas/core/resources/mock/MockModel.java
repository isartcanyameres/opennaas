package org.opennaas.core.resources.mock;

import java.util.ArrayList;
import java.util.List;

import org.opennaas.core.resources.IModel;

public class MockModel implements IModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3487020438954499667L;

	private String				name;
	private List<String>		alias;

	private List<MockModel>		realChildren		= new ArrayList<MockModel>();

	@Override
	public List<String> getChildren() {

		List<String> childrenNames = new ArrayList<String>();
		for (MockModel child : realChildren) {
			childrenNames.add(child.getName());
		}
		return childrenNames;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAlias() {
		return alias;
	}

	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

	public List<MockModel> getRealChildren() {
		return realChildren;
	}

	public void setRealChildren(List<MockModel> realChildren) {
		this.realChildren = realChildren;
	}

}

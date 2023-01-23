package org.processmining.plugins;

import java.util.List;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;


public class CompositionParameter extends PluginParametersImpl{
	private List<Object> listParam;
	
	

	public CompositionParameter(CompositionParameter parameters) {
		super(parameters);
		
	}
	
	public boolean equals(Object object) {
		if (object instanceof CompositionParameter) {
			CompositionParameter parameters = (CompositionParameter) object;
			return super.equals(parameters);
			
		}
		return false;
	}

	public List<Object> getListParam() {
		return listParam;
	}

	public void setListParam(List<Object> listParam) {
		this.listParam = listParam;
	}
	
	public void addListParam(Object obj, int index) {
		this.listParam.add(index, obj);;
	}
	
	public void deleteListParam(int index) {
		this.listParam.remove(index);
	}
	
}

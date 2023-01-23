package org.processmining.plugins.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;

public class ScaleLogParameter extends PluginParametersImpl {

	private List<HashMap<String, ScaleLogParameterAbstract>> listComponents;
	private List<String> listComponentTypes;
	private int seed;
	private String defaultTimeDuration;

	public ScaleLogParameter() {
		super();
		this.listComponents = new ArrayList<HashMap<String, ScaleLogParameterAbstract>>();
		this.listComponentTypes = new ArrayList<String>();
		this.seed = 1;
	}

	public void addListComponents(HashMap<String, ScaleLogParameterAbstract> compMap, int index) {
		this.listComponents.add(index, compMap);
	}

	public void deleteListComponents(int index) {
		this.listComponents.remove(index);
	}

	public List<HashMap<String, ScaleLogParameterAbstract>> getListComponents() {
		return listComponents;
	}

	public void setListComponents(List<HashMap<String, ScaleLogParameterAbstract>> listComponents) {
		this.listComponents = listComponents;
	}
	
	public void addListComponentTypes(String type, int index) {
		this.listComponentTypes.add(index, type);
	}
	
	public void replaceListComponentTypes(String type, int index) {
		this.listComponentTypes.set(index, type);
	}
	
	public void deleteListComponentTypes(int index) {
		this.listComponentTypes.remove(index);
	}
	
	public List<String> getListComponentTypes(){
		return this.listComponentTypes;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public String getDefaultTimeDuration() {
		return defaultTimeDuration;
	}

	public void setDefaultTimeDuration(String defaultTimeDuration) {
		this.defaultTimeDuration = defaultTimeDuration;
	}
	
	
	
}

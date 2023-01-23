package org.processmining.plugins.parameter;

public class ChoiceParameter extends ScaleLogParameterAbstract {
	private int k;
	private String timeOffset;
	private String extension;
	
	public ChoiceParameter(int k, String timeOffset, String extension) {
		this.k = k;
		this.timeOffset = timeOffset;
		this.extension = extension;
	}

	public ChoiceParameter(String timeOffset) {
		this.k = 0;
		this.timeOffset = timeOffset;
		this.extension = "_";
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public String getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(String timeOffset) {
		this.timeOffset = timeOffset;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	
}

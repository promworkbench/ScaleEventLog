package org.processmining.plugins;

import javax.swing.JPanel;

import org.processmining.plugins.parameter.ScaleLogParameter;

public interface ParameterPanel {
	JPanel create(final ScaleLogParameter param);
}

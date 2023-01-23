package org.processmining.plugins;

import javax.swing.JPanel;

import org.processmining.plugins.dialogs.ChoiceParameterPanel;
import org.processmining.plugins.dialogs.LoopParameterPanel;
import org.processmining.plugins.dialogs.ParallelParameterPanel;
import org.processmining.plugins.dialogs.SequentialParameterPanel;
import org.processmining.plugins.parameter.ScaleLogParameter;

public class ParameterPanelFactory {
	public JPanel getPanel(String type, int index, final ScaleLogParameter param) {
		if (type.equals("Sequential")) {
			return new SequentialParameterPanel(index).create(param);
		} else if (type.equals("Parallel")) {
			return new ParallelParameterPanel(index).create(param);
		} else if (type.equals("Choice")) {
			return new ChoiceParameterPanel(index).create(param);
		} else if (type.equals("Loop")) {
			return new LoopParameterPanel(index).create(param);
		} else {
			return null;
		}
		
	}
}

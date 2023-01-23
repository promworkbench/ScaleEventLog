package org.processmining.plugins.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.processmining.plugins.ParameterPanel;
import org.processmining.plugins.algorithms.NumericTextFieldFilter;
import org.processmining.plugins.parameter.LoopParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;

public class LoopParameterPanel implements ParameterPanel {
	private int compIndex;
	private LoopParameter loopObject;
	private JTextField inputLoopK;
	private JTextField inputLoopTime;
	private JTextField inputLoopExtension;
	
	public LoopParameterPanel(int compIndex) {
		this.compIndex = compIndex;
	}
	public JPanel create(final ScaleLogParameter param) {
		//UI
		JPanel panel = new JPanel();
		this.loopObject = new LoopParameter(param.getDefaultTimeDuration());
		{
			
			JLabel l3 = new JLabel("K");
			panel.add(l3);
			inputLoopK = new JTextField("", 5);
			PlainDocument doc = (PlainDocument) inputLoopK.getDocument();
			doc.setDocumentFilter(new NumericTextFieldFilter());
			panel.add(inputLoopK);

			l3 = new JLabel("Time offset");
			panel.add(l3);
			inputLoopTime = new JTextField(param.getDefaultTimeDuration(), 5);
			panel.add(inputLoopTime);

			l3 = new JLabel("Extension");
			panel.add(l3);
			inputLoopExtension = new JTextField("_", 5);
			panel.add(inputLoopExtension);
			
		}

		// Controller
		{
			inputLoopK.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					if (inputLoopK.getText().equals("")) {
						setloopObjectK(0);
					} else {
						setloopObjectK(Integer.parseInt(inputLoopK.getText()));
					}
					
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}

				public void changedUpdate(DocumentEvent e) {
					if (inputLoopK.getText().equals("")) {
						setloopObjectK(0);
					} else {
						setloopObjectK(Integer.parseInt(inputLoopK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}

				public void removeUpdate(DocumentEvent e) {
					if (inputLoopK.getText().equals("")) {
						setloopObjectK(0);
					} else {
						setloopObjectK(Integer.parseInt(inputLoopK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}
			});
			inputLoopTime.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setloopObjectOffset(inputLoopTime.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setloopObjectOffset(inputLoopTime.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);

				}

				public void removeUpdate(DocumentEvent e) {
					setloopObjectOffset(inputLoopTime.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}
			});
			inputLoopExtension.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setloopObjectExtension(inputLoopExtension.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setloopObjectExtension(inputLoopExtension.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}

				public void removeUpdate(DocumentEvent e) {
					setloopObjectExtension(inputLoopExtension.getText());
					param.getListComponents().get(compIndex).replace("Loop", loopObject);
				}
			});
		}
		return panel;

	}
	public LoopParameter getLoopObject() {
		return loopObject;
	}

	public void setloopObjectK(int k) {
		this.loopObject.setK(k);
	}

	public void setloopObjectOffset(String offset) {
		this.loopObject.setTimeOffset(offset);
		;
	}

	public void setloopObjectExtension(String extension) {
		this.loopObject.setExtension(extension);
	}

	public JTextField getInputSeqK() {
		return inputLoopK;
	}

	public JTextField getInputSeqTime() {
		return inputLoopTime;
	}

	public JTextField getInputSeqExtension() {
		return inputLoopExtension;
	}

}

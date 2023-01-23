package org.processmining.plugins.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.processmining.plugins.ParameterPanel;
import org.processmining.plugins.algorithms.NumericTextFieldFilter;
import org.processmining.plugins.parameter.ParallelParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;

public class ParallelParameterPanel implements ParameterPanel {
	private int compIndex;
	private ParallelParameter parallelObject;
	private JTextField inputParallelK;
	private JTextField inputParallelTime;
	private JTextField inputParallelExtension;

	public ParallelParameterPanel(int compIndex) {
		this.compIndex = compIndex;
	}

	public JPanel create(final ScaleLogParameter param) {
		JPanel panel = new JPanel();
		this.parallelObject = new ParallelParameter(param.getDefaultTimeDuration());
		// UI
		{
			JLabel l3 = new JLabel("K");
			panel.add(l3);
			inputParallelK = new JTextField("", 5);
			PlainDocument doc = (PlainDocument) inputParallelK.getDocument();
			doc.setDocumentFilter(new NumericTextFieldFilter());
			panel.add(inputParallelK);
			
			l3 = new JLabel("Time offset");
			panel.add(l3);
			inputParallelTime = new JTextField(param.getDefaultTimeDuration(), 5);
			panel.add(inputParallelTime);

			l3 = new JLabel("Extension");
			panel.add(l3);
			inputParallelExtension = new JTextField("_", 5);
			panel.add(inputParallelExtension);
			
		}

		// Controller
		{
			inputParallelK.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { 
					if (inputParallelK.getText().equals("")) {
						setParallelObjectK(0);
					} else {
						setParallelObjectK(Integer.parseInt(inputParallelK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}

				public void changedUpdate(DocumentEvent e) {
					if (inputParallelK.getText().equals("")) {
						setParallelObjectK(0);
					} else {
						setParallelObjectK(Integer.parseInt(inputParallelK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}

				public void removeUpdate(DocumentEvent e) {
					if (inputParallelK.getText().equals("")) {
						setParallelObjectK(0);
					} else {
						setParallelObjectK(Integer.parseInt(inputParallelK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}
			});
			inputParallelTime.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setParallelObjectOffset(inputParallelTime.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setParallelObjectOffset(inputParallelTime.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);

				}

				public void removeUpdate(DocumentEvent e) {
					setParallelObjectOffset(inputParallelTime.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}
			});
			inputParallelExtension.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setParallelObjectExtension(inputParallelExtension.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setParallelObjectExtension(inputParallelExtension.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}

				public void removeUpdate(DocumentEvent e) {
					setParallelObjectExtension(inputParallelExtension.getText());
					param.getListComponents().get(compIndex).replace("Parallel", parallelObject);
				}
			});
		}

		return panel;

	}

	public ParallelParameter getParallelObject() {
		return parallelObject;
	}

	public void setParallelObjectK(int k) {
		this.parallelObject.setK(k);
	}

	public void setParallelObjectOffset(String offset) {
		this.parallelObject.setTimeOffset(offset);
		;
	}

	public void setParallelObjectExtension(String extension) {
		this.parallelObject.setExtension(extension);
	}

	public JTextField getInputSeqK() {
		return inputParallelK;
	}

	public JTextField getInputSeqTime() {
		return inputParallelTime;
	}

	public JTextField getInputSeqExtension() {
		return inputParallelExtension;
	}
}

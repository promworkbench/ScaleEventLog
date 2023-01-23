package org.processmining.plugins.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.processmining.plugins.ParameterPanel;
import org.processmining.plugins.algorithms.NumericTextFieldFilter;
import org.processmining.plugins.parameter.ChoiceParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;

public class ChoiceParameterPanel implements ParameterPanel {
	private int compIndex;
	private ChoiceParameter choiceObject;
	private JTextField inputChoiceK;
	private JTextField inputChoiceTime;
	private JTextField inputChoiceExtension;

	public ChoiceParameterPanel(int compIndex) {
		this.compIndex = compIndex;
	}

	public JPanel create(final ScaleLogParameter param) {

		JPanel panel = new JPanel();
		this.choiceObject = new ChoiceParameter(param.getDefaultTimeDuration());
		// UI
		{
			JLabel l3 = new JLabel("K");
			panel.add(l3);
			inputChoiceK = new JTextField("", 5);
			PlainDocument doc = (PlainDocument) inputChoiceK.getDocument();
			doc.setDocumentFilter(new NumericTextFieldFilter());
			panel.add(inputChoiceK);

			l3 = new JLabel("Time offset");
			panel.add(l3);
			inputChoiceTime = new JTextField(param.getDefaultTimeDuration(), 5);
			panel.add(inputChoiceTime);

			l3 = new JLabel("Extension");
			panel.add(l3);
			inputChoiceExtension = new JTextField("_", 5);
			panel.add(inputChoiceExtension);
		}

		// Controller
		{
			inputChoiceK.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					if (inputChoiceK.getText().equals("")) {
						setChoiceObjectK(0);
					} else {
						setChoiceObjectK(Integer.parseInt(inputChoiceK.getText()));
					}
					
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);
				}

				public void changedUpdate(DocumentEvent e) {
					if (inputChoiceK.getText().equals("")) {
						setChoiceObjectK(0);
					} else {
						setChoiceObjectK(Integer.parseInt(inputChoiceK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);
				}

				public void removeUpdate(DocumentEvent e) {
					if (inputChoiceK.getText().equals("")) {
						setChoiceObjectK(0);
					} else {
						setChoiceObjectK(Integer.parseInt(inputChoiceK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);
				}
			});
			inputChoiceTime.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setChoiceObjectOffset(inputChoiceTime.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setChoiceObjectOffset(inputChoiceTime.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);

				}

				public void removeUpdate(DocumentEvent e) {
					setChoiceObjectOffset(inputChoiceTime.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);

				}
			});
			inputChoiceExtension.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setChoiceObjectExtension(inputChoiceExtension.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setChoiceObjectExtension(inputChoiceExtension.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);

				}

				public void removeUpdate(DocumentEvent e) {
					setChoiceObjectExtension(inputChoiceExtension.getText());
					param.getListComponents().get(compIndex).replace("Choice", choiceObject);

				}
			});
		}

		return panel;

	}

	public ChoiceParameter getChoiceObject() {
		return choiceObject;
	}

	public void setChoiceObjectK(int k) {
		this.choiceObject.setK(k);
	}

	public void setChoiceObjectOffset(String offset) {
		this.choiceObject.setTimeOffset(offset);
		;
	}

	public void setChoiceObjectExtension(String extension) {
		this.choiceObject.setExtension(extension);
	}

	public JTextField getInputSeqK() {
		return inputChoiceK;
	}

	public JTextField getInputSeqTime() {
		return inputChoiceTime;
	}

	public JTextField getInputSeqExtension() {
		return inputChoiceExtension;
	}

}

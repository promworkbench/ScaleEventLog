package org.processmining.plugins.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.processmining.plugins.ParameterPanel;
import org.processmining.plugins.algorithms.NumericTextFieldFilter;
import org.processmining.plugins.parameter.ScaleLogParameter;
import org.processmining.plugins.parameter.SequentialParameter;

public class SequentialParameterPanel implements ParameterPanel {
	private int compIndex;
	private SequentialParameter seqObject;
	private JTextField inputSeqK;
	private JTextField inputSeqTime;
	private JTextField inputSeqExtension;

	public SequentialParameterPanel(int index) {
		this.compIndex = index;
	}

	public JPanel create(final ScaleLogParameter param) {
		this.seqObject = new SequentialParameter(param.getDefaultTimeDuration());
		JPanel panel = new JPanel();

		// UI
		{
			JLabel l3 = new JLabel("K");
			panel.add(l3);
			inputSeqK = new JTextField("", 5);
			PlainDocument doc = (PlainDocument) inputSeqK.getDocument();
			doc.setDocumentFilter(new NumericTextFieldFilter());
			panel.add(inputSeqK);
			
			l3 = new JLabel("Time offset");
			panel.add(l3);
			inputSeqTime = new JTextField(param.getDefaultTimeDuration(), 5);
			panel.add(inputSeqTime);

			l3 = new JLabel("Extension");
			panel.add(l3);
			inputSeqExtension = new JTextField("_", 5);
			panel.add(inputSeqExtension);
			
		}

		// Controller
		{
			inputSeqK.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					if (inputSeqK.getText().equals("")) {
						setSeqObjectK(0);
					} else {
						setSeqObjectK(Integer.parseInt(inputSeqK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void changedUpdate(DocumentEvent e) {
					if (inputSeqK.getText().equals("")) {
						setSeqObjectK(0);
					} else {
						setSeqObjectK(Integer.parseInt(inputSeqK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void removeUpdate(DocumentEvent e) {
					if (inputSeqK.getText().equals("")) {
						setSeqObjectK(0);
					} else {
						setSeqObjectK(Integer.parseInt(inputSeqK.getText()));
					}
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}
			});
			inputSeqTime.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setSeqObjectOffset(inputSeqTime.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setSeqObjectOffset(inputSeqTime.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void removeUpdate(DocumentEvent e) {
					setSeqObjectOffset(inputSeqTime.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}
			});
			inputSeqExtension.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					setSeqObjectExtension(inputSeqExtension.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void changedUpdate(DocumentEvent e) {
					setSeqObjectExtension(inputSeqExtension.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}

				public void removeUpdate(DocumentEvent e) {
					setSeqObjectExtension(inputSeqExtension.getText());
					param.getListComponents().get(compIndex).replace("Sequential", seqObject);
				}
			});
		}
		return panel;

	}

	public SequentialParameter getSeqObject() {
		return seqObject;
	}

	public void setSeqObjectK(int k) {
		this.seqObject.setK(k);
	}

	public void setSeqObjectOffset(String offset) {
		this.seqObject.setTimeOffset(offset);
		;
	}

	public void setSeqObjectExtension(String extension) {
		this.seqObject.setExtension(extension);
	}

	public JTextField getInputSeqK() {
		return inputSeqK;
	}

	public JTextField getInputSeqTime() {
		return inputSeqTime;
	}

	public JTextField getInputSeqExtension() {
		return inputSeqExtension;
	}

}

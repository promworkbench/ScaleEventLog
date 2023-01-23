package org.processmining.plugins.algorithms;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class NumericTextFieldFilter extends DocumentFilter {
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string.isEmpty()) {
            super.insertString(fb, offset, string, attr);
        } else {
            fb.insertString(offset, string.replaceAll("[^\\d]", ""), attr);
        }
    }

	private boolean test(String text) {
		try {
			if (text.isEmpty()) {
				return true;
			}
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.isEmpty()) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            fb.replace(offset, length, text.replaceAll("[^\\d]", ""), attrs);
        }
    }

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.delete(offset, offset + length);

		if (test(sb.toString())) {
			super.remove(fb, offset, length);
		} else {
			// warn the user and don't allow the insert
		}

	}
}

package org.processmining.plugins.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ListPresetDialog extends JPanel {
	public static int index = 0;
	private final JPanel listPreset;

	public ListPresetDialog() throws IOException {
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel allPresetLabel = new JLabel("Presets");
		allPresetLabel.setAlignmentX(0.5f);
		add(allPresetLabel);

		listPreset = new JPanel(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(listPreset);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.setBorder(null);
		add(scrollPane);

	}

	public JPanel getListPreset() {
		return listPreset;
	}

}

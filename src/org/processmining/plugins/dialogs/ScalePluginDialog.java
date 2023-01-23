package org.processmining.plugins.dialogs;

import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;
import org.json.JSONObject;
import org.processmining.plugins.parameter.ScaleLogParameter;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

public class ScalePluginDialog extends JPanel {
	public ScalePluginDialog(final ScaleLogParameter param, XLog log) throws IOException {
		double size[][] = { { 400, 400 }, { TableLayoutConstants.FILL, TableLayoutConstants.FILL } };
		setLayout(new TableLayout(size));
		ListPresetDialog preset = new ListPresetDialog();
		StatDialog stat = new StatDialog(param, log);
		MainDialog main = new MainDialog(param, preset);
		add(main, "0, 0, 0, 1");
		add(stat, "1, 0");
		add(preset, "1, 1");
		// update list preset
		Path path = Paths.get("preset.txt");
		File file = new File("preset.txt");
		if (file.exists()) {
			long sizeFile = Files.size(path);
			if (sizeFile != 0) {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String line;
				while ((line = br.readLine()) != null) {
					JSONObject presetJSON = new JSONObject(line);
					PresetDialog presetDialog = new PresetDialog(preset, presetJSON.getString("presetName"),
							ListPresetDialog.index, main);
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridwidth = GridBagConstraints.REMAINDER;
					gbc.weightx = 1;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					preset.getListPreset().add(presetDialog, gbc, ListPresetDialog.index);
					ListPresetDialog.index++;
				}
				br.close();
			}
		}

	}

}

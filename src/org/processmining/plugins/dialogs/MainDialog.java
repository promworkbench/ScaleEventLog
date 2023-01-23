package org.processmining.plugins.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.json.JSONArray;
import org.json.JSONObject;
import org.processmining.plugins.algorithms.NumericTextFieldFilter;
import org.processmining.plugins.parameter.ChoiceParameter;
import org.processmining.plugins.parameter.LoopParameter;
import org.processmining.plugins.parameter.ParallelParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;
import org.processmining.plugins.parameter.ScaleLogParameterAbstract;
import org.processmining.plugins.parameter.SequentialParameter;

public class MainDialog extends JPanel {
	private JPanel mainList;
	private JTextField seedField;
	final ScaleLogParameter param;
	public static int index = 0;

	public MainDialog(final ScaleLogParameter param, ListPresetDialog presetDialog) {
		this.param = param;
		setLayout(new BorderLayout());

		JPanel header = new JPanel();
		header.setLayout(new BorderLayout());
		JLabel seedLabel = new JLabel("Seed");
		seedLabel.setForeground(Color.BLACK);
		header.add(seedLabel, BorderLayout.LINE_START);

		seedField = new JTextField(20);
		PlainDocument doc = (PlainDocument) seedField.getDocument();
		doc.setDocumentFilter(new NumericTextFieldFilter());
		header.add(seedField, BorderLayout.LINE_END);
		add(header, BorderLayout.PAGE_START);

		mainList = new JPanel(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainList);
		add(scrollPane);

		JPanel footer = new JPanel();
		footer.setLayout(new BorderLayout());
		JLabel savePresetLabel = new JLabel("Save preset with name: ");
		JTextField presetName = new JTextField(10);
		footer.add(presetName);
		footer.add(savePresetLabel, BorderLayout.LINE_START);
		JButton add = new JButton("Add");
		add.setBackground(new Color(80, 0, 0));
		add.setForeground(new Color(240, 240, 240));
		footer.add(add, BorderLayout.PAGE_START);
		JButton savePresetButton = new JButton("Save preset");
		savePresetButton.setBackground(new Color(80, 0, 0));
		savePresetButton.setForeground(new Color(240, 240, 240));
		footer.add(savePresetButton, BorderLayout.PAGE_END);
		add(footer, BorderLayout.PAGE_END);

		// controller 
		{
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CompositionDialog panel = new CompositionDialog(mainList, index, param);
					HashMap<String, ScaleLogParameterAbstract> map = new HashMap<String, ScaleLogParameterAbstract>();
					map.put("Sequential", panel.getSeqParam());
					map.put("Parallel", panel.getParallelParam());
					map.put("Choice", panel.getChoiceParam());
					map.put("Loop", panel.getLoopParam());
					param.addListComponents(map, index);
					param.addListComponentTypes("Sequential", index);
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridwidth = GridBagConstraints.REMAINDER;
					gbc.weightx = 1;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					mainList.add(panel, gbc, index);
					index++;

					revalidate();
					repaint();
				}
			});

			savePresetButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					System.out.println(e.getSource());
					List<HashMap<String, ScaleLogParameterAbstract>> listMap = param.getListComponents();
					List<String> listParamType = param.getListComponentTypes();
					JSONArray listComp = new JSONArray();
					for (int i = 0; i < listMap.size(); i++) {
						JSONObject comp = new JSONObject();
						String type = listParamType.get(i);
						HashMap<String, ScaleLogParameterAbstract> map = listMap.get(i);
						switch (type) {
							case "Sequential" :
								SequentialParameter seqParam = (SequentialParameter) map.get("Sequential");
								JSONObject compParam = new JSONObject();
								compParam.put("K", seqParam.getK());
								compParam.put("Offset", seqParam.getTimeOffset());
								compParam.put("Extension", seqParam.getExtension());
								comp.put("componentType", "Sequential");
								comp.put("param", compParam);
								break;
							case "Choice" :
								ChoiceParameter choiceParam = (ChoiceParameter) map.get("Choice");
								compParam = new JSONObject();
								compParam.put("K", choiceParam.getK());
								compParam.put("Offset", choiceParam.getTimeOffset());
								compParam.put("Extension", choiceParam.getExtension());
								comp.put("componentType", "Choice");
								comp.put("param", compParam);
								break;
							case "Parallel" :
								ParallelParameter parallelParam = (ParallelParameter) map.get("Parallel");
								compParam = new JSONObject();
								compParam.put("K", parallelParam.getK());
								compParam.put("Offset", parallelParam.getTimeOffset());
								compParam.put("Extension", parallelParam.getExtension());
								comp.put("componentType", "Parallel");
								comp.put("param", compParam);
								break;
							case "Loop" :
								LoopParameter loopParam = (LoopParameter) map.get("Loop");
								compParam = new JSONObject();
								compParam.put("K", loopParam.getK());
								compParam.put("Offset", loopParam.getTimeOffset());
								compParam.put("Extension", loopParam.getExtension());
								comp.put("componentType", "Loop");
								comp.put("param", compParam);
								break;
						}
						listComp.put(comp);
					}
					JSONObject obj = new JSONObject();
					obj.put("presetName", presetName.getText());
					obj.put("listComponent", listComp);
					obj.put("seed", param.getSeed());
					try (FileWriter file = new FileWriter("preset.txt", true);
							BufferedWriter buffer = new BufferedWriter(file)) {
						Path path = Paths.get("preset.txt");
						long size = Files.size(path);
						if (size != 0) {
							buffer.newLine();
						}
						buffer.write(obj.toString());
						buffer.flush();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// add to the preset list
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridwidth = GridBagConstraints.REMAINDER;
					gbc.weightx = 1;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					PresetDialog preset;
					preset = new PresetDialog(presetDialog, presetName.getText(), ListPresetDialog.index, getThis());
					presetDialog.getListPreset().add(preset, gbc, ListPresetDialog.index);
					ListPresetDialog.index++;
					presetDialog.getListPreset().revalidate();
					presetDialog.getListPreset().repaint();

				}

			});

			seedField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					if (seedField.getText().equals("")) {
						param.setSeed(0);
					} else {
						param.setSeed(Integer.parseInt(seedField.getText()));
					}

				}

				public void changedUpdate(DocumentEvent e) {
					if (seedField.getText().equals("")) {
						param.setSeed(0);
					} else {
						param.setSeed(Integer.parseInt(seedField.getText()));
					}
				}

				public void removeUpdate(DocumentEvent e) {
					if (seedField.getText().equals("")) {
						param.setSeed(0);
					} else {
						param.setSeed(Integer.parseInt(seedField.getText()));
					}
				}
			});
		}

	}

	private MainDialog getThis() {
		return this;
	}

	public JPanel getMainList() {
		return mainList;
	}

	public void setMainList(JPanel mainList) {
		this.mainList = mainList;
	}

	public ScaleLogParameter getParam() {
		return param;
	}

	public JTextField getSeedField() {
		return seedField;
	}

	public void setSeedField(JTextField seedField) {
		this.seedField = seedField;
	}

}

package org.processmining.plugins.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.processmining.plugins.parameter.ScaleLogParameterAbstract;

public class PresetDialog extends JPanel {
	private int index;

	public PresetDialog(ListPresetDialog listPreset, String presetName, int index, MainDialog mainDialog) {
		this.index = index;
		setLayout(new GridBagLayout());
		if (listPreset.getListPreset().getComponentCount() != 0) {
			setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK), this.getBorder()));
		} else {
			setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK), this.getBorder()));
		}
		
		GridBagConstraints cPanel = new GridBagConstraints();
		cPanel.fill = GridBagConstraints.HORIZONTAL;
		cPanel.gridx = 0;
		cPanel.gridy = 0;

		JLabel name = new JLabel("Preset name:");
		add(name, cPanel);

		cPanel.gridx = 1;
		cPanel.gridy = 0;
		JTextField presetNameField = new JTextField(presetName, 20);
		presetNameField.setEditable(false);
		presetNameField.setBorder(null);
		presetNameField.setHorizontalAlignment(JTextField.LEFT);
		Border border = BorderFactory.createEmptyBorder(0, 10, 0, 0);
		presetNameField.setBorder(border);
		add(presetNameField, cPanel);

		cPanel.gridx = 0;
		cPanel.gridy = 1;
		cPanel.gridwidth = 2;
		JPanel buttons = new JPanel();
		JButton useButton = new JButton("Use");
		JButton delButton = new JButton("Delete");
		buttons.add(useButton);
		buttons.add(delButton);
		add(buttons, cPanel);

		// controller
		{
			delButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel listPresetPanel = listPreset.getListPreset();
					Component[] compArr = listPresetPanel.getComponents();
					PresetDialog[] newCompArr = new PresetDialog[compArr.length - 1];
					for (int i = 0; i < newCompArr.length; i++) {
						if (i < getIndex()) {
							newCompArr[i] = (PresetDialog) compArr[i];
						} else {
							newCompArr[i] = (PresetDialog) compArr[i + 1];
						}
						newCompArr[i].setIndex(i);
					}
					listPresetPanel.removeAll();
					for (int i = 0; i < newCompArr.length; i++) {
						GridBagConstraints gbc = new GridBagConstraints();
						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.weightx = 1;
						gbc.fill = GridBagConstraints.HORIZONTAL;
						listPresetPanel.add(newCompArr[i], gbc, i);
					}
					listPresetPanel.revalidate();
					listPresetPanel.repaint();
					ListPresetDialog.index -= 1;

					File file = new File("preset.txt");
					List<String> lines = new ArrayList<>();
					try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						String line;
						while ((line = reader.readLine()) != null) {
							lines.add(line);
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					lines.remove(getIndex());
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
						for (String line : lines) {
							System.out.println(line);
							writer.write(line);
							writer.newLine();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			});

			useButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// remove all main list in main dialog
					mainDialog.getMainList().removeAll();
					MainDialog.index = 0;
					mainDialog.getMainList().revalidate();
					mainDialog.getMainList().repaint();

					// get the preset from preset.txt
					File file = new File("preset.txt");
					List<String> lines = new ArrayList<>();
					try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						String line;
						while ((line = reader.readLine()) != null) {
							lines.add(line);
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					JSONObject listCompsJson = new JSONObject(lines.get(getIndex()));
					JSONArray listComps = listCompsJson.getJSONArray("listComponent");
					mainDialog.getSeedField().setText(Integer.toString(listCompsJson.getInt("seed")));
					mainDialog.getSeedField().revalidate();
					mainDialog.getSeedField().repaint();
					for (int i = 0; i < listComps.length(); i++) {
						JSONObject compJson = listComps.getJSONObject(i);
						String compType = compJson.getString("componentType");
						CompositionDialog panel = new CompositionDialog(mainDialog.getMainList(), i,
								mainDialog.getParam());
						HashMap<String, ScaleLogParameterAbstract> map = new HashMap<String, ScaleLogParameterAbstract>();
						map.put("Sequential", panel.getSeqParam());
						map.put("Parallel", panel.getParallelParam());
						map.put("Choice", panel.getChoiceParam());
						map.put("Loop", panel.getLoopParam());
						mainDialog.getParam().addListComponents(map, i);
						mainDialog.getParam().addListComponentTypes(compType, i);
						
						switch (compType) {
							case "Sequential" :
								// set UI
								SequentialParameterPanel seqParamPanelObj = new SequentialParameterPanel(i);
								JPanel seqParamPanel = seqParamPanelObj.create(mainDialog.getParam());
								seqParamPanelObj.getInputSeqK()
										.setText(Integer.toString(compJson.getJSONObject("param").getInt("K")));
								seqParamPanelObj.getInputSeqTime()
										.setText(compJson.getJSONObject("param").getString("Offset"));
								seqParamPanelObj.getInputSeqExtension()
										.setText(compJson.getJSONObject("param").getString("Extension"));
								panel.setPaSeq(seqParamPanel);
								panel.getJcbo().setSelectedIndex(0);
								break;
							case "Parallel" :
								// set UI
								ParallelParameterPanel parParamPanelObj = new ParallelParameterPanel(i);
								JPanel parParamPanel = parParamPanelObj.create(mainDialog.getParam());
								parParamPanelObj.getInputSeqK()
										.setText(Integer.toString(compJson.getJSONObject("param").getInt("K")));
								parParamPanelObj.getInputSeqTime()
										.setText(compJson.getJSONObject("param").getString("Offset"));
								parParamPanelObj.getInputSeqExtension()
										.setText(compJson.getJSONObject("param").getString("Extension"));
								panel.setPaPar(parParamPanel);
								panel.getJcbo().setSelectedIndex(1);
								break;
							case "Choice" :
								// set UI
								ChoiceParameterPanel choiceParamPanelObj = new ChoiceParameterPanel(i);
								JPanel choiceParamPanel = choiceParamPanelObj.create(mainDialog.getParam());
								choiceParamPanelObj.getInputSeqK()
										.setText(Integer.toString(compJson.getJSONObject("param").getInt("K")));
								choiceParamPanelObj.getInputSeqTime()
										.setText(compJson.getJSONObject("param").getString("Offset"));
								choiceParamPanelObj.getInputSeqExtension()
										.setText(compJson.getJSONObject("param").getString("Extension"));
								panel.setPaChoice(choiceParamPanel);
								panel.getJcbo().setSelectedIndex(2);
								break;
							case "Loop" :
								// set UI
								LoopParameterPanel loopParamPanelObj = new LoopParameterPanel(i);
								JPanel loopParamPanel = loopParamPanelObj.create(mainDialog.getParam());
								loopParamPanelObj.getInputSeqK()
										.setText(Integer.toString(compJson.getJSONObject("param").getInt("K")));
								loopParamPanelObj.getInputSeqTime()
										.setText(compJson.getJSONObject("param").getString("Offset"));
								loopParamPanelObj.getInputSeqExtension()
										.setText(compJson.getJSONObject("param").getString("Extension"));
								panel.setPaLoop(loopParamPanel);
								panel.getJcbo().setSelectedIndex(3);
								break;
						}

						
						GridBagConstraints gbc = new GridBagConstraints();
						gbc.gridwidth = GridBagConstraints.REMAINDER;
						gbc.weightx = 1;
						gbc.fill = GridBagConstraints.HORIZONTAL;
						mainDialog.getMainList().add(panel, gbc, i);
						MainDialog.index++;

						revalidate();
						repaint();
					}
				}

			});
		}

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}

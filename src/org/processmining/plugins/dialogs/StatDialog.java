package org.processmining.plugins.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.algorithms.XLogStat;
import org.processmining.plugins.parameter.ScaleLogParameter;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

public class StatDialog extends JPanel {
	public StatDialog(final ScaleLogParameter param, XLog log) {
		XLogStat logStat = new XLogStat(log);

		setBorder(new CompoundBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.BLACK), this.getBorder()));
		JLabel statLabel = new JLabel("Statistic");
		setLayout(new FlowLayout());
		add(statLabel);

		JPanel statPanel = new JPanel();
		add(statPanel);
		double size[][] = { { 350, 46 }, { TableLayoutConstants.FILL } };
		statPanel.setLayout(new TableLayout(size));

		JPanel eventStatPanel = new JPanel();
		statPanel.add(eventStatPanel, "0, 0");
		eventStatPanel.setLayout(new GridBagLayout());
		GridBagConstraints cPanel = new GridBagConstraints();
		
		// label number of cases
		cPanel.fill = GridBagConstraints.HORIZONTAL;
		cPanel.gridx = 0;
		cPanel.gridy = 0;
		JLabel numberCaseLabel = new JLabel("Number of cases: ");
		eventStatPanel.add(numberCaseLabel, cPanel);
		// text field for number of cases
		cPanel.gridx = 1;
		cPanel.gridy = 0;
		JTextField numberCase = new JTextField(Integer.toString(logStat.getNumTrace()), 5);
		numberCase.setEditable(false);
		numberCase.setBorder(null);
		eventStatPanel.add(numberCase, cPanel);
		// label number of events
		cPanel.gridx = 0;
		cPanel.gridy = 1;
		JLabel numberEventLabel = new JLabel("Number of events: ");
		eventStatPanel.add(numberEventLabel, cPanel);
		// text field number of events
		cPanel.gridx = 1;
		cPanel.gridy = 1;
		JTextField numberEvent = new JTextField(Integer.toString(logStat.getNumEvent()), 5);
		numberEvent.setEditable(false);
		numberEvent.setBorder(null);
		eventStatPanel.add(numberEvent, cPanel);
		// label avg event in trace
		cPanel.gridx = 0;
		cPanel.gridy = 2;
		JLabel avgEventCaceLabel = new JLabel("Number events in case: ");
		eventStatPanel.add(avgEventCaceLabel, cPanel);
		// display avg event in trace
		cPanel.gridx = 1;
		cPanel.gridy = 2;
		JTextField avgEventCase = new JTextField(Float.toString(logStat.getAVGEventInCace()), 5);
		avgEventCase.setEditable(false);
		avgEventCase.setBorder(null);
		eventStatPanel.add(avgEventCase, cPanel);
		// label AVG time 
		cPanel.gridx = 0;
		cPanel.gridy = 3;
		JLabel avgTimeLabel = new JLabel("AVG time per case: ");
		eventStatPanel.add(avgTimeLabel, cPanel);
		// text field for number of cases
		cPanel.gridx = 1;
		cPanel.gridy = 3;
		JTextField avgTime = new JTextField(logStat.getAVGTime(), 15);
		avgTime.setEditable(false);
		avgTime.setBorder(null);
		eventStatPanel.add(avgTime, cPanel);
		// label median time
		cPanel.gridx = 0;
		cPanel.gridy = 4;
		JLabel medianTimeLabel = new JLabel("Median time per case: ");
		eventStatPanel.add(medianTimeLabel, cPanel);
		// text field median time
		cPanel.gridx = 1;
		cPanel.gridy = 4;
		JTextField medianTime = new JTextField(logStat.getMedianTime(), 15);
		medianTime.setEditable(false);
		medianTime.setBorder(null);
		eventStatPanel.add(medianTime, cPanel);

	}
}

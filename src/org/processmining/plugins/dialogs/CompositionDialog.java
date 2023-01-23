package org.processmining.plugins.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.processmining.plugins.ParameterPanelFactory;
import org.processmining.plugins.parameter.ChoiceParameter;
import org.processmining.plugins.parameter.LoopParameter;
import org.processmining.plugins.parameter.ParallelParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;
import org.processmining.plugins.parameter.SequentialParameter;

public class CompositionDialog extends JPanel {

	/**
	 * 
	 */
	private JPanel mainList;
	private int compIndex;
	private JPanel paSeq;
	private JPanel paPar;
	private JPanel paLoop;
	private JPanel paChoice;
	private SequentialParameter seqParam;
	private ParallelParameter parallelParam;
	private ChoiceParameter choiceParam;
	private LoopParameter loopParam;
	
	private JComboBox<String> jcbo;

	public CompositionDialog(JPanel mainList, int compIndex, final ScaleLogParameter param) {
		this.mainList = mainList;
		this.compIndex = compIndex;
		ParameterPanelFactory fact = new ParameterPanelFactory();
		this.paSeq = fact.getPanel("Sequential", this.compIndex, param);
		this.paPar = fact.getPanel("Parallel", this.compIndex, param);
		this.paLoop = fact.getPanel("Loop", this.compIndex, param);
		this.paChoice = fact.getPanel("Choice", this.compIndex, param);
		this.seqParam = new SequentialParameter(param.getDefaultTimeDuration());
		this.parallelParam = new ParallelParameter(param.getDefaultTimeDuration());
		this.choiceParam = new ChoiceParameter(param.getDefaultTimeDuration());
		this.loopParam = new LoopParameter(param.getDefaultTimeDuration());
		
		setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
		setLayout(new GridBagLayout());
		GridBagConstraints cPanel = new GridBagConstraints();

		cPanel.fill = GridBagConstraints.HORIZONTAL;
		cPanel.gridx = 0;
		cPanel.gridy = 0;

		jcbo = new JComboBox<String>();
		String[] aS = new String[] { "Sequential", "Parallel", "Choice", "Loop" };
		for (String a : aS) {
			jcbo.addItem(a);
		}
		add(jcbo, cPanel);
		JPanel paramPanel = new JPanel();
		
		
		paramPanel.add(paSeq);
		JButton del = new JButton("delete");
		del.setBackground(new Color(40, 40, 40));
		del.setForeground(new Color(240, 240, 240));

		cPanel.gridx = 1;
		cPanel.gridy = 0;
		add(del, cPanel);
		cPanel.gridx = 0;
		cPanel.gridy = 1;
		cPanel.gridwidth = 2;
		add(paramPanel, cPanel);
		jcbo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String comp = (String) jcbo.getSelectedItem();
				if (comp.equals("Sequential")) {
					paramPanel.removeAll();
					paramPanel.add(paSeq);
					paramPanel.revalidate();
					paramPanel.repaint();
					param.replaceListComponentTypes("Sequential", getCompIndex());
				} else if (comp.equals("Parallel")) {
					paramPanel.removeAll();
					paramPanel.add(paPar);
					paramPanel.revalidate();
					paramPanel.repaint();
					param.replaceListComponentTypes("Parallel", getCompIndex());
				} else if (comp.equals("Loop")) {
					paramPanel.removeAll();
					paramPanel.add(paLoop);
					paramPanel.revalidate();
					paramPanel.repaint();
					param.replaceListComponentTypes("Loop", getCompIndex());
				} else {
					paramPanel.removeAll();
					paramPanel.add(paChoice);
					paramPanel.revalidate();
					paramPanel.repaint();
					param.replaceListComponentTypes("Choice", getCompIndex());
				}

			}

		});

		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				removeAll();
				setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));

				revalidate();
				repaint();
				Component[] compArr =  mainList.getComponents();
				CompositionDialog[] newCompArr = new CompositionDialog[compArr.length - 1];
				for (int i = 0; i < newCompArr.length; i++) {
					if (i < getCompIndex()) {
						newCompArr[i] = (CompositionDialog) compArr[i];
					} else {
						newCompArr[i] = (CompositionDialog) compArr[i + 1];
					}
					newCompArr[i].setCompIndex(i);
				}
				mainList.removeAll();
				for (int i = 0; i < newCompArr.length; i++) {
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridwidth = GridBagConstraints.REMAINDER;
					gbc.weightx = 1;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					mainList.add(newCompArr[i], gbc, i);
				}
				mainList.revalidate();
				mainList.repaint();
				MainDialog.index -= 1;
				param.deleteListComponents(getCompIndex());
				param.deleteListComponentTypes(getCompIndex());

			}

		});
	}

	public int getCompIndex() {
		return compIndex;
	}

	public void setCompIndex(int index) {
		this.compIndex = index;
	}

	public JPanel getMainList() {
		return mainList;
	}

	public JPanel getPaSeq() {
		return paSeq;
	}

	public void setPaSeq(JPanel paSeq) {
		this.paSeq = paSeq;
	}

	public void setPaPar(JPanel paPar) {
		this.paPar = paPar;
	}

	public void setPaLoop(JPanel paLoop) {
		this.paLoop = paLoop;
	}

	public void setPaChoice(JPanel paChoice) {
		this.paChoice = paChoice;
	}

	public JPanel getPaPar() {
		return paPar;
	}

	public JPanel getPaLoop() {
		return paLoop;
	}

	public JPanel getPaChoice() {
		return paChoice;
	}

	public SequentialParameter getSeqParam() {
		return seqParam;
	}

	public ParallelParameter getParallelParam() {
		return parallelParam;
	}

	public ChoiceParameter getChoiceParam() {
		return choiceParam;
	}

	public LoopParameter getLoopParam() {
		return loopParam;
	}

	public JComboBox<String> getJcbo() {
		return jcbo;
	}

	public void setJcbo(JComboBox<String> jcbo) {
		this.jcbo = jcbo;
	}
	

}

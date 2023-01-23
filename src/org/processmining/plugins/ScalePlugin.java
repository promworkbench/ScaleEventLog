package org.processmining.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.algorithms.ScaleAlgorithm;
import org.processmining.plugins.algorithms.XLogStat;
import org.processmining.plugins.dialogs.ListPresetDialog;
import org.processmining.plugins.dialogs.MainDialog;
import org.processmining.plugins.dialogs.ScalePluginDialog;
import org.processmining.plugins.parameter.ScaleLogParameter;

public class ScalePlugin extends ScaleAlgorithm {
	@Plugin(name = "Scale Event Log", parameterLabels = { "Event log" }, returnLabels = {
			"Scaled log" }, returnTypes = {
					XLog.class }, help = "Scale event log using 4 compositions: Sequential, Parallel, Choice, Loop")
	@UITopiaVariant(affiliation = "RWTH Aachen", author = "Hieu Le", email = "hieu.le@rwth-aachen.de")
	public XLog runUI(UIPluginContext context, XLog log) throws Exception {
		ScaleLogParameter param = new ScaleLogParameter();
		XLogStat stat = new XLogStat(log);
		param.setDefaultTimeDuration(stat.getAVGTime());
		ScalePluginDialog dialog = new ScalePluginDialog(param, log);
		InteractionResult result = context.showWizard("Scale configuration", true, true, dialog);
		if (result == InteractionResult.FINISHED) {
			MainDialog.index = 0;
			ListPresetDialog.index = 0;
			if (log.getAttributes().get("concept:name") != null) {
				context.getFutureResult(0).setLabel("Scaled log of " + log.getAttributes().get("concept:name"));
			} else {
				context.getFutureResult(0).setLabel("Scaled log of an anonymous log");
			}
			return this.apply(context, log, param);
		} else {
			context.getFutureResult(0).cancel(true);
			MainDialog.index = 0;
			ListPresetDialog.index = 0;
			return null;
		}
	}
}

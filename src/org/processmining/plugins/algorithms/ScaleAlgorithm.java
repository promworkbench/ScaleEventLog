package org.processmining.plugins.algorithms;

import java.util.HashMap;
import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XLogImpl;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.parameter.ChoiceParameter;
import org.processmining.plugins.parameter.LoopParameter;
import org.processmining.plugins.parameter.ParallelParameter;
import org.processmining.plugins.parameter.ScaleLogParameter;
import org.processmining.plugins.parameter.ScaleLogParameterAbstract;
import org.processmining.plugins.parameter.SequentialParameter;

public class ScaleAlgorithm {
	public XLog apply(PluginContext context, XLog log, ScaleLogParameter parameters) throws Exception {
		List<HashMap<String, ScaleLogParameterAbstract>> listMap = parameters.getListComponents();
		List<String> listParamType = parameters.getListComponentTypes();
		XLogImpl res = new XLogImpl(log.getAttributes());
		XLogImpl resTemp;
		XLogImpl newLog;

		for (XEventClassifier clf : log.getClassifiers()) {
			res.getClassifiers().add(clf);
		}
		for (XEventClassifier clf : log.getClassifiers()) {
			res.setInfo(clf, log.getInfo(clf));
		}

		for (int i = 0; i < listMap.size(); i++) {
			HashMap<String, ScaleLogParameterAbstract> map = parameters.getListComponents().get(i);
			String paramType = listParamType.get(i);

			switch (paramType) {
				case "Sequential" :
					
					SequentialParameter seqParam = (SequentialParameter) map.get("Sequential");
					context.log("Processing the composition sequential_" + seqParam.getK() + "...");
					SequentialScale seqScale = new SequentialScale(parameters.getSeed(), seqParam.getK(),
							seqParam.getTimeOffset(), seqParam.getExtension());
					if (i == 0) {
						newLog = seqScale.compose(log);
					} else {
						newLog = seqScale.compose(res);
					}
					resTemp = new XLogImpl(log.getAttributes());
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.getClassifiers().add(clf);
					}
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.setInfo(clf, log.getInfo(clf));
					}
					for (XTrace tr : newLog) {
						resTemp.add(tr);
					}
					res = resTemp;
					break;
				case "Choice" :
					ChoiceParameter choiceParam = (ChoiceParameter) map.get("Choice");
					context.log("Processing the composition choice_" + choiceParam.getK() + "...");
					ChoiceScale choiceScale = new ChoiceScale(parameters.getSeed(), choiceParam.getK(),
							choiceParam.getTimeOffset(), choiceParam.getExtension());
					if (i == 0) {
						newLog = choiceScale.compose(log);
					} else {
						newLog = choiceScale.compose(res);
					}
					resTemp = new XLogImpl(log.getAttributes());
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.getClassifiers().add(clf);
					}
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.setInfo(clf, log.getInfo(clf));
					}
					for (XTrace tr : newLog) {
						resTemp.add(tr);
					}
					res = resTemp;
					break;
				case "Parallel" :
					ParallelParameter parallelParam = (ParallelParameter) map.get("Parallel");
					context.log("Processing the composition parallel_" + parallelParam.getK() + "...");
					ParallelScale parallelScale = new ParallelScale(parameters.getSeed(), parallelParam.getK(),
							parallelParam.getTimeOffset(), parallelParam.getExtension());
					if (i == 0) {
						newLog = parallelScale.compose(log);
					} else {
						newLog = parallelScale.compose(res);
					}
					resTemp = new XLogImpl(log.getAttributes());
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.getClassifiers().add(clf);
					}
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.setInfo(clf, log.getInfo(clf));
					}
					for (XTrace tr : newLog) {
						resTemp.add(tr);
					}
					res = resTemp;
					break;
				case "Loop" :
					LoopParameter loopParam = (LoopParameter) map.get("Loop");
					context.log("Processing the composition loop_" + loopParam.getK() + "...");
					LoopScale loopScale = new LoopScale(parameters.getSeed(), loopParam.getK(),
							loopParam.getTimeOffset(), loopParam.getExtension());
					if (i == 0) {
						newLog = loopScale.compose(log);
					} else {
						newLog = loopScale.compose(res);
					}
					resTemp = new XLogImpl(log.getAttributes());
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.getClassifiers().add(clf);
					}
					for (XEventClassifier clf : log.getClassifiers()) {
						resTemp.setInfo(clf, log.getInfo(clf));
					}
					for (XTrace tr : newLog) {
						resTemp.add(tr);
					}
					res = resTemp;
					break;
			}
		}
		return res;
	}
}

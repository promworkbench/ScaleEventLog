package org.processmining.plugins.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XLogImpl;

public class SequentialScale extends ScaleAbstract {

	public SequentialScale(int seed, int k, String offset, String extension) {
		super(seed, k, offset, extension);
	}

	@Override
	public List<List<XTrace>> getListCompTraces(List<List<Integer>> listPermutation, List<XTrace> listTraces,
			String classifier, String extension) {

		List<XTrace> uniqueLTrace = new ArrayList<>();
		for (XTrace tr : listTraces) {
			uniqueLTrace.add(tr);
		}
		List<List<XTrace>> res = new ArrayList<>();
		for (int i = 0; i < listTraces.size(); i++) {
			List<XTrace> listCurTrace = new ArrayList<>();
			XTrace t = this.transformTrace(listTraces.get(i), classifier, 0, extension);
			listCurTrace.add(t);
			for (int j = 0; j < listPermutation.size(); j++) {
				XTrace toAdd = uniqueLTrace.get(listPermutation.get(j).get(i));
				XTrace g = this.transformTrace(toAdd, classifier, j + 1, extension);
				listCurTrace.add(g);
			}

			res.add(listCurTrace);
		}

		return res;

	}

	@Override
	public XLogImpl compose(XLog log) throws Exception {
		this.shufflePermutation(log.size(), this.k - 1);
		String classifier = this.getClassifier(log);
		XLogImpl newLog = new XLogImpl(log.getAttributes());

		List<List<XTrace>> listCompTraces = this.getListCompTraces(this.listPermutation, this.getListTraces(log),
				classifier, this.extension);

		for (List<XTrace> listTrace : listCompTraces) {
			XTrace toAddTrace = this.appendTraces(listTrace, this.offset, this.getTimeCols(log));
			newLog.add(toAddTrace);
		}
		return newLog;
	}

}

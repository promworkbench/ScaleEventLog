package org.processmining.plugins.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;

public class ChoiceScale extends ScaleAbstract {

	public ChoiceScale(int seed, int k, String offset, String extension) {
		super(seed, k, offset, extension);
	}

	public List<List<XTrace>> getListCompTraces(List<List<Integer>> listPermutation, List<XTrace> listTraces,
			String classifier, String extension) {
		List<XTrace> uniqueLTrace = new ArrayList<>();
		for (XTrace tr : listTraces) {
			uniqueLTrace.add(tr);
		}
		List<List<XTrace>> res = new ArrayList<>();
		for (int i = 0; i < listTraces.size(); i++) {
			int index = new Random((long) Math.pow(this.seed, i)).nextInt(listPermutation.size());
			List<XTrace> curTrace = new ArrayList<>();
			XTraceImpl newTrace = new XTraceImpl(listTraces.get(i).getAttributes());
			XTrace toAdd = uniqueLTrace.get(listPermutation.get(index).get(i));
			XTrace g = this.transformTrace(toAdd, classifier, index, extension);
			for (XEvent e: g) {
				newTrace.add(e);
			}
			curTrace.add(newTrace);
			res.add(curTrace);
		}
		return res;
	}

	public XLogImpl compose(XLog log) throws Exception {
		this.shufflePermutation(log.size(), this.k);
		XLogImpl newLog = new XLogImpl(log.getAttributes());
		String classifier = this.getClassifier(log);

		List<List<XTrace>> listCompTraces = this.getListCompTraces(this.listPermutation, this.getListTraces(log),
				classifier, this.extension);
		for (List<XTrace> listTrace : listCompTraces) {
			newLog.add(listTrace.get(0));
		}
		return newLog;
	}

//	public static void main(String[] args) throws Exception {
//		ChoiceScale a = new ChoiceScale(7, 5, "1:00:00", "__");
//		File f = new File("C:\\D\\data\\running-example.xes");
//		InputStream in = new FileInputStream(f);
//		XesXmlParser p = new XesXmlParser();
//		List<XLog> lXlog = p.parse(in);
//		XLog log = lXlog.get(0);
//		a.shufflePermutation(log.size(), 5);
//		System.out.println(a.listPermutation);
//		for (List<XTrace> l : a.getListCompTraces(a.listPermutation, a.getListTraces(log), "Activity", "__")) {
//			for (XTrace t : l) {
//				a.printTrace(t, "Activity");
//			}
//		}
//		System.out.println(new Random(7 ^ 5).nextInt(5));
//	}

}
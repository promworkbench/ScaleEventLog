package org.processmining.plugins.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;

public class LoopScale extends ScaleAbstract {

	public LoopScale(int seed, int k, String offset, String extension) {
		super(seed, k, offset, extension);
	}

	public XTrace get1EventTrace(XTrace trace, HashMap<String, Date> dateMap, String classifier) {
		XTraceImpl newTrace = new XTraceImpl(trace.getAttributes());
		XEvent event = trace.get(0);

		for (String clf : event.getAttributes().keySet()) {
			if (clf.equals(classifier)) {
				event.getAttributes().put(clf, new XAttributeLiteralImpl(clf, "go0"));
			} else {

				event.getAttributes().put(clf, new XAttributeLiteralImpl(clf, "dummy_start"));
			}
		}
		for (String timeCol : dateMap.keySet()) {
			event.getAttributes().put(timeCol, new XAttributeTimestampImpl(timeCol, dateMap.get(timeCol)));
		}
		newTrace.add(event);
		return newTrace;
	}

	public List<XTrace> transformListTrace(List<XTrace> listTrace, List<String> timeCols, String classifier,
			XTrace modelTrace) throws Exception {
		XTraceImpl newTrace = new XTraceImpl(modelTrace.getAttributes());
		HashMap<String, Date> dateMap = new HashMap<String, Date>();
		for (String timeCol : timeCols) {
			Date time = new Date();
			for (int i = 0; i < modelTrace.size(); i++) {
				if (modelTrace.get(i).getAttributes().containsKey(timeCol)) {
					time = ((XAttributeTimestamp) modelTrace.get(0).getAttributes().get(timeCol)).getValue();
				}
			}
			if (time != null) {
				time = this.minusTime(time, "0:01:00");
				dateMap.put(timeCol, time);
			}
			
		}
		XEventImpl startEvent = new XEventImpl();
		for (String clf : modelTrace.get(0).getAttributes().keySet()) {
			if (timeCols.contains(clf)) {
				if (dateMap.containsKey(clf)) {
					startEvent.getAttributes().put(clf, new XAttributeTimestampImpl(clf, dateMap.get(clf)));
				}
			} else if (clf.equals(classifier)) {
				startEvent.getAttributes().put(classifier, new XAttributeLiteralImpl(classifier, "go0"));
			} else {
				startEvent.getAttributes().put(clf,
						new XAttributeLiteralImpl(clf, "dummy_start_event"));
			}
		}

		newTrace.add(startEvent);
		List<XTrace> res = new ArrayList<>();
		res.add(newTrace);
		for (int i = 0; i < listTrace.size(); i++) {
			res.add(listTrace.get(i));
		}
		return res;
	}

	public List<List<XTrace>> getListCompTraces(List<List<Integer>> listPermutation, List<XTrace> listTraces,
			String classifier, String extension) {
		List<List<XTrace>> res = new ArrayList<>();
		List<XTrace> uniqueLTrace = new ArrayList<>();
		for (XTrace tr : listTraces) {
			uniqueLTrace.add(tr);
		}
		for (int i = 0; i < listTraces.size(); i++) {
			List<XTrace> curTrace = new ArrayList<>();

			int count = 0;
			boolean cont = new Random((long) Math.pow(this.seed, count + i)).nextBoolean();
			while (cont) {
				int index = new Random((long) Math.pow(this.seed, count + i)).nextInt(listPermutation.size());
				XTrace toAdd = uniqueLTrace.get(listPermutation.get(index).get(0));
				XTrace g = this.transformTrace(toAdd, classifier, index, extension);
				curTrace.add(g);
				count++;
				cont = new Random((long) Math.pow(this.seed, count + i)).nextBoolean();
			}
			res.add(curTrace);
		}

		return res;
	}

	public XLogImpl compose(XLog log) throws Exception {
		this.randomKTrace(log.size(), this.k);
		XLogImpl newLog = new XLogImpl(log.getAttributes());
		String classifier = this.getClassifier(log);
//		HashMap<String, Date> dateMap = new HashMap<String, Date>();
		List<String> timeCols = this.getTimeCols(log);
//		for (String timeCol : timeCols) {
//			Date time = ((XAttributeTimestamp) log.get(0).get(0).getAttributes().get(timeCol)).getValue();
//			time = this.addTime(time, this.offset);
//			dateMap.put(timeCol, time);
//		}
		List<List<XTrace>> listCompTraces = this.getListCompTraces(this.listPermutation, this.getListTraces(log),
				classifier, this.extension);
		int count = 0;
		for (List<XTrace> listTrace : listCompTraces) {
			listTrace = this.transformListTrace(listTrace, timeCols, classifier, log.get(count));
			XTrace toAddTrace = this.appendTraces(listTrace, this.offset, timeCols);
			newLog.add(toAddTrace);
			count++;
		}
		return newLog;
	}

	public static void main(String[] args) throws Exception {
		File f = new File("C:\\D\\data\\running-example.xes");
		InputStream in = new FileInputStream(f);
		XesXmlParser p = new XesXmlParser();
		List<XLog> lXlog = p.parse(in);
		XLog log = lXlog.get(0);

		//		LoopScale a = new LoopScale(4, 2, "1:00:00", "__");
		//		a.randomKTrace(6, 9);
		//		System.out.println(a.listPermutation);
		//		for (int i = 0; i < 10; i++) {
		//			System.out.println(new Random((long) Math.pow(45, i)).nextBoolean());
		//		}
		//		Calendar calendar = Calendar.getInstance();
		//		calendar.set(2015, 23, 12, 10, 10);

	}

}

package org.processmining.plugins.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.DateUtils;
import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;

public abstract class ScaleAbstract {

	protected int seed;
	protected int k;
	protected String offset;
	protected String extension;
	protected List<List<Integer>> listPermutation;

	public ScaleAbstract(int seed, int k, String offset, String extension) {
		this.seed = seed;
		this.k = k;
		this.offset = offset;
		this.extension = extension;
		this.listPermutation = new ArrayList<>();
	}

	public abstract List<List<XTrace>> getListCompTraces(List<List<Integer>> listPermutation, List<XTrace> listTraces,
			String classifier, String extension);

	public abstract XLogImpl compose(XLog log) throws Exception;

	public XTrace appendTraces(List<XTrace> traces, String offset, List<String> timeCols) throws Exception {
		XTrace newTrace = traces.get(0);
		for (int i = 1; i < traces.size(); i++) {
			newTrace = this.append2Traces(newTrace, traces.get(i), offset, timeCols);
		}
		return newTrace;

	}

	public XTrace append2Traces(XTrace trace1, XTrace trace2, String offset, List<String> timeCols) throws Exception {
		HashMap<String, Date> dateMap = new HashMap<String, Date>();
		for (String timeCol : timeCols) {
			if (trace1.get(trace1.size() - 1).getAttributes().containsKey(timeCol)) {
				Date time = ((XAttributeTimestamp) trace1.get(trace1.size() - 1).getAttributes().get(timeCol)).getValue();
				dateMap.put(timeCol, time);
			} 
		}

		XTraceImpl newTrace = new XTraceImpl(trace1.getAttributes());
		for (XEvent ev : trace1) {
			newTrace.add(ev);
		}
		for (XEvent ev : trace2) {
			XEventImpl newEvent = new XEventImpl(ev.getAttributes());
			for (String timeCol : timeCols) {
				Date newDate = null;
				if (dateMap.containsKey(timeCol)) {
					newDate = this.addTime(dateMap.get(timeCol), offset);
					newEvent.getAttributes().put(timeCol, new XAttributeTimestampImpl(timeCol, newDate));
					dateMap.replace(timeCol, newDate);
				}
			}
			newTrace.add(newEvent);
		}
		return newTrace;

	}

	public String getClassifier(XLog log) throws Exception {
		if (log.getClassifiers().size() == 0) {
			if (!log.get(0).get(0).getAttributes().containsKey("concept:name")) {
				throw new Exception("No default event classifier is defined in the event log");
			} else {
				return "concept:name";
			}
		} else {
			return log.getClassifiers().get(0).getDefiningAttributeKeys()[0];
		}
	}

	public List<String> getTimeCols(XLog log) {
		List<String> res = new ArrayList<String>();
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		XAttributeInfo traceAttributeInfo = logInfo.getTraceAttributeInfo();
		for (XAttribute attr : traceAttributeInfo.getAttributes()) {
			if (attr instanceof XAttributeTimestamp) {
				res.add(attr.getKey());
			}

		}
		XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
		for (XAttribute attr : eventAttributeInfo.getAttributes()) {
			if (attr instanceof XAttributeTimestamp) {
				res.add(attr.getKey());
			}
		}

		return res;
	}

	public Date addTime(Date date, String toAdd) throws Exception {
		String[] arrToAdd = toAdd.split(":");
		if (arrToAdd.length != 3) {
			throw new Exception("Not correct time format (hh:mm:ss)");
		}
		for (int i = 0; i < arrToAdd.length; i++) {
			try {
				int t = Integer.parseInt(arrToAdd[i]);
			} catch (Exception e) {
				throw new Exception("Accept only number");
			}
		}

		Date newDate = DateUtils.addHours(date, Integer.parseInt(arrToAdd[0]));
		newDate = DateUtils.addMinutes(newDate, Integer.parseInt(arrToAdd[1]));
		newDate = DateUtils.addSeconds(newDate, Integer.parseInt(arrToAdd[2]));
		return newDate;

	}
	
	public Date minusTime(Date date, String toAdd) throws Exception {
		String[] arrToAdd = toAdd.split(":");
		if (arrToAdd.length != 3) {
			throw new Exception("Not correct format");
		}
		for (int i = 0; i < arrToAdd.length; i++) {
			try {
				int t = Integer.parseInt(arrToAdd[i]);
			} catch (Exception e) {
				throw new Exception("Accept only number");
			}
		}

		Date newDate = DateUtils.addHours(date, - Integer.parseInt(arrToAdd[0]));
		newDate = DateUtils.addMinutes(newDate, - Integer.parseInt(arrToAdd[1]));
		newDate = DateUtils.addSeconds(newDate, - Integer.parseInt(arrToAdd[2]));
		return newDate;

	}

	public List<XTrace> getListTraces(XLog log) {
		List<XTrace> res = new ArrayList<>();
		for (XTrace tr : log) {
			res.add(tr);
		}
		return res;
	}

	public XTrace transformTrace(XTrace trace, String classifier, int index, String extension) {
		XTraceImpl newTrace = new XTraceImpl(trace.getAttributes());
		for (XEvent oriEvent : trace) {
			String newName = oriEvent.getAttributes().get(classifier).toString() + extension + index;
			XEventImpl newEvent = new XEventImpl();
			for (String key : oriEvent.getAttributes().keySet()) {
				if (key.equals(classifier)) {
					newEvent.getAttributes().put(classifier, new XAttributeLiteralImpl(classifier, newName));
				} else {
					if (oriEvent.getAttributes().get(key).getClass().equals(XAttributeLiteralImpl.class)) {
						newEvent.getAttributes().put(key,
								new XAttributeLiteralImpl(key, oriEvent.getAttributes().get(key).toString()));
					}
					if (oriEvent.getAttributes().get(key).getClass().equals(XAttributeTimestampImpl.class)) {
						newEvent.getAttributes().put(key, new XAttributeTimestampImpl(key,
								((XAttributeTimestampImpl) oriEvent.getAttributes().get(key)).getValue()));
					}

				}
			}

			newTrace.add(newEvent);

		}
		return newTrace;

	}

	public void shufflePermutation(int traceNum, int k) {
		List<Integer> range = IntStream.rangeClosed(0, traceNum - 1).boxed().collect(Collectors.toList());
		for (int i = 0; i < k; i++) {
			int x = new Random((long) seed * i).nextInt(seed);
			Collections.shuffle(range, new Random(x));
			List<Integer> shuffledList = new ArrayList<>();
			for (int k1 : range) {
				shuffledList.add(k1);
			}
			this.listPermutation.add(shuffledList);
		}
	}

	public void randomKTrace(int traceNum, int k) {
		for (int i = 0; i < k; i++) {
			int x = new Random((long) seed * i).nextInt(traceNum);
			List<Integer> shuffledList = new ArrayList<>();
			shuffledList.add(x);
			this.listPermutation.add(shuffledList);
		}
	}

//	public void printTrace(XTrace tr, String classifier) {
//		int i = 0;
//		System.out.println("$$");
//		for (XEvent e : tr) {
//			System.out.println("-- Event " + i + ": " + e.getAttributes().get(classifier).toString());
//			i++;
//		}
//		System.out.println("$$");
//	}
//
//	public void printTimeTrace(XTrace tr) {
//		int i = 0;
//		System.out.println("$$");
//		for (XEvent e : tr) {
//			System.out.println("-- Event " + i + ": "
//					+ ((XAttributeTimestampImpl) e.getAttributes().get("time:timestamp")).getValue());
//			i++;
//		}
//		System.out.println("$$");
//	}
}

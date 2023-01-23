package org.processmining.plugins.algorithms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;

import javafx.util.Pair;

public class ParallelScale extends ScaleAbstract {

	public ParallelScale(int seed, int k, String offset, String extension) {
		super(seed, k, offset, extension);
	}

	public List<Pair<Integer, Integer>> updateEventPool(List<Pair<Integer, Integer>> eventPool,
			Pair<Integer, Integer> chosenEvent, List<XTrace> listTraces) {
		List<Pair<Integer, Integer>> res = new ArrayList<>();
		Pair<Integer, Integer> nextEvent = this.getNextEvent(chosenEvent, listTraces.get(chosenEvent.getKey()));

		for (Pair<Integer, Integer> event : eventPool) {
			if (event.equals(chosenEvent)) {
				if (nextEvent.getValue() != -1) {
					res.add(nextEvent);
				}
			} else {
				res.add(event);
			}
		}
		return res;
	}

	public XTrace appendTraces(List<XTrace> listTraces, String offset, List<String> timeCols) throws Exception {
		HashMap<String, Date> dateMap = new HashMap<String, Date>();
		for (String timeCol : timeCols) {
			if (listTraces.get(0).get(listTraces.get(0).size() - 1).getAttributes().containsKey(timeCol)) {
				Date time = ((XAttributeTimestamp) listTraces.get(0).get(listTraces.get(0).size() - 1).getAttributes()
						.get(timeCol)).getValue();
				dateMap.put(timeCol, time);
			}
			
		}
		XTraceImpl newTrace = new XTraceImpl(listTraces.get(0).getAttributes());

		List<Pair<Integer, Integer>> eventPool = new ArrayList<>();
		newTrace.add(listTraces.get(0).get(0));
		for (int i = 1; i < listTraces.size(); i++) {
			eventPool.add(new Pair<Integer, Integer>(i, 0));
		}
		if (this.getNextEvent(new Pair<Integer, Integer>(0, 0), listTraces.get(0)).getValue() != -1) {
			eventPool.add(this.getNextEvent(new Pair<Integer, Integer>(0, 0), listTraces.get(0)));
		}

		int counter = 0;
		while (!eventPool.isEmpty()) {
			int index = new Random((long) Math.pow(seed, counter)).nextInt(eventPool.size());
			Pair<Integer, Integer> chosenEvent = eventPool.get(index);
			XEvent newEvent = listTraces.get(chosenEvent.getKey()).get(chosenEvent.getValue());
			for (String timeCol : timeCols) {
				if (dateMap.containsKey(timeCol)) {
					Date newDate = this.addTime(dateMap.get(timeCol), offset);
					newEvent.getAttributes().put(timeCol, new XAttributeTimestampImpl(timeCol, newDate));
					dateMap.replace(timeCol, newDate);
				}
				
			}
			newTrace.add(newEvent);
			eventPool = this.updateEventPool(eventPool, chosenEvent, listTraces);
			counter++;
		}

		return newTrace;
	}

	public Pair<Integer, Integer> getNextEvent(Pair<Integer, Integer> currEvent, XTrace trace) {
		if (trace.size() > currEvent.getValue() + 1) {
			return new Pair<Integer, Integer>(currEvent.getKey(), currEvent.getValue() + 1);
		} else {
			return new Pair<Integer, Integer>(currEvent.getKey(), -1);
		}
	}

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

	public XLogImpl compose(XLog log) throws Exception {
		this.shufflePermutation(log.size(), this.k - 1);
		XLogImpl newLog = new XLogImpl(log.getAttributes());
		String classifier = this.getClassifier(log);
		List<List<XTrace>> listCompTraces = this.getListCompTraces(this.listPermutation, this.getListTraces(log),
				classifier, this.extension);

		for (List<XTrace> listTrace : listCompTraces) {
			XTrace toAddTrace = this.appendTraces(listTrace, this.offset, this.getTimeCols(log));
//			this.printTrace(toAddTrace, classifier);
			newLog.add(toAddTrace);
		}
		return newLog;
		
	}

}

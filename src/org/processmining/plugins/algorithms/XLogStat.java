package org.processmining.plugins.algorithms;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;

public class XLogStat {
	private XLog log;

	public XLogStat(XLog log) {
		this.log = log;
	}

	private Duration getDateDiff(Date date1, Date date2) {
		return Duration.between(date1.toInstant(), date2.toInstant());
	}

	public String getMedianTime() {
		Duration duration = Duration.ZERO;
		List<Long> listDuration = new ArrayList<>();

		for (XTrace tr : log) {
			for (int i = 0; i < tr.size() - 1; i++) {
				Date date1 = ((XAttributeTimestampImpl) tr.get(i).getAttributes().get("time:timestamp")).getValue();
				Date date2 = ((XAttributeTimestampImpl) tr.get(i + 1).getAttributes().get("time:timestamp")).getValue();
				Duration differ = this.getDateDiff(date1, date2);
				duration = duration.plus(differ);
				listDuration.add(duration.toMillis());
			}
		}
		Collections.sort(listDuration);
		if (listDuration.size() % 2 == 1) {
			return DurationFormatUtils.formatDuration(listDuration.get(listDuration.size() / 2), "HH:mm:ss");
		} else {
			return DurationFormatUtils.formatDuration(
					(listDuration.get((listDuration.size() - 1) / 2) + listDuration.get((listDuration.size()) / 2)) / 2,
					"HH:mm:ss");
		}

	}

	public String getAVGTime() {
		int count = 0;
		Duration duration = Duration.ZERO;

		for (XTrace tr : log) {
			for (int i = 0; i < tr.size() - 1; i++) {
				Date date1 = ((XAttributeTimestampImpl) tr.get(i).getAttributes().get("time:timestamp")).getValue();
				Date date2 = ((XAttributeTimestampImpl) tr.get(i + 1).getAttributes().get("time:timestamp")).getValue();
				Duration differ = this.getDateDiff(date1, date2);
				duration = duration.plus(differ);
				count += 1;
			}
		}
		return DurationFormatUtils.formatDuration(duration.dividedBy(count).toMillis(), "HH:mm:ss");
	}
	
	public int getNumEvent() {
		int res = 0;
		for (XTrace trace: this.log) {
			res += trace.size();
		}
		return res;
	}
	
	public int getNumTrace() {
		return this.log.size();
	}
	
	public int getNumAct(String classifier) {
		List<String> listAct = new ArrayList<>();
		for (XTrace trace: this.log) {
			for (XEvent event: trace) {
				if (!listAct.contains(event.getAttributes().get(classifier).toString())) {
					listAct.add(event.getAttributes().get(classifier).toString());
				}
			}
		}
		return listAct.size();
	}
	
	public float getAVGEventInCace() {
		float f = Math.round(((float) this.getNumEvent())/this.log.size()*100f);
		return f / 100f ;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

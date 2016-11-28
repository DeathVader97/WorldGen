package de.FelixPerko.Worldgen.Interpolation;

import java.util.ArrayList;
import java.util.Comparator;

public class Modifier {
	
	ArrayList<Interval> intervals = new ArrayList<>();
	
	double defaultValue;
	double totalMin = Double.MAX_VALUE;
	double totalMax = -Double.MAX_VALUE;
	double totalRange;
	
	public Modifier(double defaultValue){
		this.defaultValue = defaultValue;
	}
	
	public Modifier addCos(double min, double max, double v1, double v2){
		intervals.add(new CosineInterpolationInterval(min, max, v1, v2));
		update(min, max);
		return this;
	}

	public Modifier addConst(double min, double max, double v){
		intervals.add(new ConstantInterpolationInterval(min, max, v));
		update(min, max);
		return this;
	}
	
	private void update(double min, double max) {
		if (min < totalMin){
			totalMin = min;
			totalRange = totalMax-totalMin;
		}
		if (max > totalMax){
			totalMax = max;
			totalRange = totalMax-totalMin;
		}
		intervals.sort(new Comparator<Interval>() {
			@Override
			public int compare(Interval o1, Interval o2) {
				if (o1.min < o2.min)
					return -1;
				if (o1.min > o2.min)
					return 1;
				return 0;
			}
		});
	}
	
	public double modify(double v){
//		System.out.println("totalMin="+totalMin+" totalMax="+totalMax);
		if (v < totalMin || v > totalMax)
			return defaultValue;
		int i = (int)((v-totalMin)*intervals.size()/totalRange);
		int maxI = intervals.size()-1;
//		System.out.println("estimate index="+i+" max="+maxI);
		if (i < 0)
			i = 0;
		if (i > maxI)
			i = maxI;
		while (true){
			Interval interval = intervals.get(i);
			int res = interval.inInterval(v);
//			System.out.println("checkForInterval: "+interval.min+"-"+interval.max+" "+res);
			if (res == 0)
				return interval.getValue(v);
			if (res == -1){
				i--;
				if (i < 0)
					return defaultValue;
			} else if (res == 1){
				i++;
				if (i > maxI)
					return defaultValue;
			}
		}
	}
}

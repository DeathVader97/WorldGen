package de.FelixPerko.Worldgen;

public abstract class Interval {
	double min, max;
	
	public Interval(double min, double max){
		this.min = min;
		this.max = max;
	}
	
	public int inInterval(double v){
		if (v < min)
			return -1;
		if (v > max)
			return 1;
		return 0;
	}
	
	public abstract double getValue(double v);
}
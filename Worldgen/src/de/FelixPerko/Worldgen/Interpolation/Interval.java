package de.FelixPerko.Worldgen.Interpolation;

public abstract class Interval {
	public double min;
	double max;
	
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

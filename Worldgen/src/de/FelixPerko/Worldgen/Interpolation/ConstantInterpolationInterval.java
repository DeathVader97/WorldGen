package de.FelixPerko.Worldgen.Interpolation;

public class ConstantInterpolationInterval extends Interval{
	
	double v;
	
	public ConstantInterpolationInterval(double min, double max, double v) {
		super(min, max);
		this.v = v;
	}

	@Override
	public double getValue(double v) {
		return this.v;
	}

}

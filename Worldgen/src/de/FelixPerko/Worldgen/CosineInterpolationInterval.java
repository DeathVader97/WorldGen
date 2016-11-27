package de.FelixPerko.Worldgen;

public class CosineInterpolationInterval extends Interval{
	
	double v1, v2;
	
	public CosineInterpolationInterval(double min, double max, double v1, double v2) {
		super(min, max);
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public double getValue(double v) {
		return Interpolation.cosineInterpolation(v, min, max, v1, v2);
	}

}

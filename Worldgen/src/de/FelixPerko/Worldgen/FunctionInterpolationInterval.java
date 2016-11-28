package de.FelixPerko.Worldgen;

import de.FelixPerko.Worldgen.Functions.Function;
import de.FelixPerko.Worldgen.Interpolation.Interval;

public class FunctionInterpolationInterval extends Interval{
	
	Function f;
	
	public FunctionInterpolationInterval(double min, double max, Function f) {
		super(min, max);
		this.f = f;
	}
	
	@Override
	public double getValue(double v) {
		return f.getY(v);
	}}

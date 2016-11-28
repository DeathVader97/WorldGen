package de.FelixPerko.Worldgen.Functions.CombinedFunctions;

import de.FelixPerko.Worldgen.Functions.Function;

public class CombinedDivideFunction extends CombinedFunction{


	public CombinedDivideFunction(Function f1, Function f2) {
		super(f1, f2);
	}

	@Override
	public double getY(double x) {
		return f1.getY(x)/f2.getY(x);
	}

	@Override
	public Function derivative() {
		return new CombinedDivideFunction(
				new CombinedSubstractFunction(
					new CombinedMultiplyFunction(f1.derivative(), f2), 
					new CombinedMultiplyFunction(f1, f2.derivative())), 
				new CombinedMultiplyFunction(f2, f2));
	}
	
	@Override
	public String serialize() {
		return "("+f1.serialize()+") / ("+f2.serialize()+")";
	}
}

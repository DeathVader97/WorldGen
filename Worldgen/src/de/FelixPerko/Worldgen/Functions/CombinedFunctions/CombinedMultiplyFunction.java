package de.FelixPerko.Worldgen.Functions.CombinedFunctions;

import de.FelixPerko.Worldgen.Functions.Function;

public class CombinedMultiplyFunction extends CombinedFunction{

	public CombinedMultiplyFunction(Function f1, Function f2) {
		super(f1, f2);
	}

	@Override
	public double getY(double x) {
		return f1.getY(x)*f2.getY(x);
	}

	@Override
	public Function derivative() {
		return new CombinedAddFunction(
				new CombinedMultiplyFunction(f1.derivative(), f2), 
				new CombinedMultiplyFunction(f1, f2.derivative()));
	}
	
	@Override
	public String serialize() {
		return "("+f1.serialize()+") * ("+f2.serialize()+")";
	}
}

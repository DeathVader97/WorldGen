package de.FelixPerko.Worldgen.Functions.CombinedFunctions;

import de.FelixPerko.Worldgen.Functions.Function;

public class CombinedNestedFunction extends CombinedFunction {

	public CombinedNestedFunction(Function f1, Function f2) {
		super(f1, f2);
	}

	@Override
	public double getY(double x) {
		return f1.getY(f2.getY(x));
	}

	@Override
	public Function derivative() {
		return new CombinedMultiplyFunction(
				new CombinedNestedFunction(f1.derivative(), f2),
				f2.derivative());
	}
	
	@Override
	public String serialize() {
		return f1.serialize().replaceAll("x", "("+f2.serialize()+")");
	}
}

package de.FelixPerko.Worldgen.Functions;

public class ExponentialFunction extends Function {
	
	double basis;
	Function exponent = null; //null -> default (x)
	
	/**
	 * creates a function f(x) = a^x
	 * @param basis
	 */
	public ExponentialFunction(double basis) {
		super();
		this.basis = basis;
	}
	
	/**
	 * creates a function f(x) = a^(exponent)
	 * @param basis
	 * @param exponent
	 */
	public ExponentialFunction(double basis, Function exponent) {
		super();
		this.basis = basis;
	}	
	
	@Override
	public double getY(double x) {
		return Math.pow(basis, x);
	}

	@Override
	public Function derivative() {
//		if (basis == Math.E){
//			if (exponent == null)
//				return this;
//			return 
//		}
//TODO
		return null;
	}

}

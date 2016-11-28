package de.FelixPerko.Worldgen.Functions;

public class PolynomalFunction extends Function{
	
	double[] factors;
	
	public PolynomalFunction(double... factors){
		this.factors = factors;
	}

	@Override
	public double getY(double x) {
		if (factors.length == 0)
			return 0;
		double f = x;
		double y = factors[0];
		for (int i = 1 ; i < factors.length ; i++){
			y += factors[i]*f;
			f *= x;
		}
		return y;
	}

	@Override
	public Function derivative() {
		if (factors.length == 0)
			return new PolynomalFunction(0);
		double[] newFactors = new double[factors.length-1];
		for (int i = 1 ; i < factors.length ; i++){
			newFactors[i-1] = factors[i]*i;
		}
		return new PolynomalFunction(newFactors);
	}
	
	@Override
	public String serialize() {
		StringBuilder b = new StringBuilder();
		for (int i = factors.length-1 ; i >= 0 ; i--){
			double f = factors[i];
			if (f > 0){
				if (i != factors.length-1)
					b.append(" + ");
				b.append(f);
				if (i != 0)
					b.append("*x^").append(i);
			} else if (f < 0){
				if (i != factors.length-1)
					b.append(" - ");
				else
					b.append("-");
				b.append(Math.abs(f));
				if (i != 0)
					b.append("*x^").append(i);
			}
		}
		return b.toString();
	}
}

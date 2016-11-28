package de.FelixPerko.Worldgen.Functions;

public abstract class Function {
	
	public static void main(String[] args) {
		PolynomalFunction f = new PolynomalFunction(4,3,2,1);
		System.out.println(f.serialize());
		while(true){
			f = (PolynomalFunction) f.derivative();
			if (f.factors.length == 0)
				break;
			System.out.println(f.serialize());
		}
	}
	
	public abstract double getY(double x);
	
	public Function derivative(){
		System.err.println("Tried to get the derivative of a Function of which the derivative is not defined.");
		Thread.dumpStack();
		return null;
	}
	
	public String serialize(){
		return toString();
	}
}

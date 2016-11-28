package de.FelixPerko.Worldgen.Functions.CombinedFunctions;

import de.FelixPerko.Worldgen.Functions.Function;

public abstract class CombinedFunction extends Function{
	
	Function f1,f2;
	
	public CombinedFunction(Function f1, Function f2){
		this.f1 = f1;
		this.f2 = f2;
	}
}

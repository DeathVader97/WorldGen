package de.FelixPerko.Worldgen;

public class Interpolation {
	
	public static double cosineInterpolation(double x, double min, double max, double v1, double v2){
		double t = (1-Math.cos(Math.PI*(x-min)/(max-min)))/2;
		return (v1*(1-t)+v2*t);
	}
}

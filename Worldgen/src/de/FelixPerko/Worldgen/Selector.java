package de.FelixPerko.Worldgen;

public class Selector {
	
	double[] definiteMin;
	double[] definiteMax;
	boolean[] enabled;
	
	public Selector() {
		int size = TerrainFeature.values().length;
		definiteMin = new double[size];
		definiteMax = new double[size];
		enabled = new boolean[size];
	}
	
	public void setFeature(TerrainFeature feature, double definiteMin, double definiteMax){
		int i = feature.ordinal();
		enabled[i] = true;
		this.definiteMin[i] = definiteMin;
		this.definiteMax[i] = definiteMax;
	}
	
	public double getDifference(double[] features){
		double ret = 0;
		for (int i = 0 ; i < features.length ; i++){
			if (enabled[i]){
				double f = features[i];
				double min = definiteMin[i];
				double max = definiteMax[i];
				if (f < min){
					ret += min-f;
				}if (f > max){
					ret += f-max;
				}else
					continue;
			}
		}
		return ret;
	}
}

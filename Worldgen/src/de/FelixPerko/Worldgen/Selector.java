package de.FelixPerko.Worldgen;

public class Selector {
	
	double[] definiteMin;
	double[] definiteMax;
	boolean[] enabled;
	boolean[] hasCondition;
	double[] conditionMin;
	double[] conditionMax;
	
	public Selector() {
		int size = TerrainFeature.values().length;
		definiteMin = new double[size];
		definiteMax = new double[size];
		enabled = new boolean[size];
		hasCondition = new boolean[size];
		conditionMin = new double[size];
		conditionMax = new double[size];
	}
	
	public Selector setFeature(TerrainFeature feature, double definiteMin, double definiteMax){
		int i = feature.ordinal();
		enabled[i] = true;
		this.definiteMin[i] = definiteMin;
		this.definiteMax[i] = definiteMax;
		return this;
	}
	
	public Selector setCondition(TerrainFeature feature, double min, double max){
		int i = feature.ordinal();
		hasCondition[i] = true;
		conditionMin[i] = min;
		conditionMax[i] = max;
		return this;
	}
	
	public double getDifference(double[] features){
		double ret = 0;
		for (int i = 0 ; i < features.length ; i++){
			if (hasCondition[i]){
				double f = features[i];
				if (f < conditionMin[i] || f > conditionMax[i])
					return Double.MAX_VALUE;
			}
			if (enabled[i]){
				double f = features[i];
				double min = definiteMin[i];
				double max = definiteMax[i];
				if (f < min){
					ret += Math.pow(min-f,2);
				}if (f > max){
					ret += Math.pow(f-max,2);
				}else
					continue;
			}
		}
		return ret;
	}
}

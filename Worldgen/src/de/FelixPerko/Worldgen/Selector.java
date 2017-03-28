package de.FelixPerko.Worldgen;

public class Selector {
	
	double[] definiteMin;
	double[] definiteMax;
	boolean[] enabled;
	boolean[] hasCondition;
	double[] conditionMin;
	double[] conditionMax;
	
	public Selector() {
		int size = TerrainFeature.count;
		definiteMin = new double[size];
		definiteMax = new double[size];
		enabled = new boolean[size];
		hasCondition = new boolean[size];
		conditionMin = new double[size];
		conditionMax = new double[size];
	}
	
	public Selector setFeature(int feature, double definiteMin, double definiteMax){
		enabled[feature] = true;
		this.definiteMin[feature] = definiteMin;
		this.definiteMax[feature] = definiteMax;
		return this;
	}
	
	public Selector setCondition(int feature, double min, double max){
		hasCondition[feature] = true;
		conditionMin[feature] = min;
		conditionMax[feature] = max;
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

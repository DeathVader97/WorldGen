package de.FelixPerko.Worldgen.BiomeGrid;

public class GridPoint {
	
	float originalValue, smoothedValue;
	
	public GridPoint(float i) {
		originalValue = i;
		smoothedValue = i;
	}
}

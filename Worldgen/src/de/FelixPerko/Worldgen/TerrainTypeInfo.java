package de.FelixPerko.Worldgen;

/**
 * contains information about the TerrainType of the block. Used to calculate noise etc. based on a gradient between the type and the nearest other type
 *
 */

public class TerrainTypeInfo {
	
	TerrainType mainType, secondType;
	double gradient;
	
	public TerrainTypeInfo(TerrainType mainType, double d1, TerrainType secondType, double d2) {
		this.mainType = mainType;
		this.secondType = secondType;
		this.gradient = 0;
	}

}

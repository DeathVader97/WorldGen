package de.FelixPerko.Worldgen;


public class TerrainData {
	
	TerrainType type;
	double[] properties;
	
	public TerrainData(TerrainType type, double... properties){
		this.type = type;
		this.properties = properties;
	}
}

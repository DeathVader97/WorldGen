package de.FelixPerko.Worldgen;


public class TerrainData {
	
	TerrainType type;
	TerrainType lowerType;
	double gradient;
	double[] properties;
	
	public TerrainData(TerrainTypeInfo terrainTypeInfo, double... properties){
		this.type = terrainTypeInfo.mainType;
		this.lowerType = terrainTypeInfo.secondType;
		this.gradient = terrainTypeInfo.gradient;
		this.properties = properties;
	}
}

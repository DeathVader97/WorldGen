package de.FelixPerko.Worldgen;


public class TerrainData {
	
	public TerrainType type;
	public TerrainType lowerType;
	public double gradient;
	public double[] properties;
	
	public TerrainData(TerrainTypeInfo terrainTypeInfo, double... properties){
		if (terrainTypeInfo != null){
			this.type = terrainTypeInfo.mainType;
			this.lowerType = terrainTypeInfo.secondType;
			this.gradient = terrainTypeInfo.gradient;
		}
		this.properties = properties;
	}
}

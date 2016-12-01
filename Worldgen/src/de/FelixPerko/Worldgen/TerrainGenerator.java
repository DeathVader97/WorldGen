package de.FelixPerko.Worldgen;

import java.util.ArrayList;
import java.util.Random;

import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;

public abstract class TerrainGenerator {
	public long seed;
	public Random rand;
	public OpenSimplexNoise baseNoise;
	public OpenSimplexNoise temperatureNoise;
	public OpenSimplexNoise humidityNoise;
	public OpenSimplexNoise isleNoise;
	public OpenSimplexNoise isleLineNoise;
	
	public ArrayList<TerrainType> terrainTypes = new ArrayList<>();
	
	public TerrainGenerator(long seed) {
		this.seed = seed;
		rand = new Random(seed);
		baseNoise = new OpenSimplexNoise(rand.nextLong());
		temperatureNoise = new OpenSimplexNoise(rand.nextLong());
		humidityNoise = new OpenSimplexNoise(rand.nextLong());
		isleNoise = new OpenSimplexNoise(rand.nextLong());
		isleLineNoise = new OpenSimplexNoise(rand.nextLong());
	}
	
	public abstract TerrainData getData(double zoomFactor, double x, double y);
	
	protected TerrainType getType(double... features){
		double minDifference = Double.MAX_VALUE;
		TerrainType ans = null;
		for (TerrainType type : terrainTypes){
			double d = type.selector.getDifference(features);
			if (d < minDifference){
				minDifference = d;
				ans = type;
			}
		}
		return ans;
	}
}

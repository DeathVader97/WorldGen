package de.FelixPerko.Worldgen;

import java.util.ArrayList;
import java.util.Random;

import de.FelixPerko.Worldgen.BiomeGrid.BiomeGrid;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;
import de.FelixPerko.Worldgen.Utils.Pair;

public abstract class TerrainGenerator {
	public long seed;
	public Random rand;
	public OpenSimplexNoise[] baseNoise;
	public OpenSimplexNoise[] temperatureNoise;
	public OpenSimplexNoise[] humidityNoise;
	public OpenSimplexNoise[] isleNoise;
	public OpenSimplexNoise[] isleLineNoise;
	
	public ArrayList<TerrainType> terrainTypes = new ArrayList<>();
	public ArrayList<DensityBasedBlock> densityBasedBlocks = new ArrayList<>();
	public BiomeGrid biomeGrid = new BiomeGrid();
	
	public TerrainGenerator(long seed) {
		this.seed = seed;
		rand = new Random(seed);
		baseNoise = generateNoises(rand,16);
		baseNoise = generateNoises(rand,16);
		temperatureNoise = generateNoises(rand,16);
		humidityNoise = generateNoises(rand,16);
		isleNoise = generateNoises(rand,16);
		isleLineNoise = generateNoises(rand,16);
	}
	
	private OpenSimplexNoise[] generateNoises(Random rand, int maxOctaves) {
		OpenSimplexNoise[] arr = new OpenSimplexNoise[maxOctaves];
		for (int i = 0; i < maxOctaves; i++) {
			arr[i] = new OpenSimplexNoise(rand.nextLong());
		}
		return arr;
	}

	public abstract TerrainData getData(double zoomFactor, double x, double y);

	public abstract TerrainData getChunkData(double zoomFactor, int x, int y);
	
	protected TerrainTypeInfo getTypeInfo(double... features){
		double minDifference = Double.MAX_VALUE;
		TerrainType ans = null;
		double d1 = 0;
		TerrainType ans2 = null;
		double d2 = 0;
		for (TerrainType type : terrainTypes){
			double d = type.selector.getDifference(features);
			if (d < minDifference){
				minDifference = d;
				ans2 = ans;
				d2 = d1;
				ans = type;
				d1 = d;
			}
		}
		return new TerrainTypeInfo(ans, d1, ans2, d2);
	}
}

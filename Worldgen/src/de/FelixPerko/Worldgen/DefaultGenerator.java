package de.FelixPerko.Worldgen;

import java.awt.Color;

import de.FelixPerko.Worldgen.Noise.NoiseHelper;

public class DefaultGenerator extends TerrainGenerator {
	
	public DefaultGenerator(long seed) {
		super(seed);
		this.seed = seed;
		double waterBorder = 0.175;
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, -1, -0.25)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1),new Color(1f,1f,1f))); //Schnee
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.25, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, -1, -0.05)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,1f,0f))); //Wüste
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.0, 0.1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,0.5f,0f))); //Savanne
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.1, 1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,0.5f,0f))); //Regenwald
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, -0.1, 0.1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,1f,0f))); //Wiese
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, 0.05, 0.25)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,0.75f,0f))); //Wald
		terrainTypes.add(new TerrainType(new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, 0.25, 1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0.15f,0.25f,0f))); //Sumpf
		terrainTypes.add(new TerrainType(new Selector()
				.setCondition(TerrainFeature.BASIC, 0, waterBorder)
				.setFeature(TerrainFeature.TEMPERATURE, -0.2, 1), new Color(0f,0f,0.5f))); //Ozean
		terrainTypes.add(new TerrainType(new Selector()
				.setCondition(TerrainFeature.BASIC, 0, waterBorder)
				.setFeature(TerrainFeature.TEMPERATURE, -1, -0.2), new Color(0f,0f,1f))); //gefrohrender Ozean
	}

	public TerrainData getData(double zoomFactor, double x, double y){
				float f = (float)NoiseHelper.simplexNoise2D(x, y, 0.003*zoomFactor, 0.5, 2, 8, baseNoise);
				if (f < 0)
					f = -f;
				if (f < 0.175 && f > 0.05){
					float isleNoise = (float) Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.05*zoomFactor, 0.5, 2, 6, this.isleNoise));
					double lineValue = Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.01*zoomFactor, 0.5, 2, 1, isleLineNoise));
					double isleLineValue = CustomChunkGenerator.onIsleLineModifier.modify(lineValue);
					double multValue = (CustomChunkGenerator.isleLineModifier.modify(isleNoise)*isleLineValue+CustomChunkGenerator.isleGeneralModifier.modify(isleNoise)*(1-isleLineValue));
					f += 0.8*multValue*CustomChunkGenerator.isleHeightModifier.modify(f);
				}
				
				float t = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.6, 2, 8, temperatureNoise);
				float h = 0;
				if (f > 0.175){
					h = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.6, 2, 8, humidityNoise);
					return new TerrainData(getType(f,t,h), f,t,h);
			}
			return new TerrainData(getType(f,t,0), f,t,0);
	}
}

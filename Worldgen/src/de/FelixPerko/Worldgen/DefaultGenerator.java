package de.FelixPerko.Worldgen;

import java.awt.Color;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Utils.Pair;

public class DefaultGenerator extends TerrainGenerator {
	
	public DefaultGenerator(long seed) {
		super(seed);
		this.seed = seed;
		double waterBorder = 0.175;
		TerrainType snow = new TerrainType("snow",new Selector().setFeature(TerrainFeature.TEMPERATURE, -1, -0.25)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1),new Color(1f,1f,1f));
		snow.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.SNOW_BLOCK.getId()));
		snow.descriptor.representationBiome = Biome.ICE_FLATS;
		snow.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				data.properties[0] += strengh*(NoiseHelper.simplexNoise2D(x, z, 0.25*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 4)*0.4+0.15);
			}
			@SuppressWarnings("deprecation") @Override public void onPopulation(TerrainData data, int x, int z, World w) {
				double y = CustomChunkGenerator.heightModifier.modify(data.properties[0]);
				if (y > 64){
					byte blockData = (byte)(8*(y-(int)y));
					Block b = w.getBlockAt(x, (int)y+1, z);
					b.setType(Material.SNOW);
					b.setData(blockData);
				}
			}
		};
		
		TerrainType desert = new TerrainType("desert",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.25, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, -1, -0.05)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,1f,0f));
		desert.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.SAND.getId()));
		desert.descriptor.representationBiome = Biome.DESERT;
		
		TerrainType savanna = new TerrainType("savanna",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.0, 0.1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,0.5f,0f));
		savanna.descriptor.representationBiome = Biome.SAVANNA;
		
		TerrainType rainforest = new TerrainType("rainforest",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.1, 1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,0.5f,0f));
		rainforest.descriptor.representationBiome = Biome.JUNGLE;
		
		TerrainType plains = new TerrainType("plains",new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, -0.1, 0.1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,1f,0f));
		plains.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				data.properties[0] += strengh*(NoiseHelper.simplexNoise2D(x, z, 0.25*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 4)*0.4+0.15);
			}
			@Override
			public void onPopulation(TerrainData data, int x, int z, World w) {
			}
		};
		
		TerrainType forest = new TerrainType("forest",new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, 0.05, 0.25)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,0.75f,0f));
		forest.descriptor.representationBiome = Biome.FOREST;
		forest.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				data.properties[0] += strengh*(NoiseHelper.simplexNoise2D(x, z, 0.25*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 4)*0.4+0.15);
			}
			@Override
			public void onPopulation(TerrainData data, int x, int z, World w) {
				double terrainHeight = CustomChunkGenerator.heightModifier.modify(data.getProperty(TerrainFeature.BASIC));
				double helperNoise = NoiseHelper.simplexNoise2D(x, z, 0.5*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 2)*0.5+0.5;
				if (terrainHeight > 64 && Math.random()*32 <= 1*helperNoise){
					if (Math.random() > 0.05)
						w.generateTree(new Location(w, x, (int)terrainHeight+1, z), TreeType.TREE);
					else
						w.generateTree(new Location(w, x, (int)terrainHeight+1, z), TreeType.BIRCH);
				}
			}
		};
		
		TerrainType swamp = new TerrainType("swamp",new Selector().setFeature(TerrainFeature.TEMPERATURE, -0.1, 0.15)
				.setFeature(TerrainFeature.HUMIDITY, 0.25, 1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0.15f,0.25f,0f));
		swamp.descriptor.representationBiome = Biome.SWAMPLAND;
		swamp.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				data.properties[0] = (1-strengh)*data.properties[0] + strengh*(Math.abs((1-NoiseHelper.simplexNoise2D(x, z, 0.75*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 2))*0.04)+0.13);
			}
			@Override
			public void onPopulation(TerrainData data, int x, int z, World w) {
				double terrainHeight = CustomChunkGenerator.heightModifier.modify(data.getProperty(TerrainFeature.BASIC));
				double helperNoise = NoiseHelper.simplexNoise2D(x, z, 2*CustomChunkGenerator.ZOOM_FACTOR, 0.5, 2, 2);
				if (terrainHeight < 64 && Math.random()*(0.5+0.5*helperNoise) < 0.05){
					w.getBlockAt(x,65,z).setType(Material.WATER_LILY);
				}
				if (Math.random()*256*(1-helperNoise) <= 1){
					if (terrainHeight > 60){
//						w.getBlockAt(x,(int)terrainHeight,z).setType(Material.DIRT);
						w.generateTree(new Location(w, x, (int)terrainHeight+1, z), TreeType.SWAMP);
					}
				}
			}
		};
		
		TerrainType ocean = new TerrainType("ocean",new Selector()
				.setCondition(TerrainFeature.BASIC, 0, waterBorder)
				.setFeature(TerrainFeature.TEMPERATURE, -0.2, 1), new Color(0f,0f,0.5f));
		ocean.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.DIRT.getId()));
		ocean.descriptor.representationBiome = Biome.OCEAN;
		ocean.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				data.properties[0] -= strengh*0.1;
			}
			@Override
			public void onPopulation(TerrainData data, int x, int z, World w) {
			}
		};
		
		TerrainType frozenOcean = new TerrainType("frozenOcean",new Selector()
				.setCondition(TerrainFeature.BASIC, 0, waterBorder)
				.setFeature(TerrainFeature.TEMPERATURE, -1, -0.2), new Color(0f,0f,1f));
		frozenOcean.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.DIRT.getId()));
		frozenOcean.descriptor.representationBiome = Biome.FROZEN_OCEAN;
		frozenOcean.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				double v = NoiseHelper.simplexNoise2D(x, z, 0.05, 0.5, 2, 8)*0.5+0.5;
				if (v > (-data.properties[TerrainFeature.TEMPERATURE.ordinal()]-0.2)*0.2)
					Main.chunkGenerator.setBlock(result, x-xOffset, 64, z-zOffset, (byte)Material.ICE.getId());
			}
			@SuppressWarnings("deprecation") @Override public void onPopulation(TerrainData data, int x, int z, World w) {
			}
		};
		
		terrainTypes.add(snow); //Schnee
		terrainTypes.add(desert); //Wüste
		terrainTypes.add(savanna); //Savanne
		terrainTypes.add(rainforest); //Regenwald
		terrainTypes.add(plains); //Wiese
		terrainTypes.add(forest); //Wald
		terrainTypes.add(swamp); //Sumpf
		terrainTypes.add(ocean); //Ozean
		terrainTypes.add(frozenOcean); //gefrohrender Ozean
		
		ArrayList<Pair<TerrainFeature, Modifier>> longGrassModifiers = new ArrayList<>();
		longGrassModifiers.add(new Pair<TerrainFeature, Modifier>(TerrainFeature.HUMIDITY, new Modifier(0).addCos(-0.5, 1, 0, 1)));
		densityBasedBlocks.add(new DensityBasedBlock(Material.LONG_GRASS.getId(), (byte)1, Material.GRASS.getId(), longGrassModifiers));
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
			t += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, temperatureNoise)*0.025;
			float h = 0;
			if (f > 0.175){
				h = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.6, 2, 8, humidityNoise);
				h += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, humidityNoise)*0.025;
				return new TerrainData(getTypeInfo(f, t, h), f, t, h);
			}
			return new TerrainData(getTypeInfo(f, t, 0), f, t, 0);
	}
	
	public TerrainData getChunkData(double zoomFactor, int chunkX, int chunkZ){
		int x = chunkX*16;
		int y = chunkZ*16;
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
		t += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, temperatureNoise)*0.025;
		float h = 0;
		if (f > 0.175){
			h = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.6, 2, 8, humidityNoise);
			h += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, humidityNoise)*0.025;
			return new TerrainData(getTypeInfo(f, t, h), f, t, h);
		}
		return new TerrainData(getTypeInfo(f, t, 0), f, t, 0);
	}
}

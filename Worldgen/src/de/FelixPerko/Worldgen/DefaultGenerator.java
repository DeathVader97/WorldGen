package de.FelixPerko.Worldgen;

import java.awt.Color;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import static de.FelixPerko.Worldgen.TerrainFeature.*;

import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Utils.Pair;

public class DefaultGenerator extends TerrainGenerator {
	
	public DefaultGenerator(long seed) {
		super(seed);
		this.seed = seed;
		double waterBorder = 0.175;
<<<<<<< HEAD
		TerrainType mountains = new TerrainType(new Selector().setCondition(MOUNTAIN_SELECT, 0.8, 1), new Color(.5f,.5f,.5f));
		TerrainType snow = new TerrainType(new Selector().setFeature(TEMPERATURE, -1, -0.35)
				.setCondition(BASIC, waterBorder, 1),new Color(1f,1f,1f));
=======
		TerrainType snow = new TerrainType("snow",new Selector().setFeature(TerrainFeature.TEMPERATURE, -1, -0.25)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1),new Color(1f,1f,1f));
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
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
<<<<<<< HEAD
		TerrainType snowForest = new TerrainType(new Selector().setFeature(TEMPERATURE, -0.35, -0.3)
				.setFeature(HUMIDITY, 0.1, 1)
				.setCondition(BASIC, waterBorder, 1), new Color(0.6f,1f,0.6f));
		snowForest.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.SNOW.getId()));
		snowForest.descriptor.representationBiome = Biome.TAIGA_COLD;
		TerrainType desert = new TerrainType(new Selector().setFeature(TEMPERATURE, 0.15, 1.0)
				.setFeature(HUMIDITY, -1, -0.2)
				.setCondition(BASIC, waterBorder, 1), new Color(1f,1f,0f));
=======
		
		TerrainType desert = new TerrainType("desert",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.25, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, -1, -0.05)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,1f,0f));
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		desert.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.SAND.getId()));
		desert.descriptor.representationBiome = Biome.DESERT;
<<<<<<< HEAD
		TerrainType savanna = new TerrainType(new Selector().setFeature(TEMPERATURE, 0.05, 1.0)
				.setFeature(HUMIDITY, -0.15, -0.1)
				.setCondition(BASIC, waterBorder, 1), new Color(1f,0.5f,0f));
=======
		
		TerrainType savanna = new TerrainType("savanna",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.0, 0.1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(1f,0.5f,0f));
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		savanna.descriptor.representationBiome = Biome.SAVANNA;
<<<<<<< HEAD
		TerrainType steppe = new TerrainType(new Selector().setFeature(TEMPERATURE, -0.25, 0.05)
				.setFeature(HUMIDITY, -0.2, -0.15)
				.setCondition(BASIC, waterBorder, 1), new Color(1f,0.75f,0.25f));
		steppe.descriptor.representationBiome = Biome.SAVANNA;
		TerrainType rainforest = new TerrainType(new Selector().setFeature(TEMPERATURE, 0.1, 1.0)
				.setFeature(HUMIDITY, 0.2, 1)
				.setCondition(BASIC, waterBorder, 1), new Color(0.0f,0.5f,0f));
=======
		
		TerrainType rainforest = new TerrainType("rainforest",new Selector().setFeature(TerrainFeature.TEMPERATURE, 0.1, 1.0)
				.setFeature(TerrainFeature.HUMIDITY, 0.1, 1)
				.setCondition(TerrainFeature.BASIC, waterBorder, 1), new Color(0f,0.5f,0f));
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		rainforest.descriptor.representationBiome = Biome.JUNGLE;
<<<<<<< HEAD
		TerrainType plains = new TerrainType(new Selector().setFeature(TEMPERATURE, -0.15, 0.15)
				.setFeature(HUMIDITY, -0.05, 0.1)
				.setCondition(BASIC, waterBorder, 1), new Color(0f,1f,0f));
		TerrainType forest = new TerrainType(new Selector().setFeature(TEMPERATURE, -0.15, 0.15)
				.setFeature(HUMIDITY, 0.1, 0.2)
				.setCondition(BASIC, waterBorder, 1), new Color(0f,0.75f,0f));
=======
		
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
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		forest.descriptor.representationBiome = Biome.FOREST;
<<<<<<< HEAD
		TerrainType swamp = new TerrainType(new Selector().setFeature(TEMPERATURE, -0.05, 0.05)
				.setFeature(HUMIDITY, 0.2, 1)
				.setCondition(BASIC, waterBorder, 1), new Color(0.15f,0.25f,0f));
=======
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
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		swamp.descriptor.representationBiome = Biome.SWAMPLAND;
<<<<<<< HEAD
		TerrainType ocean = new TerrainType(new Selector()
				.setCondition(BASIC, 0, waterBorder)
				.setFeature(TEMPERATURE, -0.2, 1), new Color(0f,0f,0.5f));
=======
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
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		ocean.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.DIRT.getId()));
		ocean.descriptor.representationBiome = Biome.OCEAN;
<<<<<<< HEAD
		TerrainType frozenOcean = new TerrainType(new Selector()
				.setCondition(BASIC, 0, waterBorder)
				.setFeature(TEMPERATURE, -1, -0.3), new Color(0f,0f,1f));
=======
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
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		frozenOcean.descriptor.blocks.set(0, new MaterialDescriptor(1, Material.DIRT.getId()));
		frozenOcean.descriptor.representationBiome = Biome.FROZEN_OCEAN;
		frozenOcean.descriptor.modifier = new SurfaceModifier() {
			@Override public void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, float strengh, byte[][] result) {
				double v = NoiseHelper.simplexNoise2D(x, z, 0.05, 0.5, 2, 8)*0.5+0.5;
<<<<<<< HEAD
				if (v > (-data.properties[TEMPERATURE]-0.2)*2)
=======
				if (v > (-data.properties[TerrainFeature.TEMPERATURE.ordinal()]-0.2)*0.2)
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
					Main.chunkGenerator.setBlock(result, x-xOffset, 64, z-zOffset, (byte)Material.ICE.getId());
			}
			@SuppressWarnings("deprecation") @Override public void onPopulation(TerrainData data, int x, int z, World w) {
			}
		};
<<<<<<< HEAD
		terrainTypes.add(mountains); //Schnee
=======
		
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
		terrainTypes.add(snow); //Schnee
		terrainTypes.add(snowForest); //Schneewald
		terrainTypes.add(desert); //Wüste
		terrainTypes.add(savanna); //Savanne
		terrainTypes.add(steppe); //Steppe
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
	
	Modifier mountainModifier = new Modifier(0);
	{
		mountainModifier.addCos(0.25, 0.35, 0, 1);
		mountainModifier.addConst(0.35, 1, 1);
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
			
			float t = (float) NoiseHelper.simplexNoise2D(x, y, 0.0035*zoomFactor, 0.55, 2, 8, temperatureNoise);
			t += (float) NoiseHelper.simplexNoise2D(x, y, 1*zoomFactor, 0.5, 2, 2, temperatureNoise)*0.025;
			float h = 0;
			if (f > 0.175){
				float mountainSelect = (float) (NoiseHelper.simplexNoise2D(x, y, 0.005*zoomFactor, 0.5, 2, 8));
				if (mountainSelect > 0.25){
					mountainSelect = (float) (mountainModifier.modify(1)
							*(1-Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.05*zoomFactor, 0.5, 2, 2)))
							*(1-0.4*Math.abs(NoiseHelper.simplexNoise2D(x+10000, y, 0.15*zoomFactor, 0.5, 2, 2))));
					t = 0.5f*t+0.5f;
					t *= (1-0.3*mountainSelect);
					t = 2*t-1;
				}
				h = (float) NoiseHelper.simplexNoise2D(x, y, 0.0035*zoomFactor, 0.55, 2, 8, humidityNoise);
				h += (float) NoiseHelper.simplexNoise2D(x, y, 1*zoomFactor, 0.5, 2, 2, humidityNoise)*0.025;
				return new TerrainData(getTypeInfo(f, t, h, 0), f, t, h, 0);
			}
			return new TerrainData(getTypeInfo(f, t, 0, 0), f, t, 0, 0);
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
		
		float t = (float) NoiseHelper.simplexNoise2D(x, y, 0.005*zoomFactor, 0.6, 2, 8, temperatureNoise);
		t += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, temperatureNoise)*0.025;
		float h = 0;
		if (f > 0.175){
			h = (float) NoiseHelper.simplexNoise2D(x, y, 0.005*zoomFactor, 0.6, 2, 8, humidityNoise);
			h += (float) NoiseHelper.simplexNoise2D(x, y, 4*zoomFactor, 0.5, 2, 2, humidityNoise)*0.025;
			return new TerrainData(getTypeInfo(f, t, h), f, t, h);
		}
		return new TerrainData(getTypeInfo(f, t, 0), f, t, 0);
	}
}

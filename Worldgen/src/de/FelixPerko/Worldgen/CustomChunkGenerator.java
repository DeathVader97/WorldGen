package de.FelixPerko.Worldgen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import de.FelixPerko.Worldgen.Interpolation.Modifier;
import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;
import de.FelixPerko.Worldgen.Utils.Pair;

public class CustomChunkGenerator extends ChunkGenerator{
	
	public static OpenSimplexNoise defaultNoise = new OpenSimplexNoise();
	
	public static Modifier heightModifier = new Modifier(0);
	public static Modifier onIsleLineModifier = new Modifier(0);
	public static Modifier isleLineModifier = new Modifier(0);
	public static Modifier isleGeneralModifier = new Modifier(0);
	public static Modifier isleHeightModifier = new Modifier(0);
	public static int waterHeight = 64;
	
	static {
		heightModifier.addCos(0, 0.23, 32, 70);
		heightModifier.addCos(0.23, 1, 70, 100);

		onIsleLineModifier.addConst(0, 0.1, 1);
		onIsleLineModifier.addCos(0.1, 0.15, 1, 0);
		
		isleLineModifier.addCos(0.1, 0.5, 0, 0.5);
		isleLineModifier.addConst(0.5, 1, 0.5);
		
		isleGeneralModifier.addCos(0.3, 0.5, 0, 0.25);
		isleGeneralModifier.addConst(0.5, 1, 0.25);

		isleHeightModifier.addCos(0.04, 0.07, 0, 1);
		isleHeightModifier.addConst(0.07, 0.1, 1);
		isleHeightModifier.addCos(0.1, 0.175, 1, 0);
		isleHeightModifier.addConst(0.175, 1, 0);
		
	}
	
	public final static double ZOOM_FACTOR = 0.05;
	
	public HashMap<Pair<Integer, Integer>,TerrainData[][]> data = new HashMap<>();
	
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator) getPopulator());
    }
	
	@Override
	public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
		byte[][] result = new byte[world.getMaxHeight() / 16][]; //world height / chunk part height (=16, look above)
		result[0] = new byte[4096];
		int xOffset = chunkX*16;
		int zOffset = chunkZ*16;
		
		TerrainData[][] dataMap = new TerrainData[16][];
		
		for (int x = xOffset ; x < xOffset+16 ; x++){
			dataMap[x-xOffset] = new TerrainData[16];
			for (int z = zOffset ; z < zOffset+16 ; z++){
				TerrainData data = Main.generator.getData(ZOOM_FACTOR, x, z);
				dataMap[x-xOffset][z-zOffset] = data;
				biomes.setBiome(x-xOffset, z-zOffset, data.type.descriptor.representationBiome);
<<<<<<< HEAD
				if (data.type.descriptor.modifier != null)
					data.type.descriptor.modifier.onGeneration(data, x, z, xOffset, zOffset, result);
				double terrainHeight = heightModifier.modify(data.properties[TerrainFeature.BASIC]);
=======
				
				Pair<Integer, Float>[] values = Main.generator.biomeGrid.getValue((int)(x), (int)(z));
				for (Pair<Integer, Float> pair : values){
					if (pair.getSecond() <= 0)
						break;
					if (Main.generator.terrainTypes.get(pair.getFirst()).descriptor.modifier != null)
						Main.generator.terrainTypes.get(pair.getFirst()).descriptor.modifier.onGeneration(data, x, z, xOffset, zOffset, pair.getSecond(), result);
				}
				
//				if (data.type.descriptor.modifier != null)
//					data.type.descriptor.modifier.onGeneration(data, x, z, xOffset, zOffset, result);
				double terrainHeight = heightModifier.modify(data.properties[TerrainFeature.BASIC.ordinal()]);
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
				int currentY = (int) terrainHeight;
				for (int i = waterHeight ; i > (int)terrainHeight ; i--)
					setBlock(result, x-xOffset, i, z-zOffset, (byte)Material.WATER.getId());
				for (MaterialDescriptor desc : data.type.descriptor.blocks){
					for (int i = 0 ; i < desc.depth ; i++){
						setBlock(result, x-xOffset, currentY, z-zOffset, (byte)desc.material);
						currentY--;
						if (currentY < 0)
							break;
					}
					if (currentY < 0)
						break;
				}
			}
		}
		data.put(new Pair<Integer, Integer>(chunkX, chunkZ), dataMap);
		return result;
//				float value = (float)NoiseHelper.simplexNoise2D(x, z, 0.003*zoomFactor, 0.5, 2, 8);
//				if (value < 0)
//					value = -value;
//				
//				if (value < 0.175){
//					float isleNoise = (float) Math.abs(NoiseHelper.simplexNoise2D(x, z, 0.05*zoomFactor, 0.5, 2, 6));
//					double isleLineValue = CustomChunkGenerator.onIsleLineModifier.modify(Math.abs(NoiseHelper.simplexNoise2D(x, z, 0.01*zoomFactor, 0.5, 2, 1)));
//					double multValue = (CustomChunkGenerator.isleLineModifier.modify(isleNoise)*isleLineValue+CustomChunkGenerator.isleGeneralModifier.modify(isleNoise)*(1-isleLineValue));
//					value += 0.4*multValue*CustomChunkGenerator.isleHeightModifier.modify(value);
//					if (onIsleLine){
//						if (isleNoise > 0.4)
//							value += isleNoise/3;
//					} else if (isleNoise > 0.6){
//						value = isleNoise/4;
//					}
//					if (value < 0.175)
//						value = 0;
//				}
//				double yd = heightModifier.modify(value);
//				for (int y = (int)yd ; y > 0 ; y--){
//					setBlock(result, x-xOffset, y, z-zOffset, (byte)Material.DIRT.getId());
//				}
//				if (yd < 64){
//					for (int y = 64 ; y > (int)yd ; y--){
//						setBlock(result, x-xOffset, y, z-zOffset, (byte)Material.WATER.getId());
//					}
//				}
		
//		byte[][] result = new byte[world.getMaxHeight() / 16][]; //world height / chunk part height (=16, look above)
////		result[0] = new byte[4096];
//		int xOffset = chunkX*16;
//		int zOffset = chunkZ*16;
//		for (int x = 0 ; x < 16 ; x++){
//			for (int z = 0 ; z < 16 ; z++){
//				double value = 0;
//				{
//					double frequency = 0.001;
//					double amplitude = 0.5;
//					for (int o = 0 ; o < 8 ; o++){
//						value += defaultNoise.eval((x+xOffset)*frequency,(z+zOffset)*frequency)*amplitude;
//						frequency *= 2;
//						amplitude *= 0.5;
//					}
//				}
//				double temperature = 0;
//				{
//					double frequency = 0.00005;
//					double amplitude = 0.5;
//					for (int o = 0 ; o < 4 ; o++){
//						temperature += defaultNoise.eval((x+xOffset)*frequency+10000,(z+zOffset)*frequency-10000)*amplitude;
//						frequency *= 2;
//						amplitude *= 0.5;
//					}
//				}
//				double humidity = 0;
//				{
//					double frequency = 0.0001;
//					double amplitude = 0.5;
//					for (int o = 0 ; o < 8 ; o++){
//						humidity += defaultNoise.eval((x+xOffset)*frequency-10000,(z+zOffset)*frequency+10000)*amplitude;
//						frequency *= 2;
//						amplitude *= 0.5;
//					}
//				}
//				value = -value;
//				boolean negative  = value < 0;
//				value = value*value;
//				double yd;
//				int y;
//				if (!negative){
//					temperature -= value;
//					yd = (70+value*280);
//					y = (int)(70+value*280);
//				}
//				else {
//					value = -value;
//					yd = (70+value*140);
//					y = (int)(70+value*140);
//				}
//				boolean lower = yd-y <= 0.5;
//				double matR = Math.random();
//				if (y > 255)
//					y = 255;
//				if (y < 0)
//					y = 0;
//				setBiome(biomes, x, yd, z, temperature, humidity, matR);
//				if (y >= 64){
//					
//					if (yd < 67.5){
//						setBlock(result, x, y, z, (byte)Material.SAND.getId());
//						if ((humidity >= 0.15 + matR * 0.05) && (temperature > -0.35 + matR * 0.05) && (temperature < 0.1 + matR * 0.05))
//							setBlock(result, x, y, z, (byte)Material.GRASS.getId());
//						else if (temperature < -0.35 + matR * 0.05)
//							setBlock(result, x, y+1, z, (byte)Material.SNOW.getId());//snow
//					} else {
//						if (temperature <= -0.35 + matR * 0.05){
//							setBlock(result, x, y+1, z, (byte)Material.SNOW.getId());//snow
//							setBlock(result, x, y, z, (byte)Material.GRASS.getId());//snow
//						}else if (temperature >= 0.1 + matR * 0.05){
//							if (humidity >= 0.1 + matR * 0.05)
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());//rainforest
//							else if (humidity >= -0.15 + matR * 0.05)
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());//savanna
//							else if (humidity <= -0.15 + matR * 0.05)
//								setBlock(result, x, y, z, (byte)Material.SAND.getId());//desert
//							else
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());
//						}else{
//							if (humidity >= 0.15 + matR * 0.05)
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());//swamp
//							else if (humidity >= -0.15 + matR * 0.05)
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());//forest
//							else
//								setBlock(result, x, y, z, (byte)Material.GRASS.getId());//plains
//						}
//					}
//					
////					if (yd < 68.5)
////						setBlock(result, x, y, z, (byte)Material.SAND.getId());
////					else{
////						if (temperature > 0.25+matR*0.1)
////							setBlock(result, x, y, z, (byte)Material.SAND.getId());
////						else if (temperature > (-0.25+matR*0.1))
////							setBlock(result, x, y, z, (byte)Material.GRASS.getId());
////						else
////							setBlock(result, x, y, z, (byte)Material.SNOW_BLOCK.getId());
////					}
//				}else {
//					setBlock(result, x, y, z, (byte)Material.SAND.getId());
//					if (temperature < -0.35 + matR * 0.05){
//						setBlock(result, x, 64, z, (byte)Material.ICE.getId());
//						for (int y2 = 63 ; y2 > y ; y2--){
//							setBlock(result, x, y2, z, (byte)Material.WATER.getId());
//						}
//					} else {
//						for (int y2 = 64 ; y2 > y ; y2--){
//							setBlock(result, x, y2, z, (byte)Material.WATER.getId());
//						}
//					}
//				}
//				for (int y2 = y-1 ; y2 >= 0 ; y2--){
//					double caveValue = Math.abs(NoiseHelper.simplexNoise3D(x+xOffset, y2, z+zOffset, 0.01, 0.5, 2, 4))
//							+Math.abs(NoiseHelper.simplexNoise3D(x+xOffset, y2+10000, z+zOffset, 0.01, 0.5, 2, 4));
//					if (caveValue > 0.15){
//						int d = y-y2;
//						if (d <= 2)
//							setBlock(result, x, y2, z, (byte)Material.DIRT.getId());
//						else
//							setBlock(result, x, y2, z, (byte)Material.STONE.getId());
//					}
//				}
//
////				double frequency2 = 0.005;
////				double amplitude2 = 1;
////				double vegetation = (Noise.noise((x+xOffset+4332)*frequency2,(z+zOffset-10978)*frequency2)*amplitude2)*0.5+0.5;
////				if (y < 64)
////					vegetation = 0;
////				if (Math.random() <= vegetation){
////					setBlock(result, x, y+1, z, (byte)Material.LONG_GRASS.getId());
////				}
//			}
//		}
//		return result;
	}
	
	private void setBiome(BiomeGrid biomes, int x, double y, int z, double temperature, double humidity, double matR) {
		if (y < 64){
			biomes.setBiome(x, z, Biome.OCEAN);
		}
		else if (y < 67){
			if ((humidity >= 0.15 + matR * 0.05) && (temperature > -0.35 + matR * 0.05) && (temperature < 0.1 + matR * 0.05)){
				biomes.setBiome(x, z, Biome.SWAMPLAND);//swamp
			} else
				biomes.setBiome(x, z, Biome.BEACHES);
		} else {
			if (temperature <= -0.3 + matR * 0.075){
				biomes.setBiome(x, z, Biome.EXTREME_HILLS);//snow
			}else if (temperature >= 0.05 + matR * 0.075){
				if (humidity >= 0.1 + matR * 0.075)
					biomes.setBiome(x, z, Biome.JUNGLE);//rainforest
				else if (humidity >= -0.1 + matR * 0.075)
					biomes.setBiome(x, z, Biome.SAVANNA);//savanna
				else
					biomes.setBiome(x, z, Biome.DESERT);//desert
			}else{
				if (humidity >= 0.1 + matR * 0.075){
					if (y > 75 + matR * 2){
						biomes.setBiome(x, z, Biome.FOREST);
					} else 
						biomes.setBiome(x, z, Biome.SWAMPLAND);//swamp
				} else if (humidity >= -0.1 + matR * 0.075)
					biomes.setBiome(x, z, Biome.FOREST);//forest
				else
					biomes.setBiome(x, z, Biome.PLAINS);//plains
			}
//			else if (temperature > 0.15 + matR * 0.1)
//				biomes.setBiome(x, z, Biome.DESERT);
//			else if (temperature > -0.15)
//				biomes.setBiome(x, z, Biome.PLAINS);
//			else
//				biomes.setBiome(x, z, Biome.EXTREME_HILLS);
		}
	}

	public void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
	    // is this chunk part already initialized?
	    if (result[y >> 4] == null) {
	        // Initialize the chunk part
	        result[y >> 4] = new byte[4096];
	    }
	    // set the block (look above, how this is done)
	    result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}

	public BlockPopulator getPopulator() {
		return new BlockPopulator() {
			
			@Override
			public void populate(World w, Random r, Chunk c) {
				int chunkX = c.getX();
				int chunkZ = c.getZ();
				int offsetX = chunkX*16;
				int offsetZ = chunkZ*16;
				TerrainData[][] map = data.get(new Pair<Integer, Integer>(chunkX, chunkZ));
				
				for (int x = offsetX ; x < offsetX+16 ; x++){
					for (int z = offsetZ ; z < offsetZ+16 ; z++){
						TerrainData data = map[x-offsetX][z-offsetZ];
						if (data.type.descriptor.modifier != null)
							data.type.descriptor.modifier.onPopulation(data, x, z, w);
<<<<<<< HEAD
						double terrainHeight = heightModifier.modify(data.properties[TerrainFeature.BASIC]);
						if (w.getBlockAt(x, (int)terrainHeight, z).getType() == Material.GRASS){
							Block b = w.getBlockAt(x, (int)terrainHeight+1 , z);
							b.setType(Material.LONG_GRASS);
							b.setData((byte) 1);
=======
						double terrainHeight = heightModifier.modify(data.getProperty(TerrainFeature.BASIC));
						
						if (terrainHeight > 64){
							for (DensityBasedBlock b : Main.generator.densityBasedBlocks){
								if (b.process(w, x, (int)terrainHeight+1, z, data.properties))
									break;
							}
>>>>>>> branch 'master' of https://github.com/DeathVader97/WorldGen.git
						}
						
//						if (w.getBlockAt(x, (int)terrainHeight, z).getType() == Material.GRASS){
//							Block b = w.getBlockAt(x, (int)terrainHeight+1 , z);
//							b.setType(Material.LONG_GRASS);
//							b.setData((byte) 1);
//						}
					}
				}
			}
		};
	}
}

package de.FelixPerko.Worldgen;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import de.FelixPerko.Worldgen.Interpolation.Modifier;
import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;

public class CustomChunkGenerator extends ChunkGenerator{
	
	public static OpenSimplexNoise defaultNoise = new OpenSimplexNoise();
	
	public static Modifier heightModifier = new Modifier(0);
	public static Modifier onIsleLineModifier = new Modifier(0);
	public static Modifier isleLineModifier = new Modifier(0);
	public static Modifier isleGeneralModifier = new Modifier(0);
	public static Modifier isleHeightModifier = new Modifier(1);
	
	static {
		heightModifier.addCos(0, 0.23, 32, 70);
		heightModifier.addCos(0.23, 1, 70, 100);

		onIsleLineModifier.addConst(0, 0.1, 1);
		onIsleLineModifier.addCos(0.1, 0.15, 1, 0);
		
		isleLineModifier.addCos(0.1, 0.5, 0, 0.5);
		isleLineModifier.addConst(0.5, 1, 0.5);
		
		isleGeneralModifier.addCos(0.3, 0.5, 0, 0.25);
		isleGeneralModifier.addConst(0.5, 1, 0.25);
		
		isleHeightModifier.addCos(0.1, 0.175, 1, 0);
		isleHeightModifier.addConst(0.175, 1, 0);
		
	}
	
	@Override
	public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
		byte[][] result = new byte[world.getMaxHeight() / 16][]; //world height / chunk part height (=16, look above)
		result[0] = new byte[4096];
		int xOffset = chunkX*16;
		int zOffset = chunkZ*16;
		double zoomFactor = 0.05;
		for (int x = xOffset ; x < xOffset+16 ; x++){
			for (int z = zOffset ; z < zOffset+16 ; z++){
				float value = (float)NoiseHelper.simplexNoise2D(x, z, 0.003*zoomFactor, 0.5, 2, 8);
				if (value < 0)
					value = -value;
				
				if (value < 0.175){
					float isleNoise = (float) Math.abs(NoiseHelper.simplexNoise2D(x, z, 0.05*zoomFactor, 0.5, 2, 6));
					double isleLineValue = CustomChunkGenerator.onIsleLineModifier.modify(Math.abs(NoiseHelper.simplexNoise2D(x, z, 0.01*zoomFactor, 0.5, 2, 1)));
					double multValue = (CustomChunkGenerator.isleLineModifier.modify(isleNoise)*isleLineValue+CustomChunkGenerator.isleGeneralModifier.modify(isleNoise)*(1-isleLineValue));
					value += 0.4*multValue*CustomChunkGenerator.isleHeightModifier.modify(value);
//					if (onIsleLine){
//						if (isleNoise > 0.4)
//							value += isleNoise/3;
//					} else if (isleNoise > 0.6){
//						value = isleNoise/4;
//					}
//					if (value < 0.175)
//						value = 0;
				}
				double yd = heightModifier.modify(value);
				for (int y = (int)yd ; y > 0 ; y--){
					setBlock(result, x-xOffset, y, z-zOffset, (byte)Material.DIRT.getId());
				}
				if (yd < 64){
					for (int y = 64 ; y > (int)yd ; y--){
						setBlock(result, x-xOffset, y, z-zOffset, (byte)Material.WATER.getId());
					}
				}
			}
		}
		
		return result;
		
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
}

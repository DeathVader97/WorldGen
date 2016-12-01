package de.FelixPerko.Worldgen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import de.FelixPerko.Worldgen.Functions.Function;
import de.FelixPerko.Worldgen.Functions.PolynomalFunction;
import de.FelixPerko.Worldgen.Functions.CombinedFunctions.CombinedDivideFunction;
import de.FelixPerko.Worldgen.Functions.CombinedFunctions.CombinedMultiplyFunction;
import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;

public class Main extends JavaPlugin{
	
	public static int TEST_BIOME_MAP = 0;
	public static int TEST_BIOME_MAP_OLD = 1;
	public static int TEST_ICE_SHEETS = 2;
	public static int TEST_FUNCTION = 3;
	
	static int test = 3;
	
	public static TerrainGenerator generator = new DefaultGenerator(43);
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new CustomChunkGenerator();
	}
	
	public static void main(String[] args) {
		displayImage();
	}

	private static void displayImage() {
		int size = 700;
		BufferedImage img = new BufferedImage((int)(size), (int)(size), BufferedImage.TYPE_INT_RGB);
		JFrame f = new JFrame();
		JLabel label = new JLabel(new ImageIcon(img));
		f.add(label);
		f.setVisible(true);
		f.setSize(size, size);
		double z = 0;
		long t1 = 0;
		int i = 0;
		long secCounter = 0;
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calcImage(img, size, z);
		while (!Thread.interrupted()){
			t1 = System.currentTimeMillis();
			z += 0.005;
			if (z > 0.8)
				z = 0;
//			System.out.println(z);
			
//			calcImage(img, size, z);
			label.setIcon(new ImageIcon(img));
				
			try{
				Thread.sleep(1000);
			} catch (Exception e){
				e.printStackTrace();
			}
			secCounter += System.currentTimeMillis()-t1;
			i++;
			if (secCounter > 5000){
				System.out.println("FPS: "+i/(secCounter/1000.0));
				secCounter = 0;
				i = 0;
			}
		}
	}
	
	private static void calcImage(BufferedImage img, int size, double z) {
		double zoomFactor = 3;
		double fBefore = Double.NaN;
		Modifier m = new Modifier(0);
		m.addCos(0, 0.7, 0, 0.2).addFunc(0.7, 1,
				new CombinedMultiplyFunction(
						new PolynomalFunction(2.0/0.9),
						new PolynomalFunction(1,-2,1)));
		for (int x = -size/2 ; x < size/2 ; x++){
			double funX = x*(1.0/size)*2.1;
			double modifierValue = m.modify(funX);
			double yFunction = (-modifierValue/(1.0/size)/2.175);
			for (int y = -size/2 ; y < size/2 ; y++){
				
				if (test == TEST_FUNCTION){
					if (x == 0 || y == 0)
						img.setRGB(x+size/2, y+size/2, new Color(0f,0f,0f).getRGB());
					else if (Math.abs(funX-1) <= 0.002 || Math.abs(yFunction-1) <= 0.02)
						img.setRGB(x+size/2, y+size/2, new Color(0f,1f,0f).getRGB());
					else if (Math.abs(yFunction-y) <= 2)
						img.setRGB(x+size/2, y+size/2, new Color(1f,0f,0f).getRGB());
					else
						img.setRGB(x+size/2, y+size/2, new Color(1f,1f,1f).getRGB());
				}
				
				if (test == TEST_ICE_SHEETS){
					double v = NoiseHelper.simplexNoise2D(x, y, 0.05, 0.5, 2, 8);
					if (v < 0)
						v = -v;
					if (v*2+0.2 > (double)Math.sqrt((x*x)+(y*y))/(size*0.5)){
						img.setRGB(x+size/2, y+size/2, new Color(0.0f,0.0f,1f).getRGB()); //frozen ocean
					} else {
						img.setRGB(x+size/2, y+size/2, new Color(0f,0f,0.5f).getRGB()); //ocean
					}
				}
					
				if (test == TEST_BIOME_MAP || test == TEST_BIOME_MAP_OLD){
					TerrainData data = generator.getData(zoomFactor, x, y);
					if (Math.abs(x) == 0 || Math.abs(y) == 0)
						img.setRGB(x+size/2, y+size/2, new Color((float)data.properties[TerrainFeature.BASIC.ordinal()],0,0).getRGB());
					else
						img.setRGB(x+size/2, y+size/2, data.type.color.getRGB());
				
					if (test == TEST_BIOME_MAP_OLD){
						double f = data.properties[TerrainFeature.BASIC.ordinal()];
						double t = data.properties[TerrainFeature.TEMPERATURE.ordinal()];
						double h = data.properties[TerrainFeature.HUMIDITY.ordinal()];
						if (f > 0.175){
							if (t < -0.15){
								img.setRGB(x+size/2, y+size/2, new Color(1f,1f,1f).getRGB()); //snow
							}
							else if (t > 0.15){
								if (h < -0.1){
									img.setRGB(x+size/2, y+size/2, new Color(1f,1f,0f).getRGB()); //wüste
								}else if (h > 0.0){
									img.setRGB(x+size/2, y+size/2, new Color(0f,0.5f,0f).getRGB()); //regenwald
								}else{
									img.setRGB(x+size/2, y+size/2, new Color(1f,0.5f,0f).getRGB()); //Savanne
								}
							} else {
								if (h > 0.25){
									img.setRGB(x+size/2, y+size/2, new Color(0.15f,0.25f,0f).getRGB()); //Swamp
								}else if (h > 0){
									img.setRGB(x+size/2, y+size/2, new Color(0f,0.75f,0f).getRGB()); //Wald
								} else if (h < -0.25){
									img.setRGB(x+size/2, y+size/2, new Color(1f,0.5f,0f).getRGB()); //Savanne
								} else {
									img.setRGB(x+size/2, y+size/2, new Color(0f,1f,0f).getRGB()); //Wiese
								}
							}
						} else if (t < -0.3){
							img.setRGB(x+size/2, y+size/2, new Color(0.0f,0.0f,1f).getRGB()); //frozen ocean
						} else {
							img.setRGB(x+size/2, y+size/2, new Color(0f,0f,0.5f).getRGB()); //ocean
						}
					}
				}
			}
			fBefore = yFunction;
		}
	}
	
	private static float clamp(float value, float lowerBorder, float higherBorder){
		if (value > higherBorder)
			return higherBorder;
		if (value < lowerBorder)
			return lowerBorder;
		return value;
	}
}

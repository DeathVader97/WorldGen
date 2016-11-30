package de.FelixPerko.Worldgen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;

public class Main extends JavaPlugin{
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new CustomChunkGenerator();
	}
	
	public static void main(String[] args) {
		displayImage();
	}

	private static void displayImage() {
		int size = 1000;
		BufferedImage img = new BufferedImage((int)(size), (int)(size), BufferedImage.TYPE_INT_RGB);
		calcImage(img, size, 0);
		JFrame f = new JFrame();
		JLabel label = new JLabel(new ImageIcon(img));
		f.add(label);
		f.setVisible(true);
		f.setSize(size, size);
		double z = 8;
//		{
//			OpenSimplexNoise openSN = new OpenSimplexNoise();
//			long t1 = System.nanoTime();
//			for (int x = 0 ; x < 1000 ; x++){
//				for (int y = 0 ; y < 1000 ; y++){
//					openSN.eval(x, y);
//				}
//			}
//			long t2 = System.nanoTime();
//			for (int x = 0 ; x < 1000 ; x++){
//				for (int y = 0 ; y < 1000 ; y++){
//					openSN.eval(x, y, 0);
//				}
//			}
//			long t3 = System.nanoTime();
//			System.out.println(t2-t1);
//			System.out.println(t3-t2);
//		}
		long t1 = 0;
		int i = 0;
		long secCounter = 0;
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calcImage(img, size, z);
		while (!Thread.interrupted()){
			t1 = System.currentTimeMillis();
//			z += 1;
//			if (z < 1)
//				z = 1;
//			if (z > 16)
//				z = 16;
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
	
	public static long seed = 42;
	public static Random rand = new Random(seed);
	public static OpenSimplexNoise baseNoise = new OpenSimplexNoise(rand.nextLong());
	public static OpenSimplexNoise temperatureNoise = new OpenSimplexNoise(rand.nextLong());
	public static OpenSimplexNoise humidityNoise = new OpenSimplexNoise(rand.nextLong());
	public static OpenSimplexNoise isleNoise = new OpenSimplexNoise(rand.nextLong());
	public static OpenSimplexNoise isleLineNoise = new OpenSimplexNoise(rand.nextLong());

	private static void calcImage(BufferedImage img, int size, double z) {
		double zoomFactor = 2;
		for (int x = -size/2 ; x < size/2 ; x++){
			for (int y = -size/2 ; y < size/2 ; y++){
				float f = (float)NoiseHelper.simplexNoise2D(x, y, 0.003*zoomFactor, 0.5, 2, (int)z, baseNoise);
				if (f < 0)
					f = -f;
//				f *= 0.5f;
//				f += 0.5f;
				
//				if (f % 0.05 < 0.025)
//					f = 1;
//				else
//					f = 0;
				
				if (f < 0.175 && f > 0.05){
					float isleNoise = (float) Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.05*zoomFactor, 0.5, 2, 6, Main.isleNoise));
					double lineValue = Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.01*zoomFactor, 0.5, 2, 1, isleLineNoise));
					double isleLineValue = CustomChunkGenerator.onIsleLineModifier.modify(lineValue);
					double multValue = (CustomChunkGenerator.isleLineModifier.modify(isleNoise)*isleLineValue+CustomChunkGenerator.isleGeneralModifier.modify(isleNoise)*(1-isleLineValue));
					f += 0.8*multValue*CustomChunkGenerator.isleHeightModifier.modify(f);
//					while (f > 0.2){
//						f *= 0.9;
//					}
				}
				if (f < 0.175)
					f = 0;
//				if (NoiseHelper.simplexNoise2DSelector(x, y, 0.01, 0.5, 2, (int)z, NoiseHelper.openSimplexNoise, 0.175, false, true))
//					f = 1;
				
				float t = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.5, 2, 8, temperatureNoise);
				float h = 0;
				if (f > 0.175){
					h = (float) NoiseHelper.simplexNoise2D(x, y, 0.002*zoomFactor, 0.5, 2, 8, humidityNoise);
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
				
				if (Math.abs(x) == 0 || Math.abs(y) == 0)
					img.setRGB(x+size/2, y+size/2, new Color(f,0,0).getRGB());
			}
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

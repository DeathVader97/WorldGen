package de.FelixPerko.Worldgen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Noise.OpenSimplexNoise;

public class Main {
	
	public static void main(String[] args) {
		long t1 = System.nanoTime();
		for (int i = 0 ; i < 100000000 ; i++){
			if (i == 10)
				i = 11;
			else
				i = i*1;
		}
		long t2 = System.nanoTime();

		for (int i = 0 ; i < 100000000 ; i++){
			i = (i == 10) ? 11 : i*1;
		}
		long t3 = System.nanoTime();
		System.out.println(t3-t2);
		System.out.println(t2-t1);
//		displayImage();
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

	private static void calcImage(BufferedImage img, int size, double z) {
		double zoomFactor = 2;
		for (int x = 0 ; x < size ; x++){
			for (int y = 0 ; y < size ; y++){
				float f = (float)NoiseHelper.simplexNoise2D(x, y, 0.003*zoomFactor, 0.5, 2, (int)z);
				if (f < 0)
					f = -f;
//				f *= 0.5f;
//				f += 0.5f;
				
//				if (f % 0.05 < 0.025)
//					f = 1;
//				else
//					f = 0;
				
				if (f < 0.175){
					float isleNoise = (float) Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.075*zoomFactor, 0.5, 2, 3));
					if (Math.abs(NoiseHelper.simplexNoise2D(x, y, 0.01*zoomFactor, 0.5, 2, 1)) < 0.1){
						if (isleNoise > 0.4)
							f += isleNoise/3;
						else f = 0;
					} else if (isleNoise > 0.6){
						f = isleNoise/4;
					}
					if (f < 0.175)
						f = 0;
				}
//				if (NoiseHelper.simplexNoise2DSelector(x, y, 0.01, 0.5, 2, (int)z, NoiseHelper.openSimplexNoise, 0.175, false, true))
//					f = 1;
				
				double b = 0;
				if (f > 0){
					b = NoiseHelper.simplexNoise2D(x, y, 0.0015*zoomFactor, 0.5, 2, 8);
					
//					if (x % 4 == 0 || y % 4 == 0)
//						f = clamp(f-0.5f,0,1);
				}
				img.setRGB(x, y, new Color(clamp((float)(b+f),0,1),f,clamp((float)(-b+f),0,1)).getRGB());
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

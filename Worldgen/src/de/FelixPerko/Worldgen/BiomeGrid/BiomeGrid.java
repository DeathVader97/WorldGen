package de.FelixPerko.Worldgen.BiomeGrid;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import de.FelixPerko.Worldgen.CustomChunkGenerator;
import de.FelixPerko.Worldgen.Main;
import de.FelixPerko.Worldgen.Utils.Pair;

public class BiomeGrid {
	int size = 10;
	int smoothRad = 2;
	HashMap<Pair<Integer, Integer>, Integer> map = new HashMap<>();
	HashMap<Pair<Integer, Integer>, float[]> smoothMap = new HashMap<>();
	
	private float[] getSmoothChunkValues(int x, int y){
		float[] v = smoothMap.get(new Pair<Integer, Integer>(x, y));
		if (v == null){
			v = generateSmoothChunkValues(x,y,smoothRad);
			smoothMap.put(new Pair<Integer, Integer>(x,y), v);
		}
		return v;
	}
	
	private Integer getRawChunkValue(int x, int y) {
		Integer v = map.get(new Pair<Integer, Integer>(x, y));
		if (v == null){
			v = generateRawChunkValue(x,y);
			map.put(new Pair<Integer, Integer>(x,y), v);
		}
		return v;
	}
	
	public float[] generateSmoothChunkValues(int xp, int yp, int rad){
		float[] sum = new float[Main.generator.terrainTypes.size()];
		float amount = 0;
		for(int xo = -rad ; xo <= rad ; xo++){
			for (int yo = -rad ; yo <= rad ; yo++){
				int x = xp+xo;
				int y = yp+yo;
				sum[getRawChunkValue(x,y)]++;
				amount++;
			}
		}
		for (int i = 0 ; i < sum.length ; i++)
			sum[i] /= amount;
		return sum;
	}

	private Integer generateRawChunkValue(int x, int y) {
		return Main
				.generator
				.getChunkData(CustomChunkGenerator.ZOOM_FACTOR, x, y)
				.type
				.id;
	}

//	public void setColors(BufferedImage img, int imgSize){
//		int gridImgSize = imgSize/size;
//		for (int x = 0 ; x < imgSize ; x++){
//			for (int y = 0 ; y < imgSize ; y++){
////				float v = points[x/gridImgSize][y/gridImgSize].smoothedValue;
//				float px = ((float)x)/gridImgSize;
//				float py = ((float)y)/gridImgSize;
//				int ix = (int)px;
//				int iy = (int)py;
//				float dx = px-ix;
//				float dy = py-iy;
//				float v = getValue(ix,iy,dx,dy);
////				if (x/gridImgSize < 2 || x/gridImgSize > size-3 || y/gridImgSize < 2 || y/gridImgSize > size-3)
////					img.setRGB(x, y, new Color(0,0,v).getRGB());
////				else
//					img.setRGB(x, y, new Color(v,v,v).getRGB());
//			}
//		}
//	}
	
	public Pair<Integer,Float>[] getValue(int blockX, int blockZ) {
		double x = blockX/16.0;
		double y = blockZ/16.0;
		int ix = (int)x;
		int iy = (int)y;
		float dx = (float)x-ix;
		float dy = (float)y-iy;
		
		float[] v1 = getSmoothChunkValues(ix, iy);
		float[] v2 = getSmoothChunkValues(ix+1, iy);
		float[] v3 = getSmoothChunkValues(ix, iy+1);
		float[] v4 = getSmoothChunkValues(ix+1, iy+1);
		Pair<Integer,Float>[] values = new Pair[v1.length];
		
		for (int i = 0 ; i < values.length ; i++){
			float val1 = v1[i];
			float val2 = v2[i];
			float val3 = v3[i];
			float val4 = v4[i];
			if (val1 == val2 && val1 == val3 && val1 == val4){
				values[i] = new Pair<Integer,Float>(i,val1);
				continue;
			}
			float x1 = lerp(val1,val2,dx);
			float x2 = lerp(val3,val4,dx);
			float y1 = lerp(val1,val3,dy);
			float y2 = lerp(val2,val4,dy);
			values[i] = new Pair<Integer,Float>(i,(lerp(x1,x2,dy)+lerp(y1,y2,dx))*0.5f);
		}
		
		Arrays.sort(values, new Comparator<Pair<Integer,Float>>() {
			@Override
			public int compare(Pair<Integer, Float> o1, Pair<Integer, Float> o2) {
				if (o1.getSecond() < o2.getSecond())
					return -1;
				if (o1.getSecond() > o2.getSecond())
					return 1;
				return 0;
			}
		});
		
		return values;
	}
	
	private float lerp(float v1, float v2, float d){
		return (1-d)*v1+(d)*v2;
	}
}

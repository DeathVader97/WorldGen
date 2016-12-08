package de.FelixPerko.Worldgen.BiomeGrid;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.FelixPerko.Worldgen.Utils.Pair;

public class BiomeGrid {
	int size = 10;
	HashMap<Pair<Integer, Integer>, Double> map = new HashMap<>();
	
	public BiomeGrid(float[][] v){
		for (int x = 0 ; x < size ; x++){
			for (int y = 0 ; y < size ; y++){
				points[x][y] = new GridPoint(v[y][x]);
			}
		}
		for (int x = 0 ; x < size ; x++){
			for (int y = 0 ; y < size ; y++){
				smooth(x,y,2);
			}
		}
	}
	
	public void smooth(int xp, int yp, int rad){
		float sum = 0;
		float amount = 0;
		for(int xo = -rad ; xo <= rad ; xo++){
			for (int yo = -rad ; yo <= rad ; yo++){
				int x = xp+xo;
				if (x < 0 || x >= size)
					continue;
				int y = yp+yo;
				if (y < 0 || y >= size)
					continue;
				sum += points[x][y].originalValue;
				amount++;
			}
		}
		points[xp][yp].smoothedValue = sum/amount;
	}
	
	public void setColors(BufferedImage img, int imgSize){
		int gridImgSize = imgSize/size;
		for (int x = 0 ; x < imgSize ; x++){
			for (int y = 0 ; y < imgSize ; y++){
//				float v = points[x/gridImgSize][y/gridImgSize].smoothedValue;
				float px = ((float)x)/gridImgSize;
				float py = ((float)y)/gridImgSize;
				int ix = (int)px;
				int iy = (int)py;
				float dx = px-ix;
				float dy = py-iy;
				float v = getValue(ix,iy,dx,dy);
//				if (x/gridImgSize < 2 || x/gridImgSize > size-3 || y/gridImgSize < 2 || y/gridImgSize > size-3)
//					img.setRGB(x, y, new Color(0,0,v).getRGB());
//				else
					img.setRGB(x, y, new Color(v,v,v).getRGB());
			}
		}
	}

	private float getValue(int ix, int iy, float dx, float dy) {
		if (ix < 0 || ix > size-2 || iy < 0 || iy > size-2)
			return 0;
		float v1 = points[ix][iy].smoothedValue;
		float v2 = points[ix+1][iy].smoothedValue;
		float v3 = points[ix][iy+1].smoothedValue;
		float v4 = points[ix+1][iy+1].smoothedValue;
		float x1 = lerp(v1,v2,dx);
		float x2 = lerp(v3,v4,dx);
		float y1 = lerp(v1,v3,dy);
		float y2 = lerp(v2,v4,dy);
		return (lerp(x1,x2,dy)+lerp(y1,y2,dx))*0.5f;
//		return (points[ix][iy].smoothedValue*(1-dx)+points[ix+1][iy].smoothedValue*(dx)+
//				points[ix][iy].smoothedValue*(1-dy)+points[ix][iy+1].smoothedValue*(dy))*0.5f;
	}
	
	private float lerp(float v1, float v2, float d){
		return (1-d)*v1+(d)*v2;
	}
}

package de.FelixPerko.Worldgen.Noise;

public class NoiseHelper {

	public static OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();
	
	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}
	
	public static boolean simplexNoise2DSelector(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise, double border, boolean lower, boolean higher){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double relV = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			relV = Math.abs(value/maxAmp);
			double d = relV - border;
			if (d < 0)
				d = -d;
			amplitude *= persistance;
			if (d > amplitude*persistance+amplitude){
//				System.out.println("got result after "+(o+1)+"/"+octaves);
				if (relV < border)
					return lower;
				return higher;
			}
			frequency *= lacunarity;
		}
		if (relV < border)
			return lower;
		return higher;
	}

	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency, z*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}

	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves) {
		return simplexNoise2D(x, y, frequency, persistance, lacunarity, octaves, openSimplexNoise);
	}

	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves) {
		return simplexNoise3D(x, y, z, frequency, persistance, lacunarity, octaves, openSimplexNoise);
	}
}

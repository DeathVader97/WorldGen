package de.FelixPerko.Worldgen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import de.FelixPerko.Worldgen.Functions.PolynomalFunction;
import de.FelixPerko.Worldgen.Functions.CombinedFunctions.CombinedMultiplyFunction;
import de.FelixPerko.Worldgen.Noise.NoiseHelper;
import de.FelixPerko.Worldgen.Utils.Pair;

public class Main extends JavaPlugin{
	
	public static int TEST_BIOME_MAP = 0;
	public static int TEST_BIOME_MAP_OLD = 1;
	public static int TEST_ICE_SHEETS = 2;
	public static int TEST_FUNCTION = 3;
	public static int TEST_DUNES = 4;
	public static int TEST_DUNES2 = 5;
	public static int TEST_BIOME_GRID = 6; //DISABLED
	
	static int test = 3;
	
	public static double IMG_ZOOM_FACTOR = 0.2*8;
	static int size = 1000;
	static int supersample = 1;
	
	public static TerrainGenerator generator = new DefaultGenerator(43);
	
	public static CustomChunkGenerator chunkGenerator = new CustomChunkGenerator();
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return chunkGenerator;
	}
	
	@Override
	public void onEnable(){
//        PluginManager pm = this.getServer().getPluginManager();
//        pm.registerEvent(WorldInitEvent.class, new Listener(){
//            public void onWorldInit(WorldInitEvent event) {
//                event.getWorld().getPopulators().add(chunkGenerator.getPopulator());
//            }
//        }, EventPriority.NORMAL, null, this);
    }

	
	public static void main(String[] args) {
		displayImage();
	}

	private static void displayImage() {
		IMG_ZOOM_FACTOR /= supersample;
		BufferedImage img = new BufferedImage((int)(size), (int)(size), BufferedImage.TYPE_INT_RGB);
		BufferedImage img2 = new BufferedImage((int)(size*supersample), (int)(size*supersample), BufferedImage.TYPE_INT_RGB);
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
		
		calcImage(img2, size*supersample, z);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(img2, 0, 0, size, size, 0, 0, size*supersample, size*supersample, null);
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
//		g.setColor(new Color(0f,0f,0f,0.5f));
//		for (int k = 0; k < size; k += 16) {
//			g.drawLine(k, 0, k, size);
//		}
//		for (int j = 0 ; j < size ; j += 16){
//			g.drawLine(0, j, size, j);
//		}
		g.dispose();
		
		try {
			ImageIO.write(img, "png", new File("C:\\Users\\A66155335\\Desktop\\worldgenImg2.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while (!Thread.interrupted()){
			t1 = System.currentTimeMillis();
			z += 0.005;
			if (z > 0.8)
				z = 0;
			
//			calcImage(img, size, z);
			label.setIcon(new ImageIcon(img));
			Point mousePos = f.getMousePosition();
			if ((test == 1 || test == 0) && mousePos != null){
				mousePos.translate(-size/2, -size/2-10);
				double factor = IMG_ZOOM_FACTOR/CustomChunkGenerator.ZOOM_FACTOR;
				System.out.println(mousePos.x*factor+","+mousePos.y*factor);
			}
			try{
				Thread.sleep(100);
			} catch (Exception e){
				e.printStackTrace();
			}
			secCounter += System.currentTimeMillis()-t1;
			i++;
//			if (secCounter > 5000){
//				System.out.println("FPS: "+i/(secCounter/1000.0));
//				secCounter = 0;
//				i = 0;
//			}
		}
	}
	
	private static void calcImage(BufferedImage img, int size, double z) {
		Graphics2D graphics = img.createGraphics();
		double zoomFactor = IMG_ZOOM_FACTOR;
		double fBefore = Double.NaN;
//		Modifier m = new Modifier(0);
//		m.addCos(0, 0.7, 0, 0.2).addFunc(0.7, 1,
//				new CombinedMultiplyFunction(
//						new PolynomalFunction(2.0/0.9),
//						new PolynomalFunction(1,-2,1)));
		de.FelixPerko.Worldgen.Interpolation.Modifier m = CustomChunkGenerator.isleHeightModifier;
		double prevY = 0;
		for (int x = -size/2 ; x < size/2 ; x++){
			double funX = x*(1.0/size)*2.1;
			double modifierValue = m.modify(funX);
			double yFunction = (-modifierValue/(1.0/size)/2.175);
			
			if (test == TEST_DUNES2){
				float u = (float) ((x)*0.01);
				float gap = 0.1f;
				u += NoiseHelper.simplexNoise2D(x, 0, 0.005, 0.5, 2, 4)*0.5;
				if (u < 1)
					u = (1+gap)-((-u+0.5f+0.5f*gap)%(1+gap));
				else
					u = u%(1+gap)-gap;
				if (u < 0)
					u = 0;
				u = (float) -(m.modify(u));
				u *= NoiseHelper.simplexNoise2D(x+10000, 0, 0.005, 0.5, 2, 1)*0.25+0.75;
				for (int i = 0 ; i < (u*size*0.2)+size*(4.5/5.0) ; i++)
					img.setRGB(x+size/2, i, new Color(1f,1f,1f).getRGB()); //frozen ocean
			}
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
					prevY = yFunction;
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
				
				if (test == TEST_DUNES){
					
					double zoom = 0.05;
					float u = (float) ((x+y)*zoom);
					u += NoiseHelper.simplexNoise2D(x, y, 1*zoom, 0.5, 2, 2)*0.5;
					float gap = 1f;
					if (u < 1)
						u = (1+gap)-((-u+0.5f+0.5f*gap)%(1+gap));
					else
						u = (u%(1+gap))-gap;
					if (u < 0)
						u = 0;
					u = (float) (m.modify(u)*5);
					u *= NoiseHelper.simplexNoise2D(x+10000, y, 0.25*zoom, 0.5, 2, 3)*0.5;
					if (u < 0)
						u = 0;
					img.setRGB(x+size/2, y+size/2, new Color(u,u,u).getRGB()); //frozen ocean
				}
					
				if (test == TEST_BIOME_MAP || test == TEST_BIOME_MAP_OLD){
					int offsetX = (int) (0/IMG_ZOOM_FACTOR);
					int offsetZ = (int) (0/IMG_ZOOM_FACTOR);
					TerrainData data = generator.getData(zoomFactor, x+offsetX, y+offsetZ);
					float r = 0, g = 0, b = 0;
//					if (Math.abs(x) == 0 || Math.abs(y) == 0){
						float v = (float)data.properties[TerrainFeature.BASIC];
						v *= 0.5;
						v += 0.5;
						v = v*v;
						if (v > 1)
							v = 1;
//						System.out.println(v);
//						img.setRGB(x+size/2, y+size/2, new Color(v,v,v).getRGB());
//					} else
//						img.setRGB(x+size/2, y+size/2, data.type.color.getRGB());
						r = data.type.color.getRed()/256f;
						g = data.type.color.getGreen()/256f;
						b = data.type.color.getBlue()/256f;
//						r *= v;
//						g *= v;
//						b *= v;
						img.setRGB(x+size/2, y+size/2, new Color(r,g,b).getRGB());
					int i;
					Pair<Integer, Float>[] values = generator.biomeGrid.getValue(x, y);
					for (i = 0 ; i < values.length ; i++){
						float mult = values[i].getSecond();
						if (mult == 0)
							break;
						int index = values[i].getFirst();
						Color c = generator.terrainTypes.get(index).color;
						r += c.getRed()/256f;
						g += c.getGreen()/256f;
						b += c.getBlue()/256f;
//						float v = (float)data.properties[0]*0.5f+0.5f;
//						r = v;
//						g = v;
//						b = v;
					}
//					System.out.println(r+","+g+","+b);
//					img.setRGB(x+size/2, y+size/2, new Color(r/(i+1), g/(i+1), b/(i+1)).getRGB());
//					if (data.gradient > 0)
//						img.setRGB(x+size/2, y+size/2, new Color((float)data.gradient,(float)data.gradient,(float)data.gradient).getRGB());
					
					if (test == TEST_BIOME_MAP_OLD){
						double f = data.properties[TerrainFeature.BASIC];
						double t = data.properties[TerrainFeature.TEMPERATURE];
						double h = data.properties[TerrainFeature.HUMIDITY];
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

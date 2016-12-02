package de.FelixPerko.Worldgen;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class TerrainDescriptor {
	
	public ArrayList<MaterialDescriptor> blocks = new ArrayList<>();
	public Biome representationBiome = Biome.PLAINS;
	public SurfaceModifier modifier = null;
	
	/**
	 * creates the default (empty) descriptor.
	 */
	public TerrainDescriptor(){
		blocks.add(new MaterialDescriptor(1, Material.GRASS.getId()));
		blocks.add(new MaterialDescriptor(3, Material.DIRT.getId()));
		blocks.add(new MaterialDescriptor(255, Material.STONE.getId()));
	}
	
	public TerrainDescriptor(boolean empty){
		if (!empty){
			blocks.add(new MaterialDescriptor(1, Material.GRASS.getId()));
			blocks.add(new MaterialDescriptor(3, Material.DIRT.getId()));
			blocks.add(new MaterialDescriptor(255, Material.STONE.getId()));
		}
	}
	
	/**
	 * creates a TerrainDescritor with a specified overlay-block-ID.
	 * @param overlayID
	 */
	public TerrainDescriptor(int overlayID){
		blocks.add(new MaterialDescriptor(1, overlayID));
		blocks.add(new MaterialDescriptor(3, Material.DIRT.getId()));
		blocks.add(new MaterialDescriptor(255, Material.STONE.getId()));
	}
	
	public TerrainDescriptor addMat(int depth, int mat){
		blocks.add(new MaterialDescriptor(depth, mat));
		return this;
	}
}

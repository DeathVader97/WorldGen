package de.FelixPerko.Worldgen;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import de.FelixPerko.Worldgen.Utils.Pair;

/**
 * populates blocks based on a density value calculated by terrain properties via modifiers.
 * no clumping occurs since random numbers are used to determine the value.
 */

public class DensityBasedBlock {
	
	int id;
	byte data;
	Modifier[] modifiers;
	int requiredTypeBeneath;
	
	public DensityBasedBlock(int id, byte data, int requiredTypeBeneath, ArrayList<Pair<TerrainFeature, Modifier>> modifiers){
		this.id = id;
		this.data = data;
		this.requiredTypeBeneath  = requiredTypeBeneath;
		this.modifiers = new Modifier[TerrainFeature.values().length];
		for (Pair<TerrainFeature, Modifier> pair : modifiers){
			this.modifiers[pair.getFirst().ordinal()] = pair.getSecond();
		}
	}
	
	/**
	 * Sets the block at the given Location if a random number exceeds the calculated density based on terrain properties
	 * @param w
	 * @param x
	 * @param y
	 * @param z
	 * @param properties
	 * @return if block was set
	 */
	public boolean process(World w, int x, int y, int z, double[] properties){
		Block b = w.getBlockAt(x, y, z);
		Block bBeneath = w.getBlockAt(x, y-1, z);
		if (requiredTypeBeneath > 0 && requiredTypeBeneath != bBeneath.getTypeId())
			return false;
		double density = 1;
		for (int i = 0 ; i < modifiers.length ; i++){
			if (modifiers[i] == null)
				continue;
			density *= modifiers[i].modify(properties[i]);
		}
		if (density <= 0 || density < Math.random())
			return false;
		b.setType(Material.getMaterial(id));
		b.setData(data);
		return true;
	}
}

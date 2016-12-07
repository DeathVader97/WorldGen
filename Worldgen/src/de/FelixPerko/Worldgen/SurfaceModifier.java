package de.FelixPerko.Worldgen;

import org.bukkit.World;

public abstract class SurfaceModifier {
	public abstract void onGeneration(TerrainData data, int x, int z, int xOffset, int zOffset, byte[][] result);
	public abstract void onPopulation(TerrainData data, int x, int z, World w);
}

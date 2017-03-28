package de.FelixPerko.Worldgen;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.FelixPerko.Worldgen.Utils.Pair;

public class CommandHandler {

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("getProperties")){
			int[] features = new int[TerrainFeature.count];
			for (int i = 0 ; i < features.length ; i++)
				features[i] = i;
			Location l = ((Player)sender).getLocation();
			int chunkX = l.getChunk().getX();
			int chunkZ = l.getChunk().getZ();
			int inChunkX = l.getBlockX()-chunkX*16;
			int inChunkZ = l.getBlockZ()-chunkZ*16;
//			if (chunkX < 0)
//				inChunkX += 16;
//			if (chunkZ < 0)
//				inChunkZ += 16;
			double[] properties = Main.chunkGenerator.data.get(new Pair<Integer, Integer>(chunkX,chunkZ))[inChunkX][inChunkZ].properties;
			for (int i = 0 ; i < features.length ; i++){
				sender.sendMessage(features[i]+": "+properties[i]);
			}
		}
		else if (command.getName().equalsIgnoreCase("getBiome")){
			Location l = ((Player)sender).getLocation();
			Pair<Integer, Float>[] values = Main.generator.biomeGrid.getValue(l.getBlockX(), l.getBlockZ());
			for (Pair<Integer, Float> p : values){
				if (p.getSecond() == 0)
					break;
				sender.sendMessage(Main.generator.terrainTypes.get(p.getFirst())+": "+p.getSecond());
			}
		}
		return true;
	}

}

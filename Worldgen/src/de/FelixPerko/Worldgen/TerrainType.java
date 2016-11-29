package de.FelixPerko.Worldgen;

import java.awt.Color;

public class TerrainType {
	
	public Selector selector;
	public Color color;
	
	public TerrainType(Selector selector, Color color) {
		this.selector = selector;
		this.color = color;
	}
}

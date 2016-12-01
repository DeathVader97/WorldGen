package de.FelixPerko.Worldgen;

import java.awt.Color;

public class TerrainType {
	
	public Selector selector;
	public Color color;
	public TerrainDescriptor descriptor;
	
	public TerrainType(Selector selector, Color color) {
		this.selector = selector;
		this.descriptor = new TerrainDescriptor();
		this.color = color;
	}
	
	public TerrainType(Selector selector, TerrainDescriptor descriptor, Color color) {
		this.selector = selector;
		this.descriptor = descriptor;
		this.color = color;
	}
}

package de.FelixPerko.Worldgen;

import java.awt.Color;

public class TerrainType {
	
	static int ID_COUNTER;
	
	public Selector selector;
	public Color color;
	public TerrainDescriptor descriptor;
	public int id;
	
	public TerrainType(Selector selector, Color color) {
		this.selector = selector;
		this.descriptor = new TerrainDescriptor();
		this.color = color;
		this.id = ID_COUNTER;
		ID_COUNTER++;
	}
	
	public TerrainType(Selector selector, TerrainDescriptor descriptor, Color color) {
		this.selector = selector;
		this.descriptor = descriptor;
		this.color = color;
		this.id = ID_COUNTER;
		ID_COUNTER++;
	}
}

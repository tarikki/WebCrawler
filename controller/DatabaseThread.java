package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.Graph;
import model.Vertex;
import util.ConfigUtil;

/**
 * This class will just wait in the background. When it wakes up - once every hour - 
 * it writes the graph to two files. When it is interrupted, it writes all work at hand
 * to another file.
 * Make certain this is a self-runnable (active) object.
 * 
 * @author harald.drillenburg
 *
 */

public class DatabaseThread {
	private ArrayList<String> workAtHand = new ArrayList<String>();
	private ConfigUtil config;
	
	public DatabaseThread(Graph internetModel, ConfigUtil config) {
		// Implement constructor
		this.config = config;
	}
	
	public synchronized void storeWorkAtHand(Vertex source) {
		workAtHand.add(source.getName());
	}
	
	public synchronized void writeAllWorkAtHand() throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(config.getWORK_AT_HAND_FILENAME());
		file.delete();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		// First write all vertices
		writer.println(workAtHand.size());
		for (String aNode: workAtHand) {
			writer.println(aNode);
		}
		writer.close();
	}
}

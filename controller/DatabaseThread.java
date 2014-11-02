package controller;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.Graph;
import model.Vertex;

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
	
	public DatabaseThread(Graph internetModel) {
		// Implement constructor
	}
	
	public synchronized void storeWorkAtHand(Vertex source) {
		workAtHand.add(source.getName());
	}
	
	public synchronized void writeAllWorkAtHand() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(model.Constants.WORK_AT_HAND_FILENAME, "UTF-8");
		// First write all vertices
		writer.println(workAtHand.size());
		for (String aNode: workAtHand) {
			writer.println(aNode);
		}
		writer.close();
	}
}

package controller;

import java.util.Set;

import model.Edge;
import model.Graph;
import model.Vertex;
import util.MemoryUtil;

/**
 * A class which is running in the background and keeps track of key statistics, providing them whenever asked for
 * Make this an active object
 * 
 * @author harald.drillenburg
 *
 */

public class StatisticsThread {
	private Graph internetModel;
	
	public StatisticsThread(Graph internetModel) {
		// Implement this constructor. It probably should just wait untill interrupted.
	}
	
	public void showSummary() {
		System.out.println("Number of found vertices: " + internetModel.getNumberOfVertices());
		System.out.println("Number of found edges: " + internetModel.getNumberOfEdges());
		System.out.println("ratio E/V: " + internetModel.getRatioEV());
		System.out.println("Threads used: " + Thread.activeCount());
		System.out.println("Bandwidth used: " + MemoryUtil.readableFileSize((internetModel.getBandwidthUsed())));
		System.out.println();
	}
	
	public void printMostPopular(Vertex mostPopular) {
		if (mostPopular != null) {
			System.out.println("Most popular site to link to: " + mostPopular + " having " + mostPopular.getNumberOfTargetedBys() +  " linked sites.");
			System.out.println("These sites are: ");
			Set<Edge> targetedBy = mostPopular.getTargetedBy();
			for (Edge anEdge: targetedBy) {
				System.out.println(anEdge.getStartVertex());
			}
		}
	}
}

package model;

import util.ConfigUtil;
import util.StatisticsCallback;

import java.io.*;
import java.util.*;


public class Graph implements StatisticsCallback {
    private long bandwidthUsed = 0; // To keep track of the amount of bandwidth used to build the graph
    private SortedSet<Vertex> vertices; //In this case,a Vertex can only occur once
    private HashSet<Edge> edges; // We do not allow multiple edges between vertices, but sorting is irrelevant (and impossible)
    private int highestAmountOfEdges = 0; // The highest number of connections a Vertex has as the target
    private Vertex topVertex = null; // The vertex having the most links to it
    private List<Vertex> showingList = new ArrayList<Vertex>();
    private SortedSet<Vertex> sinceLastView;
    private ConfigUtil config;

    public Graph(ConfigUtil configUtil) {
        vertices = new TreeSet<Vertex>();
        edges = new HashSet<Edge>();
        sinceLastView = new TreeSet<Vertex>();
        this.config = configUtil;
    }

    public SortedSet<Vertex> getVertices() {
        return vertices;
    }

//	public Vertex getVertexByName(Vertex vertex){
//		return vertices);
//
//	}

//	public Vertex getVertexByName(Vertex vertex){
//		return allVertices.get(allVertices.indexOf(vertex));
//
//	}

    public synchronized SortedSet<Vertex> copyChangedList() {
        SortedSet<Vertex> clone = new TreeSet<Vertex>();
        for (Vertex vertex : sinceLastView) {
            clone.add(new Vertex(vertex));
        }
        sinceLastView.clear();
        return clone;
    }

    public synchronized void addVertex(Vertex newVertex) {
        if (vertices.add(newVertex)) {
            sinceLastView.add(newVertex);
            showingList.add(newVertex);
        }
    }

    public Vertex getVertex(int index) {
        return showingList.get(index);
    }

    public synchronized void addEdge(Edge newEdge) {
        edges.add(newEdge);
        if (newEdge.getEndVertex().getNumberOfTargetedBys() > highestAmountOfEdges) {
            highestAmountOfEdges = newEdge.getEndVertex().getNumberOfTargetedBys();
            topVertex = newEdge.getEndVertex();
        }
    }

    public synchronized void addEdge(Vertex startVertex, Vertex endVertex) {
        Edge newEdge = new Edge(startVertex, endVertex);
        addEdge(newEdge);
        Collections.addAll(sinceLastView, startVertex, endVertex);
    }

    public synchronized void increaseBandwidthUsed(double amount) {
        bandwidthUsed += amount;
    }

    @Override
    public void amountUsed(long amount) {
        increaseBandwidthUsed(amount);
    }

    public synchronized long getBandwidthUsed() {
        return bandwidthUsed;
    }

    public synchronized int getNumberOfVertices() {
        return vertices.size();
    }

    public synchronized int getNumberOfEdges() {
        return edges.size();
    }

    public synchronized float getRatioEV() {
        return (float) ((float) edges.size() / (float) vertices.size());
    }

    public synchronized Vertex getMostPopularVertex() {
        return topVertex;
    }

    public synchronized void dumpGraph() {
        // Heavy, do only call for debugging purposes!
        for (Vertex aVertex : vertices) {
            System.out.print("Vertex " + aVertex + " has " + aVertex.getNumberOfEdges() + " edges to ");
            for (Edge anEdge : edges) {
                if (anEdge.getStartVertex().equals(aVertex) || (anEdge.getEndVertex().equals(aVertex))) {
                    System.out.print(anEdge);
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public synchronized void dumpSummary() {
        // Heavy, do only call for debugging purposes!
        SortedSet<Vertex> showingSet = new TreeSet<Vertex>(new VertexDegreeComparator()); // Not the one I want; but need a comparator more than all nodes
        showingSet.addAll(vertices);
        for (Vertex aVertex : showingSet) {
            System.out.println(aVertex.getNumberOfEdges() + " - vertex " + aVertex + " has " + aVertex.getNumberOfEdges() + " edges.");
        }
    }

    public synchronized void writeGraph() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(config.getVERTICES_FILENAME(), "UTF-8");
        // First write all vertices
        writer.println(vertices.size());
        for (Vertex aVertex : vertices) {
            writer.println(aVertex.getName());
        }
        writer.close();

        // then write all edges
        writer = new PrintWriter(config.getEDGES_FILENAME(), "UTF-8");
        writer.println(edges.size());
        for (Edge anEdge : edges) {
            writer.println(anEdge.getStartVertex());
            writer.println(anEdge.getEndVertex());
        }
        writer.close();
    }

    public void readGraph() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(config.getVERTICES_FILENAME()));
            // First read all vertices
            String numberOfVertices = reader.readLine();
            int maxToRead = Integer.parseInt(numberOfVertices);
            for (int i = 0; i < maxToRead; i++) {
                String name = reader.readLine();
                Vertex aVertex = new Vertex(name);
                addVertex(aVertex);
            }
            reader.close();

            // then read all edges
            reader = new BufferedReader(new FileReader(config.getVERTICES_FILENAME()));
            String numberOfEdges = reader.readLine();
            maxToRead = Integer.parseInt(numberOfEdges);
            for (int i = 0; i < maxToRead; i++) {
                String nameStart = reader.readLine();
                String nameEnd = reader.readLine();
                Edge anEdge = new Edge(new Vertex(nameStart), new Vertex(nameEnd));
                addEdge(anEdge);
            }
        } catch (Exception e) {
            // No sweat. Just not a database yet.
        } finally {
            if (reader!=null)reader.close();
        }
    }
}

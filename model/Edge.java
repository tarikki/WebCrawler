package model;

public class Edge {
	private Vertex startVertex;
	private Vertex endVertex;
	
	public Edge(Vertex startVertex, Vertex endVertex) {
		super();
		this.startVertex = startVertex; // Directed edge!
		this.endVertex = endVertex;
		
		// Don't forget to inform the target vertex, as it will be asked for statistics (hence the two-way link)
		endVertex.addTargetedBy(this);
		endVertex.edgeAdded();
		startVertex.edgeAdded();
	}

	public Vertex getStartVertex() {
		return startVertex;
	}

	public Vertex getEndVertex() {
		return endVertex;
	}

	@Override
	public int hashCode() {
		final int prime = 7919;
		int result = 1;
		result = prime * result
				+ ((endVertex == null) ? 0 : endVertex.hashCode());
		result = prime * result
				+ ((startVertex == null) ? 0 : startVertex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (endVertex == null) {
			if (other.endVertex != null)
				return false;
		} else if (!endVertex.equals(other.endVertex))
			return false;
		if (startVertex == null) {
			if (other.startVertex != null)
				return false;
		} else if (!startVertex.equals(other.startVertex))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ("start: " + startVertex + ", end: " + endVertex);
	}
}

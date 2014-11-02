package model;

import java.util.Comparator;

public class VertexDegreeComparator implements Comparator<Vertex> {

	@Override
	public int compare(Vertex self, Vertex other) {
		return self.getNumberOfEdges() - other.getNumberOfEdges();
	}
}

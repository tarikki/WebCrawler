package model;

import com.sun.istack.internal.NotNull;

import java.util.Comparator;

public class VertexDegreeComparator implements Comparator<Vertex> {

	@Override
	public int compare(Vertex self, @NotNull Vertex other) {
		return self.getNumberOfEdges() - other.getNumberOfEdges();
	}
}

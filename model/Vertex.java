package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.sun.istack.internal.NotNull;
import util.URLUtil;
import util.VertexInvalidException;
import util.VertexUnreachableException;

public class Vertex implements Comparable<Vertex> {
    private String name;    // This is the URL of the website. It should be unique; as an URL can also
    // contain anchors and different protocols, but still be the same,
    // strip these in the constructor
    private Set<Edge> targetedBy = new HashSet<Edge>();
    private int numberOfEdges = 0;

    public Vertex(String name) {

        this.name = name;

    }

    public Vertex(Vertex anotherVertex){
        this.name = anotherVertex.name;
        this.targetedBy = anotherVertex.targetedBy;
        this.numberOfEdges = anotherVertex.numberOfEdges;
    }

    public String getName() {
        return name;
    }

    public Set<Edge> getTargetedBy() {
        return targetedBy;
    }

    public void addTargetedBy(Edge anEdge) {
        targetedBy.add(anEdge);
    }

    public void removeTargetedBy(Edge anEdge) {
        targetedBy.remove(anEdge);
    }

    public int getNumberOfTargetedBys() {
        return targetedBy.size();
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void edgeAdded() {
        numberOfEdges++;
    }

    public void edgeRemoved() {
        numberOfEdges--;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
//        System.out.println(result);
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
        Vertex other = (Vertex) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override

    public int compareTo(@NotNull Vertex other) {
            return this.toString().compareTo(other.toString());
        }
    }


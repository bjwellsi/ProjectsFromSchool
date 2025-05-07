package cs425A4;

import java.util.ArrayList;
import java.util.Collections;

public class Node {
	private Edge from;
	private ArrayList<Edge> edges;
	private Node SuffixLink;// should go from parent to child

	public Edge firstEdge() {
		//this may be the only non-linear part of the algorithm, if I can find a way to 
		//minimize it I can linearize the whole thing
		if (edges.isEmpty())
			return null;
		Collections.sort(edges);
		return edges.get(0);
	}

	@Override
	public String toString() {
		if(from == null)
			return "Node [from = null, edges.size() = " + edges.size() + "]";
		return "Node [from=" + from + ", edges.size()=" + edges.size() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SuffixLink == null) ? 0 : SuffixLink.hashCode());
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
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
		Node other = (Node) obj;
		if (SuffixLink == null) {
			if (other.SuffixLink != null)
				return false;
		} else if (!SuffixLink.equals(other.SuffixLink))
			return false;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		return true;
	}

	public Node(Edge from) {
		this(from, new ArrayList<Edge>(), null);
	}

	public Node(Edge from, ArrayList<Edge> edges, Node link) {
		this.setFrom(from);
		this.edges = edges;
		setSuffixLink(link);
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public void removeEdge(Edge e) {
		edges.remove(e);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public Edge getFrom() {
		return from;
	}

	public void setFrom(Edge from) {
		this.from = from;
	}

	public Node getSuffixLink() {
		return SuffixLink;
	}

	public void setSuffixLink(Node suffixLink) {
		SuffixLink = suffixLink;
	}
}

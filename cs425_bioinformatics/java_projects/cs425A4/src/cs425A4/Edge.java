package cs425A4;

public class Edge implements Comparable<Edge> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + firstChar;
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
		if (firstChar != other.firstChar)
			return false;
		return true;
	}

	private int fromIndex;
	private int toIndex;// -1 indicates current end
	private Node from;
	private Node to;

	@Override
	public String toString() {
		String ret = "Edge [fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", firstChar=" + firstChar
				+ "\t + toNode : ";
		if (to == null) {
			return ret + "null]";
		}
		ret += "not null, children : " + to.getEdges().size() + "]";
		return ret;
	}

	private char firstChar;

	public Edge(Node from, Node to, int fromIndex, int toIndex, char firstChar) {
		this.from = from;
		this.to = to;
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
		this.setFirstChar(firstChar);
	}

	public int length() {
		if (toIndex == -1)
			return Integer.MAX_VALUE - 1;
		return toIndex - fromIndex;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}

	public Node getFrom() {
		return from;
	}

	public void setFrom(Node from) {
		this.from = from;
	}

	public Node getTo() {
		return to;
	}

	public void setTo(Node to) {
		this.to = to;
	}

	public char getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(char firstChar) {
		this.firstChar = firstChar;
	}

	public int compareTo(Edge other) {
		return this.firstChar - other.firstChar;
	}
}
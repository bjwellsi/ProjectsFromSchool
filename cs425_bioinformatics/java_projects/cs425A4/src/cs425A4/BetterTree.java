package cs425A4;

public class BetterTree {

	private int remainder;
	private Node root;
	private String input;
	private int activeLength;
	private Node activeNode;
	private char activeEdge;
	private Node suffixLink;

	public BetterTree(String input) {
		remainder = 1;
		root = new Node(null);
		this.input = input;
		activeLength = 0;
		activeEdge = '\0';
		activeNode = root;
		suffixLink = null;
		buildTree();
	}

	public Node getRoot() {
		return root;
	}

	public char get(int i) {
		return input.charAt(i);
	}

	public boolean root() {
		return root(activeNode);
	}

	public boolean root(Node check) {
		return check == root;
	}

	public Edge findCurrentEdge() {
		return searchEdgeList(activeEdge);
	}

	public Edge searchEdgeList(char match) {
		return searchEdgeList(activeNode, match);
	}

	public Edge searchEdgeList(Node from, char match) {
		// search to see if the first character of each edge matches the
		// character given. if it does, return that edge
		for (Edge a : from.getEdges()) {
			if (a.getFirstChar() == match)
				return a;
		}
		return null;
	}

	public boolean hasEdge(Node on, char match) {
		return searchEdgeList(on, match) != null;
	}

	public boolean matches(char match) {
		return matches(activeNode, activeEdge, match, activeLength);
	}

	public boolean matches(Node on, char edge, char match, int length) {
		// check to see if the (length) down the edge matches the character
		// so if the edge is ABCD, char is C, length is 2, returns true
		// if char is x, returns false
		Edge onEdge = searchEdgeList(on, edge);
		return onEdge != null && get(onEdge.getFromIndex() + length) == match;
	}

	private void split(int i) {
		Edge current = findCurrentEdge();
		// where the split is happening
		int midway = current.getFromIndex() + activeLength - 1;
		// the new node
		Node B = new Node(current);
		// the new edge
		Edge three = new Edge(B, null, i, -1, get(i));
		// the second half of the original edge
		Edge two = new Edge(B, current.getTo(), midway + 1, current.getToIndex(), get(midway + 1));
		if(current.getTo() != null)
			current.getTo().setFrom(two);
		B.addEdge(three);
		B.addEdge(two);
		// need to update current, as it's now the first half of the original node
		current.setTo(B);
		current.setToIndex(midway);
		// if suffix link already exists, set this as its link
		if (suffixLink != null) {
			suffixLink.setSuffixLink(B);
		}
		// reset the suffixLink variable
		suffixLink = B;
		System.out.println("");

	}

	private void trim(int i) {
		// making the (possibly erroneous) assumption that any time you need to
		// trim, you can do it in the exact same way as any other time
		//may need to remove:

		while (1 > 0) {
			Edge current = findCurrentEdge();
			if (current == null)
				return;
			int length = current.length() + 1;
			if (length == activeLength) {
				activeLength = 0;
				activeNode = current.getTo();
				activeEdge = '\0';
			} else if (length > activeLength) {
				return;
			} else {
				// activeLength > length of your current edge
				activeLength -= length;
				activeNode = current.getTo();
				activeEdge = get(i - activeLength);

			}
		}
	}

	private void buildTree() {
		int i = 0;
		try {
		while (i < input.length()) {
			if (root()) {
				// A
				// At root
				if (activeEdge == '\0') {
					if (hasEdge(activeNode, get(i))) {
						// Y
						// Need to set your edge
						activeEdge = get(i);
						activeLength++;
						// restart the loop
						suffixLink = null;
					} else {
						// F
						// Down to last character to add
						activeLength = 0;
						remainder--;
						activeNode.addEdge(new Edge(activeNode, null, i, -1, get(i)));
						// reset activeEdge, leave activeNode as root:
						activeEdge = '\0';
						// restart the loop
						suffixLink = null;
					}
				} else {
					// C
					// active edge is not null
					if (hasEdge(activeNode, activeEdge)) {
						// E
						// this edge actually exists in root
						if (matches(get(i))) {
							// H
							// the next character is present on the branch you're already on
							activeLength++;
							// MAY NEED TO TRIM HERE
							//trim(i);
							if(activeLength == findCurrentEdge().length() + 1) {
								activeLength = 0;
								activeNode = findCurrentEdge().getTo();
								activeEdge = '\0';
							}
							//
							//
							//
							// restart the loop
							suffixLink = null;
						} else {
							// I
							// The next character isn't present on your edge
							// split the edge:
							split(i);
							// todo
							// in Splitting, B should get saved as suffixLink
							// if another split already happened this turn (suffixLink != null), set B as
							// its suffix link
							// B being the newly created node
							// go back to root:
							activeNode = root;
							// set aLength--
							activeLength--;
							// set activeEdge to i - remainder
							activeEdge = get(i - remainder + 2);// because remainder is at least one, and
							// hasn't been decremented for this turn yet, you have to add 2
							// MAY NEED TO TRIM HERE

							trim(i);
							//
							//
							//
							//

							// continueTheCurrentStep:
							remainder -= 2;
							i--;
						}
					} else {
						// D
						// ActiveEdge is not null, but doesn't actually exist in ActiveNode's edgelist
						if (remainder == 1) {
							// X
							// There is only one branch left to add for this turn
							// add the edge
							activeNode.addEdge(new Edge(activeNode, null, i, -1, activeEdge));
							// decrement remainder
							remainder--;
							// set activeLength to 0
							activeLength = 0;
							// set activeEdge to null
							activeEdge = '\0';
							// restart the loop;
							suffixLink = null;
						} else {
							// G
							// There is a whole suffix left to add, remainder > 1
							// add the edge
							activeNode.addEdge(new Edge(activeNode, null, i - remainder + 1, -1, activeEdge));
							// set activeEdge to the new activeEdge, leave activeNode at root
							activeEdge = get(i - remainder + 2);
							activeLength--;
							// MAY HAVE TO TRIM HERE
							trim(i);
							//
							//
							//
							// continueTheCurrentStep:
							remainder -= 2;
							i--;
						}
					}
				}
			} else {
				// B
				// Not at root
				// J
				// I am assuming that it is impossible to be on a non root node, with an
				// activeEdge that isn't null
				// but have the edge not be present in that node
				if (activeEdge != '\0') {
					// K
					// On an Edge
					if (matches(get(i))) {
						// L
						// Next character is present on that edge
						// increment activeLength
						activeLength++;
						// MAY NEED TO TRIM HERE
						//trim(i);
						if(activeLength == findCurrentEdge().length() + 1) {
							activeLength = 0;
							activeNode = findCurrentEdge().getTo();
							activeEdge = '\0';
						}
						//
						//
						//
						// restart the loop
						suffixLink = null;
					} else {
						// M
						// next character isn't present
						// Split the edge
						// in Splitting, B should get saved as suffixLink
						// if another split already happened this turn (suffixLink != null), set B as
						// its suffix link
						// B being the newly created node
						split(i);
						// todo
						// activeEdge, length stay the same
						// activeNode becomes either root or the suffixLink of activeNode
						if (activeNode.getSuffixLink() != null) {
							activeNode = activeNode.getSuffixLink();
						} else {
							activeNode = root;
						}
						// MAY NEED TO TRIM HERE
						trim(i);
						//
						//
						//
						// continueTheCurrentStep
						remainder -= 2;
						i--;
					}
				}
				// N
				// Not on an edge
				else if (hasEdge(activeNode, get(i))) {
					// P
					// the next character exists already in this node's edge list
					// set activeEdge to that character
					activeEdge = get(i);
					// set activeLength to 1
					activeLength = 1;
					// restart the loop
					suffixLink = null;
				} else {
					// O
					// The next character doesn't exist in this node's edge list
					// add the edge to the edge list
					//CHECK THIS!!!!!
					//activeNode.addEdge(new Edge(activeNode, null, i - remainder + 1, -1, get(i)));
					//
					//
					//I'm operating on the assumption that the only reason an internal node would add a new edge is
					//that the edge only has one char
					activeNode.addEdge(new Edge(activeNode, null, i, -1, get(i)));

					// activeEdge, length are unchanged
					// travel to the suffixlink or root
					if (activeNode.getSuffixLink() != null) {
						activeNode = activeNode.getSuffixLink();
					} else {
						activeNode = root;
					}
					// Set suffixLink to null
					suffixLink = null;
					// MAY NEED TO TRIM HERE
					trim(i);
					//
					//
					//
					// continueTheCurrentStep
					remainder -= 2;
					i--;
				}
			}
			i++;
			remainder++;
		}
	} catch(Exception e) {
		System.out.println("i : " + i);
		e.printStackTrace();
	}
	}
}

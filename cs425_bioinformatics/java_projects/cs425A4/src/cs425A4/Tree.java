package cs425A4;

public class Tree {

	private int remainder;
	private Node root;
	private String input;
	private int activeLength;
	private Node activeNode;
	private char activeEdge;

	public Tree(String input) {
		remainder = 0;
		root = new Node(null);
		this.input = input;
		activeLength = 0;
		activeEdge = '\0';
		activeNode = root;
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

	public boolean matches(char match) {
		return matches(activeNode, activeEdge, match, activeLength);
	}

	public boolean matches(Node on, char edge, char match, int length) {
		// check to see if the (length) down the edge matches the character
		// so if the edge is ABCD, char is C, length is 2, returns true
		// if char is x, returns false
		Edge onEdge = searchEdgeList(on, edge);
		if(activeLength < 0)
			System.out.println("There seems to be an issue : " + onEdge.getFromIndex() + " : " + length + " : ");
		return onEdge != null && get(onEdge.getFromIndex() + length) == match;
	}

	public void trimLength(int i) {
		// todo test
		if (findCurrentEdge() == null) {
			// you can skip trimming the length, you will be creating this edge on the next
			// loop
			return;
		}
		
		while(findCurrentEdge().length() + 1 < activeLength) {
			activeLength -= (findCurrentEdge().length() + 1);
			if(activeLength < 0) {
				System.out.println("In trimLength : " + activeLength) ;
			}
			activeNode = findCurrentEdge().getTo();
			activeEdge = get(i - activeLength);
			if (findCurrentEdge() == null)
				break;
		}
	}

	public Node split(int i) {
		Edge currentEdge = findCurrentEdge();
		// assume you're splitting the active node from the active edge at the active
		// length
		Node B = new Node(currentEdge);
		Edge two = new Edge(B, null, i, -1, get(i));
		int midwayPoint = currentEdge.getFromIndex() + activeLength - 1;
		Edge three = new Edge(B, currentEdge.getTo(), midwayPoint + 1, currentEdge.getToIndex(), get(midwayPoint + 1));
		if(three.getTo() != null) {
			//need to update its from:
			three.getTo().setFrom(three);
		}
		B.addEdge(two);
		B.addEdge(three);
		currentEdge.setToIndex(midwayPoint);
		if(midwayPoint < currentEdge.getFromIndex()) {
			System.out.println("to : "  + midwayPoint + " < from : " + currentEdge.getFromIndex() + ". ActiveLength : " + activeLength);
		}
		currentEdge.setTo(B);
		return B;
	}

	public void buildTree() {

		Node suffixLink = null;
		loop: for (int i = 0; i < input.length(); i++) {
			// first, check if you're on an edge
			char next = get(i);
			if (activeEdge == '\0') {
				// A:
				// you're not on an edge
				// check if you're at root
				if (root()) {
					// C:
					// if so, everything is simple
					// Either we're creating an edge or walking along an edge
					Edge test = searchEdgeList(next);
					if (test != null) {
						// E:
						// this means that there is an edge with that first character
						// so you need to set the active edge to this edge:
						activeEdge = next;
						// increment remainder because you haven't actually added a new branch:
						remainder++;
						// increment length because now you're after the first character on this edge:
						activeLength++;
						// leave active node the same, restart the loop:
						suffixLink = null;
						continue loop;
					}
					if (test == null) {
						// F:
						// this means there is no edge with this first character
						// so you need to create it by:
						// creating the edge and adding it to the node:
						activeNode.addEdge(new Edge(activeNode, null, i, -1, next));
						// restart the loop:
						suffixLink = null;
						continue loop;
					}
				} else {
					// D:
					// you're not on an edge, but not at root:

					// check to see if any edges match
					Edge test = searchEdgeList(next);
					if (test != null) {
						// M:
						// edge already present, set active edge
						activeEdge = next;
						// increment active length
						activeLength++;
						// increment remainder:
						remainder++;
						// restart loop:
						continue loop;
					} else {
						// N:
						// edge not already present
						// need to create a new edge
						activeNode.addEdge(new Edge(activeNode, null, i, -1, next));
						// check to see if any others were made this turn
						// if so, create a suffix link for them
						if (suffixLink != null) {
							// you created another edge already this turn, you need to set this as its
							// "child"
							suffixLink.setSuffixLink(activeNode);
							// reset suffix link in case you insert again this step
							suffixLink = activeNode;
						}

						Node link = activeNode.getSuffixLink();
						if (link != null) {
							activeNode = link;
							// don't change active length, restart the loop
						} else {
							// no suffix link present, have to go back to root
							activeNode = root;
							// don't have to reset active edge. If at any point your active edge doesn't
							// exist,
							// it will be created
						}
						// after resetting active node, make sure you aren't past the length of your
						// edge:

						trimLength(i);
						// continue on your merry way
						if (remainder > 0) {
							i--;
							remainder--;
						}
						continue loop;

					}
				}
			} else {
				// B:
				if (findCurrentEdge() == null) {
					// O:
					// activeEdge doesn't exist, need to create an edge

					// this step could be insertion from an internal node or from root
					// basically, you get here if at any point activenode changes but that node
					// doesn't have the given edge
					activeNode.addEdge(new Edge(activeNode, null, i - remainder, -1, activeEdge));
					if (remainder > 0)
						remainder--;
					// need to restart the loop
					if (remainder == 0) {
						activeNode = root;
						activeEdge = '\0';
						activeLength = 0;
						suffixLink = null;
					}
					// otherwise you just inserted from an internal node
					else {
						if (activeNode.getSuffixLink() != null) {
							activeNode = activeNode.getSuffixLink();
						} else {
							suffixLink = activeNode;
							activeNode = root;
						}
						i--;
						// you're not done with this step, so decrement i
					}
					continue loop;
				}
				// you're on an edge:
				// assume that the edge is at least as long as active length for now
				if (matches(next)) {
					// G:
					// you're on the edge, and the next character is on that edge
					// don't need to split the edge, just increment active length and remainder:
					activeLength++;
					remainder++;
					// but, we need to make sure that we're still on this edge:
					if (findCurrentEdge().length() < activeLength) {
						// no longer on this edge, update active length, activeEdge, and activeNode
						// because the length of a leaf edge has to be infinite, to node cannot be null:
						activeNode = findCurrentEdge().getTo();
						activeEdge = '\0';
						activeLength = 0;
						// next time you restart the loop, it'll progress to the "not on edge, not at
						// root, node"
					}
					// after that, just restart the loop:
					suffixLink = null;
					continue loop;
				} else {
					// H:
					// the next character isn't on that edge
					// because you're on an edge, it is safe to assume remainder is not 0, which
					// means there is only
					// one option; split the edge.

					// here is where we need to check if we're at root.
					if (root()) {
						// I:
						// if we're on an edge out of the root, we split the edge:
						Node tempLink = split(i);
						if (suffixLink == null)
							suffixLink = tempLink;
						else {
							// but also set this as the suffix link for the parent:
							suffixLink.setSuffixLink(tempLink);
							// reset suffix link in case you insert again this step
							suffixLink = tempLink;
						}
						// leave the active node as root, active edge becomes the first character of the
						// new remainder length
						activeEdge = get(i - (remainder - 1));// remainder = 1 means the suffix to be inserted is
						if(activeLength > 0)
							activeLength--;
						if(activeLength < 0 ) {
							System.out.println("in I: " + activeLength);
						}
						// bx, for example, if the character you're on is x

						trimLength(i);
						// decrement remainder:
						if (remainder > 0) {
							remainder--;
							i--;
						}
						continue loop;
					} else {
						// J:
						// not on root, but the next character isn't on the edge you're on:
						// time to first split the edge:
						Node tempLink = split(i);
						if (suffixLink == null)
							suffixLink = tempLink;
						else {
							// but also set this as the suffix link for the parent:
							suffixLink.setSuffixLink(tempLink);
							// reset suffix link in case you insert again this step
							suffixLink = tempLink;
						}
						// if it isn't the first edge created during this step, create a suffix link
						// if this is the first time inserting an edge or node from a non-root node,
						// save this node as the node to create links from during this step
						// figure out how to tell if you're on the same step you were on before

						// now move on to the next node to be changed, based on remainder.
						// leave active length and active edge the same, change active node:
						// if you have a suffix link child, follow that link
						Node link = activeNode.getSuffixLink();
						if (link != null) {
							// you have a linked child, set it to the active node
							activeNode = link;
						} else {
							// you don't have a link, set the node to root:
							activeNode = root;
						}
						// regardless of what happened here, you now need to make sure that the length
						// doesn't exceed your current edge length:

						trimLength(i);
						if (remainder > 0) {
							remainder--;
							i--;
						}
						// after all that, you've split the edge, you have added the suffix,
						// now it's time to check smaller suffixes, so you follow the suffix link or
						// set it to root, you'll restart with the smaller suffixes
						// once that is done, you restart the loop
						// decrement remainder:

						continue loop;
					}
				}
			}
		}
	}
}
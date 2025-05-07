package cs425A4;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SuffixArray {

	private String input;
	private ArrayList<Integer> suffixArray;
	private BetterTree suffixTree;

	public SuffixArray(String input) {
		this.input = input;
		suffixTree = new BetterTree(input);
		buildArray();
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public ArrayList<Integer> getSuffixArray() {
		return suffixArray;
	}

	public void setSuffixArray(ArrayList<Integer> suffixArray) {
		this.suffixArray = suffixArray;
	}

	public BetterTree getSuffixTree() {
		return suffixTree;
	}

	public void setSuffixBetterTree(BetterTree suffixTree) {
		this.suffixTree = suffixTree;
	}

	public void buildArray() {
		suffixArray = new ArrayList<Integer>();

		Node currentNode = suffixTree.getRoot();

		// from the current node, find the first child
		Edge currentEdge = currentNode.firstEdge();

		int length = 0;
		
		loop: while (1 > 0) {
			if (currentEdge == null) {
				//A
				// you've emptied a node
				if (suffixTree.root(currentNode)) {
					// you're done
					return;
				}
				// other wise you've finished this edge, set the node to its parent and remove
				// it:
				currentEdge = currentNode.getFrom();
				// removal includes subtracting its length
				length -= (currentEdge.length() + 1);
				currentNode = currentEdge.getFrom();
				currentNode.removeEdge(currentEdge);
				// then find the next edge.
				// if the node is empty, and this is null, that'll be found on the next loop
				currentEdge = currentNode.firstEdge();
				/*if(length < 0) {
					System.out.println("in SA.A : " + length);
				}*/
				continue loop;
			} else if (currentEdge.getTo() == null) {
				//B
				// if you're on a leaf edge, add it to the array by calling
				if(length > currentEdge.getFromIndex()) {
					System.out.println("in SA.B : " + length + ". fromindex : " + currentEdge.getFromIndex());
				}
				suffixArray.add(currentEdge.getFromIndex() - length);
				// then, go up to the parent node, remove the edge
				currentNode.removeEdge(currentEdge);
				// get the next edge
				currentEdge = currentNode.firstEdge();
				// if this happens to be null, next time the loop executes it will remove the
				// node
				continue loop;
			} else {
				//C
				// this is the case where the edge isn't null and it has a child node
				// in other words it's an edge that isn't a leaf
				// add its length, set current node to its child and current edge to that node's
				// child
				length += (1 + currentEdge.length());
				if(currentEdge.length() > 10000000) {
					//System.out.println(currentEdge.length());
					//System.out.println(currentEdge.toString());
					while(currentEdge.getFrom().getFrom() != null) {
						//System.out.println(currentEdge.getFrom());
						System.out.println(currentEdge + "This shouldn't happen");
						currentEdge = currentEdge.getFrom().getFrom();
					}
					System.out.println(currentEdge);
					return;
				}
				currentNode = currentEdge.getTo();
				currentEdge = currentNode.firstEdge();
				// if that edge is null, this'll be found on the next loop
				/*if(length < 0) {
					System.out.println("in SA.C : " + length);
				}*/
				continue loop;
			}
		}
	}

	public static String readFileAsString(String fileName) {
		String text = "";
		try {
			text = new String(Files.readAllBytes(Paths.get(fileName)));
			text = text.replaceAll("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public String toString() {
		String ret = "";
		for (int i : suffixArray) {
			ret += i + " : " + input.substring(i) + "\n";
		}
		return ret;
	}

	public static void main(String[] args) throws IOException {
		FileWriter f = new FileWriter(args[1]);
		String bigboi = readFileAsString(args[0]);
		BWT genome = new BWT(bigboi);
		f.write(genome.toString());
		System.out.println(genome);
		
		
		String input = "abcabxabcd$";
		BWT bwt = new BWT(input);
		
		System.out.println(bwt);
		
		BWT miss = new BWT("mississippi$");
		System.out.println(miss);
		
		/*
		f.write(bwt.toString());
		BWT miss = new BWT("mississippi$");
		f.write(miss.toString());
		f.close();
		*/
		

		
	}
}

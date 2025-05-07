package cs425A4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BWT {

	private ArrayList<Character> transform;
	private String input;
	private SuffixArray SA;
	private ArrayList<Character> characters;
	private int[][] occ;
	private int[] cnt;
	private String inverseBWT;

	public BWT(SuffixArray SA, String input) {
		// assumes the SA has already been built
		this.SA = SA;
		this.input = input;
		characters = new ArrayList<Character>();
		build();
	}

	public BWT(String input) {
		this(new SuffixArray(input), input);
	}

	private void build() {
		transform = new ArrayList<Character>();
		ArrayList<Integer> arr = SA.getSuffixArray();
		char c;
		for (int i : arr) {
			if (i == 0) {
				c = input.charAt(arr.size() - 1);
			} else {
				if(i < 0) {
					System.out.println(i);
				}
				c = input.charAt(i - 1);
			}
			transform.add(c);
			if (!characters.contains(c))
				characters.add(c);
		}
		FMindex();
		inverseBWT();
		
	}

	private void buildCNT(ArrayList<Character> sorted) {
		Collections.sort(sorted);
		cnt = new int[sorted.size()];
		cnt[characters.indexOf(sorted.get(0))] = 0;
		for (int i = 1; i < cnt.length; i++) {
			// assumes the alphabet has more than one character
			cnt[characters.indexOf(sorted.get(i))] = cnt[characters.indexOf(sorted.get(i - 1))]
					+ occ[occ.length - 1][characters.indexOf(sorted.get(i - 1))];
		}
	}

	public void FMindex() {
		ArrayList<Character> sorted = new ArrayList<Character>();
		occ = new int[transform.size()][characters.size()];
		// assumes that the transform has at least one row
		for (int i = 0; i < characters.size(); i++) {
			sorted.add(characters.get(i));
			occ[0][i] = 0;
		}
		occ[0][characters.indexOf(transform.get(0))] = 1;
		for (int i = 1; i < transform.size(); i++) {
			for (int j = 0; j < characters.size(); j++) {
				occ[i][j] = occ[i - 1][j];
				if (transform.get(i) == characters.get(j))
					occ[i][j]++;
			}
		}
		buildCNT(sorted);
	}

	public void inverseBWT() {
		String ret = "";
		int i = 0;
		char a;
		while (ret.length() < transform.size()) {
			a = transform.get(i);
			ret += a;
			i = occ[i][characters.indexOf(a)] - 1 + cnt[characters.indexOf(a)];
		}
		inverseBWT = ret;
	}

	public String toString() {
		String ret = "Original String : " + input;
		ret += "\nSuffix Array : \n" + SA;
		ret += "\nBWT : ";
		for (char a : transform) {
			ret += a;
		}
		ret += "\n\nOcc table : \n";
		ret += " ";
		for(int i = 0; i < characters.size(); i++) {
			ret += " " + characters.get(i);
		}
		ret += "\n";
		for(int i = 0; i < occ.length; i++) {
			ret += transform.get(i);
			for(int j = 0; j < occ[i].length; j++) {
				ret += " " + occ[i][j];
			}
			ret += "\n";
		}
		
		ret += "\ncnt table : \n";
		for(int i = 0; i < cnt.length; i++) {
			ret += characters.get(i) + " : " + cnt[i];
		}
		
		ret += "\n\nInverse BWT calculated from FM index : \n" + inverseBWT + "\n\n";
		return ret;
	}

}

package cs425A4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Build {
	
	private ArrayList<Integer> SAI;
	private ArrayList<Character> SAC;

	public static void main(String[] args) throws IOException {
		String input = readFileAsString(args[0]);
		SuffixArray(input, args[1]);

		// String input = "mississippi$";

		/*
		 * ArrayList<String> SA = suffixArray(input); ArrayList<Integer> SAOrder =
		 * suffixArrayOrder(SA, input); String BWT = BWTFromSuffix(SAOrder, SA, '$');
		 * System.out.println(BWT);
		 */
	}

	public static ArrayList<Integer> SuffixArray(String input, String file) throws IOException {
		ArrayList<Integer> SAI = new ArrayList<Integer>();
		ArrayList<Character> SAC = new ArrayList<Character>();

		RandomAccessFile rw = new RandomAccessFile(file, "rw");

		for (int i = 0; i < input.length(); i++) {
			rw.seek(0);
			String line;
			String temp = input.substring(i);

			for (int count = 0; count < input.length(); count++) {
				line = rw.readLine();
				if (line == null || line.startsWith("\n")) {
					rw.writeChars(temp + "\n");
					SAI.add(count, i);
					SAC.add(count, temp.charAt(0));
					break;
				}

				else if (line.compareTo(temp) > 0) {
					rw.seek(rw.getFilePointer() - temp.length() - 1);
					rw.writeChars(temp + "\n");
					SAI.add(count, i);
					SAC.add(count, temp.charAt(0));
					break;
				}
			}
		}
		rw.close();

		// loop through input, removing the first character each time
		// after you remove the first character, sort the substring into your file. When
		// you place a string
		// in the file, place it's index and first char into the same place in your
		// lists

		return SAI;
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

	public static ArrayList<Integer> suffixArrayOrder(ArrayList<String> SA, String input) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (String i : SA) {
			ret.add(input.length() - i.length());
		}
		return ret;
	}

	public static ArrayList<String> suffixArray(String input) {
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < input.length(); i++) {
			ret.add(input.substring(i));
		}
		Collections.sort(ret);
		return ret;
	}

	public static String BWTFromSuffix(ArrayList<Integer> SAorder, ArrayList<String> suffixes, char terminalChar) {
		String ret = "";
		for (int i : SAorder) {
			char a;
			if (i < 1) {
				a = terminalChar;
			} else
				a = suffixes.get(SAorder.indexOf(i - 1)).charAt(0);
			ret += a;
		}

		return ret;
	}

	/*
	 * public static ArrayList<String> reconstruct(String BWT){ ArrayList<String>
	 * ret = new ArrayList<String>(); for(int i = 0; i < BWT.length(); i++) {
	 * ret.add(BWT.substring(i, i+1)); } ArrayList<String> temp =
	 * Collections.sort(ret); }
	 */

	public static String BWT(String input) {
		return BWT(BWTArray(input));
	}

	public static String BWT(ArrayList<String> BWTarray) {
		String ret = "";
		for (String next : BWTarray) {
			ret += (next.substring(next.length() - 1));
		}
		return ret;
	}

	public static ArrayList<String> BWTArray(String input) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(input);
		String temp;
		for (int i = 0; i < input.length() - 1; i++) {
			temp = ret.get(i);
			temp = temp.charAt(temp.length() - 1) + temp.substring(0, temp.length() - 1);
			ret.add(temp);
		}
		Collections.sort(ret);
		return ret;
	}
}

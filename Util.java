package SGD;

import java.util.Vector;

public class Util {
	public static final int BUFFERSIZE = 100000;
	public static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+");
		Vector<String> tokens = new Vector<String>();
		tokens.add(words[0]);
		for (int i = 1; i < words.length; i++) {
			words[i] = words[i].replaceAll("\\W", "");
		if (words[i].length() > 0) {
		tokens.add(words[i]);
		}
		}
		return tokens;
		}
}

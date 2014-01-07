package SGD;
import java.io.*;
public class RandomlySort {
	public static void main (String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
			String cul = null;
			int T = 20;
			int number = 0;
			while((cul = in.readLine()) != null) {
				for (int i = 0; i < T; i++) {
					out.write(i + "\t" +  number + "\t" + cul + "\n");
				}
				number++;
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

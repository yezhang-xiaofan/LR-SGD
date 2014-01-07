package SGD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

public class testUsingBetas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String testFile = args[0];
			String [] labels = {"nl","el","ru","sl","pl","ca","fr","tr","hu","de","hr","es","ga","pa"};
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
			HashMap<String, double []> B = new LinkedHashMap<String, double [] >();
			HashMap<String, Double> prob = new LinkedHashMap<String, Double>();
			int dictSize = -1;
			int totalTestCount = 0;
			int rightCount = 0;
			for (String label : labels){
				String strLine = in.readLine();;
				String [] sbetas = strLine.split("\\s+");
				if (dictSize == -1){
					dictSize = sbetas.length;
				}
				double [] dbetas = new double [dictSize];
				for (int i = 0; i<dictSize; i++){
					dbetas[i] = Double.parseDouble(sbetas[i]);
				}
				B.put(label, dbetas);
			}
			BufferedReader fileReader = new BufferedReader(
					new FileReader(testFile));
			String strLine = null;
			while (( strLine = fileReader.readLine())!=null){
				Vector<String> tokens = util.Util.tokenizeDoc(strLine);
				Iterator<String> iter = tokens.iterator();

				String [] trueLabels = iter.next().split(",");
				// construct features
				int [] feature = new int[dictSize];
				while (iter.hasNext()){
					String word = iter.next();
					int id = word.hashCode() % dictSize;
					if (id<0) id+= dictSize;
					feature[id]++;
				}
				int squareSum = 0;
				double squareRoot = -1;
				for (int i = 0; i< dictSize; i++){
					squareSum+= feature[i]*feature[i];
				}
				squareRoot = Math.sqrt((double) squareSum);
				ArrayList<String> estimateLabels = new ArrayList<String> ();
				for (String label : labels){
					double [] betas = B.get(label);
					double p = 0;
					for (int i =0; i<dictSize; i++){
						p+=betas[i]*feature[i]/squareRoot;
					}
	//				System.out.println(p);

					p = sigmoid(p);
					totalTestCount++;
					if (p>0.5){
						estimateLabels.add(label);
						if (contains(trueLabels,label)){
							rightCount ++;	
						}
					}
					else{
						if (!contains(trueLabels,label)){
							//System.out.println(label);
							rightCount++;
						}
					}
					
				}
				StringBuffer strbuf = new StringBuffer();
				strbuf.append("TRUE LABELS IS ");
				for (String tmp : trueLabels){
					strbuf.append(tmp+" ");
				}
				strbuf.append("ESTIMATE LABELS "+estimateLabels);
				System.out.println(strbuf.toString());
				
			}
			System.out.print("Right "+rightCount+" Total "+totalTestCount+" correctness: "+(double) rightCount /totalTestCount);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static boolean contains(String [] array, String str){
		for (String aStr : array){
			if (str.equals(aStr)){
				return true;
			}
		}
		return false;
	}
	   private static double sigmoid(double score) {
   	 	double overflow=20;
   	 	if (score > overflow) 
   	 		score =overflow;
   	 	else if (score < -overflow) 
   	 		score = -overflow;
   	 	double exp = Math.exp(score);
   	 	return exp / (1 + exp);
}

}

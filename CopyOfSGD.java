package SGD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;


public class CopyOfSGD {
	public static void main(String [] args){
		try {
			int dictSize = 4009;
			int Ne = 106;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
			String [] labels = {"nl","el","ru","sl","pl","ca","fr","tr","hu","de","hr","es","ga","pa"};
			ArrayList<Integer> A = new ArrayList<Integer>();
			HashMap<String, ArrayList<Double>> B = new LinkedHashMap<String, ArrayList<Double> >();
			HashMap<String, Double> prob = new LinkedHashMap<String, Double>();
			for (String label : labels){
				B.put(label, new ArrayList<Double>());
				prob.put(label, (double) 0);
			}
			String strLine;
			Double u = 0.05;
			Double lamda = (double) -1;
			int k = 0;
			while ((strLine = in.readLine())!=null){
				k++;
				int t = (k)/Ne+1;
				lamda = 0.5/((double) (t*t));
				Vector<String> tokens = util.Util.tokenizeDoc(strLine);
				Iterator<String> iter = tokens.iterator();
				String[] hasLabels = iter.next().split(",");
				int [] wordID = new int [tokens.size()-1];
				int index = 0;
				initializeProb(prob);
				while(iter.hasNext()){
				
					String word = iter.next();
					
					int id = word.hashCode() % dictSize;
					if (id<0) id+= dictSize;
					wordID[index]= id;
					for (String label: labels){
						ArrayList<Double> bList = B.get(label);
						while (bList.size()<=index){
							bList.add((double) 0 );
						}
						while (A.size() <= index){
							A.add(0);
						}
						
						prob.put(label, prob.get(label)+bList.get(index)*id);
						
					}
					index++;
				}
				for (String label: labels){
					prob.put(label, sigmoid(prob.get(label)));
				}
				for (String label: labels){
					int y = 0;
					if (contains(hasLabels,label)){
						y = 1;
					}
					ArrayList<Double> vectors = B.get(label);
					for (int i = 0; i< tokens.size()-1; i++){
						if (wordID[i] >0){
							Double beta = vectors.get(i);
							if (beta!=0){
								beta*= Math.pow((1-2*lamda*u), k-A.get(i));
								
							}
							beta+=lamda*(y-prob.get(label))*wordID[i];
							vectors.set(i, beta);
							A.set(i,  k);
						}
					}
				}
				
			}
			
			
			for (String label : labels){
				ArrayList<Double> bList = B.get(label);
				for (int i = 0; i<bList.size(); i++){
					Double beta = bList.get(i);
					if (beta !=0){
						beta *= Math.pow((1-2*lamda*u), k-A.get(i));
						bList.set(i,beta);

					}
				}
			}
			
			
			ArrayList<Double> bList = B.get("nl");
			for (Double d : bList){
				System.out.print(d+" ");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void initializeProb(HashMap<String ,Double>prob){
		for (String label : prob.keySet()){
			prob.put(label, (double) 0);
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

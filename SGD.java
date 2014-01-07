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

public class SGD {
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
	public static void main(String [] args){
		try {
			//int flag = 1;
			int Ne = Integer.parseInt(args[0]);
			int dictSize = Integer.parseInt(args[1]);
			Double u = Double.parseDouble(args[2]);			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
			String [] labels = {"nl","el","ru","sl","pl","ca","fr","tr","hu","de","hr","es","ga","pa"};
			int [] A = new int [dictSize];
			HashMap<String, double []> B = new LinkedHashMap<String, double [] >();
			HashMap<String, Double> prob = new LinkedHashMap<String, Double>();
			for (String label : labels){
				double [] array = new double [dictSize];
				B.put(label, array);
				prob.put(label, (double) 0);
			}
			String strLine;
			
			Double lamda = (double) -1;
			int k = 0;
			Double eachIterP = (double )0;
			int t =1;
			while ((strLine = in.readLine())!=null){
								
				if (((k)/Ne+1)>t){
					System.out.println("iteration "+t+" is "+eachIterP);
					eachIterP = (double) 0;
				}
					
				 t = (k)/Ne+1;
				 k++;
				lamda = 0.5/((double) (t*t));
				int [] feature = new int [dictSize];
				double squareSum = 0;
				double squareRoot = (double) -1;
				Vector<String> tokens = util.Util.tokenizeDoc(strLine);
				Iterator<String> iter = tokens.iterator();
				String[] hasLabels = iter.next().split(",");
				while (iter.hasNext()){
					String word = iter.next();
					int id = word.hashCode() % dictSize;
					if (id<0) id+= dictSize;
					feature[id]++;
				}
				for (int i = 0; i< dictSize; i++){
					squareSum+= feature[i]*feature[i];
				}
				squareRoot = Math.sqrt(squareSum);

			//	initializeProb(prob);
				for (String label : labels){
					int y = 0;
					if (contains(hasLabels,label)){
						y = 1;
					}
				
					Double p = (double) 0;
					for (int i = 0; i< dictSize; i++){
						if (feature[i]>0){
						
							if (B.get(label)[i]!=0){
								B.get(label)[i] *= Math.pow((1-2*lamda*u), k-A[i]);
								p+=B.get(label)[i]*feature[i]/squareRoot/(1-2*lamda*u);
							}
							A[i] = k;
							
						}
					}
				/*	
					if (k<=3 && label.equals("es")){
						System.out.println("ES ip = "+p);
					}
					*/
					p = sigmoid(p);
					if (y==1){
						eachIterP += Math.log(p);
					}
					else{
						eachIterP+=Math.log(1-p);
					}
					prob.put(label, p);
					for (int i = 0; i<dictSize; i++){
						if (feature[i]>0){
							B.get(label)[i] += lamda*(y-prob.get(label))*feature[i]/squareRoot;
						}
					}
					
				}
				/*
				if (k<=3){
					System.out.println("P=" +prob.get("es"));
				}
				if (flag == 1 &&k ==1){
					//System.out.println(B.get("es")[3527]);
					System.out.println("AAA");
					System.out.println(Arrays.toString(B.get("es")));
					flag = 0;
				}
				*/
				
				
			}
			for (String label: labels){
				for (int i = 0; i< dictSize; i++){
					if (A[i]!=k&&B.get(label)[i]!=0){
						B.get(label)[i] *= Math.pow((1-2*lamda*u), k-A[i]);
						A[i] = k;
					}
				}
			}
			
			//show likelihood for the overall data 
			System.out.println("t = "+(t+1)+" "+ eachIterP);
			for (String label: labels){
				double [] beta = B.get(label);
				for (double d : beta){
					out.write(d+" ");
				}
				out.write('\n');
			}
			out.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

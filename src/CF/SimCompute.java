/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CF;

import static CF.Recommender.db;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ITITI
 */
public class SimCompute {
    
    static String filename = "coverMaxtrix.txt";
    static String filename1 = "simCompute.txt";
    static FileWriter fw;
    static BufferedWriter bw;
    
    public static double[] vectorCreate(String line){
        double vector[] = new double[12554];
        int j=0;          
        String[] words=line.split("\\ ");
            for (String s:words){                
                vector[j++] = Double.parseDouble(s);
            }
        return vector;
    }
    
    
    public static void SimComputemain(int n) throws FileNotFoundException, IOException{
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");
        
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        int k=0;           

        List<Integer> kmemory = new ArrayList<Integer>();
        List<Double> kdata = new ArrayList<Double>();
        
        List<double[]> data = new ArrayList<double[]>();
        double[] vectora = new double[12554];
        double[] vectorb = new double[12554];
                
        while((line = in.readLine()) != null){            
            data.add(vectorCreate(line));   
        }
        vectora = data.get(n);
        
        for (double[] s: data){            
            vectorb = s;
            double sim = MathForDummies.round(MathForDummies.cosineSimilarity(vectora, vectorb),3);
            //System.out.println(k+": "+sim);           
            if (sim>0.0 && sim!=1.0){
                kdata.add(sim);
                kmemory.add(k);
            }
            k++;
        }
        in.close();
        
        File file = new File(filename1); 
            if (!file.exists()) {
                file.createNewFile();
            }else{
                boolean success = file.delete();
                if (success) {
                    System.out.println("The file has been successfully deleted"); 
                }
            }
            
        fw = new FileWriter(file.getAbsoluteFile(), true);
        bw = new BufferedWriter(fw);
        for (int i=0;i<kmemory.size();i++){                                
            bw.write(kmemory.get(i)+ " "+ kdata.get(i)+"\r\n");    
            System.out.println(kmemory.get(i)+": "+kdata.get(i));
        }                                           
        bw.close();
    }
}

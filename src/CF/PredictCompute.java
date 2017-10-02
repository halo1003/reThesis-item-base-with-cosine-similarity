/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CF;

import static CF.Recommender.fw;
import static CF.SimCompute.filename;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author ITITI
 */
public class PredictCompute {
    static String filename1 = "simCompute.txt";
    static String filename2 = "Maxtrix.txt";
    static String filename3 = "PredictArray.txt";
    static int given =1;
    static int givenPredict =5;
    static FileWriter fw;
    static BufferedWriter bw;
    
    public static void fileCreate(String filename) throws IOException{
        File file = new File(filename); 
            if (!file.exists()) {
                file.createNewFile();
            }else{
                boolean success = file.delete();
                if (success) {
                    System.out.println("The file has been successfully deleted"); 
                }
            }
    }
    
    public static void PredictComputemain(int n) throws FileNotFoundException, IOException{
        BufferedReader in1 = new BufferedReader(new FileReader(filename1));
        String line1;      
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");
        
        DBCollection coll = db.getCollection("musics");        
        
        fileCreate(filename3);
        
        List<Integer> simmemory = new ArrayList<Integer>();
        List<Double> simdata = new ArrayList<Double>();
        while((line1 = in1.readLine()) != null){ 
            String[] words=line1.split("\\ ");
            simmemory.add(Integer.parseInt(words[0]));
            simdata.add(Double.parseDouble(words[1]));
        }        
        in1.close();
        
        BufferedReader in2 = new BufferedReader(new FileReader(filename2));
        String line2;
        int k=0;                
        List<int[]> itemsMaxtrix = new ArrayList<int[]>();
        
        while((line2 = in2.readLine()) != null){             
            int item[] = new int[13369];
            int j =0;
            String[] words=line2.split("\\ ");
            for (String s: words){
                item[j++] = Integer.parseInt(s);                
            }       
            itemsMaxtrix.add(item);
        }                
        in2.close();        
        
        List<item> recommend = new ArrayList<item>();
        
        for (int i: itemsMaxtrix.get(n)){
            System.out.println("Item["+k+"]");
            if (i == 0){
                int index =0;
                double pred =0.0;
                double sum = 0.0;
                for (int sim: simmemory){
                    int temp = itemsMaxtrix.get(sim)[k];
                    //System.out.println("Get "+temp + "of simmemory "+ sim);
                    if (temp != 0){
                        pred = pred + (double)temp*simdata.get(index);
                        sum = sum + simdata.get(index);
                    }                    
                    index++;
                }
                System.out.println("Predict of item ["+k+"] = "+pred/sum+" pred/sum: "+pred+"/"+sum);
                if (pred/sum > givenPredict){
                    item ite = new item(k,(int) MathForDummies.round((pred/sum),1));                     
                    recommend.add(ite);
                }
            }
            k++;
        }
        Collections.sort(recommend, new itemComparator());       
        
            
            fw = new FileWriter(filename3, true);
            bw = new BufferedWriter(fw);
            for (item i: recommend){   
                System.out.println(i.getPred()+"-"+i.getId()+1);
                BasicDBObject query = new BasicDBObject("_id",i.getId()+1);
                DBObject doc = coll.findOne(query);
                
                JSONObject jsonObject = new JSONObject((Map) doc);                        
                String mid = (String) jsonObject.get("mid");                
                bw.write("PredictCompute: "+i.getPred()+ " - Index in music database: "+ i.getId()+1 + " mid: "+mid+"\r\n");            
            }                                   
            bw.close();        
    }
}

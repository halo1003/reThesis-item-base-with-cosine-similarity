/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CF;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author ITITI
 */
public class Recommender {
    static Mongo mongo;
    static DB db;
    static DB db1;
    static List<String> listMusic = new ArrayList<String>();    
    static String filename1= "Nomalize maxtrix.txt";
    static String filename2= "Maxtrix.txt";
    static FileWriter fw;
    static BufferedWriter bw;    
   
    public static Boolean search(String s,List<String> mlist){     
        for(String str: mlist) {
            if(str.trim().contains(s))
               return true;
        }
    return false;
    }
    
    public static void MongoConnect() throws UnknownHostException{
        Mongo mongo = new Mongo("localhost", 27017); 
        db = mongo.getDB("train_triplets");
    }
    
    public static void GetListMusic(){
        DBCollection coll = db.getCollection("musics");
        BasicDBObject query=new BasicDBObject();
        query.put("mid",new BasicDBObject("$regex", ".").append("$options", "i"));
        
        DBCursor cur = coll.find(query);                
        while (cur.hasNext()){   
            DBObject json = cur.next();
            JSONObject jsonObject = new JSONObject((Map) json);
                        
            String mid = (String)jsonObject.get("mid");
            listMusic.add(mid);
        }
    }
    
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
    
    public static void Recommendermain () throws UnknownHostException, IOException{
        MongoConnect();
        DBCollection collection = db.getCollection("udata");
        GetListMusic();      
        
        //DBCollection coll = db.getCollection("maxtrix_udata");
        //Create list music         
        
        fileCreate(filename1);
        fileCreate(filename2);
        
        BasicDBObject query=new BasicDBObject();
        query.put("_id",new BasicDBObject("$regex", ".").append("$options", "i"));        
        DBCursor cur = collection.find(query);     
        int t=0;
        while (cur.hasNext()){   
            DBObject json = cur.next();
            JSONObject jsonObject = new JSONObject((Map) json);
            String _id = (String)jsonObject.get("_id");
            
            System.out.println(_id+" " +t++);
            Object listen = jsonObject.get("listen");                
            String str[] = listen.toString().split("[\\\"\\\"]");
            
            BasicDBList userList = new BasicDBList();
           
            List<String> checkinuserList = new ArrayList<String>();
            List<Double> dou = new ArrayList<Double>();            
            //List<Double> douW = new ArrayList<Double>(); 
            double douW[] = new double[13369];
            int intW[] = new int[13369];
            
            double m = 0.0;
            
            for (int i=1;i<str.length;i++){                                                                
                checkinuserList.add(str[i]);  
                dou.add(Double.parseDouble(str[i+2]));
                i=i+3;
            }
            
            for (double i:dou){
                m = m+i;
            }
            m = (m/(dou.size()*1.));                        
            
            int j=0;
            int k=0;
            for (int i = 0;i<listMusic.size();i++){
                if (search(listMusic.get(i),checkinuserList) == false){                                        
                    //DBObject db_obj = new BasicDBObject();
                    //    db_obj.put(listMusic.get(i),0.000);                
                    //userList.add(db_obj);
                    //douW.add(0.0);
                    intW[k] = 0;
                    douW[k++] = 0.0;                    
                }else{
                    double current = dou.get(j++);
                    double temp = MathForDummies.round(current-m, 3);
                    System.out.print(temp+" {"+k+"}");
                    //DBObject db_obj = new BasicDBObject();
                    //    db_obj.put(listMusic.get(i),temp);                
                    //userList.add(db_obj);
                    //douW.add(temp);
                    intW[k] = (int)(current) ;
                    douW[k++] = temp;
                }
            }
            System.out.println();
            
            // TODO: Write file                                    		
            double sum =0;
            
            
            fw = new FileWriter(filename1, true);
            bw = new BufferedWriter(fw);
            for (double d: douW){
                sum = sum+d*d;                
                bw.write(d+ " ");            
            }
            System.out.println(sum);
            bw.write("\r\n");                            
            bw.close();
            
            
            
            fw = new FileWriter(filename2, true);
            bw = new BufferedWriter(fw);
            for (int d: intW){                                
                bw.write(d+ " ");            
            }            
            bw.write("\r\n");                            
            bw.close();
        }
    }
}

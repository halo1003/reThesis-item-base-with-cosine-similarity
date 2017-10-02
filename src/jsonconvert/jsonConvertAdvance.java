/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonconvert;

import CF.item;
import CF.itemComparator;
import com.mongodb.BasicDBObject;
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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ITITI
 */
public class jsonConvertAdvance {
    static String filename = "musics.txt";
    
    public static String stringProcess(String str) {
        str = str.trim();
        str = str.replaceAll("\\s+", " ");
        return str;
    }
    
    public static Boolean search(String s,List<String> mlist){
            
        for(String str: mlist) {
            if(str.trim().contains(s))
               return true;
        }
        return false;
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
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        BufferedReader in = new BufferedReader(new FileReader("E:\\Prethesis\\train_triplets.txt"));
        String line;
        int i=0;
        
        fileCreate(filename);
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");
        DBCollection table = db.getCollection("udata");
                
        List<String> mid = new ArrayList<String>();
        
        //line = in.readLine();
        //line = stringProcess(line);            
        //String[] word=line.split("\\s");        
        //uid.add(word[0]);
        //mid.add(word[1]);
        
        while((line = in.readLine()) != null){
            line = stringProcess(line);            
            String[] words=line.split("\\s");
            if (search(words[1],mid)==false){
                mid.add(words[1]);
            }
                      
            System.out.println(i++);
        }          
        FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String s: mid){
            bw.write(s+"\r\n");
        }
        in.close();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonconvert;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author ITITI
 */
public class JsonConvert {
    
    public static String stringProcess(String str) {
        str = str.trim();
        str = str.replaceAll("\\s+", " ");
        return str;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        BufferedReader in = new BufferedReader(new FileReader("E:\\Prethesis\\train_triplets.txt"));
        String line;
        int i=0;
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");
        DBCollection table = db.getCollection("userdata");

        while((line = in.readLine()) != null){
            line = stringProcess(line);            
            String[] words=line.split("\\s");
            
            BasicDBObject document = new BasicDBObject();
                document.put("userid", words[0]);
                document.put("songid", words[1]);
                document.put("playcount", words[2]);
            table.insert(document);
            System.out.println("Parse: id"+ i++ +" done");
        }
        in.close();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonconvert;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author ITITI
 */
public class music_add {
    
    public static Boolean search(String s,List<String> mlist){     
        for(String str: mlist) {
            if(str.trim().contains(s))
               return true;
        }
    return false;
    }
    
    public static void main(String[]args) throws UnknownHostException{
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");   
        
        List<String> mid = new ArrayList<String>();
        BasicDBList music = new BasicDBList();
        
        DBCollection coll = db.getCollection("udata");
        DBCollection collection = db.getCollection("musics");
        
        BasicDBObject query=new BasicDBObject();
        query.put("_id",new BasicDBObject("$regex", ".").append("$options", "i"));        
        DBCursor cur = coll.find(query);     
        int t=0;
        while (cur.hasNext()){   
            DBObject json = cur.next();
            
            JSONObject jsonObject = new JSONObject((Map) json);
            Object listen = jsonObject.get("listen");
            String str[] = listen.toString().split("[\\\"\\\"]");
            for (int i =1 ; i< str.length;i++){
                System.out.println(str[i]);
                if (search(str[i],mid)== false) mid.add(str[i]);
                i = i+3;
            }
        }
        int cout = 0;
        for (String s: mid){
            BasicDBObject a = new BasicDBObject();
            a.put("_id",cout++);
            a.put("mid",s); 
            
            collection.insert(a);
        }
    }
}

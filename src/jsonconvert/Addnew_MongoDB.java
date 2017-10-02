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
import com.mongodb.util.JSON;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

/**
 *
 * @author ITITI
 */
public class Addnew_MongoDB {
    
    static List<String> listuid = new ArrayList<String>();
    
    public static Boolean seach(String s,List<String> mlist){
        
    //System.out.println("Seaching item contain in mlist...");
    
    for(String str: mlist) {
        if(str.trim().contains(s))
           return true;
    }
    return false;
    }
    
    
    
    public static void main(String[] args) throws UnknownHostException, IOException{    
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("train_triplets");
        
        DBCollection collection = db.getCollection("userdata");
        DBCollection coll = db.getCollection("udata");
                
        BasicDBObject query = new BasicDBObject();
        query.put("userid",new BasicDBObject("$regex", ".").append("$options", "i"));
        
        BasicDBObject fields=new BasicDBObject()
                .append("userid",1)         
                .append("songid",1)
                .append("playcount",1);

        DBCursor cursor = collection.find(query,fields);
        while(cursor.hasNext()) {   
            
            //System.out.println("< Cursor item of LV1 list next >");    
            
            DBObject json = cursor.next();            
            JSONObject jsonObject = new JSONObject((Map) json);
                        
            String uid = (String) jsonObject.get("userid");
            //System.out.println(id);
            
            if (seach(uid,listuid)==false) {
                listuid.add(uid);
                System.out.println("--> "+uid +" added to mlist");
                
                BasicDBObject query1 = new BasicDBObject();
                query1.put("userid",uid);       
                
                //List<User> userList = new ArrayList<User>();
                BasicDBList userList = new BasicDBList();
                
                System.out.println("Finding current uid to group...("+collection.count()+") elements");
                DBCursor c = collection.find(query1,fields);
                
                int i=0;
                while(c.hasNext()) {    
                                        
                    DBObject js = c.next();
                    //System.out.println("< Cursor item of LV2 list next >");
                    JSONObject jsObject = new JSONObject((Map) js);
                                       
                    Object id = (Object) jsObject.get("_id");
                    String sid = (String) jsObject.get("songid");
                    String pcount = (String) jsObject.get("playcount");                                        
                    
                    DBObject db_obj = new BasicDBObject();
                    db_obj.put(sid,pcount);
                    
                    BasicDBObject queryR = new BasicDBObject("_id",new ObjectId(id.toString()));                    
                    //System.out.println("Finding remove uid which is added to mlist...");
                    
                    collection.remove(queryR);
                    System.out.println(queryR +" removed");
                    
                    userList.add(db_obj);
                    
                    //ObjectMapper mapper = new ObjectMapper();
                    //String jsonInString = mapper.writeValueAsString(userList);
                    
                    //System.out.println(id+" removed");
                }
                
                BasicDBObject document = new BasicDBObject();
                    document.put("_id", uid);                       
                    document.put("listen", userList);
                coll.insert(document);
                
                System.out.println("Insert "+uid+" done");
            }
            
        }                
    }
}

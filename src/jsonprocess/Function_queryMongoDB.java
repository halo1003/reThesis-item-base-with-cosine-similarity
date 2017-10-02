/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonprocess;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ITITI
 */

public class Function_queryMongoDB {

    String artist;
    String timestamp;

    public Function_queryMongoDB() {
    }

    public Function_queryMongoDB(String artist,String timestamp) {
        this.artist = artist;
        this.timestamp = timestamp;
    }
    
    public void queryArtist(String artist) throws UnknownHostException{
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");           
        DBCollection collection = db.getCollection("user");
        
        BasicDBObject query=new BasicDBObject("artist",artist);
        BasicDBObject fields=new BasicDBObject()
                .append("artist",1)
                .append("title",1)
                .append("timestamp",1);                

        DBCursor cursor = collection.find(query,fields);
        while(cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
    
    public void queryTimestamp(String timestamp) throws UnknownHostException{
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");           
        DBCollection collection = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
        
        Integer year = Integer.parseInt(timestamp.substring(0, 4));
        query.put("timestamp",new BasicDBObject("$regex", year +".").append("$options", "i"));
        
        //BasicDBObject query=new BasicDBObject("timestamp",timestamp.substring(0, 4)+".");
        BasicDBObject fields=new BasicDBObject()
                .append("artist",1)
                .append("title",1)
                .append("timestamp",1);                

        DBCursor cursor = collection.find(query,fields);
        while(cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
    
    public static void queryIntags(List<DBObject> tags,String s,String time) throws UnknownHostException{
        
        for (DBObject tag:tags){   
            Set<String> keys = tag.keySet();
            Iterator iterator = keys.iterator();

            String key = iterator.next().toString();
            Object value = tag.get(key);
            
            if (value.toString().equals(s))
                System.out.println("{ Tag: "+key+" }\n");                 
                FilterKey(key,time);
        }
    }
    
    public static void FilterKey(String i,String timestamp) throws UnknownHostException{
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");   
        
        DBCollection collection = db.getCollection("user");        
        Integer year = Integer.parseInt(timestamp.substring(0, 4));
        
        BasicDBObject query=new BasicDBObject();
        query.put("tags."+i,"\"100\"");
        query.put("timestamp",new BasicDBObject("$regex", year +".").append("$options", "i"));
        
        BasicDBObject fields=new BasicDBObject()
                .append("artist",1)
                .append("title",1)
                .append("timestamp",1);                
                //.append("tags.$",1);

        DBCursor cursor = collection.find(query,fields);
        while(cursor.hasNext()) {
            System.out.println(cursor.next());
        }              
    } 
    
    public void querySimilar(List<DBObject> similars) throws UnknownHostException{
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");           
        DBCollection collection = db.getCollection("user");
        
        for (DBObject similar:similars){   
            Set<String> keys = similar.keySet();
            Iterator iterator = keys.iterator();

            String key = iterator.next().toString();
            Object value = similar.get(key);
            //System.out.println(key);
            //System.out.println(value);
            
            BasicDBObject query=new BasicDBObject();
            query.put("_id",key);            
            
            BasicDBObject fields=new BasicDBObject()
                    .append("artist",1)
                    .append("title",1)
                    .append("timestamp",1);                

            DBCursor cursor = collection.find(query,fields);
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }        
    }
}

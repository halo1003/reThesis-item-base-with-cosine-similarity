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
import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ITITI
 */
public class MongoDB_query {
    
    public static void main(String[] args) throws UnknownHostException{
        
        String artist = null,title,timestamp = null;
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");   
        
        DBCollection collection = db.getCollection("user"); 
      
        BasicDBObject query=new BasicDBObject("_id","TRAAIMO128F92EB778");
        BasicDBObject fields=new BasicDBObject()
                .append("artist",1)
                .append("title",1)
                .append("timestamp",1)
                .append("tags",1)
                .append("similars",1);

        DBCursor cursor = collection.find(query,fields);

            //System.out.println(cursor.next());
            DBObject json = cursor.next();
            JSONObject jsonObject = new JSONObject((Map) json);

            artist = (String) jsonObject.get("artist");
            title = (String) jsonObject.get("title");        
            timestamp = (String) jsonObject.get("timestamp"); 
            
            List<DBObject> similars = (List<DBObject>) jsonObject.get("similars");
            List<DBObject> tags = (List<DBObject>) jsonObject.get("tags");
                        
            System.out.println("You are listening: "+title);            
            System.out.println("Artist: "+artist);
            System.out.println("Time: "+timestamp.substring(0,10)+"\n");
            
        Function_queryMongoDB function = new Function_queryMongoDB();        
        System.out.println("\nFilter follow tags: -----------------------------------------------");        
        function.queryIntags(tags,"\"100\"",timestamp);
        
        System.out.println("\nFilter follow artist:"+ artist+" ----------------------------------");        
        function.queryArtist(artist);
        
        System.out.println("\nFilter follow timestamp:"+ timestamp+" ----------------------------");
        //function.queryTimestamp(timestamp);
        
        System.out.println("\nFilter follow similar: --------------------------------------------");
        function.querySimilar(similars);
    }
}

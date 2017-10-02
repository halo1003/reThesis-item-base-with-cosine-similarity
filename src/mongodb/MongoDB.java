/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ITITI
 */
public class MongoDB {

    /**
     * @param args the command line arguments
     */
    static int i =0;
    static String artist;
    static String track_id;
    static String title;
    static String timestamp;  
    static List<DBObject> listSimilar;   
    static List<DBObject> listTag;   

    static void addList(JSONArray array,List<DBObject> list){
        
        for (Object item : array) {
            String[] key1 = item.toString().split("\"");
            String[] key2 = item.toString().split(",");

            String key = key1[1];
            String value = key2[1].substring(0, key2[1].length() - 1);

            list.add(new BasicDBObject(key, value));
        }
    }
    
    private static void addJSONtoMongoDB(File file) throws FileNotFoundException, IOException, ParseException{
        
        Mongo mongo = new Mongo("localhost", 27017); 
        DB db = mongo.getDB("lastfm_test");   
        
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));        
        JSONObject jsonObject = (JSONObject) obj;

        artist = (String) jsonObject.get("artist");
        track_id = (String) jsonObject.get("track_id");
        title = (String) jsonObject.get("title");
        timestamp = (String) jsonObject.get("timestamp");
        
        JSONArray similars = (JSONArray) jsonObject.get("similars");
        listSimilar = new ArrayList<DBObject>();
        
        JSONArray tags = (JSONArray) jsonObject.get("tags");
        listTag = new ArrayList<DBObject>();
        
        addList(similars,listSimilar);
        addList(tags,listTag);
        
        DBCollection table = db.getCollection("user"); 
        
        BasicDBObject doc = new BasicDBObject()
            .append("artist", artist)
            .append("_id", track_id)
            .append("title",title)
            .append("timestamp", timestamp)
            .append("similars", listSimilar)
            .append("tags", listTag);
        table.insert(doc);     
        System.out.println("id:"+ i++);        
    }
    
    static void find_files(String root) throws IOException, FileNotFoundException, ParseException{
        
        File folder = new File(root);
        File[] files = folder.listFiles(); 
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file);
                addJSONtoMongoDB(file);
            } else if (file.isDirectory()) {
                find_files(file.toString());
            }
        }
    }
    
    public static void main(String[] args){       
        
        try {
            find_files("E:\\Prethesis\\zip\\lastfm_test\\");
        } catch (IOException ex) {
            Logger.getLogger(MongoDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}

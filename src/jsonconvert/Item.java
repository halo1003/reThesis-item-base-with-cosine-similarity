/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonconvert;

/**
 *
 * @author ITITI
 */
public class Item {
    String uid;
    String sid;
    int playcount;

    public Item(String uid, String sid, int playcount) {
        this.uid = uid;
        this.sid = sid;
        this.playcount = playcount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }
    
}

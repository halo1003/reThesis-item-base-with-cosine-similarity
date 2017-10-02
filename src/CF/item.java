/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CF;

/**
 *
 * @author ITITI
 */

import java.io.Serializable;
 
public class item implements Serializable {	
    int id;
    int pred;

    public item(int id, int pred) {
        this.id = id;
        this.pred = pred;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPred() {
        return pred;
    }

    public void setPred(int pred) {
        this.pred = pred;
    }
    
    
}


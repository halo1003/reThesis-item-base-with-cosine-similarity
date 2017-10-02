/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import CF.PredictCompute;
import CF.Recommender;
import CF.SimCompute;
import java.io.IOException;

/**
 *
 * @author ITITI
 */
public class mainTest {
    public static void main(String[]args) throws IOException{
        Recommender r = new Recommender();
        SimCompute s = new SimCompute();
        PredictCompute p = new PredictCompute();
        
        int given = 1;
            r.Recommendermain();
            s.SimComputemain(given);
            p.PredictComputemain(given);
        
        System.out.println("ALL FILE CREATED");
    }
}

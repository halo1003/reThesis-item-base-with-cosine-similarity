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
public class MathForDummies{
    
    public static double round(double number, int digit){
        if (digit > 0){
            int temp = 1, i;

            for (i = 0; i < digit; i++)
                temp = temp*10;

            number = number*temp;
            number = Math.round(number);
            number = number/temp;
        return number;
        }
        else
        return 0.0;
    }
    
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }   
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}

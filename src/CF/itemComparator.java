/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CF;

import java.util.Comparator;

/**
 *
 * @author ITITI
 */
public class itemComparator implements Comparator<item>{
 
 public int compare(item o1, item o2) {
     // Hai đối tượng null coi như bằng nhau.
     if (o1 == null && o2 == null) {
         return 0;
     }
     // Nếu o1 null, coi như o2 lớn hơn
     if (o1 == null) {
         return -1;
     }
     // Nếu o2 null, coi như o1 lớn hơn.
     if (o2 == null) {
         return 1;
     }
     // Nguyên tắc:
     // Sắp xếp tăng dần theo tuổi.
     int value =  - o1.getPred()+ o2.getPred();
     return value;     
 }
 
}

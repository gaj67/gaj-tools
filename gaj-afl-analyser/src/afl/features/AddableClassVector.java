package afl.features;

import java.util.ArrayList;
import java.util.List;

public class AddableClassVector implements ClassVector {

   private final List<Integer> values = new ArrayList<Integer>();

   public void add(int value) {
      values.add(value);
   }

   public int[] dense() {
      int[] vector = new int[values.size()];
      int i = 0;
      for (Integer value : values)
         vector[i++] = value;
      return vector;
   }

   public int length() {
      return values.size();
   }

   public int get(int index) {
      return values.get(index);
   }

}

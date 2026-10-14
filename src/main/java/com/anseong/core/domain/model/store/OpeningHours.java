package com.anseong.core.domain.model.store;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.anseong.core.domain.model.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OpeningHours extends AbstractBaseEntity {

     private static final long serialVersionUID = -756656560060443724L;

     @Column(name = "open0")
     private int open0;

     @Column(name = "open1")
     private int open1;

     @Column(name = "open2")
     private int open2;

     @Column(name = "open3")
     private int open3;

     @Column(name = "open4")
     private int open4;

     @Column(name = "open5")
     private int open5;

     @Column(name = "open6")
     private int open6;

     @Column(name = "close0")
     private int close0;

     @Column(name = "close1")
     private int close1;

     @Column(name = "close2")
     private int close2;

     @Column(name = "close3")
     private int close3;

     @Column(name = "close4")
     private int close4;

     @Column(name = "close5")
     private int close5;

     @Column(name = "close6")
     private int close6;

     public OpeningHours() {}

     public OpeningHours(Map<String, String> data) {
          OpeningHours newInstance = new OpeningHours();

          for (String date : data.keySet()) {
               String[] time = data.get(date).split("~");

               try {
                    Field openField = newInstance.getClass().getDeclaredField("open" + date);
                    Field closeField = newInstance.getClass().getDeclaredField("close" + date);

                    openField.setAccessible(true);
                    closeField.setAccessible(true);

                    openField.setInt(newInstance, Integer.parseInt(time[0]));
                    closeField.setInt(newInstance, Integer.parseInt(time[1]));

                    openField.setAccessible(false);
                    closeField.setAccessible(false);
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }
     }

     public static void update(Map<String, String> data, OpeningHours currentInstance) {
          for (String date : data.keySet()) {
               String[] time = data.get(date).split("~");

               try {
                    Field openField = currentInstance.getClass().getDeclaredField("open" + date);
                    Field closeField = currentInstance.getClass().getDeclaredField("close" + date);

                    openField.setAccessible(true);
                    closeField.setAccessible(true);

                    openField.setInt(currentInstance, Integer.parseInt(time[0]));
                    closeField.setInt(currentInstance, Integer.parseInt(time[1]));

                    openField.setAccessible(false);
                    closeField.setAccessible(false);
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }
     }

     public Map<String, String> getAllOpeningHours() {
          Map<String, String> results = new HashMap<>();

          for (int i = 0; i < 7; i++) {
               try {
                    Field openField = this.getClass().getDeclaredField("open" + i);
                    Field closeField = this.getClass().getDeclaredField("close" + i);

                    openField.setAccessible(true);
                    closeField.setAccessible(true);

                    results.put(String.valueOf(i), openField.getInt(this) + "~" + closeField.getInt(this));

                    openField.setAccessible(false);
                    closeField.setAccessible(false);
               } catch (Exception e) {
                    e.printStackTrace();
               }
          }

          return results;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + open0;
          result = prime * result + open1;
          result = prime * result + open2;
          result = prime * result + open3;
          result = prime * result + open4;
          result = prime * result + open5;
          result = prime * result + open6;
          result = prime * result + close0;
          result = prime * result + close1;
          result = prime * result + close2;
          result = prime * result + close3;
          result = prime * result + close4;
          result = prime * result + close5;
          result = prime * result + close6;
          return result;
     }

     @Override
     public boolean equals(Object obj) {
          if (this == obj)
               return true;
          if (obj == null)
               return false;
          if (getClass() != obj.getClass())
               return false;
          OpeningHours other = (OpeningHours) obj;
          if (open0 != other.open0)
               return false;
          if (open1 != other.open1)
               return false;
          if (open2 != other.open2)
               return false;
          if (open3 != other.open3)
               return false;
          if (open4 != other.open4)
               return false;
          if (open5 != other.open5)
               return false;
          if (open6 != other.open6)
               return false;
          if (close0 != other.close0)
               return false;
          if (close1 != other.close1)
               return false;
          if (close2 != other.close2)
               return false;
          if (close3 != other.close3)
               return false;
          if (close4 != other.close4)
               return false;
          if (close5 != other.close5)
               return false;
          if (close6 != other.close6)
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "OpeningHours [open0=" + open0 + ", open1=" + open1 + ", open2=" + open2 + ", open3=" + open3
                    + ", open4=" + open4 + ", open5=" + open5 + ", open6=" + open6 + ", close0=" + close0 + ", close1="
                    + close1 + ", close2=" + close2 + ", close3=" + close3 + ", close4=" + close4 + ", close5=" + close5
                    + ", close6=" + close6 + "]";
     }
}

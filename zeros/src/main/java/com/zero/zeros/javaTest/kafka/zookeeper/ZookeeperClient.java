package com.zero.zeros.javaTest.kafka.zookeeper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.http.HttpStatus;

public class ZookeeperClient {
  public static void main(String[] args) {
    bokerList("127.0.0.1:2181");
  }

  public static void bokerList(String zookeeperServer) {
    ZooKeeper zk = null;
    Watcher watcher = new Watcher() {
      @Override
      public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString());
      }
    };
    try {
      zk = new ZooKeeper(zookeeperServer, 10000, watcher);
      List<String> ids = new ArrayList<String>();
      ids = zk.getChildren("/brokers/ids", false);
      JsonObject result = new JsonObject();
      result.addProperty("brokers", "");
      //logger.info("ids?" + ids + ", length?" + ids.size());
      for(int i = 0; i < ids.size(); i++) {
        try{
          //byte[] data = zk.getData("/brokers/ids/" + id, false, null);
          JsonParser parser = new JsonParser();
          JsonObject brokerData = parser.parse(new String(zk.getData("/brokers/ids/" + ids.get(i), false, null))).getAsJsonObject();
          System.out.println("test : " + brokerData);

          if(result.get("brokers").getAsString().equals("")) {
            result.addProperty("brokers", brokerData.get("host").getAsString() + ":" + brokerData.get("port").getAsString());
          }else {
            result.addProperty("brokers", result.get("brokers").getAsString() + "," + brokerData.get("host").getAsString() + ":" + brokerData.get("port").getAsString());
          }

        }catch (Exception e){
          e.printStackTrace();
        }
      }
      System.out.println("result : " + result);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if(zk != null)
          zk.close();
      }catch(InterruptedException ie) {
        ie.printStackTrace();
      }
    }

  }

}

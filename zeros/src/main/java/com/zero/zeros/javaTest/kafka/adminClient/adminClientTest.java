package com.zero.zeros.javaTest.kafka.adminClient;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConfigsOptions;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.clients.admin.DescribeTopicsOptions;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.ConfigResource.Type;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.requests.DescribeLogDirsResponse.LogDirInfo;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import org.apache.kafka.clients.ClientUtils;

public class adminClientTest {
  static int timeout = 5000;

  public static void main(String[] args) {
    //log level modify
    //LogManager.getRootLogger().setLevel(Level.ERROR);

    AdminClient client = null;

    String zookeeperList = "localhost:2181";
    String brokerList = "localhost:9092,localhost:9093,localhost:9094";

    HashMap<String, Object> conf = new HashMap<>();
    conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
    conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, timeout);
    client = AdminClient.create(conf);

    getClusterStatus(client); // client.describeCluster() - broker 정보만 조회

    boolean clusterNormal = getClusterStatusSocket(zookeeperList, brokerList); // socket 통신으로 구현

    if (clusterNormal){
      ArrayList<HashMap<String,String>> topics = new ArrayList<>();
      HashMap<String,String> topic = new HashMap<>();
      topic.put("topic-name","zero");
      topic.put("partitions","3");
      topic.put("replication-factor","2");
      topics.add(topic);
      createTopic(client, topics); // client.createTopics(newTopicList)

      Set<String> topicNames = getTopiclist(client); // client.listTopics
      ArrayList<String> topicList = new ArrayList<>();
      topicList.addAll(topicNames);

      getTopicDescribeInfo(client, topicList, "DESC"); // client.describeTopics(topics)

      getLogDirInfo(client);

      updateTopicConfig(client, "zero"); // client.alterConfigs(configs)

      updateTopicPartition(client, "zero"); // client.createPartitions(partition)

      getTopicDescribeInfo(client, topicList, "DESC"); // client.describeTopics(topics)

      ArrayList<String> delTopicList = new ArrayList<>();
      delTopicList.add("zero");
      deleteTopic(client, delTopicList); // client.deleteTopics(topics)

      //over kafka 2.0 client
      consumerGroupList(client);
    }

    client.close();
  }

  public static void getClusterStatus(AdminClient client){
    ArrayList<Node> clusterList = new ArrayList<>();
    try {
      DescribeClusterResult clusterResult = client.describeCluster();
      Collection<Node> clusterNodes = clusterResult.nodes().get(); //wait future to complete & return result

      System.out.println(clusterNodes);
      clusterList.addAll(clusterNodes);
      clusterList.forEach(cluster -> {
        System.out.println("-- cluster : " + cluster);

      });

      Node controller = clusterResult.controller().get(); //wait future to complete & return result
      System.out.println("-- controller : " + controller);

    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  public static boolean getClusterStatusSocket(String zookeeperList, String brokerList) {
    boolean result = true;
    String[] zList = zookeeperList.split(",");
    String[] bList = brokerList.split(",");
    for(int i = 0; i < zList.length; i++) {
      boolean res = checkSocket(zList[i]);
      System.out.println("zookeeper - " + zList[i] + " : "+ res);
      if(!res) result = false;
    }
    for(int i = 0; i < bList.length; i++) {
      boolean res = checkSocket(bList[i]);
      System.out.println("broker - " + bList[i] + " : " + res);
      if(!res) result = false;
    }
    return result;
  }

  public static boolean checkSocket(String hostPort) {
    boolean rechable = false;
    Socket s = null;
    String reason = null;

    String host = hostPort.substring(0, hostPort.indexOf(":"));
    int port = Integer.parseInt(hostPort.substring(hostPort.indexOf(":") + 1, hostPort.length()));
    try {
      s = new Socket();
      s.setReuseAddress(true);
      s.setSoTimeout(timeout * 2);
      SocketAddress sa = new InetSocketAddress(host, port);
      s.connect(sa, timeout * 2);
    } catch (IOException e) {
      if ( e.getMessage().equals("Connection refused")) {
        reason = "port " + port + " on " + host + " is closed.";
      };
      if ( e instanceof UnknownHostException) {
        reason = "node " + host + " is unresolved.";
      }
      if ( e instanceof SocketTimeoutException) {
        reason = "timeout while attempting to reach node " + host + " on port " + port;
      }
    } finally {
      if (s != null) {
        if ( s.isConnected()) {
          System.out.println("Port " + port + " on " + host + " is reachable!");
          rechable = true;
        } else {
          System.out.println("Port " + port + " on " + host + " is not reachable; reason: " + reason );
        }
        try {
          s.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
    return rechable;
  }

  public static Set<String> getTopiclist(AdminClient client){
    System.out.println("===== getTopiclist start =====");

    Set<String> topics = null;
    try {
      //kafka 내부 topic을 포함하여 보여 줄 경우(ex. __consumer_offsets 등)
      //timeout 설정 안하면 AdminClient의 default timeout 설정 사용
      //ListTopicsResult ltr = client.listTopics(new ListTopicsOptions().timeoutMs(5000).listInternal(true));
      ListTopicsResult topicList = client.listTopics();
      topics = topicList.names().get(); //wait future to complete & return result
      System.out.println(topics);
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      e.printStackTrace();
    }

    System.out.println("===== getTopiclist end =====");
    return topics;
  }

  public static void getTopicDescribeInfo(AdminClient client, ArrayList<String> topics, String type){
    System.out.println("===== getTopicDescribeInfo start =====");

    try {
      //timeout 설정 안하면 AdminClient의 default timeout 설정 사용
      //DescribeTopicsResult descResult = client.describeTopics(topics, new DescribeTopicsOptions().timeoutMs(timeout));
      Map<String,TopicDescription> descResult = client.describeTopics(topics).all().get(); //wait future to complete & return result

      Map<ConfigResource, Config> configResult = null;
      if (type.equals("DESC")) {
        ArrayList<ConfigResource> configList = new ArrayList<>();
        topics.forEach(topic -> {
          ConfigResource config = new ConfigResource(Type.TOPIC, topic);
          configList.add(config);
        });
        configResult = client.describeConfigs(configList).all().get(); //wait future to complete & return result
      }

      for(String topic:topics){
        TopicDescription topicDesc = descResult.get(topic);

        System.out.println("topicDesc : " + topicDesc);
        System.out.println("topic name : " + topicDesc.name());
        System.out.println("partition size : " + topicDesc.partitions().size());
        System.out.println("replication size : " + topicDesc.partitions().get(0).replicas().size());

        if (type.equals("DESC")) {
          topicDesc.partitions().forEach(partitionInfo -> {
            System.out.println(partitionInfo);
            String replicas = null;
            String isr = null;
            for(Node node : partitionInfo.replicas()){
              if(replicas != null){
                replicas += "," + node.idString();
              }else {
                replicas = node.idString();
              }
            }
            for(Node node : partitionInfo.isr()){
              if(isr != null){
                isr += "," + node.idString();
              }else {
                isr = node.idString();
              }
            }
            System.out.println("partition : " + partitionInfo.partition() + ", leader : " + partitionInfo.leader().id() + ", replicas : " + replicas + ", isr : " + isr);
          });

          Config configs = configResult.get(new ConfigResource(ConfigResource.Type.TOPIC, topic));
          configs.entries().forEach(config->{
            if(!config.isDefault()) System.out.println("--- config :" + config);
          });
        }

      }
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== getTopicDescribeInfo end =====");
  }

  public static void getLogDirInfo(AdminClient client) {
    System.out.println("===== getLogDirInfo start =====");
    try {
      DescribeLogDirsResult logDirResult = client.describeLogDirs(asList(0,1,2));
      Map<Integer, Map<String, LogDirInfo>> result = logDirResult.all().get();
      result.forEach((index, res) -> {
        res.forEach((logDir, info)-> {
          info.replicaInfos.forEach((topicPartition, replicaInfo) -> {
            System.out.println("--index : " + index + ", Log Dir : " + logDir + ", topic-partition : " + topicPartition.topic() + "-" + topicPartition.partition()
                               + ", replica offsetLag : " + replicaInfo.offsetLag+ ", replica size : " + replicaInfo.size);
          });
        });
      });

    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== getLogDirInfo end =====");
  }

  public static void updateTopicConfig(AdminClient client, String topic) {
    System.out.println("===== updateTopicConfig start =====");
    try {
      Map<ConfigResource, Config> configs = new HashMap<>();

      ConfigResource confResource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

      ArrayList<ConfigEntry> confList = new ArrayList<>();
      confList.add(new ConfigEntry("max.message.bytes","1048576"));
      confList.add(new ConfigEntry("retention.ms","3600000"));
      Config config = new Config(confList);

      configs.put(confResource, config);

      client.alterConfigs(configs).all().get();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== updateTopicConfig end =====");
  }

  public static void updateTopicPartition(AdminClient client, String topic) {
    System.out.println("===== updateTopicPartition start =====");
    try {
      Map<String, NewPartitions> partition = new HashMap<>();
      // list 형태로 추가되는 파티션에 대해 직접 broker 지정 가능함 (ex. 기존 3개 파티션인 경우 5개로 늘리는 경우 2개 추가 파티션에 대해 broker 지정)
      //partition.put(topic, NewPartitions.increaseTo(5, asList(asList(0,1), asList(0,2))));
      partition.put(topic, NewPartitions.increaseTo(4));

      client.createPartitions(partition).all().get();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== updateTopicPartition end =====");
  }

  public static void createTopic(AdminClient client, ArrayList<HashMap<String,String>> topics) {
    System.out.println("===== createTopic start =====");
    try {
      ArrayList<NewTopic> newTopicList = new ArrayList<>();
      topics.forEach(topic -> {
        NewTopic newTopic = new NewTopic(topic.get("topic-name"), Integer.parseInt(topic.get("partitions")), Short.parseShort(topic.get("replication-factor")));
        HashMap<String,String> topicConfig = new HashMap<>();
        topicConfig.put("max.message.bytes","10485760"); //10MB
        newTopic.configs(topicConfig);
        newTopicList.add(newTopic);
      });
      CreateTopicsResult result = client.createTopics(newTopicList);
      result.all().get(); //wait future to complete & return result
      topics.forEach(topic -> {
        System.out.println(topic.get("topic-name") + " : " + result.values().get(topic.get("topic-name")).isDone());
      });
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      System.out.println("--- exception : " + e.getMessage());
      e.printStackTrace();
    }
    System.out.println("===== createTopic end =====");
  }

  public static void deleteTopic(AdminClient client, ArrayList<String> topics) {
    System.out.println("===== deleteTopic start =====");
    try {
     DeleteTopicsResult result = client.deleteTopics(topics);
     result.all().get(); //wait future to complete & return result
     System.out.println(result.values().get("zero").isDone());
    }catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("===== deleteTopic end =====");
  }

  public static void consumerGroupList(AdminClient client) {
    System.out.println("===== consumerGroupList start =====");
    try {
      Collection<ConsumerGroupListing> res = client.listConsumerGroups().all().get();
      ArrayList<String> consumerGroups = new ArrayList<>();
      res.forEach(consumer -> {
        System.out.println("--- consumer : " + consumer.toString());
        consumerGroups.add(consumer.groupId());
      });

      Map<String, ConsumerGroupDescription> consumerDesc = client.describeConsumerGroups(consumerGroups).all().get();
      consumerDesc.forEach((group, desc) -> {
        System.out.println("--- group : " + group + ", desc : " + desc);
      });

      consumerGroups.forEach(grp -> {
        ListConsumerGroupOffsetsResult offsets = client.listConsumerGroupOffsets(grp);
        System.out.println("--- grp : " + grp);
        try{
          Map<TopicPartition, OffsetAndMetadata> offset = offsets.partitionsToOffsetAndMetadata().get();
          offset.forEach((topic, offsetMeta) -> {
            System.out.println("--- topic : " + topic + ", offsetMeta : " + offsetMeta);
          });

        }catch (InterruptedException e) {
          e.printStackTrace();
        }catch (ExecutionException e) {
          //include timeout
          e.printStackTrace();
        }
      });

    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== consumerGroupList end =====");
  }






  public static void deleteTopic2(AdminClient client, ArrayList<String> topics) {
    System.out.println("===== deleteTopic start =====");
    try {
      DescribeTopicsResult descResult = client.describeTopics(topics);
      Map<String,TopicDescription> resultMap = descResult.all().get();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== deleteTopic end =====");
  }
}

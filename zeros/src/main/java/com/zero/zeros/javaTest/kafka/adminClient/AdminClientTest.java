package com.zero.zeros.javaTest.kafka.adminClient;

import static java.util.Arrays.asList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.TopicPartitionReplica;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.ConfigResource.Type;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.requests.DescribeLogDirsResponse.LogDirInfo;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import org.apache.kafka.clients.ClientUtils;

public class AdminClientTest {
  static int timeout = 5000;

  public static void main(String[] args) {
    //log level modify
    //LogManager.getRootLogger().setLevel(Level.ERROR);

    AdminClient client = null;

    String zookeeperList = "localhost:2181";
    String brokerList = "localhost:9092,localhost:9093,localhost:9094";

    Map<String, Object> conf = new HashMap<>();
    conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
    conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, timeout);
    client = AdminClient.create(conf);

    getClusterStatus(client); // client.describeCluster() - broker 정보만 조회

    boolean clusterNormal = getClusterStatusSocket(zookeeperList, brokerList); // socket 통신으로 구현

    //if (clusterNormal){
/*
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

      updateTopicConfig(client, "zero", "ADD"); // client.alterConfigs(configs)

      getTopicDescribeInfo(client, topicList, "DESC"); // client.describeTopics(topics)
                                                       // client.describeConfigs(configList).all().get();

      getLogDirInfo(client); // client.describeLogDirs(asList(0,1,2));

      updateTopicPartition(client, "zero"); // client.createPartitions(partition)

      updateTopicConfig(client, "zero", ""); // client.alterConfigs(remove)

      getTopicDescribeInfo(client, topicList, "DESC"); // client.describeTopics(topics)
                                                       // client.describeConfigs(configList).all().get();

      ArrayList<String> delTopicList = new ArrayList<>();
      delTopicList.add("zero");
      deleteTopic(client, delTopicList); // client.deleteTopics(topics)

      //over kafka 2.0 client
      ArrayList<String> consumerGroupList = consumerGroupList(client); // client.listConsumerGroups().all().get();

      //admin client 테스트용 (미사용) consumerGroupDesc(client, consumerGroupList);
      JsonObject consumerGroupPartitions = getConsumerGroupAssignedPartitions(client,"test11"); // client.describeConsumerGroups(consumerGroups).all().get();
                                                                                                // client.listConsumerGroupOffsets(groupName);

      JsonObject config = new JsonObject();
      config.addProperty("bootstrapServer", brokerList);
      config.addProperty("groupName","test11");
      getTopicOffsetInfo(config, consumerGroupPartitions); // consumer client

      deleteConsumerGroup(client, "test11"); // client.deleteConsumerGroups(groupNames).all().get();

      //kafka 1.1 broker 이상부터 가능
      //alterReplicaLogDirs(client); // client.alterReplicaLogDirs(replicaAssignment).all().get();

      //kafka 2.2 broker 이상부터 가능, 추후 고려
      //electPreferredLeaders(client, topicList);
*/

    //}

    //getLogDirInfo(client); // client.describeLogDirs(asList(0,1,2));

    client.close();
  }

  private static KafkaConsumer createKafkaConsumerClient(String bootstrapServer, String groupName) {
    Properties props = new Properties();
    props.put("bootstrap.servers", bootstrapServer);
    props.put("key.deserializer", "org.apache.kafka.common.serializati on.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("default.api.timeout.ms", timeout);

    if (groupName != null && !"".equals(groupName)) {
      props.put("group.id", groupName);
    } else {
      props.put("group.id", "AccuInsight_RP_Queue_Consumer");
    }

    KafkaConsumer<String,String> consumer = new KafkaConsumer<>(props);
    return consumer;
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

    JsonArray topicInfoList = new JsonArray();
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
        JsonObject topicInfo = new JsonObject();
        JsonArray partition = new JsonArray();

        TopicDescription topicDesc = descResult.get(topic);

        topicInfo.addProperty("Topic", topicDesc.name());
        topicInfo.addProperty("PartitionCount", topicDesc.partitions().size());
        topicInfo.addProperty("ReplicationFactor", topicDesc.partitions().get(0).replicas().size());
        System.out.println(topicInfo);

        Boolean dead = false;
        Boolean unhealthy = false;
        Boolean healthy = false;

        for (TopicPartitionInfo partitionInfo : topicDesc.partitions()){
          //System.out.println(partitionInfo);
          String replicas = "";
          String isr = "";
          for(Node node : partitionInfo.replicas()){
            if(replicas != ""){
              replicas += "," + node.idString();
            }else {
              replicas = node.idString();
            }
          }
          for(Node node : partitionInfo.isr()){
            if(isr != ""){
              isr += "," + node.idString();
            }else {
              isr = node.idString();
            }
          }

          JsonObject pInfo = new JsonObject();
          pInfo.addProperty("Topic", topic);
          pInfo.addProperty("Partition", partitionInfo.partition());
          pInfo.addProperty("Leader", partitionInfo.leader() == null?"":partitionInfo.leader().idString());
          pInfo.addProperty("Replicas", replicas);
          pInfo.addProperty("Isr", isr);

          if(partitionInfo.leader() == null){
            System.out.println("partition : " + partitionInfo.partition() + ", leader : " + ", replicas : " + replicas + ", isr : " + isr + " === dead");
            pInfo.addProperty("Status", "dead");
            dead = true;
          } else {
            if (partitionInfo.replicas().size() == partitionInfo.isr().size()){
              System.out.println("partition : " + partitionInfo.partition() + ", leader : " + partitionInfo.leader().idString() + ", replicas : " + replicas + ", isr : " + isr + " === healty");
              pInfo.addProperty("Status", "healthy");
              healthy = true;
            } else {
              System.out.println("partition : " + partitionInfo.partition() + ", leader : " + partitionInfo.leader().idString() + ", replicas : " + replicas + ", isr : " + isr + " === unhealty");
              pInfo.addProperty("Status", "unhealthy");
              unhealthy = true;
            }
          }

          if (type.equals("DESC")) {
            partition.add(pInfo);
          }
        }

        if (dead && !unhealthy && !healthy){
          System.out.println("=== dead");
          topicInfo.addProperty("status", "dead");
        } else if (dead && (unhealthy || healthy)) {
          System.out.println("=== unhealthy");
          topicInfo.addProperty("status", "unhealthy");
        } else if (!dead && unhealthy) {
          System.out.println("=== healthy : exceptReplica");
          topicInfo.addProperty("status", "exceptReplica");
        } else if (!dead && !unhealthy && healthy) {
          System.out.println("=== healthy");
          topicInfo.addProperty("status", "healthy");
        }

        if (type.equals("DESC")) {
          Config configs = configResult.get(new ConfigResource(ConfigResource.Type.TOPIC, topic));

          JsonObject configInfo = new JsonObject();
          configs.entries().forEach(config->{
            if(!config.isDefault() && !"STATIC_BROKER_CONFIG".equals(config.source().toString())) {
              System.out.println("--- config :" + config + ", " + config.source().toString());
              configInfo.addProperty(config.name(), config.value());
            }
          });
          if (configInfo != null) topicInfo.add("Configs", configInfo);
          topicInfo.add("partition", partition);
        }
        topicInfoList.add(topicInfo);
      }
      System.out.println("===topicInfoList : " + topicInfoList);
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
            if (topicPartition.topic().equals("test2"))
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

  public static void updateTopicConfig(AdminClient client, String topic, String type) {
    System.out.println("===== updateTopicConfig start =====");
    try {
      Map<ConfigResource, Config> configs = new HashMap<>();

      ConfigResource confResource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

      ArrayList<ConfigEntry> confList = new ArrayList<>();
      confList.add(new ConfigEntry("max.message.bytes","1048576"));
      confList.add(new ConfigEntry("retention.ms","3600000"));

      if(type.equals("ADD")) {
        Config config = new Config(confList);
        configs.put(confResource, config);
      } else {
        ArrayList<ConfigEntry> test = new ArrayList<>();
        test.add(new ConfigEntry("max.message.bytes","10485760"));
        Config config = new Config(test);
        configs.put(confResource, config);
      }

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
      partition.put(topic, NewPartitions.increaseTo(5));

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

  public static ArrayList<String> consumerGroupList(AdminClient client) {
    System.out.println("===== consumerGroupList start =====");
    ArrayList<String> consumerGroups = new ArrayList<>();
    try {
      Collection<ConsumerGroupListing> res = client.listConsumerGroups().all().get();
      res.forEach(consumer -> {
        System.out.println("--- consumer : " + consumer.toString());
        consumerGroups.add(consumer.groupId());
      });
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== consumerGroupList end =====");
    return consumerGroups;
  }

  //admin api test용
  public static void consumerGroupDesc(AdminClient client, ArrayList<String> consumerGroups) {
    System.out.println("===== consumerGroupDesc start =====");
    try {
      Map<String, ConsumerGroupDescription> consumerDesc = client.describeConsumerGroups(consumerGroups).all().get();
      consumerDesc.forEach((group, desc) -> {
        System.out.println("--- group : " + group);
        System.out.println("--- desc groupId " + desc.groupId() + ", partitionAssignor : " + desc.partitionAssignor() + ", state : " + desc.state()
          + "\n, member : " + desc.members());
        if (!desc.members().isEmpty()){
          Collection<MemberDescription> list = desc.members();
          System.out.println("--- desc member " + list.size());
          list.forEach(member -> {
            System.out.println("  --- member clientId : " + member.clientId() + ", consumerId : " + member.consumerId() + ", host : " + member.host()
                + ", " + member.assignment().toString());
          });
        }
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
    System.out.println("===== consumerGroupDesc end =====");
  }

  public static JsonObject getConsumerGroupAssignedPartitions(AdminClient client, String groupName) {
    System.out.println("===== getConsumerGroupAssignedPartitions start =====");
    JsonObject partitionResult = new JsonObject();
    try {
      ArrayList<String>  consumerGroups = new ArrayList();
      consumerGroups.add(groupName);

      //groupName에 해당하는 consumerGroup 정보 조회
      JsonArray groupList = new JsonArray();
      Map<String, ConsumerGroupDescription> consumerDesc = client.describeConsumerGroups(consumerGroups).all().get();
      System.out.println("--- describeConsumerGroups : " + consumerDesc);

      JsonObject groupInfo = new JsonObject();
      groupInfo.addProperty("groupId", groupName);
      groupInfo.addProperty("partitionAssignor", consumerDesc.get(groupName).partitionAssignor());
      groupInfo.addProperty("state", consumerDesc.get(groupName).state().toString());
      partitionResult.add("groupInfo", groupInfo);

      Map<TopicPartition, JsonObject> partitionMembers = new HashMap<>();
      if (!consumerDesc.get(groupName).members().isEmpty()) {
        Collection<MemberDescription> list = consumerDesc.get(groupName).members();
        list.forEach(member -> {
          member.assignment().topicPartitions().forEach(topicPartition -> {
            JsonObject part = new JsonObject();
            part.addProperty("clientId", member.clientId());
            part.addProperty("consumerId", member.consumerId());
            part.addProperty("host", member.host());
            partitionMembers.put(topicPartition, part);
          });
        });
      }

      JsonArray partitionList = new JsonArray();
      //consumerGroup에 Assign된 topic/partition 정보 조회
      ListConsumerGroupOffsetsResult offsets = client.listConsumerGroupOffsets(groupName);
      Map<TopicPartition, OffsetAndMetadata> offset = offsets.partitionsToOffsetAndMetadata().get();
      offset.forEach((topic, offsetMeta) -> {
        System.out.println("--- listConsumerGroupOffsets : topic > " + topic + ", " + offsetMeta);
        if (partitionMembers.containsKey(topic)) {
          partitionMembers.get(topic).addProperty("offset", offsetMeta.offset());
        } else {
          JsonObject part = new JsonObject();
          part.addProperty("clientId", "");
          part.addProperty("consumerId", "");
          part.addProperty("host", "");
          part.addProperty("offset", offsetMeta.offset());
          partitionMembers.put(topic, part);
        }
      });
      JsonParser parser = new JsonParser();
      partitionResult.add("partitionInfo", parser.parse(partitionMembers.toString()));
      System.out.println("--- partitionResult : " + partitionResult);

    } catch (Exception e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== getConsumerGroupAssignedPartitions end =====");
    return partitionResult;
  }

  public static void getTopicOffsetInfo (JsonObject config, JsonObject partitionInfos) {
    System.out.println("===== getTopicOffsetInfo start =====");
    System.out.println("---- config : "+ config);
    KafkaConsumer<String,String> consumer = createKafkaConsumerClient(config.get("bootstrapServer").getAsString(),config.get("groupName").getAsString());
    System.out.println("---- partitionInfos : "+ partitionInfos);
    JsonObject offsetResult = new JsonObject();
    try {
      ArrayList<TopicPartition> partitions = new ArrayList<>();
      JsonObject partitionMembers = new JsonObject();
      if (partitionInfos.size() == 0) {
        //topic partition 정보 조회
        List<PartitionInfo> plist = consumer.partitionsFor(config.get("topic").toString());
        plist.forEach(p -> {
          //System.out.println("===== topic: " + p.topic() + ", partition: " + p.partition() + ", info: " + p.toString());
          partitions.add(new TopicPartition(config.get("topic").getAsString(), p.partition()));
        });
      } else {
        //consumer group의 partition 정보 셋팅
        partitionMembers = partitionInfos.getAsJsonObject("partitionInfo");
        partitionMembers.keySet().forEach(topicPartition -> {
          partitions.add(new TopicPartition(topicPartition.substring(0,topicPartition.lastIndexOf("-")), Integer.parseInt(topicPartition.substring(topicPartition.lastIndexOf("-")+1))));
        });
        partitions.forEach(v -> System.out.println("--- partitions : " + v));
      }

      //partition assign
      consumer.assign(partitions);

      //partition offset 조회
      Map<TopicPartition, Long> currentOffsets = new HashMap<>();
      partitions.forEach(topicPartition ->
          currentOffsets.put(topicPartition, consumer.position(topicPartition))
      );
      Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
      Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(partitions);

      JsonArray offsetList = new JsonArray();
      for(TopicPartition topicPartition : partitions) {
        //partitions.forEach(topicPartition ->{
        //System.out.println("===== TopicPartition: " + t + ", begin: " + beginOffsets.get(t) + ", end: " + endOffsets.get(t)
        //    + ", current: " + currentOffsets.get(t) + ", lag: " + (endOffsets.get(t) - currentOffsets.get(t)) );
        JsonObject offsetInfo = new JsonObject();
        offsetInfo.addProperty("topic",topicPartition.topic());
        offsetInfo.addProperty("partition",topicPartition.partition());
        offsetInfo.addProperty("beginOffset", beginOffsets.get(topicPartition));
        offsetInfo.addProperty("endOffset", endOffsets.get(topicPartition));
        if (partitionInfos.size() != 0) {
          offsetInfo.addProperty("currentOffset", currentOffsets.get(topicPartition));
          offsetInfo.addProperty("lag", endOffsets.get(topicPartition) - currentOffsets.get(topicPartition));
          //offsetInfo.mergeIn(partitionMembers.getJsonObject(topicPartition.toString()));
          JsonObject member = partitionMembers.getAsJsonObject(topicPartition.toString());
          offsetInfo.addProperty("clientId", member.get("clientId").getAsString());
          offsetInfo.addProperty("consumerId", member.get("consumerId").getAsString());
          offsetInfo.addProperty("host", member.get("host").getAsString());
          offsetInfo.addProperty("offset", member.get("offset").getAsString());
        }
        offsetList.add(offsetInfo);
        //});
      }
      offsetResult.add("list",offsetList);
      System.out.println("--- offsetResult : " + offsetResult);
    } catch(Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      //System.out.println("-- consumer.close()");
      consumer.close();
    }
    System.out.println("===== getTopicOffsetInfo end =====");
    //return offsetResult.toString();
  }

  public static void deleteConsumerGroup(AdminClient client, String groupName) {
    System.out.println("===== deleteConsumerGroup start =====");
    try {
      ArrayList<String> groupNames = new ArrayList();
      for(String group : groupName.split(",")) groupNames.add(group);
      System.out.println("--- groupNames : " + groupNames);
      client.deleteConsumerGroups(groupNames).all().get();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== deleteConsumerGroup end =====");
  }

  public static void alterReplicaLogDirs(AdminClient client) {
    System.out.println("===== alterReplicaLogDirs start ===== kafka broker 1.1 이상부터 가능, 추후 고려");
    try {
      TopicPartitionReplica tp = new TopicPartitionReplica("test2",4,2);
      Map<TopicPartitionReplica, String> replicaAssignment = new HashMap<>();
      replicaAssignment.put(tp, "/tmp/kafka-logs31");
      client.alterReplicaLogDirs(replicaAssignment).all().get();
    } catch (Exception e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== alterReplicaLogDirs end =====");
  }

  public static void electPreferredLeaders(AdminClient client, ArrayList<String> topicPartitions) {
    System.out.println("===== electPreferredLeaders start ===== kafka broker 2.2 이상부터 가능, 추후 고려");
    try {
      ArrayList<TopicPartition> partitions = new ArrayList<>();
      partitions.add(new TopicPartition("test2",1));
      client.electPreferredLeaders(partitions).all().get();
    }catch (InterruptedException e) {
      e.printStackTrace();
    }catch (ExecutionException e) {
      //include timeout
      e.printStackTrace();
    }
    System.out.println("===== electPreferredLeaders end =====");
  }
}

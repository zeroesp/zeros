package com.zero.zeros.javaTest.jmxTest;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxClient {
	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		
		// Create listener
        //ClientListener listener = new ClientListener();
		
		// Get an MBeanServerConnection
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

		// Get domains from MBeanServer
		System.out.println("\nnDomains:");
		String domains[] = mbsc.getDomains();
		for (String domain : domains) {
			System.out.println("\tDomain = " + domain);
        }
		
		// Get MBean count
		System.out.println("\nMBean count = " + mbsc.getMBeanCount());
		
		// Query MBean names
		System.out.println("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
        	System.out.println("\tObjectName = " + name);
        }
        
        waitForEnterPressed();
        
        System.out.println("\n■■■■■");
        
        while(true) {
        	System.out.println("\n1. BytesInPerSec Count : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=zero"), "Count")
        	                   + ", MeanRate : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=zero"), "MeanRate"));
        	System.out.println("2. BytesOutPerSec Count : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec,topic=zero"), "Count")
                               + ", MeanRate : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec,topic=zero"), "MeanRate"));
        	System.out.println("3. TotalProduceRequestsPerSec Count : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=TotalProduceRequestsPerSec,topic=zero"), "Count"));
        	System.out.println("4. TotalFetchRequestsPerSec Count : " + mbsc.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=TotalFetchRequestsPerSec,topic=zero"), "Count"));
        	Thread.sleep(1000);
        }
        
        
        
        // Close MBeanServer connection
        /*
        System.out.println("\nClose the connection to the server");
        jmxc.close();
        System.out.println("\nEnd");
        */
	}
	
	private static void waitForEnterPressed() {
        try {
        	System.out.println("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

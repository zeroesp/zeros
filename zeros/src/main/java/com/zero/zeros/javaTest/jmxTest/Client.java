package com.zero.zeros.javaTest.jmxTest;

/* 참고
 * https://docs.oracle.com/javase/tutorial/jmx/remote/custom.html
 * https://docs.oracle.com/javase/tutorial/jmx/remote/jconsole.html
 */

/* 실행결과

Create an RMI connector client and connect it to the RMI connector server

Get an MBeanServerConnection

Press <Enter> to continue...


Domains:
	Domain = JMImplementation
	Domain = com.example
	Domain = com.sun.management
	Domain = java.lang
	Domain = java.nio
	Domain = java.util.logging

Press <Enter> to continue...

MBeanServer default domain = DefaultDomain

MBean count = 24

Query MBeanServer MBeans:
	ObjectName = JMImplementation:type=MBeanServerDelegate
	ObjectName = com.example:type=Hello
	ObjectName = com.example:type=QueueSampler
	ObjectName = com.sun.management:type=DiagnosticCommand
	ObjectName = com.sun.management:type=HotSpotDiagnostic
	ObjectName = java.lang:type=ClassLoading
	ObjectName = java.lang:type=Compilation
	ObjectName = java.lang:type=GarbageCollector,name=PS MarkSweep
	ObjectName = java.lang:type=GarbageCollector,name=PS Scavenge
	ObjectName = java.lang:type=Memory
	ObjectName = java.lang:type=MemoryManager,name=CodeCacheManager
	ObjectName = java.lang:type=MemoryManager,name=Metaspace Manager
	ObjectName = java.lang:type=MemoryPool,name=Code Cache
	ObjectName = java.lang:type=MemoryPool,name=Compressed Class Space
	ObjectName = java.lang:type=MemoryPool,name=Metaspace
	ObjectName = java.lang:type=MemoryPool,name=PS Eden Space
	ObjectName = java.lang:type=MemoryPool,name=PS Old Gen
	ObjectName = java.lang:type=MemoryPool,name=PS Survivor Space
	ObjectName = java.lang:type=OperatingSystem
	ObjectName = java.lang:type=Runtime
	ObjectName = java.lang:type=Threading
	ObjectName = java.nio:type=BufferPool,name=direct
	ObjectName = java.nio:type=BufferPool,name=mapped
	ObjectName = java.util.logging:type=Logging

Press <Enter> to continue...


>>> Perform operations on Hello MBean <<<

Add notification listener...

CacheSize = 200

Waiting for notification...

Received notification:
	ClassName: javax.management.AttributeChangeNotification
	Source: com.example:type=Hello
	Type: jmx.attribute.change
	Message: CacheSize changed
	AttributeName: CacheSize
	AttributeType: int
	NewValue: 150
	OldValue: 200

CacheSize = 150

Invoke sayHello() in Hello MBean...

Invoke add(2, 3) in Hello MBean...

add(2, 3) = 5

Press <Enter> to continue...

>>> Perform operations on QueueSampler MXBean <<<

QueueSample.Date = Thu Feb 01 16:23:03 KST 2018
QueueSample.Head = Request-1
QueueSample.Size = 3

Invoke clearQueue() in QueueSampler MXBean...

QueueSample.Date = Thu Feb 01 16:23:03 KST 2018
QueueSample.Head = null
QueueSample.Size = 0

Press <Enter> to continue...


Close the connection to the server

Bye! Bye!  
 */

/*
 * Client.java - JMX client that interacts with the JMX agent. It gets
 * attributes and performs operations on the Hello MBean and the QueueSampler
 * MXBean example. It also listens for Hello MBean notifications.
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.management.AttributeChangeNotification;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Client {

    /**
     * Inner class that will handle the notifications.
     */
    public static class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification,
                                       Object handback) {
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                    (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
        }
    }

    /* For simplicity, we declare "throws Exception".
       Real programs will usually want finer-grained exception handling. */
    public static void main(String[] args) throws Exception {
        // Create an RMI connector client and
        // connect it to the RMI connector server
        //
        echo("\nCreate an RMI connector client and " +
             "connect it to the RMI connector server");
        JMXServiceURL url =
            new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        echo("\n1");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        echo("\n2");

        // Create listener
        //
        ClientListener listener = new ClientListener();

        // Get an MBeanServerConnection
        //
        echo("\nGet an MBeanServerConnection");
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        waitForEnterPressed();

        // Get domains from MBeanServer
        //
        echo("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
            echo("\tDomain = " + domain);
        }
        waitForEnterPressed();

        // Get MBeanServer's default domain
        //
        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

        // Get MBean count
        //
        echo("\nMBean count = " + mbsc.getMBeanCount());

        // Query MBean names
        //
        echo("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names =
            new TreeSet<ObjectName>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
            echo("\tObjectName = " + name);
        }
        waitForEnterPressed();

        // ----------------------
        // Manage the Hello MBean
        // ----------------------

        echo("\n>>> Perform operations on Hello MBean <<<");

        // Construct the ObjectName for the Hello MBean
        //
        ObjectName mbeanName = new ObjectName("com.example:type=Hello");

        // Create a dedicated proxy for the MBean instead of
        // going directly through the MBean server connection
        //
        HelloMBean mbeanProxy =
            JMX.newMBeanProxy(mbsc, mbeanName, HelloMBean.class, true);

        // Add notification listener on Hello MBean
        //
        echo("\nAdd notification listener...");
	mbsc.addNotificationListener(mbeanName, listener, null, null);

        // Get CacheSize attribute in Hello MBean
        //
        echo("\nCacheSize = " + mbeanProxy.getCacheSize());

        // Set CacheSize attribute in Hello MBean
        // Calling "reset" makes the Hello MBean emit a
        // notification that will be received by the registered
        // ClientListener.
        //
        mbeanProxy.setCacheSize(150);

        // Sleep for 2 seconds to have time to receive the notification
        //
        echo("\nWaiting for notification...");
        sleep(2000);

        // Get CacheSize attribute in Hello MBean
        //
        echo("\nCacheSize = " + mbeanProxy.getCacheSize());

        // Invoke "sayHello" in Hello MBean
        //
        echo("\nInvoke sayHello() in Hello MBean...");
        mbeanProxy.sayHello();

        // Invoke "add" in Hello MBean
        //
        echo("\nInvoke add(2, 3) in Hello MBean...");
        echo("\nadd(2, 3) = " + mbeanProxy.add(2, 3));

        waitForEnterPressed();

        // ------------------------------
        // Manage the QueueSampler MXBean
        // ------------------------------

        echo("\n>>> Perform operations on QueueSampler MXBean <<<");

        // Construct the ObjectName for the QueueSampler MXBean
        //
        ObjectName mxbeanName =
            new ObjectName("com.example:type=QueueSampler");

        // Create a dedicated proxy for the MXBean instead of
        // going directly through the MBean server connection
        //
        QueueSamplerMXBean mxbeanProxy =
            JMX.newMXBeanProxy(mbsc, mxbeanName, QueueSamplerMXBean.class);

        // Get QueueSample attribute in QueueSampler MXBean
        //
        QueueSample queue1 = mxbeanProxy.getQueueSample();
        echo("\nQueueSample.Date = " + queue1.getDate());
        echo("QueueSample.Head = " + queue1.getHead());
        echo("QueueSample.Size = " + queue1.getSize());

        // Invoke "clearQueue" in QueueSampler MXBean
        //
        echo("\nInvoke clearQueue() in QueueSampler MXBean...");
        mxbeanProxy.clearQueue();

        // Get QueueSample attribute in QueueSampler MXBean
        //
        QueueSample queue2 = mxbeanProxy.getQueueSample();
        echo("\nQueueSample.Date = " + queue2.getDate());
        echo("QueueSample.Head = " + queue2.getHead());
        echo("QueueSample.Size = " + queue2.getSize());

        waitForEnterPressed();

        // Close MBeanServer connection
        //
        echo("\nClose the connection to the server");
        jmxc.close();
        echo("\nBye! Bye!");
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

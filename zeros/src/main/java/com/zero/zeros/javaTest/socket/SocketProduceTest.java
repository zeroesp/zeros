package com.zero.zeros.javaTest.socket;

import java.io.OutputStream;
import java.net.Socket;
import org.apache.commons.lang3.time.FastDateFormat;

public class SocketProduceTest {
  public static void main(String[] args) throws Exception{
    //tcp 해당 port에 produce
    Socket socket = new Socket("169.56.124.28", 17888);
    OutputStream out = socket.getOutputStream();
    int i=0;
    while(true){
      String date = FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss.SSS").format(System.currentTimeMillis());
      String msg = date+",aa,bb,cc,test" + i + "\n";
      out.write(msg.getBytes());
      out.flush();
      Thread.sleep(1000);
      i++;
    }
  }
}

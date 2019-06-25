package com.zero.zeros.javaTest.socket;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.lang3.time.FastDateFormat;

public class SocketConsumeTest {
  public static void main(String[] args) throws Exception{
    //tcp 해당 port에 produce
    Socket socket = new Socket("169.56.124.28", 17888);
    InputStream in = socket.getInputStream();
    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
    OutputStreamWriter osw = new OutputStreamWriter(System.out);
    while(true){
      int readByte = isr.read();
      if(readByte == -1) break;
      osw.write(readByte);
      osw.flush();
    }
    socket.close();
  }
}

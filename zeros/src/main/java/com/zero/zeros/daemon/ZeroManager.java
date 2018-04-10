package com.zero.zeros.daemon;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

/*
ZeroManager.java - CRUD 소스 (10초에 한번 조회하여 처리)
zero.prop - DB접속 설정, 쿼리
 
라이브러리 - jtds-1.2.2.jar, ojdbc14.jar
 
start.sh - 데몬 실행 쉘(j.sh nohup으로 실행)
j.sh - java 컴파일 및 실행
 
log_delete.sh - 로그삭제 쉘 (10일 이상된 파일 삭제, crontab 등록해야 함)
 
[crontab]
#=============================================
# log 삭제 (매일 00시 00분에 실행)
#---------------------------------------------
0 0 * * * /log/zero/log_delete.sh > /dev/null
#=============================================​
*/

public class ZeroManager
{
 public static final String PROP_FILE_NAME_STR = "zero.prop";

 private static PrintStream ps = null;

 private String m_sPropFileName;
 private Properties m_prop = new Properties();

 private Connection m_conZero = null;

 //생성자
 public ZeroManager()
 {
  m_sPropFileName = PROP_FILE_NAME_STR;
 }

 //생성자
 public ZeroManager(String sPropFileName)
 {
  m_sPropFileName = sPropFileName;
 }
 
 //로그 처리
 public static void log(String sLog)
 {
  GregorianCalendar gcld = new GregorianCalendar();
  PrintStream tps = null;
  String sDt = "";

  sDt = (gcld.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")
      + gcld.get(Calendar.HOUR_OF_DAY) + ":"
      + (gcld.get(Calendar.MINUTE) < 10 ? "0" : "")
      + gcld.get(Calendar.MINUTE) + ":"
      + (gcld.get(Calendar.SECOND) < 10 ? "0" : "")
      + gcld.get(Calendar.SECOND) + "."
      + (gcld.get(Calendar.MILLISECOND) < 10 ? "00" :
         gcld.get(Calendar.MILLISECOND) < 100 ? "0" : "")
      + gcld.get(Calendar.MILLISECOND) + " ";

  System.out.print(sDt);
  System.out.println(sLog);

  if ( ps != null )
  {
   ps.print(sDt);
   ps.println(sLog);
  }
 }

 //프로퍼티 로드
 private void loadProp() throws Throwable 
 {
  m_prop.load(new FileInputStream(m_sPropFileName));

  if ( m_prop.getProperty("log.directory") != null )
  {
   try
   {
    GregorianCalendar gcld = new GregorianCalendar();
    String sDt = "";
    sDt = gcld.get(Calendar.YEAR)
       + (gcld.get(Calendar.MONTH) < 9 ? "0" : "")
       + (gcld.get(Calendar.MONTH) + 1)
       + (gcld.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")
       + gcld.get(Calendar.DAY_OF_MONTH);
    ps = new PrintStream(new FileOutputStream(m_prop.getProperty("log.directory")+"zero_"+sDt+".log", true));
   }
   catch ( Throwable t )
   {
    t.printStackTrace();
   }
  }

  log("loadProp");
 }

 //DB connection Zero
 private void dbConnectZero() throws Throwable
 {
  Class.forName(m_prop.getProperty("db.zero.driver"));
  m_conZero = DriverManager.getConnection(m_prop.getProperty("db.zero.url"),
                                         m_prop.getProperty("db.zero.id"),
                                         m_prop.getProperty("db.zero.pass"));

  log("dbConnectZero");
 }
 
 //DB disconnection Zero
 private void dbDisconZero() throws Throwable
 {
  try
  {
   if ( m_conZero != null )
   {
    m_conZero.close();
    m_conZero = null;
   }
  }
  catch ( Throwable t )
  {
    throw t;
  }

  log("dbDisconZero");
 }

 //입력
 private void insZeroSend(String id,
                         String name) throws Throwable
 {
  log("insZeroSend Begin id:" + id
                    + ",name:" + name);

  PreparedStatement pstmt = null;

  try
  {
   pstmt = m_conZero.prepareStatement(m_prop.getProperty("db.zero.ins"));
   pstmt.setString(1, id);
   pstmt.setString(2, name);
   pstmt.execute();
  }
  catch ( Throwable t )
  {
   throw t;
  }
  finally
  {
   if ( pstmt != null )
   {
    pstmt.close();
    pstmt = null;
   }
  }

  log("insZeroSend End");
 }

 //수정
 private void updZeroSend(String id,
                        String name) throws Throwable
 {
  log("□upd Begin [" + id + "]" + id);

  PreparedStatement pstmt = null;
  String sql = "";

  sql = m_prop.getProperty("db.zero.upd");

  try
  {
   pstmt = m_conZero.prepareStatement(sql);
   pstmt.setString(1, id);
   pstmt.setString(2, name);
   pstmt.execute();
  }
  catch ( Throwable t )
  {
   throw t;
  }
  finally
  {
   if ( pstmt != null )
   {
    pstmt.close();
    pstmt = null;
   }
  }

  log("□upd End");
 }

 
 private void selZeroSend() throws Throwable
 {
  log("■ Begin");

  PreparedStatement pstmt = null;
  ResultSet rset = null;
  HttpURLConnection con = null;
  boolean urlCall = false;

  try
  {
   pstmt = m_conZero.prepareStatement(m_prop.getProperty("db.zero.sel"));
   rset = pstmt.executeQuery();

   while ( rset.next() )
   {
    log("send [" + rset.getString("ID") + "]" + rset.getString("NAME"));
    urlCall = false;
    
    String id = rset.getString("ID");
    String name = rset.getString("NAME");
    String name_en = "";
    
    try{

     name_en = URLEncoder.encode(name, "UTF-8");

     URL url = new URL("http://zero.dothome.co.kr/g5-5.0.18/index.php?name"+name_en);

     con = (HttpURLConnection) url.openConnection();
     
     //timeout설정 java 1.4버전 이하
     //System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
     //System.setProperty("sun.net.client.defaultReadTimeout", "5000");
     
     log("urlCon : " + con.getResponseMessage() + "," + con.getResponseCode());

     /*
     //요청한 URL에 대한 응답 내용 출력
     BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
     StringBuffer buffer = new StringBuffer();
     String line = null;
     if((line = reader.readLine()) != null) {
         buffer.append(line);
     }
     reader.close();
     log("urlCon res : " + buffer.toString());
     */

     //정상일 경우만 update
     if(con.getResponseCode() == 200){
         urlCall = true;
     }
     
    }
    catch( Throwable t )
    {
      t.printStackTrace();

      log("urlConnection err");
    }
    
    if(urlCall){
        try
        {
         updZeroSend(id,
                     name);
        }
        catch ( SQLException e )
        {
         e.printStackTrace();
    
         log("selZeroSend updZeroSend Catch SQLException Code : " + e.getErrorCode());
        }
        catch ( Throwable t )
        {
          t.printStackTrace();
    
          log("selZeroSend updZeroSend Catch Throwable");
        }
    }

   } /* end of while */
  } /* end of try */
  catch ( Throwable t )
  {
    throw t;
  }
  finally
  {
   try
   {
    if ( rset != null )
    {
     rset.close();
     rset = null;
    }
   }
   catch ( Throwable t )
   {
    t.printStackTrace();
   }

   if ( pstmt != null )
   {
    pstmt.close();
    pstmt = null;
   }
   
   try
   {
    if ( con != null )
    {
     con.disconnect();
     con = null;
     log("urlCon disconect");
    }
   }
   catch ( Throwable t )
   {
    t.printStackTrace();
   }
  }

  log("■ End");
 }

 public void run()
 {
  log("-run Begin-");

  try
  {
   //로그확인시 1.loadProp
   loadProp();

   //로그확인시 2.dbConnectZero
   dbConnectZero();

   //로그확인시 3.selZeroSend
   selZeroSend();
  }
  catch ( Throwable t )
  {
   t.printStackTrace();
  }
  finally
  {
   try
   {
    //로그확인시 4.dbDisconZero
    dbDisconZero();
   }
   catch ( Throwable t )
   {
    t.printStackTrace();
   }
  }

  log("-run End-");
 }

 public static void main(String[] arg)
 {
  ZeroManager.log("main Begin");

  ZeroManager zero = null;

  ZeroManager.log("usage : java zero.[PROPERTY_FILENAME=" +
           ZeroManager.PROP_FILE_NAME_STR + "]");

  if ( arg.length > 1 )
  {
   ZeroManager.log("main End");
   return;
  }
  else if ( arg.length == 1 )
  {
   ZeroManager.log("Property File : " + arg[0]);
   zero = new ZeroManager(arg[0]);
  }
  else if ( arg.length == 0 )
  {
   ZeroManager.log("Property File : " + ZeroManager.PROP_FILE_NAME_STR);
   zero = new ZeroManager();
  }

  while ( true )
  {
   zero.run();
   
   //10초 -> 10000
   try { Thread.sleep(10000); } catch ( Throwable t ) { t.printStackTrace(); }
  }

 }
 
}

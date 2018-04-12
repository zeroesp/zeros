package com.zero.zeros.javaTest.xmlParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class XmlPaserTest {
  
  public static void main(String[] args) {
    HttpURLConnection con = null;
    boolean urlCall = false;
    String dealYMD = "201801";
    String lawdCd = "41173";
    
    try {
      String name = URLEncoder.encode("test", "UTF-8");
      URL url = new URL("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade"
          + "?serviceKey=7QXgI7z5m4m%2FvHTi6o12s6W4T7FUnYh3UG3qEGoZRbCCNoSOt6XQRUfAbOFauAp%2BZ%2B4tJqsh7PvSm6vR6Q2g%3D%3D"//ae
          + "&DEAL_YMD=" + dealYMD
          + "&LAWD_CD=" + lawdCd);
      con = (HttpURLConnection) url.openConnection();
      
      //timeout설정 java 1.4버전 이하
      //System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
      //System.setProperty("sun.net.client.defaultReadTimeout", "5000");
      
      System.out.println("■ urlCon : " + con.getResponseMessage() + "," + con.getResponseCode());
  
      //요청한 URL에 대한 응답 내용 출력
      BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
      StringBuffer buffer = new StringBuffer();
      String line = null;
      if((line = reader.readLine()) != null) {
          buffer.append(line);
      }
      reader.close();
      //System.out.println("urlCon res : " + buffer.toString());
      
      
      //===== XML to CSV 파싱 Start 
      // throws IOException, SAXException, ParserConfigurationException
      /* CSV 구분자 */ 
      String split = "|"; 
      
      /* DOM Document 객체를 생성하는 단계 */ 
      DocumentBuilderFactory f = DocumentBuilderFactory.newInstance(); 
      DocumentBuilder parser = f.newDocumentBuilder(); 
      
      /* XML 파일 파싱하는 단계 */ 
      //Document xmlDoc = parser.parse("D:\\booklist.xml");
      Document xmlDoc = parser.parse(new InputSource(new java.io.StringReader(buffer.toString())));
      
      /* csv파일 첫 라인에 book의 속성 tag 명시 */ 
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\apart.txt"), "UTF-8")); 
      writer.write("거래금액|건축년도|년|법정동|아파트|월|일|전용면적|지번|지역코드|층 \r\n");
      
      /* 루트 엘리먼트 접근 */ 
      Element root = xmlDoc.getDocumentElement();

      /* book의 레코드 수만큼 속성값들 csv 파일에 출력 */
      System.out.println("totalCount : " + root.getElementsByTagName("totalCount").item(0).getTextContent());
      System.out.println("resultCode : " + root.getElementsByTagName("resultCode").item(0).getTextContent());
      System.out.println("resultMsg : " + root.getElementsByTagName("resultMsg").item(0).getTextContent());
      System.out.println("item Count : " + root.getElementsByTagName("item").getLength());
      for(int i = 0; i < root.getElementsByTagName("item").getLength(); i++) { 
        //Node rootNode = root.getElementsByTagName("item").item(i); 
        //Node node = ((Element)rootNode).getElementsByTagName("거래금액").item(0); 
        //String tagValue = node.getTextContent(); 
        //writer.write(tagValue + split); 
        String itemString = "";
        Node rootNode = root.getElementsByTagName("item").item(i); 
        itemString += ((Element)rootNode).getElementsByTagName("거래금액").item(0).getTextContent().trim() + split; 
        itemString += ((Element)rootNode).getElementsByTagName("건축년도").item(0).getTextContent().trim() + split;
        itemString += ((Element)rootNode).getElementsByTagName("년").item(0).getTextContent().trim() + split; 
        itemString += ((Element)rootNode).getElementsByTagName("법정동").item(0).getTextContent().trim() + split;
        itemString += ((Element)rootNode).getElementsByTagName("아파트").item(0).getTextContent().trim() + split; 
        itemString += ((Element)rootNode).getElementsByTagName("월").item(0).getTextContent().trim() + split;
        itemString += ((Element)rootNode).getElementsByTagName("일").item(0).getTextContent().trim() + split; 
        itemString += ((Element)rootNode).getElementsByTagName("전용면적").item(0).getTextContent().trim() + split;
        itemString += ((Element)rootNode).getElementsByTagName("지번").item(0).getTextContent().trim() + split;
        itemString += ((Element)rootNode).getElementsByTagName("지역코드").item(0).getTextContent().trim() + split; 
        itemString += ((Element)rootNode).getElementsByTagName("층").item(0).getTextContent().trim();
        writer.write(itemString + "\n"); 
        //writer.write(tagValue + "\n"); 
      } 
      writer.close();
      //===== XML to CSV 파싱 END

      
      //정상일 경우만 update
      if(con.getResponseCode() == 200){
          urlCall = true;
      }
      System.out.println("===== urlCall result : " + urlCall);
    }catch(Exception t) {
      t.printStackTrace();
    }
    
  }

}

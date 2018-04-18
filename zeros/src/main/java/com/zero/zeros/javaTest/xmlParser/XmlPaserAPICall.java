package com.zero.zeros.javaTest.xmlParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class XmlPaserAPICall {
  
  public static void main(String[] args) {
    HttpURLConnection con = null;
    String dealYMD = "201803";
    
    //String[] year = {"2015","2016","2017","2018"};
    //String[] month = {"01","02","03","04","05","06","07","08","09","10","11","12",};
    
    try {
      
      //for(String yearFor : year) {
      //  for(String monthFor : month) {
      //    dealYMD = yearFor+monthFor;
          
          System.out.println("job START : " + dealYMD + ", " + new Date(System.currentTimeMillis()));
    
          //시군구 코드 리스트 (파일위치:소스코드와 같은 경로)
          BufferedReader readerSigungu = new BufferedReader(new FileReader( XmlPaserAPICall.class.getResource("").getPath() + "sigungu.csv"));
          ArrayList<String> listSigungu = new ArrayList<String>();
          String lineSigungu = null;
          while((lineSigungu = readerSigungu.readLine()) != null) {
            listSigungu.add(lineSigungu);
          }
          readerSigungu.close();
          
          //for(String sigungu:listSigungu) {
          //  System.out.println("sigungu list :  " + sigungu);
          //}
    
          //csv파일 설정
          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\apart" + dealYMD + ".csv"), "UTF-8")); 
          
          for (String sigungu : listSigungu) {
            String sigunguCode = sigungu.split(",")[1].trim();
            String sigunguName = sigungu.split(",")[0].trim();
            String name = URLEncoder.encode("test", "UTF-8");
            URL url = new URL("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade"
                + "?serviceKey=7QXgI7z5m4m%2FvHTi6o12s6W4T7FUnYh3UG3qEGoZRbCCNoSOt6XQRUfAbOFauAp%2BZ%2B4tJqsh7PvSm6vR6Q2g%3D%3D" //ae
                + "&DEAL_YMD=" + dealYMD
                + "&LAWD_CD=" + sigunguCode);
            con = (HttpURLConnection) url.openConnection();
      
            //요청한 URL에 대한 응답 내용 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            //System.out.println("urlCon res : " + buffer.toString());
            
            System.out.println("■ urlCon : " + con.getResponseMessage() + ", " + con.getResponseCode() + 
                                  ", sigunguCode ; " + sigunguCode + ", sigunguName : " + sigunguName); // + ", result : " +  buffer.toString()
          
            //===== XML to CSV 파싱 Start 
            // throws IOException, SAXException, ParserConfigurationException
            //CSV 구분자
            String split = "|"; 
            
            //DOM Document 객체를 생성하는 단계
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder parser = f.newDocumentBuilder(); 
            
            //XML 파일 파싱하는 단계
            //Document xmlDoc = parser.parse("D:\\booklist.xml");
            Document xmlDoc = parser.parse(new InputSource(new java.io.StringReader(buffer.toString())));
            
            //루트 엘리먼트 접근 
            Element root = xmlDoc.getDocumentElement();
      
            //book의 레코드 수만큼 속성값들 csv 파일에 출력
            //System.out.println("totalCount : " + root.getElementsByTagName("totalCount").item(0).getTextContent());
            System.out.println("resultCode : " + root.getElementsByTagName("resultCode").item(0).getTextContent());
            //System.out.println("resultMsg : " + root.getElementsByTagName("resultMsg").item(0).getTextContent());
            //System.out.println("item Count : " + root.getElementsByTagName("item").getLength());
            for(int i = 0; i < root.getElementsByTagName("item").getLength(); i++) { 
              //Node rootNode = root.getElementsByTagName("item").item(i); 
              //Node node = ((Element)rootNode).getElementsByTagName("거래금액").item(0); 
              //String tagValue = node.getTextContent(); 
              //writer.write(tagValue + split); 
              String itemString = dealYMD + split + sigunguCode + split + sigunguName + split;
              Node rootNode = root.getElementsByTagName("item").item(i);
              if(((Element)rootNode).getElementsByTagName("법정동").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("법정동").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("아파트").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("아파트").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("전용면적").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("전용면적").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("거래금액").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("거래금액").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("건축년도").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("건축년도").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("년").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("년").item(0).getTextContent().replace(" ","") + split; 
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("월").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("월").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("일").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("일").item(0).getTextContent().replace(" ","") + split; 
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("지번").getLength() > 0) //지번이 없는 데이터 존재
                itemString += ((Element)rootNode).getElementsByTagName("지번").item(0).getTextContent().replace(" ","") + split;
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("지역코드").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("지역코드").item(0).getTextContent().replace(" ","") + split; 
              else  itemString += split;
              if(((Element)rootNode).getElementsByTagName("층").getLength() > 0) 
                itemString += ((Element)rootNode).getElementsByTagName("층").item(0).getTextContent().replace(" ","");
              else  itemString += split;
              writer.write(itemString + "\n"); 
              //writer.write(tagValue + "\n"); 
            } 
            //===== XML to CSV 파싱 END
          }
          writer.close();
          
          System.out.println("job END : " + dealYMD + ", " + new Date(System.currentTimeMillis()));
      //  }
      //}

    }catch(Exception t) {
      System.out.println("error : " + dealYMD + ", " + new Date(System.currentTimeMillis()));
      t.printStackTrace();
    }
    
  }

}

package com.zero.zeros.javaTest.parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionExample {

    public static void main(String[] args) {
        //정규표현식
        //String expr="^[-+]?(0|[1-9][0-9]*|0?[1-9]*\\.[0-9]+([eE][-+]?[0-9]+)?)$";    
        //String expr="^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+([eE][-+]?[0-9]+)?)?$"; //수정
        //String expr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$"; //수정
        String expr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?$"; //수정
        
        //검사할 문자열들
        String[] sources = { "0", "+0", "-0" ,"123.123","-1222223.1231111111111111","+","-","1234.00",
                             "00", "01234", "+01234", "-01234", "123401", "+1234", "-1234",
                             "-0.1234","+0.1234","00.1234","+00.1234","-00.1234",
                             "0.1234e567", "0.1234e+567","0.1234e-3","0.01234",
                             "102.10234e567", "102.10234e+567","102.10234e-3",
                             "102.10234e+56e+7","101e2","-101E-201",
                             "+0.1234e5", "-0.1234e-5",
                             "+abc", "-정규표현식","$@#$@!" };
        
        System.out.println("RegularExpression : ");
        System.out.println(expr);
        System.out.println("======================");
        
        /*
        for(String src : sources) {                    
            System.out.printf( (isNumber(src)?"통과":"---- 실패" )+" : [ %s ]%n",src);
        }    
        */
        
        System.out.println();
        System.out.println("==String 클래스의 matches메소드 이용.");
            
        for(String src : sources) {                    
            System.out.printf( (src.matches(expr)?"통과":"---- 실패" )+" : [ %s ]%n",src);
        }
    }        
    /**
     * 입력받은 문자열(str)이 숫자형태인지 판별. 
     * 숫자형태이면 true , 아니면 false  
     */
    public static boolean isNumber(String str){
        
        //String expr="^[-+]?(0|[1-9][0-9]*|0?[1-9]*\\.[0-9]+([eE][-+]?[0-9]+)?)$";
        //String expr="^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+([eE][-+]?[0-9]+)?)?$"; //수정
        //String expr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$"; //수정
        String expr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?$"; //수정
        Pattern pattern = Pattern.compile(expr);
        Matcher matcher = pattern.matcher(str);
        
        return matcher.matches(); //일치하면 true, 일치하지 않으면 false
    }    
}
//[출처] [자바] 정규표현식을 이용해서 숫자(정수/실수) 찾기, 플러스/마이너스 부호, 소수점 검색 ,지수 - Regex Signed Float|작성자 자바킹
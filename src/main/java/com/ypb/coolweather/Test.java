package com.ypb.coolweather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Test {

    private Test2 someName = null;

    Test(Test2 test2){
        this.someName = test2;
    }

    private void print3(){
        System.out.println(someName.getName());
    }


    private static class ListClass{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

    }
    private static class InnerTest{

        private List<ListClass> listClasses = null;
        private ListClass listClass ;


        InnerTest( ListClass listClass){
            listClass = listClass;
            if( listClass != null ){
                System.out.println("cons not null");
            }else{
                System.out.println("cons  null");
            }
            Print2();
        }

        InnerTest( List<ListClass> listClass){
            listClasses = listClass;
        }


        public void Print(){
            for(ListClass l:listClasses){
                System.out.println(l.getName());
            }
        }

        public void Print2(){
            if( listClass != null ){
                System.out.println(" not null");
            }else{
                System.out.println("  null");
            }
            System.out.println(listClass.getName());
        }
    }

    public static void main(String args[]){
        Test2 test2 = new Test2();
        test2.setName("aaaa");
        Test test = new Test(test2);
        test.print3();
        test2 = new Test2();
        test2.setName("bbbb");
        test.print3();
    }

    private void test2(){
        List<ListClass> listC = new ArrayList<ListClass>();
        ListClass la = new  ListClass();
        la.setName("a");
        listC.add(la);
        InnerTest innerTest = new InnerTest(listC);
        innerTest.Print();
        listC = getNewList();
        innerTest.Print();
    }

    public static List<ListClass> getNewList(){
        List<ListClass> ln = new ArrayList<ListClass>();
        ListClass lb = new  ListClass();
        lb.setName("b");
        ln.add(lb);
        return ln;
    }


    private void test1(){
        Map<String,String> mapWeather = new HashMap<String,String>();
        String s = "{\"weatherinfo\":{\"city\":\"北京\",\"cityid\":\"101010100\",\"temp1\":\"-2℃\",\"temp2\":\"16℃\",\"weather\":\"晴\",\"img1\":\"n0.gif\",\"img2\":\"d0.gif\",\"ptime\":\"18:00\"}}";
        System.out.println(s);
        String weather = (s.split(":\\{") )[1];
        String []ss =  weather.substring(0,weather.length()-2).split(",");
        for(String sss:ss){
            System.out.println(sss);
            String key = (sss.split(":"))[0];
            String value = (sss.split(":"))[1];
            mapWeather.put( Test.trim(key,"\""),Test.trim(value,"\"") );
        }

    }

    private static String trim(String orgin,String token){
        System.out.println("index=="+Integer.valueOf( orgin.indexOf(token) ).toString());

        while( orgin.indexOf(token) >= 0 ){
            orgin = orgin.substring(orgin.indexOf(token)+1,orgin.length() - 1);
            System.out.println(orgin);
        }
        return orgin;
    }
}

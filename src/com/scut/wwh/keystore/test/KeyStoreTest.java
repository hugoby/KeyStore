package com.scut.wwh.keystore.test;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by Administrator on 2016/4/3.
 * java实现日期格式转换。
 */
public class KeyStoreTest{
    public static void main(String args[])  throws Exception {
     //   java.sql.Date date=new java.sql.Date(new Date().getTime());
        Calendar ca=Calendar.getInstance();
        System.out.println(ca.getTime());
        String  str = "2016-01-24";
        Date currentTime=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("currenttime--"+currentTime);
        System.out.println("currentTime sdf--"+sdf.format(currentTime));
        System.out.println("currentTime sdf1--"+sdf1.format(currentTime));
        Date date=sdf.parse(str);
        System.out.println("date---"+date);
        System.out.println("date format1---"+sdf1.format(date));
    }
}

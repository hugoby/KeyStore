package com.scut.wwh.keystore.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/3/25.
 * * store keyString  using XML document
 * 利用properties的方法类将文件保存为xml的文档
 */
public class SaveToXML {

    public static void main(String args[]) throws Exception {
        Properties prop = new Properties();
        prop.setProperty("one-two", "buckle my shoe");
        prop.setProperty("three-four", "shut the door");
        prop.setProperty("five-six", "pick up sticks");
        prop.setProperty("seven-eight", "lay them straight");
        prop.setProperty("nine-ten", "a big, fat hen");
        FileOutputStream fos =
                new FileOutputStream("rhyme.xml");
        prop.storeToXML(fos, "Rhyme");
        fos.close();
        loadXML();
    }

    public static void loadXML()throws Exception {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream("rhyme.xml");
            prop.loadFromXML(fis);
            prop.list(System.out);
            System.out.println("\nThe foo property: " +
                    prop.getProperty("five-six"));
            fis.close();

    }
}

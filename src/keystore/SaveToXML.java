package keystore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/3/25.
 * * store keyString  using XML document
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
    }

    public static void loadXML()throws Exception {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream("rhyme.xml");
            prop.loadFromXML(fis);
            prop.list(System.out);
            System.out.println("\nThe foo property: " +
                    prop.getProperty("foo"));
            fis.close();

    }
}

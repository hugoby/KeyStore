package com.scut.wwh.keystore;

import com.scut.wwh.keystore.domain.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/31.
 */
public class RegisterHelper {
    /**
     * load local xml document
     * @return
     */
    public static Document load() {
        Document doc = null;
        try{
            File file=new File("users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            if(file.exists()){
                doc = dbBuilder.parse(file.getAbsoluteFile());
            }else{
                System.out.println("file don't exist！");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * save update to xml file
     * @param document
     */
    public static void saveToXmlFile(Document document){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            transformer.transform(new DOMSource(document), new StreamResult(new File(
                    "users.xml")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Judge xml file exist or not
     * @param fileName
     * @return flag
     */
    public boolean  ExistXmlFileOrNot(String fileName){
        boolean flag=false;
        File file=new File(fileName);
        if(file.exists())
            flag=true;
        return flag;
    }
    /**
     * If user is the first one to regieste，judge xml file exist or Not
     * @param user
     */
    public void createXmlFile(User user){
        try{
            //create root element
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root=document.createElement("users");
            root.setAttribute("name","user");
            //create children node
            Element user_1=document.createElement("user");
            user_1.setAttribute("id",new Date().toString());

            Element user_name_1=document.createElement("username");
            user_name_1.setTextContent(user.getUsername());
            user_1.appendChild(user_name_1);

            Element user_password_1=document.createElement("password");
            user_password_1.setTextContent(user.getPassword());
            user_1.appendChild(user_password_1);
            root.appendChild(user_1);
            document.appendChild(root);

            saveToXmlFile(document);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("xml 文件创建成功！");
    }

    /**
     * add new Node to  XML document
     * @param user
     */
    public void addXmlNode(User user){
        Document document=null;
        document=RegisterHelper.load();
        try{
            if(document!=null){
                //create new node
                Element user_1=document.createElement("user");
                user_1.setAttribute("id",new Date().toString());

                Element user_name_1=document.createElement("username");
                user_name_1.setTextContent(user.getUsername());
                user_1.appendChild(user_name_1);

                Element user_password_1=document.createElement("password");
                user_password_1.setTextContent(user.getPassword());
                user_1.appendChild(user_password_1);
                document.getDocumentElement().appendChild(user_1);

                //save update
                saveToXmlFile(document);
            }else{
                System.out.println("xml file read failed!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("add node success!");
    }

    /**
     * query
     * 根据用户名来判断文件中是否有相同的元素,如果有相同用户则返回true;
     * @param user
     */
    public  boolean JudgeNodeIsDuplicate(User user){
        boolean flag=false;
        Document doc=RegisterHelper.load();
        NodeList nodeList=doc.getElementsByTagName("username");
        for (int i=0;i<nodeList.getLength();i++){
            String name=nodeList.item(i).getTextContent();
            if(name.equals(user.getUsername())){
                flag=true;
                return flag;
            }
        }
        return flag;
    }
    /**
     * login validate
     * @param user
     * @return
     */
    public boolean loginValidate(User user){
        boolean flag=false;
        String username=null;
        String password=null;
        Document doc=RegisterHelper.load();
        NodeList nodeList=doc.getElementsByTagName("user");
        for (int i=0;i<nodeList.getLength();i++){
            NodeList subNode=nodeList.item(i).getChildNodes();
            for(int j=0;j<subNode.getLength();j++){
            //    System.out.println(subNode.item(j).getNodeName());
                if(subNode.item(j).getNodeName().equals("username")){
                    username=subNode.item(j).getTextContent();
                }
                if(subNode.item(j).getNodeName().equals("password")){
                    password=subNode.item(j).getTextContent();
                }
            }
            if(username.equals(user.getUsername())&&password.equals(user.getPassword())){
                flag=true;
            }

        }
        return flag;
    }

    }

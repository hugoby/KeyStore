package com.scut.wwh.keystore;

import com.scut.wwh.keystore.domain.KeyStore;
import com.scut.wwh.keystore.domain.User;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;



    /**
     * Created by Administrator on 2016/3/25.
     * @author by wwh
     */
  public class KeyStoreHelper {

    /**
     * Judge xml file exist or noteNa
     * @param fileName
     * filName is xml file
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
     * save update to xml file
     * @param document
     */
    public static void saveToXmlFile(Document document,String fileName){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            transformer.transform(new DOMSource(document), new StreamResult(new File(
                    fileName)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * load local xml document
     * @return
     */
    public static Document load() {
        Document doc = null;
        try{
            File file=new File("keys.xml");
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
           *注册用户的唯一性确保了生成密钥的唯一性，不需要检验keys.xml文件中，用户名是否相同
            *Generate KeyPair  by using ECC Algrithom
            */
    public KeyStore generateKeyInfo(User user){
        KeyPairGenerator kpg;
        KeyPair keyPair;
        KeyStore keyStore=null;
        KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
        try{
            Security.addProvider(new BouncyCastleProvider());
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            kpg= KeyPairGenerator.getInstance("ECIES", "BC");
            kpg.initialize(256);
            keyPair = kpg.generateKeyPair();
            ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
            String publicKeyStr=Base64.toBase64String(publicKey.getEncoded());
            String privateKeyStr=Base64.toBase64String(privateKey.getEncoded());
            String date=new Date().toString();
            //用管理员的公钥对上面的密钥进行加密
            String encryText=user.getUsername()+" "+publicKeyStr+" "+privateKeyStr+" "+date+" ";
            String verify=keyStoreHelper.encryption(keyStoreHelper.loadAdminPublicKey(),encryText);
            keyStore=new KeyStore(publicKeyStr,privateKeyStr,date,verify,user);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return keyStore;
    }

    /**
     * 获取管理员的公钥字符串进行加密
     * @param adminPublicKey
     * @param text
     */
    public String encryption(String adminPublicKey,String text){
             String encryStr=null;
        try{
            byte[] adminEccPublicKeyStr = (Base64.decode(adminPublicKey));//解码
            X509EncodedKeySpec X509PublicKeyObject = new X509EncodedKeySpec(adminEccPublicKeyStr);//生成X509EncodedKeySpec格式的密钥规�?
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");//获取密钥工厂对象
            ECPublicKey adminEccPublicKey= (ECPublicKey)keyFactory.generatePublic(X509PublicKeyObject);
            Cipher cipher = Cipher.getInstance("ECIES", "BC");//获取密码引擎对象
            cipher.init(Cipher.ENCRYPT_MODE, adminEccPublicKey);//初始化加密模式和公钥
            byte[] cipherText = cipher.doFinal(text.getBytes());//加密
            encryStr=Base64.toBase64String(cipherText);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("encryption--"+encryStr);
        return encryStr;
    }

    /**
     * 拿到管理员的公钥进行签名
     * @return
     */
    public   String  loadAdminPublicKey(){
         String publicKey=null;
         Document doc=KeyStoreHelper.load();
         if (doc!=null){
             NodeList keyNode=doc.getElementsByTagName("key");
             for (int i=0;i<keyNode.getLength();i++){
              //    System.out.println(keyNode.item(i).getNodeName());
                    NodeList subKeyNode=keyNode.item(i).getChildNodes();
                      for(int j=0;j<subKeyNode.getLength();j++){
                        //  System.out.println(subKeyNode.item(j).getNodeName());
                          if(subKeyNode.item(j).getNodeName().equals("username")&&subKeyNode.item(j).getTextContent().equals("admin"))
                          {
                              publicKey=subKeyNode.item(j).getNextSibling().getNextSibling().getTextContent();
                             // System.out.println(publicKey);
                          }
                      }
             }
        }else {
            System.out.println("file don’t exist！");
        }
         return publicKey;
    }
    /**
     * 将密钥信息保存到xml文件
     * @param keyStore
     */
    public Boolean   SaveKeyInfoToXmlFile(KeyStore keyStore){
        boolean symbol=false;
        Document document=null;
        String fileName="keys.xml";
        document=KeyStoreHelper.load();
        try{
            if (document!=null){
                Element keyNode=document.createElement("key");
                keyNode.setAttribute("id", keyStore.getUser().getUsername());

                Element keyNode_userName=document.createElement("username");
                keyNode_userName.setTextContent(keyStore.user.getUsername());
                keyNode.appendChild(keyNode_userName);

                Element keyNode_publicKey=document.createElement("publicKey");
                keyNode_publicKey.setTextContent(keyStore.getPublicKey());
                keyNode.appendChild(keyNode_publicKey);

                Element keyNode_privateKey=document.createElement("privateKey");
                keyNode_privateKey.setTextContent(keyStore.getPrivateKey());
                keyNode.appendChild(keyNode_privateKey);

                Element keyNode_verify=document.createElement("verify");
                keyNode_verify.setTextContent(keyStore.getVerify());
                keyNode.appendChild(keyNode_verify);

                Element keyNode_date=document.createElement("date");
                keyNode_date.setTextContent(keyStore.getDate());
                keyNode.appendChild(keyNode_date);

                document.getDocumentElement().appendChild(keyNode);
                saveToXmlFile(document,fileName);
                symbol=true;
            }
            else
            {
                System.out.println("add key Node ，xml file not find!");
            }
        }catch (Exception e){
            e.printStackTrace();
            symbol=false;
        }

      System.out.println("save success!");
      return  symbol;
    }

    /**
     * 导出用户密钥到xml文件，包含用户的公钥和私钥
     * @param keyStore
     */
    public boolean createKeyInfoXmlFile(KeyStore keyStore,String fileName){
        boolean flag=false;
        try{
            //create root element
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root=document.createElement("keys");
            root.setAttribute("name","keypair");
            //create children node
            Element keyNode=document.createElement("key");
            keyNode.setAttribute("id", keyStore.getUser().getUsername());

            Element keyNode_userName=document.createElement("username");
            keyNode_userName.setTextContent(keyStore.user.getUsername());
            keyNode.appendChild(keyNode_userName);

            Element keyNode_publicKey=document.createElement("publicKey");
            keyNode_publicKey.setTextContent(keyStore.getPublicKey());
            keyNode.appendChild(keyNode_publicKey);

            Element keyNode_privateKey=document.createElement("privateKey");
            keyNode_privateKey.setTextContent(keyStore.getPrivateKey());
            keyNode.appendChild(keyNode_privateKey);

            Element keyNode_verify=document.createElement("verify");
            keyNode_verify.setTextContent(keyStore.getVerify());
            keyNode.appendChild(keyNode_verify);

            Element keyNode_date=document.createElement("date");
            keyNode_date.setTextContent(keyStore.getDate());
            keyNode.appendChild(keyNode_date);

            root.appendChild(keyNode);
            document.appendChild(root);
            saveToXmlFile(document,fileName);
            flag=true;
        }catch (Exception e){
            flag=false;
            e.printStackTrace();
        }
        System.out.println("xml 文件创建成功！");
        return flag;
    }

        /**
         * 导出用户公钥到xml文件，包含用户的公钥和私钥
         * @param keyStore
         */
        public boolean createPublicKeyInfoXmlFile(KeyStore keyStore,String fileName){
            boolean flag=false;
            try{
                //create root element
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element root=document.createElement("keys");
                root.setAttribute("name","keypair");
                //create children node
                Element keyNode=document.createElement("key");
                keyNode.setAttribute("id", keyStore.getUser().getUsername());

                Element keyNode_userName=document.createElement("username");
                keyNode_userName.setTextContent(keyStore.user.getUsername());
                keyNode.appendChild(keyNode_userName);

                Element keyNode_publicKey=document.createElement("publicKey");
                keyNode_publicKey.setTextContent(keyStore.getPublicKey());
                keyNode.appendChild(keyNode_publicKey);

                Element keyNode_verify=document.createElement("verify");
                keyNode_verify.setTextContent(keyStore.getVerify());
                keyNode.appendChild(keyNode_verify);

                Element keyNode_date=document.createElement("date");
                keyNode_date.setTextContent(keyStore.getDate());
                keyNode.appendChild(keyNode_date);

                root.appendChild(keyNode);
                document.appendChild(root);
                saveToXmlFile(document,fileName);
                flag=true;
            }catch (Exception e){
                flag=false;
                e.printStackTrace();
            }
            System.out.println("xml 文件创建成功！");
            return flag;
        }

    /**
     * 返回公钥的信息
     * 根据用户姓名查询用户的公钥信息
     * @param user
     */
    public String queryUserPublicKeyInfo(User user){
        String publicKeyInfo="";
        Document doc=KeyStoreHelper.load();
        if (doc!=null){
            NodeList nodeList=doc.getElementsByTagName("key");
            for (int i=0;i<nodeList.getLength();i++){
               //      System.out.println(nodeList.item(i).getNodeName());
                NodeList subNode=nodeList.item(i).getChildNodes();
                for (int j=0;j<subNode.getLength();j++){
              //       System.out.println(subNode.item(j).getNodeName());
                    if(subNode.item(j).getNodeName().equals("username")&&subNode.item(j).getTextContent().equals(user.getUsername())){
                           publicKeyInfo=subNode.item(j).getNextSibling().getNextSibling().getTextContent();
                        System.out.println(publicKeyInfo);
                    }
                }
            }

        }else
        {
            System.out.println("file can't find! ");
        }
        return publicKeyInfo;

    }
        /**
         * 返回私钥的信息
         * 根据用户姓名查询用户的公钥信息
         * @param user
         */
        public String queryUserPrivateKeyInfo(User user){
            String privateKeyInfo="";
            Document doc=KeyStoreHelper.load();
            if (doc!=null){
                NodeList nodeList=doc.getElementsByTagName("key");
                for (int i=0;i<nodeList.getLength();i++){
                    //      System.out.println(nodeList.item(i).getNodeName());
                    NodeList subNode=nodeList.item(i).getChildNodes();
                    for (int j=0;j<subNode.getLength();j++){
                        //       System.out.println(subNode.item(j).getNodeName());
                        if(subNode.item(j).getNodeName().equals("username")&&subNode.item(j).getTextContent().equals(user.getUsername())){
                            privateKeyInfo=subNode.item(j).getNextSibling().getNextSibling().getNextSibling().getNextSibling().getTextContent();
                            System.out.println(privateKeyInfo);
                        }
                    }
                }

            }else
            {
                System.out.println("file can't find! ");
            }
            return privateKeyInfo;

        }
    /**
     *更改用户的密钥信息
     * @param user
     */
    public void updateUserKeyInfo(User user){

    }

    /**
     * 删除用户的密钥信息
     * @param user
     */
    public  void delteUserKeyInfo(User user){

    }

         public  static  void main(String args[]){
             //String fileName="key.xml";
             KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
//             User user=new User("admin","admin");
//             KeyStore keyStore=keyStoreHelper.generateKey(user);
//             keyStoreHelper.createKeyInfoXmlFile(keyStore);
//             keyStoreHelper.loadAdminPublicKey();
//             keyStoreHelper.encryption(keyStoreHelper.loadAdminPublicKey(),"good good study day day up!");
              User user=new User("wwh","wwh");
//                 KeyStore keyStore=keyStoreHelper.generateKeyInfo(user);
//                 keyStoreHelper.SaveKeyInfoToXmlFile(keyStore);
//             keyStoreHelper.queryUserKeyInfo(user);


          // System.out.println("test");
         }
}

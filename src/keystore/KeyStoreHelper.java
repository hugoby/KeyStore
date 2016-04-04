package keystore;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/3/25.
 * @author by wwh
 */
public class KeyStoreHelper {

        /*
         *Generate KeyPair  by using ECC Algrithom
         */
         public  KeyPair generateKey(){
             KeyPairGenerator kpg=null;
             KeyPair keyPair=null;
             try{
                 Security.addProvider(new BouncyCastleProvider());
                 Security.insertProviderAt(new BouncyCastleProvider(), 1);
                 kpg= KeyPairGenerator.getInstance("ECIES", "BC");
                 kpg.initialize(256);
                 keyPair = kpg.generateKeyPair();
             }catch(Exception ex){
                 ex.printStackTrace();
             }
           return keyPair;
         }

        /*
         Transoform keyPair to String
         */
        public HashMap<String,String> tranToString(KeyPair keyPair){
            HashMap<String, String> keyMap = new HashMap<String, String>();
            try{
                ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
                ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
//                System.out.println("pub------"+Base64.toBase64String(publicKey.getEncoded()));
//                System.out.println("pri------"+Base64.toBase64String(privateKey.getEncoded()));
                keyMap.put("publicKey", Base64.toBase64String(publicKey.getEncoded()));
                keyMap.put("privateKey", Base64.toBase64String(privateKey.getEncoded()));
                return keyMap;
            }catch (Exception ex){
              ex.printStackTrace();
            }
            return keyMap;
        }

            /**
             * store KeyPair FileName keyPair.properties
             * save publicKey and privateKey to File Properties
             * @param publicKey
             * @param privateKey
             */
         public  boolean saveKey(String publicKey,String privateKey,String userName){
                 boolean flag=false;
                 String fileName=null;
                 PropertiesConfiguration keyProperties=null;
                 File file=null;
                try{
                    fileName=userName+".properties";
                    file=new File(fileName);
                    if(file.exists()){
                         System.out.println("file exists");
                 }
                 //save public key
                  file.createNewFile();
                    System.out.println("File init success!");
                 keyProperties = new PropertiesConfiguration(file.getAbsoluteFile());
                 keyProperties.setProperty("publicKey",publicKey);
                 keyProperties.setProperty("privateKey", privateKey);
                 keyProperties.save();
                    flag=true;
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            // keyProperties.clear();
             System.out.println("Save Key Success");
             return flag;
         }
            /**
             * fileName the path keyPair store
             * @param fileName
             */
            public  void  loadKey(String fileName){
                PropertiesConfiguration keyProperties=null;
                String privateKey=null;
                String publicKey=null;
                try{
                    keyProperties=new PropertiesConfiguration(fileName);

                    if(keyProperties!=null) {
                        publicKey=keyProperties.getString("publicKey");
                         privateKey=keyProperties.getString("privateKey");
                    }else {
                        System.out.println("KeyProperties file is null");
                    }
                    //retrieve ECCpublic key
                    byte[] euk = (Base64.decode(publicKey));
                    X509EncodedKeySpec X509PublicKeyObject = new X509EncodedKeySpec(euk);
                    KeyFactory publicKeyFactory = KeyFactory.getInstance("ECDH", "BC");
                    ECPublicKey ECPublicKey=(ECPublicKey)publicKeyFactory.generatePublic(X509PublicKeyObject);

                   //retrieve ECCprivateKey
                    byte[] erk= (Base64.decode(privateKey));
                    X509EncodedKeySpec X509PrivateKeyObject = new X509EncodedKeySpec(erk);
                    KeyFactory privateKeyFactory = KeyFactory.getInstance("ECDH", "BC");
                    ECPublicKey ECPrivateKey=(ECPublicKey)privateKeyFactory.generatePublic(X509PrivateKeyObject);

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

          public PropertiesConfiguration searchKeyFileByName(String username){
              //判断密钥文件是否存在如果存在返回当前文件对象
              PropertiesConfiguration keyProperties=null;
              String fileName=null;
              try{
                  fileName=username+".properties";
                  File file=new File(fileName);
                  if(file.exists()){
                      //windows底下的文件名不区分大小写,参数的文件名是文件名，判断文件名的大小写的问题
                      //System.out.println("fileName"+file.getName());
                      keyProperties=new PropertiesConfiguration(fileName);
                     //System.out.println(keyProperties.getString("publicKey"));
                  }else{
                      System.out.println("文件不存在");
                     // System.out.println(keyProperties);
                  }
              }catch(Exception e){
                  e.printStackTrace();
              }
            return  keyProperties;
          }


}

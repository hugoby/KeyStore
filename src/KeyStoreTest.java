import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.HashMap;

/**
 * Package_name PACKAGE_NAME
 * Project_name KeyStore
 * Created by lenovo on 2015/12/25 15:48
 */
public class KeyStoreTest {

    private static HashMap<String, String> hashMap = new HashMap<String, String>();

    public String setKey(String user) throws NoSuchProviderException, NoSuchAlgorithmException {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);

            Security.addProvider(new BouncyCastleProvider());
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "BC");
            kpg.initialize(256);
            KeyPair keyPair = kpg.generateKeyPair();

            ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();

            CertAndKeyGen gen = new CertAndKeyGen("RSA", "SHA1WithRSA");
            gen.generate(1024);
            X509Certificate cert = gen.getSelfCertificate(new X500Name("CN=ROOT"), (long) 365 * 24 * 3600);
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setKeyEntry("private", privateKey, "c".toCharArray(), chain);
            String name = user + ".p12";
            keyStore.store(new FileOutputStream(name), "password".toCharArray());
            hashMap.put(user, name);
            return Base64.toBase64String(privateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchKey(String user) throws NoSuchProviderException, NoSuchAlgorithmException {
        if (hashMap.containsKey(user)) {
            String name = hashMap.get(user);
            //·µ»ØÒ»¸öË½Ô¿
            try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(new FileInputStream(name), "password".toCharArray());

                ECPrivateKey privateKey = (ECPrivateKey) keyStore.getKey("private", "password".toCharArray());
                return Base64.toBase64String(privateKey.getEncoded());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            return setKey(user);
        }
        return null;
    }
}
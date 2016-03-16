import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Package_name PACKAGE_NAME
 * Project_name KeyStore
 * Created by lenovo on 2016/3/10 20:46
 */
public class Test {
    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyStoreTest key_store = new KeyStoreTest();
        String user="Hugo";
        System.out.println(key_store.searchKey(user));
    }
}

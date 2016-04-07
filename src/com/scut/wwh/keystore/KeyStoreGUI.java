package com.scut.wwh.keystore;

import com.scut.wwh.keystore.domain.KeyStore;
import com.scut.wwh.keystore.domain.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


/**
 * Created by Administrator on 2016/3/28.
 * interface with user
 */
public class KeyStoreGUI extends JFrame implements ActionListener{
   // private JLabel label1,label2;
    public JButton addButton,queryButton,exportKeyButton,exportPubKeyButton;
    public JButton loginButton,registerButton;
    public JTextField username;
    public JPasswordField password;
    public JLabel nameLabel;
    public JLabel pwdLabel;
    public JLabel KeyInfoLabel;
    public JTextArea keyArea;
    public JPanel panel;
    public JScrollPane scrollPane;
    public String nametext=null;
    public String pwdtext=null;
    boolean label=false;

    public KeyStoreGUI(){
        this.setTitle("KeyStoreGUI");
        this.setBounds(100,140,340,500);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        panel=new JPanel();
        Border border=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder tBorder=BorderFactory.createTitledBorder(border,"KeyStoreManage",TitledBorder.CENTER,TitledBorder.TOP);

        panel.setBorder(tBorder);
        panel.setLayout(null);
        this.add(panel);

        //username
        nameLabel=new JLabel("username:");
        nameLabel.setBounds(50,50,70,25);
        panel.add(nameLabel);

        username=new JTextField();
        username.setBounds(130,50,120,22);
        panel.add(username);


        //password
         pwdLabel=new JLabel("password:");
         pwdLabel.setBounds(50,75,70,25);
         panel.add(pwdLabel);

        password=new JPasswordField();
        password.setBounds(130,75,120,22);
        panel.add(password);

        //Button
        addButton = new JButton("生成密钥");
        addButton.setBounds(50,180,100,22);
        panel.add(addButton);
        addButton.addActionListener(this);

        queryButton= new JButton("查询密钥");
        queryButton.setBounds(150,180,100,22);
        panel.add(queryButton);
        queryButton.addActionListener(this);


        exportKeyButton=new JButton("导出密钥");
        exportKeyButton.setBounds(50,225,100,22);
        panel.add(exportKeyButton);
        exportKeyButton.addActionListener(this);

        exportPubKeyButton=new JButton("导出公钥");
        exportPubKeyButton.setBounds(150,225,100,22);
        panel.add(exportPubKeyButton);
        exportPubKeyButton.addActionListener(this);


        //login
        loginButton=new JButton("login");
        loginButton.setBounds(70,110,83,22);
        loginButton.setBorder(BorderFactory.createEtchedBorder());
        panel.add(loginButton);
        loginButton.addActionListener(this);

        //RegisterGUI
        registerButton=new JButton("register");
        registerButton.setBounds(195,110,83,22);
        panel.add(registerButton);
        registerButton.addActionListener(this);

        KeyInfoLabel=new JLabel("密钥信息");
        KeyInfoLabel.setBounds(2,270,70,25);
        panel.add(KeyInfoLabel);

        keyArea=new JTextArea();
        keyArea.setEditable(false);
        scrollPane=new JScrollPane(keyArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(2,300,350,150);
        panel.add(scrollPane);
        panel.updateUI();

    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource()==loginButton){
            RegisterHelper registerHelper=new RegisterHelper();
            nametext=username.getText();
            pwdtext=new String(password.getPassword());
            System.out.println(nametext+"-----pwd:"+pwdtext);
            User user=new User(nametext,pwdtext);
            if(registerHelper.loginValidate(user)){
                label=true;
                JOptionPane.showMessageDialog(this,"恭喜登录成功！");
                System.out.println(user);
                Label messageLabel=new Label(user.getUsername()+"登陆成功!");
                messageLabel.setBounds(100,25,100,25);
                messageLabel.setVisible(true);
                panel.add(messageLabel);
                username.setText("");
                password.setText("");
            }else{
                JOptionPane.showMessageDialog(this,"用户名或密码错误!");
            }
        }
        if(e.getSource()==registerButton){
              //注册页面
               new RegisterGUI();
        }
        if(e.getSource()==addButton){
            //根据登录的用户生成密钥,将密钥信息保存在密钥文件中。
            System.out.println(nametext);
            System.out.println(pwdtext);
            KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
            String fileName="keys.xml";
            KeyStore keyStore;
            if(nametext==null||pwdtext==null||label==false){
                JOptionPane.showMessageDialog(this,"请先登录");
            }
            if(nametext!=null&&pwdtext!=null&&label==true){
                //生成用户的密钥
                User user=new User(nametext,pwdtext);
                if (keyStoreHelper.ExistXmlFileOrNot(fileName)){
                    if(keyStoreHelper.queryUserPublicKeyInfo(user).equals("")) {
                        keyStore = keyStoreHelper.generateKeyInfo(user);
                        if (keyStore != null) {
                            JOptionPane.showMessageDialog(this, "密钥生成成功！");
                            keyArea.setText("公钥信息：" + keyStore.getPublicKey());
                            int flag = JOptionPane.showConfirmDialog(null, "是否需要保存密钥到文件", "密钥保存", JOptionPane.YES_NO_OPTION);
                            if (flag == 0) {
                                if (keyStoreHelper.SaveKeyInfoToXmlFile(keyStore)) {
                                    JOptionPane.showMessageDialog(this, "密钥保存成功！");
                                    keyArea.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(this, "密钥保存失败！");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "密钥生成失败！");
                            System.out.println("密钥生成失败");
                        }
                    }else {
                        JOptionPane.showMessageDialog(this, "用户密钥信息已经存在!");
                    }
                }
            }
        }
        if (e.getSource()==queryButton){
            //查询用户根据登录的用户来进行查询
            System.out.println(label);
            String publicKeyStr="";
            KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
            if(label==false){
                JOptionPane.showMessageDialog(this,"请先登录!");
            }else {
                System.out.println(nametext+" "+pwdtext);
                User user=new User(nametext,pwdtext);

                publicKeyStr=keyStoreHelper.queryUserPublicKeyInfo(user);
                System.out.println("pubkey"+publicKeyStr);
                if (publicKeyStr.equals("")){
                    JOptionPane.showMessageDialog(this,"找不到该用户的密钥文件!");
                }else{
                    keyArea.setText("公钥信息：" + publicKeyStr);
                }
            }
        }
        if (e.getSource()==exportKeyButton)
        {
            KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
            if(label==false){
                JOptionPane.showMessageDialog(this,"请先登录!");
            }else{
                User user=new User(nametext,pwdtext);
                String publicKeyStr=keyStoreHelper.queryUserPublicKeyInfo(user);
                String privateKeyStr=keyStoreHelper.queryUserPrivateKeyInfo(user);
                String date=new Date().toString();
                String encryText=user.getUsername()+publicKeyStr+privateKeyStr+date;
                String verify=keyStoreHelper.encryption(keyStoreHelper.loadAdminPublicKey(),encryText);
                KeyStore keyStore=new KeyStore(publicKeyStr,privateKeyStr,date,verify,user);
                String fileName=user.getUsername()+"_key.xml";
                if(keyStoreHelper.createKeyInfoXmlFile(keyStore,fileName)){
                    JOptionPane.showMessageDialog(this,"密钥文件导出成功!");
                }else{
                    JOptionPane.showMessageDialog(this,"密钥文件导出失败!");
                }
            }

        }
        if(e.getSource()==exportPubKeyButton){
            KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
            if(label==false){
                JOptionPane.showMessageDialog(this,"请先登录!");
            }else{
                User user=new User(nametext,pwdtext);
                String publicKeyStr=keyStoreHelper.queryUserPublicKeyInfo(user);
                String privateKeyStr=keyStoreHelper.queryUserPrivateKeyInfo(user);
                String date=new Date().toString();
                String encryText=user.getUsername()+publicKeyStr+privateKeyStr+date;
                String verify=keyStoreHelper.encryption(keyStoreHelper.loadAdminPublicKey(),encryText);
                KeyStore keyStore=new KeyStore(publicKeyStr,privateKeyStr,date,verify,user);
                String fileName=user.getUsername()+"_public_key.xml";
                if(keyStoreHelper.createPublicKeyInfoXmlFile(keyStore,fileName)){
                    JOptionPane.showMessageDialog(this,"用户公钥文件导出成功!");
                }else{
                    JOptionPane.showMessageDialog(this,"用户公钥文件导出失败!");
                }
            }

        }
    }

      public static void main(String args[]){
           new KeyStoreGUI();
      }

}

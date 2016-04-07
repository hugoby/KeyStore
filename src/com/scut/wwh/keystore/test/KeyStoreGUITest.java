package com.scut.wwh.keystore.test;

import org.apache.commons.configuration.PropertiesConfiguration;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.security.KeyPair;
import java.util.HashMap;


/**
 * Created by Administrator on 2016/3/28.
 * interface with user
 */
public class KeyStoreGUITest extends JFrame implements ActionListener{
   // private JLabel label1,label2;
    private JButton addButton,queryButton,updateButton,deleteButton;
    private JButton loginButton,registerButton;
    public JTextField username;
    public JPasswordField password;
    public JLabel nameLabel;
    public JLabel pwdLabel;
    public JLabel KeyInfoLabel;
    private JTextArea keyArea;
    private JPanel panel;
    private JScrollPane scrollPane;

    public KeyStoreGUITest(){
        this.setTitle("KeyStoreGUITest");
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

        //login
        loginButton=new JButton("login");
        loginButton.setBounds(70,110,83,22);
        loginButton.setBorder(BorderFactory.createEtchedBorder());
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To Do login
            }
        });

        //RegisterGUI
        registerButton=new JButton("RegisterGUI");
        registerButton.setBounds(195,110,83,22);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //To Do RegisterGUI
            }
        });

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
        String text_username;
        PropertiesConfiguration keyProperties;
        KeyPair keyPair;
        HashMap<String,String> keyMap;

        //generate KeyPair
        if (e.getSource()==addButton){
            text_username=username.getText();
            //System.out.println("username--"+text_username);
            if(text_username.isEmpty()){
                //提示为空，请重新输入
                //JOptionPane.showMessageDialog(this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(this,"用户名为空，请输入用户名!");
            }else{
                //此时根据用户输入的名字进行密钥文件的生成，还需要判断该用户是否已经存在密钥文件，生成密钥文件，提示生成功，并提示用户进行保存
                KeyStoreHelperTest keyStoreHelperTest =new KeyStoreHelperTest();
                keyPair= keyStoreHelperTest.generateKey();
                keyMap= keyStoreHelperTest.tranToString(keyPair);
                if(keyMap.isEmpty()){
                    JOptionPane.showMessageDialog(this,"密钥生成失败，请重新生成");
                }else{
                    JOptionPane.showMessageDialog(this,"密钥生成成功！");
                    keyArea.setText("公钥信息：" + keyMap.get("publicKey") + "\n" +
                            "私钥信息：" + keyMap.get("privateKey"));
                   int flag=JOptionPane.showConfirmDialog(null, "是否需要保存密钥到文件", "密钥保存", JOptionPane.YES_NO_OPTION);
                    if(flag==0){
                       boolean symbol= keyStoreHelperTest.saveKey(keyMap.get("publicKey"),keyMap.get("privateKey"),text_username);
                        if(symbol){
                           JOptionPane.showMessageDialog(this,"密钥保存成功！");
                        }else{
                            JOptionPane.showMessageDialog(this,"密钥保存失败！");
                        }
                    }
                }
            }
            System.out.println("button1");
        }
        // Inquiry 密钥
        if (e.getSource()==queryButton){
            text_username=username.getText();
            //System.out.println("username--"+text_username);
            if(text_username.isEmpty()){
                //提示为空，请重新输入
                //JOptionPane.showMessageDialog(this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(this,"用户名为空，请输入用户名!");
            }else{
                try{
                    //1.从默认文件路径下进行文件的查找如果存在，则将文件信息读取出来显示到TextArea
                    //2.如果密钥不存在则提示用户进行输入用户名，重新生成密钥对
                    KeyStoreHelperTest keyStoreHelperTest =new KeyStoreHelperTest();
                    keyProperties= keyStoreHelperTest.searchKeyFileByName(text_username);
                    if(keyProperties==null){
                        JOptionPane.showMessageDialog(this,"不存在与此用户名相匹配的密钥文件!");
                    }else {
                        keyArea.setText("公钥信息：" + keyProperties.getString("publicKey") + "\n" +
                                "私钥信息：" + keyProperties.getString("privateKey"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            System.out.println("button2");
        }


    }

      public static void main(String args[]){
           new KeyStoreGUITest();

      }

}

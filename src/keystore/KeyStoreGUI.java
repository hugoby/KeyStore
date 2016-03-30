package keystore;

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
public class KeyStoreGUI extends JFrame implements ActionListener{
   // private JLabel label1,label2;
    private JButton button1,button2;
    public JTextField username;
    public JLabel nameLabel;
    public JLabel KeyInfoLabel;
    private JTextArea keyArea;
    private JPanel panel;
    private JScrollPane scrollPane;

//    public  void flush(){
//        panel.removeAll();
//        this.add(panel);
//        panel.add(button1);
//        panel.add(button2);
//        panel.add(username);
//        panel.add(nameLabel);
//        panel.add(KeyInfoLabel);
//        panel.add(keyArea);
//        panel.add(scrollPane);
//        panel.updateUI();
//    }
    public  KeyStoreGUI (){
        this.setTitle("KeyStoreGUI");
        this.setBounds(100,140,340,400);
        setResizable(false);
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


        button1 = new JButton("生成密钥");
        button1.setBounds(50,150,100,22);
        panel.add(button1);
        button1.addActionListener(this);

        button2= new JButton("查询密钥");
        button2.setBounds(150,150,100,22);
        panel.add(button2);
        button2.addActionListener(this);

        KeyInfoLabel=new JLabel("密钥信息");
        KeyInfoLabel.setBounds(2,170,70,25);
        panel.add(KeyInfoLabel);

        keyArea=new JTextArea();
        keyArea.setEditable(false);
        scrollPane=new JScrollPane(keyArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(2,200,350,150);
        panel.add(scrollPane);
        panel.updateUI();

    }
    public void actionPerformed(ActionEvent e){
        String text_username=null;
        PropertiesConfiguration keyProperties=null;
        KeyPair keyPair=null;
        HashMap<String,String> keyMap=new HashMap<String, String>();
        //generate KeyPair
        if (e.getSource()==button1){
            text_username=username.getText();
            //System.out.println("username--"+text_username);
            if(text_username.isEmpty()){
                //提示为空，请重新输入
                //JOptionPane.showMessageDialog(this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(this,"用户名为空，请输入用户名!");
            }else{
                //此时根据用户输入的名字进行密钥文件的生成，还需要判断该用户是否已经存在密钥文件，生成密钥文件，提示生成功，并提示用户进行保存
                KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
                keyPair=keyStoreHelper.generateKey();
                keyMap=keyStoreHelper.tranToString(keyPair);
                if(keyMap.isEmpty()){
                    JOptionPane.showMessageDialog(this,"密钥生成失败，请重新生成");
                }else{
                    JOptionPane.showMessageDialog(this,"密钥生成成功！");
                    keyArea.setText("公钥信息：" + keyMap.get("publicKey") + "\n" +
                            "私钥信息：" + keyMap.get("privateKey"));
                   int flag=JOptionPane.showConfirmDialog(null, "是否需要保存密钥到文件", "密钥保存", JOptionPane.YES_NO_OPTION);
                    if(flag==0){
                       boolean symbol= keyStoreHelper.saveKey(keyMap.get("publicKey"),keyMap.get("privateKey"),text_username);
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
        if (e.getSource()==button2){
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
                    KeyStoreHelper keyStoreHelper=new KeyStoreHelper();
                    keyProperties=keyStoreHelper.searchKeyFileByName(text_username);
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
           new KeyStoreGUI();

      }

}

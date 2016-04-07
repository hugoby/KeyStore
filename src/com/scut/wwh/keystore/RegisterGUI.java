package com.scut.wwh.keystore;

import com.scut.wwh.keystore.domain.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrator on 2016/3/31.
 *   user   RegisterGUI GUI
 */
public class RegisterGUI extends JPanel {
    static final int WIDTH=440;
    static  final int HEIGHT=220;
    JFrame registerFrame;

//  //  public static  void main(String  args[]){
//        new RegisterGUI();
//    }

    public void add(Component c,GridBagConstraints constraints,int x,int y,int w,int h){
        constraints.gridx=x;
        constraints.gridy=y;
        constraints.gridwidth=w;
        constraints.gridheight=h;
        add(c,constraints);
    }
    RegisterGUI(){
        registerFrame=new JFrame("KeyStore用户注册");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridBagLayout=new GridBagLayout();
        setLayout(gridBagLayout);
        registerFrame.add(this,BorderLayout.CENTER);
        registerFrame.setSize(WIDTH,HEIGHT);
        Toolkit kit=Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        int width=screenSize.width;
        int height=screenSize.height;

        int x=(width-WIDTH)/2;
        int y=(height-HEIGHT)/2;
        registerFrame.setLocation(x,y);

        final JButton register= new JButton("Register");
        JButton cancel=new JButton("cancel");
        JLabel title=new JLabel("用户注册");
        JLabel username=new JLabel("用户名:");
        final JLabel password=new JLabel("密码:");
        final JTextField nameinput=new JTextField(15);
        final JTextField passwordinput=new JTextField(15);

        GridBagConstraints constraints=new GridBagConstraints();
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor=GridBagConstraints.CENTER;
        constraints.weightx=30;
        constraints.weighty=30;
        add(title,constraints,0,0,5,1);
        add(username,constraints,0,1,1,1);
        add(password,constraints,0,2,1,1);
        add(nameinput,constraints,2,1,1,1);
        add(passwordinput,constraints,2,2,1,1);
        add(register,constraints,1,3,1,1);
        add(cancel,constraints,2,3,1,1);
        registerFrame.setResizable(true);
        registerFrame.setVisible(true);
        registerFrame.setResizable(false);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //实现用户密钥信息的保存，判断该用户是否存在，若存在则不允许注册 写入xml文档进行保存
                String nametext=nameinput.getText();
                String passtext=passwordinput.getText();
                String fileName="users.xml";
                RegisterHelper registerHelper=new RegisterHelper();
                if(nametext.isEmpty()||passtext.isEmpty()){
                    //用户名或密码为空，提示用户名或密码为空，重新输入
                    JOptionPane.showMessageDialog(registerFrame,"用户名或密码为空，请输入用户名!");
                }else{
                     User user=new User(nametext,passtext);
                    if(registerHelper.ExistXmlFileOrNot(fileName)){
                        //xml file exist 将用户写入用户文件,判断是否用户重名
                       if (!registerHelper.JudgeNodeIsDuplicate(user)){
                           //没有相同用户，则将信息写入xml文件
                            registerHelper.addXmlNode(user);
                            JOptionPane.showMessageDialog(registerFrame,"注册成功！");
                            nameinput.setText("");
                            passwordinput.setText("");
                       }else{
                           JOptionPane.showMessageDialog(registerFrame,"用户名重复!");
                           nameinput.setText("");
                       }
                    }else{
                        registerHelper.createXmlFile(user);
                    }

                }
            }
        });

      cancel.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              registerFrame.dispose();
          }
      });
    }
}

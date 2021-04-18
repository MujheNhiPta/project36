package client;

import client.admin.*;
import client.student.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LoginUIPage {
    private JFrame loginFrame;
    private JLabel status;
    final private Socket s;
    public DataOutputStream dos;
    public DataInputStream dis;

    public LoginUIPage(Socket s){
        this.s= s;
        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginPage();
    }
    private void signUpPage(String tName, String tPass){
        JFrame jf= new JFrame();
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginFrame.setVisible(true);
            }
        });
        jf.setSize(300, 200);
        JLabel hName = new JLabel("Hostel Name: ");
        hName.setBounds(20, 75, 70, 30);
        jf.add(hName);
        JTextField jtf= new JTextField("");
        jtf.setBounds(100, 80, 150, 20);
        jf.add(jtf);
        JButton create = new JButton("Create");
        create.setBounds(130, 125, 80, 20);
        jf.add(create);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str= "";
                try{
                    dos.writeUTF("0");
                    dos.flush();
                    dos.writeUTF(jtf.getText());
                    dos.flush();
                    dos.writeUTF(tName);
                    dos.flush();
                    dos.writeUTF(tPass);
                    dos.flush();
                    str= dis.readUTF();
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
                if(str.equals("0")){
                    status.setText("MessManager already present");
                }
                else if(str.equals("1")){
                    status.setText("userName Already Present");
                }
                else{
                    status.setText("Account created Successfully");
                }
                jf.dispose();
                loginFrame.setVisible(true);
            }
        });
        jf.setLayout(null);
        jf.setVisible(true);
    }
    private void loginPage(){
        loginFrame= new JFrame("SignUP/IN");
        loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    dos.writeUTF("2");
                    dos.flush();
                    dis.close();
                    dos.close();
                    s.close();
                    loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
            }
        });
        loginFrame.setSize(300, 400);
        JLabel lName = new JLabel("Username: ");
        lName.setBounds(20, 120, 70, 30);
        loginFrame.add(lName);

        JLabel lPass = new JLabel("Password: ");
        lPass.setBounds(20, 170, 70, 30);
        loginFrame.add(lPass);

        JLabel lHName = new JLabel("Hostel: ");
        lHName.setBounds(20, 90, 70, 30);
        loginFrame.add(lHName);
        lHName.setVisible(false);

        JTextField tName = new JTextField("");
        tName.setBounds(100, 125, 150, 30);
        loginFrame.add(tName);

        JTextField tPass = new JTextField("");
        tPass.setBounds(100, 175, 150, 30);
        loginFrame.add(tPass);

        JTextField tHName = new JTextField("");
        tHName.setBounds(100, 95, 150, 30);
        loginFrame.add(tHName);
        tHName.setVisible(false);

        status = new JLabel("");
        status.setBounds(20, 70, 250, 20);
        loginFrame.add(status);

        JButton signUP = new JButton("SignUP");
        signUP.setBounds(130, 270, 80, 25);
        loginFrame.add(signUP);
        signUP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("");
                loginFrame.setVisible(false);
                signUpPage(tName.getText(),tPass.getText());
            }
        });

        String languages[]={"Admin","Student"};
        JComboBox cb=new JComboBox(languages);
        cb.setBounds(105, 30,90,20);
        loginFrame.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(cb.getSelectedIndex());
                status.setText("");
                if(cb.getSelectedIndex()== 0){
                    lName.setBounds(20, 120, 70, 30);
                    tName.setBounds(100, 125, 150, 20);
                    signUP.setVisible(true);
                    lHName.setVisible(false);
                    tHName.setVisible(false);
                }
                else{
                    lName.setBounds(20, 130, 70, 30);
                    tName.setBounds(100, 135, 150, 20);
                    signUP.setVisible(false);
                    lHName.setVisible(true);
                    tHName.setVisible(true);
                }
            }
        });

        JButton signIn = new JButton("SignIN");
        signIn.setBounds(130, 230, 80, 25);
        loginFrame.add(signIn);
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("");
                String str= "";
                try {
                    dos.writeUTF("1");// signIN
                    dos.flush();
                    dos.writeUTF(tName.getText());
                    dos.flush();
                    dos.writeUTF(tPass.getText());
                    dos.flush();
                    dos.writeUTF(cb.getSelectedIndex()+"");
                    dos.flush();
                    if(cb.getSelectedIndex()== 1){
                        dos.writeUTF(tHName.getText());
                        dos.flush();
                    }
                    str = dis.readUTF();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
                if (str.equals("1")) {
                    status.setText("Logged In");
                    loginFrame.setVisible(false);
                    status.setText("Logged Out");
                    if(cb.getSelectedIndex()== 0)
                        new adminOptions(loginFrame, dis, dos,status);
                    else
                        new studentOptions(loginFrame,dis,dos);
                }else{
                    status.setText("Account not found");
                }
            }
        });
        loginFrame.setLayout(null);
        loginFrame.setVisible(true);
    }
    public static void main(String ...args){
        Socket s;
        try {
            s = new Socket("localhost", 5000);
        } catch (Exception e) {
            s = null;
        }
        new LoginUIPage(s);
    }
}

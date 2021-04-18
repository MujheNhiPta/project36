package client.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class adminPro {
    JFrame preF, jf,pPreF;
    DataOutputStream dos;
    DataInputStream dis;
    JLabel l2, stat;
    public adminPro(JFrame pPreF, JFrame preF, DataInputStream dis, DataOutputStream dos,JLabel status){
        this.pPreF= pPreF;
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        this.stat= status;
        createUI();
    }
    void createUI(){
        jf= new JFrame("Profile");
        jf.setSize(300,350);
        JLabel l1 ,l3;
        l1= new JLabel("Name: ");
        l1.setBounds(30, 40, 240, 30);
        l1.setFont(new Font("Serif", Font.BOLD, 24));
        jf.add(l1);
        l2= new JLabel("Password: ");
        l2.setBounds(30, 90, 240, 30);
        l2.setFont(new Font("Serif", Font.BOLD, 24));
        jf.add(l2);
        l3= new JLabel("Hostel Name: ");
        l3.setBounds(30, 140, 240, 30);
        l3.setFont(new Font("Serif", Font.BOLD, 24));
        jf.add(l3);

        JLabel status= new JLabel("");
        status.setBounds(95, 275, 200, 30);
        jf.add(status);

        JButton b1,b2;
        b1= new JButton("Change Password");
        b1.setBounds(25, 190, 240, 30);
        jf.add(b1);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("");
                try {
                    dos.writeUTF("1");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                jf.setVisible(false);
                password();
                status.setText("Password changed");
            }
        });

        b2= new JButton("Delete Account");
        b2.setBounds(25, 240, 240, 30);
        jf.add(b2);

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("2");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                jf.dispose();
                preF.dispose();
                pPreF.setVisible(true);
                stat.setText("Account Deleted");
            }
        });

        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                String temp= null;
                try {
                    temp = dis.readUTF();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                StringTokenizer st= new StringTokenizer(temp, "$");
                l1.setText("Name: "+ st.nextToken());
                l2.setText("Password: "+st.nextToken());
                l3.setText("Hostel Name: "+ st.nextToken());
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dos.writeUTF("0");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                preF.setVisible(true);
            }
        });

        jf.setLayout(null);
        jf.setVisible(true);
    }

    void password(){
        JFrame frame1= new JFrame("New Password");
        frame1.setSize(300, 250);
        frame1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dos.writeUTF("0");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                jf.setVisible(true);
            }
        });
        JLabel l1= new JLabel("Enter new Password");
        l1.setBounds(70, 40, 150, 30);
        frame1.add(l1);
        JTextField t= new JTextField("");
        t.setBounds(75,80,150,30);
        frame1.add(t);
        JButton b1= new JButton("Set");
        b1.setBounds(130, 145, 60,30);
        frame1.add(b1);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");
                    dos.flush();
                    dos.writeUTF(t.getText());
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                l2.setText("Password: "+t.getText());
                frame1.dispose();
                jf.setVisible(true);
            }
        });
        frame1.setLayout(null);
        frame1.setVisible(true);
    }
}

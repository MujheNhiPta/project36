package client.admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class adminOptions {
    JFrame preF;
    DataInputStream dis;
    DataOutputStream dos;
    JLabel stat;
    public adminOptions(JFrame preF, DataInputStream dis, DataOutputStream dos,JLabel status){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        this.stat= status;
        createUI();
    }
    void createUI(){
        JFrame opFrame= new JFrame("Admin");
        opFrame.setSize(300, 420);
        //opFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        opFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    dos.writeUTF("0");
                    dos.flush();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
                preF.setVisible(true);
            }
        });
        JButton b1,b2,b3,b4,b5,b6,b7;
        b1 = new JButton("Profile");
        b1.setBounds(40, 30, 200, 30);
        opFrame.add(b1);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new adminPro(preF,opFrame, dis, dos, stat);
            }
        });

        b2 = new JButton("Check IN");
        b2.setBounds(40, 80, 200, 30);
        opFrame.add(b2);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("2");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new checkIN(opFrame, dis, dos);
            }
        });

        b3 = new JButton("Check OUT");
        b3.setBounds(40, 130, 200, 30);
        opFrame.add(b3);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("3");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new checkOut(opFrame, dis, dos);
            }
        });

        b4 = new JButton("Present Status");
        b4.setBounds(40, 180, 200, 30);
        opFrame.add(b4);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("4");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new presentStatus(opFrame, dis, dos);
            }
        });

        b5 = new JButton("Seat Allotment");
        b5.setBounds(40, 230, 200, 30);
        opFrame.add(b5);
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("5");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new alloter(opFrame, dis, dos);
            }
        });

        b6 = new JButton("Student List");
        b6.setBounds(40, 270, 200, 30);
        opFrame.add(b6);
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("6");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new studentList(opFrame, dis, dos);
            }
        });

        b7 = new JButton("Mess Structure");
        b7.setBounds(40, 320, 200, 30);
        opFrame.add(b7);
        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("7");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                opFrame.setVisible(false);
                new messStructure(opFrame, dis, dos);
            }
        });
        opFrame.setLayout(null);
        opFrame.setVisible(true);
    }
}

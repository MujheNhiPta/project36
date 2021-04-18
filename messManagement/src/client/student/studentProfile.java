package client.student;

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

public class studentProfile{
    JFrame preF,frame2;
    DataInputStream dis;
    DataOutputStream dos;
    JLabel l7;
    public studentProfile(JFrame preF, DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        createUI();
    }
    void createUI(){
        frame2 = new JFrame("Profile");
        JPanel panel2 = new JPanel();
        frame2.setSize(400, 550);
        frame2.add(panel2);

        JLabel l1=new JLabel();
        l1.setBounds(0,50,400,30);
        l1.setFont(new Font("Serif",Font.BOLD, 25));
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l1);

        JLabel l2=new JLabel("Reg. No :  ");
        l2.setBounds(0,100,400,30);
        l2.setFont(new Font("Serif", Font.BOLD, 25));
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l2);

        JLabel l3=new JLabel();
        l3.setBounds(0,150,400,30);
        l3.setFont(new Font("Serif", Font.PLAIN, 25));
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l3);

        JLabel l4=new JLabel();
        l4.setBounds(0,200,400,30);
        l4.setFont(new Font("Serif", Font.PLAIN, 25));
        l4.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l4);

        JLabel l5=new JLabel();
        l5.setBounds(0,250,400,30);
        l5.setFont(new Font("Serif", Font.PLAIN, 25));
        l5.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l5);

        JLabel l6=new JLabel();
        l6.setBounds(0,300,400,30);
        l6.setFont(new Font("Serif", Font.PLAIN, 25));
        l6.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l6);

        l7=new JLabel();
        l7.setBounds(0,350,400,30);
        l7.setFont(new Font("Serif", Font.PLAIN, 25));
        l7.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l7);

        JButton change=new JButton("Change Password");
        change.setBounds(110,415,175,30);
        change.setFont(new Font("Serif", Font.PLAIN, 20));
        panel2.add(change);

        JLabel status= new JLabel("");
        status.setBounds(110, 450, 200, 30);
        panel2.add(status);
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    status.setText("");
                    dos.writeUTF("1");   //1-> change dabaya
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                frame2.setVisible(false);
                password();
                status.setText("Password Changed");
            }
        });
        frame2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                String st= "";
                try {
                    st= dis.readUTF();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                StringTokenizer st_Token = new StringTokenizer(st, "$");
                l1.setText("Name: "+st_Token.nextToken());
                l2.setText("Reg: "+st_Token.nextToken());
                l3.setText("Hostel: "+st_Token.nextToken());
                l4.setText("Room No.: "+st_Token.nextToken());
                l5.setText("Mobile No.: "+st_Token.nextToken());
                l6.setText("Email: "+st_Token.nextToken());
                l7.setText("Password: "+st_Token.nextToken());
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
        panel2.setLayout(null);
        frame2.setVisible(true);
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
                frame2.setVisible(true);
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
                l7.setText("Password: "+t.getText());
                frame1.dispose();
                frame2.setVisible(true);
            }
        });
        frame1.setLayout(null);
        frame1.setVisible(true);
    }
}
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class checkOut {
    JFrame frame=null;
    JPanel panel;
    Connection conn;
    Statement stmt;
    JFrame preF;
    DataOutputStream dos;
    DataInputStream dis;

    public checkOut(JFrame preF, DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        checkOutUI();
    }

    void checkOutUI(){
        frame = new JFrame("Check OUT");
        panel = new JPanel();
//        frame.setLocation(300,400);
        frame.setSize(400, 400);
        panel.setLayout(null);
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
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

        JLabel lb=new JLabel("Registration Number");
        lb.setBounds(75,75,300,50);
        lb.setFont(new Font("Serif", Font.BOLD, 30));
        panel.add(lb);

        JTextField tf=new JTextField();
        tf.setBounds(100,150,200,35);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(tf);

        JButton in=new JButton("CHECK Out");
        in.setBounds(150,225,125,35);
        panel.add(in);

        JLabel l= new JLabel("");
        l.setBounds(75, 270, 300, 40);
        l.setFont(new Font("Serif",Font.PLAIN, 17));
        panel.add(l);

        in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.setText("");
                try {
                    dos.writeUTF("1"); // check in Button dabaya h
                    dos.flush();
                    dos.writeUTF(tf.getText());
                    dos.flush();
                    String st= dis.readUTF();
                    if(st.equals("1"))
                        l.setText("CheckOut successfully");
                    else
                        l.setText("Can Not client.admin.checkOut Now");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        frame.setVisible(true);
    }
}
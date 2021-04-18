package client.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class studentOptions {
    private JFrame preF;
    DataOutputStream dos;
    DataInputStream dis;
    public studentOptions(JFrame preF, DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        createUI();
    }
    private void createUI(){
        JFrame jf= new JFrame("Student");
        jf.setSize(300, 200);
        jf.addWindowListener(new WindowAdapter() {
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
        JButton b1, b2;
        b1= new JButton("Profile");
        b1.setBounds(90, 45, 100, 30);
        jf.add(b1);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                jf.setVisible(false);
                new studentProfile(jf, dis, dos);
            }
        });
        b2= new JButton("Status");
        b2.setBounds(90, 105, 100, 30);
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
                jf.setVisible(false);
                new bookingStatus(jf, dis, dos);
            }
        });
        jf.setLayout(null);
        jf.setVisible(true);
    }
}

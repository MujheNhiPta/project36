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

public class bookingStatus {
    JFrame preF;
    DataOutputStream dos;
    DataInputStream dis;
    int slots;
    public bookingStatus(JFrame preF, DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        try {
            slots= Integer.parseInt(dis.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
        createUI();
    }
    void createUI(){
        JFrame jf= new JFrame();
        jf.setSize(350,330);
        String slo[]= new String[slots];
        for(int i= 0;i<slots;i++)
        { slo[i]= ""+(i+1);
        System.out.println(slo[i]);
        }
        JLabel l1= new JLabel("Choose Slot: ");
        l1.setBounds(40, 75, 150, 40);
        l1.setFont(new Font("Serif", Font.PLAIN, 25));
        jf.add(l1);

        JComboBox jb= new JComboBox(slo);
        jb.setBounds(225, 80, 70, 30);
        jf.add(jb);
        JButton b1= new JButton("Send Request");
        b1.setBounds(100, 140, 160, 30);
        jf.add(b1);
        JLabel reqStatus= new JLabel("");
        reqStatus.setBounds(40, 240, 200, 30);
        reqStatus.setFont(new Font("Serif", Font.PLAIN, 17));
        jf.add(reqStatus);
        JLabel bookStatus= new JLabel("");
        bookStatus.setBounds(40, 220, 200, 30);
        bookStatus.setFont(new Font("Serif", Font.PLAIN, 17));
        jf.add(bookStatus);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                b1.setEnabled(false);
                String temp="0$00";
                try {
                    dos.writeUTF("1");
                    dos.flush();
                    dos.writeUTF(slo[jb.getSelectedIndex()]+"");
                    dos.flush();
                    temp= dis.readUTF();     //seat+booked or not
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println(temp);
                StringTokenizer st= new StringTokenizer(temp,"$");
                temp= st.nextToken();
                if(st.nextToken().equals("1")){
                    bookStatus.setText("Slot= "+ (slo[jb.getSelectedIndex()])+ "And Seat= "+ temp);
                    reqStatus.setText("Request Accepted");
                }
                else
                    reqStatus.setText("Slot is Full");
            }
        });
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                //slot and seat info to setText client.student.bookingStatus
                String t1= "0$00", t2;
                int min= 0;
                try {
                    t1= dis.readUTF();
                    min= Integer.parseInt(dis.readUTF());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                StringTokenizer ss= new StringTokenizer(t1, "$");
                t1= ss.nextToken();
                t2= ss.nextToken();
                if(t1.equals("0") && t2.equals("0"))
                    bookStatus.setText("Slot and Seat Not Declared");
                else{
                    b1.setEnabled(false);
                    bookStatus.setText("Slot= "+t1+"And Seat= "+t2);
                }
                if(min>= -60){
                    b1.setEnabled(false);
                }
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
}

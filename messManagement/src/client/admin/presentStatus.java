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
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class presentStatus {
    DataInputStream dis;
    DataOutputStream dos;
    JFrame frame=null,frame2=null,preF;
    JPanel panel,panel2;
    int rows,cols,presentSlot;//to be filled
    ArrayList<JButton> benches=new ArrayList<>();

    public presentStatus(JFrame preF,DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        mess();
    }


    void mess() {
        String str= "0$0$0";
        try {
            str = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringTokenizer st_T = new StringTokenizer(str, "$");
        this.rows= Integer.parseInt(st_T.nextToken());
        this.cols= Integer.parseInt(st_T.nextToken());
        this.presentSlot=Integer.parseInt(st_T.nextToken());
        frame = new JFrame();
        panel = new JPanel();
        frame.setSize(900, 700);
        frame.setLocation(175,50);
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
        buildMess(rows, cols);
        for (int t=0;t<benches.size();t++)
            benches.get(t).setVisible(true);
        makeSeatClickListeners();
        frame.setVisible(true);
    }

    void buildMess(int rows,int cols){
        JButton b1=new JButton("");
        b1.setBounds(50,25,25,25);
        b1.setBackground(Color.orange);
        panel.add(b1);

        JButton b2=new JButton("");
        b2.setBounds(250,25,25,25);
        b2.setBackground(Color.cyan);
        panel.add(b2);

        JButton b3=new JButton("");
        b3.setBounds(450,25,25,25);
        b3.setBackground(Color.GREEN);
        panel.add(b3);

        JLabel l1=new JLabel(": Has Visited");
        l1.setBounds(80,25,125,25);
        panel.add(l1);

        JLabel l2=new JLabel(": Not Reported Yet");
        l2.setBounds(280,25,125,25);
        panel.add(l2);

        JLabel l3=new JLabel(": Is In the Mess");
        l3.setBounds(480,25,125,25);
        panel.add(l3);

        int x=50,y=125,i,j,l=30,c=1;
        for(i=0;i<2*rows;i++){
            if(i%2==0) {
                JLabel rowCount = new JLabel("ROW: " + Integer.toString((i / 2) + 1));
                rowCount.setBounds(x, y - 60, 160, 40);
                rowCount.setHorizontalAlignment(SwingConstants.CENTER);
                rowCount.setFont(new Font("Serif", Font.BOLD, 25));
                panel.add(rowCount);
            }
            for(j=0;j<cols;j++){
                JButton temp=new JButton("Empty");
                temp.setBounds(x,y,80,25);
                temp.setVisible(false);
                panel.add(temp);
                benches.add(temp);
                JLabel lb=new JLabel(Integer.toString(c++));
                lb.setBounds(l,y,50,25);
                panel.add(lb);
                lb.setVisible(true);
                y+=35;
            }
            if(i%2==0) {
                x += 95;
                l=x+90;
            }
            else {
                x += 200;
                l=x-20;
            }
            y=125;
        }


        try{
            String t="";
            while(true) {
                t=dis.readUTF();
                if(t.equals("0"))
                    break;
                StringTokenizer st_Token = new StringTokenizer(t, "$");
                String RegNo = st_Token.nextToken();
                String st = st_Token.nextToken();
                String checkin=st_Token.nextToken();
                String checkout=st_Token.nextToken();
                int seat = Integer.parseInt(st);

                benches.get(seat - 1).setText(RegNo);

                if(checkin.equals("1") && checkout.equals("1"))
                    benches.get(seat - 1).setBackground(Color.orange);
                else if(checkin.equals("1") && checkout.equals("0"))
                    benches.get(seat - 1).setBackground(Color.GREEN);
                else
                    benches.get(seat - 1).setBackground(Color.cyan);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    void makeSeatClickListeners(){
        int y;
        for(y=0;y<benches.size();y++){
            int x=y;
            JButton jb=benches.get(x);
            jb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s=jb.getText();
                    if(!s.equals("Empty")){
                        frame.setVisible(false);
                        frame2 = new JFrame(s);
                        panel2 = new JPanel();
                        frame2.setLocation(325,100);
                        frame2.setSize(400, 500);
                        panel2.setLayout(null);
                        frame2.add(panel2);

                        JLabel hd=new JLabel("Seat No: "+Integer.toString(x+1));
                        hd.setBounds(0,40,400,40);
                        hd.setFont(new Font("Serif", Font.BOLD, 32));
                        hd.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(hd);

                        JLabel l1=new JLabel();
                        l1.setBounds(0,100,400,30);
                        l1.setFont(new Font("Serif", Font.PLAIN, 25));
                        l1.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l1);

                        JLabel l2=new JLabel("Reg. No :  "+s);
                        l2.setBounds(0,150,400,30);
                        l2.setFont(new Font("Serif", Font.PLAIN, 25));
                        l2.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l2);

                        JLabel l3=new JLabel();
                        l3.setBounds(0,200,400,30);
                        l3.setFont(new Font("Serif", Font.PLAIN, 25));
                        l3.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l3);

                        JLabel l4=new JLabel();
                        l4.setBounds(0,250,400,30);
                        l4.setFont(new Font("Serif", Font.PLAIN, 25));
                        l4.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l4);

                        JLabel l5=new JLabel();
                        l5.setBounds(0,300,400,30);
                        l5.setFont(new Font("Serif", Font.PLAIN, 25));
                        l5.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l5);

                        JLabel l6=new JLabel();
                        l6.setBounds(0,350,400,30);
                        l6.setFont(new Font("Serif", Font.PLAIN, 25));
                        l6.setHorizontalAlignment(SwingConstants.CENTER);
                        panel2.add(l6);
                        frame2.setVisible(true);

                        frame2.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowOpened(WindowEvent e) {
                                String st= "";
                                try {
                                    dos.writeUTF(jb.getText());
                                    dos.flush();
                                    st= dis.readUTF();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
//                                System.out.println(st);
                                StringTokenizer st_Token = new StringTokenizer(st, "$");
                                l1.setText("Name: "+st_Token.nextToken());
                                l3.setText("Hostel: "+st_Token.nextToken());
                                l4.setText("Room No.: "+st_Token.nextToken());
                                l5.setText("Mobile No.: "+st_Token.nextToken());
                                l6.setText("Email: "+st_Token.nextToken());
                            }

                            @Override
                            public void windowClosing(WindowEvent e) {
//                                try{
//                                    dos.writeUTF("2");
//                                    dos.flush();
//                                }catch(Exception ex){
//                                    ex.printStackTrace();
//                                }
                                frame2.dispose();
                                frame.setVisible(true);
                            }
                        });
                    }
                }
            });
        }
    }
}

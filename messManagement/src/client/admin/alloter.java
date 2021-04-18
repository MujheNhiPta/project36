package client.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.StringTokenizer;

public class alloter {
    DataOutputStream dos;
    DataInputStream dis;
    JFrame frame=null, preF;
    JPanel panel;
    JList js,tmp=null;
    JTextField tf;
    JButton search,cancel;
    DefaultListModel<String> list=new DefaultListModel<>();
    DefaultListModel<String> tp;
    public alloter(JFrame preF, DataInputStream dis, DataOutputStream dos) {
        this.preF = preF;
        this.dis = dis;
        this.dos = dos;
        generateList();
    }

    void generateList(){
            frame = new JFrame();
            panel = new JPanel();
            frame.setSize(700, 700);
            panel.setLayout(null);
            JScrollPane jsp = new JScrollPane(panel);
            panel.setPreferredSize(new Dimension(250, 10000));
            panel.setLayout(null);
            frame.getContentPane().add(jsp);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    String temp= "";
                    while(!temp.equals("0")){
                        try {
                            temp= dis.readUTF();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        if(!temp.equals("0"))
                        {
                            StringTokenizer st_Token = new StringTokenizer(temp, "$");
                            list.addElement("           "+st_Token.nextToken()+"                    " + st_Token.nextToken()+"                " + st_Token.nextToken());
                        }
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

            JLabel rg=new JLabel("Registration No.");
            rg.setBounds(115,175,150,35);
            rg.setFont(new Font("Serif", Font.BOLD, 19));
            rg.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(rg);

            JLabel sl=new JLabel("Slot");
            sl.setBounds(300,175,100,35);
            sl.setFont(new Font("Serif", Font.BOLD, 19));
            sl.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(sl);

            JLabel sn=new JLabel("Seat No.");
            sn.setBounds(425,175,100,35);
            sn.setFont(new Font("Serif", Font.BOLD, 19));
            sn.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(sn);

            JButton newTokens=new JButton("GENERATE TOKENS");
            newTokens.setBounds(100,40,500,50);
            newTokens.setFont(new Font("Serif", Font.PLAIN, 25));
            panel.add(newTokens);

            JLabel l= new JLabel("");
            l.setBounds(320, 10, 100, 30);
            panel.add(l);

            newTokens.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        l.setText("Generating...");
                        dos.writeUTF("1");  //1-> new student
                        dos.flush();
                        String s= dis.readUTF();
                        if(s.equals("1"))
                            l.setText("Generated");
                        list.clear();
                        String temp= "";
                        while(!temp.equals("0")){
                            try {
                                temp= dis.readUTF();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            if(!temp.equals("0"))
                            {
                                StringTokenizer st_Token = new StringTokenizer(temp, "$");
                                list.addElement("           "+st_Token.nextToken()+"                    " + st_Token.nextToken()+"                " + st_Token.nextToken());
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            js=new JList(list);
            js.setBounds(100,225,500,10000);
            js.setFont(new Font("Serif", Font.PLAIN, 25));
            panel.add(js);


            tf=new JTextField("Search by RegNo.");
            tf.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    tf.setText("");
                }
            });
            tf.setBounds(125,125,275,35);
            tf.setFont(new Font("Serif", Font.PLAIN, 20));
            panel.add(tf);

            search=new JButton("Search");
            search.setBounds(400,125,125,35);
            search.setFont(new Font("Serif", Font.PLAIN, 20));
            panel.add(search);

            cancel=new JButton("Cancel");
            cancel.setBounds(400,125,125,35);
            cancel.setFont(new Font("Serif", Font.PLAIN, 20));
            panel.add(cancel);
            cancel.setVisible(false);

            search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dos.writeUTF("2");
                        dos.flush();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    js.setVisible(false);
                    String str=tf.getText();
                    tp=new DefaultListModel<>(); ///to be modified
                    String temp= "";
                    try {
                        dos.writeUTF(str);
                        dos.flush();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    //to be updated
                        try {
                            temp= dis.readUTF();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        if(!temp.equals("0"))
                        {
                            StringTokenizer st_Token = new StringTokenizer(temp, "$");
                            tp.addElement("           "+str+"                   " + st_Token.nextToken()+"                " + st_Token.nextToken());
                        }
                        else{
                            //to be done
                        }

                    tmp=new JList(tp);
                    tmp.setBounds(100,225,500,2000);
                    tmp.setFont(new Font("Serif", Font.PLAIN, 25));
                    panel.add(tmp);

                    search.setVisible(false);
                    cancel.setVisible(true);
                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(tmp!=null)
                                tmp.setVisible(false);
                            js.setVisible(true);
                            tf.setText("");
                            search.setVisible(true);
                            cancel.setVisible(false);
                        }
                    });
                }
            });

            frame.setVisible(true);

        }
}

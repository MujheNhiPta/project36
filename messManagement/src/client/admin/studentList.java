package client.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class studentList  {
    DataOutputStream dos;
    DataInputStream dis;
    JFrame frame=null,frame2=null, preF;
    JPanel panel,panel2;
    JList js,tmp=null;
    JTextField tf;
    JButton search,cancel;
    String sql;
    int x=0,clicked=0;
    DefaultListModel<String> list=new DefaultListModel<>();
    DefaultListModel<String> tp;

    public studentList(JFrame preF, DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        generateList();
    }

    studentList addr(){
        return this;
    }

    void updateList(String reg, String name){
        list.addElement("           "+reg+"                 " + name);
    }

    void generateList() {
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
                        list.addElement("           "+st_Token.nextToken()+"                 " + st_Token.nextToken());
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

        JLabel nam=new JLabel("Name");
        nam.setBounds(325,175,150,35);
        nam.setFont(new Font("Serif", Font.BOLD, 19));
        nam.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nam);

        JButton newRegister=new JButton("REGISTER NEW");
        newRegister.setBounds(100,40,500,50);
        newRegister.setFont(new Font("Serif", Font.PLAIN, 25));
        panel.add(newRegister);




        newRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");  //1-> new student
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                new regStudent(frame, dis, dos, addr());
                frame.setVisible(false);
            }
        });

        js=new JList(list);
        js.setBounds(100,225,500,10000);
        js.setFont(new Font("Serif", Font.PLAIN, 25));
        panel.add(js);

        js.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    dos.writeUTF("3");   //3-> main list dabaya
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                int index=js.getSelectedIndex();
                String reg="";
                String temp= list.elementAt(index);
                int i=0;
                while(temp.charAt(i)==' ') {
                    i++;
                }
                while(temp.charAt(i)!=' '){
                    reg+=temp.charAt(i);
                    i++;
                }
                frame.setVisible(false);
                displayInfo(index,reg);
            }
        });

        tf=new JTextField("Search by Reg No. or Name");
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
                    dos.writeUTF("2");   //2->search dabaya
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                x=1;
                js.setVisible(false);
                String str=tf.getText();
                tp=new DefaultListModel<>();
                String temp= "";
                try {
                    dos.writeUTF(str);
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                while(!temp.equals("0")){
                    try {
                        temp= dis.readUTF();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if(!temp.equals("0"))
                    {
                        StringTokenizer st_Token = new StringTokenizer(temp, "$");
                        tp.addElement("           "+st_Token.nextToken()+"                 " + st_Token.nextToken());
                    }
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
                            cancelListener();
                        }
                    });

                    tmp.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            try {
                                dos.writeUTF("3");
                                dos.flush();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            clicked=tmp.getSelectedIndex();
                            int idx=list.indexOf(tp.get(clicked));
                            String reg="";
                            String temp= list.elementAt(idx);
                            int i=0;
                            while(temp.charAt(i)==' ') {
                                i++;
                            }
                            while(temp.charAt(i)!=' '){
                                reg+=temp.charAt(i);
                                i++;
                            }
                            frame.setVisible(false);
                            displayInfo(idx,reg);
                        }
                    });
           }
        });

        frame.setVisible(true);

    }
    void displayInfo(int index,String reg){
        frame2 = new JFrame(reg);
        panel2 = new JPanel();
        frame2.setSize(400, 550);
        panel2.setLayout(null);
        frame2.add(panel2);



        JLabel l1=new JLabel();
        l1.setBounds(0,50,400,30);
        l1.setFont(new Font("Serif", Font.BOLD, 25));
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l1);

        JLabel l2=new JLabel("Reg. No :  "+reg);
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

        JLabel l7=new JLabel();
        l7.setBounds(0,350,400,30);
        l7.setFont(new Font("Serif", Font.PLAIN, 25));
        l7.setHorizontalAlignment(SwingConstants.CENTER);
        panel2.add(l7);

        JButton deleteStd=new JButton("Delete Student");
        deleteStd.setBounds(110,425,175,40);
        deleteStd.setFont(new Font("Serif", Font.PLAIN, 20));
        panel2.add(deleteStd);

        deleteStd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");   //1-> delete dabaya
                    dos.flush();
                    dos.writeUTF(reg);
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                list.remove(index);
                frame2.dispose();
                dingding();
                frame.setVisible(true);
            }
        });
        frame2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                String st= "";
                try {
                    dos.writeUTF(reg);
                    dos.flush();
                    st= dis.readUTF();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                StringTokenizer st_Token = new StringTokenizer(st, "$");
                l1.setText("Name: "+st_Token.nextToken());
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
                frame.setVisible(true);
            }
        });
        frame2.setVisible(true);
    }

    void cancelListener(){
        if(tmp!=null)
            tmp.setVisible(false);
        js.setVisible(true);
        tf.setText("");
        search.setVisible(true);
        cancel.setVisible(false);
    }
    void dingding(){
        if(x==1 && clicked!=-1){
            tp.remove(clicked);
            x=0;
        }
    }
}


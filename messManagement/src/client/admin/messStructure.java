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
import java.util.ArrayList;
import java.util.StringTokenizer;

public class messStructure {
    private JFrame preF;
    DataOutputStream dos;
    DataInputStream dis;
    String str;
    ArrayList<JLabel> lbs=new ArrayList<>();
    public messStructure(JFrame preF,DataInputStream dis, DataOutputStream dos){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        createUI();
    }
    void createUI(){
        JFrame frame;
        frame=new JFrame();
        frame.setSize(600,600);
        JLabel rows=new JLabel("Rows: ");
        rows.setBounds(40, 95, 125, 30);
        rows.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(rows);

        JTextField rowEntry=new JTextField("");
        rowEntry.setBounds(200,100,250,25);
        frame.add(rowEntry);

        JLabel cols=new JLabel("Columns: ");
        cols.setBounds(40, 140, 125, 30);
        cols.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(cols);

        JTextField colEntry=new JTextField("");
        colEntry.setBounds(200,145,250,25);
        frame.add(colEntry);

        JLabel strTime=new JLabel("Mess Start Time: ");
        strTime.setBounds(40, 195, 125, 30);
        strTime.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(strTime);

        JTextField hhEntry=new JTextField("");
        hhEntry.setBounds(200,200,30,25);
        frame.add(hhEntry);

        JLabel hh=new JLabel("hh");
        hh.setBounds(235, 195, 30, 30);
        hh.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(hh);

        JTextField mmEntry=new JTextField("");
        mmEntry.setBounds(280,200,30,25);
        frame.add(mmEntry);

        JLabel mm=new JLabel("mm");
        mm.setBounds(315, 195, 30, 30);
        mm.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(mm);
        String languages[]={"AM","PM"};
        JComboBox cb=new JComboBox(languages);
        cb.setBounds(365, 200,90,20);
        frame.add(cb);

        JLabel messTotTime=new JLabel("Mess Tot Time(min): ");
        messTotTime.setBounds(40, 240, 175, 30);
        messTotTime.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(messTotTime);

        JTextField messTotTimeEntry=new JTextField("");
        messTotTimeEntry.setBounds(200,245,250,25);
        frame.add(messTotTimeEntry);

        JLabel gap=new JLabel("Gap(min): ");
        gap.setBounds(40, 295, 125, 30);
        gap.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(gap);

        JTextField gapEntry=new JTextField("");
        gapEntry.setBounds(200,300,250,25);
        frame.add(gapEntry);

        JLabel totStud=new JLabel("Total Student: ");
        totStud.setBounds(40, 340, 125, 30);
        totStud.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(totStud);

        JTextField totStudEntry=new JTextField("");
        totStudEntry.setBounds(200,345,250,25);
        frame.add(totStudEntry);

        JLabel status= new JLabel("");
        status.setBounds(40, 390, 250, 30);
        frame.add(status);

        JButton show= new JButton("Show");
        show.setBounds(180,430,150,25);
        frame.add(show);

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("");
                frame.setVisible(false);
                int r= Integer.parseInt(rowEntry.getText());
                int c= Integer.parseInt(colEntry.getText());
                int s= Integer.parseInt(totStudEntry.getText());
                int T= Integer.parseInt(messTotTimeEntry.getText());
                int g= Integer.parseInt(gapEntry.getText());
                int slo;
                double ti;
                try {
                     slo = (int) Math.ceil((double)s/(r*c));
                     ti = ((double)(T / slo)) - g;
                }
                catch(Exception d){
                    slo= 0;
                    ti= 0.0;
                }
                generateMess(r,c,frame, slo, ti);
                //frame.setVisible(true);
            }
        });

        JButton sett= new JButton("Set");
        sett.setBounds(180,480,150,25);
        frame.add(sett);

        sett.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");
                    dos.flush();
                    int r= Integer.parseInt(rowEntry.getText());
                    int c= Integer.parseInt(colEntry.getText());
                    int s= Integer.parseInt(totStudEntry.getText());
                    int T= Integer.parseInt(messTotTimeEntry.getText());
                    int g= Integer.parseInt(gapEntry.getText());
                    int slo;
                    double ti;
                    try {
                        slo = (int) Math.ceil((double)s/(r*c));
                        ti = ((double)(T / slo)) - g;
                    }
                    catch(Exception d){
                        slo= 0;
                        ti= 0.0;
                    }
                    dos.writeUTF(rowEntry.getText()+"#"+ colEntry.getText()+"#"+ hhEntry.getText()+"$"+ mmEntry.getText()+"$"+ cb.getSelectedIndex()+"#"+ messTotTimeEntry.getText()+"#"+gapEntry.getText()+"#"+slo+"#"+ti+"#"+totStudEntry.getText());
                    dos.flush();
                    if(dis.readUTF().equals("1"))
                        status.setText("Data Updated");
                    else
                        status.setText("Wrong input");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    str= dis.readUTF();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                StringTokenizer st_Token = new StringTokenizer(str, "$");
                String t[]= {st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken(),st_Token.nextToken()};
                rowEntry.setText(t[0]);
                colEntry.setText(t[1]);
                hhEntry.setText(t[2]);
                mmEntry.setText(t[3]);
                if(t[4].equals("1"))
                    cb.setSelectedIndex(1);
                messTotTimeEntry.setText(t[5]);
                gapEntry.setText(t[6]);
                totStudEntry.setText(t[7]);

            }
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dos.writeUTF("0");
                    dos.flush();
                    preF.setVisible(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }

    void generateMess(int rows,int cols, JFrame preF, int slot, double time){
        JFrame jf= new JFrame("Structure");
        jf.setSize(700, 600);
        JLabel l1= new JLabel("Slots: "+ slot);
        l1.setBounds(100, 50 ,150, 30);
        l1.setFont(new Font("Serif", Font.PLAIN, 22));
        jf.add(l1);
        JLabel l2= new JLabel("Time for each Slot: "+ time);
        l2.setBounds(350, 50 ,250, 30);
        l2.setFont(new Font("Serif", Font.PLAIN, 22));
        jf.add(l2);

        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                preF.setVisible(true);
            }
        });
        int x=50,y=175,i,j,l=30,c=1;
        for(i=0;i<2*rows;i++){
            if(i%2==0) {
                JLabel rowCount = new JLabel("ROW: " + Integer.toString((i / 2) + 1));
                rowCount.setBounds(x, y - 60, 160, 40);
                lbs.add(rowCount);
                rowCount.setVisible(true);
                rowCount.setHorizontalAlignment(SwingConstants.CENTER);
                rowCount.setFont(new Font("Serif", Font.BOLD, 25));
                jf.add(rowCount);
            }
            for(j=0;j<cols;j++){
                JLabel temp=new JLabel(Integer.toString(c++),SwingConstants.CENTER);
                temp.setBounds(x,y,80,25);
                temp.setBackground(Color.cyan);
                temp.setVisible(true);
                temp.setOpaque(true);
                lbs.add(temp);
                jf.add(temp);
                y=y+35;
            }
            if(i%2==0) {
                x += 95;
                l=x+90;
            }
            else {
                x += 200;
                l=x-20;
            }
            y=175;
        }
        //panel.setLayout(null);
        jf.setLayout(null);
        jf.setVisible(true);
    }
}

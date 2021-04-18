package client.admin;

import javax.swing.*;;import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class regStudent {
    private JFrame preF;
    DataOutputStream dos;
    DataInputStream dis;
    studentList stu;
    public regStudent(JFrame preF, DataInputStream dis, DataOutputStream dos, studentList stu){
        this.preF= preF;
        this.dis= dis;
        this.dos= dos;
        this.stu= stu;
        createUI();
    }
    private void createUI(){
        JFrame frame;
        JButton create;
        frame=new JFrame();
        frame.setSize(600,600);
        frame.addWindowListener(new WindowAdapter() {
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

        JLabel name=new JLabel("Name: ");
        name.setBounds(40, 95, 125, 30);
        name.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(name);

        JTextField nameEntry=new JTextField("");
        nameEntry.setBounds(175,100,250,25);
        frame.add(nameEntry);

        JLabel regno=new JLabel("Registration no.: ");
        regno.setBounds(40, 135, 125, 30);
        regno.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(regno);

        JTextField regEntry=new JTextField("");
        regEntry.setBounds(175,140,250,25);
        frame.add(regEntry);

        JLabel hostel=new JLabel("Hostel name: ");
        hostel.setBounds(40, 175, 125, 30);
        hostel.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(hostel);

        JTextField hostelEntry=new JTextField("");
        hostelEntry.setBounds(175,180,250,25);
        frame.add(hostelEntry);

        JLabel rno=new JLabel("Room no.: ");
        rno.setBounds(40, 215, 125, 30);
        rno.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(rno);

        JTextField rnoEntry=new JTextField("");
        rnoEntry.setBounds(175,220,250,25);
        frame.add(rnoEntry);

        JLabel mobileno=new JLabel("Mobile no.: ");
        mobileno.setBounds(40, 255, 125, 30);
        mobileno.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(mobileno);

        JTextField mobilenoEntry=new JTextField("");
        mobilenoEntry.setBounds(175,260,250,25);
        frame.add(mobilenoEntry);

        JLabel email=new JLabel("E-mail id: ");
        email.setBounds(40, 295, 125, 30);
        email.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(email);

        JTextField emailEntry=new JTextField("");
        emailEntry.setBounds(175,300,250,25);
        frame.add(emailEntry);

        JLabel pswd=new JLabel("Password: ");
        pswd.setBounds(40, 335, 125, 30);
        pswd.setFont(new Font("Serif", Font.PLAIN, 17));
        frame.add(pswd);

        JTextField pswdEntry=new JTextField("");
        pswdEntry.setBounds(175,340,250,25);
        frame.add(pswdEntry);

        create=new JButton("Create");
        create.setBounds(180,400,100,35);
        frame.add(create);

        JLabel status= new JLabel("");
        status.setBounds(40, 460, 150, 30);
        frame.add(status);

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("1");
                    dos.flush();
                    dos.writeUTF(pswdEntry.getText()+"$"+ emailEntry.getText() +"$"+ mobilenoEntry.getText()+"$"+ rnoEntry.getText()+"$"+ hostelEntry.getText()+"$"+ regEntry.getText()+"$"+ nameEntry.getText());
                    dos.flush();
                    String t= dis.readUTF();
                    if(t.equals("1")){
                        stu.updateList(regEntry.getText(),nameEntry.getText());
                        pswdEntry.setText("");
                        emailEntry.setText("");
                        mobilenoEntry.setText("");
                        rnoEntry.setText("");
                        hostelEntry.setText("");
                        regEntry.setText("");
                        nameEntry.setText("");
                        status.setText("Account Created");
                    }
                    else{
                        status.setText("Not Created, Wrong input");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

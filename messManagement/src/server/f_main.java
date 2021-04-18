package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

public class f_main implements Runnable{
    static Connection con,conStud, conSlot, conVacant,beg;
    static ServerSocket ss;
    public f_main(){
        try {
            ss=new ServerSocket(5000);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    public void run(){
        Socket s;
        while(true){
            try {
                s= ss.accept();
            } catch (IOException e3) {
                continue;
            }
            loginClass lC= new loginClass(s);
            Thread lC_T= new Thread(lC);
            lC_T.start();
        }
    }
    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            f_main.beg= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/","root","beetroot");
            String q= "CREATE DATABASE IF NOT EXISTS messaccounts";
            f_main.beg.createStatement().execute(q);

            q= "CREATE DATABASE IF NOT EXISTS student";
            f_main.beg.createStatement().execute(q);

            q= "CREATE DATABASE IF NOT EXISTS slots";
            f_main.beg.createStatement().execute(q);

            q= "CREATE DATABASE IF NOT EXISTS vacant";
            f_main.beg.createStatement().execute(q);

            f_main.beg.close();

            f_main.con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/messaccounts","root","beetroot");
            q= "CREATE TABLE IF NOT EXISTS `messaccounts`.`manager_accounts` (`userName` VARCHAR(30) NOT NULL,`password` VARCHAR(20) NOT NULL,`hostelName` VARCHAR(70) NOT NULL,`studentFile` VARCHAR(45) NOT NULL,`rows` VARCHAR(10) NOT NULL,`cols` VARCHAR(10) NOT NULL,`messTime` VARCHAR(10) NOT NULL,`messtotTime` VARCHAR(10) NOT NULL,`gap` VARCHAR(10) NOT NULL,`TotSlots` VARCHAR(10) NOT NULL,`timeForSlot` VARCHAR(10) NOT NULL,`totStud` VARCHAR(14) NOT NULL, UNIQUE INDEX `userName_UNIQUE` (`userName` ASC) VISIBLE, UNIQUE INDEX `hostelName_UNIQUE` (`hostelName` ASC) VISIBLE, UNIQUE INDEX `studentFile_UNIQUE` (`studentFile` ASC) VISIBLE)";
            f_main.con.createStatement().execute(q);

            f_main.conStud= DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "beetroot");
            f_main.conSlot= DriverManager.getConnection("jdbc:mysql://localhost:3306/slots", "root", "beetroot");
            f_main.conVacant= DriverManager.getConnection("jdbc:mysql://localhost:3306/vacant", "root", "beetroot");
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }
    public static void main(String ...args){
        f_main fm= new f_main();
        Thread fm_T= new Thread(fm);
        fm_T.start();
    }
}

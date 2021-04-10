import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class f_main implements Runnable{
    static Connection con,conStud, conSlot, conVacant;
    //static Statement stmt,stmtStud, stmtSlot, stmtVacant;
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
            f_main.con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/messaccounts","root","beetroot");
            //f_main.stmt= con.createStatement();
            f_main.conStud= DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "beetroot");
            //f_main.stmtStud= conStud.createStatement();
            f_main.conSlot= DriverManager.getConnection("jdbc:mysql://localhost:3306/slots", "root", "beetroot");
            //f_main.stmtSlot= conStud.createStatement();
            f_main.conVacant= DriverManager.getConnection("jdbc:mysql://localhost:3306/vacant", "root", "beetroot");
            //f_main.stmtVacant= conStud.createStatement();


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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.StringTokenizer;

public class loginClass implements Runnable{
    final private Socket vSkt;
    private DataInputStream dis;
    private DataOutputStream dos;
    public void run(){
        String query,temp1="",temp2="",temp3="";
        while(true){
            try {
                temp1= dis.readUTF(); //0->signUP 1->signIN 2->cutPage
            } catch (IOException e7) {
                e7.printStackTrace();
            }
            //for signUP
            if(temp1.equals("0")) {
                try {
                    temp1 = dis.readUTF();// hostelName
                    temp2 = dis.readUTF();// userName
                    temp3 = dis.readUTF();// password
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                temp1= temp1.toLowerCase();//hostelName only in lowercase in database
                query = "SELECT * From manager_accounts WHERE hostelName= '" + temp1 + "'";
                ResultSet rs;
                boolean flag= false;
                try {
                    rs = f_main.con.createStatement().executeQuery(query);
                    flag= rs.next();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }

                // No manager for this hostel
                if (!flag) {
                    query = "SELECT * From manager_accounts WHERE userName= '" + temp2 + "'";
                    try {
                        rs = f_main.con.createStatement().executeQuery(query);
                        flag= rs.next();
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                    // new UserName
                    if (!flag) {
                        query= "CREATE TABLE `student`.`"+ temp1+"` (`Name` VARCHAR(45) NOT NULL,`reg` VARCHAR(45) NOT NULL,`hostelName` VARCHAR(70) NOT NULL,`roomNo` VARCHAR(20) NOT NULL,`mobNo` VARCHAR(45) NOT NULL,`email` VARCHAR(45) NOT NULL,`pass` VARCHAR(45) NOT NULL, UNIQUE INDEX `userName_UNIQUE` (`reg` ASC) VISIBLE, PRIMARY KEY (`reg`))";
                        try {
                            f_main.conStud.createStatement().execute(query);
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }

                        query= "CREATE TABLE `slots`.`"+temp1+"` (`reg` VARCHAR(20) NOT NULL,`slot` VARCHAR(10) NOT NULL,`seat` VARCHAR(10) NOT NULL,`checkin` VARCHAR(10) NOT NULL,`checkout` VARCHAR(10) NOT NULL, PRIMARY KEY (`reg`),UNIQUE INDEX `reg_UNIQUE` (`reg` ASC) VISIBLE)";
                        try {
                            f_main.conSlot.createStatement().execute(query);
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }

                        query= "CREATE TABLE `vacant`.`"+temp1+"` (`slotNo` VARCHAR(20) NOT NULL,`Vacant` VARCHAR(20) NOT NULL, UNIQUE INDEX `slotNo_UNIQUE` (`slotNo` ASC))";
                        try {
                            f_main.conVacant.createStatement().execute(query);
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }

                        query = "INSERT INTO manager_accounts VALUES (" + "'" + temp2 + "'" + "," + "'" + temp3 + "'" + "," + "'" + temp1 + "'" + "," + "'" + temp1 + "'"+ ",'0','0','0$0$0','0','0','0','0','0'"+")";
                        try {
                            f_main.con.prepareStatement(query).execute(query);
                            dos.writeUTF("2");//Account created Successfully
                            dos.flush();
                        } catch (Exception t) {
                            t.printStackTrace();
                        }
                    } else {
                        try {
                            dos.writeUTF("1");// user already present with this userName
                            dos.flush();
                        } catch (IOException e9) {
                            e9.printStackTrace();
                        }
                    }
                } else {
                    try {
                        dos.writeUTF("0");// MessManager for this mess already present
                        dos.flush();
                    } catch (IOException e10) {
                        e10.printStackTrace();
                    }
                }
            }
            //for signIN
            else if(temp1.equals("1")){
                String str= "";
                try {
                    temp2 = dis.readUTF();// userName
                    temp3 = dis.readUTF();// password
                    temp1= dis.readUTF();//0->admin or 1->student
                    if(temp1.equals("1")){
                        str= dis.readUTF();
                    }
                } catch (IOException e11) {
                    e11.printStackTrace();
                }
                ResultSet rs;

                //means student
                if(temp1.equals("1")){
                    query= "SELECT * FROM "+str+" WHERE reg= '"+temp2+"'"+" AND pass= '"+temp3+ "'";
                    try {
                        rs = f_main.conStud.createStatement().executeQuery(query);
                        if(!rs.next())
                        {
                            dos.writeUTF("0");// Either username or password wrong OR no user with this info
                            dos.flush();
                        }
                        else{
                            dos.writeUTF("1");// successfully loggedIN
                            dos.flush();
                            while(true){
                                temp1= dis.readUTF();
                                if(temp1.equals("0")) //page cut
                                    break;
                                switch (temp1){
                                    case "1":   //for profile Student
                                        //proStud(rs);
                                        break;
                                    case "2":   //for booking status
                                        break;
                                }

                            }
                        }

                    } catch (Exception e6) {
                        try {
                            dos.writeUTF("0");// Either username or password wrong OR no user with this info
                            dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //e6.printStackTrace();
                    }
                }
                //means admin
                else{
                    query = "SELECT * FROM manager_accounts WHERE userName= '"+temp2+"'"+" AND password= '"+temp3+ "'";
                    try {
                        rs = f_main.con.createStatement().executeQuery(query);
                        if(!rs.next())
                        {
                            dos.writeUTF("0");// Either username or password wrong OR no user with this info
                            dos.flush();
                        }
                        else{
                            dos.writeUTF("1");// successfully loggedIN
                            dos.flush();
                            while(true){
                                temp1= dis.readUTF();
                                if(temp1.equals("0")) //page cut
                                    break;
                                switch(temp1){
                                    case "1"://profile
                                        //proAd();
                                        break;
                                    case "2"://checkIn
                                        checkIn(rs);
                                        break;
                                    case "3"://checkOut
                                        checkOut(rs);
                                        break;
                                    case "4"://Present Status
                                        status(rs);
                                        break;
                                    case "5"://Seat Allotment
                                        try{allotment(rs);}
                                        catch(Exception e){e.printStackTrace();}
                                        break;
                                    case "6"://New Reg Student
                                        try{studList(rs);}
                                        catch(Exception e){e.printStackTrace();}
                                        //reStud();
                                        break;
                                    case "7"://Mess Structure
                                        mesStruct(rs);
                                        break;
                                }
                            }
                        }
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
            }
            else{
                try {
                    vSkt.close();
                    dis.close();
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    void status(ResultSet rs)throws IOException,SQLException{
        int rows=Integer.parseInt(rs.getString(5));
        int cols=Integer.parseInt(rs.getString(6));
        StringTokenizer st_Token = new StringTokenizer(rs.getString(7), "$");
        LocalTime lt= LocalTime.now();
        LocalTime lt1= LocalTime.of(Integer.parseInt(st_Token.nextToken()),Integer.parseInt(st_Token.nextToken()),0);
        long min= ChronoUnit.MINUTES.between(lt,lt1);
        Math.ceil((double)min/)

        int presentSlot= 1;
        dos.writeUTF(rows+"$"+cols+"$"+presentSlot);
        dos.flush();

        String temp = "select * from slots." + rs.getString(3) + " where slot = '"+ presentSlot + "'";// and checkin = '1' and checkout= '1'";
        ResultSet rf = f_main.conStud.createStatement().executeQuery(temp);
        while (rf.next()){
            String t1=rf.getString(1);
            String t2=rf.getString(3);
            String t3=rf.getString(4);
            String t4=rf.getString(5);
            String t5=t1+"$"+t2+"$"+t3+"$"+t4;
            dos.writeUTF(t5);
            dos.flush();
        }
        dos.writeUTF("0");
        dos.flush();

        String temp1= dis.readUTF(); //reg
        if(!temp1.equals("0")) {
            String temp2 = "select * from student." + rs.getString(3) + " where reg= '" + temp1 + "'";
            ResultSet rc = f_main.conStud.createStatement().executeQuery(temp2);
            rc.next();
            dos.writeUTF(rc.getString(1) + "$" + rc.getString(3) + "$" + rc.getString(4) + "$" + rc.getString(5) + "$" + rc.getString(6));
            dos.flush();
        }
    }

    void allotment(ResultSet rs)throws IOException, SQLException{
        String flag= dis.readUTF();
        if(flag.equals("0"))
            return;
        int r= Integer.parseInt(rs.getString(5));
        int c= Integer.parseInt(rs.getString(6));
        int s= Integer.parseInt(rs.getString(10));
        String p= Integer.toString(r*c);
        String query;
        for(int i= 1;i<=s;i++) {
            query = "INSERT INTO `vacant`.`" + rs.getString(3) + "` (`slotNo`, `Vacant`) VALUES ('" + i + "', '" + p + "')";
            f_main.conVacant.createStatement().executeUpdate(query);
        }
        giveSlot(rs,r,c);
    }

    void giveSlot(ResultSet rc, int r, int c) throws SQLException {
        ResultSet rr;
        String hos= rc.getString(3);
        String sql = "SELECT * FROM vacant." + hos;
        ResultSet rs = f_main.conVacant.createStatement().executeQuery(sql);
        sql = "select * from slots." + hos + " where slot= '" + 0 + "'";
        rr = f_main.conSlot.prepareStatement(sql).executeQuery(sql);
        while(rs.next()) {
            int sl, st, seatNo = 0;
            sl = Integer.parseInt(rs.getString(1));
            st = Integer.parseInt(rs.getString(2));
            int j= st;
            for (int i = 1; i <= st; i++) {
                if (rr.next()) {
                    seatNo = (r * c - j) * 2; //50 to be replaced by total
                    if (sl % 2 == 1)
                        seatNo += 1;
                    else
                        seatNo += 2;
                    j= j-1;
                    sql = "UPDATE `slots`.`" + hos + "` SET `slot` = '" + sl + "', `seat` = '" + seatNo + "' WHERE (`reg` = '" + rr.getString(1) + "')";
                    f_main.conSlot.createStatement().executeUpdate(sql);
                } else
                    break;
            }
        }
    }

    void studList(ResultSet rs) throws IOException,SQLException{
        String q= "select Name ,reg from student."+rs.getString(3);
        ResultSet f= f_main.conStud.createStatement().executeQuery(q);
        while(f.next()){
            dos.writeUTF(f.getString(2) +"$" +f.getString(1));
            dos.flush();
        }
        dos.writeUTF("0");
        dos.flush();

        while(true){
            String temp= dis.readUTF();
            if(temp.equals("0"))
                break;
            else if(temp.equals("1")){
                reStud();
            }
            else if(temp.equals("2")){
                String temp1= dis.readUTF(); // reg or name
                String temp2= "select * from student."+rs.getString(3)+" where (Name= '"+temp1+"' or reg= '"+temp1+"')";
                ResultSet rc= f_main.conStud.createStatement().executeQuery(temp2);
                while(rc.next()){
                    dos.writeUTF(rc.getString(2) +"$" +rc.getString(1));
                    dos.flush();
                }
                dos.writeUTF("0");
                dos.flush();
            }
            else{
                dialog(rs.getString(3));
            }
        }
    }

    void dialog(String hos) throws IOException, SQLException{
        String temp1= dis.readUTF(); //reg
        String  temp2= "select * from student."+hos +" where reg= '"+temp1+"'";
        ResultSet rc= f_main.conStud.createStatement().executeQuery(temp2);
        rc.next();
        dos.writeUTF(rc.getString(1)+"$"+ rc.getString(3)+"$"+rc.getString(4)+"$"+rc.getString(5)+"$"+rc.getString(6)+"$"+rc.getString(7));
        dos.flush();
        temp1= dis.readUTF(); //1->del 0->cut page
        if(temp1.equals("1")){
            String str= dis.readUTF(); //reg
            String temp= "DELETE FROM `slots`.`"+hos+"` WHERE (`reg` = '"+str+"')";
            f_main.conSlot.createStatement().executeUpdate(temp);
            temp= "DELETE FROM `student`.`"+hos+"` WHERE (`reg` = '"+str+"')";
            f_main.conStud.createStatement().executeUpdate(temp);
        }
    }

    void checkOut(ResultSet rs){
        try{
            String temp1= dis.readUTF(); //1->checkin kro 0->cut page
            if(temp1.equals("1")){
                temp1= dis.readUTF(); //reg reading
                String query= "UPDATE `slots`.`"+rs.getString(3)+"` SET `checkout` = '1' WHERE (`reg` = '"+temp1+"')";
                f_main.conSlot.createStatement().executeUpdate(query);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void checkIn(ResultSet rs){
        try{
            String temp1= dis.readUTF(); //1->checkin kro 0->cut page
            if(temp1.equals("1")){
                temp1= dis.readUTF(); //reg reading
                String query= "UPDATE `slots`.`"+rs.getString(3)+"` SET `checkin` = '1' WHERE (`reg` = '"+temp1+"')";
                f_main.conSlot.createStatement().executeUpdate(query);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void mesStruct(ResultSet rs){
        String str= null;
        try{
            ResultSet rs1= null;
            str= "SELECT * FROM manager_accounts WHERE userName= '"+rs.getString(1)+"'"+" AND password= '"+rs.getString(2)+ "'";
            //System.out.println("kamal2");
            rs1= f_main.con.prepareStatement(str).executeQuery(str);
            rs1.next();
            dos.writeUTF(rs1.getString(5)+"$"+rs1.getString(6)+"$"+rs1.getString(7)+"$"+rs1.getString(8)+"$"+rs1.getString(9)+"$"+rs1.getString(12));
            dos.flush();
        }
        catch(Exception e){
            //System.out.println("kamal1");
            e.printStackTrace();
        }
        while(true){
            try {
                str = dis.readUTF();       //1->set 0->cut page
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(str.equals("1")){ //set
                    str= dis.readUTF();
                    StringTokenizer st_Token = new StringTokenizer(str, "#");
                    str= "UPDATE `messaccounts`.`manager_accounts` SET `rows` = '"+ st_Token.nextToken()+"', `cols` = '"+st_Token.nextToken()+"', `messTime` = '"+st_Token.nextToken()+"', `messtotTime` = '"+st_Token.nextToken()+"', `gap` = '"+ st_Token.nextToken()+"', `totSlots` ='"+st_Token.nextToken()+"', `timeForSlot` ='"+st_Token.nextToken()+"', `totStud` ='"+st_Token.nextToken()+"' WHERE (`userName` = '"+ rs.getString(1)+"')";
                    f_main.con.prepareStatement(str).executeUpdate(str);
                    dos.writeUTF("1");
                    dos.flush();
                }
                else{
                    break;
                }
            }
            catch (Exception e){
                try {
                    dos.writeUTF("0");
                    dos.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
        }
//        try {
//            rs1.close();
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
    }

    void reStud() {
        while (true) {
            try {
                String temp1 = dis.readUTF();
                if (temp1.equals("1")) { //create
                    temp1 = dis.readUTF();
                    StringTokenizer st_Token = new StringTokenizer(temp1, "$");
                    String bit[] = {st_Token.nextToken(), st_Token.nextToken(), st_Token.nextToken(), st_Token.nextToken(), st_Token.nextToken(), st_Token.nextToken(), st_Token.nextToken()};
                    temp1 = "INSERT INTO " + bit[4] + " VALUES (" + "'" + bit[6] + "'" + "," + "'" + bit[5] + "'" + "," + "'" + bit[4] + "'" + "," + "'" + bit[3] + "'" + "," + "'" + bit[2] + "'" + "," + "'" + bit[1] + "'" + "," + "'" + bit[0] + "'" + ")";
                    f_main.conStud.prepareStatement(temp1).execute(temp1);
                    temp1="INSERT INTO `slots`.`"+bit[4]+"` (`reg`, `slot`, `seat`, `checkin`, `checkout`) VALUES ('"+bit[5]+"', '0', '0', '0', '0')";
                    f_main.conSlot.prepareStatement(temp1).execute(temp1);
                    dos.writeUTF("1");// created
                    dos.flush();
                } else {
                    break;
                }
            } catch (Exception e) {
                try{
                    dos.writeUTF("0");
                    dos.flush();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
    public loginClass(Socket s){
        this.vSkt= s;
        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        }
        catch(Exception e4){
            dis= null;
            dos= null;
        }
    }
}

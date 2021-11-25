import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;

public class EchoServer implements Runnable {
    Socket socket;
    BufferedReader bfr;
    PrintWriter writer;

    public EchoServer(Socket socket) {
        this.socket = socket;
        try {
            this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer =  new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {  //Write all the code and logic here
        while (true) {
            try {
                String lineRead = bfr.readLine();  //WHAT DOES THE PERSON WANT TO DO
                /**
                 * IF USER WANTS TO CREATE ACCOUNT
                 */
                if (lineRead.equals("createAccount")) {
                    String userName = bfr.readLine();
                    String userEmail = bfr.readLine();
                    System.out.println(userName + userEmail);
                    //Make the People object
                }
            } catch (Exception e) {
                //e.printStackTrace();
                break;
            }
        }
    }
}

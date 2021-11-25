import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;

public class LmsMain {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        if (getPeopleListInFile() == null) {
            ArrayList<People> newArrayList = new ArrayList<>();
            updatePeopleListInFile(newArrayList);
        }

        while (true) {
            Socket socket = serverSocket.accept();
            EchoServer server = new EchoServer(socket);
            new Thread(server).start();
            System.out.println("Connection made");
        }
    }




















    public static ArrayList<People> getPeopleListInFile() {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("People.txt"))) {
            Object o = oos.readObject();
            if (o == null) {
                return null;
            }
            ArrayList<People> thePeople = (ArrayList<People>) o;
            return thePeople;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method puts the new People arrayList into People.txt and saves it
     * @param newArrayList
     */
    public static void updatePeopleListInFile(ArrayList<People> newArrayList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("People.txt"))) {
            oos.writeObject(newArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

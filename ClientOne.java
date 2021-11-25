import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.*;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.awt.*;


public class ClientOne {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            JFrame startMenu = new JFrame("Start Menu");
            Container startContent = startMenu.getContentPane();
            startContent.setLayout(new BorderLayout());
            startMenu.setSize(500,500);
            startMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel startJpanel = new JPanel();

            JButton createAccBut = new JButton("Create Account");
            startJpanel.add(createAccBut, BorderLayout.CENTER);

            /**
             * WHAT HAPPENS GUI WISE WHEN USER CREATES ACCOUNT
             */
            ActionListener createAccListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == createAccBut) {
                        String name = JOptionPane.showInputDialog(null, "Enter name", "Enter name", JOptionPane.QUESTION_MESSAGE);
                        String email = "";
                        boolean emailExists = false;
                        while (true) {
                            email = JOptionPane.showInputDialog(null, "Enter email", "Enter email", JOptionPane.QUESTION_MESSAGE);
                            for (People p: getPeopleListInFile()) {
                                emailExists = true;
                                break;
                            }
                            if (emailExists) {
                                JOptionPane.showMessageDialog(null, "Email already used", "Email already used", JOptionPane.ERROR_MESSAGE);
                            } else {
                                break;
                            }
                        }
                        writer.write("createAccount");
                        writer.println();
                        writer.flush();
                        writer.write(name);
                        writer.println();
                        writer.flush();
                        writer.write(email);
                        writer.println();
                        writer.flush();
                    }
                }
            };
            createAccBut.addActionListener(createAccListener);
            startContent.add(startJpanel, BorderLayout.CENTER);
            startMenu.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
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
}

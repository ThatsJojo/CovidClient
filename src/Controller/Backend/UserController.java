package Controller.Backend;

import Controller.Frontend.FXMLDocumentController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TCPClient;
import model.User;
import util.DuplicatedUserException;

public class UserController {

    private static HashMap<String, User> USERS;
    private static TCPClient tcpClient;

    public static void stopConnection(User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private FXMLDocumentController tela;

    public UserController(FXMLDocumentController tela, HashMap<String, User> USERS) {
        this.USERS = USERS;
        this.tela = tela;
    }

    public void configReceiver(String ip, int port) {
        tcpClient = TCPClient.getIstance(ip, port, this);

    }

    public User createUser(String key, String name, String age, String sex, FXMLDocumentController listenner) throws DuplicatedUserException {
        User user = new User(key, name, age, sex, tela);
        USERS.put(key, user);
        return USERS.get(key);
    }

    public static void killAllUsersKonnections() {
        tcpClient.stopTask();
        tcpClient.interrupt();
    }

    public static User removeUser(String key) {
        return USERS.remove(key);
    }

    public void startConnection(String ip, int port) {
        configReceiver(ip, port);
        tcpClient.start();
    }

    public static void sendAllert(String key, String message) {
        try {
            tcpClient.sendAllert(key, message);
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void attUsers(LinkedList<String> usersArray) {
        if (usersArray == null) {
            return;
        }
        if (usersArray.size() < 2) {
            return;
        }
        Iterator it = usersArray.iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            if (s != null) {
                String[] data = s.split(":");
                if (data.length == 2) {
                    String key = data[0];
                    data = data[1].split(",");
                    User u = new User(key, data[0], data[1].replace('&', ' '), data[2], tela);
                    USERS.put(key, u);
                }
            }
        }
        tela.attUSERS(USERS);
    }
}

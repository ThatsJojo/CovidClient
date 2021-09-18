package model;

import Controller.Backend.UserController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Thread {

    private final int port;
    private final String ip;
    private final UserController myController;
    private boolean connected;
    private static TCPClient tcpClient;

    public static TCPClient getIstance(String ip, int port, UserController myController) {
        if (tcpClient == null) {
            tcpClient = new TCPClient(ip, port, myController);
        }
        return tcpClient;
    }
    
    public static TCPClient getInstance(){
        return tcpClient;
    }

    private TCPClient(String ip, int port, UserController myController) {
        this.ip = ip;
        this.port = port;
        this.myController = myController;
        connected = false;
    }

    public LinkedList<String> getUser(String user) throws IOException {
        return sendRequest("GET /user/" + user + " HTTP/1.1\r\n\r\n");
    }

    public LinkedList<String> getUsers() throws IOException {
        return sendRequest("GET / HTTP/1.1\r\n\r\n");
    }

    public LinkedList<String> sendAllert(String userKey, String message) throws IOException {
        return sendRequest("GET /sendAllert/" + userKey + " HTTP/1.1\r\n" + "message:" + message + "\r\n\r\n");
    }

    private LinkedList<String> sendRequest(String request) throws IOException {
        Socket socket = new Socket(ip, port);
        if (socket.isConnected()) {
            System.out.println("Cliente conectado a " + socket.getInetAddress());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(request);
        writer.flush();
        LinkedList<String> linhas = new LinkedList();
        String linha = reader.readLine();
        
        while (linha != null && !linha.isEmpty()) {
            linha = reader.readLine();
            linhas.add(linha);
        }
        //System.out.println(linhas);
        return linhas;

    }

    @Override
    public void run() {
        connected = true;
        while (connected) {
            try {
                myController.attUsers(tcpClient.getUsers());
            } catch (IOException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public void stopTask() {
        connected = false;
    }

}

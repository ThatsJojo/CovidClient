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

    /**
     * Recebe o Client caso saiba as informações de Conexão.
     * @param ip Endereço para conexão com a API.
     * @param port Porta para conexão com a API.
     * @param myController Controllador de Usuários.
     * @return Instância única para o Client.
     */
    public static TCPClient getIstance(String ip, int port, UserController myController) {
        if (tcpClient == null) {
            tcpClient = new TCPClient(ip, port, myController);
        }
        return tcpClient;
    }
    
    /**
     * Recebe o Client sem saber as informações de conexão.
     * @return 
     */
    public static TCPClient getInstance(){
        return tcpClient;
    }

    /**
     * Cliente que se conecta com a APIRest.
     * @param ip Endereço IP para a conexão com a API.
     * @param port Porta para a conexão com a API.
     * @param myController Controllador de Usuários.
     */
    private TCPClient(String ip, int port, UserController myController) {
        this.ip = ip;
        this.port = port;
        this.myController = myController;
        connected = false;
    }

    public LinkedList<String> getUser(String user) throws IOException {
        return sendRequest("GET /user/" + user + " HTTP/1.1\r\n\r\n");
    }

    /**
     * Requisita à APIRest as informações gerais dos usuários conectados.
     * @return Lista com as linhas referentes à resposta da APIRest.
     * @throws IOException Caso a Request não seja realizada com sucesso.
     */
    public LinkedList<String> getUsers() throws IOException {
        return sendRequest("GET / HTTP/1.1\r\n\r\n");
    }

    /**
     * Envia uma mensagem via APIRest.
     * @param userKey Chave do usuário.
     * @param message Mensagem a ser enviada.
     * @return Resposta da APIRest.
     * @throws IOException Caso a Request não seja realizada com sucesso.
     */
    public LinkedList<String> sendAllert(String userKey, String message) throws IOException {
        return sendRequest("PUT /sendAllert/" + userKey + " HTTP/1.1\r\n" + "message:" + message + "\r\n\r\n");
    }

    /**
     * Envia uma request à APIRest
     * @param request
     * @return
     * @throws IOException 
     */
    private LinkedList<String> sendRequest(String request) throws IOException {
        Socket socket = new Socket(ip, port);
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
        return linhas;

    }

    /**
     * Enquanto @connected == true, atualiza as informações dos usuários logados.
     */
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

    /**
     * Finaliza a tarefa em execução.
     */
    public void stopTask() {
        connected = false;
    }

}

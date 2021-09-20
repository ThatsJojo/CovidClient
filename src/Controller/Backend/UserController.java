package Controller.Backend;

import Controller.Frontend.FXMLDocumentController;
import java.io.IOException;
import java.util.HashMap;
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
        currentUser.stopAtt();
    }
    private final FXMLDocumentController tela;

    /**
     * Controllador das funções relacionadas às informações dos usuários.
     * @param tela Tela em que as informações estão sendo exibidas.
     * @param USERS HashMap que guarda os Usuários e os mapeia pela chave.
     */
    public UserController(FXMLDocumentController tela, HashMap<String, User> USERS) {
        UserController.USERS = USERS;
        this.tela = tela;
    }

    /**
     * Configura a conexão do receptor TCP.
     * @param ip Endereço ip para conexão da API.
     * @param port Porta para conexão da API.
     */
    public void configReceiver(String ip, int port) {
        tcpClient = TCPClient.getIstance(ip, port, this);

    }

    /**
     * Cria um usuário.
     * @param key chave do usuário.
     * @param name nome do usuário.
     * @param age idade do usuário.
     * @param sex sexo do usuário.
     * @param listenner Tela em que os dados são exibidos.
     * @return o usuário criado.
     * @throws DuplicatedUserException  Caso o usuário já tenha sido adicionado.
     */
    public User createUser(String key, String name, String age, String sex, FXMLDocumentController listenner) throws DuplicatedUserException {
        User user = new User(key, name, age, sex, tela);
        USERS.put(key, user);
        return USERS.get(key);
    }

    /**
     * Para a conexão de todos os usuários.
     */
    public static void killAllUsersKonnections() {
        if(tcpClient == null)
            return;
        tcpClient.stopTask();
        tcpClient.interrupt();
    }

    /**
     * Remove um usuário da lista de usuários conectados. Este método excluirá o usuário
     * apenas se ele não mais enviar dados. Caso o usuário continue enviando, ele será
     * adicionado novamente.
     * @param key
     * @return 
     */
    public static User removeUser(String key) {
        return USERS.remove(key);
    }

    /**
     * Inicia a conexão com a API.
     * @param ip Endereço ip para conexão com a API.
     * @param port Porta para conexãocom a API.
     */
    public void startConnection(String ip, int port) {
        configReceiver(ip, port);
        tcpClient.start();
    }

    /**
     * Envia uma mensagem para o usuário.
     * @param key Chave para o usuário.
     * @param message Mensagem a ser enviada.
     */
    public static void sendAllert(String key, String message) {
        try {
            tcpClient.sendAllert(key, message);
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Atualiza o array de usuários logados. Chama o método que atualiza a tela com esses usuários.
     * @param usersList Lista com os usuários recebidos.
     */
    public void attUsers(LinkedList<String> usersList) {
        if (usersList == null) {
            return;
        }
        if (usersList.size() < 2) {
            return;
        }
        usersList.stream().filter((s) -> (s != null)).map((s) -> s.split(":")).filter((data) -> (data.length == 2)).forEachOrdered((data) -> {
            String key = data[0];
            data = data[1].split(",");
            User u = new User(key, data[0].replace('&', ' '), data[1], data[2], tela);
            USERS.put(key, u);
        });
        tela.attUSERS(USERS);
    }
}

package Controller.Frontend;

import Controller.Backend.UserController;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;

/**
 *
 * @author Cleyton
 */
public class FXMLDocumentController implements Initializable {

    private Tab connectedUser;
    private ObservableList<Tab> tabs;
    private HashMap<Tab, User> tabsXusers;
    private Tab currentTab;
    private User currentUser;
    private static Stage myStage;
    private Node content;
    @FXML
    private TabPane TabPaneUsers;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserAge;
    @FXML
    private Label lblUserSex;
    @FXML
    private Label lblConectado;
    @FXML
    private Button btnStartSend;
    @FXML
    private Label lblUserIP;
    @FXML
    private Label lblUserPort;
    @FXML
    private Label lblUserKey;
    @FXML
    private Label userHeartRate;
    @FXML
    private Label userBreathingRate;
    @FXML
    private Label userOxygenSaturation;
    @FXML
    private Label userSystolicBloodPressure;
    @FXML
    private Label userDiastolicBloodPressure;
    @FXML
    private Tab tabLogin;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField userKey;
    @FXML
    private TextField userIP;
    @FXML
    private TextField userPort;
    @FXML
    private Label lblAllerts;
    @FXML
    private DialogPane AllertUser;
    @FXML
    private Pane AllertConfigPane;
    private TextField allertIP;
    private TextField AllertPort;
    @FXML
    private Button btnAllertSave;
    @FXML
    private Label lblAllertError;
    @FXML
    private ComboBox<String> boxPacient;
    @FXML
    private Label lblConnected;
    @FXML
    private Button btnLookAtHim;
    @FXML
    private Label userTemperature;
    @FXML
    private TextField allertMessage;
    private HashMap<String, User> users;
    private HashMap<String, User> auxMap = new HashMap();
    private UserController userController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        users = new HashMap<>();
        userController = new UserController(this, users);
        myStage.onCloseRequestProperty().set((EventHandler) (Event event) -> {
            UserController.killAllUsersKonnections();
        });
        tabs = TabPaneUsers.getTabs();
        connectedUser = tabs.get(0);
        content = connectedUser.getContent();
        tabs.remove(0);
        tabsXusers = new HashMap<>();
        myStage.getIcons().add(new Image("/resources/icons/coronavirus.png"));
        AllertUser.lookupButton(ButtonType.CLOSE).addEventHandler(ActionEvent.ACTION, (event2) -> {
            TabPaneUsers.setDisable(false);
            AllertUser.setVisible(false);
            AllertUser.setDisable(true);
        });
        AllertUser.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        AllertConfigPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    @FXML
    private void handleBtnStartSend(ActionEvent event) {
        TabPaneUsers.setDisable(true);
        AllertConfigPane.setVisible(true);
    }

    @FXML
    private void handleBtnStartConnection(ActionEvent event) {
        int port = Integer.parseInt(userPort.getText());
        String ip = userIP.getText();
        if (port > 60000 || port < 1025) {
            userPort.setText("Porta Inválida");
        } else {
            userController.startConnection(ip, port);
            btnLogin.setDisable(true);
            lblConnected.setVisible(true);
            boxPacient.setVisible(true);
            btnLookAtHim.setVisible(true);
        }
    }

    @FXML
    private void userTabChanged(Event event) {
        Tab t = (Tab) event.getTarget();
        currentTab = t;
        if (t.getText().equals("V^Ja3?UGc*qp<D&Nky=9!n4Hr]MV,)/n&s$hxd@SMhJ}6X9qPjUd?Tb/mS$B/7kuD,K,!mN3C,j{*F~A8U*nXmD:*XWE%~nEPfqdYK#k*pT$(!b<&nzfS$rjz-tKP?^.")) {
            return;
        }
        String name = t.getText();
        if (t.isSelected()) {
            t.setContent(content);
            User user = tabsXusers.get(t);
            currentUser = user;
            currentUser.att();
            lblUserName.setText(user.getName());
            lblUserAge.setText(user.getAge());
            lblUserSex.setText(user.getSex());
            lblUserKey.setText(user.getKey());
            userBreathingRate.setText(user.getBreathingRate());
            userSystolicBloodPressure.setText(user.getSystolicBloodPressure());
            userDiastolicBloodPressure.setText(user.getDiastolicBloodPressure());
            userHeartRate.setText(user.getHeartRate());
            userOxygenSaturation.setText(user.getOxygenSaturation());
            userTemperature.setText(user.getTemperature());
        } else {
            t.setContent(null);
            currentTab = tabLogin;
            currentUser.stopAtt();
        }
    }

    public static Stage getMyStage() {
        return myStage;
    }

    public static void setMyStage(Stage myStage) {
        FXMLDocumentController.myStage = myStage;
    }

    public void userTemperatureOnChange(String txt) {
        Platform.runLater(() -> {
            userTemperature.setText(txt);
        });
    }

    public void userBreathingRateOnChange(String txt) {
        Platform.runLater(() -> {
            userBreathingRate.setText(txt);
        });
    }

    public void userHeartRateOnChange(String txt) {
        Platform.runLater(() -> {
            userHeartRate.setText(txt);
        });
    }

    public void userOxygenSaturationOnChange(String txt) {
        Platform.runLater(() -> {
            userOxygenSaturation.setText(txt);
        });
    }

    public void userSystolicBloodPressureOnChange(String txt) {
        Platform.runLater(() -> {
            userSystolicBloodPressure.setText(txt);
        });
    }

    public void userDiastolicBloodPressureOnChange(String txt) {
        Platform.runLater(() -> {
            userDiastolicBloodPressure.setText(txt);
        });
    }

    public void pisca(User user) {
        if (!currentUser.equals(user)) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                lblConectado.setTextFill(Color.SPRINGGREEN);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    lblConectado.setTextFill(Color.BLACK);
                }
                lblConectado.setTextFill(Color.BLACK);
            }
        }.start();
    }

    private void changeUsers(HashMap<String, User> users) {
        this.users = users;
    }

    @FXML
    private void btnChangeUser(ActionEvent event) {
        String u = (String) boxPacient.getValue();
        User user = auxMap.get(u);
        currentUser = user;
        String key = userKey.getText();
        Tab tab = new Tab();
        tabsXusers.put(tab, user);
        tab.setText(user.getKey());
        tab.setOnSelectionChanged((Event event1) -> {
            userTabChanged(event1);
        });
        tab.setOnClosed((Event event1) -> {
            stopConnection(event1);
        });
        tabs.add(tabs.size() - 1, tab);
    }

    @FXML
    private void stopConnection(Event event) {
        UserController.stopConnection(currentUser);
    }

    @FXML
    private void OnallertSend(ActionEvent event) {
        String messsage = allertMessage.getText();
        if (!messsage.equals("")) {
            TabPaneUsers.setDisable(false);
            AllertConfigPane.setVisible(false);
            String message = "";
            UserController.sendAllert(currentUser.getKey(), message);
            System.out.println("SAIU");
        }
    }

    public void attUSERS(HashMap<String, User> USERS) {
        ArrayList<String> tempArray = new ArrayList();

        USERS.forEach((t, u) -> {
            String st = "" + u.getKey() + " - " + u.getName() + " " + u.getAge() + " " + u.getSex();
            auxMap.put(st, u);
            tempArray.add(st);
        });
        Platform.runLater(() -> {
            boxPacient.setItems(FXCollections.observableArrayList(tempArray));
        });
    }
};

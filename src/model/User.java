package model;

import Controller.Frontend.FXMLDocumentController;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class User {

    private final String key;
    private String name;
    private String age;
    private String sex;
    private String temperature;
    private String breathingRate;
    private String heartRate;
    private String oxygenSaturation;
    private String systolicBloodPressure;
    private String diastolicBloodPressure;
    private final FXMLDocumentController listenner;
    private String allertException;
    private String allertString;
    private boolean hasUnseenAllert;
    private Thread att;
    private boolean atting;
    private String date;
    private String time;

    /**
     * Cria um usuário.
     * @param key Chave do usuário.
     * @param name Nome do usuário.
     * @param age Idade do usuário.
     * @param sex Sexo do usuário.
     * @param listenner Tela em que as informações do usuário serão exibidas.
     */
    public User(String key, String name, String age, String sex, FXMLDocumentController listenner) {
        hasUnseenAllert = false;
        this.key = key;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.listenner = listenner;
        temperature = "36 ºC";
        oxygenSaturation = "96";
        breathingRate = "12 ipm";
        heartRate = "60 bpm";
        systolicBloodPressure = "12 mmHg";
        diastolicBloodPressure = "8 mmHg";
        date = "null";
        time = "null";
    }

    /**
     * Atualiza o nome do usuário na classe e na tela.
     * @param name 
     */
    public void setName(String name) {
        listenner.userNameOnChange(name);
        this.name = name;
    }

    public void setAge(String age) {
        listenner.userAgeOnChange(age);
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        listenner.userDateOnChange(date);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        listenner.userTimeOnChange(time);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        listenner.userSexOnChange(sex);
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getKey() {
        return key;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
        listenner.userTemperatureOnChange(temperature);
    }

    public String getBreathingRate() {
        return breathingRate;
    }

    public void setBreathingRate(String breathingRate) {
        this.breathingRate = breathingRate;
        listenner.userBreathingRateOnChange(breathingRate);
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
        listenner.userHeartRateOnChange(heartRate);
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
        listenner.userOxygenSaturationOnChange(oxygenSaturation);
    }

    public String getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(String systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
        listenner.userSystolicBloodPressureOnChange(systolicBloodPressure);
    }

    public String getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(String diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
        listenner.userDiastolicBloodPressureOnChange(diastolicBloodPressure);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.key);
        return hash;
    }

    public String getAllertException() {
        return allertException;
    }

    public void setAllertException(String allertException) {
        this.allertException = allertException;
    }

    public String getAllertString() {
        return allertString;
    }

    public void setAllertString(String allertString) {
        this.allertString = allertString;
    }

    public boolean isHasUnseenAllert() {
        return hasUnseenAllert;
    }

    public void setHasUnseenAllert(boolean hasUnseenAllert) {
        this.hasUnseenAllert = hasUnseenAllert;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    /**
     * Organiza as informações do usuário.
     * @return Array de bytes para as informações do usuário.
     */
    byte[] getData() {
        String data = "key: " + key + "\n"
                + "name: " + name + "\n"
                + "age: " + age + "\n"
                + "sex: " + sex + "\n"
                + "temperature: " + temperature + "\n"
                + "xygenSaturation: " + oxygenSaturation + "\n"
                + "oxygenSaturation: " + breathingRate + "\n"
                + "heartRate: " + heartRate + "\n"
                + "systolicBloodPressure: " + systolicBloodPressure + "\n"
                + "diastolicBloodPressure: " + diastolicBloodPressure + "   \n";
        return data.getBytes();
    }

    /**
     * Avisa à tela que um pacote foi recebido para o usuário atual.
     */
    public void sentAllert() {
        this.listenner.pisca(this);
    }

    /**
     * Enquanto solicitado para atualização, requisita da API as informações atuais do usuário.
     * E envia para a tela.
     */
    public void att() {
        atting = true;
        User u = this;
        att = new Thread() {
            @Override
            public void run() {
                while (atting) {
                    try {
                        Thread.sleep(200);
                        if (!atting) {
                            throw new InterruptedException();
                        }
                        LinkedList<String> data = TCPClient.getInstance().getUser(key);
                        Iterator it = data.iterator();
                        it.next();
                        u.setName((String) it.next());
                        u.setAge((String) it.next());
                        u.setSex((String) it.next());
                        u.setTemperature((String) it.next());
                        u.setBreathingRate((String) it.next());
                        u.setHeartRate((String) it.next());
                        u.setOxygenSaturation((String) it.next());
                        u.setSystolicBloodPressure((String) it.next());
                        u.setDiastolicBloodPressure((String) it.next());
                        u.setDate((String) it.next());
                        u.setTime((String) it.next());
                        u.listenner.pisca(u);
                    } catch (IOException | InterruptedException ex) {
                        System.out.println("Parando de ouvir " + u.key);
                    }

                }
            }
        };
        att.start();
    }

    /**
     * Para de atualizar as informações do usuário.
     */
    public void stopAtt() {
        atting = false;
        att.interrupt();
    }

}

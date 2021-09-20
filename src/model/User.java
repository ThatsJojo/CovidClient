package model;

import Controller.Frontend.FXMLDocumentController;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public User(String key, String name, String age, String sex, FXMLDocumentController listenner) {
        hasUnseenAllert = false;
        this.key = key;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.listenner = listenner;
        temperature = "36 ºC";
        oxygenSaturation = "96 %";
        breathingRate = "12 ipm";
        heartRate = "60 bpm";
        systolicBloodPressure = "12 mmHg";
        diastolicBloodPressure = "8 mmHg";
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
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

    public void sentAllert() {
        this.listenner.pisca(this);
    }

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
                        this.setName(sex);
                        Iterator it = data.iterator();
                        u.setName((String) it.next());
                        u.setAge((String) it.next());
                        u.setSex((String) it.next());
                        u.setTemperature((String) it.next());
                        u.setBreathingRate((String) it.next());
                        u.setHeartRate((String) it.next());
                        u.setOxygenSaturation((String) it.next());
                        u.setSystolicBloodPressure((String) it.next());
                        u.setDiastolicBloodPressure((String) it.next());
                        u.listenner.pisca(u);
                        while (it.hasNext()) {
                            System.out.println(it.next());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };
        att.start();
    }

    public void stopAtt() {
        atting = false;
        att.interrupt();
    }

}

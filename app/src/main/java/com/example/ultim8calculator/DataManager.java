package com.example.ultim8calculator;

import android.app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DataManager {

    Activity activity;

    public DataManager(Activity activity) {
        this.activity = activity;
    }

    public void saveData(String dataAsString) {
        File path = activity.getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "data.txt"));
            writer.write(dataAsString.getBytes());
            writer.close();
            System.out.println(dataAsString);
        } catch (Exception e) {
            System.err.println("Error occured while attempting to save data, data may not be saved!");
        }
    }

    public String[] loadData() {
        File path = activity.getApplicationContext().getFilesDir();
        File readFrom = new File(path, "data.txt");
        byte[] rawData = new byte[(int) readFrom.length()];
        String[] data;
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(rawData);
            data = new String(rawData).split(";");

        } catch (Exception e) {
            data = null;
            System.err.println("Error occured while attempting to read saved data, resetting calculator to defaults!");
        }
        return data;
    }
}

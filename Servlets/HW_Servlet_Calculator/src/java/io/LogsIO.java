/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sherafgan
 */
public class LogsIO {

    public void put(float firstNumber, float secondNumber, String operation, int idForLogs) throws IOException {
        File file = new File(Integer.toString(idForLogs) + ".logs");
        String newLog = operation + "(" + firstNumber + ", " + secondNumber + ") \n";
        List<String> logs;
        if (file.exists() && !file.isDirectory()) {
            logs = getLogsFor(idForLogs);
        } else {
            logs = new LinkedList<>();
        }
        logs.add(newLog);
        FileWriter fileWriter = new FileWriter(Integer.toString(idForLogs) + ".logs");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String s : logs) {
            bufferedWriter.write(s);
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public List<String> getLogsFor(int idForLogs) throws IOException {
        FileReader fileReader = new FileReader(Integer.toString(idForLogs) + ".logs");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> logs = new LinkedList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            logs.add(line);
        }
        return logs;
    }
}

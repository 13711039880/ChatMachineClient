package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.chat.machine.client.Main.*;

public class RequestServer {
    private static final Logger log = LogManager.getLogger(RequestServer.class);
    private static int RequestsNumber = 0;
    private static Socket socket;

    public static void main() throws IOException {
        socket = new Socket(config.ip, config.port);
    }

    public static String send(String text) {
        RequestsNumber++;
        String to = text + ":" + config.key;
        info("向 " + config.ip + ":" + config.port + " 发送内容: " + to);
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(to);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String ReturnStr;
        try {
            ReturnStr = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        info(config.ip + ":" + config.port + " 发送的内容: " + ReturnStr);
        return ReturnStr;
    }

    public static String LoadSpecialPurpose(String text, String purpose, JTextArea ErrorTextArea) {
        RequestsNumber++;
        String to = text;
        if (!text.split(":")[0].equals("verify")) to = text + ":" + config.key;
        info("向 " + config.ip + ":" + config.port + " 发送内容: " + to);

        BufferedReader reader = null;
        String ReturnStr = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(to);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            load.LoadMain.HaveError = true;
            ErrorTextArea.append("[错误] " + purpose + ": " + new RuntimeException(e) + "\n");
            error("[load] " + new RuntimeException(e));
        }

        try {
            ReturnStr = reader.readLine();
        } catch (IOException e) {
            load.LoadMain.HaveError = true;
            ErrorTextArea.append("[错误] " + purpose + ": " + new RuntimeException(e) + "\n");
            log.error("[load] " + new RuntimeException(e));
        }

        info(config.ip + ":" + config.port + " 发送的内容: " + ReturnStr);
        return ReturnStr;
    }

    public static File CompleteFileSpecialPurpose(String file) {
        RequestsNumber++;
        String to = "cfile:" + file + ":" + config.key;
        info("向 " + config.ip + ":" + config.port + " 发送内容: " + to);

        try {
            byte[] buffer = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            String fileName = "test.txt";
            outputStream.write(fileName.getBytes());
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] bytes = new byte[1024];
            int length;

            while ((length = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
            }

            fos.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private static void error(String text) {
        log.error("[请求:" + RequestsNumber + "]" + text);
    }

    private static void info(String text) {
        log.info("[请求:" + RequestsNumber + "]" + text);
    }
}

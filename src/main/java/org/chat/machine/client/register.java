package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static org.chat.machine.client.Main.*;

public class register {
    protected static final Logger log = LogManager.getLogger(register.class);
    private static JFrame frame;
    private JPanel jp;
    private JLabel 注册Label;
    private JButton 注册Button;
    private JButton 登录Button;
    private JPanel RegisterJp;
    private JTextField 用户名TextField;
    private JTextField 密码TextField;
    private JLabel 用户名Label;
    private JLabel 密码Label;

    public register() {
        登录Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                login.main();
            }
        });

        注册Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket(config.ip, config.port);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write("register:" + 用户名TextField.getText() + "," + 密码TextField.getText());
                    writer.newLine();
                    writer.flush();
                    if (reader.readLine().equals("true")) {
                        log.info("注册成功");
                        info.UserName = 用户名TextField.getText();
                        info.UserUUID = RequestServer.send("getuuid:" + info.UserName);
                        frame.dispose();
                        MainUI.main();
                    } else {
                        log.info("注册失败");
                        JOptionPane.showMessageDialog(null, "注册失败");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void main() {
        frame = new JFrame("聊天器 - 注册");
        frame.setContentPane(new register().jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

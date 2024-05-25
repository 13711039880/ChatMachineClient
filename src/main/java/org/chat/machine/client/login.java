package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.chat.machine.client.Main.*;

public class login {
    protected static final Logger log = LogManager.getLogger(login.class);
    public static boolean OK;
    private static JFrame frame;
    private JPanel jp;
    private JLabel 登录Label;
    private JPanel LoginJp;
    private JButton 登录Button;
    private JButton 注册Button;
    private JTextField 用户名TextField;
    private JTextField 密码TextField;
    private JLabel 用户名Label;
    private JLabel 密码Label;

    public login() {
        登录Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("登录: " + 用户名TextField.getText() + "," + 密码TextField.getText());
                登录Button.setText("登录中...");

                if (RequestServer.send("login:" + 用户名TextField.getText() + "," + 密码TextField.getText()).equals("true")) {
                    log.info("登录成功");
                    tray.message("欢迎, " + info.UserName, "登录成功");
                    info.UserName = 用户名TextField.getText();
                    info.UserUUID = RequestServer.send("getuuid:" + info.UserName);
                    frame.dispose();
                    MainUI.main();
                } else {
                    log.info("登录失败");
                    JOptionPane.showMessageDialog(null, "用户名或密码不正确");
                    登录Button.setText("登录");
                }
            }
        });

        注册Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                register.main();
            }
        });

        if (set.AutoLogin.enable) {
            log.info("自动登录: " + set.AutoLogin.username + "," + set.AutoLogin.password);

            登录Button.setText("登录中...");
            注册Button.setEnabled(false);
            登录Button.setEnabled(false);

            用户名TextField.setText(set.AutoLogin.username);
            密码TextField.setText(set.AutoLogin.password);
            用户名TextField.setEnabled(false);
            密码TextField.setEnabled(false);

            if (RequestServer.send("login:" + set.AutoLogin.username + "," + set.AutoLogin.password).equals("true")) {
                log.info("登录成功");
                info.UserName = 用户名TextField.getText();
                info.UserUUID = RequestServer.send("getuuid:" + info.UserName);
                OK = true;

                try {
                    Thread.sleep(1000);
                    登录Button.setText("登录成功");
                    tray.message("欢迎, " + info.UserName, "登录成功");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                frame.dispose();
                MainUI.main();
            } else {
                log.info("登录失败");
                JOptionPane.showMessageDialog(null, "用户名或密码不正确");
                System.exit(2);
            }
        }
    }

    public static void main() {
        frame = new JFrame("聊天器 - 登录");
        frame.setContentPane(new login().jp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void CloseFrame() {
        frame.dispose();
    }
}

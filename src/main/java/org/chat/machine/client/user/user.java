package org.chat.machine.client.user;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.chat.machine.client.Main.*;

public class user {
    private JPanel jp;
    private JLabel UserName;
    private JButton 注销Button;
    private JLabel UserIco;
    private JScrollPane ListJsp;
    private JPanel ListJp;
    private JLabel UUID;
    private JLabel server;

    public user() {
        UserName.setText(info.UserName);
        UserIco.setIcon(resource.icon.user);
        UUID.setText("UUID: " + info.UserUUID);
        server.setText("server: " + config.ip + ":" + config.port);

        注销Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfirmLogout.main();
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("聊天器 - 用户");
        frame.setContentPane(new user().jp);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

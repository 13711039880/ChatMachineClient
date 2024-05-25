package org.chat.machine.client.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.RequestServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.chat.machine.client.Main.info.UserName;

public class ConfirmLogout {
    protected static final Logger log = LogManager.getLogger(ConfirmLogout.class);
    private static JFrame frame;
    private JPanel jp;
    private JLabel 确认注销吗Label;
    private JButton 确定Button;
    private JButton 取消Button;

    public ConfirmLogout() {
        确定Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RequestServer.send("logoutuser:" + UserName).equals("true")) {
                    log.info("注销成功");
                    JOptionPane.showMessageDialog(null, "注销成功");
                    frame.dispose();
                } else {
                    log.info("注销失败");
                    JOptionPane.showMessageDialog(null, "注销失败");
                }
            }
        });

        取消Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public static void main() {
        frame = new JFrame("确认注销");
        frame.setContentPane(new ConfirmLogout().jp);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

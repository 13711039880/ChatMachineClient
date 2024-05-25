package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.settings.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.chat.machine.client.Main.resource.icon.main;
import static org.chat.machine.client.room.HaveRoom;

public class tray {
    protected static final Logger log = LogManager.getLogger(tray.class);
    private static TrayIcon ti;

    public static void main(JTextArea ErrorTextArea) {
        SystemTray tray = SystemTray.getSystemTray();
        ti = new TrayIcon(main.getImage(), "ChatMachineClient", menu());
        ti.setImageAutoSize(true);

        ti.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    MainUI.frame.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        try {
            tray.add(ti);
        } catch (AWTException e) {
            load.LoadMain.HaveError = true;
            ErrorTextArea.append("[错误] 创建托盘: " + new RuntimeException(e) + "\n");
            log.error("[load] 验证: " + new RuntimeException(e));
        }
    }

    private static PopupMenu menu() {
        PopupMenu pop = new PopupMenu();

        MenuItem CloseButton = new MenuItem("close");
        CloseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close.main();
            }
        });
        pop.add(CloseButton);

        MenuItem MainUIButton = new MenuItem("MainUI");
        MainUIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainUI.frame.setVisible(true);
            }
        });
        pop.add(MainUIButton);

        MenuItem RoomButton = new MenuItem("room");
        RoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (HaveRoom) {
                    room.visible();
                } else {
                    JOptionPane.showMessageDialog(null, "未选择房间");
                }
            }
        });
        pop.add(RoomButton);

        MenuItem SettingButton = new MenuItem("setting");
        SettingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.main();
            }
        });
        pop.add(SettingButton);

        return pop;
    }

    public static void message(String title, String text) {
        log.info("message: " + title + " | " + text);
        ti.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }
}

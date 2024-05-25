package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.RoomManagement.RoomManagement;
import org.chat.machine.client.settings.settings;
import org.chat.machine.client.user.user;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.chat.machine.client.Main.*;
import static org.chat.machine.client.login.OK;

public class MainUI {
    protected static final Logger log = LogManager.getLogger(MainUI.class);
    public static JFrame frame;
    private JPanel jp;
    private JScrollPane ListJsp;
    private JButton 我Button;
    private JButton 设置Button;
    private JList RoomList;
    private JButton 房间Button;

    public MainUI() {
        我Button.setText(info.UserName);
        我Button.setIcon(resource.icon.user);
        设置Button.setIcon(resource.icon.set);
        房间Button.setIcon(resource.icon.room);
        RoomList.setListData(info.room.split(","));
        jp.setBackground(set.ui.background);
        我Button.setBackground(set.ui.buttoncolor != null ? set.ui.buttoncolor : Color.WHITE);
        设置Button.setBackground(set.ui.buttoncolor != null ? set.ui.buttoncolor : Color.WHITE);

        RoomList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int b = RoomList.getLeadSelectionIndex();
                ListModel<String> model = RoomList.getModel();
                org.chat.machine.client.room.main(model.getElementAt(b));
            }
        });

        设置Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.main();
            }
        });

        我Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.main();
            }
        });

        房间Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoomManagement.main();
            }
        });
    }

    public static void main() {
        new LoginFrameClose().start();

        frame = new JFrame("聊天器");
        frame.setContentPane(new MainUI().jp);
        frame.setSize(600, 400);
        frame.setIconImage(resource.icon.main.getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (records.StartCount == 0) {
            ConfigOperation.SetRecord("StartCount", String.valueOf(records.StartCount + 1));
            org.chat.machine.client.settings.EULA.main();
        } else {
            ConfigOperation.SetRecord("StartCount", String.valueOf(records.StartCount + 1));
        }
    }

    static class LoginFrameClose extends Thread {
        @Override
        public void run() {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (OK) {
                login.CloseFrame();
            }
        }
    }
}

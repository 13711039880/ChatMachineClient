package org.chat.machine.client.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.timing;
import org.util.file.DeleteFile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class log {
    protected static final Logger log = LogManager.getLogger(log.class);
    private JPanel jp;
    private JScrollPane LogListJsp;
    private JScrollPane LogTextJsp;
    private JPanel LogTextJp;
    private JTextArea LogText;
    private JTextArea LogTextLine;
    private JList LogList;
    private JLabel LogName;
    private JButton 删除Button;
    private JButton 刷新Button;

    public log() {
        LogTextJsp.getVerticalScrollBar().setUnitIncrement(30);
        LogListJsp.getVerticalScrollBar().setUnitIncrement(30);

        log.info(ls("[获取日志] 获取日志..."));
        timing GetLogListTime = new timing();
        File[] FileList = new File(".\\logs").listFiles();
        int i = 0;
        String[] list = new String[FileList.length];
        for (File file : FileList) {
            if (!file.isDirectory()) {
                list[i] = file.getName();
                i++;
            }
        }
        LogList.setListData(list);
        log.info(ls("[获取日志] 获取日志完成({}ms)", String.valueOf(GetLogListTime.stop())));

        LogList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int b = LogList.getLeadSelectionIndex();
                ListModel<String> model = LogList.getModel();
                String log = model.getElementAt(b);

                LogTextLine.setText("");
                LogText.setText("");
                LogName.setText(log);

                org.chat.machine.client.settings.log.log.info(ls("[获取日志内容] 获取日志内容..."));
                timing GetLogContentTime = new timing();

                int LineCount = 0;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(".\\logs\\" + log));

                    while (reader.readLine() != null) {
                        LineCount++;
                        LogTextLine.append(LineCount + "\n");
                    }

                    reader.close();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }

                try (Scanner sc = new Scanner(new FileReader(".\\logs\\" + log))) {
                    while (sc.hasNextLine()) {
                        LogText.append(sc.nextLine() + "\n");
                    }
                } catch (FileNotFoundException exception) {
                    throw new RuntimeException(exception);
                }

                org.chat.machine.client.settings.log.log.info(ls("[获取日志内容] 获取日志内容完成({}ms)", String.valueOf(GetLogContentTime.stop())));
            }
        });

        刷新Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info(ls("[获取日志] 获取日志..."));
                timing GetLogListTime = new timing();

                File[] FileList = new File(".\\logs").listFiles();
                int i = 0;
                String[] list = new String[FileList.length];

                for (File file : FileList) {
                    if (!file.isDirectory()) {
                        list[i] = file.getName();
                        i++;
                    }
                }

                LogList.setListData(list);
                log.info(ls("[获取日志] 获取日志完成({}ms)", String.valueOf(GetLogListTime.stop())));
            }
        });

        删除Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info(ls("[删除日志] 删除日志..."));
                timing time = new timing();

                try {
                    int b = LogList.getLeadSelectionIndex();
                    ListModel<String> model = LogList.getModel();
                    String log = model.getElementAt(b);

                    if (Files.exists(Path.of(".\\logs\\" + log))) {
                        DeleteFile.DeleteFile(".\\logs\\" + log);
                        JOptionPane.showMessageDialog(null, "删除成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "日志不存在");
                    }
                } catch (Exception ex) {
                    String error = String.valueOf(new RuntimeException(ex));
                    log.error(error);
                }

                log.info(ls("[删除日志] 删除日志完成({}ms)", String.valueOf(time.stop())));
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("日志");
        frame.setContentPane(new log().jp);
        frame.setSize(1400, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String ls(String text) {
        return "[查看日志] " + text;
    }

    private static String ls(String text, String replace) {
        return "[查看日志] " + text.replace("{}", replace);
    }
}

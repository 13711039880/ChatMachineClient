package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * _____   __  __    _____
 * / ____| |  \/  |  / ____|
 * | |      | \  / | | |
 * | |      | |\/| | | |
 * | |____  | |  | | | |____
 * \_____| |_|  |_|  \_____|
 * ChatMachineClient - 1.0
 *
 * @author 8043
 * @version 1.0
 */

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    public static Properties args;

    public static class records {
        public static int StartCount;
    }

    public static class config {
        public static String ip;
        public static int port;
        public static String key;
    }

    public static class info {
        public static String UserUUID;
        public static String UserName;
        public static String room;
    }


    public static class resource {
        public static class music {
            public static File message;
        }

        public static class icon {
            public static ImageIcon main;
            public static ImageIcon user;
            public static ImageIcon set;
            public static ImageIcon room;
        }

        public static class look {
            public static class text {
                public static String EULA;
                public static String PrivacyPolicy;
            }

            public static class html {
                public static String about;
                public static String SendReplaceHelp;
                public static String ReceiveReplaceHelp;
            }
        }
    }

    public static class set {
        public static class ui {
            public static Color background;
            public static Color buttoncolor;
        }

        public static class AutoLogin {
            public static boolean enable;
            public static String username;
            public static String password;
        }

        public static class chat {
            public static boolean SendReplace;
            public static boolean ReceiveReplace;
            public static int UpdateInterval;
        }

        public static class load {
            public static boolean ConciseLoad;
            public static boolean ConciseLoadFast;
            public static boolean StereoscopicLogo;
        }
    }

    public static void main(String[] args) {
        Main.args = ArgsToProperties(args);

        if (Main.args.getProperty("StartLogo") == null || Main.args.getProperty("StartLogo").equals("concise_flat")) {
            MoreLineInfoLog("""
                  _____   __  __    _____\s
                 / ____| |  \\/  |  / ____|
                | |      | \\  / | | |    \s
                | |      | |\\/| | | |    \s
                | |____  | |  | | | |____\s
                 \\_____| |_|  |_|  \\_____|
                ChatMachineClient - 1.0
                """);
        } else if (Main.args.getProperty("StartLogo").equals("concise_stereoscopic")) {
            MoreLineInfoLog("""
                 ______     __    __     ______   \s
                /\\  ___\\   /\\ "-./  \\   /\\  ___\\  \s
                \\ \\ \\____  \\ \\ \\-./\\ \\  \\ \\ \\____ \s
                 \\ \\_____\\  \\ \\_\\ \\ \\_\\  \\ \\_____\\\s
                  \\/_____/   \\/_/  \\/_/   \\/_____/\s
                ChatMachineClient - 1.0
                """);
        } else if (Main.args.getProperty("StartLogo").equals("complex_flat")) {
            MoreLineInfoLog("""
                   _____ _           _   __  __            _     _             _____ _ _            _  \s
                  / ____| |         | | |  \\/  |          | |   (_)           / ____| (_)          | | \s
                 | |    | |__   __ _| |_| \\  / | __ _  ___| |__  _ _ __   ___| |    | |_  ___ _ __ | |_\s
                 | |    | '_ \\ / _` | __| |\\/| |/ _` |/ __| '_ \\| | '_ \\ / _ \\ |    | | |/ _ \\ '_ \\| __|
                 | |____| | | | (_| | |_| |  | | (_| | (__| | | | | | | |  __/ |____| | |  __/ | | | |_\s
                  \\_____|_| |_|\\__,_|\\__|_|  |_|\\__,_|\\___|_| |_|_|_| |_|\\___|\\_____|_|_|\\___|_| |_|\\__|
                ChatMachineClient - 1.0
                """);
        } else if (Main.args.getProperty("StartLogo").equals("complex_stereoscopic")) {
            MoreLineInfoLog("""
                 ________  ___  ___  ________  _________  _____ ______   ________  ________  ___  ___  ___  ________   _______   ________  ___       ___  _______   ________   _________  \s
                |\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\\\   _ \\  _   \\|\\   __  \\|\\   ____\\|\\  \\|\\  \\|\\  \\|\\   ___  \\|\\  ___ \\ |\\   ____\\|\\  \\     |\\  \\|\\  ___ \\ |\\   ___  \\|\\___   ___\\\s
                \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_\\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\ \\  \\\\ \\  \\ \\   __/|\\ \\  \\___|\\ \\  \\    \\ \\  \\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_|\s
                 \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\ \\ \\  \\\\|__| \\  \\ \\   __  \\ \\  \\    \\ \\   __  \\ \\  \\ \\  \\\\ \\  \\ \\  \\_|/_\\ \\  \\    \\ \\  \\    \\ \\  \\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\ \s
                  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\    \\ \\  \\ \\  \\ \\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\_|\\ \\ \\  \\____\\ \\  \\____\\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\\s
                   \\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\ \\ \\__\\    \\ \\__\\ \\__\\ \\__\\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\_______\\ \\_______\\ \\_______\\ \\__\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\
                    \\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|  \\|__|     \\|__|\\|__|\\|__|\\|_______|\\|__|\\|__|\\|__|\\|__| \\|__|\\|_______|\\|_______|\\|_______|\\|__|\\|_______|\\|__| \\|__|    \\|__|
                ChatMachineClient - 1.0
                    """);
        }

        log.info("启动参数: " + Main.args);

        IfFileExists(new String[]{
            "config.properties",
            "set.properties",
            "resource.zip",
            "record.properties"
        });

        log.info("[获取设置] 获取设置...");
        timing time = new timing();
        GetSet.main();
        log.info("[获取设置] 完成({}ms)", time.stop());

//        RequestServer.main("sendmail:fengD54@163.com,aaa,aaa");
//        RequestServer.send("cfile:resource.zip");

        try {
            log.info("UI风格: " + UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (set.load.ConciseLoad) {
            new ConciseLoad();
        } else {
            new load();
        }
    }

    private static Color StringToColor(String color) {
        switch (color) {
            default -> {
                log.error("颜色不存在或不支持: " + color);
                return null;
            }

            case "white" -> {
                return Color.WHITE;
            }

            case "green" -> {
                return Color.GREEN;
            }

            case "red" -> {
                return Color.RED;
            }

            case "yellow" -> {
                return Color.YELLOW;
            }

            case "default" -> {
                return null;
            }
        }
    }

    private static void IfFileExists(String[] files) {
        log.info("[检测文件] 检测文件...");
        int deficiency = 0;

        log.info("[检测文件] temp\\:");
        if (Files.exists(Path.of(".\\temp"))) {
            log.info("[检测文件]   存在");
        } else {
            log.warn("[检测文件]   不存在");
            log.info("[检测文件]   创建...");
            new File(".\\temp").mkdirs();
        }

        for (String file : files) {
            log.info("[检测文件] " + file + ":");

            if (Files.exists(Path.of(".\\" + file))) {
                log.info("[检测文件]   存在");
            } else {
                log.warn("[检测文件]   不存在");
                deficiency++;
            }
        }

        log.info("[检测文件] 检测文件完成(缺失: " + deficiency + ")");
        if (deficiency != 0) {
            log.error("[检测文件] 有文件缺失");
            System.exit(1);
        }
    }

    private static void MoreLineInfoLog(String text) {
        for (String s : text.split("\n")) {
            log.info(s);
        }
    }

    private static Properties ArgsToProperties(String[] args) {
        Properties properties = new Properties();

        for (String text : args) {
            properties.setProperty(text.split("=")[0], text.split("=")[1]);
        }

        return properties;
    }

    public static class GetSet {
        public static void main() {
            config.main();
            set.main();
            records.main();
        }

        static class config {
            private static final Logger log = LogManager.getLogger(config.class);

            private static void main() {
                i("获取config...");
                timing time = new timing();

                Main.config.ip = ConfigOperation.ReadConfig("ServerIP").split(":")[0];
                Main.config.port = Integer.parseInt(ConfigOperation.ReadConfig("ServerIP").split(":")[1]);
                Main.config.key = ConfigOperation.ReadConfig("key");

                i("ip = " + Main.config.ip);
                i("port = " + Main.config.port);
                i("key = " + Main.config.key);

                i("完成({}ms)", String.valueOf(time.stop()));
            }

            private static void i(String text) {
                log.info("[获取设置][config] {}", text);
            }

            private static void i(String text, String replace) {
                log.info(("[获取设置][config] " + text).replace("{}", replace));
            }
        }

        static class set {
            private static final Logger log = LogManager.getLogger(config.class);

            private static void main() {
                i("获取set...");
                timing time = new timing();

                Main.set.ui.background = StringToColor(ConfigOperation.ReadSet("ui.background"));
                Main.set.ui.buttoncolor = StringToColor(ConfigOperation.ReadSet("ui.buttoncolor"));
                Main.set.load.ConciseLoad = Boolean.parseBoolean(ConfigOperation.ReadSet("load.concise.enable"));
                Main.set.load.ConciseLoadFast = Boolean.parseBoolean(ConfigOperation.ReadSet("load.concise.fast"));
                Main.set.load.StereoscopicLogo = Boolean.parseBoolean(ConfigOperation.ReadSet("load.concise.StereoscopicLogo"));
                Main.set.AutoLogin.enable = Boolean.parseBoolean(ConfigOperation.ReadSet("AutoLogin.enable"));
                Main.set.AutoLogin.username = ConfigOperation.ReadSet("AutoLogin.username");
                Main.set.AutoLogin.password = ConfigOperation.ReadSet("AutoLogin.password");
                Main.set.chat.SendReplace = Boolean.parseBoolean(ConfigOperation.ReadSet("chat.send.replace"));
                Main.set.chat.UpdateInterval = Integer.parseInt(ConfigOperation.ReadSet("chat.update.interval"));
                Main.set.chat.ReceiveReplace = Boolean.parseBoolean(ConfigOperation.ReadSet("chat.receive.replace"));

                i("ui.background = " + ConfigOperation.ReadSet("ui.background"));
                i("ui.buttoncolor = " + ConfigOperation.ReadSet("ui.buttoncolor"));
                i("AutoLogin.enable = " + Main.set.AutoLogin.enable);
                i("AutoLogin.username = " + Main.set.AutoLogin.username);
                i("AutoLogin.password = " + Main.set.AutoLogin.password);
                i("chat.send.replace = " + Main.set.chat.SendReplace);
                i("chat.update.interval = " + Main.set.chat.UpdateInterval);
                i("chat.receive.replace = " + Main.set.chat.ReceiveReplace);
                i("load.concise.enable = " + Main.set.load.ConciseLoad);
                i("load.concise.fast = " + Main.set.load.ConciseLoadFast);
                i("load.concise.StereoscopicLogo = " + Main.set.load.StereoscopicLogo);

                i("完成({}ms)", String.valueOf(time.stop()));
            }

            private static void i(String text) {
                log.info("[获取设置][set] {}", text);
            }

            private static void i(String text, String replace) {
                log.info(("[获取设置][set] " + text).replace("{}", replace));
            }
        }

        static class records {
            private static final Logger log = LogManager.getLogger(records.class);

            private static void main() {
                i("获取record...");
                timing time = new timing();

                Main.records.StartCount = Integer.parseInt(ConfigOperation.ReadRecord("StartCount"));
                i("StartCount = " + Main.records.StartCount);

                i("完成({}ms)", String.valueOf(time.stop()));
            }

            private static void i(String text) {
                log.info("[获取设置][record] {}", text);
            }

            private static void i(String text, String replace) {
                log.info(("[获取设置][record] " + text).replace("{}", replace));
            }
        }
    }
}

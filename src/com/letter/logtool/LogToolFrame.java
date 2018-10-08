package com.letter.logtool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class LogToolFrame extends JFrame {

    public static final String[] DEBUG_LEVEL = {"Verbose", "Debug", "Info", "Warning", "Error"};
    public static final String[] BAUDRATE = {"9600", "19200", "38400", "56000", "57600", "115200"};
    public static final String[] LEVEL_MODE = {"以下", "仅当前"};

    private static final String textFont = "Consolas";
    private static final int textSize = 18;

    private JTextArea inputTextArea;
    private JScrollPane inputScrollPane;
    private JPanel setPanel;
    private JLabel serialListLabel;
    private JComboBox<String> serialListComboBox;
    private JComboBox<String> baudrateComboBox;
    private JButton connectButton;
    private JLabel debugLevelLabel;
    private JComboBox<String> debugLevelComboBox;
    private JComboBox<String> levelModeComboBox;
    private JTextField searchTextField;
    private JButton searchButton;
    private JTextField sendTextField;
    private JButton sendButton;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;

    private TextNumChangedListener connectButtonListener;
    private TextChangedListener levelModeComboBoxListener;
    private TextChangedListener debugLevelComboBoxListener;
    private TextChangedListener searchTextListener;
    private TextChangedListener sendTextListener;

    public LogToolFrame () {
        inputTextArea = new JTextArea(20,100);
        inputScrollPane = new JScrollPane(inputTextArea);
        setPanel = new JPanel();
        serialListLabel = new JLabel("串口");
        serialListComboBox = new JComboBox<>();
        baudrateComboBox = new JComboBox<>();
        connectButton = new JButton("连接");
        debugLevelLabel = new JLabel("级别");
        debugLevelComboBox = new JComboBox<>();
        levelModeComboBox = new JComboBox<>();
        searchTextField = new JTextField(50);
        searchButton = new JButton("搜索");
        sendTextField = new JTextField(88);
        sendButton = new JButton("发送");
        logTextArea = new JTextArea(10, 100);
        logScrollPane = new JScrollPane(logTextArea);

        inputTextArea.setFont(new Font(textFont, Font.PLAIN, textSize));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        inputScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        for (String str : BAUDRATE) {
            baudrateComboBox.addItem(str);
        }

        connectButton.addActionListener(e -> {
            // TODO: 获取serialListComboBox内容并连接对应串口
            connectButtonListener.textNumPerformed(serialListComboBox.getSelectedItem().toString(),
                    Integer.parseInt(baudrateComboBox.getSelectedItem().toString()));
        });

        for (String level : DEBUG_LEVEL) {
            debugLevelComboBox.addItem(level);
        }
        debugLevelComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // TODO: 获取debugLevelComboBox内容并设置调试信息级别，清空logTextArea
                logTextArea.setText("");
                debugLevelComboBoxListener.textPerformed(debugLevelComboBox.getSelectedItem().toString());
            }
        });

        for (String mode : LEVEL_MODE) {
            levelModeComboBox.addItem(mode);
        }
        levelModeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // TODO: 获取levelModeComboBox内容并设置调试信息模式，清空logTextArea
                logTextArea.setText("");
                levelModeComboBoxListener.textPerformed(levelModeComboBox.getSelectedItem().toString());
            }
        });

        searchTextField.setFont(new Font(textFont, Font.PLAIN, textSize));
        searchTextField.addActionListener((e -> {
            // TODO: 搜索字符串
            logTextArea.setText("");
            searchTextListener.textPerformed(searchTextField.getText());
        }));

        searchButton.addActionListener((e -> {
            // TODO: 搜索字符串
            logTextArea.setText("");
            searchTextListener.textPerformed(searchTextField.getText());
        }));

        sendTextField.setFont(new Font(textFont, Font.PLAIN, textSize));
        sendTextField.addActionListener(e -> {
            // TODO: 发送sendTextField的内容到串口并清空
            sendTextListener.textPerformed(sendTextField.getText());
            sendTextField.setText("");
        });

        sendButton.addActionListener(e -> {
            // TODO: 发送sendTextField的内容到串口并清空
            sendTextListener.textPerformed(sendTextField.getText());
            sendTextField.setText("");
        });

        logTextArea.setFont(new Font(textFont, Font.PLAIN, textSize));
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);

        logScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel upSetPanel = new JPanel();
        JPanel downSetPanel = new JPanel();

        downSetPanel.setLayout(new FlowLayout());
        downSetPanel.add(serialListLabel);
        downSetPanel.add(serialListComboBox);
        downSetPanel.add(baudrateComboBox);
        downSetPanel.add(connectButton);
        downSetPanel.add(debugLevelLabel);
        downSetPanel.add(debugLevelComboBox);
        downSetPanel.add(levelModeComboBox);
        downSetPanel.add(searchTextField);
        downSetPanel.add(searchButton);

        upSetPanel.setLayout(new FlowLayout());
        upSetPanel.add(sendTextField);
        upSetPanel.add(sendButton);

        setPanel.setLayout(new BorderLayout());
        setPanel.add(upSetPanel, BorderLayout.NORTH);
        setPanel.add(downSetPanel, BorderLayout.SOUTH);

        JPanel setAndLogPanel = new JPanel();
        setAndLogPanel.setLayout(new BorderLayout());
        setAndLogPanel.add(setPanel, BorderLayout.NORTH);
        setAndLogPanel.add(logScrollPane, BorderLayout.CENTER);

        this.setTitle("logTools");
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.add(inputScrollPane, BorderLayout.CENTER);
        this.add(setAndLogPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setMinimumSize(this.getSize());
//        this.setResizable(false);
    }

    public void setConnectButtonListener(TextNumChangedListener connectButtonListener) {
        this.connectButtonListener = connectButtonListener;
    }

    public void setDebugLevelComboBoxListener(TextChangedListener debugLevelComboBoxListener) {
        this.debugLevelComboBoxListener = debugLevelComboBoxListener;
    }

    public void setSearchTextListener(TextChangedListener searchTextListener) {
        this.searchTextListener = searchTextListener;
    }

    public void setSendTextListener(TextChangedListener sendTextListener) {
        this.sendTextListener = sendTextListener;
    }

    public void setLevelModeComboBoxListener(TextChangedListener levelModeComboBoxListener) {
        this.levelModeComboBoxListener = levelModeComboBoxListener;
    }

    public void addSerialPort (String string) {
        serialListComboBox.addItem(string);
    }

    public void addTextToInputTextArea (String string) {
        inputTextArea.append(string);
        inputTextArea.setCaretPosition(inputTextArea.getText().length());
    }

    public void addTextToLogTextArea (String string) {
        logTextArea.append(string);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

    public void clearLogTextArea () {
        logTextArea.setText("");
    }

    public void setConnectButtonText (String string) {
        connectButton.setText(string);
    }

    static public void main (String[] args) {
        LogToolFrame logToolFrame = new LogToolFrame();
        logToolFrame.setConnectButtonListener((String string, int i) -> {

        });
        logToolFrame.setDebugLevelComboBoxListener(string -> {
            logToolFrame.addTextToLogTextArea(string + "\r\n");
        });
        logToolFrame.setSendTextListener(string -> {
            logToolFrame.addTextToInputTextArea(string + "\r\n");
        });
        logToolFrame.addSerialPort("COM1");
    }
}


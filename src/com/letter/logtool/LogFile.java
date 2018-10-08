package com.letter.logtool;

import java.io.*;

public class LogFile {
    public static final int LOG_LEVEL_NONE = 6;
    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_WARNING = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_DEBUG = 4;
    public static final int LOG_LEVEL_VERBOSE = 5;

    public static final int LEVEL_MODE_LOW = 0;
    public static final int LEVEL_MODE_EQU = 1;

    private String searchPatten = "";

    private File file;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedReader;
    private int logLevel = LOG_LEVEL_VERBOSE;
    private int levelMode = LEVEL_MODE_LOW;

    public LogFile (String string) {
        try {
            file = new File(string);
            if (!file.exists()) {
                File dir = new File(file.getParent());
            dir.mkdirs();
            file.createNewFile();
        }
        inputStream = new FileInputStream(file);
        outputStream = new FileOutputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            bufferedReader.mark(1000000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetReader () {
        try {
            if (bufferedReader != null) {
                bufferedReader.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bufferedReader.mark(1000000);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void writeFile (String string) {
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine () {
        String string = null;
        try {
            string = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    public String toDebugLine (String string) {
        int strLevel = 0;
        if (string != null && string.contains(searchPatten)) {
            if (string.contains("[Verbose]")) {
                strLevel = LOG_LEVEL_VERBOSE;
            } else if (string.contains("[Debug]")) {
                strLevel = LOG_LEVEL_DEBUG;
            } else if (string.contains("[Info]")) {
                strLevel = LOG_LEVEL_INFO;
            } else if (string.contains("[Warning]")) {
                strLevel = LOG_LEVEL_WARNING;
            } else if (string.contains("[Error]")) {
                strLevel = LOG_LEVEL_ERROR;
            } else {
                strLevel = LOG_LEVEL_NONE;
            }
            if (levelMode == LEVEL_MODE_LOW) {
                if (strLevel > logLevel) {
                    string = null;
                }
            } else {
                if (strLevel != logLevel) {
                    string = null;
                }
            }
        } else {
            string = null;
        }
        return string;
    }

    public String readDebugLine () {
        return toDebugLine(readLine());
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setLevelMode(int levelMode) {
        this.levelMode = levelMode;
    }

    public void setSearchPatten(String searchPatten) {
        this.searchPatten = searchPatten;
    }
}

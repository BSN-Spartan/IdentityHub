// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package com.reddate.hub.server.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

  public static void writeContent2File(String content, String filePath) {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(filePath);
      fileWriter.write(content);
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      try {
        fileWriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static String readContent(String filePath) {
    StringBuilder stringBuilder = new StringBuilder();
    char[] contentChar = new char[1024];
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
      int num = fileReader.read(contentChar);

      for (int i = 0; i < num; i++) {
        stringBuilder.append(contentChar[i]);
      }

    } catch (IOException e) {
      try {
        fileReader.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return stringBuilder.toString();
  }
}

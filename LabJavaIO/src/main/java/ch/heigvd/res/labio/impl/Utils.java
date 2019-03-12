package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    // Implementation with regex assuming a line doesn't contain more than one line break type
    // Not used in Application
    String[] result;
    if(lines.contains("\r\n") || lines.contains("\n")) {
      result = lines.split("(?<=\\r?\\n)", 2);
    } else if(lines.contains("\r")) {
      result = lines.split("(?<=\\r)", 2);
    } else {
      result = new String[2];
      result[0] = "";
      result[1] = lines;
    }

    return result;
  }

}

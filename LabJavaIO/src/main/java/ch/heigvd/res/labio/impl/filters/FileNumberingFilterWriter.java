package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  private int lineNum;
  private boolean rBreak;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
    // current line number
    this.lineNum = 1;
    // flag to keep track of \r line breaks
    this.rBreak = false;
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    write(str.toCharArray(), off, len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i = off; i < off + len; i++) {
      write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    if(lineNum == 1) {
      // in case of first line, start by writing the line
      super.write(lineNum + "\t",0, (int) (Math.log10(lineNum) + 2));
      lineNum++;
    }

    // (int) (Math.log10(lineNum) + 1) to get the number of digits

    if(c == '\n') {
      // handle the \n and \r\n case
      rBreak = false;
      super.write("\n" + lineNum + "\t",0, (int) (Math.log10(lineNum) + 3));
      lineNum++;
    } else if(c == '\r') {
      rBreak = true;
      super.write(c);
    } else {
      if(rBreak) {
        // handle the \r case
        rBreak = false;
        super.write(lineNum + "\t",0, (int) (Math.log10(lineNum) + 2));
        lineNum++;
      }
      super.write(c);
    }
  }
}

package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

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
    this.lineNum = 1;
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
      super.write(lineNum + "\t",0, (int) (Math.log10(lineNum) + 2));
      lineNum++;
    }

    if(c == '\n') {
      rBreak = false;
      super.write("\n" + lineNum + "\t",0, (int) (Math.log10(lineNum) + 3));
      lineNum++;
    } else if(c == '\r') {
      rBreak = true;
      super.write(c);
    } else {
      if(rBreak) {
        rBreak = false;
        super.write(lineNum + "\t",0, (int) (Math.log10(lineNum) + 2));
        lineNum++;
      }
      super.write(c);
    }
  }
}

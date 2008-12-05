/**
 * LOGBack: the reliable, fast and flexible logging library for Java.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.core.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.WarnStatus;


/**
 * The <code>Compression</code> class implements ZIP and GZ file
 * compression/decompression methods.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class Compressor extends ContextAwareBase {

  final CompressionMode compressionMode;
  final String nameOfFile2Compress;
  final String nameOfCompressedFile;

  
  public Compressor(CompressionMode compressionMode, String nameOfFile2Compress, String nameOfCompressedFile) {
    this.compressionMode = compressionMode;
    this.nameOfFile2Compress = nameOfFile2Compress;
    this.nameOfCompressedFile = nameOfCompressedFile;
  }
  
  public Compressor(CompressionMode compressionMode, String nameOfFile2Compress) {
    // Here we rely on the fact that the two argument version of ZIPCompress/GZCompress
    // automatically adds a .zip/.gz extension to the second argument
    this(compressionMode, nameOfFile2Compress, nameOfFile2Compress);
  }

  public void compress() {
    switch(compressionMode) {
    case GZ: 
      gzCompress(nameOfFile2Compress, nameOfCompressedFile);
      break;
    case ZIP:
      zipCompress(nameOfFile2Compress, nameOfCompressedFile);
      break;
    }
  }

  private void zipCompress(String nameOfFile2zip, String nameOfZippedFile) {
    File file2zip = new File(nameOfFile2zip);

    if (!file2zip.exists()) {
      addStatus(new WarnStatus(
              "The file to compress named [" + nameOfFile2zip
                  + "] does not exist.", this));

      return;
    }

    if (!nameOfZippedFile.endsWith(".zip")) {
      nameOfZippedFile = nameOfZippedFile + ".zip";
    }

    File zippedFile = new File(nameOfZippedFile);

    if (zippedFile.exists()) {
      addStatus(new WarnStatus(
          "The target compressed file named [" + nameOfZippedFile
              + "] exist already.", this));

      return;
    }

    try {
      FileOutputStream fos = new FileOutputStream(nameOfZippedFile);
      ZipOutputStream zos = new ZipOutputStream(fos);
      FileInputStream fis = new FileInputStream(nameOfFile2zip);

      ZipEntry zipEntry = new ZipEntry(file2zip.getName());
      zos.putNextEntry(zipEntry);

      byte[] inbuf = new byte[8102];
      int n;

      while ((n = fis.read(inbuf)) != -1) {
        zos.write(inbuf, 0, n);
      }

      fis.close();
      zos.close();

      if (!file2zip.delete()) {
        addStatus(new WarnStatus("Could not delete [" + nameOfFile2zip + "].",
            this));
      }
    } catch (Exception e) {
      addStatus(new ErrorStatus("Error occurred while compressing ["
          + nameOfFile2zip + "] into [" + nameOfZippedFile + "].", this, e));
    }
  }

  private void gzCompress(String nameOfFile2gz, String nameOfgzedFile) {
    File file2gz = new File(nameOfFile2gz);

    if (!file2gz.exists()) {
      addStatus(new WarnStatus("The file to compress named [" + nameOfFile2gz
          + "] does not exist.", this));

      return;
    }

    if (!nameOfgzedFile.endsWith(".gz")) {
      nameOfgzedFile = nameOfgzedFile + ".gz";
    }

    File gzedFile = new File(nameOfgzedFile);

    if (gzedFile.exists()) {
      addStatus(new WarnStatus("The target compressed file named ["
          + nameOfgzedFile + "] exist already.", this));

      return;
    }

    try {
      FileOutputStream fos = new FileOutputStream(nameOfgzedFile);
      GZIPOutputStream gzos = new GZIPOutputStream(fos);
      FileInputStream fis = new FileInputStream(nameOfFile2gz);
      byte[] inbuf = new byte[8102];
      int n;

      while ((n = fis.read(inbuf)) != -1) {
        gzos.write(inbuf, 0, n);
      }

      fis.close();
      gzos.close();

      if (!file2gz.delete()) {
        addStatus(new WarnStatus("Could not delete [" + nameOfFile2gz + "].",
            this));
      }
    } catch (Exception e) {
      addStatus(new ErrorStatus("Error occurred while compressing ["
          + nameOfFile2gz + "] into [" + nameOfgzedFile + "].", this, e));
    }
  }
  
  @Override
  public String toString() {
    return "c.q.l.core.rolling.helper.Compress";
  }
}

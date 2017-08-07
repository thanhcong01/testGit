/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.exportpdf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
class HandleFile {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HandleFile.class);
    public static void saveFile(byte[] content, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(content, 0, content.length);
        }
    }

    public static byte[] getByteFromFile(File input) {
        int size = (int) input.length();
        byte[] output = new byte[size];
        try {
            FileInputStream fis = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                bis.read(output, 0, size);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            bis.close();
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(HandleFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
}

package com.ftu.exportpdf;

import com.ftu.utils.ResourceBundleUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

public class FileUtil {

    public static void copyFile(File sourceFile, File destFile)
            throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static boolean validFileType(String fileType) throws UnsupportedEncodingException {
        try {
            //linhdx truong hop firefox lay filetype la abc.pdf thi tach lay phan duoi
            if (fileType.contains(".")) {
                String[] ext = fileType.split(".");
                fileType = ext[1];
            }
        } catch (Exception ex) {
        }

        boolean result = false;
        String sExt = ResourceBundleUtil.getString("extend_file", "config");
        String[] fileTypes = sExt.split(",");
        for (String s : fileTypes) {
            if (s.toLowerCase().equals(fileType.toLowerCase())) {
                result = true;
                break;
            }
        }
        return result;
        //return true;
    }

    public static boolean validFileTypeImage(String fileType) throws UnsupportedEncodingException {
        try {
            //linhdx truong hop firefox lay filetype la abc.pdf thi tach lay phan duoi
            if (fileType.contains(".")) {
                String[] ext = fileType.split(".");
                fileType = ext[1];
            }
        } catch (Exception ex) {
        }
        boolean result = false;
        String sExt = ResourceBundleUtil.getString("extend_file_image", "config");
        String[] fileTypes = sExt.split(",");
        for (String s : fileTypes) {
            if (s.toLowerCase().equals(fileType.toLowerCase())) {
                result = true;
                break;
            }
        }
        return result;
        //return true;
    }

    public static boolean saveFile(String path, InputStream inputStream) throws Exception {
        boolean bReturn = true;
        if (inputStream == null) {
            return bReturn;
        }
        File f = new File(path);

        if (f.exists()) {
        } else {
            f.createNewFile();
        }
        OutputStream outputStream = null;
        try {
            outputStream = null;
            outputStream = new FileOutputStream(f);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception ex) {
          //  LogUtils.addLog(ex.getMessage());
            bReturn = false;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bReturn;
    }

    public static boolean saveAttachFile(Long attachId, InputStream inputStream) throws Exception {
        boolean bReturn = true;
        String path = ResourceBundleUtil.getString("dir_upload");
        File folder = new File(path);
        //
        // Kiem tra thu muc co ton tai hay khong
        //
        if (folder.exists()) {
        } else {
            folder.mkdirs();
        }
        //
        // Ghi ra file theo duong dan
        //
        path = path + File.separator + attachId;

        return saveFile(path, inputStream);
    }

    public static File createTempFile(File source, String name) {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, name);
        try {
            copyFile(source, tempFile);
        } catch (IOException e) {
          //  LogUtils.addLog(e.getMessage());
        }
        return tempFile;
    }

    /**
     * Make folder path
     *
     * @param path
     * @return
     */
    public static boolean mkdirs(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

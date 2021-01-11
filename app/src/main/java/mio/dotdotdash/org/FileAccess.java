package mio.dotdotdash.org;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAccess {

    public static boolean writeToFile(Context context, String filename, String text) {
        File path = context.getFilesDir();
        File file = new File(path, filename);
        FileOutputStream stream = null;
        boolean ret = true;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            ret = false;
        }
        try {
            stream.write(text.getBytes());
        } catch (IOException e) {
            ret = false;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                ret = false;
            }
        }
        return ret;
    }

    public static boolean appendToFile(Context context, String filename, String text) {
        File path = context.getFilesDir();
        File file = new File(path, filename);
        FileOutputStream stream = null;
        boolean ret = true;
        try {
            stream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            ret = false;
        }
        try {
            stream.write(text.getBytes());
        } catch (IOException e) {
            ret = false;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                ret = false;
            }
        }
        return ret;
    }

    // todo: fix error handling - currently, reading from a non-existing file crashes the app
    public static String readFromFile(Context context, String filename) {
        File path = context.getFilesDir();
        File file = new File(path, filename);
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;
        boolean err = false;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            err = true;
        }
        try {
            in.read(bytes);
        } catch (IOException e) {
            err = true;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                err = true;
            }
        }
        String contents = null;
        if (!err) contents = new String(bytes);
        return contents;
    }
}

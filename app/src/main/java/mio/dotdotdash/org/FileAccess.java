package mio.dotdotdash.org;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileAccess {
    public static final String USERSCRIPT_FILENAME = "userScript.txt";
    public static final String PRACTICE_WORDLIST_FILENAME = "CKOgden.txt";
    public static final String EXERCISE_WORDLIST_FILENAME = "exercise.txt";
    public static final String ABCS_FILENAME = "ABCs.txt";


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

    public static String getStringAsset(Context context, String fname) {
        AssetManager assetManager = context.getAssets();
        StringBuffer sb = null;
        try {
            InputStream is = assetManager.open(fname);
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isReader);
            String str;
            sb = new StringBuffer();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sb == null){
            return "An error occurred while attempting to load file " + fname;
        }
        return sb.toString();
    }
}

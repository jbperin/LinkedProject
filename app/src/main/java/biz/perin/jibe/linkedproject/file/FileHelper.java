package biz.perin.jibe.linkedproject.file;

import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * Created by Famille PERIN on 28/10/2017.
 */
public class FileHelper implements IFiler{

    private static final String TAG = FileHelper.class.getName();
    private static FileHelper ourInstance = new FileHelper();
    private Context context;


    public static FileHelper getInstance() {
        return ourInstance;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    private FileHelper() {}

    @Override
    public void writeStringToFile(String content, String filename) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), filename));
            out.write(content);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException during writeStringToFile "+e);
        }
    }

    @Override
    public String readStringFromFile(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), filename)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException during readStringFromFile "+e);
        } catch (IOException e) {
            Log.e(TAG, "IOException during readStringFromFile "+e);
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean fileExists(String filename) {
        File file = new File(context.getFilesDir(), filename);
        return(file.exists());
    }

    public static void main(String[] args) {
        final String filename = "toto.txt";
        FileHelper.getInstance().writeStringToFile("Hello World", filename);
        String str = FileHelper.getInstance().readStringFromFile(filename);
        assert (str.equals("Hello World") );
    }


}

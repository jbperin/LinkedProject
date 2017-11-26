package biz.perin.jibe.linkedproject.file;

import android.content.Context;

import java.io.*;

/**
 * Created by Famille PERIN on 28/10/2017.
 */
public class FileHelper implements IFiler{
    private static FileHelper ourInstance = new FileHelper();
    private Context context;


    public static FileHelper getInstance() {
        return ourInstance;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    private FileHelper() {

    }


//    public static void writeStringAsFile(final String fileContents, String fileName)  {
//        try {
//            try(  PrintWriter out = new PrintWriter( fileName )  ){
//                out.println( fileContents );
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String readFileAsString(String fileName) {
//        Charset encoding = Charset.defaultCharset();
//        byte[] encoded = new byte[0];
//        try {
//            encoded = Files.readAllBytes(Paths.get(fileName));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new String(encoded, encoding);
//
//
//    }


    public static void main(String[] args) {
        final String filename = "toto.txt";
        FileHelper.getInstance().writeStringToFile("Hello World", filename);
        String str = FileHelper.getInstance().readStringFromFile(filename);
        assert (str.equals("Hello World") );

    }

    @Override
    public void writeStringToFile(String content, String filename) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), filename));
            out.write(content);
            out.close();
        } catch (IOException e) {
            // Log.logError(TAG, e);
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
            //Log.logError(TAG, e);
        } catch (IOException e) {
            // Log.logError(TAG, e);
        }

        return stringBuilder.toString();

    }

    @Override
    public boolean fileExists(String filename) {
        File file = new File(filename);
        return(file.exists());

    }
}

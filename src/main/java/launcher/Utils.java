package launcher;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import com.mulesoft.schema.validator.ValidationResult;

public class Utils {
    

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void writeLog(ValidationResult vr, File outputFolder)
    {
        outputFolder.mkdirs();
        
        File file = vr.file();
        String message = vr.message();
        Exception exception = vr.exception();

        String schName = file.getName();
        String logFileName = schName.substring(0, schName.lastIndexOf("."))
                + ".txt";
        File logFile = new File(outputFolder, logFileName);
        if (logFile.exists()) {
            logFile.delete();
        }

        PrintStream ps;
        try {
            ps = new PrintStream(logFile);
            ps.append(file.getAbsolutePath()).append(LINE_SEPARATOR);
            ps.append(message);
            if (exception != null) {
                ps.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
                exception.printStackTrace(ps);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getContent(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];

        BufferedInputStream bis = new BufferedInputStream(fis);
        int l = 0;
        while ((l = bis.read(buf)) >= 0) {
            bos.write(buf, 0, l);
        }
        bis.close();

        String str = new String(bos.toByteArray(), "UTF-8");
        return str;

    }
    
    public static void writeContent(File file, String content) throws Exception {

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes("UTF-8"));
        fos.close();

    }

}

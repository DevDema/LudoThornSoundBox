package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class FileUtils {

    public static final String AudioAssetpath = "audio";

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    public static String inputStreamToString(InputStream isTxt) throws IOException {
        int size = isTxt.available();
        byte[] buffer = new byte[size];
        isTxt.read(buffer);
        isTxt.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public static String createAudioPath(String fileName) {
        return AudioAssetpath + "/" + fileName;
    }
}

package com.loki;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by loki on 2017/12/3.
 */
public class Utils {

    final static int BUFFER_SIZE = 4096;

    public static byte[] InputStreamTOArray(InputStream in) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while((count = in.read(data,0,BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = outStream.toByteArray();
        return bytes;
    }

    public static InputStream byteTOInputStream(byte[] in) {

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }


    public static void sys(byte[] in) {

        System.out.println(in.toString());
        for (int i = 0; i <in.length ; i++) {
            System.out.println(in[i]);
        }

        System.out.println("==================");
    }


}

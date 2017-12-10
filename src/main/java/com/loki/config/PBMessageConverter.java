package com.loki.config;

/**
 * Created by loki on 2017/12/2.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.google.protobuf.GeneratedMessage;

/**
 * pb对象转换，自动完成pb对象的序列化与反序列化，让controller的接口脱离这些重复工作
 * @author shannon
 * @time Dec 6, 2012 12:16:11 PM
 * @email shannonchou@126.com
 */
public class PBMessageConverter extends AbstractHttpMessageConverter<GeneratedMessage> {


    public PBMessageConverter() {
        //设置该转换器支持的媒体类型
        super(new MediaType("application", "x-protobuf", Charset.forName("UTF-8")));
    }

    @Override
    protected GeneratedMessage readInternal(
            Class<? extends GeneratedMessage> arg0, HttpInputMessage arg1)
            throws IOException, HttpMessageNotReadableException {
        Method parseMethod;
        try {
            //利用反射机制获得parseFrom方法
            parseMethod = arg0.getDeclaredMethod("parseFrom", InputStream.class);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        try {
            //调用parseFrom方法从InputStream中反序列化PB对象
            return (GeneratedMessage) parseMethod.invoke(arg0, arg1.getBody());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected boolean supports(Class<?> arg0) {
        //如果是GeneratedMessage的子类则支持
        return GeneratedMessage.class.isAssignableFrom(arg0);
    }

    @Override
    protected void writeInternal(GeneratedMessage arg0, HttpOutputMessage arg1)
            throws IOException, HttpMessageNotWritableException {
        //不用自己设置type，父类会根据构造函数中给的type设置
        //arg1.getHeaders().setContentType(new MediaType("application","x-protobuf", Charset.forName("UTF-8")));
        OutputStream outputStream = arg1.getBody();

        //自己直接设置contentLength会导致异常，覆盖父类的getContentLength（）方法才是标准做法
        //arg1.getHeaders().setContentLength(bytes.length);
        arg0.writeTo(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    protected Long getContentLength(GeneratedMessage t, MediaType contentType) {
        return Long.valueOf(t.toByteArray().length);
    }
}
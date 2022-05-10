package com.lespayne.smartwear.dataHelper;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {
    public static boolean isSD = false;

    //将网络地址转化为输入流对象
    public static InputStream getInputStreamByPath(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(3000);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == httpURLConnection.HTTP_OK) {
            return httpURLConnection.getInputStream();
        }
        return null;
    }

    //将输入流转化为字节数组
    public static byte[] getByteArrayByInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    //降输入流转化为字符串
    public static String getStringInputStream(InputStream inputStream) throws IOException {
        return new String(getByteArrayByInputStream(inputStream));
    }

    //解析xml
    //START_DOCUMENT:表示xml开始解析。END_DOCUMENT:表示xml解析结束

    public static String getByXmlString(String xml) throws Exception {
        String shidu = null;
        //得到Pull解析器对象
        XmlPullParser xmlPullParser = Xml.newPullParser();
        //设置需要解析的Xml数据
        xmlPullParser.setInput(new StringReader(xml));
        //得到事件对象
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {//开始标签
                String startTag = xmlPullParser.getName();
                if (startTag.equals("shidu")) {
                    isSD = true;
                }
            } else if (eventType == XmlPullParser.TEXT) {//文本内容
                String text = xmlPullParser.getText();
                if (isSD) {
                    shidu = xmlPullParser.getText();
                    isSD = false;
                }
            }
            eventType = xmlPullParser.next();
        }
        return shidu;
    }
}
package com.example.mac.carwash.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 访问接口类
 * Created by xk on 2017/7/4.
 */

public class DataUtil {
    //命名空间和方法名一起放入SoapObject对象中
    private static final String namespace = "http://ep.pub.wqsm.gaf.com/";

    //图片上传外网
    private static String url_img = "http://cetc.me";

    //外网图片下载前缀
    public static String url_img_down = "http://pic.yiqisong.com.cn/";

    //java外网
    //public static String URL = "http://cetc.me/";

    //内网（刘）
//    public static String URL = "http://192.168.0.110:8080/cetc/";

    //java内网（华总）
    public static String URL = "http://192.168.0.193:8080/cetc/";

    //java内网(蔡哥)
//    public static String URL = "http://192.168.0.110:8282/cetc/";

    /**
     * @param methodName-接口方法名
     * @param json-传入的数据
     * @param suffix URL的拼接后缀
     * @return-服务器返回的数据
     */
    public static String callWebService(String suffix, String methodName, String json) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        try {
            HttpTransportSE transport = null;
            transport = new HttpTransportSE(URL + suffix, 40000);
            transport.debug = true;
            SoapObject soapObject = new SoapObject(namespace,methodName);
            soapObject.addProperty("reqxml", json);
    //        envelope.headerOut = getSoapHeader(); 添加请求头部操作，这里不需要
            envelope.setOutputSoapObject(soapObject);
            envelope.bodyOut = soapObject;
            transport.call(null, envelope);//namespace + methodName
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SoapObject result = null;
            String str = envelope.getResponse().toString();
            if (str == null) {
                return null;
            }
            str = str.substring(1, str.length() - 1);
            return str;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
}

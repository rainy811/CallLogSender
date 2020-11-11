package com.example.tongxunluf.upload;

import android.os.Environment;

import com.example.tongxunluf.callLog.CallLogInfosUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.utils.ContextUtil;
import com.example.tongxunluf.utils.NotifyUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Upload {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String MYURL = "http://49.235.3.119:80/Service.asmx";
    private static final String METHOD = "SaveFile";
    private static final String SB = "SB";
    private static final String FILENAME = "fileName";
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TEXT/";

    public static void upload() {
        //Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + bshift.replace("/", "-")
        // + "  " + salesmanName + "  通话数量：" + oo + "  通话时长" + durationSum + "s" + ".csv"
        String salesman = SalesNameUtil.getSalesName();
        String message = CallLogInfosUtils.getMessage();
        String fileName = PATH + salesman;

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD);
        soapObject.addProperty(SB, message);
        soapObject.addProperty(FILENAME, fileName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(MYURL);
        httpTransportSE.debug = true;

        try {
            httpTransportSE.call(NAMESPACE + METHOD, envelope);

        } catch (HttpResponseException | XmlPullParserException e) {
            e.printStackTrace();
            NotifyUtil.notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败，若手动发送依旧失败，请邮件联系Tech");
        } catch (IOException e) {
            e.printStackTrace();
            NotifyUtil.notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败，若手动发送依旧失败，请邮件联系Tech");
        }
    }

    public static void sendBySoap(){
        new Thread(){
            @Override
            public void run(){
                upload();
            }
        }.start();
    }
}

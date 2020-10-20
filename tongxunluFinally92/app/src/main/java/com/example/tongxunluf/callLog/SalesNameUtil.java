package com.example.tongxunluf;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SalesNameUtil {

    private String name;

    private static final String nameSpace = "http://tempuri.org/";
    private static final String Myurl = "http://49.235.3.119:80/Service.asmx";
    private static final String Mymethod2 = "IMEI";

    public String getName() {
        return name;
    }

    public String getSalesName(String imei) {
        SoapObject soapObject2;
        soapObject2 = new SoapObject(nameSpace, Mymethod2);
        soapObject2.addProperty("imei", imei);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject2;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject2);
        HttpTransportSE httpTransportSE1 = new HttpTransportSE(Myurl);
        httpTransportSE1.debug = true;
        String name = "未搜索到销售名";
        try {
            httpTransportSE1.call(nameSpace + Mymethod2, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            name = object.getProperty(0).toString();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return name;
    }
}

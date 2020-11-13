package com.ocr.common.constant;

import java.util.HashMap;
/*
 * 图像类型的静态类，方便存取
 * 若有新增、删除请务必在getChannelType，getChannelType2两个方法中都新增、删除
 *
 */
public class ChannelTypeConstants {
    public static final String SYS_USER = "SYS_USER";
    private static HashMap hashMap=null ;
    private static HashMap hashMap2=null ;
    public static HashMap<String,String> getChannelType(){
        if(hashMap==null){
            hashMap=new HashMap<String,String>();
            hashMap.put("0","None");
            hashMap.put("1","IDCardFront");
            hashMap.put("2","BankCard");
            hashMap.put("3","Deposit");
            hashMap.put("4","PremisesPermit");
            hashMap.put("6","ResidenceBooklet");
            hashMap.put("7","MarriageLicense");
            hashMap.put("8","DrivingLicense");
            hashMap.put("9","DriversLicense");
            hashMap.put("10","PlateNumber");
            hashMap.put("11","BusinessLicense");
            hashMap.put("12","VatInvoice");
            hashMap.put("13","Invoice");
            hashMap.put("14","Itinerary");
            hashMap.put("15","RalTicket");
            hashMap.put("16","TollInvoice");
            hashMap.put("17","QuotaInvoice");
            hashMap.put("18","EleInvoice");
            hashMap.put("10000","GeneralText");
            hashMap.put("10001","IDCardFront_Video");
            hashMap.put("10002","IDCardBack_Video");
            hashMap.put("10003","PremisesPermit_Video");
            hashMap.put("10004","BusinessLicense_Video");

            hashMap.put("None","None");
            hashMap.put("IDCardFront","IDCardFront");
            hashMap.put("BankCard","BankCard");
            hashMap.put("Deposit","Deposit");
            hashMap.put("PremisesPermit","PremisesPermit");
            hashMap.put("ResidenceBooklet","ResidenceBooklet");
            hashMap.put("MarriageLicense","MarriageLicense");
            hashMap.put("DrivingLicense","DrivingLicense");
            hashMap.put("DriversLicense","DriversLicense");
            hashMap.put("PlateNumber","PlateNumber");
            hashMap.put("BusinessLicense","BusinessLicense");
            hashMap.put("VatInvoice","VatInvoice");
            hashMap.put("Invoice","Invoice");
            hashMap.put("Itinerary","Itinerary");
            hashMap.put("RalTicket","RalTicket");
            hashMap.put("TollInvoice","TollInvoice");
            hashMap.put("QuotaInvoice","QuotaInvoice");
            hashMap.put("EleInvoice","EleInvoice");
            hashMap.put("GeneralText","GeneralText");
            hashMap.put("IDCardFront_Video","IDCardFront_Video");
            hashMap.put("IDCardBack_Video","IDCardBack_Video");
            hashMap.put("PremisesPermit_Video","PremisesPermit_Video");
            hashMap.put("BusinessLicense_Video","BusinessLicense_Video");
        }

        return hashMap;
    }

    public static HashMap<String,String> getChannelType2(){
        if(hashMap2==null){
            hashMap2=new HashMap<String,String>();
            hashMap2.put("0","无效类型");
            hashMap2.put("1","身份证");
            hashMap2.put("2","银行卡");
            hashMap2.put("3","存单");
            hashMap2.put("4","房本");
            hashMap2.put("6","户口本");
            hashMap2.put("7","结婚证");
            hashMap2.put("8","行驶证");
            hashMap2.put("9","驾驶证");
            hashMap2.put("10","车牌号");
            hashMap2.put("11","营业执照");
            hashMap2.put("12","增值税发票");
            hashMap2.put("13","普通发票");
            hashMap2.put("14","航空运输电子客票行程单");
            hashMap2.put("15","火车票");
            hashMap2.put("16","通行费发票");
            hashMap2.put("17","定额发票");
            hashMap2.put("18","电子发票");
            hashMap2.put("10000","通用文字识别");
            hashMap2.put("10001","身份证正面");
            hashMap2.put("10002","身份证反面");
            hashMap2.put("10003","房本");
            hashMap2.put("10004","营业执照");

            hashMap2.put("None","0");
            hashMap2.put("IDCardFront","1");
            hashMap2.put("BankCard","2");
            hashMap2.put("Deposit","3");
            hashMap2.put("PremisesPermit","4");
            hashMap2.put("ResidenceBooklet","6");
            hashMap2.put("MarriageLicense","7");
            hashMap2.put("DrivingLicense","8");
            hashMap2.put("DriversLicense","9");
            hashMap2.put("PlateNumber","10");
            hashMap2.put("BusinessLicense","11");
            hashMap2.put("VatInvoice","12");
            hashMap2.put("Invoice","13");
            hashMap2.put("Itinerary","14");
            hashMap2.put("RalTicket","15");
            hashMap2.put("TollInvoice","16");
            hashMap2.put("QuotaInvoice","17");
            hashMap2.put("EleInvoice","18");
            hashMap2.put("GeneralText","10000");
            hashMap2.put("IDCardFront_Video","10001");
            hashMap2.put("IDCardBack_Video","10002");
            hashMap2.put("PremisesPermit_Video","10003");
            hashMap2.put("BusinessLicense_Video","10004");
        }

        return hashMap2;
    }
}

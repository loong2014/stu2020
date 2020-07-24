package com.sunny.apk;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by wangsk on 2020/5/18.
 */
public class APKUtil {
    public static final String VERSION_NAME_KEY = "versionName";
    public static final String VERSION_CODE_KEY = "versionCode";

    /**
     * @param filePath apk路径，例如：/home/ /Qk_test.apk
     * @return versionCodee
     * @throws Exception
     */
    public static Map<String, String> getVersionCodeAndName(String filePath) throws Exception {

        Map<String, String> versionMap = new HashMap<>();
        ZipFile zip = null;
        try {

            File file = new File(filePath);
            zip = new ZipFile(file);
            Enumeration enume = zip.entries();

            String filename = null;
            ZipEntry zipEntry = null;
            while (enume.hasMoreElements()) {
                zipEntry = (ZipEntry) enume.nextElement();
                filename = zipEntry.getName();
                if ("AndroidManifest.xml".equalsIgnoreCase(filename)) {
//                    InputStream inputStream = zip.getInputStream(zipEntry);
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    int size = bufferedInputStream.available();
//                    byte[] data = new byte[size];
//                    bufferedInputStream.read(data);
//                    System.out.println(new String(data));
                    String versionCode = getValue(zip.getInputStream(zipEntry), VERSION_CODE_KEY);
                    String versionName = getValue(zip.getInputStream(zipEntry), VERSION_NAME_KEY);
                    versionMap.put(VERSION_CODE_KEY, versionCode);
                    versionMap.put(VERSION_NAME_KEY, versionName);

                    break;
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (null != zip)
                    zip.close();
            } catch (Exception e) {
            }
        }

        return versionMap;
    }

    private static String getValue(InputStream is, String key) throws Exception {

        String versionCode = null;
        try {
            AXmlResourceParser parser = new AXmlResourceParser();
            parser.open(is);

            boolean brek = false;
            while (true) {
                int type = parser.next();
                if (type == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                switch (type) {
                    case XmlPullParser.START_TAG:
                        System.out.println("parser.getAttributeCount() :"+parser.getAttributeCount());

                        for (int i = 0; i != parser.getAttributeCount(); ++i) {
//                            if (key.equals(parser.getAttributeName(i))) {
//                                versionCode = getAttributeValue(parser, i);
//                                brek = true;
//                                break;
//                            }
                        }
                }

                if (brek) {
                    break;
                }
            }

        } catch (Exception e) {
            throw e;
        }

        return versionCode;
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == TypedValue.TYPE_STRING) {
            return parser.getAttributeValue(index);
        }
        if (type == TypedValue.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == TypedValue.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == TypedValue.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (type == TypedValue.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (type == TypedValue.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data))
                    + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type == TypedValue.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data))
                    + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
        }
        if (type >= TypedValue.TYPE_FIRST_COLOR_INT
                && type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (type >= TypedValue.TYPE_FIRST_INT
                && type <= TypedValue.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    private static float complexToFloat(int complex) {
        return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private static final float RADIX_MULTS[] = {0.00390625F, 3.051758E-005F,
            1.192093E-007F, 4.656613E-010F};
    private static final String DIMENSION_UNITS[] = {"px", "dip", "sp", "pt", "in",
            "mm", "", ""};
    private static final String FRACTION_UNITS[] = {"%", "%p", "", "", "", "", "", ""};

    public static void main(String[] args) {
        try {
            Map<String, String> map = null;
            map = getVersionCodeAndName("/Users/qazwsx/Desktop/tv-letvApp-cibn-release.apk");
//            map = getVersionCodeAndName("/Users/qazwsx/Desktop/litehost-release.apk");
//            map = getVersionCodeAndName("/Users/qazwsx/Desktop/app-unityplayer.apk");

            System.out.println("print k-v");
            for (String k : map.values()) {
                System.out.println(k);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

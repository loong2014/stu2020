package com.sunny.lib.common.utils;


import com.sunny.lib.utils.AppConfigUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Domain related utils.
 * Created by cuixiaobo on 6/3/16.
 */
public class DomainUtils {

    private static final String LETVIMG = ".letvimg.com";
    private static final String LETVIMG_CIBN = ".img.cp21.ott.cibntv.net";
    private static final String LETVIMG_MG = "-img-letv.yyssh.mgtv.com";


    private static final Map<String, String> CIBN_DOMAIN_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> MGTV_DOMAIN_MAP = new ConcurrentHashMap<>();
    private static Map<String, String> sDomainMap = null;

    static {
        setDomainInfo("i0.letvimg.com", "i0.img.cp21.ott.cibntv.net", "i0-img-letv.yysh.mgtv.com");
        setDomainInfo("i1.letvimg.com", "i1.img.cp21.ott.cibntv.net", "i1-img-letv.yysh.mgtv.com");
        setDomainInfo("i2.letvimg.com", "i2.img.cp21.ott.cibntv.net", "i2-img-letv.yysh.mgtv.com");
        setDomainInfo("i3.letvimg.com", "i3.img.cp21.ott.cibntv.net", "i3-img-letv.yysh.mgtv.com");

        setDomainInfo("static.letvcdn.com", "static.cdn.cp21.ott.cibntv.net", "static-cdn-letv.yysh.mgtv.com");
        setDomainInfo("g3.letv.cn", "g3cn.cp21.ott.cibntv.net", "g3cn-letv.yysh.mgtv.com");
        setDomainInfo("g3.letv.com", "g3com.cp21.ott.cibntv.net", "g3com-letv.yysh.mgtv.com");
    }

    private static void setDomainInfo(String letv, String cibn, String mgtv) {
        CIBN_DOMAIN_MAP.put(letv, cibn);
        MGTV_DOMAIN_MAP.put(letv, mgtv);
    }

    private static Map<String, String> getDomainMap() {
        if (sDomainMap == null) {
            if (AppConfigUtils.isMgtvBroadcast()) {
                sDomainMap = MGTV_DOMAIN_MAP;
            } else {
                sDomainMap = CIBN_DOMAIN_MAP;
            }
        }
        return sDomainMap;
    }

    /**
     * 图片地址域名替换
     */
    public static String urlDomainReplace(String url) {
        if (SunStrUtils.equalsNull(url)) {
            return "";
        }
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return url;
        }

        String letvDomain = u.getHost();
        String newDomain = getDomainMap().get(letvDomain);

        if (newDomain != null) {
            url = url.replace(letvDomain, newDomain);

        } else if (letvDomain.contains(LETVIMG)) {
            if (AppConfigUtils.isMgtvBroadcast()) {
                String subU = letvDomain.substring(0, letvDomain.indexOf(LETVIMG));
                String subNew = letvDomain.replace(subU, subU.replace('.', '-'));
                newDomain = subNew.replace(LETVIMG, LETVIMG_MG);

                MGTV_DOMAIN_MAP.put(letvDomain, newDomain);
            } else {

                newDomain = letvDomain.replace(LETVIMG, LETVIMG_CIBN);

                CIBN_DOMAIN_MAP.put(letvDomain, newDomain);
            }

            url = url.replace(letvDomain, newDomain);
        }

        return url;
    }
}

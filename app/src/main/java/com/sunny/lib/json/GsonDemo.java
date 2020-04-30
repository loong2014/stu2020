package com.sunny.lib.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangxin17 on 2020-04-20
 */
public class GsonDemo {

    /**
     * activity_id : 5e943bcd6128556c418b45e7
     * activity_timeout : -1
     * code : {"errno":"10000","errmsg":"","data":{"msgType":"voiceAnimCard","list":[{"animVoiceKey":"13","animAction":3,"paramType":20,"paramValue":"{\"params\": \"{\\\"callType\\\": 2,\\\"type\\\": 101,\\\"resource\\\": 8,\\\"action\\\": \\\"com.letv.external.unityplayer\\\",\\\"from\\\":\\\"com.kuyun.common.androidtv\\\",\\\"value\\\": \\\"{\\\\\\\"animVoiceKey\\\\\\\": \\\\\\\"13\\\\\\\",\\\\\\\"result\\\\\\\": \\\\\\\"0\\\\\\\"}\\\"}\"}}"}]}}
     * creativeid : 5e958252723b12304d8b45c0-1586945280
     * dataType : 41
     * errno : 10000
     * img : http://picture-scloud.cp21.ott.cibntv.net/stargazer/1586856472636_615.png
     * jump : {"extend":{"action":"com.android.browser.external","appPackageName":"com.android.browser","dataType":0,"isParse":1,"resource":"1"},"type":41,"value":{"type":1,"value":{"url":"https://v.qq.com/x/cover/mzc00200qpetzpe.html","fullscreen":"true","jsfocus":"false","focus":"true","webplaymode":"true","forcereload":"false","wideview":"false","ua":"0","from":"com.kuyun.common.androidtv"}}}
     * key : 13
     * position : album_interaction_ad
     * report : {"admaster":{"click":"","imp":""},"miaozhen":{"click":"","imp":""},"posid":"album_interaction_ad","promoId":"5e943bcd6128556c418b45e7","reqid":"2ea3e866b2d94750a07f2e462d72bc5b"}
     * reqid : 2ea3e866b2d94750a07f2e462d72bc5b
     * sessionid : e90d40dca1084385b868bb3545f1bfd4
     */

    private void demo(){
        int m =10;
        int n =20;

        Integer i;
        Long l;
    }

    private String activity_id;
    private int activity_timeout;
    private String code;
    private String creativeid;
    private int dataType;
    private String errno;
    private String img;
    private JumpBean jump;
    private String key;
    private String position;
    private ReportBean report;
    private String reqid;
    private String sessionid;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public int getActivity_timeout() {
        return activity_timeout;
    }

    public void setActivity_timeout(int activity_timeout) {
        this.activity_timeout = activity_timeout;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreativeid() {
        return creativeid;
    }

    public void setCreativeid(String creativeid) {
        this.creativeid = creativeid;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public JumpBean getJump() {
        return jump;
    }

    public void setJump(JumpBean jump) {
        this.jump = jump;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ReportBean getReport() {
        return report;
    }

    public void setReport(ReportBean report) {
        this.report = report;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public static class JumpBean {
        /**
         * extend : {"action":"com.android.browser.external","appPackageName":"com.android.browser","dataType":0,"isParse":1,"resource":"1"}
         * type : 41
         * value : {"type":1,"value":{"url":"https://v.qq.com/x/cover/mzc00200qpetzpe.html","fullscreen":"true","jsfocus":"false","focus":"true","webplaymode":"true","forcereload":"false","wideview":"false","ua":"0","from":"com.kuyun.common.androidtv"}}
         */

        private ExtendBean extend;
        private int type;
        private ValueBeanX value;

        public ExtendBean getExtend() {
            return extend;
        }

        public void setExtend(ExtendBean extend) {
            this.extend = extend;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ValueBeanX getValue() {
            return value;
        }

        public void setValue(ValueBeanX value) {
            this.value = value;
        }

        public static class ExtendBean {
            /**
             * action : com.android.browser.external
             * appPackageName : com.android.browser
             * dataType : 0
             * isParse : 1
             * resource : 1
             */

            private String action;
            private String appPackageName;
            private int dataType;
            private int isParse;
            private String resource;

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public String getAppPackageName() {
                return appPackageName;
            }

            public void setAppPackageName(String appPackageName) {
                this.appPackageName = appPackageName;
            }

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
            }

            public int getIsParse() {
                return isParse;
            }

            public void setIsParse(int isParse) {
                this.isParse = isParse;
            }

            public String getResource() {
                return resource;
            }

            public void setResource(String resource) {
                this.resource = resource;
            }
        }

        public static class ValueBeanX {
            /**
             * type : 1
             * value : {"url":"https://v.qq.com/x/cover/mzc00200qpetzpe.html","fullscreen":"true","jsfocus":"false","focus":"true","webplaymode":"true","forcereload":"false","wideview":"false","ua":"0","from":"com.kuyun.common.androidtv"}
             */

            private int type;
            private ValueBean value;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public ValueBean getValue() {
                return value;
            }

            public void setValue(ValueBean value) {
                this.value = value;
            }

            public static class ValueBean {
                /**
                 * url : https://v.qq.com/x/cover/mzc00200qpetzpe.html
                 * fullscreen : true
                 * jsfocus : false
                 * focus : true
                 * webplaymode : true
                 * forcereload : false
                 * wideview : false
                 * ua : 0
                 * from : com.kuyun.common.androidtv
                 */

                private String url;
                private String fullscreen;
                private String jsfocus;
                private String focus;
                private String webplaymode;
                private String forcereload;
                private String wideview;
                private String ua;
                private String from;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getFullscreen() {
                    return fullscreen;
                }

                public void setFullscreen(String fullscreen) {
                    this.fullscreen = fullscreen;
                }

                public String getJsfocus() {
                    return jsfocus;
                }

                public void setJsfocus(String jsfocus) {
                    this.jsfocus = jsfocus;
                }

                public String getFocus() {
                    return focus;
                }

                public void setFocus(String focus) {
                    this.focus = focus;
                }

                public String getWebplaymode() {
                    return webplaymode;
                }

                public void setWebplaymode(String webplaymode) {
                    this.webplaymode = webplaymode;
                }

                public String getForcereload() {
                    return forcereload;
                }

                public void setForcereload(String forcereload) {
                    this.forcereload = forcereload;
                }

                public String getWideview() {
                    return wideview;
                }

                public void setWideview(String wideview) {
                    this.wideview = wideview;
                }

                public String getUa() {
                    return ua;
                }

                public void setUa(String ua) {
                    this.ua = ua;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }
            }
        }
    }

    public static class ReportBean {
        /**
         * admaster : {"click":"","imp":""}
         * miaozhen : {"click":"","imp":""}
         * posid : album_interaction_ad
         * promoId : 5e943bcd6128556c418b45e7
         * reqid : 2ea3e866b2d94750a07f2e462d72bc5b
         */

        private AdmasterBean admaster;
        private MiaozhenBean miaozhen;
        private String posid;
        private String promoId;
        private String reqid;

        public AdmasterBean getAdmaster() {
            return admaster;
        }

        public void setAdmaster(AdmasterBean admaster) {
            this.admaster = admaster;
        }

        public MiaozhenBean getMiaozhen() {
            return miaozhen;
        }

        public void setMiaozhen(MiaozhenBean miaozhen) {
            this.miaozhen = miaozhen;
        }

        public String getPosid() {
            return posid;
        }

        public void setPosid(String posid) {
            this.posid = posid;
        }

        public String getPromoId() {
            return promoId;
        }

        public void setPromoId(String promoId) {
            this.promoId = promoId;
        }

        public String getReqid() {
            return reqid;
        }

        public void setReqid(String reqid) {
            this.reqid = reqid;
        }

        public static class AdmasterBean {
            /**
             * click :
             * imp :
             */

            private String click;
            private String imp;

            public String getClick() {
                return click;
            }

            public void setClick(String click) {
                this.click = click;
            }

            public String getImp() {
                return imp;
            }

            public void setImp(String imp) {
                this.imp = imp;
            }
        }

        public static class MiaozhenBean {
            /**
             * click :
             * imp :
             */

            private String click;
            private String imp;

            public String getClick() {
                return click;
            }

            public void setClick(String click) {
                this.click = click;
            }

            public String getImp() {
                return imp;
            }

            public void setImp(String imp) {
                this.imp = imp;
            }
        }
    }
}

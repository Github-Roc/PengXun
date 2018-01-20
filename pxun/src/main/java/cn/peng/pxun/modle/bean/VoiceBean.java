package cn.peng.pxun.modle.bean;

import java.util.ArrayList;

/**
 * 语音识别的JavaBean
 */
public class VoiceBean {

    public ArrayList<WsBean> ws;


    public class WsBean{
        public ArrayList<CwBean> cw;
    }

    public class CwBean {
        public String w;
    }
}

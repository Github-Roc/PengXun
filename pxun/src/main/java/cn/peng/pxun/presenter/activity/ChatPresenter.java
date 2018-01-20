package cn.peng.pxun.presenter.activity;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.turing.androidsdk.TuringApiManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.peng.pxun.MyApplication;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.TuringBean;
import cn.peng.pxun.modle.bean.VoiceBean;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.modle.greendao.MessageDao;
import cn.peng.pxun.presenter.base.BasePresenter;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.activity.ChatActivity;
import cn.peng.pxun.utils.DateUtil;
import cn.peng.pxun.utils.ThreadUtil;
import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * ChatActivity的业务类
 */
public class ChatPresenter extends BasePresenter{
    private ChatActivity mActivity;
    /** 机器人管理器 */
    private TuringApiManager mTaManager;

    public ChatPresenter(BaseActivity activity) {
        super(activity);
        mActivity = (ChatActivity) activity;
    }

    /**
     * 显示语音录入的对话框
     * @param listener
     */
    public void showSpeechDialog(RecognizerDialogListener listener) {
        RecognizerDialog mDialog = new RecognizerDialog(mActivity, null);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        mDialog.setListener(listener);
        mDialog.show();
    }

    /**
     * 解析语音录入的每个字符
     * @param result
     * @return 解析后的字符
     */
    public String processData(String result) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(result, VoiceBean.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<VoiceBean.WsBean> ws = voiceBean.ws;
        for (VoiceBean.WsBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * 让机器人把汉字说出来
     * @param answerContent 要讲的话
     */
    public void startSpeak(String answerContent) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(mActivity, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录12.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "60");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(answerContent, null);
    }

    /**
     * 初始化图灵机器人
     */
    public void initTuring() {
        if(AppConfig.isInitTuring){
            mTaManager = new TuringApiManager(mActivity);
            mTaManager.setHttpListener(new HttpConnectionListener() {
                @Override
                public void onError(ErrorMessage message) {
                    Message msg = new Message();
                    msg.isTuring = true;
                    msg.message = "对不起,你的话太深奥了!";
                    msg.fromUserID = "tuling";
                    msg.toUserID = AppConfig.getUserId(AppConfig.appUser);
                    msg.messageType = Message.TEXT_TYPE;

                    mActivity.addDataAndRefreshUi(msg,false);
                }

                @Override
                public void onSuccess(RequestResult result) {
                    String s = result.getContent().toString();
                    Gson gson = new Gson();
                    TuringBean turing = gson.fromJson(s,TuringBean.class);

                    Message msg = new Message();
                    msg.isTuring = true;
                    msg.message = turing.text;
                    msg.fromUserID = "tuling";
                    msg.toUserID = AppConfig.getUserId(AppConfig.appUser);
                    if (turing.code == 200000){
                        msg.messageType = Message.PIC_TYPE;
                        msg.picURL = getPicURL();
                    }else {
                        msg.messageType = Message.TEXT_TYPE;
                    }

                    mActivity.addDataAndRefreshUi(msg,true);
                }
            });
        }
    }

    /**
     * 获取图灵机器人的聊天记录
     * @return
     */
    public List<Message> getTulingMessages() {
        MessageDao messageDao = MyApplication.session.getMessageDao();
        QueryBuilder queryBuilder = messageDao.queryBuilder();
        List<Message> list = queryBuilder.where(queryBuilder
                .or(queryBuilder
                        .and(MessageDao.Properties.FromUserID.eq(AppConfig.getUserId(AppConfig.appUser)),
                                MessageDao.Properties.ToUserID.eq("tuling")), queryBuilder
                        .and(MessageDao.Properties.FromUserID.eq("tuling"),
                                MessageDao.Properties.ToUserID.eq(AppConfig.getUserId(AppConfig.appUser)))))
                .list();
        return list;
    }

    /**
     * 从环信获取用户聊天记录
     * @param toChatUserId
     * @return
     */
    public List<Message> getHuanxinMessages(String toChatUserId) {
        List<Message> list = new ArrayList<>();
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUserId);
        if (conversation != null) {
            EMMessage lastMessage = conversation.getLastMessage();
            int count = 19;
            if (list.size() >= 19) {
                count = list.size() + 1;
            }
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            messages.add(lastMessage);

            if (messages != null && messages.size() > 0) {
                for (EMMessage emMsg : messages) {
                    Message msg = new Message();
                    msg.date = DateUtil.getDate(emMsg.getMsgTime());
                    msg.message = splitEmMessage(emMsg);
                    msg.fromUserID = emMsg.getFrom();
                    msg.toUserID = emMsg.getTo();
                    msg.messageType = Message.TEXT_TYPE;
                    msg.isTuring = false;
                    list.add(msg);
                }
            }
        }
        return list;
    }

    /**
     * 请求图灵机器人的接口
     * @param ask
     */
    public void requestTuring(String ask) {
        if (mTaManager != null){
            mTaManager.requestTuringAPI(ask);
        }
    }

    /**
     * 异步发送文本消息
     * @param msg
     */
    public void sendTextMessage(final String msg, final String toChatUserId, final boolean isGroup) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createTxtSendMessage(msg, toChatUserId);
                if (isGroup) {
                    message.setChatType(EMMessage.ChatType.GroupChat);
                }
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }

    /**
     * 随机获取一张图片的url
     * @return
     */
    public String getPicURL() {
        String pic_url = AppConfig.BELLE_PIC[new Random().nextInt(AppConfig.BELLE_PIC.length)];
        return pic_url;
    }

    /**
     * 保存聊天记录
     */
    public void keepMessage(Message msg) {
        MessageDao messageDao = MyApplication.session.getMessageDao();
        messageDao.insert(msg);
    }

}

package cn.peng.pxun.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.modle.greendao.Message;
import cn.peng.pxun.presenter.activity.ChatPresenter;
import cn.peng.pxun.ui.adapter.SuperBaseApapter;
import cn.peng.pxun.ui.adapter.holder.BaseHolder;
import cn.peng.pxun.ui.adapter.holder.ChatHolder;
import cn.peng.pxun.ui.adapter.recycleview.EmojiAdapter;
import cn.peng.pxun.ui.view.SmoothInputLayout;
import cn.peng.pxun.utils.DateUtil;
import cn.peng.pxun.utils.ToastUtil;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static cn.peng.pxun.R.id.ll_chat_more;

/**
 * 聊天页面
 * @author Peng
 * Created by msi on 2016/12/30.
 */
public class ChatActivity extends BaseActivity<ChatPresenter> {

    @BindView(R.id.chat_toolbar)
    Toolbar mChatToolbar;
    @BindView(R.id.iv_chat_goback)
    ImageView mIvChatGoback;
    @BindView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @BindView(R.id.lv_chat)
    ListView mLvChat;
    @BindView(R.id.sil_lyt_content)
    SmoothInputLayout mSilLytContent;
    @BindView(R.id.bt_chat_type)
    AppCompatImageButton mBtChatType;
    @BindView(R.id.bt_chat_emoji)
    AppCompatImageButton mBtChatEmoji;
    @BindView(R.id.bt_chat_more)
    AppCompatImageButton mBtChatMore;
    @BindView(R.id.et_chat_input)
    AppCompatEditText mEtChatInput;
    @BindView(R.id.bt_chat_speech)
    AppCompatButton mBtChatSpeech;
    @BindView(R.id.bt_chat_send)
    AppCompatImageButton mBtChatSend;
    @BindView(R.id.ll_chat_emoji)
    LinearLayout mLlChatEmoji;
    @BindView(ll_chat_more)
    LinearLayout mLlChatMore;
    private RecyclerView mRvMoreEmoji;
    private TextView mTvMorePicture;
    private TextView mTvMoreTakephoto;

    // 消息数据集合
    private List<Message> list;
    // 消息list的数据适配器
    private ChatAdapter chatAdapter;
    private EmojiAdapter emojiAdapter;

    // 是否是群聊
    private boolean isGroup = false;
    // 聊天用户
    private User toChatUser;
    // 聊天群组
    private Group toChatGroup;
    // 会话对象ID
    private String toChatId;
    // 会话对象名称
    private String toChatName;


    @Override
    protected void init() {
        super.init();
        //初始化科大讯飞语音识别
        SpeechUtility.createUtility(this, "appid=" + AppConfig.IFLYTEK_APPID);
        //注册EventBus
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        isGroup = intent.getBooleanExtra("isGroup", false);

        if (isGroup){
            toChatGroup = (Group) intent.getSerializableExtra("toChatGroup");
            toChatName = toChatGroup.getGroupName();
            toChatId = toChatGroup.getGroupNum();
        }else {
            toChatUser = (User) intent.getSerializableExtra("toChatUser");
            toChatName = toChatUser.getUsername();
            toChatId = AppConfig.getUserId(toChatUser);
        }

        list = new ArrayList<>();
        if ("tuling".equals(toChatId)) {
            presenter.initTuring();
            list = presenter.getTulingMessages();
        } else {
            list = presenter.getHuanxinMessages(toChatId);
        }
    }

    @Override
    public int setLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvChatTitle.setText(toChatName);
        if (list != null && list.size() > 0) {
            chatAdapter = new ChatAdapter(list);
            mLvChat.setAdapter(chatAdapter);
            mLvChat.setSelection(list.size() - 1);
        }
        mRvMoreEmoji = (RecyclerView) mLlChatEmoji.findViewById(R.id.rv_more_emoji);
        mRvMoreEmoji.setLayoutManager(new GridLayoutManager(this,6));
        emojiAdapter = new EmojiAdapter(this);
        mRvMoreEmoji.setAdapter(emojiAdapter);
        mTvMorePicture = (TextView) mLlChatMore.findViewById(R.id.tv_more_picture);
        mTvMoreTakephoto = (TextView) mLlChatMore.findViewById(R.id.tv_more_takephoto);
    }

    @Override
    protected void initListener() {
        mSilLytContent.setOnVisibilityChangeListener(new SmoothInputLayout.OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if (visibility == View.GONE) {
                    mBtChatEmoji.setSelected(false);
                } else {
                    mBtChatEmoji.setSelected(mLlChatEmoji.getVisibility() == View.VISIBLE);
                }
            }
        });
        mIvChatGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtChatType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtChatMore.setSelected(false);
                mBtChatEmoji.setSelected(false);
                if (mBtChatType.isSelected()) {
                    mBtChatType.setSelected(false);
                    showInputWidget();
                } else {
                    mBtChatType.setSelected(true);
                    mSilLytContent.closeInputPane();
                    mSilLytContent.closeKeyboard(true);
                    showVoiceWidget();
                }
            }
        });
        mBtChatEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtChatMore.setSelected(false);
                mBtChatType.setSelected(false);
                if (mBtChatEmoji.isSelected()) {
                    mBtChatEmoji.setSelected(false);
                    mSilLytContent.showKeyboard();
                } else {
                    mBtChatEmoji.setSelected(true);
                    showEmoji();
                }
            }
        });
        mBtChatMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtChatEmoji.setSelected(false);
                mBtChatType.setSelected(false);
                if (mBtChatMore.isSelected()) {
                    mBtChatMore.setSelected(false);
                    mSilLytContent.showKeyboard();
                    checkTextIsEmpty();
                } else {
                    mBtChatMore.setSelected(true);
                    showMore();
                }
            }
        });
        mBtChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String spellMsg = mEtChatInput.getText().toString().trim();
                if (TextUtils.isEmpty(spellMsg)) {
                    ToastUtil.showToast(ChatActivity.this, "发送消息不能为空");
                    return;
                }
                mEtChatInput.setText("");

                Message msg = new Message();
                msg.date = DateUtil.getDate(System.currentTimeMillis());
                msg.message = spellMsg;
                msg.fromUserID = AppConfig.getUserId(AppConfig.appUser);
                msg.messageType = Message.TEXT_TYPE;
                if ("智能小白".equals(toChatName)) {
                    if (AppConfig.isInitTuring){
                        msg.isTuring = true;
                        msg.toUserID = "tuling";
                        presenter.requestTuring(spellMsg);
                    }
                } else {
                    msg.isTuring = false;
                    msg.toUserID = toChatId;
                    presenter.sendTextMessage(spellMsg, toChatId, isGroup);
                }
                addDataAndRefreshUi(msg, false);
            }
        });
        mBtChatSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                }else{
                    showSpeechDialog();
                }
            }
        });
        mTvMorePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity,"相册");
            }
        });
        mTvMoreTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity,"拍照");
            }
        });
        emojiAdapter.setOnEmojiClickListener(new EmojiAdapter.OnEmojiClickListener() {
            @Override
            public void onEmojiClick(int emojoId, int position) {
                ToastUtil.showToast(mActivity,"emojo:"+position);
            }
        });
        mEtChatInput.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    mBtChatMore.setVisibility(View.GONE);
                    mBtChatSend.setVisibility(View.VISIBLE);
                } else {
                    mBtChatMore.setVisibility(View.VISIBLE);
                    mBtChatSend.setVisibility(View.GONE);
                }
            }
        });
        mEtChatInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mBtChatType.setSelected(false);
                mBtChatEmoji.setSelected(false);
                mBtChatMore.setSelected(false);
                return false;
            }
        });
        mLvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mBtChatType.setSelected(false);
                mBtChatEmoji.setSelected(false);
                mBtChatMore.setSelected(false);
                mSilLytContent.closeKeyboard(true);
                mSilLytContent.closeInputPane();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSilLytContent.isInputPaneOpen()) {
            mSilLytContent.closeInputPane();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示输入控件
     */
    private void showInputWidget() {
        checkTextIsEmpty();
        mEtChatInput.setVisibility(View.VISIBLE);
        mBtChatEmoji.setVisibility(View.VISIBLE);
        mBtChatSpeech.setVisibility(View.GONE);
        mSilLytContent.showKeyboard();
    }

    /**
     * 显示语音控件
     */
    private void showVoiceWidget() {
        mEtChatInput.setVisibility(View.GONE);
        mBtChatEmoji.setVisibility(View.VISIBLE);
        mBtChatSpeech.setVisibility(View.VISIBLE);
        mBtChatMore.setVisibility(View.VISIBLE);
        mBtChatSend.setVisibility(View.GONE);
    }

    /**
     *  显示Emoji面板
     */
    private void showEmoji() {

        checkTextIsEmpty();
        mEtChatInput.setVisibility(View.VISIBLE);
        mBtChatSpeech.setVisibility(View.GONE);
        mLlChatEmoji.setVisibility(View.VISIBLE);
        mLlChatMore.setVisibility(View.GONE);
        mSilLytContent.showInputPane(false);
    }

    /**
     * 显示更多面板
     */
    private void showMore() {
        mEtChatInput.setVisibility(View.VISIBLE);
        mBtChatSpeech.setVisibility(View.GONE);
        mLlChatEmoji.setVisibility(View.GONE);
        mLlChatMore.setVisibility(View.VISIBLE);
        mSilLytContent.showInputPane(false);
    }

    /**
     * 检测输入框文字是否为空
     */
    private void checkTextIsEmpty() {
        if (!TextUtils.isEmpty(mEtChatInput.getText().toString())){
            mBtChatMore.setVisibility(View.GONE);
            mBtChatSend.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 添加数据并且刷新UI
     * @param msg
     * @param isSpeak
     */
    public void addDataAndRefreshUi(Message msg, boolean isSpeak) {
        list.add(msg);
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(list);
            mLvChat.setAdapter(chatAdapter);
        }
        chatAdapter.setDataSets(list);
        chatAdapter.notifyDataSetChanged();
        mLvChat.setSelection(list.size() - 1);
        if (msg.isTuring) {
            presenter.keepMessage(msg);
            if (isSpeak) {
                presenter.startSpeak(msg.message);
            }
        }
    }

    /**
     * 检测是否有录音权限
     */
    private void checkPermission() {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));

        requestPermission(permissionItems, new PermissionCallback() {
            @Override
            public void onClose() {
                ToastUtil.showToast(mActivity, "授权失败");
            }

            @Override
            public void onFinish() {
                showSpeechDialog();
            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    /**
     * 显示语音录入的对话框
     */
    private void showSpeechDialog() {
        final StringBuffer mBuffer = new StringBuffer();
        presenter.showSpeechDialog(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String result = recognizerResult.getResultString();
                String resultString = presenter.processData(result);

                mBuffer.append(resultString);
                if (b) {
                    final String speechMsg = mBuffer.toString();

                    Message msg = new Message();
                    msg.date = DateUtil.getDate(System.currentTimeMillis());
                    msg.message = speechMsg;
                    msg.fromUserID = AppConfig.getUserId(AppConfig.appUser);
                    msg.messageType = Message.TEXT_TYPE;

                    if ("智能小白".equals(toChatName)) {
                        if (AppConfig.isInitTuring) {
                            msg.isTuring = true;
                            msg.toUserID = "tuling";
                            presenter.requestTuring(speechMsg);
                        }
                    } else {
                        msg.isTuring = false;
                        msg.toUserID = toChatId;
                        presenter.sendTextMessage(speechMsg, toChatId, isGroup);
                    }
                    addDataAndRefreshUi(msg, false);
                }
            }

            @Override
            public void onError(SpeechError error) {
                ToastUtil.showToast(ChatActivity.this, "语音解析失败");
            }
        });
    }

    /**
     * 收到新的消息
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(Message msg) {
        if(toChatId.equals(msg.fromUserID)){
            addDataAndRefreshUi(msg, false);
        }
    }

    class ChatAdapter extends SuperBaseApapter<Message> {
        public ChatAdapter(List<Message> dataSets) {
            super(dataSets);
        }

        @Override
        public BaseHolder setHolder() {
            return new ChatHolder(ChatActivity.this, toChatUser);
        }
    }
}

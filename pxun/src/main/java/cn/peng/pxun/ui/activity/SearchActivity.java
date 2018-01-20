package cn.peng.pxun.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.Group;
import cn.peng.pxun.modle.bmob.User;
import cn.peng.pxun.presenter.activity.SearchPresenter;
import cn.peng.pxun.ui.adapter.recycleview.SearchAdapter;
import cn.peng.pxun.utils.ToastUtil;

/**
 * 搜索界面   搜索好友,搜索群组
 */
public class SearchActivity extends BaseActivity<SearchPresenter> {
    public static final int SEARCH_USER = 1000;
    public static final int SEARCH_GROUP = 1001;

    @BindView(R.id.et_search_content)
    EditText mEtSearchContent;
    @BindView(R.id.tv_search_cancel)
    TextView mTvSearchCancel;
    @BindView(R.id.search_recycle)
    RecyclerView mSearchRecycle;

    private SearchAdapter adapter;
    private int searchType;

    @Override
    public int setLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public SearchPresenter initPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        searchType = intent.getIntExtra("searchType",SEARCH_USER);
    }

    @Override
    protected void initView() {
        super.initView();
        mSearchRecycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(this, presenter);
        mSearchRecycle.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        mEtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = mEtSearchContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)){
                        ToastUtil.showToast(mActivity,"搜索内容不能为空!");
                        return true;
                    }
                    showLoadingDialog("正在搜索...");
                    presenter.search(searchType,content);
                    return true;
                }
                return false;
            }
        });
//        mEtSearchContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String content = s.toString().trim();
//                presenter.search(searchType, content);
//            }
//        });
        mTvSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onUserSearch(List<User> list) {
        dismissLoadingDialog();
        if(list == null || list.size() == 0){
            ToastUtil.showToast(mActivity,"没有找到符合条件的用户");
            adapter.setUserData(null);
            return;
        }
        adapter.setUserData(list);
    }

    public void onGroupSearch(List<Group> list) {
        dismissLoadingDialog();
        if(list == null || list.size() == 0){
            ToastUtil.showToast(mActivity,"没有找到符合条件的群组");
            adapter.setUserData(null);
            return;
        }
        adapter.setGroupData(list);
    }
}

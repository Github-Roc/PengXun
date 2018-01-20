package cn.peng.pxun.presenter.fragment;

import cn.peng.pxun.presenter.base.BaseUserPresenter;
import cn.peng.pxun.ui.fragment.MineFragment;


/**
 * Created by msi on 2017/1/3.
 */
public class MinePresenter extends BaseUserPresenter {
    private MineFragment mMineFragment;

    public MinePresenter(MineFragment fragment) {
        super(fragment);
        this.mMineFragment = fragment;

        setListener();
    }

    private void setListener() {
        addUpLoadFileListener(new UpLoadFileListener() {
            @Override
            public void onUpLoadFinish(String path) {
                mMineFragment.onBgUpLoadFinish(path);
            }

            @Override
            public void onUpLoadProgress(int value) {
                mMineFragment.onBgUpLoadProgress(value);
            }
        });
        addUpdataUserListener(new UpdataUserListener() {
            @Override
            public void onResult(int result) {
                mMineFragment.updataResult(result);
            }
        });
    }

}

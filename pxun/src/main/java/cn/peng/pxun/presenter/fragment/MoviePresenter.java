package cn.peng.pxun.presenter.fragment;


import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.modle.bean.MovieBean;
import cn.peng.pxun.presenter.base.BaseRetrofitPresenter;
import cn.peng.pxun.service.MovieService;
import cn.peng.pxun.ui.fragment.MovieFragment;
import cn.peng.pxun.utils.LogUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tofirst on 2017/10/30.
 */

public class MoviePresenter extends BaseRetrofitPresenter {
    private MovieFragment mMovieFragment;

    private MovieService mApi;

    public MoviePresenter(MovieFragment fragment) {
        super(fragment);
        this.mMovieFragment = fragment;

        mApi = mRetrofit.create(MovieService.class);
    }

    public void getMovieList(int start, int end) {
        beforeExecute(mApi.getInTheatersMovieList(start, end))
            .subscribe(new Observer<MovieBean>() {
                @Override
                public void onSubscribe(@NonNull Disposable disposable) {

                }

                @Override
                public void onNext(@NonNull MovieBean movieBean) {
                    mMovieFragment.setData(movieBean, AppConfig.SUCCESS);
                }

                @Override
                public void onError(@NonNull Throwable throwable) {
                    mMovieFragment.setData(null, AppConfig.ERROR);
                    LogUtil.e("error:" + throwable.toString());
                }

                @Override
                public void onComplete() {

                }
        });
    }
}

package cn.peng.pxun.presenter.base;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tofirst on 2017/10/30.
 */

public class BaseRetrofitPresenter extends BasePresenter{
    protected Retrofit mRetrofit;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                    return chain.proceed(builder.build());
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public BaseRetrofitPresenter(BaseActivity activity) {
        super(activity);
        initRetrofit();
    }

    public BaseRetrofitPresenter(BaseFragment fragment) {
        super(fragment);
        initRetrofit();
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_MOVIE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    protected  <D> Observable<D> beforeExecute(Observable<D> observable){
        return observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!isNetUsable(mContext)) {
                            showToast( "请检查网络连接");
                            disposable.dispose();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}

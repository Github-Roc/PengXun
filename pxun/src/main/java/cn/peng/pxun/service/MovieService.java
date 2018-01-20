package cn.peng.pxun.service;

import cn.peng.pxun.modle.bean.MovieBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tofirst on 2017/10/31.
 */

public interface MovieService {

    /**
     * 正在热映
     * @param start
     * @param count
     * @return
     */
    @GET("in_theaters")
    Observable<MovieBean> getInTheatersMovieList(@Query("start") int start, @Query("count")int count);

    /**
     * 即将上映
     * @param start
     * @param count
     * @return
     */
    @GET("coming_soon")
    Observable<MovieBean> getComingSoonMovieList(@Query("start") int start, @Query("count")int count);

    /**
     * top250
     * @param start
     * @param count
     * @return
     */
    @GET("top250")
    Observable<MovieBean> getTop250MovieList(@Query("start") int start, @Query("count")int count);

    /**
     * 口碑榜
     * @param start
     * @param count
     * @return
     */
    @GET("weekly")
    Observable<MovieBean> getWeeklyMovieList(@Query("start") int start, @Query("count")int count);

    /**
     * 北美票房榜
     * @param start
     * @param count
     * @return
     */
    @GET("us_box")
    Observable<MovieBean> getUsboxMovieList(@Query("start") int start, @Query("count")int count);

     /**
     * 新片榜
     * @param start
     * @param count
     * @return
     */
    @GET("new_movies")
    Observable<MovieBean> getNewMovieList(@Query("start") int start, @Query("count")int count);

}

package com.jiajun.demo.network.api;


import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.homepage.entities.GuideImagesBean;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/*******************************************************************
 * * * * *   * * * *   *     *       Created by OCN.Yang
 * *     *   *         * *   *       Time:2017/2/24 14:53.
 * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public interface HomePageApi {
    @POST("marke_Interfaces.jsp")
    Observable<BaseBean<HomePageBean>> getHomePage(@QueryMap Map<String, String> map);
}

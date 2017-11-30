package com.jiajun.demo.network.api;


import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.account.entities.LoginBean;
import com.jiajun.demo.moudle.homepage.entities.GuideImagesBean;

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


public interface GetGuideImagesApi {
    @POST("marke_Interfaces.jsp")
    Observable<BaseBean<GuideImagesBean>> getGuideImages(@QueryMap Map<String, String> map);
}

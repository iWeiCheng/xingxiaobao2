package com.jiajun.demo.network.api;


import com.jiajun.demo.model.BaseBean;

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


public interface RegisterApi {
    @POST("marke_Interfaces.jsp")
    Observable<BaseBean<Object>> register(@QueryMap Map<String, String> map);
}

package com.workerassistant.network;

import com.workerassistant.bean.PersonBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by eva on 2017/4/21.
 */

public class netService {

//    当  path 是绝对路径的形式时：
//    path = "/apath"，baseUrl = "http://host:port/a/b"
//    Url = "http://host:port/apath"

     public interface PersonService{
        @GET("person")
        Call<List<PersonBean>> getPerson(
        );

//        @FormUrlEncoded
//        @POST("person")
//        Call<String> insertPerson(@Field("name") String name,
//                                  @Field("phone") String phone,
//                                   @Field("age") String age,
//                                  @Field("level") String level,
//                                  @Field("city") String city,
//                                  @Field("workType") String workType
//        );
//    @Headers({"Content-Type: application/json; charset=utf-8"})//需要添加头
    @POST("person/")
    Call<PersonBean> insertOnePerson(@Body PersonBean route);//传入的参数为RequestBody


//    @Headers({"Content-Type: application/json; charset=utf-8"})//需要添加头
    @POST("person")
         Call<PersonBean> insertPerson(@Body RequestBody route);//传入的参数为RequestBody
    }



    public interface RequestServes {
        @POST("mobileLogin/submit.html")
        Call<String> getString(@Query("loginname") String loginname,
                               @Query("nloginpwd") String nloginpwd);
    }
}

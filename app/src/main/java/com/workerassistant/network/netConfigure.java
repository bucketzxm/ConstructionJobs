package com.workerassistant.network;

import android.util.Log;

import com.google.gson.Gson;
import com.workerassistant.bean.PersonBean;
import com.workerassistant.bean.ProjectBean;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by eva on 2017/4/21.
 */

public class netConfigure {
    public static String TAG = "netConfigure";
    public static String baseUrl= "http://10.101.1.151:27017/";
    static Retrofit retrofit = null;
    private static volatile netConfigure self;
    public Observable observableAllPersonData;

//    返回null时候对应的Converter
    public static class NobodyConverterFactory extends Converter.Factory {
        public static final NobodyConverterFactory create() {
            return new NobodyConverterFactory();
        }
        private NobodyConverterFactory() {
        }
        //将响应对象responseBody转成目标类型对象(也就是Call里给定的类型)
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            //判断当前的类型是否是我们需要处理的类型
            if (PersonBean.class.equals(type) || ProjectBean.class.equals(type)) {
                //是则创建一个Converter返回转换数据
                return new Converter<ResponseBody, NoBodyEntity>() {
                    @Override
                    public NoBodyEntity convert(ResponseBody value) throws IOException {
                        //这里直接返回null是因为我们不需要使用到响应体,本来也没有响应体.
                        //返回的对象会存到response.body()里.
                        return null;
                    }
                };
            }
            return null;
        }

        //其它两个方法不需要使用到.所以不需要重写
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return null;
        }

        @Override
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
                                                    Retrofit retrofit) {
            return null;
        }
    }

    //懒汉法
    public static netConfigure getInstance(){
        if (self == null) {
            synchronized (netConfigure.class) {
                if (self == null) {
                    self = new netConfigure();
                    init();
                }
            }
        }
        return self;
    }
    private static void init(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为null的支持
                .addConverterFactory(NobodyConverterFactory.create())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持


//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public netService.ApiService getPersonService(){
        netService.ApiService apiService = retrofit.create(netService.ApiService.class);
        return apiService;
    }

    public Observable getObservableAllPersonData(){
        netService.ApiService apiService = retrofit.create(netService.ApiService.class);
        final Call<List<PersonBean>> callAllPerson = apiService.getPerson();

        observableAllPersonData = Observable.create(new Observable.OnSubscribe<List<PersonBean>>() {
            @Override
            public void call(Subscriber<? super List<PersonBean>> subscriber) {
                Response<List<PersonBean>> beanResponse = null;
                try {
                    beanResponse = callAllPerson.execute();
                    subscriber.onNext(beanResponse.body());
                }catch (Exception e){
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
        return observableAllPersonData;
    }

    public void LogGetAllPersonData(){
        netService.ApiService apiService = retrofit.create(netService.ApiService.class);
        final Call<List<PersonBean>> callAllPerson = apiService.getPerson();

        callAllPerson.enqueue(new Callback<List<PersonBean>>() {
            @Override
            public void onResponse(Call<List<PersonBean>> call, Response<List<PersonBean>> response) {
                Log.d(TAG,"callAllPerson:onResponse code:"+response.raw().code() );
                List<PersonBean>  personData = response.body();
                for(PersonBean s:personData){
                    Log.d(TAG,"callAllPerson:onResponse "+s.getName() );
                }
            }

            @Override
            public void onFailure(Call<List<PersonBean>> call, Throwable t) {
                Log.d(TAG,"callAllPerson:onFailure");
                t.printStackTrace();
            }
        });
    }
    public void insertProject(ProjectBean projectBean){
        netService.ApiService apiService = retrofit.create(netService.ApiService.class);
        Gson gson = new Gson();
        Log.d(TAG,gson.toJson(projectBean));
        Call<ProjectBean> callInsertProject = apiService.insertOneProject(projectBean);
        callInsertProject.enqueue(new Callback<ProjectBean>() {
            @Override
            public void onResponse(Call<ProjectBean> call, Response<ProjectBean> response) {
                Log.d(TAG,"insertProject:onResponse code:"+response.raw().code() );
                ProjectBean res = response.body();
                if(response.isSuccessful()) {
                    //提醒更新
                    if (res != null) {
                        Log.d(TAG, "insertProject:onResponse " + res.toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<ProjectBean> call, Throwable t) {
                Log.d(TAG,"insertProject:onFailure 是因为服务器不会返回相应上传的信息");
                t.printStackTrace();
            }
        });
    }

    public void insertPerson(PersonBean personBean){
        netService.ApiService apiService = retrofit.create(netService.ApiService.class);
//        Call<String> callAllPerson =
//                personService.insertPerson(
//                        personBean.getName(),
//                        personBean.getAge(),
//                        personBean.getPhone(),
//                        personBean.getLevel(),
//                        personBean.getCity(),
//                        personBean.getWorkType()
//                );
        Gson gson = new Gson();
        Log.d(TAG,gson.toJson(personBean));
//        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),gson.toJson(personBean));
//        Call<PersonBean> callInsertPerson = personService.insertPerson(body);

//        final Call<PersonBean> callInsertPerson = personService.insertOnePerson(personBean);
//        Observable observable = Observable.create(new Observable.OnSubscribe<PersonBean>() {
//            @Override
//            public void call(Subscriber<? super PersonBean> subscriber) {
//                Response<PersonBean> beanResponse = null;
//                try {
//                    beanResponse = callInsertPerson.execute();
//                    subscriber.onNext(beanResponse.body());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                subscriber.onCompleted();
//            }
//        });


        Call<PersonBean> callInsertPerson = apiService.insertOnePerson(personBean);
        callInsertPerson.enqueue(new Callback<PersonBean>() {
            @Override
            public void onResponse(Call<PersonBean> call, Response<PersonBean> response) {
                Log.d(TAG,"callInsertPerson:onResponse code:"+response.raw().code() );
                PersonBean  res = response.body();
                if(response.isSuccessful()) {
                    //提醒更新
                    if (res != null) {
                        Log.d(TAG, "callInsertPerson:onResponse " + res.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonBean> call, Throwable t) {
                Log.d(TAG,"callAllPerson:onFailure 是因为服务器不会返回相应上传的信息");
                t.printStackTrace();
            }
        });

    }
}

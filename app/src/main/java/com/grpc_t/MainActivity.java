package com.grpc_t;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fields.nano.CommonFields;
import io.grpc.ManagedChannelBuilder;
import partners.AdServiceGrpc;
import partners.nano.Ad;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
    private HashMap<String, Object> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        try {

        AdServiceGrpc.AdServiceBlockingClient stub = AdServiceGrpc.newBlockingStub(
                ManagedChannelBuilder.forAddress("ip地址", Integer.parseInt("端口地址"))
                        .usePlaintext(true)
                        .build());
        CommonFields.CommonRequest commonRequest = new CommonFields.CommonRequest();

        Ad.AdMediaRequest adMediaRequest = new Ad.AdMediaRequest();
//        adMediaRequest.positionSign ="1";
//        adMediaRequest.appType =;
//        adMediaRequest.partnerID =;
        commonRequest.timestamp = "1";

        final Ad.AdMediaResponse adMediaResponse = stub.getAdMediaList(adMediaRequest);

        Observable.just(adMediaResponse.reply)
                .map(new Func1<CommonFields.CommonResponse, Integer>() {
                    @Override
                    public Integer call(CommonFields.CommonResponse commonResponse) {

                        return commonResponse.status;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        switch (integer) {
                            case 0:
                                
                                break;
                            case 2:

                                break;
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {

                        return integer == 1;
                    }
                })
                .flatMap(new Func1<Integer, Observable<Ad.AdMedia>>() {
                    @Override
                    public Observable<Ad.AdMedia> call(Integer integer) {

                        return Observable.from(adMediaResponse.mediaList);
                    }
                })
                .map(new Func1<Ad.AdMedia, ArrayList<HashMap<String, Object>>>() {
                    @Override
                    public ArrayList<HashMap<String, Object>> call(Ad.AdMedia adMedia) {
                        hashMap = new HashMap<>();
                        hashMap.put("url_ad", adMedia.uRL);
                        arrayList.add(hashMap);

                        return arrayList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<HashMap<String, Object>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("error", e.toString());
                    }

                    @Override
                    public void onNext(final ArrayList<HashMap<String, Object>> arrayList) {
                        List<String> mAdImages_list = new ArrayList<>();

                        mAdImages_list.add(String.valueOf(arrayList.get(0).get("url_ad")));

                    }
                });
//        } catch (Exception e) {
////            mLinearLayout_nointernet.setVisibility(View.VISIBLE);
//        }


    }
}

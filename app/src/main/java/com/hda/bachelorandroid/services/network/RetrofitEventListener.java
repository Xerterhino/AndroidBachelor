package com.hda.bachelorandroid.services.network;

import com.hda.bachelorandroid.Activity;

import retrofit2.Call;

public interface RetrofitEventListener {
     void onSuccess(Call call, Object response);
     void onError(Call call, Throwable t);
}

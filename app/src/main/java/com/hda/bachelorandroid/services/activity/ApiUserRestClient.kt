package com.hda.bachelorandroid.services.activity

import android.util.Log
import android.view.View
import com.hda.bachelorandroid.services.activity.model.Activity
import com.hda.bachelorandroid.services.activity.model.CreateActivityBody
import com.hda.bachelorandroid.services.activity.model.UpdateActivityBody
import com.hda.bachelorandroid.services.model.Post
import com.hda.bachelorandroid.services.network.NetworkClient
import com.hda.bachelorandroid.services.network.RetrofitEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ApiUserRestClient {

    companion object {
        val instance = ApiUserRestClient()
    }

    private var mApiUser: APIActivity? = null


    fun getUserList(retrofitEventListener: RetrofitEventListener) {
        val retrofit = NetworkClient.retrofitClient
        mApiUser = retrofit.create<APIActivity>(APIActivity::class.java)

        val data = HashMap<String, String>()

        val apiUserCall = mApiUser!!.getUserList(data)

        apiUserCall.enqueue(object : Callback<List<Activity>> {
           override fun onFailure(call: Call<List<Activity>>?, t: Throwable?) {
                retrofitEventListener.onError(call, t)
            }
            override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
                if (response?.body() != null) {
                    retrofitEventListener.onSuccess(call, response?.body())
                }
            }

        })

    }
    fun savePostActivity(retrofitEventListener: RetrofitEventListener, duration: String, name: String) {
        val retrofit = NetworkClient.retrofitClient
        mApiUser = retrofit.create<APIActivity>(APIActivity::class.java)

        val data = HashMap<String, String>()

        val createBody = CreateActivityBody(duration, name)
        val apiUserCall = mApiUser!!.postJsonActivity(createBody)

        apiUserCall.enqueue(object : Callback<Activity> {
           override fun onFailure(call: Call<Activity>?, t: Throwable?) {
                retrofitEventListener.onError(call, t)
            }
            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                if (response?.body() != null) {
                    retrofitEventListener.onSuccess(call, response?.body())
                }
            }
        })
    }
    fun deleteActivity(retrofitEventListener: RetrofitEventListener, activityId: String) {
        val retrofit = NetworkClient.retrofitClient
        mApiUser = retrofit.create<APIActivity>(APIActivity::class.java)


        val apiUserCall = mApiUser!!.deleteActivity(activityId)

        apiUserCall.enqueue(object : Callback<Activity> {
           override fun onFailure(call: Call<Activity>?, t: Throwable?) {
                retrofitEventListener.onError(call, t)
            }
            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                if (response?.body() != null) {
                    retrofitEventListener.onSuccess(call, response?.body())
                }
            }
        })
    }
    fun updateActivity(retrofitEventListener: RetrofitEventListener, activityId: String, name: String, duration: String, finished: Boolean) {
        val retrofit = NetworkClient.retrofitClient
        mApiUser = retrofit.create<APIActivity>(APIActivity::class.java)


        val updateBody = UpdateActivityBody(name, duration, finished)
        val apiUserCall = mApiUser!!.updateActivity(activityId, updateBody)

        apiUserCall.enqueue(object : Callback<Activity> {
           override fun onFailure(call: Call<Activity>?, t: Throwable?) {
                retrofitEventListener.onError(call, t)
            }
            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                if (response?.body() != null) {
                    retrofitEventListener.onSuccess(call, response?.body())
                }
            }
        })
    }

}

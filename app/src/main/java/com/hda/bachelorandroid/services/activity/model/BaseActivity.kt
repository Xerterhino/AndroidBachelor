package com.hda.bachelorandroid.services.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseActivity {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("duration")
    @Expose
    var duration: Int? = null
    @SerializedName("finished")
    @Expose
    var finished: Boolean? = false
    @SerializedName("activityId")
    @Expose
    var activityId: String? = null
    @SerializedName("data")
    @Expose
    var data: List<Activity>? = null

}
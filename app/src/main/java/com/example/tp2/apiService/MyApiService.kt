package com.example.tp2.apiService

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MyApiService {
    @GET("realestate")
    fun getProperties(): Deferred<List<MarsProperty>>
}

object MyApi {
    val retrofitService : MyApiService by lazy { retrofit.create(MyApiService::class.java) }
}

data class MarsProperty(
    @ColumnInfo(name = "id")
    private var _id: String = "",

    @Json(name = "img_src")
    @ColumnInfo(name = "imgSrcUrl")
    private var _imgSrcUrl: String? = "",

    @ColumnInfo(name = "type")
    private var _type: String? = "",

    @ColumnInfo(name = "price")
    private var _price: Double? = 0.0): Parcelable,

    BaseObservable() {

    var id: String
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    var imgSrcUrl: String?
        @Bindable get() = _imgSrcUrl
        set(value) {
            _imgSrcUrl = value
            notifyPropertyChanged(BR.imgSrcUrl)
        }

    var type: String?
        @Bindable get() = _type
        set(value) {
            _type = value
            notifyPropertyChanged(BR.type)
        }

    var price: Double?
        @Bindable get() = _price
        set(value) {
            _price = value
            notifyPropertyChanged(BR.price)
        }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgSrcUrl)
        parcel.writeString(type)
        parcel.writeDouble(price!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarsProperty> {
        override fun createFromParcel(parcel: Parcel): MarsProperty {
            return MarsProperty(parcel)
        }

        override fun newArray(size: Int): Array<MarsProperty?> {
            return arrayOfNulls(size)
        }
    }
}

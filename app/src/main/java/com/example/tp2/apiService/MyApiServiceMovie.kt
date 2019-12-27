package com.example.tp2.apiService

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MyApiServiceMovie {
    @GET("movie/now_playing")
    fun getProperties(@Query("api_key") api_key: String, @Query("language") language: String, @Query("page") page: Int): Deferred<ListMovie>

    @GET("search/movie")
    fun getPropertiesSearched(@Query("api_key") api_key: String, @Query("query") query: String, @Query("language") language: String, @Query("page") page: Int): Deferred<ListMovie>
}

object MyApiMovie {
    val retrofitService : MyApiServiceMovie by lazy { retrofit.create(MyApiServiceMovie::class.java) }
 }

data class Movie(
    private var id: String? = "",
    private var popularity: Double? = 0.0,
    private var title: String? = "",
    private var poster_path: String? = "",
    private var vote_count: Int? = 0,
    private var video: Boolean? = false,
    private var adult: Boolean? = false,
    private var backdrop_path: String? = "",
    private var original_language: String? = "",
    private var original_title: String? = "",
    private var vote_average: Double? = 0.0,
    private var overview: String? = "",
    private var release_date: String? = "",
    private var genre_ids: List<Int>?
): Parcelable,

    BaseObservable() {

    var _id: String?
        @Bindable get() = id
        set(value) {
            id = value
            notifyPropertyChanged(BR._id)
        }

    var _popularity: Double?
        @Bindable get() = popularity
        set(value) {
            popularity = value
            notifyPropertyChanged(BR._popularity)
        }

    var _title: String?
        @Bindable get() = title
        set(value) {
            title = value
            notifyPropertyChanged(BR._title)
        }

    var _poster_path: String?
        @Bindable get() = poster_path
        set(value) {
            poster_path = value
            notifyPropertyChanged(BR._poster_path)
        }

    var _vote_count: Int?
        @Bindable get() = vote_count
        set(value) {
            vote_count = value
            notifyPropertyChanged(BR._vote_count)
        }

    var _video: Boolean?
        @Bindable get() = video
        set(value) {
            video = value
            notifyPropertyChanged(BR._video)
        }

    var _adult: Boolean?
        @Bindable get() = adult
        set(value) {
            adult = value
            notifyPropertyChanged(BR._adult)
        }

    var _backdrop_path: String?
        @Bindable get() = backdrop_path
        set(value) {
            backdrop_path = value
            notifyPropertyChanged(BR._backdrop_path)
        }

    var _original_language: String?
        @Bindable get() = original_language
        set(value) {
            original_language = value
            notifyPropertyChanged(BR._original_language)
        }

    var _original_title: String?
        @Bindable get() = original_title
        set(value) {
            original_title = value
            notifyPropertyChanged(BR._original_title)
        }

    var _vote_average: Double?
        @Bindable get() = vote_average
        set(value) {
            vote_average = value
            notifyPropertyChanged(BR._vote_average)
        }

    var _overview: String?
        @Bindable get() = overview
        set(value) {
            overview = value
            notifyPropertyChanged(BR._overview)
        }

    var _release_date: String?
        @Bindable get() = release_date
        set(value) {
            release_date = value
            notifyPropertyChanged(BR._release_date)
        }

    var _genre_ids: List<Int>?
        @Bindable get() = genre_ids
        set(value) {
            genre_ids = value
            notifyPropertyChanged(BR._genre_ids)
        }

    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        listOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeDouble(_popularity!!)
        parcel.writeString(_poster_path)
        parcel.writeString(_title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

data class ListMovie(
    private var page: Int,
    private var total_results: Int,
    private var total_pages: Int,
    private var results: List<Movie>): Parcelable,

    BaseObservable() {

    var _page: Int
        @Bindable get() = page
        set(value) {
            page = value
            notifyPropertyChanged(BR._page)
        }

    var _total_results: Int
        @Bindable get() = total_results
        set(value) {
            total_results = value
            notifyPropertyChanged(BR._total_results)
        }

    var _total_pages: Int
        @Bindable get() = total_pages
        set(value) {
            total_pages = value
            notifyPropertyChanged(BR._total_pages)
        }

    var _results: List<Movie>
        @Bindable get() = results
        set(value) {
            results = value
            notifyPropertyChanged(BR._results)
        }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        listOf<Movie>().apply {
            parcel.readList(this, Movie::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_page)
        parcel.writeInt(_total_results)
        parcel.writeInt(_total_pages)
        parcel.writeList(_results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListMovie> {
        override fun createFromParcel(parcel: Parcel): ListMovie {
            return ListMovie(parcel)
        }

        override fun newArray(size: Int): Array<ListMovie?> {
            return arrayOfNulls(size)
        }
    }
}

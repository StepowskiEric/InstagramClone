package com.example.instagramclone;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("2k1lEBlvTCLep16VXbWeRvj1TiunBSrwPfdz2HBO")
                // if defined
                .clientKey("DBgqvYfUuORSMQejB4psej3GOBSpDZ4XGrjN0SY0")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

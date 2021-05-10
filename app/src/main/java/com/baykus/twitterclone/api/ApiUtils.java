package com.baykus.twitterclone.api;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ApiUtils {

    public static final String BASE_URL = "http://www.bayramkus.com/";

    public ApiUtils() throws IOException {
    }

    public static KisilerDao getKisilerDao(){
        return RetrofitClient.getClient(BASE_URL).create(KisilerDao.class);

    }

}

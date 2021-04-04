package com.baykus.twitterclone.api;

public class ApiUtils {
    public static final String BASE_URL = "http://busrakus.cf/";

    public static KisilerDao getKisilerDao(){
        return RetrofitClient.getClient(BASE_URL).create(KisilerDao.class);

    }

}

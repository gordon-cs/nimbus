package com.vmware.nimbus.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequest {
    private static SingletonRequest instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private SingletonRequest(Context context) {
        this.ctx = context;
        this.requestQueue = getRequestQueue();

    }

    public static synchronized SingletonRequest getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

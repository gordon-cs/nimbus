package com.vmware.nimbus.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class that maintains one instance of the Volley RequestQueue at all times. The queue
 * can be added to with addToRequestQueue(Request)
 */
public class SingletonRequest {
    private static SingletonRequest instance;
    private static Context ctx;
    private RequestQueue requestQueue;

    /**
     * Singleton constructor for the class. Only one instance can exist at a time.
     *
     * @param context - the context of the app
     */
    private SingletonRequest(Context context) {
        ctx = context;
        this.requestQueue = getRequestQueue();

    }

    /**
     * Gets the instance of the SingletonRequest object.
     *
     * @param context - the context of the app
     * @return - the instance of SingletonRequest
     */
    public static synchronized SingletonRequest getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonRequest(context);
        }
        return instance;
    }

    /**
     * Gets the Volley RequestQueue associated with the object.
     *
     * @return - the Volley RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds a Request to the Volley RequestQueue
     *
     * @param req<T> - the Request to add of type T
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

package com.vmware.nimbus.api;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class APIService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_DEPLOYMENTS = "com.vmware.nimbus.api.action.GET_DEPLOYMENTS";
    private static final String ACTION_GET_BLUEPRINTS = "com.vmware.nimbus.api.action.GET_BLUEPRINTS";

    // TODO: Rename parameters
    private static final String URL = "com.vmware.nimbus.api.extra.URL";
    private static final String KEY = "com.vmware.nimbus.api.extra.KEY";

    public APIService() {
        super("APIService");
    }

    /**
     * Starts this service to perform action Get Deployments with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetDeployments(Context context, String url, String key) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_DEPLOYMENTS);
        intent.putExtra(URL, url);
        intent.putExtra(KEY, key);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Get Blueprints with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetBlueprints(Context context, String url, String key) {
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(ACTION_GET_BLUEPRINTS);
        intent.putExtra(URL, url);
        intent.putExtra(KEY, key);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_DEPLOYMENTS.equals(action)) {
                final String url = intent.getStringExtra(URL);
                final String key = intent.getStringExtra(KEY);
                handleActionGetDeployments(url, key);
            } else if (ACTION_GET_BLUEPRINTS.equals(action)) {
                final String url = intent.getStringExtra(URL);
                final String key = intent.getStringExtra(KEY);
                handleActionGetBlueprints(url, key);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetDeployments(String url, String key) {
        // TODO: Handle action Foo

        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetBlueprints(String url, String key) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

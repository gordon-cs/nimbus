package com.vmware.nimbus;

import android.content.Context;

import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.BlueprintCallback;
import com.vmware.nimbus.api.LogInCallback;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.LoginModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApiCallTest {
    //Set your valid api key here for testing
    public static final String GOOD_API_KEY = "replace me";
    public static final String BAD_API_KEY = "invalidkey";

    private APIService service;
    public static final String url = getInstrumentation().getTargetContext().getResources().getString(R.string.csp_URL);

    @Before
    public void createAPIService() {
        service = new APIService();
    }

    @Test
    public void testLoginFail() {
        Context appContext = getInstrumentation().getTargetContext();

        service.LogIn(appContext, url, BAD_API_KEY, new LogInCallback() {
            @Override
            public void onSuccess(boolean result) {
                assertEquals(false, result);
            }

            @Override
            public void onFailure(boolean result) {

            }
        });
    }

    @Test
    public void testLoginSuccess() {
        Context appContext = getInstrumentation().getTargetContext();

        service.LogIn(appContext, url, GOOD_API_KEY, new LogInCallback() {
            @Override
            public void onSuccess(boolean result) {
                assertEquals(true, result);
            }

            @Override
            public void onFailure(boolean result) {

            }
        });
    }

    @Test
    public void testLoadBlueprints() {
        Context appContext = getInstrumentation().getTargetContext();

        service.loadBlueprints(new BlueprintCallback() {
            @Override
            public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                assertNotNull(result);
            }
        }, appContext);
    }

    @Test
    public void testLogout() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();

        LoginModel.getInstance(appContext).setApi_token(GOOD_API_KEY);
        LoginModel.getInstance(appContext).setAuthenticated(true);
        service.LogOut(appContext);
        assertEquals("", LoginModel.getInstance(appContext).getApi_token());
        assertEquals(false, LoginModel.getInstance(appContext).isAuthenticated());
    }
}

package com.vmware.nimbus;

import android.content.Context;

import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.data.model.LoginModel;
import com.vmware.nimbus.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApiCallTest {
    public static final String TEST_API_KEY = "sample";
    private APIService service;

    @Before
    public void createAPIService() {
        service = new APIService();
    }

    @Test
    public void testLogout() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        LoginModel.getInstance(appContext).setApi_token(TEST_API_KEY);
        LoginModel.getInstance(appContext).setAuthenticated(true);
        service.LogOut(appContext);
        assertEquals("", LoginModel.getInstance(appContext).getApi_token());
        assertEquals(false, LoginModel.getInstance(appContext).isAuthenticated());

    }
}

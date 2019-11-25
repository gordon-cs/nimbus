package com.vmware.nimbus.ui.main.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.DeploymentsModel;
import com.vmware.nimbus.data.model.LoginModel;

import java.util.ArrayList;
import java.util.List;

public class DeploymentsViewModel extends AndroidViewModel {

    public DeploymentsViewModel(Application application) {
        super(application);
    }
    //private int count;

    //public DeploymentsViewModel(int count) {
    //    this.count = count;
    //}

    private List<DeploymentsModel> deploymentsTests;
    private List<DeploymentItemModel> deploymentsData;

    //protected int getCount() {
    //    return this.count;
    //}

    // Use LiveData object here
    LoginModel loginData = LoginModel.getInstance(getApplication().getApplicationContext());
    public List<DeploymentItemModel> initializeData() {
        String api_token = loginData.getApi_token();
        return deploymentsData;
    }

    public List<DeploymentsModel> initializeDeploymentsData() {
        String hello = "Hello ";
        String deployments = "deployments ";
        deploymentsTests = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
            deploymentsTests.add(new DeploymentsModel(hello, deployments, Integer.toString(i + 1)));

        return deploymentsTests;
    }
}

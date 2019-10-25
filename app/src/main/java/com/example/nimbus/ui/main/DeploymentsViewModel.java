package com.example.nimbus.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.nimbus.data.model.DeploymentsModel;

import java.util.ArrayList;
import java.util.List;

public class DeploymentsViewModel extends ViewModel {

    //private int count;

    //public DeploymentsViewModel(int count) {
    //    this.count = count;
    //}

    private List<DeploymentsModel> deploymentsTests;

    //protected int getCount() {
    //    return this.count;
    //}

    public List<DeploymentsModel> initializeDeploymentsData() {
        String hello = "Hello ";
        String deployments = "deployments ";
        deploymentsTests = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
            deploymentsTests.add(new DeploymentsModel(hello, deployments, Integer.toString(i + 1)));

        return deploymentsTests;
    }
}

package com.vmware.nimbus.ui.main.viewmodels;


import androidx.lifecycle.ViewModel;

import com.vmware.nimbus.data.model.BlueprintsModel;

import java.util.ArrayList;
import java.util.List;

public class BlueprintsViewModel extends ViewModel {

    //private int count;

    //public BlueprintsViewModel(int count) {
    //    this.count = count;
    //}
    private List<BlueprintsModel> blueprintsTests;

//    protected int getCount() {
//        return count;
//    }

    public List<BlueprintsModel> initializeBlueprintsData() {
        String hello = "Hello ";
        String blueprints = "blueprints ";
        blueprintsTests = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
            blueprintsTests.add(new BlueprintsModel(hello, blueprints, Integer.toString(i + 1)));

        return blueprintsTests;
    }
}


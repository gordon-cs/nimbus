package com.vmware.nimbus.ui.main.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.DeploymentsModel;
import com.vmware.nimbus.ui.main.adapters.DeploymentsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.DeploymentsViewModel;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeploymentsViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private DeploymentsViewModel mViewModel;

    private RecyclerView recyclerView;

    private DeploymentsAdapter rvAdapter;
    private List<DeploymentsModel> deploymentsTest;

    public static DeploymentsViewFragment newInstance(int index) {
        DeploymentsViewFragment fragment = new DeploymentsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DeploymentsViewModel.class);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_deployments, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        recyclerView = root.findViewById(R.id.fragment_deployments_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

//        deploymentsTest = new ArrayList<>();
//        deploymentsTest = mViewModel.initializeDeploymentsData();
        //mViewModel.loadDeploymentStore();
        List<DeploymentItemModel.DeploymentItem> deploymentItems;
        mViewModel.loadDeployments();
        try {
            deploymentItems = mViewModel.getDeploymentItems();
        }
        catch (NullPointerException e) {
            deploymentItems = new ArrayList();
        }

        rvAdapter = new DeploymentsAdapter(deploymentItems);

        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DeploymentsViewModel.class);
        // TODO: Use the ViewModel
    }

}

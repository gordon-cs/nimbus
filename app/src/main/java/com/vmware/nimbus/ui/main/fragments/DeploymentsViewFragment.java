package com.vmware.nimbus.ui.main.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.DeploymentCallback;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.adapters.DeploymentsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.DeploymentsViewModel;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.List;

/**
 * A fragment containing a list of deployment items.
 */
public class DeploymentsViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private DeploymentsViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;

    private DeploymentsAdapter rvAdapter;
    private List<DeploymentItemModel.DeploymentItem> deploymentList;

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
        return inflater.inflate(R.layout.fragment_deployments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.fragment_deployments_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer_blueprints);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mViewModel.loadDeployments(new DeploymentCallback() {
                            @Override
                            public void onSuccess(List<DeploymentItemModel.DeploymentItem> result) {
                                rvAdapter.clear();
                                deploymentList = result;
                                rvAdapter.addAll(deploymentList);
                                rvAdapter.notifyDataSetChanged();
                            }
                        });
                        swipeContainer.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_bright,
                R.color.colorAccent);

        mViewModel.loadDeployments(new DeploymentCallback() {
            @Override
            public void onSuccess(List<DeploymentItemModel.DeploymentItem> result) {
                deploymentList = result;
                rvAdapter = new DeploymentsAdapter(getContext(), deploymentList);
                mRecyclerView.setAdapter(rvAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }
}

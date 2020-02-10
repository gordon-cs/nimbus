package com.vmware.nimbus.ui.main.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.BlueprintCallback;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.ui.main.adapters.BlueprintsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.BlueprintsViewModel;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.List;

/**
 * A [Fragment] containing a list of blueprint items.
 */
public class BlueprintsViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private BlueprintsViewModel mViewModel;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    private BlueprintsAdapter rvAdapter;
    private List<BlueprintItemModel.BlueprintItem> blueprintList;

    /**
     * Creates a new instance of the view fragment.
     * @param index - index of the instance
     * @return - a fragment
     */
    public static BlueprintsViewFragment newInstance(int index) {
        BlueprintsViewFragment fragment = new BlueprintsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Called when ViewFragment is created
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BlueprintsViewModel.class);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    /**
     * Called when the View is created
     * @param inflater - the LayoutInflater
     * @param container - the ViewGroup
     * @param savedInstanceState - the savedInstanceState
     * @return - returns the inflated View
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blueprints, container, false);
    }

    /**
     * Called after the View is created.
     * @param view - the View
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_blueprints_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer_blueprints);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mViewModel.loadBlueprints(new BlueprintCallback() {
                            @Override
                            public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                                rvAdapter.clear();
                                blueprintList = result;
                                rvAdapter.addAll(blueprintList);
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

        mViewModel.loadBlueprints(new BlueprintCallback() {
            @Override
            public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                blueprintList = result;
                rvAdapter = new BlueprintsAdapter(getContext(), blueprintList);
                recyclerView.setAdapter(rvAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
    }
}


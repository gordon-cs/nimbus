package com.vmware.nimbus.ui.main.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import com.vmware.nimbus.ui.main.EndlessRecyclerOnScrollListener;
import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.DeploymentCallback;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.adapters.DeploymentsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A [Fragment] containing a list of deployment items.
 */
public class DeploymentsViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;

    private DeploymentsAdapter rvAdapter;
    private List<DeploymentItemModel.DeploymentItem> deploymentList;

    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView = null;

    private ProgressBar loadingPB;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    /**
     * Creates a new instance of the view fragment.
     *
     * @param index - index of the instance
     * @return - a fragment
     */
    public static DeploymentsViewFragment newInstance(int index) {
        DeploymentsViewFragment fragment = new DeploymentsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Called when ViewFragment is created
     *
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        setHasOptionsMenu(true);
    }

    /**
     * Called when the View is created
     *
     * @param inflater           - the LayoutInflater
     * @param container          - the ViewGroup
     * @param savedInstanceState - the savedInstanceState
     * @return - returns the inflated View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deployments, container, false);
    }

    /**
     * Called after the View is created.
     *
     * @param view               - the View
     * @param savedInstanceState - the savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.fragment_deployments_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);

        loadingPB = view.findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.GONE);

        rvAdapter = new DeploymentsAdapter(getContext(), new ArrayList<>(), new ArrayList<>());
        mRecyclerView.setAdapter(rvAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer_blueprints);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        APIService.loadDeployments(new DeploymentCallback() {
                            @Override
                            public void onSuccess(List<DeploymentItemModel.DeploymentItem> result) {
                                rvAdapter.clear();
                                deploymentList = result;
                                rvAdapter.addAll(deploymentList);
                                rvAdapter.notifyDataSetChanged();
                                endlessRecyclerOnScrollListener.resetState();
                            }

                            @Override
                            public void onError(Throwable error) {

                            }
                        }, getContext(), 0);
                        swipeContainer.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_bright,
                R.color.colorAccent);

        //Initial call for populating the recycler view for blueprints
        loadNextDataFromApi(0);

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                loadingPB.setVisibility(View.VISIBLE);
                loadNextDataFromApi(current_page);
            }
        };
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

    }

    public void loadNextDataFromApi(int offset) {

        APIService.loadDeployments(new DeploymentCallback() {
            @Override
            public void onSuccess(List<DeploymentItemModel.DeploymentItem> result) {
                rvAdapter.addAll(result);
                rvAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                loadingPB.setVisibility(View.GONE);
            }
        }, getContext(), offset);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    rvAdapter.getFilter().filter(newText);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}

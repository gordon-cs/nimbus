package com.vmware.nimbus.ui.main.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.APIService;
import com.vmware.nimbus.api.BlueprintCallback;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.ui.main.adapters.BlueprintsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.List;
import java.util.concurrent.TimeoutException;

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
 * A [Fragment] containing a list of blueprint items.
 */
public class BlueprintsViewFragment extends Fragment implements BlueprintsAdapter.BlueprintsAdapterCallback {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    private BlueprintsAdapter rvAdapter;
    private List<BlueprintItemModel.BlueprintItem> blueprintList;

    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView = null;

    private LinearLayout showErrorLayout;
    private Button btnRetry;

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 0;

    private OnLoadMoreListener onLoadMoreListener;

    TextView txtError;

    /**
     * Creates a new instance of the view fragment.
     *
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
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blueprints, container, false);
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
        recyclerView = view.findViewById(R.id.fragment_blueprints_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        rvAdapter = new BlueprintsAdapter(getContext(), this);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        showErrorLayout = view.findViewById(R.id.error_layout);
        txtError = view.findViewById(R.id.error_txt_cause);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer_blueprints);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextDataFromApi(0);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        APIService.loadBlueprints(new BlueprintCallback() {
                            @Override
                            public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                                hideErrorView();
                                rvAdapter.clear();
                                blueprintList = result;
                                rvAdapter.addAll(blueprintList);
                                resetState();
                            }

                            @Override
                            public void onError(Throwable error) {
                                rvAdapter.clear();
                                showErrorMessage(error);
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

        onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadNextDataFromApi(current_page);
            }
        };


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = llm.getItemCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                // Handling the scenario where item count is referring to the searched
                // items which will always be less or equal than the previous total
                if (!loading && (totalItemCount < previousTotal)) {
                    totalItemCount = previousTotal;
                    return;
                }

                if (!loading && (totalItemCount)
                        <= (firstVisibleItem + visibleItemCount)) {
                    current_page++;
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });

    }

    public void loadNextDataFromApi(int offset) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                APIService.loadBlueprints(new BlueprintCallback() {
                    @Override
                    public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                        hideErrorView();
                        if (offset != 0) {
                            rvAdapter.removeNull();
                        }
                        rvAdapter.addAll(result);
                        rvAdapter.addNull();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        if (current_page == 0) {
                            showErrorMessage(error);
                        } else {
                            rvAdapter.showRetry(true, fetchErrorMessage(error));
                        }
                    }
                }, getContext(), offset);
            }
        }, 1000);
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

    @Override
    public void retryPageLoad() {
        loadNextDataFromApi(current_page);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void resetState() {
        this.current_page = 0;
        this.previousTotal = 0;
        this.loading = true;
    }

    private void showErrorMessage (Throwable th) {
        if (showErrorLayout.getVisibility() == View.GONE) {
            showErrorLayout.setVisibility(View.VISIBLE);
            txtError.setText(fetchErrorMessage(th));
        }
    }

    private void hideErrorView() {
        if (showErrorLayout.getVisibility() == View.VISIBLE) {
            showErrorLayout.setVisibility(View.GONE);
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);
        if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }
        return errorMsg;
    }

}


package com.vmware.nimbus.ui.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.DataCallback;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.data.model.BlueprintsModel;
import com.vmware.nimbus.ui.main.adapters.BlueprintsAdapter;
import com.vmware.nimbus.ui.main.viewmodels.BlueprintsViewModel;
import com.vmware.nimbus.ui.main.viewmodels.PageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BlueprintsViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private BlueprintsViewModel mViewModel;

    private RecyclerView recyclerView;

    private BlueprintsAdapter rvAdapter;
    private List<BlueprintItemModel.BlueprintItem> blueprintList;


    public static BlueprintsViewFragment newInstance(int index) {
        BlueprintsViewFragment fragment = new BlueprintsViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

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

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_blueprints, container, false);

        recyclerView = root.findViewById(R.id.fragment_blueprints_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        //blueprintList = mViewModel.initializeBlueprintsData();
        rvAdapter = new BlueprintsAdapter(blueprintList);

        mViewModel.loadBlueprints(new DataCallback() {
            @Override
            public void onSuccess(List<BlueprintItemModel.BlueprintItem> result) {
                blueprintList = result;
                Toast.makeText(getActivity().getApplicationContext(), "blueprint-list:" + blueprintList.get(0).toString(), Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return recyclerView;
    }

}


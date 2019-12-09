package com.vmware.nimbus.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.DeploymentItemModel;

import java.util.List;

public class DeploymentsAdapter extends RecyclerView.Adapter<DeploymentsAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView deployments_card_view;
        TextView bpid_deployments_text;
        TextView name_deployments_text;


        CardViewHolder(View itemView) {
            super(itemView);
            deployments_card_view = itemView.findViewById(R.id.deployments_card_view);
            bpid_deployments_text = deployments_card_view.findViewById(R.id.bpid_deployments_text);
            name_deployments_text = deployments_card_view.findViewById(R.id.name_deployments_text);
        }
    }

    List<DeploymentItemModel.DeploymentItem> deploymentsData;

    public DeploymentsAdapter(List<DeploymentItemModel.DeploymentItem> deploymentsData) {
        this.deploymentsData = deploymentsData;
    }

    @Override
    public int getItemCount() { return this.deploymentsData.size(); }

    @Override
    public DeploymentsAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deployments_card_view, viewGroup, false);
        DeploymentsAdapter.CardViewHolder cvh = new DeploymentsAdapter.CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.bpid_deployments_text.setText("Blueprint: " + deploymentsData.get(i).id);
        cardViewHolder.name_deployments_text.setText(deploymentsData.get(i).name);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

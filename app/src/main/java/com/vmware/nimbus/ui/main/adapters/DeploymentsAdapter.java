package com.vmware.nimbus.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.data.model.DeploymentsModel;

import java.util.List;

public class DeploymentsAdapter extends RecyclerView.Adapter<DeploymentsAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView hello_deployments_text;
        TextView world_deployments_text;
        TextView index_deployments_text;

        TextView deployment_id;
        TextView created_by;

        CardViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.deployments_card_view);
            hello_deployments_text = cardView.findViewById(R.id.hello_deployments_text);
            world_deployments_text = cardView.findViewById(R.id.world_deployments_text);
            index_deployments_text = cardView.findViewById(R.id.index_deployments_text);
            deployment_id = cardView.findViewById(R.id.deployment_id);
            created_by = cardView.findViewById(R.id.created_by);
        }

    }

    List<DeploymentsModel> deploymentsData;

    List<DeploymentItemModel.DeploymentItem> deploymentItems;

//    public DeploymentsAdapter(List<DeploymentsModel> deploymentsData) {
//        this.deploymentsData = deploymentsData;
//    }

    public DeploymentsAdapter(List<DeploymentItemModel.DeploymentItem> deploymentItems) {
        this.deploymentItems = deploymentItems;
    }

    @Override
    public int getItemCount() { return deploymentItems.size(); }

    @Override
    public DeploymentsAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deployments_card_view, viewGroup, false);
        DeploymentsAdapter.CardViewHolder cvh = new DeploymentsAdapter.CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
//        cardViewHolder.hello_deployments_text.setText(deploymentsData.get(i).helloData);
//        cardViewHolder.world_deployments_text.setText(deploymentsData.get(i).worldData);
//        cardViewHolder.index_deployments_text.setText(deploymentsData.get(i).dataIndex);
        cardViewHolder.deployment_id.setText(deploymentItems.get(i).id);
        cardViewHolder.created_by.setText(deploymentItems.get(i).createdBy);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

package com.vmware.nimbus.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.ItemClickListener;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.DeploymentActivity;

import java.util.List;

public class DeploymentsAdapter extends RecyclerView.Adapter<DeploymentsAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bpid_deployments_text;
        TextView name_deployments_text;
        private ItemClickListener itemClickListener;


        CardViewHolder(View itemView) {
            super(itemView);
            bpid_deployments_text = itemView.findViewById(R.id.bpid_deployments_text);
            name_deployments_text = itemView.findViewById(R.id.name_deployments_text);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
    }

    Context c;
    List<DeploymentItemModel.DeploymentItem> deploymentsData;

    public DeploymentsAdapter(Context ctx, List<DeploymentItemModel.DeploymentItem> deploymentsData) {
        this.deploymentsData = deploymentsData;
        this.c = ctx;
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

        cardViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(c, DeploymentActivity.class);
                Toast toast = Toast.makeText(c, "pos " + pos, Toast.LENGTH_SHORT);
                toast.show();

                c.startActivity(i);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

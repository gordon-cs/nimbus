package com.vmware.nimbus.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.ItemClickListener;
import com.vmware.nimbus.data.model.DeploymentItemModel;
import com.vmware.nimbus.ui.main.DeploymentActivity;

import java.io.Serializable;
import java.util.List;

/**
 * A [Serializable] [RecyclerView.Adapter] that connects the DeploymentItemModel to the RecyclerView
 * for the Deployments page of the app.
 */
public class DeploymentsAdapter extends RecyclerView.Adapter<DeploymentsAdapter.CardViewHolder> implements Serializable {
    /**
     * A [RecyclerView.ViewHolder] static inner class that connects individual items in the model
     * to individual cards in the RecyclerView for the Deployments page of the app.
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bpid_deployments_text;
        TextView name_deployments_text;
        private ItemClickListener itemClickListener;

        /**
         * A CardViewHolder for each card in the Deployments page.
         * @param itemView - the View for each item
         */
        CardViewHolder(View itemView) {
            super(itemView);
            bpid_deployments_text = itemView.findViewById(R.id.bpid_deployments_text);
            name_deployments_text = itemView.findViewById(R.id.name_deployments_text);

            itemView.setOnClickListener(this);
        }

        /**
         * Calls the itemClickListener when this item is clicked.
         * @param v - the View
         */
        @Override
        public void onClick(View v){
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        /**
         * Sets the itemClickListener for this item.
         * @param ic - the ItemClickListener
         */
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
    }

    Context c;
    List<DeploymentItemModel.DeploymentItem> deploymentsData;

    /**
     * Constructor for this adapter.
     * @param ctx - the context
     * @param deploymentsData - List of items from the model for the RecyclerView to consume
     */
    public DeploymentsAdapter(Context ctx, List<DeploymentItemModel.DeploymentItem> deploymentsData) {
        this.deploymentsData = deploymentsData;
        this.c = ctx;
    }

    /**
     * Clears the data associated with the RecyclerView
     */
    public void clear() {
        deploymentsData.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list to the data.
     * @param list - the List of DeploymentItems
     */
    public void addAll(List<DeploymentItemModel.DeploymentItem> list) {
        deploymentsData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Gets the size of the list associated with the RecyclerView.
     * @return - the size of the list
     */
    @Override
    public int getItemCount() { return this.deploymentsData.size(); }

    /**
     * Inflates the layout and creates the CardViewHolder. Called iteratively through the data set.
     * @param viewGroup - the ViewGroup
     * @param i - index of the CardViewHolder based on the position in the data set.
     * @return - a CardViewHolder for this card.
     */
    @Override
    public DeploymentsAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deployments_card_view, viewGroup, false);
        DeploymentsAdapter.CardViewHolder cvh = new DeploymentsAdapter.CardViewHolder(v);
        return cvh;
    }

    /**
     * Called when the CardViewHolder is bound to the View.
     * @param cardViewHolder - the CardViewHolder for this card.
     * @param i - the index of the card's position in the list.
     */
    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.bpid_deployments_text.setText("Blueprint: " + deploymentsData.get(i).id);
        cardViewHolder.name_deployments_text.setText(deploymentsData.get(i).name);

        cardViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(c, DeploymentActivity.class);
                i.putExtra("DeploymentItemModel.DeploymentItem", deploymentsData.get(pos));
                c.startActivity(i);
            }
        });
    }

    /**
     * Called when the card is attached to the RecyclerView.
     * @param recyclerView - the RecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

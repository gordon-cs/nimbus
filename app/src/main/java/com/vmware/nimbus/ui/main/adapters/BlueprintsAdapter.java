package com.vmware.nimbus.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.api.ItemClickListener;
import com.vmware.nimbus.data.model.BlueprintItemModel;
import com.vmware.nimbus.ui.main.BlueprintActivity;

import java.io.Serializable;
import java.util.List;

/**
 * A [Serializable] [RecyclerView.Adapter] that connects the BlueprintItemModel to the RecyclerView
 * for the Blueprints page of the app.
 */
public class BlueprintsAdapter extends RecyclerView.Adapter<BlueprintsAdapter.CardViewHolder> implements Serializable {
    /**
     * A [RecyclerView.ViewHolder] static inner class that connects individual items in the model
     * to individual cards in the RecyclerView for the Blueprints page of the app.
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_blueprints_text;
        TextView id_blueprints_text;
        TextView createdby_blueprints_text;
        private ItemClickListener itemClickListener;

        /**
         * A CardViewHolder for each card in the Blueprints page.
         * @param itemView - the View for each item
         */
        CardViewHolder(View itemView) {
            super(itemView);
            name_blueprints_text = itemView.findViewById(R.id.name_blueprints_text);
            id_blueprints_text = itemView.findViewById(R.id.id_blueprints_text);
            createdby_blueprints_text = itemView.findViewById(R.id.createdby_blueprints_text);

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
        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }
    }

    Context c;
    List<BlueprintItemModel.BlueprintItem> blueprintsData;

    /**
     * Constructor for this adapter.
     * @param ctx - the context
     * @param blueprintsData - List of items from the model for the RecyclerView to consume
     */
    public BlueprintsAdapter(Context ctx, List<BlueprintItemModel.BlueprintItem> blueprintsData) {
        this.blueprintsData = blueprintsData;
        this.c = ctx;
    }

    /**
     * Clears the data associated with the RecyclerView
     */
    public void clear() {
        blueprintsData.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list to the data.
     * @param list - the List of BlueprintItems
     */
    public void addAll(List<BlueprintItemModel.BlueprintItem> list) {
        blueprintsData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Gets the size of the list associated with the RecyclerView.
     * @return - the size of the list
     */
    @Override
    public int getItemCount() { return this.blueprintsData.size(); }

    /**
     * Inflates the layout and creates the CardViewHolder. Called iteratively through the data set.
     * @param viewGroup - the ViewGroup
     * @param i - index of the CardViewHolder based on the position in the data set.
     * @return - a CardViewHolder for this card.
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blueprints_card_view, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    /**
     * Called when the CardViewHolder is bound to the View.
     * @param cardViewHolder - the CardViewHolder for this card.
     * @param i - the index of the card's position in the list.
     */
    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.name_blueprints_text.setText(blueprintsData.get(i).name);
        cardViewHolder.id_blueprints_text.setText(blueprintsData.get(i).id);
        cardViewHolder.createdby_blueprints_text.setText(blueprintsData.get(i).createdBy);

        cardViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(c, BlueprintActivity.class);
                i.putExtra("BlueprintItemModel.BlueprintItem", blueprintsData.get(pos));

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

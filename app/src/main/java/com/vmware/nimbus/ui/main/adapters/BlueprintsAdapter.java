package com.vmware.nimbus.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintItemModel;

import java.util.List;

public class BlueprintsAdapter extends RecyclerView.Adapter<BlueprintsAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView blueprints_card_view;
        TextView name_blueprints_text;
        TextView id_blueprints_text;
        TextView createdby_blueprints_text;

        CardViewHolder(View itemView) {
            super(itemView);
            blueprints_card_view = itemView.findViewById(R.id.blueprints_card_view);
            name_blueprints_text = blueprints_card_view.findViewById(R.id.hello_blueprints_text);
            id_blueprints_text = blueprints_card_view.findViewById(R.id.world_blueprints_text);
            createdby_blueprints_text = blueprints_card_view.findViewById(R.id.index_blueprints_text);
        }
    }

    List<BlueprintItemModel.BlueprintItem> blueprintsData;

    public BlueprintsAdapter(List<BlueprintItemModel.BlueprintItem> blueprintsData) {
        this.blueprintsData = blueprintsData;
    }

    @Override
    public int getItemCount() { return this.blueprintsData.size(); }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blueprints_card_view, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.name_blueprints_text.setText(blueprintsData.get(i).name);
        cardViewHolder.id_blueprints_text.setText(blueprintsData.get(i).id);
        cardViewHolder.createdby_blueprints_text.setText(blueprintsData.get(i).createdBy);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

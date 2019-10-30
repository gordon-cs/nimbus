package com.vmware.nimbus.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vmware.nimbus.R;
import com.vmware.nimbus.data.model.BlueprintsModel;

import java.util.List;

public class BlueprintsAdapter extends RecyclerView.Adapter<BlueprintsAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView blueprints_card_view;
        TextView hello_blueprints_text;
        TextView world_blueprints_text;
        TextView index_blueprints_text;

        CardViewHolder(View itemView) {
            super(itemView);
            blueprints_card_view = itemView.findViewById(R.id.blueprints_card_view);
            hello_blueprints_text = blueprints_card_view.findViewById(R.id.hello_blueprints_text);
            world_blueprints_text = blueprints_card_view.findViewById(R.id.world_blueprints_text);
            index_blueprints_text = blueprints_card_view.findViewById(R.id.index_blueprints_text);
        }

    }

    List<BlueprintsModel> blueprintsData;

    public BlueprintsAdapter(List<BlueprintsModel> blueprintsData) {
        this.blueprintsData = blueprintsData;
    }

    @Override
    public int getItemCount() { return blueprintsData.size(); }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blueprints_card_view, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.hello_blueprints_text.setText(blueprintsData.get(i).helloData);
        cardViewHolder.world_blueprints_text.setText(blueprintsData.get(i).worldData);
        cardViewHolder.index_blueprints_text.setText(blueprintsData.get(i).dataIndex);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

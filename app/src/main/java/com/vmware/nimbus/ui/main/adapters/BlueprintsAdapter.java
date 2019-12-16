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

public class BlueprintsAdapter extends RecyclerView.Adapter<BlueprintsAdapter.CardViewHolder> implements Serializable {

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_blueprints_text;
        TextView id_blueprints_text;
        TextView createdby_blueprints_text;
        private ItemClickListener itemClickListener;

        CardViewHolder(View itemView) {
            super(itemView);
            name_blueprints_text = itemView.findViewById(R.id.name_blueprints_text);
            id_blueprints_text = itemView.findViewById(R.id.id_blueprints_text);
            createdby_blueprints_text = itemView.findViewById(R.id.createdby_blueprints_text);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }
    }

    Context c;
    List<BlueprintItemModel.BlueprintItem> blueprintsData;

    public BlueprintsAdapter(Context ctx, List<BlueprintItemModel.BlueprintItem> blueprintsData) {
        this.blueprintsData = blueprintsData;
        this.c = ctx;
    }

    public void clear() {
        blueprintsData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<BlueprintItemModel.BlueprintItem> list) {
        blueprintsData.addAll(list);
        notifyDataSetChanged();
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

        cardViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(c, BlueprintActivity.class);
                i.putExtra("BlueprintItemModel.BlueprintItem", blueprintsData.get(pos));

                c.startActivity(i);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

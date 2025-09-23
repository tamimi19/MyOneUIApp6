package com.example.oneuiapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneuiapp.R;

import java.util.List;

public class ScrollListAdapter extends RecyclerView.Adapter<ScrollListAdapter.ViewHolder> {
    
    private List<String> itemList;
    
    public ScrollListAdapter(List<String> itemList) {
        this.itemList = itemList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_scroll, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.titleText.setText(item);
        
        // Add some variety to the descriptions
        int descriptionRes;
        switch (position % 4) {
            case 0:
                descriptionRes = R.string.item_description_1;
                break;
            case 1:
                descriptionRes = R.string.item_description_2;
                break;
            case 2:
                descriptionRes = R.string.item_description_3;
                break;
            default:
                descriptionRes = R.string.item_description_4;
                break;
        }
        holder.descriptionText.setText(descriptionRes);
        
        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            // Handle item click if needed
        });
    }
    
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleText;
        TextView descriptionText;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            titleText = itemView.findViewById(R.id.title_text);
            descriptionText = itemView.findViewById(R.id.description_text);
        }
    }
}

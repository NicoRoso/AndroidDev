package com.mirea.nabiulingb.employeedb;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {
    private List<Hero> heroes;
    private OnHeroClickListener listener;

    public interface OnHeroClickListener {
        void onEditClick(Hero hero);
        void onDeleteClick(Hero hero);
    }

    public HeroAdapter(OnHeroClickListener listener) {
        this.listener = listener;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Hero hero = heroes.get(position);
        holder.tvName.setText(hero.name);
        holder.tvPower.setText(hero.superpower + " (Сила: " + hero.powerLevel + ")");

        holder.btnEdit.setOnClickListener(v -> {
            Log.d("HeroAdapter", "Edit clicked for hero: " + hero.name);
            listener.onEditClick(hero);
        });

        holder.btnDelete.setOnClickListener(v -> {
            Log.d("HeroAdapter", "Delete clicked for hero: " + hero.name);
            listener.onDeleteClick(hero);
        });
    }

    @Override
    public int getItemCount() {
        return heroes != null ? heroes.size() : 0;
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPower;
        Button btnEdit, btnDelete;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPower = itemView.findViewById(R.id.tvPower);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
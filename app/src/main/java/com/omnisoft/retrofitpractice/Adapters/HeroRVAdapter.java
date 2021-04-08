package com.omnisoft.retrofitpractice.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.omnisoft.retrofitpractice.App;
import com.omnisoft.retrofitpractice.R;
import com.omnisoft.retrofitpractice.Room.Entity;
import com.omnisoft.retrofitpractice.databinding.HeroModelViewBinding;

import java.util.ArrayList;
import java.util.List;

public class HeroRVAdapter extends RecyclerView.Adapter<HeroRVAdapter.RVHolder> {

    ArrayList<Entity> heroes;
    HeroModelViewBinding bd;
    Activity activity;

    public HeroRVAdapter(Activity activity) {
        this.activity = activity;
        heroes = new ArrayList<>();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bd = HeroModelViewBinding.inflate(activity.getLayoutInflater(), parent, false);
        return new RVHolder(bd.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, int position) {
        holder.setBio(heroes.get(position).getBio());
        holder.setCreatedby(heroes.get(position).getCreatedby());
        holder.setFirstappearance(heroes.get(position).getFirstappearance());
        holder.setImageurl(heroes.get(position).getImageurl());
        holder.setName(heroes.get(position).getName());
        holder.setPublisher(heroes.get(position).getPublisher());
        holder.setRealname(heroes.get(position).getRealname());
        holder.setTeam(heroes.get(position).getTeam());
    }

    public void updateList(List<Entity> list) {
        heroes = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return heroes.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {

        public RVHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setBio(String bio) {
            bd.bio.setText(bio);
        }

        public void setCreatedby(String createdby) {
            bd.createdBy.setText(createdby);
        }

        public void setFirstappearance(String firstappearance) {
            bd.firstAppearance.setText(firstappearance);
        }

        public void setImageurl(String imageurl) {
            Glide.with(App.context).load(imageurl).placeholder(ContextCompat.getDrawable(App.context, R.drawable.ic_baseline_image_24)).transition(DrawableTransitionOptions.withCrossFade()).into(bd.imgView);
        }

        public void setName(String name) {
            bd.name.setText(name);
        }

        public void setPublisher(String publisher) {
            bd.publisher.setText(publisher);
        }

        public void setRealname(String realname) {
            bd.realName.setText(realname);
        }

        public void setTeam(String team) {
            bd.team.setText(team);
        }
    }
}

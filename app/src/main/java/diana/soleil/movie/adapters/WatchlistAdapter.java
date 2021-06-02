package diana.soleil.movie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import diana.soleil.movie.R;
import diana.soleil.movie.databinding.ItemContainerTvShowBinding;

import diana.soleil.movie.listeners.WatchlistListner;
import diana.soleil.movie.models.TVShow;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {

    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListner watchlistListner;

    public WatchlistAdapter(List<TVShow> tvShows, WatchlistListner watchlistListner ) {
        this.tvShows = tvShows;
        this.watchlistListner = watchlistListner;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_tv_show,parent,false);
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }
        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListner.onTVShowClicked(tvShow));
            itemContainerTvShowBinding.imageDelete.setOnClickListener(v -> watchlistListner.removeTVShowFromWatchlist(tvShow, getAdapterPosition()));
            itemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }

}

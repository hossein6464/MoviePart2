package diana.soleil.movie.adapters;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import diana.soleil.movie.R;
import diana.soleil.movie.databinding.ItemContainerEpisodesBinding;
import diana.soleil.movie.models.Episode;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

        private List<Episode> episodes;
        private LayoutInflater layoutInflater;

        public EpisodesAdapter(List<Episode> episodes) {
                this.episodes = episodes;
        }

        @NonNull
        @NotNull
        @Override
        public EpisodeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                if (layoutInflater == null) {
                        layoutInflater = LayoutInflater.from(parent.getContext());
                }
                ItemContainerEpisodesBinding itemContainerEpisodesBinding = DataBindingUtil.inflate(
                  layoutInflater, R.layout.item_container_episodes,parent,false
                );
                return new EpisodeViewHolder(itemContainerEpisodesBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull EpisodesAdapter.EpisodeViewHolder holder, int position) {
                holder.bindEpisode(episodes.get(position));
        }

        @Override
        public int getItemCount() {
                return episodes.size();
        }

        static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerEpisodesBinding itemContainerEpisodesBinding;

        public EpisodeViewHolder(ItemContainerEpisodesBinding itemContainerEpisodesBinding) {
        super(itemContainerEpisodesBinding.getRoot());
        this.itemContainerEpisodesBinding = itemContainerEpisodesBinding;
        }
        public void bindEpisode(Episode episode) {
                String title = "S";
                String season = episode.getSeason();
                if (season.length() == 1) {
                        season = "0".concat(season);
                }
                String episodeNumber = episode.getEpisode();
                if (episodeNumber.length() == 1) {
                        episodeNumber = "0".concat(episodeNumber);
                }
                episodeNumber = "E".concat(episodeNumber);
                title = title.concat(season).concat(episodeNumber);
                itemContainerEpisodesBinding.setTitle(title);
                itemContainerEpisodesBinding.setName(episode.getName());
                itemContainerEpisodesBinding.setAirDate(episode.getAirDate());
        }
        }
}

package diana.soleil.movie.listeners;

import diana.soleil.movie.models.TVShow;

public interface WatchlistListner {
    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWatchlist(TVShow tvShow, int position);
}

package diana.soleil.movie.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import diana.soleil.movie.database.TVShowsDatabase;
import diana.soleil.movie.models.TVShow;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {
    private TVShowsDatabase tvShowsDatabase;

    public WatchlistViewModel (@NonNull Application application){
        super (application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchList() {
        return tvShowsDatabase.tvShowDao().getWatchList();
    }
    public Completable removeTVShowFromWatchList (TVShow tvShow){

        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}

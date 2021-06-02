package diana.soleil.movie.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;
import diana.soleil.movie.database.TVShowsDatabase;
import diana.soleil.movie.models.TVShow;
import diana.soleil.movie.repositories.TVShowDetailsRepository;
import diana.soleil.movie.responses.TVShowDetailsResponse;
import io.reactivex.Completable;
import io.reactivex.Flowable;


public class TVShowDetailsViewModel extends AndroidViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }
    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchList(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().addToWatchList(tvShow);
    }
    public Flowable<TVShow> getTVShowFromWatchList(String tvShowId) {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }
    public Completable removeTVShowFromWatchList(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}

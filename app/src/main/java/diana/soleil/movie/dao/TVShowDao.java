package diana.soleil.movie.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import diana.soleil.movie.models.TVShow;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShow>> getWatchList();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchList(TVShow tvShow);

    @Delete
    Completable removeFromWatchList(TVShow tvShow);

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<TVShow> getTVShowFromWatchlist (String tvShowId);
}

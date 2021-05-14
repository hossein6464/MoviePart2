package diana.soleil.movie.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import diana.soleil.movie.repositories.MostPopularTVShowsRepository;
import diana.soleil.movie.responses.TVShowsResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();
    }
    public LiveData<TVShowsResponse> getMostPopularTVShows(int page) {
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}

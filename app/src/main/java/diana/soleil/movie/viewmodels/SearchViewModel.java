package diana.soleil.movie.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import diana.soleil.movie.repositories.SearchTVShowRepository;
import diana.soleil.movie.responses.TVShowsResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }
    public LiveData<TVShowsResponse> searchTVShow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query,page);
    }
}

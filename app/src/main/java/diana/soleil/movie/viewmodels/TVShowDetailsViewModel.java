package diana.soleil.movie.viewmodels;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;
import diana.soleil.movie.repositories.TVShowDetailsRepository;
import diana.soleil.movie.responses.TVShowDetailsResponse;


public class TVShowDetailsViewModel extends ViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;

    public TVShowDetailsViewModel() {
        tvShowDetailsRepository = new TVShowDetailsRepository();
    }
    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
}

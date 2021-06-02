package diana.soleil.movie.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import diana.soleil.movie.network.ApiClient;
import diana.soleil.movie.network.ApiService;
import diana.soleil.movie.responses.TVShowsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTVShowRepository {

    private ApiService apiService;

    public SearchTVShowRepository () {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<TVShowsResponse> searchTVShow (String query, int page) {
        MutableLiveData<TVShowsResponse> data = new MutableLiveData<>();
        apiService.searchTVShow(query,page).enqueue(new Callback<TVShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsResponse> call, @NonNull Response<TVShowsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsResponse> call,@NonNull Throwable t) {
            data.setValue(null);
            }
        });
        return data;
    }
}

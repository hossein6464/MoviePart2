package diana.soleil.movie.network;

import diana.soleil.movie.responses.TVShowsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET ("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);
}
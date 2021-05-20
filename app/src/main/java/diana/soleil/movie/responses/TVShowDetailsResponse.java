package diana.soleil.movie.responses;

import com.google.gson.annotations.SerializedName;

import diana.soleil.movie.models.TVShowDetails;

public class TVShowDetailsResponse {
    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}

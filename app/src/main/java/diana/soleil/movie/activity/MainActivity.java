package diana.soleil.movie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import diana.soleil.movie.R;
import diana.soleil.movie.viewmodels.MostPopularTVShowsViewModel;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MostPopularTVShowsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        getMostPopularTVShows();
    }
    public void getMostPopularTVShows(){
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponse ->
                Toast.makeText(this, "Total Pages: " + mostPopularTVShowsResponse.getTotalPages(), Toast.LENGTH_SHORT).show());
    }
}
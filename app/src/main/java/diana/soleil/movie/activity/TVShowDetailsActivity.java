package diana.soleil.movie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import diana.soleil.movie.R;
import diana.soleil.movie.adapters.EpisodesAdapter;
import diana.soleil.movie.adapters.ImageSliderAdapter;
import diana.soleil.movie.databinding.ActivityTVShowDetailsBinding;
import diana.soleil.movie.databinding.LayoutEpidosdesBottomSheetBinding;
import diana.soleil.movie.network.ApiService;
import diana.soleil.movie.responses.TVShowDetailsResponse;
import diana.soleil.movie.viewmodels.TVShowDetailsViewModel;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;


public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTVShowDetailsBinding activityTVShowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpidosdesBottomSheetBinding layoutEpidosdesBottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_t_v_show_details);
        doInitialization();

    }
    private void doInitialization(){
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTVShowDetailsBinding.imageBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getTVShowDetails();
    }
    private void getTVShowDetails() {
        activityTVShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse ->{
                    activityTVShowDetailsBinding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowDetails() != null) {
                        if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                        }
                            activityTVShowDetailsBinding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                            activityTVShowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.setDescription(
                                    String.valueOf(
                                            HtmlCompat.fromHtml(
                                                    tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                            )
                                    )
                            );
                            activityTVShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (activityTVShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                        activityTVShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                        activityTVShowDetailsBinding.textDescription.setEllipsize(null);
                                        activityTVShowDetailsBinding.textReadMore.setText(R.string.read_less);
                                    } else {
                                        activityTVShowDetailsBinding.textDescription.setMaxLines(4);
                                        activityTVShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                        activityTVShowDetailsBinding.textReadMore.setText(R.string.read_more);
                                    }
                                }
                            });
                            activityTVShowDetailsBinding.setRating(
                                    String.format(
                                            Locale.getDefault(),
                                            "%.2f",
                                            Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                                    )
                            );
                            if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                                activityTVShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                            } else {
                                activityTVShowDetailsBinding.setGenre("N/A");
                            }
                            activityTVShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                            activityTVShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                                startActivity(intent);
                            });
                            activityTVShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                            activityTVShowDetailsBinding.buttonEpisode.setVisibility(View.VISIBLE);

                        activityTVShowDetailsBinding.buttonEpisode.setOnClickListener(v -> {
                            if (episodesBottomSheetDialog == null) {
                                episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                layoutEpidosdesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailsActivity.this),
                                        R.layout.layout_epidosdes_bottom_sheet,
                                        findViewById(R.id.episodeContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpidosdesBottomSheetBinding.getRoot());
                                layoutEpidosdesBottomSheetBinding.episodesRecycleView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpidosdesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", getIntent().getStringExtra("name"))
                                );
                                layoutEpidosdesBottomSheetBinding.imageClose.setOnClickListener(v1 -> episodesBottomSheetDialog.dismiss());
                            }
                            // Optional Section
                            FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            // End of Optional Section

                            episodesBottomSheetDialog.show();

                        });
                            loadBasicTVShowDetails();
                        }
                }
                );
    }
    private void loadImageSlider (String[] sliderImages) {

        activityTVShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTVShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTVShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTVShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentIndicator(position);
            }
        });
    }
    private void setupSliderIndicators(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0; i<indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTVShowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTVShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setupCurrentIndicator(0);
    }
    private void setupCurrentIndicator(int position){
        int childCount = activityTVShowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i=0; i < childCount; i++){
            ImageView imageView = (ImageView) activityTVShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.background_slider_indicator_inactive));
            }
        }
    }
    private void loadBasicTVShowDetails() {
        String name = String.valueOf(getIntent().getStringExtra("name"));
        String startDate = String.valueOf(getIntent().getStringExtra("startDate"));
        String country = String.valueOf(getIntent().getStringExtra("country"));
        String network = String.valueOf(getIntent().getStringExtra("network"));
        String status = String.valueOf(getIntent().getStringExtra("status"));
        activityTVShowDetailsBinding.setTvShowName(name);
        activityTVShowDetailsBinding.setNetworkCountry(network + " (" + country + ")");
        activityTVShowDetailsBinding.setStatus(status);
        activityTVShowDetailsBinding.setStartDate(startDate);
        activityTVShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);


    }
}
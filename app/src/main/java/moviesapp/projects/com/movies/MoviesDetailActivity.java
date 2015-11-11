package moviesapp.projects.com.movies;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MoviesDetailActivity extends Activity {
    private final String LOG_TAG = MoviesDetailActivity.class.getSimpleName();
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    JSONObject myMovieDetails;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        try{
            myMovieDetails = new JSONObject(getIntent().getStringExtra("MovieJsonObj"));
            ImageView iv = (ImageView)findViewById(R.id.iv_movie_backdrop);
            if (iv != null)  {
                if(myMovieDetails.getString("backdrop_path") != null) {
                    Log.i(LOG_TAG, "Backdrop path:" + BASE_URL + myMovieDetails.getString("backdrop_path"));
                    Picasso.with(getApplicationContext()).load(BASE_URL + myMovieDetails.getString("backdrop_path")).into(iv);
                    //iv.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
            TextView tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
            if(tv_movie_title != null){
                if(myMovieDetails.getString("original_title")!= null){
                    Log.i(LOG_TAG,"Movie title:" + myMovieDetails.getString("original_title"));
                    tv_movie_title.setText(myMovieDetails.getString("original_title"));
                }
            }
            RatingBar userRating = (RatingBar) findViewById(R.id.rb_user_rating);
            if (userRating!=null){
                if(myMovieDetails.getString("vote_average")!= null){
                    Log.i(LOG_TAG,"vote_average:" + myMovieDetails.getString("vote_average"));
                    userRating.setRating(myMovieDetails.getLong("vote_average") / 2);
                }
            }
            TextView tv_movie_plot = (TextView) findViewById(R.id.tv_movie_plot);
            if (tv_movie_plot!=null){
                if(myMovieDetails.getString("overview")!= null){
                    tv_movie_plot.setText(myMovieDetails.getString("overview"));
                }
            }
            TextView tv_release_Date = (TextView) findViewById(R.id.tv_release_date);
            if (tv_release_Date!=null){
                if(myMovieDetails.getString("overview")!= null){
                    tv_release_Date.setText(myMovieDetails.getString("release_date"));
                }
            }
        }catch(Exception ex)    {
            Log.e(LOG_TAG, ex.getMessage(),ex);
        }


    }

}

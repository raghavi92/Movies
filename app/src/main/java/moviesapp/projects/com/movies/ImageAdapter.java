package moviesapp.projects.com.movies;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by welcome on 21-10-2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private JSONArray myArray;
    LayoutInflater layoutInflater;
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    public ImageAdapter(Context context, JSONArray myArray)    {
        this.context = context;
        this.myArray = myArray;
        layoutInflater = LayoutInflater.from(this.context);
        Log.i(LOG_TAG,"Image adapter initialized!!  " + getCount());
    }
    @Override
    public int getCount() {
        return myArray.length();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final String baseUrlForImage = "http://image.tmdb.org/t/p/w185/";
        MyViewHolder myViewHolder ;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.movies_imageview,parent,false);
            myViewHolder = new MyViewHolder();
            myViewHolder.iv = (ImageView) convertView.findViewById(R.id.my_image_view);
            convertView.setTag(myViewHolder);

        }   else    {
            myViewHolder =(MyViewHolder) convertView.getTag();
        }
        try {
            final JSONObject myMovie =  myArray.getJSONObject(position);
            if (myMovie != null) {

                Log.i(LOG_TAG, "image path for position " + position + " is:" + BASE_URL + myMovie.getString("poster_path"));
                Picasso.with(this.context).load(BASE_URL + myMovie.getString("poster_path")).resize(200,200).centerCrop().into(myViewHolder.iv);
                myViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           try {
                                                               Log.i(LOG_TAG, "Movie name: " + myMovie.getString("original_title"));
                                                               Intent startDetailMovieIntent = new Intent();
                                                               startDetailMovieIntent.putExtra("MovieJsonObj",myMovie.toString());
                                                               startDetailMovieIntent.setClass(context,MoviesDetailActivity.class);
                                                               context.startActivity(startDetailMovieIntent);
                                                           } catch (JSONException e) {
                                                               Log.e(LOG_TAG, e.getMessage(), e);
                                                           }

                                                       }
                                                   }

                );
                }
            } catch (JSONException e){
            Log.e(LOG_TAG,e.getMessage(),e);
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        try {
            return myArray.getJSONObject(position);
        }   catch(Exception e)  {
            Log.e(LOG_TAG,e.getMessage(),e);
            return null;
        }
    }

    static class MyViewHolder  {
        ImageView iv;

    }
}

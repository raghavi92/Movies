package moviesapp.projects.com.movies;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoverMainActivityFragment extends Fragment {
    View rootView;
    JSONArray myMoviesArray;
    private final String LOG_TAG = RequestTheMovies.class.getSimpleName();
    public DiscoverMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discover_main, container, false);
        accessMoviesData();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void accessMoviesData()   {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            String params[] = new String[1];
            params[0] = sharedPreferences.getString(getString(R.string.settings_sort_by_key), getString(R.string.settings_sort_by_default));
            RequestTheMovies movies = new RequestTheMovies();

            movies.execute(params);
        } catch (Exception e ){
            Log.e(LOG_TAG,e.getMessage(),e);
        }
    }
    public class RequestTheMovies extends AsyncTask<String,Void,String> {
        private final String LOG_TAG = RequestTheMovies.class.getSimpleName();
        private final String LOG_FOR_JSONEXCEPTION = "JSONEXCEPTION";
        private final String MY_API_KEY = "";
        private ProgressDialog progressDialog;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    myMoviesArray = jsonObject.getJSONArray("results");
                    Log.i(LOG_TAG, "results array count:" + myMoviesArray.length());
                    GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
                    gridView.setAdapter(new ImageAdapter(rootView.getContext(), myMoviesArray));
                }else   {
                    Log.i(LOG_TAG,"return value from bg is null");
                }
                progressDialog.dismiss();
            } catch(JSONException e)    {
                Log.e(LOG_TAG,e.getMessage(),e);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(rootView.getContext());
            progressDialog.setMessage("Loading movies");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if( params.length == 0 ){
                return null;
            }
            Log.i(LOG_TAG,"Sort by value:" + params[0]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resp;
            try {
                Uri builtURI = Uri.parse("http://api.themoviedb.org/3/discover/movie?").buildUpon()
                        .appendQueryParameter("sort_by", params[0])
                        .appendQueryParameter("api_key",MY_API_KEY)
                        .build();
                URL url = new URL(builtURI.toString());
                Log.i(LOG_TAG, "Build URI:" + builtURI.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream iStream = urlConnection.getInputStream();
                if (iStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                resp = buffer.toString();
                Log.i(LOG_TAG, "response:" + resp);

                return resp;
            }   catch(Exception e ) {
                Log.e(LOG_TAG,e.getMessage(),e);
                return null;
            }
        }
    }

}

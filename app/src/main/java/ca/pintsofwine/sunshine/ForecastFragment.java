package ca.pintsofwine.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private ArrayAdapter<String> weatherAdapter;
    private SharedPreferences prefs;
    private String locationKey;
    private String defaultValue;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        locationKey = getString(R.string.pref_location_key);
        defaultValue = getString(R.string.pref_location_default);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        weatherAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                new ArrayList<String>()
        );

        //Goal is to bind weatherAdapter to the list view (which is in fragment_main.xml ID
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(weatherAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = weatherAdapter.getItem(position);
                Intent detailedForecastIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(detailedForecastIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeatherData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            updateWeatherData();
            return true;
        } else if (item.getItemId() == R.id.action_show_location) {
            Log.v(LOG_TAG, "Match show location item");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + prefs.getString(locationKey, defaultValue)));

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.e(LOG_TAG, "Couldn't resolve activity!");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateWeatherData(String[] weatherData) {
        ArrayList<String> weatherDataList = new ArrayList<String>(Arrays.asList(weatherData));
        weatherAdapter.clear();

        for (String forecast : weatherDataList) {
            weatherAdapter.add(forecast);
        }
    }

    private void updateWeatherData() {
        FetchWeatherTask task = new FetchWeatherTask(this);
        task.execute(prefs.getString(locationKey, defaultValue));
    }
}

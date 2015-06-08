package ca.pintsofwine.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private ArrayAdapter weatherAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] weatherData = new String[] {
                "Today - Sunny 88/63",
                "Tomorrow - Sunny 88/63",
                "Saturday - Sunny 88/63",
                "Sunday - Sunny 88/63",
                "Monday - Sunny 88/63",
                "Tuesday - Sunny 88/63",
                "Wednesday - Sunny 88/63"
        };

        List<String> weatherDataList = new ArrayList<String>(Arrays.asList(weatherData));
        weatherAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weatherDataList
        );

        //Goal is to bind weatherAdapter to the list view (which is in fragment_main.xml ID
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(weatherAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            FetchWeatherTask task = new FetchWeatherTask(this);
            task.execute("98119");
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
}
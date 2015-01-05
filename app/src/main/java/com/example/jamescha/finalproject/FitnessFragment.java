package com.example.jamescha.finalproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jamescha.finalproject.data.FitnessContract;

public class FitnessFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private final String LOG_TAG = FitnessFragment.class.getSimpleName();
    private FitnessAdapter mFitnessAdapter;
    ListView mListView;

    private static final int FITNESS_LOADER = 0;

    public FitnessFragment() {
        // Required empty public constructor
    }

    private static final String[] STEPS_COLUMNS = {
            FitnessContract.StepsEntry.TABLE_NAME + "." + FitnessContract.StepsEntry._ID,
            FitnessContract.StepsEntry.COLUMN_STEPS_DATE,
            FitnessContract.StepsEntry.COLUMN_STEPS_COUNT
    };

    public static final int COL_STEPS_ID = 0;
    public static final int COL_STEPS_DATE = 1;
    public static final int COL_STEPS_COUNT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Fragment Created");
        super.onCreate(savedInstanceState);
    }

    public interface Callback {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Create Fitness Fragment View")
        mFitnessAdapter = new FitnessAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_fitness_data);
        mListView.setAdapter(mFitnessAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FITNESS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = null;
        Log.i(LOG_TAG, "Create Loader");
        Uri stepsUri = FitnessContract.StepsEntry.buildStepsUri(id);

        return new CursorLoader(getActivity(), stepsUri, STEPS_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFitnessAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFitnessAdapter.swapCursor(null);
    }
}

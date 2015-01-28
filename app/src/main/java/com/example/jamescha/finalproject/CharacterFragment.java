package com.example.jamescha.finalproject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jamescha.finalproject.data.FitnessContract;

public class CharacterFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = CharacterFragment.class.getSimpleName();

    private static final int CHARACTER_LOADER = 0;

    private static final String[] CHARACTER_COLUMNS = {
            FitnessContract.CharacterEntry.TABLE_NAME + "." + FitnessContract.CharacterEntry._ID,
            FitnessContract.CharacterEntry.COLUMN_CHARACTER_NAME,
            FitnessContract.CharacterEntry.COLUMN_CHARACTER_LEVEL,
            FitnessContract.CharacterEntry.COLUMN_CHARACTER_EXP
    };

    private TextView mCharacterName;
    private TextView mCharacterLevel;
    private TextView mCharacterExp;
    private ImageView mCharacterImage;

    public CharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "CharacterFragment ActivityCreated");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_character, container, false);
        Log.i(LOG_TAG, "CharacterFragment View Created");

     //   mCharacterImage = (ImageView) rootView.findViewById(R.id.character_image);
        mCharacterName = (TextView) rootView.findViewById(R.id.character_name);
        mCharacterExp = (TextView) rootView.findViewById(R.id.character_exp);
        mCharacterLevel = (TextView) rootView.findViewById(R.id.character_level);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "CharacterFragment Loader Created");
        String sortOrder = null;


        return new CursorLoader(
                getActivity(),
                FitnessContract.CharacterEntry.CONTENT_URI,
                CHARACTER_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "Character Fragment Loaded");
        mCharacterName.setText(data.getString(data.getColumnIndex(FitnessContract.CharacterEntry.COLUMN_CHARACTER_NAME)));
        mCharacterLevel.setText(data.getInt(data.getColumnIndex(FitnessContract.CharacterEntry.COLUMN_CHARACTER_LEVEL)));
        mCharacterExp.setText(data.getString(data.getColumnIndex(FitnessContract.CharacterEntry.COLUMN_CHARACTER_EXP)));
     //   mCharacterImage.setImageResource();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

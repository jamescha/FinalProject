package com.example.jamescha.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by jamescha on 1/1/15.
 */
public class FitnessAdapter extends CursorAdapter{

    private static final int VIEW_TYPE_STEPS = 0;
    private static final int VIEW_TYPE_CHARACTER = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private boolean mUseLayout = true;

    public static class ViewHolder {

        public final TextView textView;

        public ViewHolder (View view) {
            textView = (TextView) view.findViewById(R.id.list_item_textview);
        }
    }

    public FitnessAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        Integer vT = viewType;

        Log.i("FitnessAdapter", vT.toString());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_STEPS: {
                layoutId = R.layout.list_fitness_data;
                break;
            }
            case VIEW_TYPE_CHARACTER: {
                layoutId = R.layout.fragment_character;
                break;
            }
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int viewType = getItemViewType(cursor.getPosition());



        switch (viewType) {
            case VIEW_TYPE_STEPS: {
                if (cursor.getCount() == 0) {
                    viewHolder.textView.setText("You have no step data!");
                } else {
                    String textString = cursor.getString(FitnessFragment.COL_STEPS_COUNT);
                    String dateString = cursor.getString(FitnessFragment.COL_STEPS_DATE);

                    viewHolder.textView.setText(dateString + " " + textString + " Steps");
                }
            }
            case VIEW_TYPE_CHARACTER: {

            }
        }


    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public void setUseLayout(boolean useLayout) {
        mUseLayout = useLayout;
    }
}

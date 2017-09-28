package com.etech.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> mData;
    int textViewResourceId;
    
    public AutoCompleteAdapter(Context context, int textViewResourceId, ArrayList<String> mData) {
        super(context, textViewResourceId, mData);
        this.mData = mData;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
    
    /*
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        TextView originalView = (TextView) super.getView(position, convertView, parent); // Get the original view

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final TextView view = (TextView) inflater.inflate(textViewResourceId , parent, false);

        view.setText(originalView.getText());
        view.setTextColor(Color.MAGENTA);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        return view;
    }
    */
}
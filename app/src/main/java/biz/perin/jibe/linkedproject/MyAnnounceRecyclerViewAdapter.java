package biz.perin.jibe.linkedproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import biz.perin.jibe.linkedproject.AnnounceFragment.OnListAnnounceInteractionListener;


import java.util.HashMap;
import java.util.List;


public class MyAnnounceRecyclerViewAdapter extends RecyclerView.Adapter<MyAnnounceRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListAnnounceInteractionListener mListener;

    public MyAnnounceRecyclerViewAdapter(List<String> items, OnListAnnounceInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_announce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String jsAnnonce = mValues.get(position);

        HashMap<String, String> dValues = MyHelper.json2dict(jsAnnonce);

        holder.announceId.setText(dValues.get("id"));
        holder.description.setText(dValues.get("description"));

        if (dValues.get("direction").equals("DEMAND")) {
            holder.direction.setImageDrawable(new ColorDrawable(Color.GREEN));
        } else {
            holder.direction.setImageDrawable(new ColorDrawable(Color.BLUE));
        }

        int categoryColor = Color.WHITE;
        // TODO retrouver la catégorie
        holder.category.setImageDrawable(new ColorDrawable(categoryColor));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListAnnounceInteraction(jsAnnonce);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView announceId;
        public TextView description;
        public ImageView direction;
        public ImageView category;
        public String mItem;
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            announceId = (TextView) view.findViewById(R.id.announceId);
            description = (TextView) view.findViewById(R.id.description);
            direction = (ImageView) view.findViewById(R.id.direction);
            category = (ImageView) view.findViewById(R.id.category);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "announceId=" + announceId +
                    ", description=" + description +
                    ", direction=" + direction +
                    ", category=" + category +
                    '}';
        }
    }
}

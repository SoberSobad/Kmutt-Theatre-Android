package com.kmutt.sit.theater.booking.showtimes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmutt.sit.theater.R;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

class ShowtimeLocationSection extends StatelessSection {

    //
    // Props
    //
//    List<Showtime.MovieInfo> itemList = Arrays.asList("Item1", "Item2", "Item3");
//    List<Showtime.MovieInfo> itemList;
    Showtime mShowtime;
    Context mContext;

    //
    // OnCLickListener
    //
    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener mListener) {
        this.mListener = mListener;
    }


    //
    // Constructor
    //
    public ShowtimeLocationSection(Context context, Showtime showtime) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_movie_showtime)
                .headerResourceId(R.layout.item_movie_showtime_section_header)
                .build());
        mContext = context;
        mShowtime = showtime;
    }

//    public void updateMoviesList(List<Showtime.MovieInfo> itemList) {
//        this.itemList = itemList;
//        notifyDataSetChanged();
//    }

    @Override
    public int getContentItemsTotal() {
        return mShowtime.rooms.size(); // number of items of this section
    }

    //
    // ITEM
    //
    ViewGroup itemParent;

    @Override
    public View getItemView(ViewGroup parent) {
        this.itemParent = parent;
        return LayoutInflater.from(mContext).inflate(R.layout.item_showtime, itemParent);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_showtime, itemParent);

        // VIEW HOLDER
        final ViewHolder vh = new ViewHolder(view);

        // ON CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onClick(vh);
            }
        });

        return vh;
    }
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.bind(mShowtime.rooms.get(position));
    }

    //
    // SECTION HEADER
    //
    ViewGroup headerParent;
    @Override
    public View getHeaderView(ViewGroup headerParent) {
        this.headerParent = headerParent;
        return LayoutInflater.from(mContext).inflate(R.layout.item_movie_showtime_section_header, headerParent);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_showtime_section_header, headerParent);
        SectionHeaderViewHolder vh = new SectionHeaderViewHolder(view);

        return vh;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SectionHeaderViewHolder vh = (SectionHeaderViewHolder) holder;
        vh.bind(mShowtime);
    }

    //
    // ITEM VH
    //
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRoomNo;
        private final TextView tvRoomType;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRoomNo = itemView.findViewById(R.id.tvRoomNo);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
        }

        public void bind(Showtime.Room r) {
            tvRoomNo.setText("THEATRE NO " + r.roomNo);
            tvRoomType.setText(r.roomType);
        }
    }


    //
    // SECTION HEADER VH
    //
    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvBranchName;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);

            tvBranchName = itemView.findViewById(R.id.tvBranchName);
        }

        public void bind(Showtime s) {
            tvBranchName.setText(s.branchName);
        }
    }

    //
    // OnClickListener
    //
    public interface OnClickListener {
        void onClick(ViewHolder v);
    }

}

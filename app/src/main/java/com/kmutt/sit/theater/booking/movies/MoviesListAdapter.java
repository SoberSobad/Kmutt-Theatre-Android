package com.kmutt.sit.theater.booking.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmutt.sit.theater.R;
import com.kmutt.sit.theater.shared.libs.GlideApp;

import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    //
    // Props
    //
    private Context mContext;
    private List<Movie> moviesList;

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
    public MoviesListAdapter(Context context, List<Movie> moviesList) {
        super();
        mContext = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // VIEW
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_movie, null);

        // VIEW HOLDER
        final ViewHolder vh = new ViewHolder(rootView);

        // ON CLICK
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onClick(vh);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(moviesList.get(i));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Movie movie;
        TextView tvMovieTitle, tvMovieLang, tvShowtimes;
        ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieLang = itemView.findViewById(R.id.tvMovieLang);
            tvShowtimes = itemView.findViewById(R.id.tvShowtimes);

            poster = itemView.findViewById(R.id.poster);
        }

        public void bindData(Movie m) {
            this.movie = m;

            tvMovieTitle.setText(m.name);
            GlideApp.with(mContext)
                    .load(m.imageUrl)
                    .into(poster);

//            String showTimeText = "";
//            for (String s : m.showTimes) {
//                showTimeText += (s + " ");
//            }
//            tvShowtimes.setText(showTimeText);
        }
    }

    //
    // OnClickListener
    //
    public interface OnClickListener {
        void onClick(ViewHolder v);
    }
}

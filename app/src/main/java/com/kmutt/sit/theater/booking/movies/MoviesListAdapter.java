package com.kmutt.sit.theater.booking.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

    public void updateMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
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
        TextView tvMovieTitle, tvMovieLang, tvMovieLength;
        ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieLang = itemView.findViewById(R.id.tvMovieGenre);
            tvMovieLength = itemView.findViewById(R.id.tvMovieLength);

            poster = itemView.findViewById(R.id.poster);
        }

        public void bindData(Movie m) {
            this.movie = m;

            //
            // TITLE
            //
            tvMovieTitle.setText(m.name);

            //
            // POSTER
            //
            m.imageUrl = m.imageUrl.replace('\\', '\0');    // remove all the \ symbol
            GlideApp.with(mContext)
                    .load(m.imageUrl)
                    .into(poster);

            //
            // LENGTH
            //
            String[] length = m.length.split(":");
            String hr = length[0];
            String min = length[1];

            // remove 0 from first digit
            if (hr.charAt(0) == '0')
                hr = hr.substring(1);
            if (min.charAt(0) == '0')
                min = min.substring(1);

            tvMovieLength.setText(hr + " hr " + min + " min.");

//            tvMovieLength.setText(m.imageUrl);

//            String showTimeText = "";
//            for (String s : m.showTimes) {
//                showTimeText += (s + " ");
//            }
//            tvMovieLength.setText(showTimeText);
        }
    }

    //
    // OnClickListener
    //
    public interface OnClickListener {
        void onClick(ViewHolder v);
    }
}

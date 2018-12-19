package com.kmutt.sit.theater.booking.showtimes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmutt.sit.theater.R;

import java.util.List;

public class ShowtimesListAdapter extends RecyclerView.Adapter<ShowtimesListAdapter.ViewHolder> {

    //
    // Props
    //
    private Context mContext;
    private List<Showtime> showtimeList;

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
    public ShowtimesListAdapter(Context context, List<Showtime> showtimeList) {
        super();
        mContext = context;
        this.showtimeList = showtimeList;
    }

    public void updateMoviesList(List<Showtime> moviesList) {
        this.showtimeList = moviesList;
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
        viewHolder.bindData(showtimeList.get(i));
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Showtime showtime;
        TextView tvMovieTitle, tvMovieLang, tvMovieLength;
        ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieLang = itemView.findViewById(R.id.tvMovieGenre);
            tvMovieLength = itemView.findViewById(R.id.tvMovieLength);

            poster = itemView.findViewById(R.id.poster);
        }

        public void bindData(Showtime s) {
            this.showtime = s;

            //
            // TITLE
            //
            tvMovieTitle.setText(s.branchName);

//            //
//            // POSTER
//            //
//            GlideApp.with(mContext)
//                    .load(s.imageUrl)
//                    .into(poster);

//            //
//            // LENGTH
//            //
//            String[] length = s.length.split(":");
//            String hr = length[0];
//            String min = length[1];
//
//            // remove 0 from first digit
//            if (hr.charAt(0) == '0')
//                hr = hr.substring(1);
//            if (min.charAt(0) == '0')
//                min = min.substring(1);
//
//            tvMovieLength.setText(hr + " hr " + min + " min.");

//            tvMovieLength.setText(s.imageUrl);
        }
    }

    //
    // OnClickListener
    //
    public interface OnClickListener {
        void onClick(ViewHolder v);
    }
}

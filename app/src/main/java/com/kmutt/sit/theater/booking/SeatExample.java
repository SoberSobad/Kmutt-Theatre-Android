package com.kmutt.sit.theater.booking;

import android.graphics.Color;
import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;

public class SeatExample implements Seat {

        public int id;
        public int color = Color.RED;
        public String marker;
        public String selectedSeatMarker;
        public HallScheme.SeatStatus status;

        @Override
        public int id() {
            return id;
        }

        @Override
        public int color() {
            return color;
        }

        @Override
        public String marker() {
            return marker;
        }

        @Override
        public String selectedSeat() {
            return selectedSeatMarker;
        }

        @Override
        public HallScheme.SeatStatus status() {
            return status;
        }

        @Override
        public void setStatus(HallScheme.SeatStatus status) {
            this.status = status;
        }





    }



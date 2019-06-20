package com.example.eticket;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eticket.model.Booking;

import java.util.List;

public class BookingRecyclerAdapter extends RecyclerView.Adapter<BookingRecyclerAdapter.UserViewHolder> {

    private List<Booking> listUsers;

    public BookingRecyclerAdapter(List<Booking> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewSource.setText(listUsers.get(position).getSource());
        holder.textViewDestination.setText(listUsers.get(position).getDestination());
        holder.textViewAmount.setText(listUsers.get(position).getAmount());
        holder.textViewPassengerName.setText(listUsers.get(position).getPassengerName());
        holder.textViewLinetype.setText(listUsers.get(position).getLineType());
    }

    @Override
    public int getItemCount() {
        Log.v(BookingRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewSource;
        public AppCompatTextView textViewDestination;
        public AppCompatTextView textViewAmount;
        public AppCompatTextView textViewPassengerName;
        public AppCompatTextView textViewLinetype;

        public UserViewHolder(View view) {
            super(view);
            textViewSource = (AppCompatTextView) view.findViewById(R.id.textViewSource);
            textViewDestination = (AppCompatTextView) view.findViewById(R.id.textViewDestination);
            textViewAmount = (AppCompatTextView) view.findViewById(R.id.textViewAmount);
            textViewPassengerName = (AppCompatTextView) view.findViewById(R.id.textViewPassengerName);
            textViewLinetype = (AppCompatTextView) view.findViewById(R.id.textViewLinetype);
        }
    }


}
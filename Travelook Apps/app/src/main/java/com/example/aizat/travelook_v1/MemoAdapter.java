package com.example.aizat.travelook_v1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.aizat.travelook_v1.MemoModel.MemoAttr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private List<MemoAttr> memoAttrList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder{

        TextView memoTitle;
        TextView memoDate;

        public MemoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            memoTitle = (TextView) itemView.findViewById(R.id.memoTitle);
            memoDate = (TextView) itemView.findViewById(R.id.memoDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public MemoAdapter(List<MemoAttr> memo){
        memoAttrList = memo;
    }


    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_layout,viewGroup,false);
        MemoViewHolder memoViewHolder = new MemoViewHolder(view, mListener);
        return memoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder memoViewHolder, int i) {

        MemoAttr memo = memoAttrList.get(i);

        String date = memo.getDate().toString();
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy, HH:mm");
        try {
            Date date1 = dateFormat.parse(date);
            DateFormat dateFormat1 = new SimpleDateFormat("EEE, d MMM yyyy");
            String newDate = dateFormat1.format(date1);
            memoViewHolder.memoDate.setText(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        memoViewHolder.memoTitle.setText(memo.getTitle());


    }

    @Override
    public int getItemCount() {
        return memoAttrList.size();
    }

}

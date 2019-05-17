package com.example.aizat.travelook_v1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aizat.travelook_v1.BudgetModel.BudgetAttr;

import java.util.List;

public class BudgetRVAdapter extends RecyclerView.Adapter<BudgetRVAdapter.BudgetViewHolder> {

    private List<BudgetAttr> budgetAttrList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder{
         TextView destination, budget, budgetDate;

        public BudgetViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            destination = (TextView) itemView.findViewById(R.id.destination);
            budget = (TextView) itemView.findViewById(R.id.budget);
            budgetDate = (TextView) itemView.findViewById(R.id.budgetDate);

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

    public BudgetRVAdapter (List<BudgetAttr> budget){
        budgetAttrList = budget;
    }


    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.budget_layout,viewGroup,false);
        BudgetViewHolder budgetViewHolder = new BudgetViewHolder(view,mListener);
        return budgetViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull BudgetRVAdapter.BudgetViewHolder budgetViewHolder, int i) {

        BudgetAttr budgetAttr = budgetAttrList.get(i);

        budgetViewHolder.destination.setText(budgetAttr.getDestination());
        budgetViewHolder.budgetDate.setText(budgetAttr.getDate());
        budgetViewHolder.budget.setText(budgetAttr.getBudget());

    }

    @Override
    public int getItemCount() {
        return budgetAttrList.size();
    }
}

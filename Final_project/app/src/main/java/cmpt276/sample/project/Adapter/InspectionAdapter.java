package cmpt276.sample.project.Adapter;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cmpt276.sample.project.Model.Date;
import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.RestaurantManager;
/**
 * InspectionAdapter class is the helper class Recycler View for
 * the second screen display the list of inspections to the screen.
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */


import cmpt276.sample.project.R;

// https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class InspectionAdapter extends RecyclerView.Adapter<InspectionAdapter.InspectionViewHolder> {
    private List<Inspection> inspectionList;

    private OnItemClickListener inspectionListener;

    public InspectionAdapter(int restaurantPosition)
    {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        inspectionList = restaurantManager.getRestaurant(restaurantPosition).getInspections();
    }

    public static class InspectionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewForHazardLevel;
        TextView textViewDate;
        TextView textViewNumOfCritical;
        TextView textViewNumOfNonCritical;

        public InspectionViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewForHazardLevel = itemView.findViewById(R.id.imageView_Single_Restaurant_hazard_icon);
            textViewDate = itemView.findViewById(R.id.textView_Single_Restaurant_Date_Found);
            textViewNumOfCritical = itemView.findViewById(R.id.textView_Single_Restaurant_NumOfCritical_Found);
            textViewNumOfNonCritical = itemView.findViewById(R.id.textView_Single_Restaurant_NumOfNonCritical_Found);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int positionClicked = getAdapterPosition();
                        if (positionClicked != RecyclerView.NO_POSITION) {
                            listener.onItemClick(positionClicked);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public InspectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.inspection_item, parent, false);

        return new InspectionViewHolder(view, inspectionListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder holder, int position) {
        Inspection inspection = inspectionList.get(position);

        holder.textViewDate.setText (
                Date.DAY_MONTH_YEAR.getDateString(inspection.getInspectionDate())
        );
        holder.textViewNumOfCritical.setText (
                String.valueOf(inspection.getNumOfCritical())
        );
        holder.textViewNumOfNonCritical.setText (
                String.valueOf(inspection.getNumOfNonCritical())
        );

        switch (inspection.getHazardRating()) {
            case "Low" :
                holder.imageViewForHazardLevel.setImageResource(R.drawable.green_circle);
                break;
            case "Moderate" :
                holder.imageViewForHazardLevel.setImageResource(R.drawable.orange_circle);
                break;
            case "High" :
                holder.imageViewForHazardLevel.setImageResource(R.drawable.red_circle);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return inspectionList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        inspectionListener = listener;
    }

}


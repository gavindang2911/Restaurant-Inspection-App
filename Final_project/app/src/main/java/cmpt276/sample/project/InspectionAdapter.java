package cmpt276.sample.project;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.RestaurantManager;

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

    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder holder, int position) {
        Inspection inspection = inspectionList.get(position);

        holder.textViewDate.setText();
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}

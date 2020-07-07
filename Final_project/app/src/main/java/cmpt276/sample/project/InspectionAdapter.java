package cmpt276.sample.project;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cmpt276.sample.project.Model.Inspection;

// https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class InspectionAdapter extends RecyclerView.Adapter<InspectionAdapter.ViewHolder> {
    private List<Inspection> inspectionList;

    private OnItemClickListener inspectionListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}

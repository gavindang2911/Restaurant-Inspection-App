package cmpt276.sample.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import cmpt276.sample.project.R;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter{
    private final View mWindow;
    private Context mContext;

    public CustomInfoAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    private void redoWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView textTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            textTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView textSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            textSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        redoWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        redoWindowText(marker,mWindow);
        return mWindow;
    }
}

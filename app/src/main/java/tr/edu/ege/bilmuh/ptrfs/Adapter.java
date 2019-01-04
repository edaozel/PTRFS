package tr.edu.ege.bilmuh.ptrfs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Adapter extends RecyclerView.Adapter<Adapter.CardDesignHolder> {
    private Context mContext;
    private TreeMap<Double, Route> routeMap;
    private ArrayList<Station> stationList;

    public Adapter(Context mContext, TreeMap<Double, Route> routeMap, ArrayList<Station> stationList) {
        this.mContext = mContext;
        this.routeMap = routeMap;
        this.stationList = stationList;
    }

    @NonNull
    @Override
    public CardDesignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup,false);

        return new CardDesignHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardDesignHolder cardDesignHolder, final int i) {
        Map.Entry<Double, Route> route = (Map.Entry<Double, Route>) routeMap.entrySet().toArray()[i];
        String key = route.getValue().printAllSteps();
        cardDesignHolder.route = route.getValue();
        cardDesignHolder.satirYazi.setText("" + (i + 1) + ". " + key);
        cardDesignHolder.satirSure.setText(String.valueOf((int) route.getValue().getTotalDuration()) + " dk");

    }

    @Override
    public int getItemCount() {
        return routeMap.size();
    }

    public class CardDesignHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView satirCard;
        public TextView satirYazi;
        public TextView satirSure;
        public Route route;

        public CardDesignHolder(View view){
            super(view);
            satirCard = view.findViewById(R.id.satirCard);
            satirYazi = view.findViewById(R.id.satirYazi);
            satirSure = view.findViewById(R.id.satirSure);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent tempIntent = new Intent(mContext, RouteDetailActivity.class);
            tempIntent.putExtra("route", this.route);
            tempIntent.putExtra("stationList", stationList);
            mContext.startActivity(tempIntent);
        }
    }

}
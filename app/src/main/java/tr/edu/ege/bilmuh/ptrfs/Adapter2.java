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

public class Adapter2 extends RecyclerView.Adapter<Adapter2.CardDesignHolder> {
    private Context mContext;
    private List<String> routeList;

    public Adapter2(Context mContext, List<String> routeList) {
        this.mContext = mContext;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public CardDesignHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup,false);

        return new CardDesignHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardDesignHolder cardDesignHolder, final int i) {
        String route = routeList.get(i);
        cardDesignHolder.satirYazi.setText(route);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class CardDesignHolder extends RecyclerView.ViewHolder  {
        public CardView satirCard;
        public TextView satirYazi;
        public TextView satirSure;

        public CardDesignHolder(View view){
            super(view);
            satirCard = view.findViewById(R.id.satirCard);
            satirYazi = view.findViewById(R.id.satirYazi);
            satirSure = view.findViewById(R.id.satirSure);
        }

    }

}
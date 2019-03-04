package cl.uta.clubdeportivo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;
import cl.uta.clubdeportivo.listeners.ListItemClickListener;

public class ActDeportivasAdapter extends RecyclerView.Adapter<ActDeportivasAdapter.ViewHolder> {

    private final ArrayList<ActDeportivas> actDeportivasList;
    private Context context;
    private ListItemClickListener itemClickListener;


    public ActDeportivasAdapter(Context context, ArrayList<ActDeportivas> actDepList) {
        this.context = context;
        actDeportivasList = actDepList;
    }

    public ListItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_act_dep, parent, false);
        return new ViewHolder(view, viewType, itemClickListener);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgActDep;
        private TextView actDepTitle;
        private CardView mCardView;
        private ListItemClickListener itemClickListener;

        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            imgActDep = itemView.findViewById(R.id.act_img);
            actDepTitle = itemView.findViewById(R.id.act_title_text);
            mCardView = itemView.findViewById(R.id.card_view_actividades);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null ) {
                itemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }

    }

    @Override
    public void onBindViewHolder(ActDeportivasAdapter.ViewHolder mainHolder, int position) {
        final ActDeportivas model = actDeportivasList.get(position);

        String title = model.getTitleActDep().getRendered();
        mainHolder.actDepTitle.setText(Html.fromHtml(title));

        String imgUrl = null;
        if (model.getImagen()!= null) {
            imgUrl = model.getImagen().getGuid();
        }

        if (imgUrl != null ){
            Glide.with(context)
                    .load(imgUrl)
                    .into(mainHolder.imgActDep);
        } else {
            Glide.with(context)
                    .load(R.color.imgPlaceholder)
                    .into(mainHolder.imgActDep);
        }

    }

    @Override
    public int getItemCount() {
        return (null != actDeportivasList ? actDeportivasList.size(): 0);
    }
}

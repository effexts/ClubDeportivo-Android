package cl.uta.clubdeportivo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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

public class MisActDeportivasAdapter extends RecyclerView.Adapter<MisActDeportivasAdapter.ViewHolder> {
    private static final String TAG = "Club Deportivo";
    private final ArrayList<ActDeportivas> misActividadesDeportivasList;
    private Context context;
    private ListItemClickListener itemClickListener;

    public MisActDeportivasAdapter(ArrayList<ActDeportivas> misActividadesDeportivasList, Context context) {
        this.misActividadesDeportivasList = misActividadesDeportivasList;
        this.context = context;
    }

    public ArrayList<ActDeportivas> getMisActividadesDeportivasList() {
        return misActividadesDeportivasList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener {
        private ImageView imgMisActDep;
        private TextView tvMisActDepTitle;
        private CardView cardViewMisActDep;
        private ListItemClickListener listItemClickListener;

        public ViewHolder(View itemView, int viewType, ListItemClickListener listItemClickListener) {
            super(itemView);
            this.listItemClickListener = listItemClickListener;
            imgMisActDep = itemView.findViewById(R.id.act_img);
            tvMisActDepTitle = itemView.findViewById(R.id.act_title_text);
            cardViewMisActDep = itemView.findViewById(R.id.card_view_actividades);
            cardViewMisActDep.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: "+ v.getId());
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Eliminar Actividad")
                    .setMessage("¿Seguro que desea eliminar la actividad?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //apiinterface 
                            Log.d(TAG, "onClick: longclick + si");
                            Log.d(TAG, "onClick: eliminar esta act"+ misActividadesDeportivasList.get(getLayoutPosition()).getId());

                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                            Log.d(TAG, "onClick: no se borra nah");
                        }
                    })
                    .setIcon(R.drawable.ic_error)
                    .show();

            if (listItemClickListener != null ) {
                listItemClickListener.onItemClick(getLayoutPosition(), v);
            }
            return true;
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ActDeportivas model = misActividadesDeportivasList.get(position);
        String title = model.getTitleActDep().getRendered();
        holder.tvMisActDepTitle.setText(Html.fromHtml(title));

        String imgUrl = model.getImagen().getGuid();
        if (imgUrl!=null) {
            Glide.with(context)
                    .load(imgUrl)
                    .into(holder.imgMisActDep);
        } else {
            Glide.with(context)
                    .load(R.color.imgPlaceholder)
                    .into(holder.imgMisActDep);
        }

    }

    @Override
    public int getItemCount() {
        return ( null != misActividadesDeportivasList?misActividadesDeportivasList.size() : 0 );
    }

}

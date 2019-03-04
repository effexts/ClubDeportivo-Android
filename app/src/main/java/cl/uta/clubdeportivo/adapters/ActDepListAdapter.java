package cl.uta.clubdeportivo.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cl.uta.clubdeportivo.R;
import cl.uta.clubdeportivo.api.models.actividades.ActDeportivas;

public class ActDepListAdapter extends BaseAdapter {

    private List<ActDeportivas> data;
    private LayoutInflater layoutInflater;

    public ActDepListAdapter(Context context, List<ActDeportivas> data) {
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.nombreAct =  convertView.findViewById(R.id.nombreActDep);
            holder.id =  convertView.findViewById(R.id.idActDep);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nombreAct.setText(Html.fromHtml(data.get(position).getTitleActDep().getRendered()));
        holder.id.setText(data.get(position).getId().toString());

        return convertView;
    }
    static class ViewHolder {
        TextView nombreAct;
        TextView id;
    }
}

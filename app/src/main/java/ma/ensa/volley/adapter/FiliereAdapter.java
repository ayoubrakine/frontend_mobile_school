package ma.ensa.volley.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;


public class FiliereAdapter extends BaseAdapter {
    private List<Filiere> filieres;
    private LayoutInflater inflater;

    public FiliereAdapter(List<Filiere> filieres, Activity activity) {
        this.filieres = filieres;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filieres.size();
    }

    @Override
    public Object getItem(int position) {
        return filieres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filieres.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_filiere, null);

        TextView id = convertView.findViewById(R.id.id);


        TextView code = convertView.findViewById(R.id.code);
        TextView libelle = convertView.findViewById(R.id.libelle);



        Filiere filiere = filieres.get(position);

        id.setText(String.valueOf(filiere.getId()));
        libelle.setText(filiere.getLibelle());
        code.setText(filiere.getCode());


        return convertView;
    }
}

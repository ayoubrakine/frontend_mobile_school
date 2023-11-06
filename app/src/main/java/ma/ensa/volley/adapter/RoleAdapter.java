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
import ma.ensa.volley.beans.Role;


public class RoleAdapter extends BaseAdapter {
    private List<Role> roles;
    private LayoutInflater inflater;

    public RoleAdapter (List<Role> roles, Activity activity) {
        this.roles = roles;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return roles.size();
    }

    @Override
    public Object getItem(int position) {
        return roles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return roles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_role, null);

        TextView id = convertView.findViewById(R.id.id);


        TextView name = convertView.findViewById(R.id.name);



        Role role = roles.get(position);

        id.setText(String.valueOf(role.getId()));


        name.setText(role.getName());


        return convertView;
    }



}

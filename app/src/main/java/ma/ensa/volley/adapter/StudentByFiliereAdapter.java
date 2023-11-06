package ma.ensa.volley.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;

public class StudentByFiliereAdapter extends BaseAdapter {
    private List<Student> students;
    private LayoutInflater inflater;

    public StudentByFiliereAdapter(List<Student> students, Context context) {
        this.students = students;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return students.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_student, parent, false);
        }

        TextView id = convertView.findViewById(R.id.id);
        TextView username = convertView.findViewById(R.id.username);
        TextView name = convertView.findViewById(R.id.name);
        TextView email = convertView.findViewById(R.id.email);
        TextView phone = convertView.findViewById(R.id.phone);
        TextView filiere = convertView.findViewById(R.id.filiere);
        TextView role = convertView.findViewById(R.id.role);

        Student student = students.get(position);

        id.setText(String.valueOf(student.getId()));

        username.setText(student.getUsername());
        name.setText(student.getName());
        email.setText(student.getEmail());
        phone.setText(String.valueOf(student.getPhone()));

        filiere.setText(student.getFiliere().getLibelle());


        if (student.getRoles() != null && !student.getRoles().isEmpty()) {
            StringBuilder rolesText = new StringBuilder();
            for (Role rolee : student.getRoles()) {
                rolesText.append(rolee.getName()).append(", "); // Concaténez tous les rôles dans une chaîne
            }
            rolesText.setLength(rolesText.length() - 2); // Retirez la virgule et l'espace en trop à la fin

            Log.d("Roles_Debug", "Roles for Student " + student.getId() + ": " + rolesText.toString());

            role.setText(rolesText.toString());
        } else {
            Log.d("Roles_Debug", "No roles found for Student " + student.getId());
            role.setText("No role");
        }



        return convertView;
    }

}

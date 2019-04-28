package com.example.familytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAddapter extends RecyclerView.Adapter<MembersAddapter.MembersViewHolder> {

    ArrayList<CreateUser> namelist;
    Context c;

    MembersAddapter(ArrayList<CreateUser> namelist , Context c){
        this.namelist = namelist;
        this.c = c;
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(v,c,namelist);

        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        CreateUser createUser = namelist.get(position);
        holder.name_txt.setText(createUser.name);
        Picasso.get().load(createUser.imageUri).placeholder(R.drawable.ic_person_outline_black_24dp).into(holder.civ);
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name_txt;
        CircleImageView civ;
        View v;
        Context c;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;
        FirebaseUser user;

        public MembersViewHolder(View itemView, Context c, ArrayList<CreateUser> nameArrayList) {
            super(itemView);
            this.c = c;
            this.nameArrayList = nameArrayList;

            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name_txt = itemView.findViewById(R.id.item_title);
            civ = itemView.findViewById(R.id.circleimage);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(c,"You have clicked this user",Toast.LENGTH_SHORT).show();
        }
    }
}

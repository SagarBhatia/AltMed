package com.example.nikmul19.medicine;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.view.ViewGroup;

import com.example.nikmul19.medicine.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class MedicineAdapter  extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder>{
    private List <Medicine> medicineList;
    private DatabaseReference db;
    Context ctx;
    public MedicineAdapter(List<Medicine> medicineList, Context ctx){
        this.medicineList=medicineList;
        this.ctx=ctx;
        Log.e("cons","hi"+medicineList.size());
        db=FirebaseDatabase.getInstance().getReference().child("medicines");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name,price,manufacturer;
        private EditText qty;
        private Button altbtn;

        public MyViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.medicine_name);
            price=view.findViewById(R.id.medicine_price);
            manufacturer=view.findViewById(R.id.manufacturer);
            altbtn=view.findViewById(R.id.show);


        }


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  Medicine medicine= medicineList.get(position);
        holder.name.setText(medicine.getName());
        holder.manufacturer.setText(medicine.getManf());
        Log.e("error1",medicine.getName()+medicine.getPrice());
        holder.price.setText("Rs:"+medicine.getPrice());
        holder.altbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.e("List ", String.valueOf(dataSnapshot.child(medicine.getMid()).child("alternative").getChildrenCount()));
                        List<String> list=new ArrayList<>();
                        for(DataSnapshot ds:dataSnapshot.child(medicine.getMid()).child("alternative").getChildren())
                        {
                            String abc=ds.getValue(String.class);
                            list.add(abc);

//                            Log.e("data : ",abc);
                        }
                        DrawerActivity da=new DrawerActivity();
                        ((DrawerActivity) ctx).setFragment(1, 0,list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
//        holder.qty.setText("1");


    }
    @Override
    public int getItemCount(){


        Log.e("error","item-count:"+medicineList.size());
        return medicineList.size();
    }

}

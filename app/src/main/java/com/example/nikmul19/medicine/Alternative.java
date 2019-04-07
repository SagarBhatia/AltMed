//package com.example.nikmul19.medicine;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Alternative.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link Alternative#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Alternative extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public Alternative() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Alternative.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Alternative newInstance(String param1, String param2) {
//        Alternative fragment = new Alternative();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_alternative, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}

package com.example.nikmul19.medicine;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Alternative extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView.LayoutManager layoutManager;
    private AltAdapter adapter;

    private List<Medicine>MedicinesList= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recyler, container, false);

        final List<String> aletrenative = getArguments().getStringArrayList("data");
//        if ()
//        Log.e("recyclerfragment data",aletrenative.size()+"");
        recyclerView = view.findViewById(R.id.recycler);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("medicines");
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this.getActivity());
        // layoutManager.canScrollVertically();
        // layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AltAdapter(MedicinesList);

        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        ShapeDrawable shapeDrawable= new ShapeDrawable(new RectShape());
        int dividerThickness=5;
        shapeDrawable.setIntrinsicHeight(dividerThickness);
        shapeDrawable.setAlpha(0);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.border1));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //recyclerView.setAdapter(adapter);
        try{
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //MedicinesList.clear();
                    for (String s : aletrenative) {

                        String name1= (String) dataSnapshot.child(s).child("name").getValue();
                        String Manf=dataSnapshot.child(s).child("manufacturer").getValue(String.class);
                        Integer price1=dataSnapshot.child(s).child("price").getValue(Integer.class);
                        Log.e("db data ",name1);
                        Medicine medicine = new Medicine(name1, String.valueOf(price1),Manf,null);

                        MedicinesList.add(medicine);
                        adapter.notifyDataSetChanged();
                        Log.e("Sagar",String.valueOf(MedicinesList.size()));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e)
        {


        }
        /*TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(name);*/

        //TODO get students as per selected criteria

        return view;
    }








//    public void addMedicines(){
//
//        Medicine medicine;
//        db= FirebaseDatabase.getInstance().getReference().child("medicines");
//        db.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
//
//                    String name= childSnap.child("name").getValue(String.class);
//                    int price= childSnap.child("price").getValue(Integer.class);
//
//                    Medicine medicine1= new Medicine(name,String.valueOf(price));
//
//                   // Log.e("error","hi"+price);
//                    System.out.print(price);
//                    MedicinesList.add(medicine1);
//                    adapter.notifyDataSetChanged();
//                   System.out.print("hi"+MedicinesList.size());
//                    Log.e("error","Size"+MedicinesList.size());
//
//
//
//
//                    // Log.i("hichilds",name+price);
//                }
//                //medicine.putData();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        /*medicine = new Medicine("Crocin","07");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Repalol","120");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Metacine","12");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Emset","15");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Crocin","07");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Repalol","120");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Metacine","12");
//        MedicinesList.add(medicine);
//
//        medicine = new Medicine("Emset","15");
//        MedicinesList.add(medicine);
//        medicine = new Medicine("Crocin","07");
//        MedicinesList.add(medicine);*/
//
//

}


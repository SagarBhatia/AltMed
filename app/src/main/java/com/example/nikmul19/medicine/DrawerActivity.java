package com.example.nikmul19.medicine;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

import com.example.nikmul19.medicine.LoginSignUpContainer;
import com.example.nikmul19.medicine.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    Medicine medicine;


    private TextView headerTitle;
    private DatabaseReference db;

    Alternative recylerFragment;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        db= FirebaseDatabase.getInstance().getReference().child("medicines");

        medicine.putData();

        SearchManager searchManager = (SearchManager)DrawerActivity.this.getSystemService(Context.SEARCH_SERVICE);
        db= FirebaseDatabase.getInstance().getReference().child("medicines");
        final ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> manufacturer = new ArrayList<String>();
        final ArrayList<String> med_id = new ArrayList<String>();
        final ArrayList<Integer> prices = new ArrayList<Integer>();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  int flag = 0;
                  for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                      names.add(childSnap.child("name").getValue(String.class));
                      prices.add(childSnap.child("price").getValue(Integer.class));
                      manufacturer.add(childSnap.child("manufacturer").getValue(String.class));
                      med_id.add(childSnap.child("medicine_id").getValue(String.class));
                  }
              }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
          });

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search Medicines or Stores...");
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(DrawerActivity.this.getComponentName()));
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                //searchItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                int flag=0;
                final String str = s;
                final ArrayList<String> searchname = new ArrayList<String>();
                final ArrayList<String> search_manf = new ArrayList<String>();
                final ArrayList<String> search_mid = new ArrayList<String>();
                final ArrayList<Integer> searchprice = new ArrayList<Integer>();
                        for (int i=0;i<names.size();i++) {
                            String[] name1=names.get(i).split(" ");
                            name1[0]=name1[0].toLowerCase();
                            if(name1[0].contains(str)) {
                                flag=1;
                                searchname.add(names.get(i));
                                searchprice.add(prices.get(i));
                                search_manf.add(manufacturer.get(i));
                                search_mid.add(med_id.get(i));
                            }
                        }
                        if(flag==1)
                        {
                            fragmentManager= getSupportFragmentManager();
                            RecylerFragment fragment= new RecylerFragment();
                            fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container,fragment,null).commit();

                            drawerLayout.closeDrawers();
                            Bundle bundle = new Bundle();

                            bundle.putIntegerArrayList("price",searchprice);
                            bundle.putStringArrayList("name",searchname);
                            bundle.putStringArrayList("manf",search_manf);
                            bundle.putStringArrayList("med_id",search_mid);

                            fragment.setArguments(bundle);

                        }






                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        medicine= new Medicine();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        fragmentManager=getSupportFragmentManager();
        setHomeFragment();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_white_24dp);
        if (headerTitle==null){
            Log.i("test","null");
        }
        //VolleyResponseclass volleyResponseclass=new VolleyResponseclass(DrawerActivity.this);
        //volleyResponseclass.getData();
        View header=navigationView.getHeaderView(0);
        headerTitle=(TextView) (header.findViewById(R.id.nav_header_title));
        headerTitle.setText("Hi, "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        headerTitle.setTypeface(null, Typeface.ITALIC);





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                System.out.println("id"+id);
                drawerLayout.closeDrawers();

                switch (id)
                {
                    case R.id.medicine_search:
                        Intent intent= new Intent(DrawerActivity.this,RecyclerActivity.class);
                        //startActivity(intent);
                        fragmentManager= getSupportFragmentManager();
                        RecylerFragment fragment= new RecylerFragment();
                        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container,fragment,null).commit();

                        drawerLayout.closeDrawers();
                        break;






                    case R.id.logout_menu:
                        drawerLayout.closeDrawers();

                        DrawerActivity.this.finish();
                        FirebaseAuth.getInstance().signOut();

                        intent= new Intent(DrawerActivity.this,LoginSignUpContainer.class);
                        startActivity(intent);

                        break;

                    case R.id.feedback_provide:

                        break;


                    case R.id.drawer_home:
                        DrawerHomeFragment homeFragment= new DrawerHomeFragment();
                        drawerLayout.closeDrawers();

                        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container,homeFragment,null).addToBackStack(null).commit();
                        break;

                }


                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;


        }
        return super.onOptionsItemSelected(item);

    }
    public void setHomeFragment(){
        DrawerHomeFragment fragment= new DrawerHomeFragment();
        fragmentManager.beginTransaction().replace(R.id.drawer_fragments_container,fragment,null).commit();
    }

    @SuppressLint("SetTextI18n")
    public void setFragment(int item, int position, List<String> aletrnative) {
        recylerFragment=null;
        switch (item) {
            case 1:
                recylerFragment = new Alternative();
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("data", (ArrayList<String>) aletrnative);
                recylerFragment.setArguments(bundle);
                pushFragment(recylerFragment, true);

                break;


        }
    }
    public void pushFragment(android.support.v4.app.Fragment fragment, boolean add) {
        FragmentTransaction transation = getSupportFragmentManager()
                .beginTransaction();
        transation.replace(R.id.drawer_fragments_container, fragment);
        transation.commit();
    }

}


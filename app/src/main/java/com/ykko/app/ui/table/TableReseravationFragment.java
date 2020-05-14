package com.ykko.app.ui.table;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.materialspinner.MaterialSpinner;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ykko.app.R;
import com.ykko.app.data.Global_Variable;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.data.model.Order;
import com.ykko.app.ui.fragments.DatePickerFragment;
import com.ykko.app.ui.fragments.TimePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableReseravationFragment extends Fragment implements DatePickerFragment.OnDateListener, TimePickerFragment.OnTimeListener {

    private TableReseravationViewModel tableReseravationViewModel;
    FirebaseDatabase database;
    private List<FoodMenu> foodMenuPosts = new ArrayList<>();
    private ArrayList foods = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    public List<Integer> viewid = new ArrayList<>();

    private Order newOrder = new Order();
    String branch = "";
    String date = "";
    String time = "";
    String township = "";
    String foodOne = "";
    String foodTwo = "";
    JSONObject foodmenu = new JSONObject();


    MaterialSpinner foodOneSpinner;
    MaterialSpinner foodTwoSpinner;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        tableReseravationViewModel =
                ViewModelProviders.of(this).get(TableReseravationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_table_reservation, container, false);
        final TextInputEditText nameEditText = root.findViewById(R.id.reserve_name);
        final TextInputEditText phNoEditText = root.findViewById(R.id.reserve_phNo);
        SharedPreferences sp =getActivity().getSharedPreferences("FILE_NAME", 0);
        SharedPreferences.Editor edit = sp.edit();

        final String Username = sp.getString("key","NON");
        final String Userphone =sp.getString("phone","NON");
        nameEditText.setText(Username);
        phNoEditText.setText(Userphone);
        final TextInputEditText numberOfPersonsEditText = root.findViewById(R.id.noOfPersons);
        final TextInputEditText foodDesEditText = root.findViewById(R.id.food_des);
        foodOneSpinner = root.findViewById(R.id.food1_spinner);
        foodOneSpinner.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodOne = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foodTwoSpinner = root.findViewById(R.id.food2_spinner);
        foodTwoSpinner.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodTwo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        database = FirebaseDatabase.getInstance();


        DatabaseReference menuPostsRef = database.getReference("menuPosts").child("posts");

        ValueEventListener menuPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                keys.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    FoodMenu post = keyNode.getValue(FoodMenu.class);
                    foodMenuPosts.add(post);
                }
                for (FoodMenu post : foodMenuPosts) {
                    foods.add(post.foodStickName);
                }

                foodOneSpinner.setLabel("Food1");

                ArrayAdapter<String> foodOneAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foods);
                // Specify the layout to use when the list of choices appears
                foodOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodOneSpinner.setAdapter(foodOneAdapter);

                foodTwoSpinner.setLabel("Food2");
                ArrayAdapter<String> foodTwoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foods);
                // Specify the layout to use when the list of choices appears
                foodTwoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodTwoSpinner.setAdapter(foodTwoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("", "loadPost:onCancelled", databaseError.toException());
            }
        };
        menuPostsRef.addValueEventListener(menuPostListener);

        MaterialSpinner townshipSpinner = root.findViewById(R.id.township_spinner);
        townshipSpinner.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                township = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        townshipSpinner.setLabel("Township");
        ArrayAdapter<CharSequence> townshipAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.townships_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        townshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        townshipSpinner.setAdapter(townshipAdapter);

        MaterialSpinner branchesSpinner = root.findViewById(R.id.branches_spinner);
        branchesSpinner.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        branchesSpinner.setLabel("Branch");
        ArrayAdapter<CharSequence> branchesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.branches_array, android.R.layout.simple_spinner_item);
        branchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchesSpinner.setAdapter(branchesAdapter);


        Button datePickerBtn = root.findViewById(R.id.datePickerBtn);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        Button addnewfoodbtn = root.findViewById(R.id.add_new_food_btn);
        addnewfoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodAdd(inflater, container, v);
            }
        });
        Button timePickerBtn = root.findViewById(R.id.timePickerBtn);
        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        Button cancelbtn = root.findViewById(R.id.cancel_table_btn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        Button reserveTableBtn = root.findViewById(R.id.reserve_table_btn);
        reserveTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //string datetime = datePickerBtn.
                newOrder.name = nameEditText.getText().toString();
                newOrder.phNo = phNoEditText.getText().toString();
                //newOrder.date = "20/Mar/2020 - 12:00 PM";
                newOrder.date = date + " " + time;
                newOrder.branch = branch;
                newOrder.township = township;
                newOrder.numberOfPersons = Integer.valueOf(numberOfPersonsEditText.getText().toString());
                String s = "";
                StringBuilder SB = new StringBuilder();
                SB.append(foodOne).append(System.getProperty("line.separator"));
                if(foodmenu.length()!=0){
                    for (int i=0;i<foodmenu.length();i++){

                        try {
                            String data = viewid.get(i).toString();
                            SB.append(foodmenu.getString(String.valueOf(data))).append(System.getProperty("line.separator"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    s =SB.toString();
                }
                newOrder.food1 = s;
                //newOrder.food2 = foodTwo;
                newOrder.description = foodDesEditText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putParcelable("newOrderKey", newOrder);
                Navigation.findNavController(v).navigate(R.id.nav_confirm_order, bundle);
            }
        });

        return root;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
        //        DatePickerDialog dialog = (DatePickerDialog) newFragment.getDialog();
//        DatePicker picker = dialog.getDatePicker();
//        date = picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "timePicker");

    }

    @Override
    public void OnDateSubmit(String Selected_Date) {
        date = Selected_Date;
        Button btnFragment2 = (Button) getView().findViewById(R.id.datePickerBtn);
        btnFragment2.setText(date);

    }

    @Override
    public void OnTimeSubmit(String Selected_Time) {
        time = Selected_Time;
        Button btnFragment3 = (Button) getView().findViewById(R.id.timePickerBtn);
        btnFragment3.setText(time);
    }

    int foodnumber = 2;

    public void FoodAdd(@NonNull LayoutInflater inflater,
                        ViewGroup container, View v) {

        final View material_sp = inflater.inflate(R.layout.material_custom_spinner, container, false);

        // final MaterialSpinner material_sp = (MaterialSpinner) material_sp_view.findViewById(R.id.spinner);
        final Spinner material_sp_spinner = material_sp.findViewById(R.id.spinner);
//        String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner = (MaterialSpinner) findViewById(R.id.spinner);
//        spinner.setAdapter(adapter);

        material_sp.setId(View.generateViewId());
        TextView materiallbl = material_sp.findViewById(R.id.label);

        String foodlabel = Integer.toString(foodnumber);
        materiallbl.setText("Food " + foodlabel);

        material_sp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String food = parent.getItemAtPosition(position).toString();
                try {

                    if (foodmenu.has(Integer.toString(material_sp.getId()))) {
                        //get Value of video
                        foodmenu.put(Integer.toString(material_sp.getId()), food);
                    } else {
                        foodmenu.accumulate(Integer.toString(material_sp.getId()), food);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> materialfoodadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foods);
        // Specify the layout to use when the list of choices appears
        materialfoodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        material_sp_spinner.setAdapter(materialfoodadapter);

        final Button deletebtn = new Button(getContext());
        deletebtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deletebtn.setText("Delete" + materiallbl.getText());
        deletebtn.setId(View.generateViewId());


        final LinearLayout LL = getActivity().findViewById(R.id.more_food_menu);
        viewid.add(material_sp.getId());
        //LinearLayout linearLayout = getActivity().findViewById(R.id.infoLayout);
        LL.addView(material_sp);
        LL.addView(deletebtn);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LL.removeView(material_sp);
                LL.removeView(deletebtn);
                foodmenu.remove(Integer.toString(material_sp.getId()));
                int index = viewid.indexOf(material_sp.getId());
                viewid.remove(index);
            }
        });
        foodnumber++;
    }
}

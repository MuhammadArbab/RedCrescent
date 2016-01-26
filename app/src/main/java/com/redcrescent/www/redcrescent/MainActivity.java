package com.redcrescent.www.redcrescent;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private LinearLayout mainLayout;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String currentSex = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                int height = 0;
                for (int i = 0; i < expListView.getChildCount(); i++) {
                    height += expListView.getChildAt(i).getMeasuredHeight();
                    height += expListView.getDividerHeight();
                }
                expListView.getLayoutParams().height = (height+6)*3;

                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                expListView.getLayoutParams().height = 100;

                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                //mainLayout.getLayoutParams().height = (50+6)*4;

                if (childPosition == 0){

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.contact_us, null);

                    TextView termsTextView = (TextView) inflatedLayout.findViewById(R.id.agreeText);
                    termsTextView.append("IMPORTANT! Please check this box to confirm that you understand and excpet ");
                    termsTextView.append(getText(R.string.terms_of_use));
                    termsTextView.append(" and ");
                    termsTextView.append(getText(R.string.privacy_policy));
                    termsTextView.setMovementMethod(LinkMovementMethod.getInstance());

                    TextView contactText = (TextView) inflatedLayout.findViewById(R.id.contactNumberText);
                    contactText.append("If you need to contact a specific department - Call our main Line at ");
                    contactText.append(getText(R.string.contactNumber));
                    contactText.append("and ask for the following extensions :");

                    // Spinner element
                    Spinner spinner = (Spinner) inflatedLayout.findViewById(R.id.spinner);

                    // Spinner click listener
                    spinner.setOnItemSelectedListener(MainActivity.this);

                    // Spinner Drop down elements
                    List<String> categories = new ArrayList<String>();
                    categories.add("Feedback");
                    categories.add("Inquiry");
                    categories.add("General");

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);

                    mainLayout.addView(inflatedLayout);

                }else if (childPosition == 1)
                {
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.getappointed, null);

                    // Spinner element
                    Spinner spinnerDate = (Spinner) inflatedLayout.findViewById(R.id.spinnerDate);
                    Spinner spinnerMonth = (Spinner) inflatedLayout.findViewById(R.id.spinnerMonth);
                    Spinner spinnerYear = (Spinner) inflatedLayout.findViewById(R.id.spinnerYear);

                    // Spinner click listener
                    spinnerDate.setOnItemSelectedListener(MainActivity.this);
                    spinnerMonth.setOnItemSelectedListener(MainActivity.this);

                    // Spinner Drop down elements
                    List<String> categories = new ArrayList<String>();
                    categories.add("1");
                    categories.add("2");
                    categories.add("3");
                    categories.add("4");
                    categories.add("5");
                    categories.add("5");

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinnerDate.setAdapter(dataAdapter);
                    spinnerMonth.setAdapter(dataAdapter);
                    spinnerYear.setAdapter(dataAdapter);

                    mainLayout.addView(inflatedLayout);
                }else if (childPosition == 2){

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.finddoctor, null);

                    GridView grid = (GridView) inflatedLayout.findViewById(R.id.gridView);

                    final String[] letters = new String[] {
                            "A", "B", "C", "D", "E",
                            "F", "G", "H", "I", "J",
                            "K", "L", "M", "N", "O",
                            "P", "Q", "R", "S", "T",
                            "U", "V", "W", "X", "Y", "Z"};


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.gridviewitem, letters);

                    grid.setAdapter(adapter);

                    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Toast.makeText(getApplicationContext(),
                                    ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Spinner spinnerSpeciality = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpeciality);
                    Spinner spinnerLanguage = (Spinner) inflatedLayout.findViewById(R.id.spinnerLanguage);

                    // Spinner click listener
                    spinnerSpeciality.setOnItemSelectedListener(MainActivity.this);
                    spinnerLanguage.setOnItemSelectedListener(MainActivity.this);

                    // Spinner Drop down elements
                    List<String> categories = new ArrayList<String>();
                    categories.add("1");
                    categories.add("2");
                    categories.add("3");
                    categories.add("4");
                    categories.add("5");
                    categories.add("5");

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spinnerLanguage.setAdapter(dataAdapter);
                    spinnerSpeciality.setAdapter(dataAdapter);

                    mainLayout.addView(inflatedLayout);
                    //mainLayout.setMinimumHeight(inflatedLayout.getMeasuredHeight());
                }

                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Menu");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void addListenerOnButton() {
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);

        currentSex =  radioSexButton.getText().toString();

        Toast.makeText(MainActivity.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();

    }

    public void gridItemClickEvent(View v) {
        // does something very interesting
        Toast.makeText(getApplicationContext(),
                ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }
}
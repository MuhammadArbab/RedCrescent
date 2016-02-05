package com.redcrescent.www.redcrescent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    String genderString = "1";

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private LinearLayout mainLayout;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private RadioGroup radioTimeGroup;
    private RadioButton radioTimeButton;

    private String currentSex = "";
    private ProgressDialog progress;
    String contactSubject = "";

    String dayString = "";
    String monthString = "";
    String yearString = "";
    String dateString = "";

    String selectedSex = "";
    String selectedTime = "";
    String smsStatus = "";

    static final String[] Months = new String[] { "Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};

    ArrayList<LanguageModal> languages;
    ArrayList<String> langList;
    ArrayList<String> specialistList;
    Spinner spinnerLanguage;
    Spinner spinnerGender;
    private String lng_id = "";


    ArrayList<SpecialityModal> specialities;
    ArrayList<String> specialityList;
    Spinner spinnerSpeciality;
    Spinner spinnerSpecialiest;
    private String speciality_id = "";

    ArrayList<DoctorModal> doctors;
    ArrayList<String> doctorsList;
    private CustomAdapter adapter;
    ListView doctorsListView;

    private String specialiestId = "";

    private String urlString = "http://www.wellnessvisit.com/red-crescent/doregisterappointment.php?"
            + "email=" + "send2arbab@gmail.com"
            + "&password=" + "12345"
            + "&fullname=" + "ArbabMuhammad"
            + "&day=" + "2"
            + "&month=" + "4"
            + "&year=" + "1990"
            + "&gender=" + "1"
            + "&address=" + ""
            + "&address1=" + ""
            + "&country=" + "Pakistan"
            + "&city=" + "karachi"
            + "&state=" + "test state"
            + "&pcode=" + "12345"
            + "&pcnum=" + "03444444444"
            + "&specialist=" + "3"
            + "&apdate=" + "30-12-2015"
            + "&aptime=" + "1"
            + "&reason=" + "testing reason"
            + "&sms=" + "1";

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

        expListView.setSelectedChild(0,0,true);
        expListView.expandGroup(0, false);


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
                expListView.getLayoutParams().height = (height+6)*5;

//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                int height = 0;
                for (int i = 0; i < expListView.getChildCount(); i++) {
                    height += expListView.getChildAt(i).getMeasuredHeight();
                    height += expListView.getDividerHeight();
                }
                expListView.getLayoutParams().height = -(height+6)*3;

//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();

                mainLayout.removeAllViews();
                expListView.collapseGroup(0);

                if (childPosition == 0){

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.home, null);

                    mainLayout.addView(inflatedLayout);

                }else if (childPosition == 1)
                {
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.getappointed, null);
                    //radioSexGroup = (CheckBox) inflatedLayout.findViewById(R.id.checkBoxSMS);
                    radioSexGroup = (RadioGroup) inflatedLayout.findViewById(R.id.radioSex);
                    radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {


                            //selectedSex = checkedId;
                        }
                    });
                    radioTimeGroup = (RadioGroup) inflatedLayout.findViewById(R.id.radioTime);
                    radioTimeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            //selectedTime = checkedId;
                        }
                    });

                    final EditText email = (EditText) inflatedLayout.findViewById(R.id.input_Email);
                    final EditText password = (EditText) inflatedLayout.findViewById(R.id.input_Pasword);
                    final EditText name = (EditText) inflatedLayout.findViewById(R.id.input_Name);
                    final EditText address = (EditText) inflatedLayout.findViewById(R.id.input_Address);
                    final EditText address1 = (EditText) inflatedLayout.findViewById(R.id.input_Address1);
                    final EditText phoneNumber = (EditText) inflatedLayout.findViewById(R.id.input_PhoneNumber);
                    final LinearLayout parentView = (LinearLayout) inflatedLayout.findViewById(R.id.subFeildsParent);

                    final EditText country = (EditText) inflatedLayout.findViewById(R.id.input_CountryName);
                    final EditText city = (EditText) inflatedLayout.findViewById(R.id.input_City);
                    final EditText state = (EditText) inflatedLayout.findViewById(R.id.input_State);
                    final EditText zip = (EditText) inflatedLayout.findViewById(R.id.input_ZipCode);

                    final EditText message = (EditText) inflatedLayout.findViewById(R.id.input_AppointmentReason);

                    phoneNumber.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (country.getText().toString().equalsIgnoreCase("malaysia")){
                                parentView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            }
                            return false;
                        }
                    });

                    spinnerSpecialiest = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpecialiest);
                    spinnerSpeciality = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpeciality);

                    if ( isOnline() ){

                        new HttpAsyncTaskForSpecialitiesForAppointment().execute("http://www.wellnessvisit.com/red-crescent/get-all-specialties.php");

                    }else {

                        Toast.makeText(MainActivity.this, "Not connected to internet.",
                                Toast.LENGTH_SHORT).show();

                    }

                    Button submitButton =  (Button) inflatedLayout.findViewById(R.id.submit);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // call AsynTask to perform network operation on separate thread
                            String dayString = "";
                            String monthString = "";
                            String yearString = "";

                            if ( isValidEmail(email.getText())  ){

                                if ( name.getText().length() > 0 ){

                                    if ( message.getText().length() > 0 ){

                                        String contactUrll = "http://www.wellnessvisit.com/red-crescent/doregisterappointment.php?"
                                                + "email=" + email.getText().toString()
                                                + "&password=" + password.getText().toString()
                                                + "&fullname=" + name.getText().toString()
                                                + "&day=" + dayString
                                                + "&month=" + monthString
                                                + "&year=" + yearString
                                                + "&gender=" + selectedSex
                                                + "&address=" + address.getText().toString()
                                                + "&address1=" + address1.getText().toString()
                                                + "&country=" + country.getText().toString()
                                                + "&city=" + city.getText().toString()
                                                + "&state=" + state.getText().toString()
                                                + "&pcode=" + zip.getText().toString()
                                                + "&pcnum=" + phoneNumber.getText().toString()
                                                + "&specialist=" + specialiestId
                                                + "&apdate=" + dateString
                                                + "&aptime=" + selectedTime
                                                + "&reason=" + message.getText().toString()
                                                + "&sms=" + smsStatus;

                                        if ( isOnline() ){

                                            new HttpAsyncTask().execute(contactUrll);
                                        }else {

                                            Toast.makeText(MainActivity.this, "Not connected to internet.",
                                                    Toast.LENGTH_SHORT).show();
                                        }


                                    }else{

                                        Toast.makeText(MainActivity.this, "Please input a reason for appointment", Toast.LENGTH_LONG).show();
                                    }

                                }else {

                                    Toast.makeText(MainActivity.this, "Please input your name", Toast.LENGTH_LONG).show();
                                }
                            }else {

                                Toast.makeText(MainActivity.this, "Email entered does not match or not a valid one..", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    // Spinner element
                    // Set days
                    final ArrayList<String> days = new ArrayList<String>();
                    for (int i = 1; i <= 31; i++) {
                        days.add(Integer.toString(i));
                    }
                    ArrayAdapter<String> adapterDays = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, days);
                    Spinner spinnerDate = (Spinner) inflatedLayout.findViewById(R.id.spinnerDate);
                    spinnerDate.setAdapter(adapterDays);

                    // Set months
                    ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_item, Months);
                    adapterMonths.setDropDownViewResource(R.layout.spinner_item);

                    Spinner spinnerMonth = (Spinner) inflatedLayout.findViewById(R.id.spinnerMonth);
                    spinnerMonth.setAdapter(adapterMonths);

                    // Set years
                    final ArrayList<String> years = new ArrayList<String>();
                    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int i = 1900; i <= thisYear; i++) {
                        years.add(Integer.toString(i));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, years);

                    Spinner spinnerYear = (Spinner) inflatedLayout.findViewById(R.id.spinnerYear);
                    spinnerYear.setAdapter(adapter);

                    // Spinner click listener
                    spinnerDate
                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {
                                    // TODO Auto-generated method stub
                                    dayString = days.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });

                    spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            monthString = Months[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                    spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            yearString = years.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

                    final EditText setDate = (EditText) inflatedLayout.findViewById(R.id.date_setter);
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            Calendar myCal = Calendar.getInstance();

                            myCal.set(Calendar.YEAR, year);
                            myCal.set(Calendar.MONTH, monthOfYear);
                            myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "MM-dd-yy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                            setDate.setText(sdf.format(myCal.getTime()));

                            dateString = sdf.format(myCal.getTime()).toString();
                        }
                    };

                    setDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DatePickerDialog(MainActivity.this, date, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    setDate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    mainLayout.addView(inflatedLayout);
                }else if (childPosition == 2){
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View inflatedLayout= inflater.inflate(R.layout.finddoctor, null);

                    spinnerSpeciality  = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpeciality);

                    if ( isOnline() ){

                        new HttpAsyncTaskForLanguage().execute("http://www.wellnessvisit.com/red-crescent/get-all-languages.php");
                        new HttpAsyncTaskForSpecialities().execute("http://www.wellnessvisit.com/red-crescent/get-all-specialties.php");

                    }else {

                        Toast.makeText(MainActivity.this, "Not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    }

                    doctorsListView = (ListView) inflatedLayout.findViewById(R.id.doctorsList);

                    Button showAllDoctorsBtn = (Button) inflatedLayout.findViewById(R.id.showAllBtn);
                    showAllDoctorsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if ( isOnline() ){

                                new HttpAsyncTaskForDoctorsResult().execute("http://www.wellnessvisit.com/red-crescent/get-search-doctor-all.php");
                            }else {

                                Toast.makeText(MainActivity.this, "Not connected to internet.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    Button searchDoctorButton = (Button) inflatedLayout.findViewById(R.id.searchBtn);
                    searchDoctorButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            EditText searchDoctor = (EditText) inflatedLayout.findViewById(R.id.doctorText);
                            String url = "http://www.wellnessvisit.com/red-crescent/get-search-doctor.php?docName=" + searchDoctor.getText() + "&sp_id=" + speciality_id + "&lng_id=" + lng_id + "&gender=" + genderString;

                            if ( isOnline() ){

                                new HttpAsyncTaskForDoctorsResult().execute(url);

                            }else {

                                Toast.makeText(MainActivity.this, "Not connected to internet.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if ( isOnline() ){
                                new HttpAsyncTaskForDoctorsResult().execute("http://www.wellnessvisit.com/red-crescent/get-alphabet-search.php?skey=" + letters[position]);
                            }else {
                                Toast.makeText(MainActivity.this, "Not connected to internet.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    spinnerSpeciality = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpeciality);
                    spinnerLanguage = (Spinner) inflatedLayout.findViewById(R.id.spinnerLanguage);

                    final ArrayList<String> genderList = new ArrayList<String>();
                    genderList.add("Male");
                    genderList.add("Female");

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, genderList);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spinnerGender = (Spinner) inflatedLayout.findViewById(R.id.spinnerGender);

                    spinnerGender.setAdapter(dataAdapter);

                    spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (genderList.get(position).equalsIgnoreCase("Male")){
                                genderString = "1";
                            }else {
                                genderString = "2";
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    mainLayout.addView(inflatedLayout);
                }else if (childPosition == 3){

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View inflatedLayout= inflater.inflate(R.layout.contact_us, null);

                    WebView webview = (WebView) inflatedLayout.findViewById(R.id.webView1);
                    webview.setWebViewClient(new WebViewClient());
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.loadUrl("https://www.google.com/maps/place/Karachi,+Pakistan/data=!4m2!3m1!1s0x3eb33e06651d4bbf:0x9cf92f44555a0c23?sa=X&ved=0ahUKEwjah5qcotfKAhUGkY4KHYg4BlkQ8gEIHDAA");

                    final EditText name = (EditText) inflatedLayout.findViewById(R.id.input_Name);
                    final EditText email = (EditText) inflatedLayout.findViewById(R.id.input_Email);
                    final EditText confirmEmail = (EditText) inflatedLayout.findViewById(R.id.input_Confirm_Email);
                    final EditText phoneNumber = (EditText) inflatedLayout.findViewById(R.id.input_Contact_tNumber);
                    final EditText message = (EditText) inflatedLayout.findViewById(R.id.input_Message);

                    final CheckBox acceptedAgreement = (CheckBox) inflatedLayout.findViewById(R.id.checkBox);

                    Button submitButton =  (Button) inflatedLayout.findViewById(R.id.submit);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // call AsynTask to perform network operation on separate thread

                            if (confirmEmail.getText().toString().equalsIgnoreCase(email.getText().toString()) &&
                                    isValidEmail(email.getText())){

                                if ( acceptedAgreement.isChecked()){

                                    if ( isOnline() ){
                                        String url = "http://www.wellnessvisit.com/red-crescent/docontactus.php?yname=" + name.getText().toString() + "&email=" + email.getText().toString() + "&cn=" + phoneNumber.getText().toString() + "&subject=" + contactSubject +"&msg=" + message.getText().toString();
                                        new HttpAsyncTask().execute(url);
                                    }else {

                                        Toast.makeText(MainActivity.this, "Not connected to internet.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(MainActivity.this, "Please check that you accpet terma and conditions", Toast.LENGTH_LONG).show();
                                }
                            }else {

                                Toast.makeText(MainActivity.this, "Email entered does not match or not a valid one..", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

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
                    final Spinner spinner = (Spinner) inflatedLayout.findViewById(R.id.spinner);

                    // Spinner click listener
                    spinner
                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int position, long arg3) {
                                    // TODO Auto-generated method stub
                                    contactSubject = spinner.getSelectedItem().toString();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });

                    // Spinner Drop down elements
                    List<String> categories = new ArrayList<String>();
                    categories.add("Feedback");
                    categories.add("Inquiry");
                    categories.add("General");

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.leftspinneritem, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(R.layout.leftspinneritem);

                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);

                    mainLayout.addView(inflatedLayout);
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
        top250.add("Home");
        top250.add("Get Appointment");
        top250.add("Find Doctor");
        top250.add("Contact Us");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void addListenerOnButton() {
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        currentSex =  radioSexButton.getText().toString();
//Toast.makeText(MainActivity.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();

    }

    public void gridItemClickEvent(View v) {
        // does something very interesting

        if ( isOnline() ){

            TextView temp = (TextView) v;
            Log.e("",""+temp.getText().toString());
            new HttpAsyncTaskForDoctorsResult().execute("http://www.wellnessvisit.com/red-crescent/get-alphabet-search.php?skey=" + temp.getText().toString());
//        Toast.makeText(getApplicationContext(),
//                ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(MainActivity.this, "Not connected to internet.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class emailVerificationResult {
        public String statusNbr;
        public String hygieneResult;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON(urls[0],10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            String responceMessage = "";
            try {
                JSONObject  jsonRootObject = new JSONObject(result);
                responceMessage = jsonRootObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),
                    (responceMessage), Toast.LENGTH_SHORT).show();

        }
    }

    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private class HttpAsyncTaskForSpecialities extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON("http://www.wellnessvisit.com/red-crescent/get-all-specialties.php",10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            specialities = new ArrayList<SpecialityModal>();
            specialityList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    SpecialityModal speciality__ = new SpecialityModal();
                    speciality__.setId(jsonobject.optString("id"));
                    speciality__.setSpeciality(jsonobject.optString("value"));

                    specialities.add(speciality__);

                    // Populate spinner with country names
                    specialityList.add(jsonobject.optString("value"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Spinner adapter
            spinnerSpeciality
                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_bigger_item,
                            specialityList));

            // Spinner on item click listener
            spinnerSpeciality
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub

                            speciality_id = specialities.get(position).getId();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }

    private class HttpAsyncTaskForSpecialitiesForAppointment extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON("http://www.wellnessvisit.com/red-crescent/get-all-specialties.php",10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            specialities = new ArrayList<SpecialityModal>();
            specialityList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    SpecialityModal speciality__ = new SpecialityModal();
                    speciality__.setId(jsonobject.optString("id"));
                    speciality__.setSpeciality(jsonobject.optString("value"));

                    specialities.add(speciality__);

                    // Populate spinner with country names
                    specialityList.add(jsonobject.optString("value"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Spinner adapter
            spinnerSpeciality
                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_bigger_item,
                            specialityList));

            // Spinner on item click listener
            spinnerSpeciality
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub

                            speciality_id = specialities.get(position).getId();
                            new HttpAsyncTaskForSepcielistDoctorsResult().execute("http://www.wellnessvisit.com/red-crescent/get-doctor-by-speciality.php?sp_id=" + speciality_id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }

    private class HttpAsyncTaskForLanguage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            //progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON("http://www.wellnessvisit.com/red-crescent/get-all-languages.php",10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            String responceMessage = "";
            languages = new ArrayList<LanguageModal>();
            langList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    LanguageModal language = new LanguageModal();
                    language.setId(jsonobject.optString("id"));
                    language.setLanguage(jsonobject.optString("value"));

                    languages.add(language);

                    // Populate spinner with country names
                    langList.add(jsonobject.optString("value"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Spinner adapter
            spinnerLanguage
                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinner_bigger_item,
                            langList));

            // Spinner on item click listener
            spinnerLanguage
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub

                            lng_id = languages.get(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });

        }
    }

    private class HttpAsyncTaskForGridDoctorsResult extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON(urls[0],10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            String responceMessage = "";

            doctors = new ArrayList<DoctorModal>();
            //langList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    DoctorModal doctor = new DoctorModal();
                    doctor.setdoctorName(jsonobject.optString("DocName"));
                    doctor.seteducationss(jsonobject.optString("specialities"));
                    doctor.setSpecialities(jsonobject.optString("educations"));
                    doctor.setimgSrc(jsonobject.optString("imgSrc"));

                    doctors.add(doctor);

                    specialistList.add(jsonobject.optString("DocName"));
                    // Populate spinner with country names
                    //doctorsList.add(jsonobject.optString("value"));
                }

                // Spinner adapter
                spinnerSpecialiest
                        .setAdapter(new ArrayAdapter<String>(MainActivity.this,
                                R.layout.spinner_bigger_item,
                                specialistList));

                // Spinner on item click listener
                spinnerSpecialiest
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0,
                                                       View arg1, int position, long arg3) {
                                // TODO Auto-generated method stub
                                specialiestId = doctors.get(position).getdoctorName();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                adapter = new CustomAdapter(MainActivity.this, doctors);
                doctorsListView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(doctorsListView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpAsyncTaskForDoctorsResult extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON(urls[0], 10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            String responceMessage = "";

            doctors = new ArrayList<DoctorModal>();
            //langList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    DoctorModal doctor = new DoctorModal();
                    doctor.setdoctorName(jsonobject.optString("DocName"));
                    doctor.seteducationss(jsonobject.optString("specialities"));
                    doctor.setSpecialities(jsonobject.optString("educations"));
                    doctor.setimgSrc(jsonobject.optString("imgSrc"));

                    doctors.add(doctor);

                    // Populate spinner with country names
                    //doctorsList.add(jsonobject.optString("value"));
                }

                adapter = new CustomAdapter(MainActivity.this, doctors);
                doctorsListView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(doctorsListView);

            } catch (JSONException e) {
                String abc = e.getMessage();

                if (abc.contains("not found"))
                {
                    Toast.makeText(MainActivity.this,
                            "No record found",
                            Toast.LENGTH_SHORT).show();

                }
                e.printStackTrace();
            }
        }
    }
    private class HttpAsyncTaskForSepcielistDoctorsResult extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = getJSON(urls[0], 10000);
            return  data;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();
            String responceMessage = "";

            doctors = new ArrayList<DoctorModal>();
            doctorsList = new ArrayList<String>();

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    DoctorModal doctor = new DoctorModal();
                    doctor.setdoctorName(jsonobject.optString("doctorFName") + jsonobject.optString("doctorLName"));
                    doctor.seteducationss(jsonobject.optString("specialities"));
                    doctor.setSpecialities(jsonobject.optString("educations"));
                    doctor.setimgSrc(jsonobject.optString("imgSrc"));

                    doctors.add(doctor);

                    // Populate spinner with country names
                    doctorsList.add(jsonobject.optString("doctorFName") + jsonobject.optString("doctorLName"));
                }

                if ( !result.equals("[{\"\":\"\"}]\n") ){

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_bigger_item, doctorsList);

                    spinnerSpecialiest.setAdapter(adapter);

                    spinnerSpecialiest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }


//                adapter = new CustomAdapter(MainActivity.this, doctors);
//                doctorsListView.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(doctorsListView);

            } catch (JSONException e) {
                String abc = e.getMessage();

                if (abc.contains("not found"))
                {
                    Toast.makeText(MainActivity.this,
                            "No record found",
                            Toast.LENGTH_SHORT).show();

                }
                e.printStackTrace();
            }
        }
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())) + 1000;
        listView.setLayoutParams(params);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm  =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

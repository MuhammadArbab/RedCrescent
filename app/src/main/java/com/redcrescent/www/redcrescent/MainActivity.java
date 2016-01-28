package com.redcrescent.www.redcrescent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private LinearLayout mainLayout;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String currentSex = "";
    private ProgressDialog progress;

    ArrayList<LanguageModal> languages;
    ArrayList<String> langList;
    Spinner spinnerLanguage;
    private String lng_id = "";


    ArrayList<SpecialityModal> specialities;
    ArrayList<String> specialityList;
    Spinner spinnerSpeciality;
    private String speciality_id = "";


    ArrayList<DoctorModal> doctors;
    ArrayList<String> doctorsList;
    private CustomAdapter adapter;
    ListView doctorsListView;


    private String urlString = "http://www.wellnessvisit.com/red-crescent/doregisterappointment.php?"
            + "email=" + "send2arbab@gmail.com"
            + "&password=" + "12345"
            + "&fullname=" + "ArbabMuhammad"
            + "&day=" + "2"
            + "&month=" + "4"
            + "&year=" + "1990"
            + "&gender=" + 1
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

                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
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

                    Button submitButton =  (Button) inflatedLayout.findViewById(R.id.submit);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // call AsynTask to perform network operation on separate thread
                            new HttpAsyncTask().execute(urlString);


                            //http://www.wellnessvisit.com/red-crescent/doregisterappointment.php?email=mrashid.bsse@gmail.com&password=123&fullname=Rashid&day=05&month=01&year=1988&gender=1&address=Future%20colon&address1=Karchi&country=Malaysia&city=karachi&state=1&pcode=72150&pcnum=0333562634&specialist=3&apdate=30-12-2015&aptime=1&reason=testing&sms=1
                        }
                    });

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

                    new HttpAsyncTaskForLanguage().execute("http://www.wellnessvisit.com/red-crescent/get-all-languages.php");
                    new HttpAsyncTaskForSpecialities().execute("http://www.wellnessvisit.com/red-crescent/get-all-specialties.php");

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View inflatedLayout= inflater.inflate(R.layout.finddoctor, null);

                    doctorsListView = (ListView) inflatedLayout.findViewById(R.id.doctorsList);


                    Button showAllDoctorsBtn = (Button) inflatedLayout.findViewById(R.id.showAllBtn);

                    showAllDoctorsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new HttpAsyncTaskForDoctorsResult().execute("http://www.wellnessvisit.com/red-crescent/get-search-doctor-all.php");



                        }
                    });

                    Button searchDoctorButton = (Button) inflatedLayout.findViewById(R.id.searchBtn);

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

                    spinnerSpeciality = (Spinner) inflatedLayout.findViewById(R.id.spinnerSpeciality);
                    spinnerLanguage = (Spinner) inflatedLayout.findViewById(R.id.spinnerLanguage);

                    // Spinner click listener
                    //spinnerSpeciality.setOnItemSelectedListener(MainActivity.this);
                    //spinnerLanguage.setOnItemSelectedListener(MainActivity.this);

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
                    //spinnerLanguage.setAdapter(dataAdapter);
                    //spinnerSpeciality.setAdapter(dataAdapter);

                    mainLayout.addView(inflatedLayout);
                    //mainLayout.setMinimumHeight(inflatedLayout.getMeasuredHeight());
                }else if (childPosition == 3){

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
            String data = getJSON(urlString,10000);
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
                            android.R.layout.simple_spinner_dropdown_item,
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

    private class HttpAsyncTaskForLanguage extends AsyncTask<String, Void, String> {

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
                            android.R.layout.simple_spinner_dropdown_item,
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

                    // Populate spinner with country names
                    //doctorsList.add(jsonobject.optString("value"));
                }

                adapter = new CustomAdapter(MainActivity.this, doctors);
                doctorsListView.setAdapter(adapter);

                setListViewHeightBasedOnChildren(doctorsListView);

            } catch (JSONException e) {
                e.printStackTrace();
            }



//            // Spinner adapter
//            spinnerLanguage
//                    .setAdapter(new ArrayAdapter<String>(MainActivity.this,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            langList));
//
//            // Spinner on item click listener
//            spinnerLanguage
//                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                        @Override
//                        public void onItemSelected(AdapterView<?> arg0,
//                                                   View arg1, int position, long arg3) {
//                            // TODO Auto-generated method stub
//
//                            lng_id = languages.get(position).getId();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> arg0) {
//                            // TODO Auto-generated method stub
//                        }
//                    });

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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

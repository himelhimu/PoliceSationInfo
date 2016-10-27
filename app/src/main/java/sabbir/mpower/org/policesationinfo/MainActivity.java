package sabbir.mpower.org.policesationinfo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sabbir.mpower.org.policesationinfo.model.PoliceStation;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public Spinner devisionSpinner;
    public Spinner districtSpinner;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        devisionSpinner=(Spinner)findViewById(R.id.spinnerDiv);
        //districtSpinner=(Spinner) findViewById(R.id.spinnerDistrict);
        listView=(ListView) findViewById(R.id.listView);

        ArrayAdapter<CharSequence> divAdapter = ArrayAdapter.createFromResource(this,
                R.array.devision_array, android.R.layout.simple_spinner_item);

        divAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        devisionSpinner.setAdapter(divAdapter);
        devisionSpinner.setOnItemSelectedListener(MainActivity.this);







    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*switch (position)
        {
            case 0:
                break;
            case 1:
                loadJSONFromAsset();
                break;
        }*/
       loadJSONFromAsset();
        switch (position){
            case 0:
                listView.smoothScrollToPosition(1);
                break;
            case 1:
                listView.smoothScrollToPosition(230);
                break;
            case 2:
                listView.smoothScrollToPosition(300);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("allstations.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

            loadJson(json);
            Log.d("LOG","GOT JSON"+json.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
           // return null;
        }
       return json;

    }


    public void loadJson(String json)
    {
        List<PoliceStation> policeStationsList;

        try {
            JSONObject jsonObject=new JSONObject(json);
            JSONArray jsonArray=jsonObject.getJSONArray("stations");
            PoliceStation policeStation=null;
            policeStationsList=new ArrayList<>();
            Gson gson=new Gson();






            for (int i=0;i<jsonArray.length();i++)

            {
                //policeStation=new PoliceStation();
                policeStation=gson.fromJson(jsonArray.getJSONObject(i).toString(),PoliceStation.class);
                policeStationsList.add(policeStation);

            }


            StationAdapter stationAdapter=new StationAdapter(getApplicationContext(),R.layout.row_item,policeStationsList);
            listView.setAdapter(stationAdapter);
            stationAdapter.notifyDataSetChanged();
            Log.d("LOG","GOT THE ARRAY"+policeStation.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class StationAdapter extends ArrayAdapter

             { private LayoutInflater layoutInflater;
                 List<PoliceStation> stationList;
                 private int resource;


                 public StationAdapter(Context context, int resource, List objects) {
                     super(context, resource, objects);
                     this.resource=resource;
                     stationList=objects;

                     layoutInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                 }

                 @NonNull
                 @Override
                 public View getView(int position, View convertView, ViewGroup parent) {
                     //View view=convertView;

                     ViewHolder holder=null;
                     if (convertView ==null) {

                          holder = new ViewHolder();

                         convertView=layoutInflater.inflate(resource,null);

                        holder.tvSerial=(TextView)convertView.findViewById(R.id.tvSerial);
                         holder.tvMobile = (TextView) convertView.findViewById(R.id.tvMobile);
                         holder.tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
                         holder.tvThana=(TextView) convertView.findViewById(R.id.tvThana);
                         convertView.setTag(holder);
                     }else {

                         holder=(ViewHolder) convertView.getTag();
                     }


                     holder.tvThana.setTextColor(Color.RED);
                     holder.tvPosition.setTextColor(Color.GREEN);
                     holder.tvMobile.setTextColor(Color.BLUE);

                    holder.tvSerial.setText(stationList.get(position).getSerialNo());
                     holder.tvMobile.setText(stationList.get(position).getMobileNo());
                     holder.tvPosition.setText(stationList.get(position).getPosition());
                     holder.tvThana.setText(stationList.get(position).getThana());

                     return convertView;
                 }
             }

     class ViewHolder{
         private TextView tvThana,tvPosition,tvMobile,tvSerial;

    }
}

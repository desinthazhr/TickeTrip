package pbm.projects.TickeTrip.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pbm.projects.TickeTrip.R;
import pbm.projects.TickeTrip.adapter.HotelAdapter;
import pbm.projects.TickeTrip.database.DatabaseHelper;
import pbm.projects.TickeTrip.model.HotelModel;
import pbm.projects.TickeTrip.session.SessionManager;

import java.util.ArrayList;

public class FindHotelActivity extends AppCompatActivity {
    protected Cursor cursor;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private SessionManager session;
    private String id_book = "", nama = "", lokasi, harga;
    private int gambar;
    private TextView tvNotFound;
    private String getTanggal, getDewasa, getAnak, getKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotel);

        dbHelper = new DatabaseHelper(FindHotelActivity.this);
        db = dbHelper.getReadableDatabase();

        tvNotFound = findViewById(R.id.notFound);

        refreshList();
    }

    public void refreshList() {
        final ArrayList<HotelModel> hasil = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String searchHotel;
        if(extras == null) {
            searchHotel= null;
        } else {
            searchHotel= extras.getString("hasil");
            getTanggal = extras.getString("checkin")+ "-" + extras.getString("checkout");
            getAnak = extras.getString("anak");
            getDewasa = extras.getString("dewasa");
            getKamar =extras.getString("kamar");
        }
        cursor = db.rawQuery("SELECT * FROM TB_HOTEL WHERE HOTEL_LOKASI = '" + searchHotel + "'", null);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            id_book = cursor.getString(0);
            nama = cursor.getString(1);
            lokasi = cursor.getString(5) + ", " + cursor.getString(2);
            harga = cursor.getString(3);

            String imageName = cursor.getString(4);
            int resID = getResources().getIdentifier(imageName, "drawable", "pbm.projects.TickeTrip");
            gambar = resID;

            hasil.add(new HotelModel(id_book, nama, lokasi, harga, gambar));
        }

        ListView listBook = findViewById(R.id.list_hotel);
        HotelAdapter arrayAdapter = new HotelAdapter(this, hasil);
        listBook.setAdapter(arrayAdapter);

        //pilih data
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String selection = hasil.get(i).getNama();
                Intent intent = new Intent(FindHotelActivity.this, RoomActivity.class);
                intent.putExtra("Hotel", selection);
                intent.putExtra("Durasi", getTanggal);
                intent.putExtra("Anak", getAnak);
                intent.putExtra("Dewasa", getDewasa);
                intent.putExtra("Kamar", getKamar);
                startActivity(intent);
            }
        });

        if (nama.equals("")) {
            tvNotFound.setVisibility(View.VISIBLE);
            listBook.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.GONE);
            listBook.setVisibility(View.VISIBLE);
        }
    }
}
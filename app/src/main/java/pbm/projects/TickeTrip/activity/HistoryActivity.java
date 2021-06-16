package pbm.projects.TickeTrip.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pbm.projects.TickeTrip.R;
import pbm.projects.TickeTrip.model.HistoryModel;
import pbm.projects.TickeTrip.adapter.HistoryAdapter;
import pbm.projects.TickeTrip.database.DatabaseHelper;
import pbm.projects.TickeTrip.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    protected Cursor cursor, cs;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    SessionManager session;
    String id_book = "", asal, tujuan, tanggal, dewasa, anak, riwayat, total, nama_hotel, id_hotel;
    String email;
    TextView tvNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        tvNotFound = findViewById(R.id.noHistory);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        email = user.get(SessionManager.KEY_EMAIL);

        refreshList();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbHistory);
        toolbar.setTitle("Riwayat Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshList() {
        final ArrayList<HistoryModel> hasil = new ArrayList<>();
        cursor = db.rawQuery("SELECT * FROM TB_BOOK, TB_HARGA WHERE TB_BOOK.id_book = TB_HARGA.id_book AND username='" + email + "'", null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            id_hotel = cursor.getString(3);
            if(id_hotel != null) {
                cs = db.rawQuery("SELECT * FROM TB_HOTEL WHERE TB_HOTEL.id_hotel ='" + id_hotel + "'", null);
                cs.moveToFirst();
                id_book = cursor.getString(0);
                tanggal = cursor.getString(4);
                total = cursor.getString(11);
                nama_hotel = cs.getString(1);

                riwayat = "Berhasil melakukan booking kamar di " + nama_hotel + " dengan durasi " + tanggal + ". ";
                String[] tgl = tanggal.split("-");
                tanggal = tgl[0];
            }
            else {
                id_book = cursor.getString(0);
                asal = cursor.getString(1);
                tujuan = cursor.getString(2);
                tanggal = cursor.getString(4);
                dewasa = cursor.getString(5);
                anak = cursor.getString(6);
                total = cursor.getString(11);
                riwayat = "Berhasil melakukan booking untuk melakukan perjalanan dari " + asal + " menuju " + tujuan + " pada tanggal " + tanggal + ". " +
                        "Jumlah pembelian tiket dewasa sejumlah " + dewasa + " dan tiket anak-anak sejumlah " + anak + ".";
            }

            hasil.add(new HistoryModel(id_book, tanggal, riwayat, total, R.drawable.profile));
        }

        ListView listBook = findViewById(R.id.list_booking);
        HistoryAdapter arrayAdapter = new HistoryAdapter(this, hasil);
        listBook.setAdapter(arrayAdapter);

        //delete data
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String selection = hasil.get(i).getIdBook();
                final CharSequence[] dialogitem = {"Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try {
                            db.execSQL("DELETE FROM TB_BOOK where id_book = " + selection + "");
                            id_book = "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refreshList();
                    }
                });
                builder.create().show();
            }
        });

        if (id_book.equals("")) {
            tvNotFound.setVisibility(View.VISIBLE);
            listBook.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.GONE);
            listBook.setVisibility(View.VISIBLE);
        }

    }
}

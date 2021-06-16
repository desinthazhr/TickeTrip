package pbm.projects.TickeTrip.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import pbm.projects.TickeTrip.R;
import pbm.projects.TickeTrip.database.DatabaseHelper;
import pbm.projects.TickeTrip.session.SessionManager;

import java.util.HashMap;


public class RoomActivity extends AppCompatActivity {
    protected Cursor cursor;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String sNama, sLokasi, sHarga, idHotel = "";
    private int image, id_book, hargaTotal;
    private String durasi, anak, dewasa, kamar;
    String email;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_hotel);

        dbHelper = new DatabaseHelper(RoomActivity.this);
        db = dbHelper.getReadableDatabase();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        Bundle extras = getIntent().getExtras();
        String namaHotel;
        if(extras == null) {
            namaHotel= null;
            durasi= null;
        } else {
            namaHotel= extras.getString("Hotel");
            durasi = extras.getString("Durasi");
            dewasa = extras.getString("Dewasa");
            anak = extras.getString("Anak");
            kamar = extras.getString("Kamar");
        }
        cursor = db.rawQuery("SELECT * FROM TB_HOTEL WHERE HOTEL_NAMA = '" + namaHotel + "'", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            idHotel = cursor.getString(0);
            sNama = cursor.getString(1);
            sLokasi = cursor.getString(5) + ", " + cursor.getString(2);
            sHarga = cursor.getString(3);

            String imageName = cursor.getString(4);
            int resID = getResources().getIdentifier(imageName, "drawable", "package.name");

            image = resID;
        }
        TextView hotelName = findViewById(R.id.detail_nama_hotel);
        TextView hotelLokasi = findViewById(R.id.detail_lokasi_hotel);
        TextView hotelHarga = findViewById(R.id.detail_harga_sewa);

        ImageView hotelGambar = findViewById(R.id.detail_gambar_hotel);

        hotelName.setText(sNama);
        hotelLokasi.setText(sLokasi);
        hotelHarga.setText("Rp." + sHarga);
        hotelGambar.setImageResource(image);

        Button btnstd = findViewById(R.id.pesanstd);
        Button btndlx = findViewById(R.id.pesandlx);

        final TextView kamarstd = findViewById(R.id.kamar_std);
        final TextView kamardlx = findViewById(R.id.kamar_dlx);

        final String[] jenisKamar = {""};
        btnstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenisKamar[0] = kamarstd.getText().toString();
                pesanHotel(jenisKamar[0]);
            }
        });

        btndlx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenisKamar[0] = kamardlx.getText().toString();
                pesanHotel(jenisKamar[0]);
            }
        });

    }

    public void pesanHotel(String kamar) {
        db = dbHelper.getWritableDatabase();
        hitungHarga();
        AlertDialog dialog = new AlertDialog.Builder(RoomActivity.this)
                .setTitle("Ingin booking Hotel sekarang?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.execSQL("INSERT INTO TB_BOOK (id_hotel, tanggal, dewasa, anak) VALUES ('" +
                                    idHotel + "','" +
                                    durasi + "','" +
                                    dewasa + "','" +
                                    anak + "');");
                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                            cursor.moveToLast();
                            if (cursor.getCount() > 0) {
                                cursor.moveToPosition(0);
                                id_book = cursor.getInt(0);
                            }
                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_total) VALUES ('" +
                                    email + "','" +
                                    id_book + "','" +
                                    hargaTotal + "');");
                            Toast.makeText(RoomActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(RoomActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", null)
                .create();
        dialog.show();

    }

    public void hitungHarga() {
        int hargaPerMalam = Integer.valueOf(sHarga);
        int jumlahKamar = Integer.valueOf(kamar);

        String[] duration = durasi.split("-");

        String checkin = duration[0].substring(0,1);
        String checkout = duration[1].substring(0,1);

        int tglchekin = Integer.parseInt(checkin);
        int tglchekout = Integer.parseInt(checkout);

        int total_hari = (tglchekout - tglchekin);
        if(total_hari == 0) {
            total_hari = 1;
        }

        hargaTotal = (hargaPerMalam * jumlahKamar) * total_hari;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package pbm.projects.TickeTrip.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pbm.projects.TickeTrip.R;
import pbm.projects.TickeTrip.database.DatabaseHelper;
import pbm.projects.TickeTrip.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class BookHotelActivity extends AppCompatActivity {
    protected Cursor cursor;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Spinner spinLokasi, spinDewasa, spinAnak, spinKamar;
    private SessionManager session;
    private String email;
    private int id_book;
    private EditText checkin, checkout;
    public String sLokasi, sTanggalCheckin, sTanggalCheckout, sDewasa, sAnak, sKamar;
    private DatePickerDialog dpTanggalCheckin, dpTanggalCheckout;
    private Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hotel);

        dbHelper = new DatabaseHelper(BookHotelActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] dewasa = {"0", "1", "2", "3", "4", "5"};
        final String[] anak = {"0", "1", "2", "3", "4", "5"};
        final String[] kamar = {"0", "1", "2", "3", "4", "5"};

        spinLokasi = findViewById(R.id.lokasi);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);
        spinKamar = findViewById(R.id.kamar);

        cursor = db.rawQuery("SELECT DISTINCT HOTEL_LOKASI FROM TB_HOTEL", null);
        cursor.moveToFirst();
        String[] result = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
             result[i] = cursor.getString(0);
        }

        ArrayAdapter<CharSequence> adapterLokasi = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, result);
        adapterLokasi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLokasi.setAdapter(adapterLokasi);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        ArrayAdapter<CharSequence> adapterKamar = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, kamar);
        adapterKamar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinKamar.setAdapter(adapterKamar);

        spinLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sLokasi = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinKamar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sKamar = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        checkin = findViewById(R.id.tanggal_checkin);
        checkin.setInputType(InputType.TYPE_NULL);
        checkin.requestFocus();

        checkout = findViewById(R.id.tanggal_checkout);
        checkout.setInputType(InputType.TYPE_NULL);
        checkout.requestFocus();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        Button btnCari = findViewById(R.id.cari);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sLokasi != null && sTanggalCheckin != null && sTanggalCheckout != null && !sDewasa.equals("0") && !sKamar.equals("0")) {
                    Intent i = new Intent(BookHotelActivity.this, FindHotelActivity.class);
                    i.putExtra("hasil", sLokasi);
                    i.putExtra("checkin", sTanggalCheckin);
                    i.putExtra("checkout", sTanggalCheckout);
                    i.putExtra("dewasa", sDewasa);
                    i.putExtra("anak", sAnak);
                    i.putExtra("kamar", sKamar);
                    startActivity(i);
                }
                else {
                    Toast.makeText(BookHotelActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbKrl);
        toolbar.setTitle("Form Cari");
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

    private void setDateTimeField() {
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggalCheckin.show();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggalCheckout.show();
            }
        });

        dpTanggalCheckin = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggalCheckin = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                checkin.setText(sTanggalCheckin);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dpTanggalCheckout = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggalCheckout = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                checkout.setText(sTanggalCheckout);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}

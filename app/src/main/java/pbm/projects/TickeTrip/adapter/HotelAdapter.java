package pbm.projects.TickeTrip.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import pbm.projects.TickeTrip.R;
import pbm.projects.TickeTrip.model.HotelModel;

import java.util.ArrayList;

public class HotelAdapter extends ArrayAdapter<HotelModel> {
    public HotelAdapter(Activity context, ArrayList<HotelModel> notification) {
        super(context, 0, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_hotel, parent, false);
        }

        HotelModel current = getItem(position);

        TextView namaHotel = listItemView.findViewById(R.id.nama_hotel);
        namaHotel.setText(current.getNama());

        ImageView gambarHotel = listItemView.findViewById(R.id.gambar_hotel);
        gambarHotel.setImageResource(current.getGambar());

        TextView lokasiHotel = listItemView.findViewById(R.id.lokasi_hotel);
        lokasiHotel.setText(current.getLokasi());

        TextView harga = listItemView.findViewById(R.id.harga_kamar);
        harga.setText("Rp. " + current.getHarga());

        return listItemView;
    }
}

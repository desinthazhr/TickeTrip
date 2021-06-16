package pbm.projects.TickeTrip.model;

public class HotelModel {

    private String mIdBook, mNama, mLokasi, mHarga;
    private int gambar;

    public HotelModel(String idBook, String nama, String lokasi, String harga, int path) {
        mIdBook = idBook;
        mNama = nama;
        mLokasi = lokasi;
        mHarga = harga;
        gambar = path;

    }

    public String getIdBook() {
        return mIdBook;
    }

    public String getNama() { return mNama; }

    public String getLokasi() { return mLokasi; }

    public String getHarga() { return  mHarga; }

    public int getGambar() {
        return gambar;
    }
}


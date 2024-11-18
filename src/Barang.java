
public class Barang {

    private String nama;
    private String jenis;
    private int harga;
    private int stok;
    private String lokasiPenyimpanan;
    private int id; 
    
    public Barang() { 
    }

    // Constructor
    public Barang(int id, String nama, String jenis, int harga, int stok, String lokasiPenyimpanan) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.harga = harga;
        this.stok = stok;
        this.lokasiPenyimpanan = lokasiPenyimpanan;
    }

    // Getter dan Setter untuk ID dan field lainnya
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        if (harga >= 0) {
            this.harga = harga;
        } else {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok >= 0) {
            this.stok = stok;
        } else {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }
    }

    public String getLokasiPenyimpanan() {
        return lokasiPenyimpanan;
    }

    public void setLokasiPenyimpanan(String lokasiPenyimpanan) {
        this.lokasiPenyimpanan = lokasiPenyimpanan;
    }
}

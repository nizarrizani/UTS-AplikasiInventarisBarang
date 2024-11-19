
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarangDatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:barang.db"; // Lokasi database SQLite
    private static Connection connection = null;

    // Inisialisasi koneksi ke database (jika belum ada koneksi)
    private static void initializeDatabase() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(DB_URL);  // Menghubungkan ke database
            createTableIfNotExists();  // Memastikan tabel sudah ada
        }
    }

    // Pastikan tabel Barang sudah dibuat jika belum ada
    private static void createTableIfNotExists() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS barang (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL,
                jenis TEXT NOT NULL,
                harga INTEGER NOT NULL,
                stok INTEGER NOT NULL,
                lokasi_penyimpanan TEXT NOT NULL
            );
        """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);  // Menjalankan perintah SQL untuk membuat tabel
        }
    }

    // Tambah Barang (C) ke dalam database
    public static void tambahBarang(Barang barang) throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String insertSQL = "INSERT INTO barang (nama, jenis, harga, stok, lokasi_penyimpanan) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, barang.getNama());
            preparedStatement.setString(2, barang.getJenis());
            preparedStatement.setInt(3, barang.getHarga());
            preparedStatement.setInt(4, barang.getStok());
            preparedStatement.setString(5, barang.getLokasiPenyimpanan());
            preparedStatement.executeUpdate();  // Eksekusi perintah SQL untuk memasukkan data
        }
    }

    // Baca Barang (R) berdasarkan ID
    public static Optional<Barang> bacaBarang(int id) throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String selectSQL = "SELECT * FROM barang WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Barang barang = new Barang(
                            id,
                            resultSet.getString("nama"),
                            resultSet.getString("jenis"),
                            resultSet.getInt("harga"),
                            resultSet.getInt("stok"),
                            resultSet.getString("lokasi_penyimpanan")
                    );
                    return Optional.of(barang);
                }
            }
        }
        return Optional.empty();  // Jika tidak ada barang dengan ID tersebut
    }

    // Update Barang (U) berdasarkan ID
    public static void updateBarang(Barang barang) throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String updateSQL = """
            UPDATE barang
            SET nama = ?, jenis = ?, harga = ?, stok = ?, lokasi_penyimpanan = ?
            WHERE id = ?
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, barang.getNama());
            preparedStatement.setString(2, barang.getJenis());
            preparedStatement.setInt(3, barang.getHarga());
            preparedStatement.setInt(4, barang.getStok());
            preparedStatement.setString(5, barang.getLokasiPenyimpanan());
            preparedStatement.setInt(6, barang.getId());
            preparedStatement.executeUpdate();  // Eksekusi perintah update
        }
    }

    // Hapus Barang (D) berdasarkan ID
    public static void hapusBarang(Barang barang) throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String deleteSQL = "DELETE FROM barang WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, barang.getId());
            preparedStatement.executeUpdate();  // Eksekusi perintah hapus
        }
    }

    public static void simpanAtauPerbaruiBarang(Barang barang) throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String insertOrReplaceSQL = """
        INSERT OR REPLACE INTO barang (id, nama, jenis, harga, stok, lokasi_penyimpanan)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrReplaceSQL)) {
            preparedStatement.setInt(1, barang.getId());
            preparedStatement.setString(2, barang.getNama());
            preparedStatement.setString(3, barang.getJenis());
            preparedStatement.setInt(4, barang.getHarga());
            preparedStatement.setInt(5, barang.getStok());
            preparedStatement.setString(6, barang.getLokasiPenyimpanan());
            preparedStatement.executeUpdate();  // Eksekusi perintah insert or replace
        } catch (SQLException e) {
            // Menangani error jika ada masalah saat eksekusi query
            throw new SQLException("Gagal menyimpan atau memperbarui barang: " + e.getMessage(), e);
        }
    }

    // Pastikan koneksi terinisialisasi, jika belum maka otomatis inisialisasi
    private static void ensureConnection() throws SQLException {
        if (connection == null) {
            initializeDatabase();  // Inisialisasi koneksi jika belum ada
        }
    }

    // Dapatkan semua barang dari database
    public static List<Barang> bacaSemuaBarang() throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        String selectSQL = "SELECT * FROM barang";
        List<Barang> daftarBarang = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Barang barang = new Barang(
                        id,
                        resultSet.getString("nama"),
                        resultSet.getString("jenis"),
                        resultSet.getInt("harga"),
                        resultSet.getInt("stok"),
                        resultSet.getString("lokasi_penyimpanan")
                );
                daftarBarang.add(barang);  // Menambahkan barang ke dalam daftar
            }
        }
        return daftarBarang;
    }

    // Ambil list jenis barang yang ada di database
    public static List<String> getJenisList() throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        List<String> jenisList = new ArrayList<>();
        String query = "SELECT DISTINCT jenis FROM barang";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                jenisList.add(rs.getString("jenis"));
            }
        }
        return jenisList;
    }

    // Ambil list lokasi penyimpanan yang ada di database
    public static List<String> getLokasiList() throws SQLException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        List<String> lokasiList = new ArrayList<>();
        String query = "SELECT DISTINCT lokasi_penyimpanan FROM barang";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lokasiList.add(rs.getString("lokasi_penyimpanan"));
            }
        }
        return lokasiList;
    }

    public static void exportToCSV(String filePath) throws SQLException, IOException {
        ensureConnection();  // Pastikan koneksi sudah terinisialisasi
        List<Barang> daftarBarang = bacaSemuaBarang();  // Mengambil semua barang dari database

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Menulis header CSV
            writer.write("ID,Nama,Jenis,Harga,Stok,Lokasi_Penyimpanan");
            writer.newLine();

            // Menulis data barang ke dalam CSV
            for (Barang barang : daftarBarang) {
                writer.write(barang.getId() + ","
                        + barang.getNama() + ","
                        + barang.getJenis() + ","
                        + barang.getHarga() + ","
                        + barang.getStok() + ","
                        + barang.getLokasiPenyimpanan());
                writer.newLine();
            }
        }
    }

    public static void importBarangFromCSV(String filePath) throws SQLException, IOException {
        List<Barang> barangList = new ArrayList<>();  // Menyimpan barang yang valid

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header CSV
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 6) {
                    throw new IllegalArgumentException("Format baris salah: " + line);  // Lempar error jika format salah
                }

                // Mengambil data dari CSV
                int id = Integer.parseInt(data[0].trim());  // Ambil ID dari kolom pertama
                String nama = data[1].trim();
                String jenis = data[2].trim();
                int harga;
                int stok;
                try {
                    harga = Integer.parseInt(data[3].trim());
                    stok = Integer.parseInt(data[4].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Format angka salah: " + line, e);  // Lempar error jika format angka salah
                }

                // Validasi harga dan stok tidak negatif
                if (harga < 0 || stok < 0) {
                    throw new IllegalArgumentException("Harga atau stok tidak valid untuk barang: " + nama);  // Lempar error jika harga/stok negatif
                }

                // Menambahkan barang ke dalam list jika valid
                Barang barang = new Barang(id, nama, jenis, harga, stok, data[5].trim());
                barangList.add(barang);  // Simpan barang yang valid ke list
            }
        } catch (IOException e) {
            throw new IOException("Terjadi kesalahan saat membaca file CSV: " + filePath, e);  // Lempar error jika ada masalah membaca file CSV
        }

        // Setelah selesai membaca dan memvalidasi semua data, lakukan upsert ke database
        for (Barang barang : barangList) {
            try {
                simpanAtauPerbaruiBarang(barang);
            } catch (SQLException e) {
                throw new SQLException("Database error saat menyimpan barang: " + barang.getNama(), e);  // Lempar error jika terjadi masalah dengan database
            }
        }
    }
}

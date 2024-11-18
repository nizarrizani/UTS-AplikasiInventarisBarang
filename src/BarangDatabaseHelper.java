
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
}

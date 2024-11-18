
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.*;
import java.util.List;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.*;
import java.util.List;
import javax.swing.*;

public class EditBarangDialog extends JDialog {

    private InventarisBarangFrame frame;
    private Barang barang;
    private final JTextField namaField;
    private final JComboBox<String> jenisComboBox, lokasiComboBox;
    private final JButton saveButton, cancelButton;
    private final boolean isCreateMode;  // Flag untuk menentukan apakah mode create atau edit
    private final JSpinner hargaSpinner, stokSpinner;

    // Konstruktor
    public EditBarangDialog(InventarisBarangFrame frame, Barang barang, boolean isCreateMode) {
        this.frame = frame;
        this.barang = barang;
        this.isCreateMode = isCreateMode;  // Menentukan apakah Create atau Edit
        setTitle(isCreateMode ? "Tambah Barang" : "Edit Barang");
        setSize(300, 300);  // Ukuran dialog disesuaikan
        setLocationRelativeTo(null);
        setModal(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));  // Menambahkan 1 baris untuk jenis dan lokasi

        panel.add(new JLabel("Nama:"));
        namaField = new JTextField(barang != null ? barang.getNama() : "");  // Jika Create, biarkan kosong
        panel.add(namaField);

        panel.add(new JLabel("Harga:"));
        // Membuat JSpinner untuk harga dengan step 1000
        hargaSpinner = new JSpinner(new SpinnerNumberModel(barang != null ? barang.getHarga() : 0, 0, Integer.MAX_VALUE, 1000));
        panel.add(hargaSpinner);

        panel.add(new JLabel("Stok:"));
        // Membuat JSpinner untuk stok
        stokSpinner = new JSpinner(new SpinnerNumberModel(barang != null ? barang.getStok() : 0, 0, Integer.MAX_VALUE, 1));
        panel.add(stokSpinner);

        // ComboBox untuk Jenis Barang
        panel.add(new JLabel("Jenis:"));
        jenisComboBox = new JComboBox<>();
        jenisComboBox.setEditable(true);  // Mengizinkan pengguna untuk mengetikkan nilai
        loadJenisOptions();  // Mengisi ComboBox dengan nilai dari database
        jenisComboBox.setSelectedItem(barang != null ? barang.getJenis() : "");  // Set nilai default
        panel.add(jenisComboBox);

        // ComboBox untuk Lokasi Penyimpanan
        panel.add(new JLabel("Lokasi Penyimpanan:"));
        lokasiComboBox = new JComboBox<>();
        lokasiComboBox.setEditable(true);  // Mengizinkan pengguna untuk mengetikkan nilai
        loadLokasiOptions();  // Mengisi ComboBox dengan nilai dari database
        lokasiComboBox.setSelectedItem(barang != null ? barang.getLokasiPenyimpanan() : "");  // Set nilai default
        panel.add(lokasiComboBox);

        // Tombol Simpan
        saveButton = new JButton(isCreateMode ? "Tambah" : "Simpan");
        saveButton.addActionListener(e -> {
            saveData();
        });

        cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> {
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    // Mengambil data dari form dan mengupdate objek Barang
    public Barang updateBarangFromForm() {
        barang.setNama(namaField.getText());
        barang.setHarga((Integer) hargaSpinner.getValue());  // Ambil harga dari JSpinner
        barang.setStok((Integer) stokSpinner.getValue());  // Ambil stok dari JSpinner
        barang.setJenis((String) jenisComboBox.getSelectedItem());  // Ambil nilai dari ComboBox Jenis
        barang.setLokasiPenyimpanan((String) lokasiComboBox.getSelectedItem());  // Ambil nilai dari ComboBox Lokasi
        return barang;
    }

    private void saveData() {
        // Validasi input
        if (namaField.getText().trim().isEmpty() || hargaSpinner.getValue().toString().trim().isEmpty()
                || stokSpinner.getValue().toString().trim().isEmpty() || jenisComboBox.getSelectedItem().toString().trim().isEmpty()
                || lokasiComboBox.getSelectedItem().toString().trim().isEmpty()) {

            // Menampilkan pesan error jika ada field yang kosong
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.WARNING_MESSAGE);
            return;  // Hentikan eksekusi jika ada yang kosong
        }

        try {
            // Mengambil data dari form dan mengupdate objek barang
            updateBarangFromForm();

            if (isCreateMode) {
                // Untuk Create: Simpan data baru ke database
                BarangDatabaseHelper.tambahBarang(barang);
                JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
            } else {
                // Untuk Edit: Update data di database
                BarangDatabaseHelper.updateBarang(barang);
                JOptionPane.showMessageDialog(this, "Barang berhasil diperbarui!");
            }

            frame.updateBarangTable();  // Update tabel setelah data disimpan
            dispose();  // Menutup dialog
        } catch (SQLException e) {
            // Menampilkan error jika terjadi SQLException
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mengambil data jenis dari database dan menambahkannya ke ComboBox
    private void loadJenisOptions() {
        try {
            List<String> jenisList = BarangDatabaseHelper.getJenisList();  // Mengambil jenis barang dari database
            for (String jenis : jenisList) {
                jenisComboBox.addItem(jenis);  // Menambahkan ke ComboBox
            }
        } catch (SQLException e) {
            // Menampilkan error jika terjadi SQLException saat memuat jenis
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat jenis barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mengambil data lokasi penyimpanan dari database dan menambahkannya ke ComboBox
    private void loadLokasiOptions() {
        try {
            List<String> lokasiList = BarangDatabaseHelper.getLokasiList();  // Mengambil lokasi dari database
            for (String lokasi : lokasiList) {
                lokasiComboBox.addItem(lokasi);  // Menambahkan ke ComboBox
            }
        } catch (SQLException e) {
            // Menampilkan error jika terjadi SQLException saat memuat lokasi
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat lokasi penyimpanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

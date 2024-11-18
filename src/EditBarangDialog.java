
import java.sql.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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
        setSize(400, 300);  // Ukuran dialog lebih besar agar lebih nyaman
        setResizable(false);
        setLocationRelativeTo(frame);
        setModal(true);

        // Panel utama menggunakan GridBagLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Jarak antar komponen

        // Label dan input untuk Nama
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;  // Label di sebelah kanan
        panel.add(new JLabel("Nama:"), gbc);

        namaField = new JTextField(barang != null ? barang.getNama() : "");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Mengisi ruang horizontal
        panel.add(namaField, gbc);

        // Reset fill setelah penggunaan
        gbc.fill = GridBagConstraints.NONE;

        // Label dan input untuk Harga
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Harga:"), gbc);

        hargaSpinner = new JSpinner(new SpinnerNumberModel(barang != null ? barang.getHarga() : 0, 0, Integer.MAX_VALUE, 1000));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(hargaSpinner, gbc);

        // Reset fill setelah penggunaan
        gbc.fill = GridBagConstraints.NONE;

        // Label dan input untuk Stok
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Stok:"), gbc);

        stokSpinner = new JSpinner(new SpinnerNumberModel(barang != null ? barang.getStok() : 0, 0, Integer.MAX_VALUE, 1));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(stokSpinner, gbc);

        // Reset fill setelah penggunaan
        gbc.fill = GridBagConstraints.NONE;

        // Label dan input untuk Jenis
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Jenis:"), gbc);

        jenisComboBox = new JComboBox<>();
        jenisComboBox.setEditable(true);
        loadJenisOptions();
        jenisComboBox.setSelectedItem(barang != null ? barang.getJenis() : "");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(jenisComboBox, gbc);

        // Reset fill setelah penggunaan
        gbc.fill = GridBagConstraints.NONE;

        // Label dan input untuk Lokasi Penyimpanan
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Lokasi Penyimpanan:"), gbc);

        lokasiComboBox = new JComboBox<>();
        lokasiComboBox.setEditable(true);
        loadLokasiOptions();
        lokasiComboBox.setSelectedItem(barang != null ? barang.getLokasiPenyimpanan() : "");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lokasiComboBox, gbc);

        // Reset fill setelah penggunaan
        gbc.fill = GridBagConstraints.NONE;

        // Panel tombol simpan dan batal
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        saveButton = new JButton(isCreateMode ? "Tambah" : "Simpan");
        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/assets/check.png"));
        saveButton.setIcon(saveIcon);
        saveButton.addActionListener(e -> {
            saveData();
        });

        cancelButton = new JButton("Batal");
        ImageIcon cancelIcon = new ImageIcon(getClass().getResource("/assets/cross.png"));
        cancelButton.setIcon(cancelIcon);
        cancelButton.addActionListener(e -> {
            dispose();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Menambahkan panel utama dan panel tombol ke dialog
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

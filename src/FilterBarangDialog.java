
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FilterBarangDialog extends JDialog {

    private InventarisBarangFrame frame;
    private JTextField namaField;
    private JComboBox<String> jenisComboBox, lokasiComboBox;
    private JSpinner hargaMinSpinner, hargaMaxSpinner;
    private JCheckBox namaCheckBox, jenisCheckBox, hargaMinCheckBox, hargaMaxCheckBox, lokasiCheckBox;  // Checkbox untuk menonaktifkan filter
    private JButton applyButton, cancelButton;

    public FilterBarangDialog(InventarisBarangFrame frame) {
        this.frame = frame;
        setTitle("Filter Barang");
        setSize(350, 400);  // Ukuran dialog disesuaikan
        setLocationRelativeTo(frame);  // Posisi dialog di tengah frame
        setModal(true);  // Agar dialog modal
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());  // Ganti dengan GridBagLayout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Margin panel

        // Atur GridBagLayout untuk memberikan gap antar komponen
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Menambahkan jarak antar komponen
        
        // Filter Nama
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;  // Label ter-anchor ke kanan
        panel.add(new JLabel("Nama:"), gbc);

        namaField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Menambahkan fill horizontal untuk inputan
        panel.add(namaField, gbc);

        namaCheckBox = new JCheckBox();
        namaCheckBox.setSelected(true);  // Set default checkbox active
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;  // Reset fill untuk checkbox
        panel.add(namaCheckBox, gbc);

        // Set listener untuk checkbox Nama
        namaCheckBox.addActionListener(e -> namaField.setEnabled(namaCheckBox.isSelected()));

        // Filter Jenis
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;  // Label ter-anchor ke kanan
        panel.add(new JLabel("Jenis:"), gbc);

        jenisComboBox = new JComboBox<>();
        loadJenisOptions();  // Memuat pilihan jenis barang
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Menambahkan fill horizontal untuk inputan
        panel.add(jenisComboBox, gbc);

        jenisCheckBox = new JCheckBox();
        jenisCheckBox.setSelected(true);  // Set default checkbox active
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;  // Reset fill untuk checkbox
        panel.add(jenisCheckBox, gbc);

        // Set listener untuk checkbox Jenis
        jenisCheckBox.addActionListener(e -> jenisComboBox.setEnabled(jenisCheckBox.isSelected()));

        // Filter Harga Min
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;  // Label ter-anchor ke kanan
        panel.add(new JLabel("Harga Min:"), gbc);

        hargaMinSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1000));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Menambahkan fill horizontal untuk inputan
        panel.add(hargaMinSpinner, gbc);

        hargaMinCheckBox = new JCheckBox();
        hargaMinCheckBox.setSelected(true);  // Set default checkbox active
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;  // Reset fill untuk checkbox
        panel.add(hargaMinCheckBox, gbc);

        // Set listener untuk checkbox Harga
        hargaMinCheckBox.addActionListener(e -> hargaMinSpinner.setEnabled(hargaMinCheckBox.isSelected()));

        // Filter Harga Max
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;  // Label ter-anchor ke kanan
        panel.add(new JLabel("Harga Max:"), gbc);

        hargaMaxSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1000));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Menambahkan fill horizontal untuk inputan
        panel.add(hargaMaxSpinner, gbc);

        hargaMaxCheckBox = new JCheckBox();
        hargaMaxCheckBox.setSelected(true);  // Set default checkbox active
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;  // Reset fill untuk checkbox
        panel.add(hargaMaxCheckBox, gbc);

        // Set listener untuk checkbox Harga Max
        hargaMaxCheckBox.addActionListener(e -> hargaMaxSpinner.setEnabled(hargaMaxCheckBox.isSelected()));

        // Filter Lokasi Penyimpanan
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;  // Label ter-anchor ke kanan
        panel.add(new JLabel("Lokasi:"), gbc);

        lokasiComboBox = new JComboBox<>();
        loadLokasiOptions();  // Memuat pilihan lokasi penyimpanan barang
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Menambahkan fill horizontal untuk inputan
        panel.add(lokasiComboBox, gbc);

        lokasiCheckBox = new JCheckBox();
        lokasiCheckBox.setSelected(true);  // Set default checkbox active
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;  // Reset fill untuk checkbox
        panel.add(lokasiCheckBox, gbc);

        // Set listener untuk checkbox Lokasi
        lokasiCheckBox.addActionListener(e -> lokasiComboBox.setEnabled(lokasiCheckBox.isSelected()));
        
        // Panel tombol simpan dan batal
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        applyButton = new JButton("Terapkan");
        ImageIcon applyIcon = new ImageIcon(getClass().getResource("/assets/check.png"));
        applyButton.setIcon(applyIcon);
        
        cancelButton = new JButton("Batal");
        ImageIcon cancelIcon = new ImageIcon(getClass().getResource("/assets/cross.png"));
        cancelButton.setIcon(cancelIcon);
        
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        // Aksi untuk tombol Terapkan
        applyButton.addActionListener(e -> {
            applyFilter();
            dispose();  // Tutup dialog setelah apply
        });

        // Aksi untuk tombol Batal
        cancelButton.addActionListener(e -> dispose());  // Tutup dialog tanpa perubahan

        getContentPane().add(panel, BorderLayout.CENTER);  // Menambahkan panel ke dialog
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);  // Menambahkan tombol ke bawah
    }

    // Memuat jenis barang dari database
    private void loadJenisOptions() {
        try {
            List<String> jenisList = BarangDatabaseHelper.getJenisList();  // Mengambil jenis barang dari database
            jenisComboBox.addItem("All");  // Menambahkan opsi 'All' untuk semua jenis
            for (String jenis : jenisList) {
                jenisComboBox.addItem(jenis);  // Menambahkan ke ComboBox
            }
        } catch (SQLException e) {
            // Menampilkan error jika terjadi kesalahan saat memuat jenis
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat jenis barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Memuat lokasi penyimpanan dari database
    private void loadLokasiOptions() {
        try {
            List<String> lokasiList = BarangDatabaseHelper.getLokasiList();  // Mengambil lokasi penyimpanan barang dari database
            lokasiComboBox.addItem("All");  // Menambahkan opsi 'All' untuk semua lokasi
            for (String lokasi : lokasiList) {
                lokasiComboBox.addItem(lokasi);  // Menambahkan ke ComboBox
            }
        } catch (SQLException e) {
            // Menampilkan error jika terjadi kesalahan saat memuat lokasi
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat lokasi barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menyaring barang berdasarkan input filter
    private void applyFilter() {
        String namaFilter = (namaCheckBox.isSelected() && !namaField.getText().trim().isEmpty()) ? namaField.getText().toLowerCase() : "";  // Nama yang difilter
        String jenisFilter = (jenisCheckBox.isSelected() && jenisComboBox.getSelectedItem() != null) ? (String) jenisComboBox.getSelectedItem() : "All";  // Jenis yang dipilih
        String lokasiFilter = (lokasiCheckBox.isSelected() && lokasiComboBox.getSelectedItem() != null) ? (String) lokasiComboBox.getSelectedItem() : "All";  // Lokasi yang dipilih
        int hargaMin = (hargaMinCheckBox.isSelected()) ? (int) hargaMinSpinner.getValue() : 0;  // Harga min yang dipilih
        int hargaMax = (hargaMinCheckBox.isSelected()) ? (int) hargaMaxSpinner.getValue() : Integer.MAX_VALUE;  // Harga max yang dipilih

        // Menyaring barang berdasarkan input filter
        filterBarang(namaFilter, jenisFilter, lokasiFilter, hargaMin, hargaMax);
    }

    // Fungsi untuk menyaring barang berdasarkan filter
    private void filterBarang(String nama, String jenis, String lokasi, int hargaMin, int hargaMax) {
        try {
            List<Barang> daftarBarang = BarangDatabaseHelper.bacaSemuaBarang();  // Mengambil semua barang dari database

            List<Barang> filteredList = daftarBarang.stream()
                    .filter(barang -> nama.isEmpty() || barang.getNama().toLowerCase().contains(nama)) // Filter berdasarkan nama
                    .filter(barang -> jenis.equals("All") || barang.getJenis().equals(jenis)) // Filter berdasarkan jenis
                    .filter(barang -> lokasi.equals("All") || barang.getLokasiPenyimpanan().equals(lokasi)) // Filter berdasarkan lokasi
                    .filter(barang -> barang.getHarga() >= hargaMin && barang.getHarga() <= hargaMax) // Filter berdasarkan harga
                    .collect(Collectors.toList());

            // Memperbarui tabel dengan data yang sudah difilter
            frame.updateBarangTable(filteredList);  // Panggil update tabel dengan daftar barang yang sudah difilter
        } catch (SQLException ex) {
            Logger.getLogger(FilterBarangDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

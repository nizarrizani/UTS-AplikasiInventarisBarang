# Aplikasi Inventaris Barang

NPM   : **2210010487**  
NAMA  : **Muhammad Nizar Rizani**  
KELAS : **5A Reguler Banjarbaru**

Aplikasi ini adalah program manajemen inventaris berbasis Java dengan antarmuka GUI. Aplikasi dirancang untuk membantu pengguna dalam mengelola data barang, termasuk menambahkan, mengedit, menghapus, dan menampilkan grafik terkait stok barang.

## Fitur Utama

- **Manajemen Data Barang**: Tambah, edit, dan hapus data barang.
- **Pencarian dan Filter**: Mencari barang berdasarkan kriteria tertentu.
- **Visualisasi Data**: Menampilkan grafik stok barang dan jenis barang.
- **Penyimpanan Data**: Menggunakan database SQLite (`barang.db`).

## Struktur Proyek

- `src/`: Berisi kode sumber aplikasi, termasuk file-file berikut:
  - `Barang.java`: Representasi entitas barang.
  - `InventarisBarangFrame.java`: Frame utama GUI aplikasi.
  - File lainnya untuk mendukung dialog dan fitur tambahan.
- `barang.db`: Database SQLite untuk menyimpan data inventaris.
- `build.xml`: Skrip build menggunakan Apache Ant.

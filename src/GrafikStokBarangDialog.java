
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GrafikStokBarangDialog extends JDialog {

    public GrafikStokBarangDialog(Frame owner, Map<String, Map<String, Integer>> data) {
        super(owner, "Grafik Stok Barang Berdasarkan Jenis", true);
        setLayout(new BorderLayout());

        // Membuat dataset
        CategoryDataset dataset = createDataset(data);

        // Membuat grafik
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Stok Barang Berdasarkan Jenis", // Judul grafik
                "Jenis Barang", // Label sumbu X
                "Jumlah Stok", // Label sumbu Y
                dataset, // Dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL, // Orientasi grafik
                true, // Menampilkan legenda
                true, // Menampilkan tooltip
                false // Menampilkan URL
        );

        // Menyesuaikan renderer grafik untuk menampilkan bar stacked
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false); // Menghapus garis luar pada bar

        // Menampilkan label pada setiap bar
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        // Membuat panel untuk grafik
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(owner);
    }

    private CategoryDataset createDataset(Map<String, Map<String, Integer>> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Melakukan iterasi pada data dan menambahkannya ke dataset
        for (Map.Entry<String, Map<String, Integer>> jenisEntry : data.entrySet()) {
            String jenis = jenisEntry.getKey();
            Map<String, Integer> barangData = jenisEntry.getValue();

            // Menambahkan setiap 'namaBarang' (item) sebagai seri dan mengelompokkan berdasarkan 'jenis'
            for (Map.Entry<String, Integer> barangEntry : barangData.entrySet()) {
                String namaBarang = barangEntry.getKey();
                Integer stok = barangEntry.getValue();

                // Menambahkan setiap 'namaBarang' sebagai seri terpisah dan mengelompokkannya dalam kategori 'jenis' yang sama
                dataset.addValue(stok, namaBarang, jenis);
            }
        }

        return dataset;
    }
}

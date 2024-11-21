
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GrafikJenisBarangDialog extends JDialog {

    public GrafikJenisBarangDialog(Frame owner, Map<String, Integer> dataPieChart) {
        super(owner, "Grafik Barang Berdasarkan Jenis", true);

        // Buat dataset untuk pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : dataPieChart.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Buat Pie Chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Stok Barang Berdasarkan Jenis", // Judul grafik
                dataset, // Data
                true, // Legend
                true, // Tooltips
                false // URLs
        );

        // Tambahkan chart ke dalam panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        // Tambahkan tombol tutup
        JButton closeButton = new JButton("Tutup");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Atur ukuran dialog
        pack();
        setLocationRelativeTo(owner);
    }
}

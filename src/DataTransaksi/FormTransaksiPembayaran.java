/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataTransaksi;

import DashboardTampilan.DashboardStaffAdministrasi;
import DashboardTampilan.DashboardSiswa;
import Koneksi.DatabaseConnection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ORIGIN
 */
public class FormTransaksiPembayaran extends javax.swing.JFrame {
     private String role;

    /**
     * Creates new form FormTransaksiPembayaran
     */
    private List<String> daftarPembayaran = new ArrayList<>();
    private double totalPembayaran = 0;
    
   public FormTransaksiPembayaran(String role) {
        initComponents();
        this.role = role;
        setLocationRelativeTo(null);
        tampilkanDataTransaksi();
        generateIdPembayaran();
        setBiayaDefault();
        setTanggalOtomatis();
    
        // Make fields uneditable
        jTextField1.setEditable(false);
        jTextField6.setEditable(false);
        jTextField7.setEditable(false);
        jTextField8.setEditable(false);
    
        // Add focus listener for student ID field
        jTextField2.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
        cariNamaSiswa();
        }
    });
        
         // Apply role-based restrictions
        if (role.equalsIgnoreCase("siswa")) {
            hanyaLihat();
        }
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = jTable1.getSelectedRow();
        if (row >= 0) {
            jTextField1.setText(jTable1.getValueAt(row, 0).toString()); // ID Pembayaran
            jTextField2.setText(jTable1.getValueAt(row, 1).toString()); // ID Siswa
            jTextField4.setText(jTable1.getValueAt(row, 2).toString()); // Nama Siswa
            jTextField3.setText(jTable1.getValueAt(row, 3).toString()); // ID Staff
        }
    }
});

}
   
     private void hanyaLihat() {
      
        jButton4.setVisible(false); // hapus
        jButton3.setVisible(false); // edit
        
    }
   
     private void setBiayaDefault() {
        jTextField6.setText("650000"); // Biaya SPP
        jTextField7.setText("250000"); // Biaya Seragam
        jTextField8.setText("250000"); // Biaya Buku
        totalPembayaran = 0; // Reset total
        jTextField9.setText("Rp 0"); // Reset tampilan total
}
   
     private double getBiayaPenuh(String jenisBayar) {
    switch(jenisBayar) {
        case "SPP":
            return 650000; // Nilai default SPP
        case "SERAGAM":
            return 250000; // Nilai default seragam
        case "BUKU":
            return 250000; // Nilai default buku
        default:
            return 0;
    }
}
     
     private void cariNamaSiswa() {
        String idSiswa = jTextField2.getText().trim();
        if (!idSiswa.isEmpty()) {
            try {
                Connection conn = DatabaseConnection.connect();
                String sql = "SELECT nama_siswa FROM data_siswa WHERE id_siswa = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idSiswa);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    jTextField4.setText(rs.getString("nama_siswa"));
                } else {
                    JOptionPane.showMessageDialog(this, "ID Siswa tidak ditemukan!");
                    jTextField2.setText("");
                    jTextField4.setText("");
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error saat mencari siswa: " + ex.getMessage());
            }
        }
    }
    
   
    
    private void setTanggalOtomatis() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jTextField10.setText(sdf.format(new Date()));
    }
    
    private void setStatusDefault() {
        jTextField11.setText("Belum Lunas"); // Status default
    }
    
  private void tampilkanDataTransaksi() {
        try {
        Connection conn = DatabaseConnection.connect();
        String sql = "SELECT tp.*, ds.nama_siswa as nama_siswa_asli " +
                   "FROM transaksi_pembayaran tp " +
                   "LEFT JOIN data_siswa ds ON tp.id_siswa = ds.id_siswa " +
                   "ORDER BY tanggal DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        model.addColumn("ID Pembayaran");
        model.addColumn("ID Siswa");
        model.addColumn("Nama Siswa");
        model.addColumn("ID Staff");
        model.addColumn("Jenis Pembayaran");
        model.addColumn("Bulan"); // Kolom baru untuk bulan
        model.addColumn("Total Pembayaran");
        model.addColumn("Metode Pembayaran");
        model.addColumn("Tanggal");
        model.addColumn("Status");

        while (rs.next()) {
            String namaSiswa = rs.getString("nama_siswa_asli") != null ? 
                              rs.getString("nama_siswa_asli") : 
                              rs.getString("nama_siswa");
            
            model.addRow(new Object[]{
                rs.getString("id_pembayaran"),
                rs.getString("id_siswa"),
                namaSiswa,
                rs.getString("id_staff"),
                rs.getString("jenis_pembayaran"),
                rs.getString("bulan"), // Data bulan dari database
                "Rp " + rs.getDouble("total_pembayaran"),
                rs.getString("metode_pembayaran"),
                rs.getString("tanggal"),
                rs.getString("status_pembayaran").equals("1") ? "Lunas" : "Belum Lunas"
            });
        }

        jTable1.setModel(model);
        
        rs.close();
        stmt.close();
        conn.close();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error saat menampilkan data: " + e.getMessage());
    }
}
  
  private void jenisPembayaranChanged(java.awt.event.ActionEvent evt) {
    if (jComboBox1.getSelectedItem() != null) {
        String jenis = jComboBox1.getSelectedItem().toString();
        
        // Tampilkan bulan hanya untuk SPP
        boolean showBulan = jenis.equals("SPP");
        jLabel15.setVisible(showBulan);
        jComboBox3.setVisible(showBulan);
        
        // Jika bukan SPP, reset nilai bulan
        if (!showBulan) {
            jComboBox3.setSelectedIndex(-1);
        }
    }
}
    
     private void clearForm() {
        jTextField2.setText("");
        jTextField4.setText(""); 
        jTextField3.setText("");
        jTextField9.setText("");
        jTextField11.setText("Belum Lunas");
        jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox3.setSelectedIndex(-1); // Reset combobox bulan
        totalPembayaran = 0;
}
    
    private void generateIdPembayaran() {
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT MAX(id_pembayaran) as last_id FROM transaksi_pembayaran";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String lastId = rs.getString("last_id");
                if (lastId != null) {
                    int nextId = Integer.parseInt(lastId) + 1;
                    nextId = Math.max(nextId, 101101);
                    jTextField1.setText(String.valueOf(nextId));
                } else {
                    jTextField1.setText("101101");
                }
            } else {
                jTextField1.setText("101101");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException | NumberFormatException e) {
            jTextField1.setText("101101");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setMinimumSize(new java.awt.Dimension(958, 801));

        jLabel1.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("TRANSAKSI PEMBAYARAN");

        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Id Pembayaran : ");

        jTextField1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel3.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Id Siswa :");

        jTextField2.setBackground(new java.awt.Color(204, 255, 255));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Id Staff :");

        jTextField3.setBackground(new java.awt.Color(204, 255, 255));

        jLabel5.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jenis  Pembayaran :");

        jLabel6.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Metode Pembayaran :");

        jLabel7.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Biaya SPP Perbulan :");

        jTextField6.setBackground(new java.awt.Color(204, 255, 255));

        jLabel8.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Biaya Seragam :");

        jTextField7.setBackground(new java.awt.Color(204, 255, 255));

        jLabel9.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Biaya Buku :");

        jTextField8.setBackground(new java.awt.Color(204, 255, 255));

        jLabel10.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Total Pembayaran :");

        jTextField9.setBackground(new java.awt.Color(204, 255, 255));

        jTextField10.setBackground(new java.awt.Color(204, 255, 255));

        jLabel11.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tanggal");

        jLabel12.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Status Pembayaran :");

        jTextField11.setBackground(new java.awt.Color(204, 255, 255));

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton1.setText("BAYAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 255, 255));
        jButton2.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton2.setText("LIHAT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 255, 255));
        jButton3.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton3.setText("EDIT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(204, 255, 255));
        jButton4.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton4.setText("HAPUS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(204, 255, 255));
        jButton5.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton5.setText("CARI");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Cari :");

        jTextField12.setBackground(new java.awt.Color(204, 255, 255));

        jButton6.setBackground(new java.awt.Color(204, 255, 255));
        jButton6.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton6.setText("HITUNG TOTAL");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jComboBox1.setBackground(new java.awt.Color(51, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SPP", "SERAGAM", "BUKU" }));
        jComboBox1.setSelectedIndex(-1);

        jComboBox2.setBackground(new java.awt.Color(51, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TRANSFER", "CASH" }));
        jComboBox2.setSelectedIndex(-1);

        jLabel14.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nama Siswa");

        jTextField4.setBackground(new java.awt.Color(204, 255, 255));

        jButton8.setBackground(new java.awt.Color(204, 255, 255));
        jButton8.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton8.setText("KEMBALI");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jComboBox3.setBackground(new java.awt.Color(51, 255, 255));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "JANUARI", "FEBRUARI", "MARET", "APRIL ", "MEI", "JUNI", "JULI", "AGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DESEMBER" }));
        jComboBox3.setSelectedIndex(-1);

        jLabel15.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Bulan : ");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addComponent(jScrollPane2)
                .addGap(114, 114, 114))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel11)
                        .addGap(107, 107, 107)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel13)
                        .addGap(106, 106, 106)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel10)
                        .addGap(36, 36, 36)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel12)
                        .addGap(14, 14, 14)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel5)
                        .addGap(33, 33, 33)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel9)
                        .addGap(64, 64, 64)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel4)
                        .addGap(108, 108, 108)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel8)
                        .addGap(40, 40, 40)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel14)
                        .addGap(84, 84, 84)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel15)
                        .addGap(95, 95, 95)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel3)
                        .addGap(99, 99, 99)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel7)
                        .addGap(11, 11, 11)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel2)
                        .addGap(50, 50, 50)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel6)
                        .addGap(1, 1, 1)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7))
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8))))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11))
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(120, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBounds(0, 0, 976, 846);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    try {
        // Validasi input
        String idSiswa = jTextField2.getText().trim();
        if (idSiswa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap masukkan ID Siswa!");
            return;
        }
        
        // Verifikasi siswa
        Connection conn = DatabaseConnection.connect();
        String checkStudentSql = "SELECT id_siswa FROM data_siswa WHERE id_siswa = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkStudentSql);
        checkStmt.setString(1, idSiswa);
        ResultSet rs = checkStmt.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "ID Siswa tidak valid!");
            rs.close();
            checkStmt.close();
            conn.close();
            return;
        }
        rs.close();
        checkStmt.close();
        
        // Validasi field wajib
        if (jTextField3.getText().trim().isEmpty() || 
            jComboBox1.getSelectedItem() == null || 
            jComboBox2.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Harap isi semua field yang wajib!");
            conn.close();
            return;
        }
        
        // Validasi khusus untuk SPP
        String jenisBayar = jComboBox1.getSelectedItem().toString();
        if (jenisBayar.equals("SPP") && jComboBox3.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Harap pilih bulan untuk pembayaran SPP!");
            conn.close();
            return;
        }
        
        if (totalPembayaran <= 0) {
            JOptionPane.showMessageDialog(this, "Total pembayaran harus lebih dari 0!");
            conn.close();
            return;
        }
        
        // Handle bulan
        String bulan = jenisBayar.equals("SPP") ? jComboBox3.getSelectedItem().toString() : null;
        
        String sql = "INSERT INTO transaksi_pembayaran (id_pembayaran, id_siswa, nama_siswa, id_staff, " +
                   "jenis_pembayaran, bulan, total_pembayaran, metode_pembayaran, biaya_spp_perbulan, " +
                   "biaya_seragam, biaya_buku, status_pembayaran, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, jTextField1.getText());
        ps.setString(2, idSiswa);
        ps.setString(3, jTextField4.getText());
        ps.setString(4, jTextField3.getText());
        ps.setString(5, jenisBayar);
        ps.setString(6, bulan); // Bisa null untuk seragam/buku
        ps.setDouble(7, totalPembayaran);
        ps.setString(8, jComboBox2.getSelectedItem().toString());
        
        double spp = jenisBayar.equals("SPP") ? Double.parseDouble(jTextField6.getText()) : 0;
        double seragam = jenisBayar.equals("SERAGAM") ? Double.parseDouble(jTextField7.getText()) : 0;
        double buku = jenisBayar.equals("BUKU") ? Double.parseDouble(jTextField8.getText()) : 0;
        
        ps.setDouble(9, spp);
        ps.setDouble(10, seragam);
        ps.setDouble(11, buku);
        ps.setString(12, jTextField11.getText().equals("Lunas") ? "1" : "0");
        ps.setString(13, jTextField10.getText());
        
        int affectedRows = ps.executeUpdate();
        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
            tampilkanDataTransaksi();
            clearForm();
            generateIdPembayaran();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi");
        }
        
        ps.close();
        conn.close();
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error saat menyimpan data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Format biaya tidak valid!");
      }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         tampilkanDataTransaksi();      
                                            
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    try {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit!");
                return;
            }
            
            String idPembayaran = jTable1.getValueAt(selectedRow, 0).toString();
            
            Connection conn = DatabaseConnection.connect();
            String sql = "UPDATE transaksi_pembayaran SET id_siswa=?, id_staff=?, " +
                       "jenis_pembayaran=?, total_pembayaran=?, metode_pembayaran=?, " +
                       "status_pembayaran=? WHERE id_pembayaran=?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, jTextField2.getText());
            ps.setString(2, jTextField3.getText());
            ps.setString(3, jComboBox1.getSelectedItem().toString());
            ps.setDouble(4, totalPembayaran);
            ps.setString(5, jComboBox2.getSelectedItem().toString());
            ps.setString(6, jTextField11.getText().equals("Lunas") ? "1" : "0");
            ps.setString(7, idPembayaran);
            
            int affectedRows = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, affectedRows > 0 ? "BERHASIL" : "GAGAL");
            tampilkanDataTransaksi();
            
            ps.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mengupdate data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Yakin ingin menghapus data pembayaran ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String idPembayaran = jTable1.getValueAt(selectedRow, 0).toString();
                
                Connection conn = DatabaseConnection.connect();
                String sql = "DELETE FROM transaksi_pembayaran WHERE id_pembayaran = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, idPembayaran);
                
                int affectedRows = ps.executeUpdate();
                JOptionPane.showMessageDialog(this, affectedRows > 0 ? "BERHASIL" : "GAGAL");
                tampilkanDataTransaksi();
                clearForm();
                
                ps.close();
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat menghapus data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
      String keyword = jTextField12.getText().trim();
        
        if (keyword.isEmpty()) {
            tampilkanDataTransaksi();
            return;
        }
        
        try {
            Connection conn = DatabaseConnection.connect();
            // Modified search to use nama_siswa from transaksi_pembayaran table
            String sql = "SELECT * FROM transaksi_pembayaran " +
                       "WHERE id_siswa LIKE ? OR nama_siswa LIKE ? " +
                       "ORDER BY tanggal DESC";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Pembayaran");
            model.addColumn("ID Siswa");
            model.addColumn("Nama Siswa");
            model.addColumn("ID Staff");
            model.addColumn("Jenis Pembayaran");
            model.addColumn("Total Pembayaran");
            model.addColumn("Metode Pembayaran");
            model.addColumn("Tanggal");
            model.addColumn("Status");
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_pembayaran"),
                    rs.getString("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("id_staff"),
                    rs.getString("jenis_pembayaran"),
                    "Rp " + rs.getDouble("total_pembayaran"),
                    rs.getString("metode_pembayaran"),
                    rs.getString("tanggal"),
                    rs.getString("status_pembayaran").equals("1") ? "Lunas" : "Belum Lunas"
                });
            }
            
            jTable1.setModel(model);
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mencari data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
      try {
        if (jComboBox1.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih jenis pembayaran terlebih dahulu!");
            return;
        }
        
        String jenisBayar = jComboBox1.getSelectedItem().toString();
        double biaya = 0;
        
        // Reset total pembayaran setiap kali dihitung
        totalPembayaran = 0;
        
        // Hitung berdasarkan jenis pembayaran yang dipilih
        switch(jenisBayar) {
            case "SPP":
                biaya = Double.parseDouble(jTextField6.getText());
                break;
            case "SERAGAM":
                biaya = Double.parseDouble(jTextField7.getText());
                break;
            case "BUKU":
                biaya = Double.parseDouble(jTextField8.getText());
                break;
            default:
                biaya = 0;
        }
        
        // Set total pembayaran (tidak diakumulasi)
        totalPembayaran = biaya;
        jTextField9.setText("Rp " + totalPembayaran);
        
        // Set status pembayaran
        if (totalPembayaran >= getBiayaPenuh(jenisBayar)) {
            jTextField11.setText("Lunas");
        } else {
            jTextField11.setText("Belum Lunas");
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error menghitung total: " + e.getMessage());
    }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
          if (role.equalsIgnoreCase("StaffAdministrasi")) {
    new DashboardStaffAdministrasi().setVisible(true);
} else if (role.equalsIgnoreCase("Siswa")) {
    new DashboardSiswa().setVisible(true); 
}
dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTransaksiPembayaran("StaffAdministrasi").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataTransaksi;

import DashboardTampilan.DashboardStaffAdministrasi;
import DashboardTampilan.DashboardSiswa;
import Koneksi.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ORIGIN
 */
public class FormDataPembayaranSiswa extends javax.swing.JFrame {
     private String role;

    /**
     * Creates new form FormDataPembayaranSiswa
     */
     
    public FormDataPembayaranSiswa(String role) {
        initComponents();
        this.role = role;
        setLocationRelativeTo(null);
        tampilkanDataPembayaranSiswa();
        
          // Apply role-based restrictions
        if (role.equalsIgnoreCase("siswa")) {
            hanyaLihat();
        }
    }
    
     private void hanyaLihat() {
      
       
    }

    
    private void tampilkanDataPembayaranSiswa() {
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT ds.id_siswa, ds.nama_siswa, "
                       + "(SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP') AS jumlah_bulan_dibayar, "
                       + "(12 - (SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP')) AS jumlah_bulan_tunggakan, "
                       + "IF(EXISTS(SELECT 1 FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'BUKU'), 'lunas', 'belum_lunas') AS status_pembayaran_buku, "
                       + "IF(EXISTS(SELECT 1 FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SERAGAM'), 'lunas', 'belum_lunas') AS status_pembayaran_seragam, "
                       + "IF((SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP') = 12, 'lunas', 'belum_lunas') AS status_pembayaran_spp "
                       + "FROM data_siswa ds";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            model.addColumn("ID Siswa");
            model.addColumn("Nama Siswa");
            model.addColumn("Bulan Dibayar");
            model.addColumn("Bulan Tunggakan");
            model.addColumn("Status Buku");
            model.addColumn("Status Seragam");
            model.addColumn("Status SPP");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("jumlah_bulan_dibayar"),
                    rs.getString("jumlah_bulan_tunggakan"),
                    rs.getString("status_pembayaran_buku"),
                    rs.getString("status_pembayaran_seragam"),
                    rs.getString("status_pembayaran_spp")
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

    private void cariDataPembayaranSiswa(String keyword) {
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT ds.id_siswa, ds.nama_siswa, "
                       + "(SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP') AS jumlah_bulan_dibayar, "
                       + "(12 - (SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP')) AS jumlah_bulan_tunggakan, "
                       + "IF(EXISTS(SELECT 1 FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'BUKU'), 'lunas', 'belum_lunas') AS status_pembayaran_buku, "
                       + "IF(EXISTS(SELECT 1 FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SERAGAM'), 'lunas', 'belum_lunas') AS status_pembayaran_seragam, "
                       + "IF((SELECT COUNT(*) FROM transaksi_pembayaran tp "
                       + "WHERE tp.id_siswa = ds.id_siswa AND tp.jenis_pembayaran = 'SPP') = 12, 'lunas', 'belum_lunas') AS status_pembayaran_spp "
                       + "FROM data_siswa ds "
                       + "WHERE ds.id_siswa LIKE ? OR ds.nama_siswa LIKE ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("jumlah_bulan_dibayar"),
                    rs.getString("jumlah_bulan_tunggakan"),
                    rs.getString("status_pembayaran_buku"),
                    rs.getString("status_pembayaran_seragam"),
                    rs.getString("status_pembayaran_spp")
                });
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mencari data: " + e.getMessage());
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FORM DATA PEMBAYARAN PER SISWA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 209;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.insets = new java.awt.Insets(30, 158, 1, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

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
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 824;
        gridBagConstraints.ipady = 180;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(35, 49, 0, 50);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton1.setText("LIHAT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 143;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(45, 38, 0, 0);
        jPanel1.add(jButton1, gridBagConstraints);

        jTextField1.setBackground(new java.awt.Color(204, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 204;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(47, 18, 0, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cari :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(55, 61, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jButton2.setBackground(new java.awt.Color(204, 255, 255));
        jButton2.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton2.setText("SEARCH ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 125;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
        jPanel1.add(jButton2, gridBagConstraints);

        jButton3.setBackground(new java.awt.Color(204, 255, 255));
        jButton3.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton3.setText("KEMBALI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 209;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(89, 2, 48, 0);
        jPanel1.add(jButton3, gridBagConstraints);

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

        setBounds(0, 0, 968, 682);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
          if (role.equalsIgnoreCase("StaffAdministrasi")) {
    new DashboardStaffAdministrasi().setVisible(true);
} else if (role.equalsIgnoreCase("Siswa")) {
    new DashboardSiswa().setVisible(true); 
}
dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String keyword = jTextField1.getText().trim();
        if (!keyword.isEmpty()) {
            cariDataPembayaranSiswa(keyword);
        } else {
            tampilkanDataPembayaranSiswa();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tampilkanDataPembayaranSiswa();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(FormDataPembayaranSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDataPembayaranSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDataPembayaranSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDataPembayaranSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDataPembayaranSiswa("StaffAdministrasi").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

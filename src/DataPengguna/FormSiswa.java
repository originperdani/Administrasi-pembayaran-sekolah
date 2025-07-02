/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataPengguna;

import DashboardTampilan.DashboardStaffAdministrasi;
import DashboardTampilan.DashboardSiswa;
import Koneksi.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ORIGIN
 */
public class FormSiswa extends javax.swing.JFrame {
    
   private DefaultTableModel model;
    private String role;

    /**
     * Creates new form FormSiswa
     */
   public FormSiswa(String role) {
        initComponents();
        this.role = role;
        setLocationRelativeTo(null);
        generateIdSiswa();
        
        String[] columnNames = {"ID", "Nama Siswa", "NIS", "Kelas", "Jenis Kelamin", "Alamat"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };
        jTable1.setModel(model);
        loadTableData();
        
        jTextField1.addActionListener(e -> loadDataById());
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    jTextField1.setText(jTable1.getValueAt(row, 0).toString());
                    loadDataById();
                }
            }
        });
        
         // Apply role-based restrictions
        if (role.equalsIgnoreCase("siswa")) {
            hanyaLihat();
        }

    }
   
   private void hanyaLihat() {
      
        jButton2.setVisible(false); // EDIT
        jButton4.setVisible(false); // HAPUS
       
    }
    
    private void loadDataById() {
        String id = jTextField1.getText().trim();
        if (id.isEmpty()) {
            return;
        }
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM data_siswa WHERE id_siswa = ?")) {
            
            pst.setInt(1, Integer.parseInt(id));
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                jTextField2.setText(rs.getString("nama_siswa"));
                jTextField3.setText(rs.getString("nis"));
                jTextField4.setText(rs.getString("kelas"));
                jComboBox1.setSelectedItem(rs.getString("jenis_kelamin"));
                jTextArea1.setText(rs.getString("alamat"));
                
                // Enable tombol edit dan hapus
                jButton2.setEnabled(true);
                jButton4.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
                clearForm();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
       
   private void generateIdSiswa() {
    try {
        Connection conn = DatabaseConnection.connect();
        
        // Cari ID maksimum yang ada
        String maxIdSql = "SELECT MAX(id_siswa) as last_id FROM data_siswa";
        PreparedStatement maxStmt = conn.prepareStatement(maxIdSql);
        ResultSet maxRs = maxStmt.executeQuery();
        
        if (maxRs.next()) {
            int lastId = maxRs.getInt("last_id");
            if (lastId > 0) {
                // Cek apakah ada gap di antara ID yang ada
                String gapSql = "SELECT MIN(t1.id_siswa + 1) as next_id " +
                               "FROM data_siswa t1 " +
                               "WHERE NOT EXISTS (SELECT 1 FROM data_siswa t2 WHERE t2.id_siswa = t1.id_siswa + 1) " +
                               "AND t1.id_siswa < ?";
                PreparedStatement gapStmt = conn.prepareStatement(gapSql);
                gapStmt.setInt(1, lastId);
                ResultSet gapRs = gapStmt.executeQuery();
                
                if (gapRs.next() && gapRs.getInt("next_id") > 0) {
                    jTextField1.setText(String.valueOf(gapRs.getInt("next_id")));
                } else {
                    jTextField1.setText(String.valueOf(lastId + 1));
                }
                gapRs.close();
                gapStmt.close();
            } else {
                jTextField1.setText("10001"); // Jika tabel kosong
            }
        } else {
            jTextField1.setText("10001"); // Fallback
        }
        
        maxRs.close();
        maxStmt.close();
        conn.close();
    } catch (Exception e) {
        jTextField1.setText("10001"); // Default jika error
        e.printStackTrace();
    }
}
    
    
 private void clearForm() {
    jTextField1.setText(""); // ID Siswa
    jTextField2.setText(""); // Nama Siswa
    jTextField3.setText(""); // NIS
    jTextField4.setText(""); // Kelas
    jComboBox1.setSelectedIndex(0); // Jenis Kelamin
    jTextArea1.setText(""); // Alamat
    
    generateIdSiswa(); // Generate ID baru setelah clear
}
    
 private void loadTableData() {
        model.setRowCount(0); // Clear table
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM data_siswa");
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("nis"),
                    rs.getString("kelas"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("alamat")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Siswa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 54, 0, 0);
        jPanel2.add(jLabel2, gridBagConstraints);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 255, 255));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 241;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 46, 0, 0);
        jPanel2.add(jTextField1, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Siswa :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 54, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        jTextField2.setBackground(new java.awt.Color(204, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 241;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 46, 0, 0);
        jPanel2.add(jTextField2, gridBagConstraints);

        jTextField3.setBackground(new java.awt.Color(204, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 241;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 46, 0, 0);
        jPanel2.add(jTextField3, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NIS :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(22, 54, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kelas :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 54, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        jTextField4.setBackground(new java.awt.Color(204, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 240;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 46, 0, 0);
        jPanel2.add(jTextField4, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Jenis Kelamin :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 54, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        jComboBox1.setBackground(new java.awt.Color(51, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Laki-laki", "Perempuan" }));
        jComboBox1.setSelectedIndex(-1);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.ipadx = 154;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 46, 0, 0);
        jPanel2.add(jComboBox1, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 776;
        gridBagConstraints.ipady = 107;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(27, 54, 0, 50);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton1.setText("TAMBAH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 54, 0, 0);
        jPanel2.add(jButton1, gridBagConstraints);

        jButton2.setBackground(new java.awt.Color(204, 255, 255));
        jButton2.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton2.setText("EDIT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 102, 0, 0);
        jPanel2.add(jButton2, gridBagConstraints);

        jButton3.setBackground(new java.awt.Color(204, 255, 255));
        jButton3.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton3.setText("CARI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(29, 1, 0, 0);
        jPanel2.add(jButton3, gridBagConstraints);

        jTextArea1.setBackground(new java.awt.Color(204, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.ipady = 151;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 60, 0, 50);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Alamat :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 106, 0, 0);
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Rockwell", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FORM SISWA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.ipady = 41;
        jPanel2.add(jLabel1, gridBagConstraints);

        jButton4.setBackground(new java.awt.Color(204, 255, 255));
        jButton4.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton4.setText("HAPUS");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(29, 99, 0, 5);
        jPanel2.add(jButton4, gridBagConstraints);

        jButton5.setBackground(new java.awt.Color(204, 255, 255));
        jButton5.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jButton5.setText("KEMBALI");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.ipadx = 221;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(67, 0, 49, 0);
        jPanel2.add(jButton5, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
        );

        setBounds(0, 0, 924, 707);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    if (jTextField2.getText().trim().isEmpty() || jTextField3.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama dan NIS harus diisi!");
        return;
    }
    
    // Cek apakah NIS sudah ada
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement checkStmt = conn.prepareStatement(
             "SELECT 1 FROM data_siswa WHERE nis = ?")) {
         
        checkStmt.setString(1, jTextField3.getText().trim());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "NIS sudah terdaftar!");
            return;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        return;
    }
    
    // Lanjutkan insert data
    try (Connection conn = DatabaseConnection.connect()) {
        String insertSiswa = "INSERT INTO data_siswa (id_siswa, nama_siswa, nis, kelas, jenis_kelamin, alamat) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(insertSiswa)) {
            pst.setInt(1, Integer.parseInt(jTextField1.getText()));
            pst.setString(2, jTextField2.getText().trim());
            pst.setString(3, jTextField3.getText().trim());
            pst.setString(4, jTextField4.getText().trim());
            pst.setString(5, jComboBox1.getSelectedItem().toString());
            pst.setString(6, jTextArea1.getText().trim());
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Data siswa berhasil ditambahkan!");
            clearForm();
            loadTableData();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
      if (jTextField1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit!");
            return;
        }
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE data_siswa SET nama_siswa=?, nis=?, kelas=?, jenis_kelamin=?, alamat=? WHERE id_siswa=?")) {
            
            pst.setString(1, jTextField2.getText().trim());
            pst.setString(2, jTextField3.getText().trim());
            pst.setString(3, jTextField4.getText().trim());
            pst.setString(4, jComboBox1.getSelectedItem().toString());
            pst.setString(5, jTextArea1.getText().trim());
            pst.setInt(6, Integer.parseInt(jTextField1.getText()));
            
            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       String keyword = JOptionPane.showInputDialog(this, "Masukkan nama atau NIS:");
        if (keyword == null || keyword.trim().isEmpty()) {
            loadTableData();
            return;
        }
        
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM data_siswa WHERE nama_siswa LIKE ? OR nis LIKE ?")) {
            
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("nis"),
                    rs.getString("kelas"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("alamat")
                });
            }
            
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
                loadTableData();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    String selectedId = jTextField1.getText().trim();
    if (selectedId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Hapus data dengan ID " + selectedId + "?", 
        "Konfirmasi", 
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Mulai transaction
            
            try {
                // 1. Hapus transaksi terkait dahulu
                try (PreparedStatement pstTransaksi = conn.prepareStatement(
                    "DELETE FROM transaksi_pembayaran WHERE id_siswa=?")) {
                    pstTransaksi.setInt(1, Integer.parseInt(selectedId));
                    pstTransaksi.executeUpdate();
                }
                
                // 2. Baru hapus siswa
                try (PreparedStatement pstSiswa = conn.prepareStatement(
                    "DELETE FROM data_siswa WHERE id_siswa=?")) {
                    pstSiswa.setInt(1, Integer.parseInt(selectedId));
                    int deleted = pstSiswa.executeUpdate();
                    
                    if (deleted > 0) {
                        conn.commit(); // Commit jika berhasil semua
                        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                        clearForm();
                        loadTableData();
                    }
                }
            } catch (Exception e) {
                conn.rollback(); // Rollback jika ada error
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
      }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
            if (role.equalsIgnoreCase("StaffAdministrasi")) {
    new DashboardStaffAdministrasi().setVisible(true);
} else if (role.equalsIgnoreCase("Siswa")) {
    new DashboardSiswa().setVisible(true); 
}
dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

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
            java.util.logging.Logger.getLogger(FormSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormSiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormSiswa("StaffAdministrasi").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}

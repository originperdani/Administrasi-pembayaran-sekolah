Download zipnya lalu extract ke dalam 1 folder
Cut Folder tersebut pindahkan ke dalam tempat penyimpanan netbean (biasanya di document/NetBeansProject)
Nyalakan Xampp atau Laragon untuk mengaktifkan database local
import database "db_arion" di phpmyadmin
Buka netbeans lalu open files netbeans yang sudah di download
import libraries mysql-connector (jika ada merah di file java maka klik kanan di file javanya, dan pilih solve problem lalu masukkan libraries mysql-connector)
Masukan Username: sandika Password:123456 (akun staff)
ps:

sebelum run projek, masuk ke form user untuk membuat akun staff
setelah membuat akun staff, klik "kembali". Lalu login dengan akun yang sudah anda buat
jika ingin masuk sebagai role "siswa" dan belum punya hak akses. klik "registrasi" terlebih dahulu
Penting! mengapa tampilan login form setelah di klik "register" terhubung ke form user? karena apabila Siswa belum memiliki akun akses untuk Login maka Siswa harus klik registrasi dan menuju ke Form Users meskipun ke Form Users yang sama dengan akses role staff, Siswa dan Staff memiliki tampilan akses yang berbeda. Jika Staff yang membuka tampilan Form Users, Staff bisa mengakses untuk menambahkan data Siswa dan Staff, mengapa di role staff bisa menambahkan data Siswa juga? supaya jika terdapat masalah pada registrasi Siswa maka Staff bisa memasukan Siswa supaya mendapatkan hak akses. Jika Siswa yang membuka melalui klik register di tampilan login dikarenakan belum memiliki hak akses maka Siswa akan mengarah ke Form user namun ada pembatasan akses yang membuat Siswa tidak bisa menambah data Staff dan juga menghapus datanya.

Selamat Mencoba :)

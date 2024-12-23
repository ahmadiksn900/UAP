Deskripsi

Aplikasi Booking Villa adalah aplikasi desktop berbasis Java yang memungkinkan pengguna untuk melakukan pemesanan villa. Aplikasi ini menyediakan antarmuka pengguna yang intuitif untuk login, memilih villa, memasukkan detail pemesanan, menghitung biaya, dan mengelola pemesanan. Selain itu, aplikasi ini juga menghasilkan dokumen Word untuk menyimpan informasi pemesanan.

Fitur Utama

- Login Pengguna : Autentikasi pengguna dengan username dan password.
- Pemilihan Villa: Pengguna dapat memilih dari beberapa villa yang tersedia.
- Perhitungan Biaya: Menghitung total biaya berdasarkan villa yang dipilih dan durasi menginap.
- Manajemen Pemesanan: Membuat, mengubah status, dan menghapus pemesanan.
- Dokumentasi: Menghasilkan dokumen Word untuk catatan check-in dan check-out.
- Tampilan Gambar Villa: Menampilkan gambar villa yang dipilih.

Teknologi yang Digunakan

- Java: Bahasa pemrograman utama.
- Swing: Untuk antarmuka pengguna grafis (GUI).
- Apache POI: Untuk manipulasi dokumen Word.
- Java Date and Time API: Untuk penanganan tanggal dan waktu.

Prasyarat

Sebelum menjalankan aplikasi, pastikan Anda memiliki:
- Java Development Kit (JDK) terinstal di komputer 
- Apache POI library ditambahkan ke dalam proyek Anda.

Instalasi

- Clone atau Unduh: Clone repositori ini atau unduh kode sumber.
- Gambar Villa: Pastikan Anda memiliki gambar villa yang diperlukan dan simpan di jalur yang sesuai dalam kode.
- Kompilasi dan Jalankan: Kompilasi dan jalankan kelas "BookingVillaApp".

Cara Menggunakan

- Luncurkan Aplikasi: Jalankan aplikasi untuk membuka layar login.
- Login: Masukkan username dan password (default: admin / admin).
- Pilih Villa: Setelah login, pilih villa dari dropdown.
- Masukkan Detail Pemesanan: Isi nama pemesan dan tanggal check-in/check-out.
- Hitung Biaya: Klik tombol "Hitung Biaya" untuk menghitung total biaya pemesanan.
- Pesan Villa: Klik "Pesan Sekarang" untuk menyelesaikan pemesanan.
- Kelola Pemesanan: Ubah status pemesanan atau hapus pemesanan yang sudah check-out.
- Lihat Gambar Villa: Klik "Lihat Gambar Villa" untuk menampilkan gambar villa yang dipilih.

Struktur File

- BookingVillaApp.java: Kelas utama untuk aplikasi.
- LoginFrame.java: Kelas untuk antarmuka login.
- BookingVillaFrame.java: Kelas untuk manajemen pemesanan.
- Data_Checkin_Villa.docx: Dokumen untuk menyimpan data check-in.
- Data_Checkout_Villa.docx: Dokumen untuk menyimpan data check-out.
- Gambar villa harus disimpan di jalur yang ditentukan dalam kode.
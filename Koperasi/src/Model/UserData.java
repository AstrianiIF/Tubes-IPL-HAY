package Model;

public class UserData {
    private String username;
    private String nama;
    private String alamat;
    private String tanggalLahir;

    // Constructor, getter dan setter

    public UserData(String username, String nama, String alamat, String tanggalLahir) {
        this.username = username;
        this.nama = nama;
        this.alamat = alamat;
        this.tanggalLahir = tanggalLahir;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }
}

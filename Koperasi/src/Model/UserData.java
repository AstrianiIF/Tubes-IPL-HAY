package Model;

public class UserData {
    private static UserData instance;
    private String nama;
    private String alamat;
    private String tanggalLahir;

    private UserData() {
        // Private constructor to restrict instantiation
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}

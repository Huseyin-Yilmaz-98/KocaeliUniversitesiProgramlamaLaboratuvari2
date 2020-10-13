package Netflix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLClass {
    public Connection con;
    public Statement stmt;
    public ResultSet rs;
    public boolean girisYapildiMi = false;
    public int id;
    public String isim;
    public String tarih;
    public GirisPaneli girisPaneli;

    SQLClass() throws Exception {
        //giris panelini yarat ve gorunur yap
        this.girisPaneli = new GirisPaneli(this);
        girisPaneli.setVisible(true);
        boolean dosyaBulunduMu;
        try {
            FileReader deneme = new FileReader(new File("SQL/netflix_db.db"));
            deneme.close();
            dosyaBulunduMu = true;
        } catch (Exception deneme) {
            dosyaBulunduMu = false;
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:SQL/netflix_db.db");
            stmt = con.createStatement();
            if (dosyaBulunduMu == false) {
                girisPaneli.girisButonu.setVisible(false);
                girisPaneli.kayitOlButonu.setVisible(false);
                girisPaneli.girisDurum.setText("Database dosyası bulunamadı, oluşturuluyor, bekleyin...");
                girisPaneli.girisDurum.setVisible(true);
                tablolariOlustur();
                girisPaneli.girisDurum.setText("");
                girisPaneli.girisDurum.setVisible(false);
                girisPaneli.girisButonu.setVisible(true);
                girisPaneli.kayitOlButonu.setVisible(true);
            }
        } catch (Exception asd) {
            girisPaneli.girisButonu.setVisible(false);
            girisPaneli.kayitOlButonu.setVisible(false);
            girisPaneli.girisDurum.setText("SQL klasörü bulunamadı veya içinde driver yok!");
            girisPaneli.girisDurum.setVisible(true);
        }
    }

    //belirtilen epostayla kayitli kullanici olup olmadigini dondurur
    public boolean kullaniciVarmi(String eposta) throws Exception {
        rs = stmt.executeQuery(String.format("SELECT * FROM kullanicilar WHERE email='%s';", eposta));
        int boyut = 0;
        while (rs.next()) {
            boyut++;
        }
        if (boyut == 0) {
            return false;
        } else
            return true;
    }

    //tablolari olusturmak icin bir defa calistirildi
    public void tablolariOlustur() throws Exception {
        ArrayList<String> dosyalar = new ArrayList<>();
        dosyalar.add("SQL/programlar.sql");
        dosyalar.add("SQL/turler.sql");
        dosyalar.add("SQL/tur_program.sql");
        dosyalar.add("SQL/kullanicilar.sql");
        dosyalar.add("SQL/kullanici_program.sql");
        BufferedReader dosya = null;
        for (int i = 0; i < dosyalar.size(); i++) {
            dosya = new BufferedReader(new FileReader(new File(dosyalar.get(i))));
            String satir;
            while ((satir = dosya.readLine()) != null) {
                System.out.println(satir);
                stmt.executeUpdate(satir.replace("\n", ""));
            }
        }
    }

    //belirtilen yapim icin izleme kaydini gunceller
    public void izlemeBilgisiGuncelle(int izleme_suresi, int hangi_bolum, int program_id) {
        try {
            rs = stmt.executeQuery("SELECT * FROM  kullanici_program WHERE program_id=" + program_id + " AND user_id=" + id + ";");
            if (rs.next()) {
                stmt.executeUpdate("UPDATE kullanici_program SET izleme_suresi=" + izleme_suresi + ",izleme_tarihi=DATETIME('now','localtime'),hangi_bolum=" + hangi_bolum + " WHERE program_id=" + program_id + " AND user_id=" + id + ";");
            } else {
                stmt.executeUpdate("INSERT INTO kullanici_program(user_id,izleme_tarihi,izleme_suresi,hangi_bolum,puan,program_id) VALUES(" + id + ",CURRENT_TIMESTAMP," + izleme_suresi + "," + hangi_bolum + ",0," + program_id + ");");
            }
        } catch (Exception asada) {
            System.out.println("SQL hata");
        }
    }

    //belirtilen yapimi oylar
    public void oyla(int puan, int program_id) {
        try {
            stmt.executeUpdate("UPDATE kullanici_program SET puan=" + puan + " WHERE program_id=" + program_id + " AND user_id=" + id + ";");
        } catch (Exception asada) {
            System.out.println("SQL hata");
        }
    }

    //giris yaptiran fonksiyon
    public boolean kullaniciGiris(String eposta, String parola) throws Exception {
        if (!kullaniciVarmi(eposta)) {
            girisPaneli.girisDurum.setVisible(false);
            girisPaneli.girisDurum.setText("Eposta bulunamadı!");
            girisPaneli.girisDurum.setVisible(true);
            return false;
        }
        rs = stmt.executeQuery(String.format("SELECT * FROM kullanicilar WHERE email='%s' AND password='%s'", eposta, parola));
        int boyut = 0;
        while (rs.next()) {
            boyut++;
        }
        if (boyut == 0) {
            girisPaneli.girisDurum.setText("Yanlış parola!");
            return false;
        } else {
            rs = stmt.executeQuery(String.format("SELECT * FROM kullanicilar WHERE email='%s' AND password='%s'", eposta, parola));
            rs.next();
            id = rs.getInt("user_id");
            isim = rs.getString("name");
            tarih = rs.getString("birthday").toString();
            girisYapildiMi = true;
            return true;

        }
    }

    //kayit yaptiran fonksiyon
    public boolean kullaniciKayit(String eposta, String parola, String isim, int yil, int ay, int gun) throws Exception {
        if (kullaniciVarmi(eposta)) {
            girisPaneli.kayitDurum.setText("Bu eposta zaten kayıtlı!");
            return false;
        }
        stmt.executeUpdate(String.format("INSERT INTO kullanicilar(email,password,name,birthday) VALUES ('%s','%s','%s','%d-%d-%d');", eposta, parola, isim, yil, ay, gun));
        return true;
    }
}

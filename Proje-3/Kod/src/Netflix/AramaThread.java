package Netflix;

import java.util.ArrayList;

//arama ubugundaki degisiklikleri takip eden classtir, bir degisiklik tespit edilince ona gore film listelemesini degistirir
public class AramaThread extends Thread {
    public String enSonGirdi, anlikGirdi;
    public AnaEkran anaEkran;

    AramaThread(AnaEkran anaEkran) {
        this.anaEkran = anaEkran;
        enSonGirdi = "";
        anlikGirdi = "";

    }

    public void run() {
        while (true) {
            anlikGirdi = anaEkran.aramaYeri.getText();
            if (anlikGirdi.equals(enSonGirdi) == false) {
                try {
                    anaEkran.sqlObje.rs = anaEkran.sqlObje.stmt.executeQuery("SELECT * FROM programlar WHERE name LIKE '%" + anlikGirdi + "%'");
                    ArrayList<String> filmler = new ArrayList<>();
                    while (anaEkran.sqlObje.rs.next()) {
                        filmler.add(anaEkran.sqlObje.rs.getString("name"));
                    }
                    anaEkran.filmListele(filmler);
                    enSonGirdi = anlikGirdi;
                    anaEkran.kategoriKutusu.clearSelection();
                } catch (Exception adb) {
                    continue;
                }
            }
            try {
                sleep(500);
            } catch (Exception eee) {
            }
        }

    }
}

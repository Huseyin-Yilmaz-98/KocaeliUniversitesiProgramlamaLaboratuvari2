package Harita;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Graf {
    //sehirlerin birbiriyle komsuluk iliskisini iceren matris
    boolean[][] komsuluklar = new boolean[82][82];
    //her sehrin objesini tutan liste
    List<Sehir> sehirler = new ArrayList<>();
    String[] sehirAdlari = new String[82];

    Graf() throws Exception {
        //her sehrin listedeki indisi plakasina esit olmasi icin 0. indise bos bir obje atanir
        sehirler.add(new Sehir("bos"));
        sehirAdlariniOku();
        komsuluklariOku();
        komsuMesafeleriOku();
        for (int i = 1; i < 82; i++) {
            mesafeleriHesapla(i);
        }
        sehirKoordinatlariOku();

    }

    //dosyadan sehirlerin harita resmindeki alt ve ust koordinatlarini okuyan fonksiyon
    public void sehirKoordinatlariOku() throws Exception {
        BufferedReader dosya = new BufferedReader(new FileReader(new File("altkoordinatlar.txt")));
        String satir;
        while (true) {
            satir = dosya.readLine();
            if (satir == null) {
                break;
            }
            int plaka = Integer.parseInt(satir.split(",")[0]);
            int x = Integer.parseInt(satir.split(",")[1]);
            int y = Integer.parseInt(satir.split(",")[2]);
            sehirler.get(plaka).altkoordinatlar[0] = x;
            sehirler.get(plaka).altkoordinatlar[1] = y;

        }
        dosya.close();
        dosya = new BufferedReader(new FileReader(new File("ustkoordinatlar.txt")));
        while (true) {
            satir = dosya.readLine();
            if (satir == null) {
                break;
            }
            int plaka = Integer.parseInt(satir.split(",")[0]);
            int x = Integer.parseInt(satir.split(",")[1]);
            int y = Integer.parseInt(satir.split(",")[2]);
            sehirler.get(plaka).ustkoordinatlar[0] = x;
            sehirler.get(plaka).ustkoordinatlar[1] = y;

        }
        dosya.close();
    }

    //Dosyadan her plakanin sehir adini okuyan fonksiyon
    public void sehirAdlariniOku() throws Exception {
        BufferedReader dosya = new BufferedReader(new FileReader(new File("sehir.txt")));
        while (true) {
            String satir = dosya.readLine();
            if (satir == null) break;
            String[] bolunmus = satir.split(",");
            sehirAdlari[Integer.parseInt(bolunmus[0])] = bolunmus[1];
            sehirler.add(new Sehir(bolunmus[1]));
        }
    }

    //sehir adi alip plakasini donduren fonksiyon
    public int plakaDondur(String sehirAdi) {
        for (int i = 1; i < 82; i++) {
            if (sehirAdi.equals(sehirAdlari[i])) {
                return i;
            }
        }
        return -1;
    }

    //dosyadan sehirlerin komsuluk durumu okuyup matrise atayan fonksiyon
    public void komsuluklariOku() throws Exception {
        for (int i = 0; i < 82; i++) {
            for (int y = 0; y < 82; y++) {
                komsuluklar[i][y] = false;
            }

        }
        BufferedReader dosya = new BufferedReader(new FileReader(new File("sehir.txt")));
        while (true) {
            String satir = dosya.readLine();
            if (satir == null) {
                break;
            }
            String[] bolunmus = satir.split(",");
            int plaka = Integer.parseInt(bolunmus[0]);
            for (int i = 2; i < bolunmus.length; i++) {
                int komsuplaka = plakaDondur(bolunmus[i]);
                komsuluklar[plaka][komsuplaka] = true;
                sehirler.get(plaka).komsular.add(komsuplaka);

            }
        }
    }

    //dosyadan her sehrin komsulariyla arasindaki mesafeyi okuyan fonksiyon
    public void komsuMesafeleriOku() throws Exception {
        BufferedReader dosya = new BufferedReader(new FileReader(new File("mesafeler.txt")));
        while (true) {
            String satir = dosya.readLine();
            if (satir == null) {
                break;
            }
            String[] bolunmus = satir.split(",");
            int plaka = Integer.parseInt(bolunmus[0]);
            for (int i = 1; i < bolunmus.length; i++) {
                sehirler.get(plaka).komsuMesafeler.add(Integer.parseInt(bolunmus[i]));

            }
        }

    }

    //81 ilin de birbiriyle en kisa mesafesini hesaplayan fonksiyon
    public void mesafeleriHesapla(int plaka) {
        boolean[] gecilmis = new boolean[82];
        List<Integer> gecilmemis = new ArrayList<>();
        int anlikSehir = plaka;

        for (int i = 1; i < 82; i++) {
            sehirler.get(plaka).mesafeler.put(i, 100000);
            sehirler.get(plaka).guzergahlar.put(i, new ArrayList<>());
            gecilmis[i] = false;
        }
        sehirler.get(plaka).mesafeler.replace(plaka, 0);

        while (true) {

            int anlikMesafe = sehirler.get(plaka).mesafeler.get(anlikSehir);
            for (int i = 0; i < sehirler.get(anlikSehir).komsular.size(); i++) {
                int kiyaslananSehir = sehirler.get(anlikSehir).komsular.get(i);
                int anliklaKiyaslananArasindakiMesafe = sehirler.get(anlikSehir).komsuMesafeler.get(i);
                int anaSehirleKiyaslananArasindakiMesafe = sehirler.get(plaka).mesafeler.get(kiyaslananSehir);
                if (anlikMesafe + anliklaKiyaslananArasindakiMesafe < anaSehirleKiyaslananArasindakiMesafe) {
                    sehirler.get(plaka).mesafeler.replace(kiyaslananSehir, anlikMesafe + anliklaKiyaslananArasindakiMesafe);
                    List<Integer> yeniGuzergah = listeKopyala(sehirler.get(plaka).guzergahlar.get(anlikSehir));
                    yeniGuzergah.add(kiyaslananSehir);
                    sehirler.get(plaka).guzergahlar.replace(kiyaslananSehir, yeniGuzergah);
                }
            }
            gecilmis[anlikSehir] = true;
            gecilmemis.clear();
            for (int i = 1; i < 82; i++) {
                if (gecilmis[i] == false) {
                    gecilmemis.add(i);
                }
            }
            if (gecilmemis.size() == 0) {
                break;
            }
            int enKucuk = sehirler.get(plaka).mesafeler.get(gecilmemis.get(0));
            int enYakinPlaka = gecilmemis.get(0);
            for (int i = 0; i < gecilmemis.size(); i++) {
                if (sehirler.get(plaka).mesafeler.get(gecilmemis.get(i)) < enKucuk) {
                    enKucuk = sehirler.get(plaka).mesafeler.get(gecilmemis.get(i));
                    enYakinPlaka = gecilmemis.get(i);

                }
            }
            anlikSehir = enYakinPlaka;
        }
    }

    //integer listesinin kopyasini donduren fonksiyon
    public List<Integer> listeKopyala(List<Integer> alinan) {
        List<Integer> donecek = new ArrayList<>();
        for (int i = 0; i < alinan.size(); i++) {
            donecek.add(new Integer(alinan.get(i)));
        }
        return donecek;
    }

    //bir rota alip toplam mesafesini donduren fonksiyon
    int rotaMesafe(String rota) {
        String[] bolunmus = rota.split(",");
        int toplam = 0;
        for (int i = 0; i < bolunmus.length - 1; i++) {
            toplam += sehirler.get(Integer.parseInt(bolunmus[i])).mesafeler.get(Integer.parseInt(bolunmus[i + 1]));
        }
        return toplam;
    }


}


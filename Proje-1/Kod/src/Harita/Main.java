package Harita;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public Graf graf;
    public SecimEkrani secimEkrani;
    public HaritaEkrani haritaEkrani;
    public List<String> enKisaRotalar = new ArrayList<>();

    Main() throws Exception {
        graf = new Graf();
        secimEkrani = new SecimEkrani(this);
    }

    public static void main(String[] args) throws Exception {
        Main obje = new Main();
        obje.secimEkrani.setVisible(true);
    }

    //SecimEkrani objesinde sehir secimleri tamamlandiktan sonra bu fonksiyon cagirilir, en kisa rotalari hesaplayip harita ekranini cagirir.
    public void baslatma(DefaultListModel secilmisler) throws Exception {
        List<Integer> gidilecekler = new ArrayList<>();
        System.out.print("Teslimat sehirleri : ");
        //gidilecek sehirler listesini plaka listesine donustur ve sehir adlarini yazdir
        for (int i = 0; i < secilmisler.size(); i++) {
            System.out.print((i + 1) + ". " + secilmisler.get(i) + " ");
            gidilecekler.add(graf.plakaDondur(secilmisler.get(i).toString()));
        }
        System.out.println();
        //tum muhtemel rotalari hesapla
        List<String> rotalar = muhtemelYollariHesapla(gidilecekler);
        //her rotanin mesafesini hesapla
        List<Integer> rotaMesafeler = rotaMesafeleriHesapla(rotalar);
        //en kisa rotalari bul
        enKisaRotalar = enKisaRotalariHesapla(enKisaRotalar, rotalar, rotaMesafeler);
        //harita ekranini goster
        haritaEkrani = new HaritaEkrani(this);
        haritaEkrani.setVisible(true);
    }

    //sehir listesini alir ve tum muhtemel siralari liste olarak dondurur
    public static List<String> muhtemelYollariHesapla(List<Integer> gidilecekler) {
        List<String> onceki = new ArrayList<>();
        List<String> son = new ArrayList<>();
        son.add("41," + gidilecekler.get(0) + ",41");

        //bu algoritmada mantik, o anda isleme alinan sehri onceki muhtemel rotalar listesinde her araya koyarak yeni liste yaratmaktir
        //ornegin o andaki tek rota Kocaeli-Istanbul-Kocaeli ise Adana sehri iki araya da konur ve yeni listede Kocaeli-Adana-Istanbul-Kocaeli ve Kocaeli-Istanbul-Adana-Kocaeli kombinasyonlari olusur
        for (int i = 1; i < gidilecekler.size(); i++) {
            onceki = listeKopyala(son);
            son.clear();
            int eklenecek = gidilecekler.get(i);
            for (int y = 0; y < onceki.size(); y++) {
                String[] duzenlenecek = onceki.get(y).split(",");

                for (int j = 1; j < duzenlenecek.length; j++) {
                    String yeniEleman = "";
                    for (int a = 0; a < j; a++) {
                        yeniEleman += (duzenlenecek[a] + ",");
                    }
                    yeniEleman += eklenecek;
                    for (int b = j; b < duzenlenecek.length; b++) {
                        yeniEleman += ("," + duzenlenecek[b]);
                    }
                    son.add(yeniEleman);

                }
            }
        }
        return son;
    }

    //bir string listesi alinir ve kopyasi dondurulur
    public static List<String> listeKopyala(List<String> alinan) {
        List<String> donecek = new ArrayList<>();
        for (int i = 0; i < alinan.size(); i++) {
            donecek.add(alinan.get(i));
        }
        return donecek;
    }

    //rotalar listesi alir ve her rotanin toplam mesafesini dondurur
    public List<Integer> rotaMesafeleriHesapla(List<String> rotalar) {
        List<Integer> rotaMesafeler = new ArrayList<>();
        for (int i = 0; i < rotalar.size(); i++) {
            int toplam = 0;
            String[] rota = rotalar.get(i).split(",");
            for (int j = 0; j < rota.length - 1; j++) {
                toplam += graf.sehirler.get(Integer.parseInt(rota[j])).mesafeler.get(Integer.parseInt(rota[j + 1]));

            }
            rotaMesafeler.add(toplam);
        }
        return rotaMesafeler;
    }

    //en kisa 5 rotayi dondurur
    public List<String> enKisaRotalariHesapla(List<String> enKisaRotalar, List<String> rotalar, List<Integer> rotaMesafeler) {
        while (enKisaRotalar.size() < 5) {
            if (rotalar.size() == 0) {
                break;
            }
            int enKisa = 100000;
            String enKisaRota = "";
            for (int i = 0; i < rotaMesafeler.size(); i++) {
                if (rotaMesafeler.get(i) < enKisa) {
                    enKisa = rotaMesafeler.get(i);
                    enKisaRota = rotalar.get(i);
                }
            }
            if (!enKisaRotalar.contains(enKisaRota))
                enKisaRotalar.add(enKisaRota);
            rotalar.remove(rotaMesafeler.indexOf(enKisa));
            rotaMesafeler.remove(rotaMesafeler.indexOf(enKisa));
        }
        return enKisaRotalar;
    }
}


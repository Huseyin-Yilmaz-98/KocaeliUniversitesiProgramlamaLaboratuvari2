package Harita;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaritaEkrani extends JFrame {
    Main obje;
    JLabel haritaResim = new JLabel();
    ImageIcon harita;
    List<List<Arrow>> okListeleri = new ArrayList<>();

    HaritaEkrani(Main obje) throws Exception {
        this.obje = obje;
        harita = new ImageIcon(ImageIO.read(new File("harita2.png")));
        setSize(1215, 655);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //kac tane rota varsa o kadar buton olustur
        for (int i = 0; i < obje.enKisaRotalar.size(); i++) {
            butonOLustur(i);
            System.out.print((i + 1) + ". Rota: ");
            okListeleri.add(oklariOlustur(obje.enKisaRotalar.get(i)));
        }
        //ilk rotanin oklarini cizdir
        oklariGoster(0);
        haritaResim.setIcon(harita);
        haritaResim.setBounds(0, 41, 1200, 613);
        add(haritaResim);
    }

    //her rota icin bir buton olusturur, parametre olarak rotanin indisini alir ve her butona tiklandiginda o rota icin oklar cizdirilir
    public JButton butonOLustur(int rotaIndisi) {
        JButton buton = new JButton((rotaIndisi + 1) + ". Rota (" + obje.graf.rotaMesafe(obje.enKisaRotalar.get(rotaIndisi)) + " km)");
        buton.setBounds(rotaIndisi * 240, 0, 240, 40);
        buton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oklariGoster(rotaIndisi);
            }
        });
        add(buton);
        return buton;
    }

    //parametre olarak bir rota indisi alir ve o indisteki rotanin oklarini cizdirir
    public void oklariGoster(int indis) {
        for (int i = 0; i < okListeleri.size(); i++) {
            for (int j = 0; j < okListeleri.get(i).size(); j++) {
                if (i == indis) {
                    okListeleri.get(i).get(j).setVisible(true);

                } else {
                    okListeleri.get(i).get(j).setVisible(false);
                }
            }
        }
    }

    /*parametre olarak bir rota alir ve oklari ok listesi dondurur
    oklar cizilirken bir sehirden her geciste okun giris cikisinin alt ve ust sirasi degistirilerek oklarin cakismasi onlenmeye calisildi*/
    public List<Arrow> oklariOlustur(String rota) {
        List<Arrow> oklar = new ArrayList<>();
        String[] anaGuzergah = rota.split(",");
        List<Integer> tamGuzergah = new ArrayList<>();
        tamGuzergah.add(41);
        for (int i = 0; i < anaGuzergah.length - 1; i++) {
            List<Integer> anlikGuzergah = obje.graf.sehirler.get(Integer.parseInt(anaGuzergah[i])).guzergahlar.get(Integer.parseInt(anaGuzergah[i + 1]));
            for (int j = 0; j < anlikGuzergah.size(); j++) {
                tamGuzergah.add(anlikGuzergah.get(j));
            }
        }
        Map<Integer, Boolean> gecildiMi = new HashMap();
        for (int i = 1; i < 82; i++) {
            gecildiMi.put(i, false);
        }
        for (int i = 0; i < tamGuzergah.size(); i++) {
            System.out.print(obje.graf.sehirAdlari[tamGuzergah.get(i)] + ",");
        }
        System.out.println();
        for (int i = 0; i < tamGuzergah.size() - 1; i++) {
            Sehir baslangic = obje.graf.sehirler.get(tamGuzergah.get(i));
            Sehir hedef = obje.graf.sehirler.get(tamGuzergah.get(i + 1));
            //Cikis sehrinden ilk defa cikis oluyorsa ok yukaridan cikar, giris sihrine ilk defa giris oluyorsa da ok yukaridan girer
            if (gecildiMi.get(tamGuzergah.get(i)) == false) {
                if (gecildiMi.get(tamGuzergah.get(i + 1))) {
                    oklar.add(okYarat(baslangic.ustkoordinatlar[0], baslangic.ustkoordinatlar[1], hedef.altkoordinatlar[0], hedef.altkoordinatlar[1]));
                } else {
                    oklar.add(okYarat(baslangic.ustkoordinatlar[0], baslangic.ustkoordinatlar[1], hedef.ustkoordinatlar[0], hedef.ustkoordinatlar[1]));
                }
                gecildiMi.replace(tamGuzergah.get(i), true);
            } else {
                if (gecildiMi.get(tamGuzergah.get(i + 1))) {
                    oklar.add(okYarat(baslangic.altkoordinatlar[0], baslangic.altkoordinatlar[1], hedef.altkoordinatlar[0], hedef.altkoordinatlar[1]));
                } else {
                    oklar.add(okYarat(baslangic.altkoordinatlar[0], baslangic.altkoordinatlar[1], hedef.ustkoordinatlar[0], hedef.ustkoordinatlar[1]));
                }
                gecildiMi.replace(tamGuzergah.get(i), false);
            }
        }
        return oklar;
    }

    //verilen koordinatlarla bir ok objesi yaratir
    public Arrow okYarat(int x1, int y1, int x2, int y2) {
        Arrow ok = new Arrow(x1, y1, x2, y2);
        ok.setVisible(false);
        add(ok);
        return ok;
    }
}

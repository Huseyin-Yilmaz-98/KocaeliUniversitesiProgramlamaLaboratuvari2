package Netflix;


import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AnaEkran extends JFrame {
    public JPanel profilPanel;
    public JPanel kategoriPanel;
    public JTextField aramaYeri;
    public JPanel ortaPanel;
    public SQLClass sqlObje;
    public JLabel profilFoto;
    public JLabel bilgiler;
    public DefaultListModel<String> kategoriList = new DefaultListModel<>();
    public JList<String> kategoriKutusu;
    public JScrollPane kategoriScroll;
    public JScrollPane scroll;
    public boolean oynuyorMu;
    public boolean butonlariGoster;


    AnaEkran(SQLClass sqlObje) throws Exception {
        oynuyorMu = false;
        butonlariGoster = true;
        this.sqlObje = sqlObje;
        getContentPane().setBackground(new Color(0, 113, 188));
        setSize(1185, 708);
        setLocationRelativeTo(null);

        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        profilPanel = new JPanel();
        profilPanel.setBounds(10, 10, 211, 111);
        profilPanel.setBorder(BorderFactory.createEmptyBorder());
        profilPanel.setOpaque(true);
        profilPanel.setBackground(new Color(27, 73, 111));
        profilPanel.setLayout(null);
        add(profilPanel);

        kategoriPanel = new JPanel();
        kategoriPanel.setBounds(10, 130, 211, 531);
        kategoriPanel.setBorder(BorderFactory.createEmptyBorder());
        kategoriPanel.setOpaque(true);
        kategoriPanel.setLayout(null);
        kategoriPanel.setBackground(new Color(27, 73, 111));
        add(kategoriPanel);
        aramaYeri = new JTextField();
        aramaYeri.setBounds(470, 20, 511, 30);
        aramaYeri.setFont(new Font("Acre", Font.BOLD, 15));
        aramaYeri.setForeground(new Color(0, 113, 188));
        add(aramaYeri);
        JLabel aramaYazi = new JLabel("Ara:");
        aramaYazi.setBounds(420, 20, 49, 30);
        aramaYazi.setForeground(Color.WHITE);
        aramaYazi.setFont(new Font("Acre", Font.BOLD, 16));
        add(aramaYazi);
        ortaPanel = new JPanel();
        ortaPanel.setBorder(BorderFactory.createEmptyBorder());
        ortaPanel.setOpaque(true);
        ortaPanel.setBackground(new Color(27, 73, 111));
        scroll = new JScrollPane(ortaPanel);
        scroll.setBounds(270, 70, 860, 590);
        scroll.getVerticalScrollBar().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setOpaque(true);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.lightGray;
            }
        });
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(true);
        add(scroll);

        ImageIcon pp = new ImageIcon("pp.jpg");

        profilFoto = new JLabel();
        profilFoto.setBounds(10, 13, 80, 80);
        profilFoto.setIcon(pp);
        profilPanel.add(profilFoto);

        bilgiler = new JLabel("<html>" + sqlObje.isim + "<br/>" + sqlObje.tarih + "</html>", SwingConstants.CENTER);
        bilgiler.setBounds(93, 10, 115, 100);
        bilgiler.setForeground(Color.WHITE);
        bilgiler.setFont(new Font("Acre", Font.BOLD, 16));
        profilPanel.add(bilgiler);

        kategoriList.addElement("Hepsi");
        sqlObje.rs = sqlObje.stmt.executeQuery("SELECT name FROM turler;");
        while (sqlObje.rs.next()) {
            kategoriList.addElement(sqlObje.rs.getString("name"));
        }

        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 1) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        String arama = o.toString();
                        if (o.toString() == "Hepsi") {
                            try {
                                sqlObje.rs = sqlObje.stmt.executeQuery("SELECT * FROM programlar");
                                ArrayList<String> filmler = new ArrayList<>();
                                while (sqlObje.rs.next()) {
                                    filmler.add(sqlObje.rs.getString("name"));
                                }
                                filmListele(filmler);
                            } catch (Exception adb) {
                            }
                            return;
                        }
                        try {
                            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT programlar.name AS Ad, turler.name AS Tur FROM  tur_program  INNER JOIN  programlar ON programlar.program_id=tur_program.program_id INNER JOIN turler ON turler.tur_id=tur_program.tur_id WHERE turler.name='" + arama + "';");
                            ArrayList<String> yapimAdlari = new ArrayList<>();
                            while (sqlObje.rs.next()) {
                                yapimAdlari.add(sqlObje.rs.getString("Ad"));
                            }
                            filmListele(yapimAdlari);
                        } catch (Exception adb) {
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent mouseEvent) {
                mousePressed(mouseEvent);
            }
        };

        kategoriKutusu = new JList<>(kategoriList);
        kategoriKutusu.addMouseListener(mouseListener);
        kategoriKutusu.setBackground(new Color(27, 73, 111));
        kategoriKutusu.setForeground(Color.WHITE);
        kategoriKutusu.setFont(new Font("Acre", Font.BOLD, 12));
        kategoriKutusu.setBorder(BorderFactory.createEmptyBorder());
        kategoriKutusu.setOpaque(true);
        kategoriKutusu.setSelectionBackground(Color.lightGray);
        kategoriKutusu.setSelectionForeground(Color.BLACK);

        kategoriScroll = new JScrollPane();
        kategoriScroll.setViewportView(kategoriKutusu);
        kategoriScroll.setBounds(8, 0, 211, 531);
        kategoriScroll.setBackground(Color.BLACK);
        kategoriScroll.setBorder(BorderFactory.createEmptyBorder());
        kategoriScroll.setOpaque(true);
        kategoriScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        kategoriPanel.add(kategoriScroll);
        AramaThread thread = new AramaThread(this);
        thread.start();
        try {
            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT * FROM programlar");
            ArrayList<String> filmler = new ArrayList<>();
            while (sqlObje.rs.next()) {
                filmler.add(sqlObje.rs.getString("name"));
            }
            filmListele(filmler);
        } catch (Exception adb) {
        }
        setVisible(true);
    }

    //ekrana filmleri listeleyen fonksiyon
    public void filmListele(ArrayList<String> filmler) {
        butonlariGoster = true;
        oynuyorMu = false;
        scroll.setVisible(false);
        ortaPanel.removeAll();
        GridBagLayout gridbag = new GridBagLayout();
        ortaPanel.setLayout(gridbag);
        int sayac = 0;
        int y = 0;
        int i;
        for (i = 0; i < filmler.size(); i++) {
            JPanel film = new JPanel();
            film.setBorder(BorderFactory.createEmptyBorder());
            film.setOpaque(true);
            film.setBackground(new Color(27, 73, 111));
            film.setLayout(null);
            ImageIcon afis;
            try {
                afis = new ImageIcon("Posters/" + filmler.get(i).replace(":", "") + ".jpg");
            } catch (Exception as) {
                afis = new ImageIcon("Posters/noposter.jpg");
            }
            JLabel afisLabel = new JLabel();
            afisLabel.setBounds(15, 3, 130, 200);
            afisLabel.setIcon(afis);
            film.add(afisLabel);
            JLabel filmAdi = new JLabel(filmler.get(i), SwingConstants.CENTER);
            filmAdi.setBounds(0, 205, 160, 25);
            filmAdi.setForeground(Color.WHITE);
            filmAdi.setFont(new Font("Acre", Font.BOLD, 12));
            film.add(filmAdi);
            film.setVisible(true);
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1.0;
            c.gridx = sayac;
            c.gridy = y;
            film.setPreferredSize(new Dimension(160, 230));
            c.insets = new Insets(10, 5, 5, 10);
            gridbag.setConstraints(film, c);
            String ad = filmler.get(i);
            film.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    filmGoster(ad);
                }

            });
            ortaPanel.add(film);
            if (sayac < 3) {
                sayac++;
            } else {
                sayac = 0;
                y++;
            }
        }

        //8den az film listelenmisse hiza bozulmasin diye bos afisler eklenir
        for (; i < 8; i++) {
            JPanel film = new JPanel();
            film.setBorder(BorderFactory.createEmptyBorder());
            film.setOpaque(true);
            film.setBackground(new Color(27, 73, 111));
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1.0;
            c.gridx = sayac;
            c.gridy = y;
            film.setPreferredSize(new Dimension(160, 230));
            c.insets = new Insets(10, 5, 5, 10);
            gridbag.setConstraints(film, c);
            ortaPanel.add(film);
            if (sayac < 3) {
                sayac++;
            } else {
                sayac = 0;
                y++;
            }
        }
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setVisible(true);
    }

    //belirtilen addaki filmi ekrana getirir
    public void filmGoster(String filmAdi) {
        scroll.setVisible(false);
        ortaPanel.removeAll();
        ImageIcon afis;
        boolean izlenmisMi = false;
        String izlenmeTarihi = "";
        int izlemeSuresi = 0;
        int hangiBolum = 1;
        int puan = 0;
        float ortalamaPuan = 0;
        int id = 1;
        int bolumSayisi = 1;
        int sure = 0;
        int oySayisi = 0;
        ArrayList<String> turler = new ArrayList<>();
        String tip = "";
        try {
            afis = new ImageIcon("Posters/" + filmAdi.replace(":", "") + ".jpg");
        } catch (Exception as) {
            afis = new ImageIcon("Posters/noposter.jpg");
        }
        ortaPanel.setLayout(null);
        JLabel afisLabel = new JLabel(afis);
        afisLabel.setBounds(10, 10, 130, 200);
        ortaPanel.add(afisLabel);
        try {
            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT * FROM programlar WHERE name='" + filmAdi + "';");
            sqlObje.rs.next();
            id = sqlObje.rs.getInt("program_id");
            bolumSayisi = sqlObje.rs.getInt("bolum_sayisi");
            sure = sqlObje.rs.getInt("sure");
            tip = sqlObje.rs.getString("tip");
            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT programlar.name AS Ad, turler.name AS Tur FROM  tur_program  INNER JOIN  programlar ON programlar.program_id=tur_program.program_id INNER JOIN turler ON turler.tur_id=tur_program.tur_id WHERE programlar.program_id=" + id + ";");
            while (sqlObje.rs.next()) {
                turler.add(sqlObje.rs.getString("Tur"));
            }
            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT * FROM kullanici_program WHERE user_id=" + sqlObje.id + " AND program_id=" + id + ";");

            if (sqlObje.rs.next()) {
                izlenmisMi = true;
                izlenmeTarihi = sqlObje.rs.getString("izleme_tarihi").toString();
                izlemeSuresi = sqlObje.rs.getInt("izleme_suresi");
                hangiBolum = sqlObje.rs.getInt("hangi_bolum");
                puan = sqlObje.rs.getInt("puan");
            } else {
                izlenmisMi = false;
            }
            sqlObje.rs = sqlObje.stmt.executeQuery("SELECT SUM(puan) AS puanlar, COUNT(puan) AS puan_sayisi FROM kullanici_program WHERE program_id=" + id + " AND puan<>0;");
            sqlObje.rs.next();

            oySayisi = sqlObje.rs.getInt("puan_sayisi");
            if (oySayisi == 0) {
                ortalamaPuan = 0;

            } else {
                ortalamaPuan = (float) sqlObje.rs.getInt("puanlar") / (float) oySayisi;
            }
        } catch (Exception asd) {
        }
        ortaPanel.add(bilgiLabelDondur(160, 10, 500, 20, "Film Adı: " + filmAdi));
        String turAdlari = "";
        for (int i = 0; i < turler.size(); i++) {
            turAdlari += turler.get(i);
            if (i + 1 != turler.size()) {
                turAdlari += ", ";
            }
        }
        ortaPanel.add(bilgiLabelDondur(160, 35, 800, 20, "Kategori: " + turAdlari));
        int id2 = id;
        int sure2 = sure;
        if (tip.equals("Dizi")) {
            ortaPanel.add(bilgiLabelDondur(160, 60, 500, 20, "Tür: " + "Dizi (" + bolumSayisi + " Bölüm)"));
        } else {
            ortaPanel.add(bilgiLabelDondur(160, 60, 500, 20, "Tür: " + tip));
        }
        ortaPanel.add(bilgiLabelDondur(160, 85, 500, 20, "Süre: " + sure + " Dakika"));
        ortaPanel.add(bilgiLabelDondur(160, 110, 500, 20, "Ortalama Puan: " + ortalamaPuan + " (Toplam " + oySayisi + " oy)"));
        String[] bolumListesi = new String[bolumSayisi];
        for (int i = 0; i < bolumSayisi; i++) {
            bolumListesi[i] = "" + (i + 1) + ". Bölüm";
        }
        JComboBox<String> bolumler = new JComboBox<>(bolumListesi);
        if (tip.equals("Dizi")) {
            bolumler.setBounds(200, 280, 90, 50);
            ortaPanel.add(bolumler);
        }
        if (izlenmisMi) {
            ortaPanel.add(bilgiLabelDondur(160, 135, 500, 20, "Son İzleme Tarihi: " + izlenmeTarihi));
            if (tip.equals("Dizi")) {
                ortaPanel.add(bilgiLabelDondur(160, 185, 500, 20, "En Son İzlenen Bölüm: " + hangiBolum));
                if (izlemeSuresi < sure) {
                    ortaPanel.add(bilgiLabelDondur(160, 160, 500, 20, izlemeSuresi + ". dakikaya kadar izlendi."));
                } else {
                    ortaPanel.add(bilgiLabelDondur(160, 160, 500, 20, "Bölüm bitmiş."));
                }
                if (puan > 0)
                    ortaPanel.add(bilgiLabelDondur(160, 210, 300, 20, "Verilen Puan: " + puan));
                else
                    ortaPanel.add(bilgiLabelDondur(160, 210, 300, 20, "Puan verilmedi."));
                String[] puanListesi = new String[10];
                for (int i = 0; i < 10; i++) {
                    puanListesi[i] = "" + (i + 1);
                }
                JComboBox<String> puanlar = new JComboBox<>(puanListesi);
                puanlar.setBounds(300, 210, 70, 25);
                ortaPanel.add(puanlar);
                JButton puanVer = buttonDondur(380, 210, 70, 25, "Oyla", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        sqlObje.oyla(puanlar.getSelectedIndex() + 1, id2);
                        filmGoster(filmAdi);
                    }
                });
                int anlikSure2 = izlemeSuresi;
                int hangiBolum2 = hangiBolum;
                JButton bastanOynatButon = buttonDondur(310, 280, 120, 50, "Baştan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        oynat(alinanId, alinanSure, bolumler.getSelectedIndex() + 1, 0, filmAdi);
                    }
                });
                JButton devamEtButon = buttonDondur(440, 280, 250, 50, hangiBolum + ". Bölüm " + anlikSure2 + ". Dakikadan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        oynat(alinanId, alinanSure, hangiBolum2, anlikSure2, filmAdi);
                    }
                });
                if (butonlariGoster == false) {
                    devamEtButon.setEnabled(false);
                    bastanOynatButon.setEnabled(false);
                    puanVer.setEnabled(false);
                }
                if (sure == anlikSure2) {
                    devamEtButon.setEnabled(false);
                    devamEtButon.setText(hangiBolum + ". bölüm bitmiş.");
                }
            } else {
                if (izlemeSuresi < sure) {
                    ortaPanel.add(bilgiLabelDondur(160, 160, 500, 20, izlemeSuresi + ". dakikaya kadar izlendi."));
                } else {
                    ortaPanel.add(bilgiLabelDondur(160, 160, 500, 20, "Film sonuna kadar izlenmiş."));
                }
                if (puan > 0)
                    ortaPanel.add(bilgiLabelDondur(160, 185, 500, 20, "Verilen Puan: " + puan));
                else
                    ortaPanel.add(bilgiLabelDondur(160, 185, 500, 20, "Puan verilmedi."));
                String[] puanListesi = new String[10];
                for (int i = 0; i < 10; i++) {
                    puanListesi[i] = "" + (i + 1);
                }
                JComboBox<String> puanlar = new JComboBox<>(puanListesi);
                puanlar.setBounds(300, 185, 70, 25);
                ortaPanel.add(puanlar);
                JButton puanVer = buttonDondur(380, 185, 70, 25, "Oyla", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        sqlObje.oyla(puanlar.getSelectedIndex() + 1, id2);
                        filmGoster(filmAdi);
                    }
                });
                JButton bastanOynatButon = buttonDondur(260, 280, 115, 50, "Baştan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        oynat(alinanId, alinanSure, filmAdi);
                    }
                });
                int anlikSure2 = izlemeSuresi;
                JButton devamEtButon = buttonDondur(410, 280, 170, 50, anlikSure2 + ". Dakikadan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        int anlikSure = anlikSure2;
                        oynat(alinanId, alinanSure, 1, anlikSure, filmAdi);
                    }
                });
                if (butonlariGoster == false) {
                    devamEtButon.setEnabled(false);
                    bastanOynatButon.setEnabled(false);
                    puanVer.setEnabled(false);
                }
                if (sure == anlikSure2) {
                    devamEtButon.setEnabled(false);
                    devamEtButon.setText("Film bitmiş.");
                }
            }
        } else {
            ortaPanel.add(bilgiLabelDondur(160, 135, 500, 20, "Puan vermek için yapımı izlemelisiniz."));
            if (tip.equals("Dizi")) {
                JButton bastanOynatButon = buttonDondur(310, 280, 120, 50, "Baştan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        oynat(alinanId, alinanSure, bolumler.getSelectedIndex() + 1, 0, filmAdi);
                    }
                });
                if (butonlariGoster == false) {
                    bastanOynatButon.setEnabled(false);
                }
            } else {
                JButton bastanOynatButon = buttonDondur(260, 280, 115, 50, "Baştan Oynat", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oynuyorMu = false;
                        butonlariGoster = false;
                        filmGoster(filmAdi);
                        int alinanId = id2;
                        int alinanSure = sure2;
                        oynat(alinanId, alinanSure, filmAdi);
                    }
                });
                if (butonlariGoster == false) {
                    bastanOynatButon.setEnabled(false);
                }
            }
        }
        scroll.setVisible(true);

    }

    //belirtilen yapimi oynatmaya baslar
    public void oynat(int filmId, int toplamSure, int bolum, int kalinanDakika, String filmAdi) {
        oynuyorMu = true;
        ortaPanel.setVisible(false);
        JButton durdur = buttonDondur(370, 420, 100, 30, "Durdur", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oynuyorMu = false;
                butonlariGoster = true;
                filmGoster(filmAdi);
            }
        });
        JProgressBar bar = new JProgressBar(0, toplamSure);
        bar.setBounds(250, 360, 340, 40);
        bar.setStringPainted(true);
        ortaPanel.add(bar);
        bar.setValue(1);
        bar.setVisible(true);
        bar.setValue(kalinanDakika);
        ortaPanel.setVisible(true);
        Thread oynatici = new Thread() {
            @Override
            public void run() {
                int anlikSure = kalinanDakika;
                while (true) {
                    if (oynuyorMu == false) {
                        bar.setVisible(false);
                        ortaPanel.remove(bar);
                        butonlariGoster = true;
                        break;
                    }
                    if (anlikSure == toplamSure) {
                        butonlariGoster = true;
                        bar.setValue(anlikSure);
                        sqlObje.izlemeBilgisiGuncelle(anlikSure, bolum, filmId);
                        bar.setString("Bitti.");
                        filmGoster(filmAdi);
                        break;
                    }
                    sqlObje.izlemeBilgisiGuncelle(anlikSure, bolum, filmId);
                    bar.setValue(anlikSure);
                    if (bolum == 1) {
                        bar.setString(anlikSure + " / " + toplamSure);
                    } else {
                        bar.setString(bolum + ". Bölüm: " + anlikSure + " / " + toplamSure);
                    }
                    anlikSure++;
                    try {
                        Thread.sleep(175);
                    } catch (Exception eee) {
                    }
                }
            }
        };
        oynatici.start();
    }

    public void oynat(int filmId, int toplamSure, int bolum, String filmAdi) {
        oynat(filmId, toplamSure, bolum, 0, filmAdi);
    }

    public void oynat(int filmId, int toplamSure, String filmAdi) {
        oynat(filmId, toplamSure, 1, 0, filmAdi);
    }

    //Belirtilen boyut ve koordinatlarda bir buton olusturur
    public JButton buttonDondur(int x, int y, int genislik, int uzunluk, String yazi, ActionListener listener) {
        JButton donecek = new JButton(yazi);
        donecek.setBounds(x, y, genislik, uzunluk);
        donecek.addActionListener(listener);
        donecek.setFocusPainted(false);
        donecek.setBorder(BorderFactory.createEmptyBorder());
        donecek.setOpaque(true);
        donecek.setForeground(new Color(27, 73, 111));
        donecek.setBackground(Color.WHITE);
        donecek.setFont(new Font("Acre", Font.BOLD, 15));
        ortaPanel.add(donecek);
        donecek.setVisible(true);
        return donecek;
    }

    //Belirtilen boyut ve koordinatlarda bir label dondurur
    public JLabel bilgiLabelDondur(int x, int y, int w, int h, String yazi) {
        JLabel donecek = new JLabel(yazi);
        donecek.setBounds(x, y, w, h);
        donecek.setForeground(Color.WHITE);
        donecek.setFont(new Font("Acre", Font.BOLD, 15));
        return donecek;
    }
}

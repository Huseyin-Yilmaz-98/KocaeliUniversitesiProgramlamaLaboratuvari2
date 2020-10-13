package Netflix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GirisPaneli extends JFrame {
    public SQLClass sqlObje;
    public AnaEkran pencere;
    public JButton girisButonu, kayitOlButonu;
    public JTextField girisEposta, kayitEposta, kayitIsim;
    public JPasswordField girisParola, kayitParola;
    public JButton girisYapButonu, kayitKayitOlButonu;
    public JButton girisGeriButonu, kayitGeriButonu;
    public JLabel girisEpostaLabel, girisParolaLabel;
    public JLabel kayitEpostaLabel, kayitParolaLabel, kayitIsimLabel, kayitDogumTarihiLabel;
    public ActionListener girisButonuListener, kayitOlButonuListener, girisYapButonuListener, geriButonuListener, kayitKayitOlListener, kayitGeriButonuListener;
    public List<JComponent> girisSayfasiOgeleri;
    public List<JComponent> kayitSayfasiOgeleri;
    public JComboBox<String> gunBox, ayBox, yilBox;
    public JLabel girisDurum, kayitDurum;

    GirisPaneli(SQLClass sqlObje) throws Exception {
        this.sqlObje = sqlObje;
        setLayout(null);
        setSize(500, 460);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(27, 73, 111));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listenerlariTanimla();
        girisButonu = buttonDondur(190, 120, 101, 41, "Giriş Yap", girisButonuListener);
        kayitOlButonu = buttonDondur(190, 210, 101, 41, "Kayıt Ol", kayitOlButonuListener);
        girisSayfasiOgeleri = new ArrayList<>();
        kayitSayfasiOgeleri = new ArrayList<>();
        girisEpostaLabel = labelDondur(115, 120, 60, 40, "Eposta:");
        girisEpostaLabel.setForeground(Color.WHITE);
        girisEpostaLabel.setFont(new Font("Acre", Font.BOLD, 14));
        girisSayfasiOgeleri.add(girisEpostaLabel);
        girisParolaLabel = labelDondur(115, 180, 60, 40, "Parola:");
        girisParolaLabel.setForeground(Color.WHITE);
        girisParolaLabel.setFont(new Font("Acre", Font.BOLD, 14));
        girisSayfasiOgeleri.add(girisParolaLabel);
        girisEposta = new JTextField();
        girisEposta.setBounds(180, 130, 200, 25);
        girisSayfasiOgeleri.add(girisEposta);
        add(girisEposta);
        girisParola = new JPasswordField();
        girisParola.setBounds(180, 190, 200, 25);
        girisSayfasiOgeleri.add(girisParola);
        add(girisParola);
        girisYapButonu = buttonDondur(140, 245, 101, 40, "Giriş Yap", girisYapButonuListener);
        girisSayfasiOgeleri.add(girisYapButonu);
        girisGeriButonu = buttonDondur(255, 245, 101, 40, "Geri", geriButonuListener);
        girisSayfasiOgeleri.add(girisGeriButonu);
        girisDurum = labelDondur(0, 290, 500, 20, "");
        girisDurum.setHorizontalAlignment(SwingConstants.CENTER);
        girisDurum.setForeground(Color.WHITE);
        girisDurum.setFont(new Font("Acre", Font.BOLD, 14));
        girisSayfasiOgeleri.add(girisDurum);
        for (int i = 0; i < girisSayfasiOgeleri.size(); i++) {
            girisSayfasiOgeleri.get(i).setVisible(false);
        }
        kayitEpostaLabel = labelDondur(115, 60, 60, 40, "Eposta:");
        kayitSayfasiOgeleri.add(kayitEpostaLabel);
        kayitParolaLabel = labelDondur(115, 120, 60, 40, "Parola:");
        kayitSayfasiOgeleri.add(kayitParolaLabel);
        kayitParola = new JPasswordField();
        kayitParola.setBounds(180, 130, 200, 25);
        kayitSayfasiOgeleri.add(kayitParola);
        add(kayitParola);
        kayitEposta = new JTextField();
        kayitEposta.setBounds(180, 70, 200, 25);
        kayitSayfasiOgeleri.add(kayitEposta);
        add(kayitEposta);
        kayitIsimLabel = labelDondur(115, 180, 60, 40, "İsim:");
        kayitSayfasiOgeleri.add(kayitIsimLabel);
        kayitIsim = new JTextField();
        kayitIsim.setBounds(180, 190, 200, 25);
        kayitSayfasiOgeleri.add(kayitIsim);
        add(kayitIsim);
        String[] gunlerList = new String[31];
        for (int i = 1; i < 32; i++) {
            gunlerList[i - 1] = "" + i;
        }
        kayitDogumTarihiLabel = labelDondur(50, 240, 125, 40, "Doğum Tarihi:");
        kayitSayfasiOgeleri.add(kayitDogumTarihiLabel);
        gunBox = new JComboBox<>(gunlerList);
        gunBox.setBounds(180, 245, 50, 30);
        kayitSayfasiOgeleri.add(gunBox);
        add(gunBox);
        String[] ayList = {"Ocak", "Subat", "Mart", "Nisan", "Mayis", "Haziran", "Temmuz", "Agustos", "Eylul", "Ekim", "Kasim", "Aralik"};
        ayBox = new JComboBox<>(ayList);
        ayBox.setBounds(235, 245, 100, 30);
        kayitSayfasiOgeleri.add(ayBox);
        add(ayBox);
        String[] yillarList = new String[121];
        for (int i = 1900; i < 2021; i++) {
            yillarList[i - 1900] = "" + i;
        }
        yilBox = new JComboBox<>(yillarList);
        yilBox.setBounds(340, 245, 60, 30);
        kayitSayfasiOgeleri.add(yilBox);
        add(yilBox);
        kayitKayitOlButonu = buttonDondur(140, 290, 101, 40, "Kayıt Ol", kayitKayitOlListener);
        kayitSayfasiOgeleri.add(kayitKayitOlButonu);
        kayitGeriButonu = buttonDondur(255, 290, 101, 40, "Geri", kayitGeriButonuListener);
        kayitSayfasiOgeleri.add(kayitGeriButonu);
        kayitDurum = labelDondur(0, 340, 500, 20, "");
        kayitDurum.setHorizontalAlignment(SwingConstants.CENTER);
        kayitDurum.setForeground(Color.WHITE);
        kayitDurum.setFont(new Font("Acre", Font.BOLD, 14));
        kayitSayfasiOgeleri.add(kayitDurum);
        for (int i = 0; i < kayitSayfasiOgeleri.size(); i++) {
            kayitSayfasiOgeleri.get(i).setVisible(false);
        }
    }


    public JLabel labelDondur(int x, int y, int genislik, int uzunluk, String yazi) {
        JLabel donecek = new JLabel(yazi);
        donecek.setBounds(x, y, genislik, uzunluk);
        donecek.setForeground(Color.WHITE);
        donecek.setFont(new Font("Acre", Font.BOLD, 14));
        add(donecek);
        donecek.setVisible(true);
        return donecek;
    }


    public JButton buttonDondur(int x, int y, int genislik, int uzunluk, String yazi, ActionListener listener) {
        JButton donecek = new JButton(yazi);
        donecek.setBounds(x, y, genislik, uzunluk);
        donecek.addActionListener(listener);
        donecek.setFocusPainted(false);
        donecek.setForeground(new Color(27, 73, 111));
        donecek.setBackground(Color.WHITE);
        donecek.setFont(new Font("Acre", Font.BOLD, 15));
        donecek.setBorderPainted(false);
        donecek.setContentAreaFilled(true);
        donecek.setOpaque(true);
        add(donecek);
        donecek.setVisible(true);
        return donecek;
    }

    public void girisYap() throws Exception {
        setVisible(false);
        pencere = new AnaEkran(sqlObje);

    }

    public void listenerlariTanimla() throws Exception {
        girisButonuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisButonu.setVisible(false);
                kayitOlButonu.setVisible(false);
                for (int i = 0; i < girisSayfasiOgeleri.size(); i++) {
                    girisSayfasiOgeleri.get(i).setVisible(true);
                }
            }
        };
        kayitOlButonuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisButonu.setVisible(false);
                kayitOlButonu.setVisible(false);
                for (int i = 0; i < kayitSayfasiOgeleri.size(); i++) {
                    kayitSayfasiOgeleri.get(i).setVisible(true);
                }
            }
        };
        girisYapButonuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (sqlObje.kullaniciGiris(girisEposta.getText(), new String(girisParola.getPassword()))) {
                        girisYap();
                    }
                } catch (Exception t) {
                }
            }
        };
        geriButonuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisButonu.setVisible(true);
                kayitOlButonu.setVisible(true);
                for (int i = 0; i < girisSayfasiOgeleri.size(); i++) {
                    girisSayfasiOgeleri.get(i).setVisible(false);
                }
            }
        };
        kayitGeriButonuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisButonu.setVisible(true);
                kayitOlButonu.setVisible(true);
                for (int i = 0; i < kayitSayfasiOgeleri.size(); i++) {
                    kayitSayfasiOgeleri.get(i).setVisible(false);
                }
            }
        };
        kayitKayitOlListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (kayitEposta.getText().equals("")) {
                    kayitDurum.setText("Eposta alanı boş olamaz!");
                    return;
                }
                if (new String(kayitParola.getPassword()).equals("")) {
                    kayitDurum.setText("Parola alanı boş olamaz!");
                    return;
                }
                if (kayitIsim.getText().equals("")) {
                    kayitDurum.setText("İsim alanı boş olamaz!");
                    return;
                }
                try {
                    if (sqlObje.kullaniciKayit(kayitEposta.getText(), new String(kayitParola.getPassword()), kayitIsim.getText(), yilBox.getSelectedIndex() + 1900, ayBox.getSelectedIndex() + 1, gunBox.getSelectedIndex() + 1)) {
                        if (sqlObje.kullaniciGiris(kayitEposta.getText(), new String(kayitParola.getPassword()))) {
                            girisYap();
                            SecimEkrani secim = new SecimEkrani(sqlObje, pencere);
                            secim.setVisible(true);
                        }
                    }
                } catch (Exception aa) {
                }
            }
        };
    }

}

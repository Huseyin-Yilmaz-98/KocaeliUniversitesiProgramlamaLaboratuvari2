package Netflix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SecimEkrani extends JFrame {
    public JPanel secileceklerPanel;
    public DefaultListModel<String> secileceklerList = new DefaultListModel<>();
    public JList secileceklerKutusu;
    public JPanel secilmislerPanel;
    public DefaultListModel<String> secilmislerList = new DefaultListModel<>();
    ;
    public JList secilmislerKutusu;
    public ActionListener tamamDugmesiListener;
    public ActionListener eklemeDugmesiListener;
    public ActionListener silmeDugmesiListener;
    public JLabel secilmemisLabel;
    public JLabel secilmisLabel;
    public JLabel bildirim = new JLabel();
    public ArrayList<JButton> ilkDugmeler;
    public SQLClass sqlObje;
    public ArrayList<JLabel> yapimSatirlari = new ArrayList<>();
    public ArrayList<String> yapimAdlari = new ArrayList<>();
    public AnaEkran anaEkran;

    SecimEkrani(SQLClass sqlObje, AnaEkran anaEkran) throws Exception {
        ilkDugmeler = new ArrayList<JButton>();
        this.anaEkran = anaEkran;
        this.sqlObje = sqlObje;
        listenerlariTanimla();
        sqlObje.rs = sqlObje.stmt.executeQuery("SELECT name FROM turler;");
        while (sqlObje.rs.next()) {
            secileceklerList.addElement(sqlObje.rs.getString("name"));
        }

        secileceklerKutusu = new JList(secileceklerList);
        secileceklerKutusu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secileceklerKutusu.setBounds(0, 0, 191, 351);
        secileceklerKutusu.setBackground(new Color(27, 73, 111));
        secileceklerKutusu.setFont(new Font("Acre", Font.BOLD, 13));
        secileceklerKutusu.setForeground(Color.WHITE);
        secilmislerKutusu = new JList(secilmislerList);
        secilmislerKutusu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secilmislerKutusu.setBounds(0, 0, 191, 351);
        secilmislerKutusu.setBackground(new Color(27, 73, 111));
        secilmislerKutusu.setFont(new Font("Acre", Font.BOLD, 13));
        secilmislerKutusu.setForeground(Color.WHITE);
        secileceklerPanel = panelOlustur(20, 60, 191, 351, secileceklerKutusu);
        secilmislerPanel = panelOlustur(310, 60, 191, 351, secilmislerKutusu);

        setSize(535, 563);
        setLocationRelativeTo(null);
        setLayout(null);
        JLabel yukariYazi = new JLabel("3 tane kategori seçin:", SwingConstants.CENTER);
        yukariYazi.setForeground(Color.WHITE);
        yukariYazi.setFont(new Font("Acre", Font.BOLD, 16));
        getContentPane().setBackground(new Color(0, 113, 188));
        yukariYazi.setBounds(0, 10, 500, 20);
        add(yukariYazi);
        add(secilmislerPanel);
        add(secileceklerPanel);
        add(dugmeOlustur("+", 240, 130, 45, 41, eklemeDugmesiListener));
        add(dugmeOlustur("-", 240, 280, 45, 41, silmeDugmesiListener));
        add(dugmeOlustur("Tamam", 210, 470, 91, 31, tamamDugmesiListener));
        bildirim.setBounds(0, 420, 510, 32);
        bildirim.setForeground(Color.WHITE);
        bildirim.setFont(new Font("Acre", Font.BOLD, 16));
        bildirim.setHorizontalAlignment(SwingConstants.CENTER);
        bildirim.setVerticalAlignment(JLabel.CENTER);
        secilmemisLabel = new JLabel("Kategoriler");
        secilmemisLabel.setForeground(Color.WHITE);
        secilmemisLabel.setFont(new Font("Acre", Font.BOLD, 16));
        secilmemisLabel.setBounds(20, 40, 191, 20);
        secilmemisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secilmemisLabel.setVerticalAlignment(JLabel.CENTER);
        add(secilmemisLabel);
        secilmisLabel = new JLabel("Seçilmiş Kategoriler");
        secilmisLabel.setForeground(Color.WHITE);
        secilmisLabel.setFont(new Font("Acre", Font.BOLD, 16));
        secilmisLabel.setBounds(310, 40, 191, 20);
        secilmisLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secilmisLabel.setVerticalAlignment(JLabel.CENTER);
        add(secilmisLabel);
        add(bildirim);
        setTitle("Kategori Seç");
    }

    public JPanel panelOlustur(int x, int y, int width, int height, JList liste) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, width, height);
        panel.add(liste);
        return panel;
    }

    public JButton dugmeOlustur(String text, int x, int y, int width, int height, ActionListener listener) {
        JButton buton = new JButton(text);
        buton.setBounds(x, y, width, height);
        buton.addActionListener(listener);
        buton.setFocusPainted(false);
        buton.setBorderPainted(false);
        buton.setContentAreaFilled(true);
        buton.setOpaque(true);
        buton.setForeground(new Color(0, 113, 188));
        buton.setBackground(Color.WHITE);
        buton.setFont(new Font("Acre", Font.BOLD, 11));
        ilkDugmeler.add(buton);
        return buton;
    }

    public void listenerlariTanimla() {
        tamamDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() < 3) {
                    bildirim.setText("3 tane kategori seçmeniz lazım");
                    return;
                }
                try {
                    secilmislerPanel.setVisible(false);
                    secileceklerPanel.setVisible(false);
                    secilmemisLabel.setVisible(false);
                    secilmisLabel.setVisible(false);
                    for (int i = 0; i < ilkDugmeler.size(); i++) {
                        ilkDugmeler.get(i).setVisible(false);
                    }
                    ArrayList<String> filmler = new ArrayList<>();
                    sqlObje.rs = sqlObje.stmt.executeQuery("SELECT DISTINCT programlar.program_id as ID, programlar.name AS Ad,\n" +
                            "(SELECT SUM(puan)/COUNT(puan) \n" +
                            "FROM kullanici_program\n" +
                            "WHERE kullanici_program.program_id=programlar.program_id and puan<>0) as puanlar FROM tur_program\n" +
                            "INNER JOIN  programlar ON programlar.program_id=tur_program.program_id\n" +
                            "INNER JOIN turler ON turler.tur_id=tur_program.tur_id WHERE turler.name='" + secilmislerList.get(1) + "'\n" +
                            "OR turler.name='" + secilmislerList.get(0) + "' or turler.name='" + secilmislerList.get(2) + "' ORDER BY puanlar DESC;");
                    for (int i = 0; i < 6; i++) {
                        if (sqlObje.rs.next())
                            filmler.add(sqlObje.rs.getString("Ad"));
                    }
                    anaEkran.filmListele(filmler);
                    dispose();
                } catch (Exception t) {
                    System.out.println("Program calistirilamadi");
                }
            }
        };
        eklemeDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() >= 3) {
                    bildirim.setText("3'ten fazla kategori seçilemez!");
                    return;
                }
                if (secileceklerKutusu.getSelectedValue() == null) {
                    bildirim.setText("Seçim yapmadınız.");
                    return;
                }
                bildirim.setText("");
                String secilmis = secileceklerKutusu.getSelectedValue().toString();
                int index = secileceklerList.indexOf(secilmis);
                secileceklerList.remove(index);
                secilmislerList.addElement(secilmis);
            }
        };
        silmeDugmesiListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secilmislerList.size() == 0) {
                    bildirim.setText("Listede kategori yok.");
                    return;
                }
                if (secilmislerKutusu.getSelectedValue() == null) {
                    bildirim.setText("Kategori seçiniz.");
                    return;
                }
                bildirim.setText("");
                String secilmis = secilmislerKutusu.getSelectedValue().toString();
                int index = secilmislerList.indexOf(secilmis);
                secilmislerList.remove(index);
                secileceklerList.addElement(secilmis);
            }
        };
    }

    public JLabel yapimSatiriOlustur(int x, int y, int genislik, int yukseklik, String filmAdi, String kategori) {
        JLabel donecek = new JLabel(filmAdi + " (" + kategori + ")");
        donecek.setBounds(x, y, genislik, yukseklik);
        donecek.setHorizontalAlignment(JLabel.CENTER);
        donecek.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        donecek.setVisible(true);
        add(donecek);
        return donecek;
    }
}

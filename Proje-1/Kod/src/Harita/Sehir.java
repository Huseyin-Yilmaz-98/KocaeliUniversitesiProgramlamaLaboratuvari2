package Harita;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sehir {
    String sehirAdi;
    //sehrin tum komsularinin plaka listesi
    List<Integer> komsular = new ArrayList<>();
    //Sehrin tum komsularina olan mesafesinin komsular listesiyle ayni indis sirasinda listesi
    List<Integer> komsuMesafeler = new ArrayList<>();
    //sehrin haritadaki her ile olan en kisa mesafesinin hashmap formatinda listesi
    Map<Integer, Integer> mesafeler = new HashMap<>();
    //sehrin her ile en kisa mesafedeki rotasi
    Map<Integer, List<Integer>> guzergahlar = new HashMap<>();
    //sehrin harita resmindeki alt noktasinin koordinatlari
    int[] altkoordinatlar = new int[2];
    //sehrin harita resmindeki ust noktasinin koordinatlari
    int[] ustkoordinatlar = new int[2];

    Sehir(String sehirAdi) {
        this.sehirAdi = sehirAdi;
    }
}

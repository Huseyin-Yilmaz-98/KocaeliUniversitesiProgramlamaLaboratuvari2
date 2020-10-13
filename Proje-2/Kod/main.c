#include <stdio.h>
#include <stdlib.h>
#include <string.h>

FILE *deflatedosya;
float deflateboyut=0;

typedef struct deflate_struct
{
    unsigned char eslesmeuzunlugu;
    int eslesmekonumu;
    int harfMi;
} deflate_node;

typedef struct huffman_struct
{
    struct huffman_struct *sagdal;
    struct huffman_struct *soldal;
    unsigned char veri;
    int agirlik;
} huffman_node;

void lz77_enuzuneslesme(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu,int anlikkonum,short int *enuzuneslesmekonumu,unsigned char *enuzuneslesmeuzunlugu);
void deflate_enuzuneslesme(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu,int anlikkonum,int *enuzuneslesmekonumu,unsigned char *enuzuneslesmeuzunlugu);
huffman_node nodeyarat(unsigned char veri,int agirlik);
huffman_node nodebirlestir(huffman_node soldal,huffman_node sagdal);
void nodeSirala(huffman_node nodelar[],int boyut);
void koddondur(int veri,huffman_node *anliknode,int dizi[],int boyut,int dizi2[],int *boyut2);
int pow2(int alt,int us);
unsigned char bityazdir(int bit,unsigned char byte,int *indis);
void deflate_encode(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu, int blokuzunlugu);
int bitdondur(unsigned char *byte,int *indis,FILE *dosya);
huffman_node *node_yarat();
void huffman_deger_ekle(huffman_node **ilknode,int dizi[],int boyut,int veri);
void deflate_decode();
void lz77_decode();
void lz77_encode(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu);

int main()
{
    //deflate ciktisinin yazilacagi dosyayi olustur
    deflatedosya=fopen("deflate.txt","wb");

    char *metin=(char*)malloc(0*sizeof(char));
    int harfSayisi=0;
    char harf;

    //sikistirilacak metni oku
    FILE *dosya=fopen("metin.txt","r");
    if(!dosya){
        printf("metin.txt bulunamadi!");
        return 0;
    }
    while(!feof(dosya))
    {
        harf=fgetc(dosya);
        metin=(char*)realloc(metin,sizeof(char)*(harfSayisi+1));
        metin[harfSayisi++]=harf;
    }
    fclose(dosya);

    //metnin son karakterine bitis isareti ekle
    metin[harfSayisi]='\0';

    //lz77 sikistirmasini uygula
    lz77_encode(metin,32760,255);

    //deflate sikistirmasini uygula
    deflate_encode(metin,32760,255,1000);

    //yapilan lz77 sikistirmasini decode et
    lz77_decode();

    fclose(deflatedosya);

    //yazilan deflate sikistirmasini decode et
    deflate_decode();

    printf("Cikmak icin entera basin."),
    scanf("%c",&harf);
    return 0;
}

//lz77 sikistirmasinda kullanilan, en uzun eslesme konumunu ve uzunlugunu bulan fonksiyon
void lz77_enuzuneslesme(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu,int anlikkonum,short int *enuzuneslesmekonumu,unsigned char *enuzuneslesmeuzunlugu)
{
    *enuzuneslesmeuzunlugu=0;
    int tamponbaslangici;
    int ileritampon,aramatamponu;
    if(anlikkonum-aramatamponuzunlugu<=0)
    {
        tamponbaslangici=0;
    }
    else
    {
        tamponbaslangici=anlikkonum-aramatamponuzunlugu;
    }

    int eslesmeuzunlugu;
    for(aramatamponu=tamponbaslangici; aramatamponu<anlikkonum; aramatamponu++)
    {
        eslesmeuzunlugu=0;
        for(ileritampon=anlikkonum; ileritampon-anlikkonum<ileritamponuzunlugu; ileritampon++)
        {
            if(metin[aramatamponu+eslesmeuzunlugu]==metin[ileritampon] && (metin[aramatamponu+eslesmeuzunlugu]!='\0' && metin[ileritampon]!='\0'))
            {
                eslesmeuzunlugu++;
            }
            else
            {
                break;
            }
        }
        if(eslesmeuzunlugu>=*enuzuneslesmeuzunlugu)
        {
            *enuzuneslesmeuzunlugu=eslesmeuzunlugu;
            *enuzuneslesmekonumu=anlikkonum-(aramatamponu);
        }
    }
}

//deflate sikistirmasinda kullanilan, en uzun eslesme konumunu ve uzunlugunu bulan fonksiyon
void deflate_enuzuneslesme(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu,int anlikkonum,int *enuzuneslesmekonumu,unsigned char *enuzuneslesmeuzunlugu)
{
    *enuzuneslesmeuzunlugu=0;
    int tamponbaslangici;
    int ileritampon,aramatamponu;
    if(anlikkonum-aramatamponuzunlugu<=0)
    {
        tamponbaslangici=0;
    }
    else
    {
        tamponbaslangici=anlikkonum-aramatamponuzunlugu;
    }

    int eslesmeuzunlugu;
    for(aramatamponu=tamponbaslangici; aramatamponu<anlikkonum; aramatamponu++)
    {
        eslesmeuzunlugu=0;
        for(ileritampon=anlikkonum; ileritampon-anlikkonum<ileritamponuzunlugu; ileritampon++)
        {
            if(metin[aramatamponu+eslesmeuzunlugu]==metin[ileritampon] && (metin[aramatamponu+eslesmeuzunlugu]!='\0' && metin[ileritampon]!='\0'))
            {
                eslesmeuzunlugu++;
            }
            else
            {
                break;
            }
        }
        if(eslesmeuzunlugu>=*enuzuneslesmeuzunlugu)
        {
            *enuzuneslesmeuzunlugu=eslesmeuzunlugu;
            *enuzuneslesmekonumu=anlikkonum-(aramatamponu);
        }
    }
}

//huffman node'u yaratir ve yarattigi node'u dondurur
huffman_node nodeyarat(unsigned char veri,int agirlik)
{
    huffman_node yeninode;
    yeninode.sagdal=NULL;
    yeninode.soldal=NULL;
    yeninode.agirlik=agirlik;
    yeninode.veri=veri;
    return yeninode;
}

//iki huffman node'unu birlestirip bu yeni node'u dondurur
huffman_node nodebirlestir(huffman_node soldal,huffman_node sagdal)
{
    huffman_node yeninode;
    yeninode.agirlik=soldal.agirlik+sagdal.agirlik;
    yeninode.soldal=(struct huffman_struct*)malloc(sizeof(struct huffman_struct));
    yeninode.soldal->agirlik=soldal.agirlik;
    yeninode.soldal->veri=soldal.veri;
    yeninode.soldal->sagdal=soldal.sagdal;
    yeninode.soldal->soldal=soldal.soldal;
    yeninode.sagdal=(huffman_node*)malloc(sizeof(huffman_node));
    yeninode.sagdal->agirlik=sagdal.agirlik;
    yeninode.sagdal->veri=sagdal.veri;
    yeninode.sagdal->sagdal=sagdal.sagdal;
    yeninode.sagdal->soldal=sagdal.soldal;
    return yeninode;
}

//huffman agacindaki nodelari siralar
void nodeSirala(huffman_node nodelar[],int boyut)
{
    int i,j;
    int enKucuk,enKucukIndis;
    huffman_node temp;
    for(i=0; i<boyut; i++)
    {
        enKucuk=nodelar[i].agirlik;
        enKucukIndis=i;
        for(j=i; j<boyut; j++)
        {
            if(nodelar[j].agirlik<enKucuk)
            {
                enKucuk=nodelar[j].agirlik;
                enKucukIndis=j;
            }
        }
        temp=nodelar[enKucukIndis];
        nodelar[enKucukIndis]=nodelar[i];
        nodelar[i]=temp;
    }
}

//aldigi karakterin huffman agacindaki kodunu dondurur, rekürsif fonksiyondur, uygun node'u bulana kadar kendini cagirmaya devam eder
void koddondur(int veri,huffman_node *anliknode,int dizi[],int boyut,int dizi2[],int *boyut2)
{
    if(anliknode->sagdal!=NULL)
    {
        dizi[boyut]=1;
        koddondur(veri,anliknode->sagdal,dizi,boyut+1,dizi2,boyut2);
    }
    if(anliknode->soldal!=NULL)
    {
        dizi[boyut]=0;
        koddondur(veri,anliknode->soldal,dizi,boyut+1,dizi2,boyut2);
    }
    if(anliknode->sagdal==NULL && anliknode->soldal==NULL)
    {
        if(anliknode->veri==veri)
        {
            int i;
            for(i=0; i<boyut; i++)
            {
                dizi2[i]=dizi[i];
            }
            *boyut2=boyut;
        }
    }
}

//birinci parametrenin ikinci parametreye gore ustunu alip dondurur
int pow2(int alt,int us)
{
    int carpim=1;
    int i;
    for(i=0; i<us; i++)
    {
        carpim*=alt;
    }
    return carpim;
}

//aldigi biti anlik byte'a ekler, eger byte dolduysa dosyaya yazdirip yeni byte'a gecer
unsigned char bityazdir(int bit,unsigned char byte,int *indis)
{
    unsigned char kiyas;
    if(bit)
    {
        kiyas=pow2(2,*indis);
    }
    else
    {
        kiyas=0;
    }
    byte|=kiyas;
    if(*indis==0)
    {
        fwrite(&byte,sizeof(unsigned char),1,deflatedosya);
        deflateboyut++;
        byte=0;
    }
    if(*indis==0)
    {
        *indis=7;
    }
    else
    {
        *indis=(*indis)-1;
    }
    return byte;
}

//deflate algoritmasi kullanarak metni sikistiran algoritmadir, detaylar rapordadir
void deflate_encode(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu, int blokuzunlugu)
{
    FILE *dosya2=fopen("deflate_okunabilir.txt","w");
    int anlikkonum=0;
    int node_sayac=0,i;
    deflate_node nodelar[1024];
    unsigned char enuzuneslesmeuzunlugu;
    int enuzuneslesmekonumu;
    char sayi[6];
    huffman_node huffman1[10];
    huffman_node huffman2[10];
    int boyut2=10;
    int boyut=10,j,k,m;
    int dizi[30],dizi2[30];
    int diziboyut2;
    unsigned char byte=0;
    int indis=7;
    for(i=0; i<10; i++)
    {
        huffman1[i].agirlik=0;
        huffman1[i].veri=i;
        huffman1[i].sagdal=NULL;
        huffman1[i].soldal=NULL;
        huffman2[i].agirlik=0;
        huffman2[i].veri=i;
        huffman2[i].sagdal=NULL;
        huffman2[i].soldal=NULL;
    }
    while(1)
    {
        deflate_enuzuneslesme(metin,aramatamponuzunlugu,ileritamponuzunlugu,anlikkonum,&enuzuneslesmekonumu,&enuzuneslesmeuzunlugu);
        if(enuzuneslesmeuzunlugu>=3)
        {
            nodelar[node_sayac].eslesmekonumu=enuzuneslesmekonumu;
            nodelar[node_sayac].eslesmeuzunlugu=enuzuneslesmeuzunlugu;
            nodelar[node_sayac].harfMi=0;
            anlikkonum+=enuzuneslesmeuzunlugu;
        }
        else
        {
            nodelar[node_sayac].harfMi=1;
            nodelar[node_sayac].eslesmeuzunlugu=metin[anlikkonum];
            anlikkonum++;
        }
        for(i=0; i<6; i++)
        {
            sayi[i]='\0';
        }
        sprintf(sayi,"%d",nodelar[node_sayac].eslesmeuzunlugu);
        for(i=0; sayi[i]!='\0'; i++)
        {
            huffman1[sayi[i]-48].agirlik++;
        }

        if(nodelar[node_sayac].harfMi==0)
        {
            for(i=0; i<6; i++)
            {
                sayi[i]='\0';
            }
            sprintf(sayi,"%d",nodelar[node_sayac].eslesmekonumu);
            for(i=0; sayi[i]!='\0'; i++)
            {
                huffman2[sayi[i]-48].agirlik++;
            }
        }
        node_sayac++;
        if(anlikkonum>strlen(metin)-2 || node_sayac==blokuzunlugu)
        {
            for(; boyut>1;)
            {
                nodeSirala(huffman1,boyut);

                if(huffman1[0].agirlik==0 && boyut>2)
                {

                    for(j=0; j<boyut; j++)
                    {
                        huffman1[j]=huffman1[j+1];
                    }
                }
                else
                {
                    huffman1[0]=nodebirlestir(huffman1[0],huffman1[1]);
                    for(j=1; j<boyut; j++)
                    {
                        huffman1[j]=huffman1[j+1];
                    }
                }
                boyut--;
            }
            for(; boyut2>1; boyut2--)
            {
                nodeSirala(huffman2,boyut2);
                if(huffman2[0].agirlik==0 && boyut2>2)
                {
                    for(i=0; i<boyut2; i++)
                    {
                        huffman2[i]=huffman2[i+1];
                    }
                }
                else
                {
                    huffman2[0]=nodebirlestir(huffman2[0],huffman2[1]);
                    for(i=1; i<boyut2; i++)
                    {
                        huffman2[i]=huffman2[i+1];
                    }
                }
            }
            if(anlikkonum>strlen(metin)-2)
            {
                fprintf(dosya2,"SON BLOK\n");
                byte=bityazdir(1,byte,&indis);
            }
            else
            {
                fprintf(dosya2,"SON BLOK DEGIL\n");
                byte=bityazdir(0,byte,&indis);
            }
            byte=bityazdir(1,byte,&indis);
            byte=bityazdir(0,byte,&indis);
            fprintf(dosya2,"DINAMIK HUFFMAN\n");
            int nodesayisi=node_sayac;
            fprintf(dosya2,"BU BLOKTA %d GIRDI VAR\n",nodesayisi);
            for(k=9; k>=0; k--)
            {
                if(nodesayisi>=pow2(2,k))
                {
                    byte=bityazdir(1,byte,&indis);
                    nodesayisi-=pow2(2,k);
                }
                else
                {
                    byte=bityazdir(0,byte,&indis);
                }
            }
            fprintf(dosya2,"\nESLESME UZUNLUKLARI/KARAKTER HUFFMAN AGACI:\n");
            for(k=0; k<10; k++)
            {
                diziboyut2=0;
                koddondur(k,&huffman1[0],dizi,0,dizi2,&diziboyut2);
                int diziboyut2_2=diziboyut2;
                fprintf(dosya2,"%d: ",k);
                for(j=3; j>=0; j--)
                {
                    if(diziboyut2_2>=pow2(2,j))
                    {
                        byte=bityazdir(1,byte,&indis);
                        diziboyut2_2-=pow2(2,j);
                    }
                    else
                    {
                        byte=bityazdir(0,byte,&indis);
                    }
                }
                if(diziboyut2==0){
                    fprintf(dosya2,"Bu deger hic kullanilmamis");
                }
                for(j=0; j<diziboyut2; j++)
                {
                    byte=bityazdir(dizi2[j],byte,&indis);
                    fprintf(dosya2,"%d",dizi2[j]);
                }
                fprintf(dosya2,"\n");
            }
            fprintf(dosya2,"\nESLESME KONUMLARI HUFFMAN AGACI:\n");
            for(k=0; k<10; k++)
            {
                diziboyut2=0;
                koddondur(k,&huffman2[0],dizi,0,dizi2,&diziboyut2);
                int diziboyut2_2=diziboyut2;
                fprintf(dosya2,"%d: ",k);
                for(j=3; j>=0; j--)
                {
                    if(diziboyut2_2>=pow2(2,j))
                    {
                        byte=bityazdir(1,byte,&indis);
                        diziboyut2_2-=pow2(2,j);
                    }
                    else
                    {
                        byte=bityazdir(0,byte,&indis);
                    }
                }
                if(diziboyut2==0){
                    fprintf(dosya2,"Bu deger hic kullanilmamis");
                }
                for(j=0; j<diziboyut2; j++)
                {
                    byte=bityazdir(dizi2[j],byte,&indis);
                    fprintf(dosya2,"%d",dizi2[j]);
                }
                fprintf(dosya2,"\n");
            }
            fprintf(dosya2,"\n");
            for(k=0; k<node_sayac; k++)
            {
                fprintf(dosya2,"%d. ",k+1);
                if(nodelar[k].harfMi==1)
                {
                    fprintf(dosya2,"HARF: ");
                    byte=bityazdir(1,byte,&indis);
                    for(j=0; j<6; j++)
                    {
                        sayi[j]='\0';
                    }
                    sprintf(sayi,"%d",nodelar[k].eslesmeuzunlugu);
                    for(j=0; sayi[j]!='\0'; j++)
                    {
                        if(sayi[j+1]=='\0')
                        {
                            byte=bityazdir(1,byte,&indis);
                        }
                        else
                        {
                            byte=bityazdir(0,byte,&indis);
                        }
                        koddondur(sayi[j]-48,&huffman1[0],dizi,0,dizi2,&diziboyut2);
                        for(m=0; m<diziboyut2; m++)
                        {
                            byte=bityazdir(dizi2[m],byte,&indis);
                            fprintf(dosya2,"%d",dizi2[m]);
                        }
                        if(sayi[j+1]!='\0')
                        {
                            fprintf(dosya2,"-");
                        }
                    }
                    fprintf(dosya2," (%d) (%c)\n",nodelar[k].eslesmeuzunlugu,nodelar[k].eslesmeuzunlugu);
                }
                else
                {
                    fprintf(dosya2,"ESLESME UZUNLUGU: ");
                    byte=bityazdir(0,byte,&indis);
                    for(j=0; j<6; j++)
                    {
                        sayi[j]='\0';
                    }
                    sprintf(sayi,"%d",nodelar[k].eslesmeuzunlugu);
                    for(j=0; sayi[j]!='\0'; j++)
                    {
                        if(sayi[j+1]=='\0')
                        {
                            byte=bityazdir(1,byte,&indis);
                        }
                        else
                        {
                            byte=bityazdir(0,byte,&indis);
                        }
                        koddondur(sayi[j]-48,&huffman1[0],dizi,0,dizi2,&diziboyut2);
                        for(m=0; m<diziboyut2; m++)
                        {
                            byte=bityazdir(dizi2[m],byte,&indis);
                            fprintf(dosya2,"%d",dizi2[m]);
                        }
                        if(sayi[j+1]!='\0')
                        {
                            fprintf(dosya2,"-");
                        }
                    }
                    fprintf(dosya2," (%d)    ",nodelar[k].eslesmeuzunlugu);
                    fprintf(dosya2,"ESLESME KONUMU: ");
                    for(j=0; j<6; j++)
                    {
                        sayi[j]='\0';
                    }
                    sprintf(sayi,"%d",nodelar[k].eslesmekonumu);
                    for(j=0; sayi[j]!='\0'; j++)
                    {
                        if(sayi[j+1]=='\0')
                        {
                            byte=bityazdir(1,byte,&indis);
                        }
                        else
                        {
                            byte=bityazdir(0,byte,&indis);
                        }
                        koddondur(sayi[j]-48,&huffman2[0],dizi,0,dizi2,&diziboyut2);
                        for(m=0; m<diziboyut2; m++)
                        {
                            byte=bityazdir(dizi2[m],byte,&indis);
                            fprintf(dosya2,"%d",dizi2[m]);
                        }
                    }
                    fprintf(dosya2," (%d)\n",nodelar[k].eslesmekonumu);
                }
            }
            while(indis<7)
            {
                byte=bityazdir(0,byte,&indis);
            }
            fprintf(dosya2,"\n-----------------------------------------------------------\n\n",nodelar[k].eslesmekonumu);
            node_sayac=0;
            for(i=0; i<10; i++)
            {
                huffman1[i]=nodeyarat(i,0);
            }
            boyut=10;
            for(i=0; i<10; i++)
            {
                huffman2[i]=nodeyarat(i,0);
            }
            boyut2=10;

        }
        if(anlikkonum>strlen(metin)-2)
        {
            break;
        }
    }
    fclose(dosya2);
    printf("Deflate sikistirma orani : %c%f\n",37,(strlen(metin)-deflateboyut)/strlen(metin)*100);
}

//anlik bytedaki belirtilen indisteki biti okuyup dondurur
int bitdondur(unsigned char *byte,int *indis,FILE *dosya)
{
    unsigned char kiyas=pow2(2,*indis);
    int donecek=((*byte)&kiyas)!=0;
    if(*indis==0)
    {
        fread(byte,sizeof(unsigned char),1,dosya);
        *indis=7;
    }
    else
    {
        *indis=*indis-1;
    }
    return donecek;
}

//huffman node'u yaratip bu node'u donduren fonksiyon
huffman_node *node_yarat()
{
    huffman_node *donecek=(huffman_node*)malloc(sizeof(huffman_node));
    donecek->soldal=NULL;
    donecek->sagdal=NULL;
    donecek->veri=255;
    return donecek;
}

//huffman agacina yeni node ekler
void huffman_deger_ekle(huffman_node **ilknode,int dizi[],int boyut,int veri)
{
    huffman_node *anliknode=*ilknode;
    int i;
    for(i=0; i<boyut; i++)
    {
        if(dizi[i]==1)
        {
            if(anliknode->sagdal==NULL)
            {
                anliknode->sagdal=node_yarat();
            }
            anliknode=anliknode->sagdal;
        }
        if(dizi[i]==0)
        {
            if(anliknode->soldal==NULL)
            {
                anliknode->soldal=node_yarat();
            }
            anliknode=anliknode->soldal;
        }
    }
    if(boyut!=0)
        anliknode->veri=veri;
}

//deflate.txt dosyasini okuyup decode eder
void deflate_decode()
{
    FILE *dosya=fopen("deflate.txt","rb");
    char *metin=(char*)malloc(0*sizeof(char));
    int harfsayaci=0;
    int i,j;
    int sonblokmu;
    int indis=7;
    int girdisayisi;
    int dizi[30];
    unsigned char byte;
    huffman_node *huffman1;
    huffman_node *huffman2;
    huffman_node *anliknode;
    int koduzunlugu;
    int harfmi;
    int sonbasamakmi;
    char sayi[6];
    int basamak;
    int deger;
    int eslesmeuzunlugu,eslesmekonumu;
    fread(&byte,sizeof(unsigned char),1,dosya);
    while(1)
    {
        sonblokmu=bitdondur(&byte,&indis,dosya);
        bitdondur(&byte,&indis,dosya);
        bitdondur(&byte,&indis,dosya);
        girdisayisi=0;

        for(i=9; i>=0; i--)
        {
            girdisayisi+=(bitdondur(&byte,&indis,dosya)*pow2(2,i));
        }
        huffman1=node_yarat();
        huffman2=node_yarat();
        for(j=0; j<10; j++)
        {
            koduzunlugu=0;
            for(i=3; i>=0; i--)
            {
                koduzunlugu+=(bitdondur(&byte,&indis,dosya)*pow2(2,i));
            }
            for(i=0; i<koduzunlugu; i++)
            {
                dizi[i]=bitdondur(&byte,&indis,dosya);
            }
            huffman_deger_ekle(&huffman1,dizi,koduzunlugu,j);
        }
        for(j=0; j<10; j++)
        {
            koduzunlugu=0;
            for(i=3; i>=0; i--)
            {
                koduzunlugu+=(bitdondur(&byte,&indis,dosya)*pow2(2,i));
            }
            for(i=0; i<koduzunlugu; i++)
            {
                dizi[i]=bitdondur(&byte,&indis,dosya);
            }
            huffman_deger_ekle(&huffman2,dizi,koduzunlugu,j);
        }
        for(i=0; i<girdisayisi; i++)
        {
            harfmi=bitdondur(&byte,&indis,dosya);
            for(j=0; j<6; j++)
            {
                sayi[j]='\0';
            }
            basamak=0;
            while(1)
            {
                sonbasamakmi=bitdondur(&byte,&indis,dosya);
                anliknode=huffman1;
                while(1)
                {
                    if(anliknode->veri!=255)
                    {
                        break;
                    }
                    deger=bitdondur(&byte,&indis,dosya);
                    if(deger==1)
                    {
                        anliknode=anliknode->sagdal;
                    }
                    else
                    {
                        anliknode=anliknode->soldal;
                    }
                }
                sayi[basamak]=anliknode->veri;
                basamak++;
                if(sonbasamakmi==1)
                {
                    break;
                    printf("\n");
                }
            }
            if(harfmi==1)
            {
                metin=(char*)realloc(metin,sizeof(char)*harfsayaci+1);
                metin[harfsayaci]=0;
                int k=0;
                for(j=basamak-1; j>=0; j--)
                {
                    metin[harfsayaci]+=(pow2(10,j)*sayi[k]);
                    k++;
                }
                harfsayaci++;
            }
            if(harfmi==0)
            {
                eslesmeuzunlugu=0;
                int k=0;
                for(j=basamak-1; j>=0; j--)
                {
                    eslesmeuzunlugu+=(pow2(10,j)*sayi[k]);
                    k++;
                }
                for(j=0; j<6; j++)
                {
                    sayi[j]='\0';
                }
                basamak=0;
                while(1)
                {
                    sonbasamakmi=bitdondur(&byte,&indis,dosya);
                    anliknode=huffman2;
                    while(1)
                    {
                        if(anliknode->veri!=255)
                        {
                            break;
                        }
                        deger=bitdondur(&byte,&indis,dosya);
                        if(deger==1)
                        {
                            anliknode=anliknode->sagdal;
                        }
                        else
                        {
                            anliknode=anliknode->soldal;
                        }
                    }
                    sayi[basamak]=anliknode->veri;
                    basamak++;
                    if(sonbasamakmi==1)
                    {
                        break;
                    }
                }
                eslesmekonumu=0;
                k=0;
                for(j=basamak-1; j>=0; j--)
                {
                    eslesmekonumu+=(pow2(10,j)*sayi[k]);
                    k++;
                }
                for(k=0; k<eslesmeuzunlugu; k++)
                {
                    metin=(char*)realloc(metin,sizeof(char)*harfsayaci+1);
                    metin[harfsayaci]=metin[harfsayaci-eslesmekonumu];
                    harfsayaci++;
                }
            }
        }
        while(indis<7)
        {
            bitdondur(&byte,&indis,dosya);
        }
        if(sonblokmu==1)
        {
            break;
        }
    }
    if(metin[harfsayaci]!='\0')
    {
        metin=(char*)realloc(metin,sizeof(char)*(harfsayaci+1));
        metin[harfsayaci]='\0';
    }
    dosya=fopen("deflate_decoded.txt","w");
    for(i=0; metin[i]!='\0'; i++)
    {
        fprintf(dosya,"%c",metin[i]);
    }
    fclose(dosya);
    printf("Deflate decode edildi.\n");
}

//lz77.txt dosyasini okuyup decode eder
void lz77_decode()
{
    FILE *dosya=fopen("lz77.txt","rb");
    char *metin=(char*)malloc(0*sizeof(char));
    int harfsayaci=0;
    int i;
    short int eslesmekonumu;
    unsigned char eslesmeuzunlugu;
    char harf;
    while(1)
    {
        if(fread(&eslesmekonumu,sizeof(short int),1,dosya)==0)
            break;
        if(fread(&eslesmeuzunlugu,sizeof(unsigned char),1,dosya)==0)
            break;
        for(i=0; i<eslesmeuzunlugu; i++)
        {
            metin=(char*)realloc(metin,sizeof(char)*(harfsayaci+1));
            metin[harfsayaci]=metin[harfsayaci-eslesmekonumu];
            harfsayaci+=1;
        }
        metin=(char*)realloc(metin,sizeof(char)*(harfsayaci+1));
        if(fread(&harf,sizeof(char),1,dosya)==0)
        {
            break;
        }
        metin[harfsayaci]=harf;
        harfsayaci+=1;
    }
    if(metin[harfsayaci]!='\0' && metin[harfsayaci-1]!='\0')
    {
        metin=(char*)realloc(metin,sizeof(char)*(harfsayaci+1));
        metin[harfsayaci-1]='\0';
    }
    fclose(dosya);
    dosya=fopen("lz77_decoded.txt","w");
    for(i=0; metin[i]!='\0'; i++)
    {
        fprintf(dosya,"%c",metin[i]);
    }
    fclose(dosya);
    printf("LZ77 decode edildi.\n");
}

//lz77 algoritmasi kullanarak metni sikistirir, detaylar rapordadir
void lz77_encode(char *metin,int aramatamponuzunlugu,int ileritamponuzunlugu)
{
    unsigned char enuzuneslesmeuzunlugu;
    short int enuzuneslesmekonumu;
    int anlikkonum=0;
    char harf;
    FILE *dosya=fopen("lz77.txt","wb");
    FILE *dosya2=fopen("lz77_okunabilir.txt","w");
    float toplamboyut=0;
    while(1)
    {
        lz77_enuzuneslesme(metin,aramatamponuzunlugu,ileritamponuzunlugu,anlikkonum,&enuzuneslesmekonumu,&enuzuneslesmeuzunlugu);
        if(enuzuneslesmeuzunlugu==0)
        {
            enuzuneslesmekonumu=0;
        }
        harf=metin[anlikkonum+enuzuneslesmeuzunlugu];
        anlikkonum+=enuzuneslesmeuzunlugu+1;
        fwrite(&enuzuneslesmekonumu,sizeof(short int),1,dosya);
        fwrite(&enuzuneslesmeuzunlugu,sizeof(unsigned char),1,dosya);
        if(anlikkonum>strlen(metin)-1)
        {
            harf='\0';
        }
        fwrite(&harf,sizeof(char),1,dosya);
        if(harf=='\0'){
            fprintf(dosya2,"(%d, %d, END)\n",enuzuneslesmekonumu,enuzuneslesmeuzunlugu);
        }
        else{
            fprintf(dosya2,"(%d, %d, %c)\n",enuzuneslesmekonumu,enuzuneslesmeuzunlugu,harf);
        }
        toplamboyut+=sizeof(unsigned char)+sizeof(short int)+sizeof(char);

        if(anlikkonum>strlen(metin)-1)
        {
            break;
        }
    }
    fclose(dosya2);
    fclose(dosya);
    printf("LZ77 sikistirma orani :    %c%f\n",37,(strlen(metin)-toplamboyut)/strlen(metin)*100);
}


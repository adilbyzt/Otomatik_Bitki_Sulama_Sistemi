# Otomatik Bitki Sulama

# 1. Giriş

## 1.1. Projenin Alt yapısı

Proje Arduino IDE ve Android Studio kullanarak. C ve Java dilleri ile geliştirilmiştir. Projede IOT
projeler için API sağlayan ThingSpeak kullanılmıştır.

## 1.2. Projenin Amacı

Geliştirmiş olduğumuz proje olan Otomatik Bitki Sulama Sistemi, dışarıdan müdahale olmadan
bitkilerin sulanmasını sağlar. Bu sayede bitkiler ihtiyacı olan nem oranında tutularak bitkinin gelişimi
verimli şekilde sağlanır. Aynı zamanda mobil uygulama sayesinde bitkinin yanında olunmasına gerek
kalmadan oda sıcaklığı, odanın nemi, bitkinin toprak nem oranı ve bitkinin aldığı ışık miktarı
uygulama üzerinde gösterilmesi sağlanır.

## 1.3. Projenin Kullanım Alanları

Proje kişisel kullanım için daha uygun olmakla birlikte. Tarım alanlarında veya sulamanın
yapılacağı her türlü sisteme entegre edilebilir.

## 1.4. Projede Kullanılan Cihaz ve Sensörler

- 1 Adet Arduino Uno.
- 1 Adet Toprak Nem Sensörü.
- 1 Adet 6 V Mini Su Pompası.
- 1 Adet 5V Röle.
- 1 Adet LDR Işık Sensörü.
- 4 Adet 1,5V pil.
- 1 Adet Nokia 5110 LCD Ekran.
- 1 Adet ESP8266 Wifi Modülü.
- 1 Adet DHT11 Sıcaklık ve Nem Sensörü.


# 2. Projenin Genel Tanıtımı

Sistem üzerindeki toprak nem sensörü çiçeğin toprağına saplanır. Toprak nem sensörü
topraktaki nem oranını ölçer. Eğer nem oranı belirlenen nem oranından düşük bir değere
düşerse, röleye bağlı pompa çalıştırılarak toprağın sulanması sağlanır. DHT11 sıcaklık sensörü
kullanılarak bulunduğu ortamın nemi ve sıcaklığı ölçülür. LDR ışık sensörü kullanılarak bitkinin
bulunduğu ortamın ışık seviyesi ölçülür. Alının veriler LCD ekrana ve mobil uygulamaya yazdırılır.
Mobil uygulama üzerinden belirlenen nem oranı değiştirilebilir ve manuel sulama yapılabilir.

## 2.1. Proje nin Detaylı Anlatımı

## 2.1.1. Projenin Donanımı
### Projenin Donanım Görüntüsü
> ![image](https://user-images.githubusercontent.com/77435563/170590666-62eb84da-c354-4770-8cf3-c2a57045c64f.png)
<br/><br/>
### Toprak Nem Sensörü ve Su Motorunun Arduino Devre Şeması Bağlantısı
> ![image](https://user-images.githubusercontent.com/77435563/170590942-727d7c62-c31f-4d91-b0ef-eea24e951ee8.png)
<br/><br/>
### ESP8266 Wi-fi Modülünün Arduino Bağlantı Devre Şeması 
> ![image](https://user-images.githubusercontent.com/77435563/170591059-9e571f68-5cf9-4fe7-b553-81dd85c39d97.png)
<br/><br/>
### LDR ışık sensörünün Arduino Bağlantı Devre Şeması 
> ![image](https://user-images.githubusercontent.com/77435563/170591115-5951f344-6dfa-4d44-b3d5-d23d99fa9e06.png)
<br/><br/>
### DHT11 Sıcaklık Nem Sensörünün Bağlantı Devre Şeması
> ![image](https://user-images.githubusercontent.com/77435563/170591192-87f96f63-64f6-499b-b3a2-4bb6530cae3c.png)
 <br/><br/>

## -Sensörlerin Tanıtılması

```
Toprak Nem Sensörü, bitkinin bulunduğu topraktaki nem
değerine göre analog okuma yapar. İki bacağı vardır ve topraktaki
nem arttıkça iki bacağı arasındaki iletim artar. Böylece analog
değer de artmış olur.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592071-685c43ea-4ae5-4a56-809b-7d15f26b127d.png)
<br/>

```
Mini su pompası, 6V’luk enerji ile çalışır. Projede mini su
pompasını 4 adet AA pil ile çalıştırıyoruz. Toprağı sulamak için
kullanılmaktadır.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592185-babf8a27-f4fa-4b5f-89fb-1c572005654b.png)
<br/>

```
Röle, Su pompasını Arduino’dan çalıştırabilmek
için 5V’luk röle kullanıyoruz. Role Arduino’ya
bağlıdır. Bağlı olduğu dijital pine 5V gönderildiği
zaman röle aracılığıyla su pompasını çalıştırır.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592262-b839ac8b-eca0-48f2-8aa6-d4cfa22fff61.png)
<br/>

```
LDR, ortama gelen ışığın miktarına göre direnç değerini
değiştiren devre elemanıdır. Bitkinin alması gereken ışık
miktarını LDR ile alıyoruz.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592331-7982960b-4be7-43bb-8423-1c829ed9c445.png)

<br/>

```
Nokia 5110 LCD, 48x84 pixelden oluşan ekrana sahiptir.
Sensörden gelen verileri kullanıcıya göstermek için bu
LCD’yi kullanıyoruz.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592413-b1f76445-cef9-4e81-956b-ed163ad94a27.png)
<br/>

```
ESP 8266, elektronik projelerinde kullanılan internetten
veri alma ve göndermemizi sağlayan modüldür. AT
komutlarını kullanarak çalışır. Projemizde bitkiden
aldığımız verileri mobil uygulamaya göndermek ve mobil
uygulamadan veri almak için kullanıyoruz.
```

> ![image](https://user-images.githubusercontent.com/77435563/170592465-74b15202-c7af-4547-8a46-e9b9377645dd.png)
<br/>

```
DHT 11, ortamda algıladığı sıcaklık ve nem değerlerini
dijital olarak düzenleyip çıkış verebilen bir ısı
sensörüdür. Projemizde DHT 11 kullanarak bitkinin
bulunduğu odada gerekli olan sıcaklık ve nem
miktarını hesaplarız.
```
> ![image](https://user-images.githubusercontent.com/77435563/170592508-992cf20a-9644-469d-bab7-d1a6c1894ee9.png)
<br/>

## 2.1.2. Projede Kullanılan API Servisi

Projemizde Arduino’da bulunan sensörlerden aldığımız verileri depolamak için ThingSpeak adında API
servisi kullanıyoruz. Mobil uygulamadan da verileri Arduino’ya göndererek işlemler yapabiliyoruz.

**ThingSpeak Arayüzü**
> ![image](https://user-images.githubusercontent.com/77435563/170593705-e529ca8c-d44b-4f73-acab-89395b6ab73a.png)



## 2.1.3. Projenin Yazılımı

Projenin yazılımı 5 bölümden oluşmaktadır. Projenin ana kısmı olan “Main_IOT” kısmında
Arduino’ya bağlı sensörlerin ve diğer dosyaların projeye dahil edildiği yerdir.

“SetupEsp” Fonksiyonu, proje çalıştırıldığında bir kez çalıştırılarak ESP8266 modülünün ayarlarının
yapılmasını sağlar.

“PostSensorler” Fonksiyonu, sensörlerden verilerin alınarak API üzerinden ThingSpeak’ e
gönderilmesini ve verilerin LCD ye yazdırıldığı kısımdır.

“GetEsikDeger” Fonksiyonu, mobil uygulamamızdaki seçilmiş olan eşik değerini ThingSpeak API
servisinden getirir.

“GetManuelSulama” Fonksiyonu, Thingspeakdeki manuel sulama durumunu devamlı olarak kontrol
eder. Eğer uygulama üzerinden manuel sulama istenirse eşik değerine bakmaksızın 5 saniye boyunca
sulamayı gerçekleştirir.


## 2. 1. 4. Projenin Mobil Uygulaması
> ![image](https://user-images.githubusercontent.com/77435563/170593755-21273917-9752-495f-93b7-6541ae6f81af.png)

Mobil uygulamanın üst kısmında odanın sıcaklığı ,
odanın nem oranı, bitkinin aldığı ışık miktarı, toprağın nem
oranı görüntülenir.

Uygulamanın orta kısmındaki bar’ ı kullanarak
bitkinin hangi nem oranının altına düştüğünde
sulanmasının istenildiği seçilir bu seçilen değer ThingSpeak
deki esikdegeri’nin değerini değiştirir. Arduino devamlı
olarak ThingSpeak üzerindeki esikdegeri’ ni okuyup kodun
içerisine uygular. Eşik değeri değiştirildikten sonra 15
saniye boyunca eşik değeri değiştirilemez.

Uygulamanın en alt kısmındaki manuel sulama
butonuna basıldığında uygulama volley kütüphanesini
kullanarak APİ üzerinden ThingSpeak manuelsulama
değerini 1 olarak değiştirir. Arduino 30 saniye içerisinde
değeri ThingSpeakden okuyup sulamayı başlatır. Butona
basıldıktan sonra 60 saniye boyunca yeniden butona
basılamaz.


# 3. Sonuç

Projemiz sayesinde bitkinin insan müdahalesine gerek kalmadan IOT ile kendi nem oranında
sulanması sağlanıyor. Örneğin tatile çıktığınızda veya bitkinize ayıracak vaktiniz olmadığında,
bitkinizin verimli şekilde sulanmasını sağlar.

Bitki sulama projemiz her bitkiye özel olarak bitkinin ihtiyaç duyduğu nem oranında
kullanılabilir.

Bu proje demo olarak geliştirilmiş olsa da sera sistemleri, tarımcılık gibi birçok alanda
kullanılarak fazla su kullanımını engeller ve israfın önüne geçer.

Mobil uygulamamız her yaştaki kitleye hitap eden basit ve kullanışlı bir arayüze sahiptir.
Mobil uygulamamız sayesinden dünyanın her yerinden bitkinizin durumunu kontrol edip. Onun
hakkındaki bilgileri görebilir ve eşik değerini değiştirerek projemizi yönetebiliriz.






void SetupEsp();
void PostSensorler();
void GetEsikDeger();
void GetManuelSulama();
//String EsikDegerAl();

String responseEsikDeger,responseManuelSulama;

#include <SoftwareSerial.h>                                   //Gerekli kütüphaneleri ekliyoruz.
#include <Wire.h>
#include <dht11.h> 
#include <LCD5110_Basic.h>

LCD5110 lcd(7,8,9,11,10); 
extern uint8_t SmallFont[];
extern uint8_t MediumNumbers[];

String agAdi = "BEYAZIT 2.4";                      //Kablosuz Ağ adını buraya yazıyoruz.    
String agSifresi = "1212121212";                              //Ağ şifresini buraya yazıyoruz.
int rxPin = 3;                                               //ESP8266 TX pini
int txPin = 4;                                               //ESP8266 RX pini
String ip = "api.thingspeak.com";                                //Thingspeak sitesinin ip adresi
float sicaklik, nem;
float tnemDeger,ldrDeger;
long simdikiZaman,eskiZaman=0;
String esikDeger,manuelDeger="0";
dht11 DHT11;
int dht11Pin = 5;
int tnemPin=A0;
int ldrPin=A1;
int kontrolManuel=0;
String sonManuelDeger="0";
float tnemEsikDeger=1023.0;
float tempTnem;

SoftwareSerial esp(rxPin, txPin);                             //Seri haberleşme için pin ayarlarını yapıyoruz.


void setup() {
  lcd.InitLCD();
  Serial.begin(9600);
  esp.begin(115200); 
  pinMode(13,OUTPUT);
  pinMode(6,OUTPUT),
  pinMode(tnemPin,INPUT);//A0 toprak nem sensörünü analog giriş olarak ayarlar
  pinMode(ldrPin,INPUT);//A1 pinine bağlı ldr ışık sensörünü analog giriş olarak ayarlar
  digitalWrite(6,HIGH);
  SetupEsp();
}

void loop() {
  lcd.setFont(SmallFont); //lcd fontunu ayarla
  lcd.print("Ortam Nem:",0,0);
  lcd.print("O.Sicaklik:",0,15);  
  lcd.print("T.Nem:%",0,20); 
  lcd.print("Isik Mik:%",0,30); 

  PostSensorler();
  GetEsikDeger();
  GetManuelSulama();
  
  if(manuelDeger=="1" && !(sonManuelDeger=="1")){ //android tarafından manuel sulama istendiği zaman manuelDeger değişkeni 1 olduğu için eğer 1 ise ve o değerden önceki değer 0 ise ancak 
                                               //sulamayı başlatıyoruz çünkü android tarafında manuel sulama değişkeni 30sn boyunca 1 olarak kalıyor ve daha sonra 0 a çekiliyor
                                               //30 saniye içinde 2 kere manuel sulama değerinin 1 okunması halinde 2 kere sulama yapacağı için burada kontrol ediyoruz
    digitalWrite(6,LOW);
    delay(5000);
    digitalWrite(6,HIGH);
    }
    sonManuelDeger=manuelDeger;

    if(esikDeger == "0"){       //%20 seviyesi
      tnemEsikDeger=20;
    }
    else if(esikDeger == "1"){  //%40 seviyesi
      tnemEsikDeger=40;
    }
    else if(esikDeger == "2"){  //%60 seviyesi
      tnemEsikDeger=60;
    }

    if(tnemDeger<=tnemEsikDeger){
      digitalWrite(6,LOW);
      delay(5000);
      digitalWrite(6,HIGH);
    }


    
  //Serial.println("Baglantı Kapatildi.");
  //esp.println("AT+CIPCLOSE"); 
}

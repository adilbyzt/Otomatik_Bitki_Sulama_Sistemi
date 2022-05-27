void PostSensorler(){
  DHT11.read(dht11Pin);
  ldrDeger=100.0-((analogRead(ldrPin)*100.0)/1023.0);         //ışık sensöründen gelen veriyi % olarak göstermek için gelen degeri 100 ile çarpıp 1023 e bölüyoruz
  tnemDeger=100.0-((analogRead(tnemPin)*100.0)/1023.0); //toprak nem sensöründen gelen veriyi % olarak göstermek için gelen degeri 100 ile çarpıp 1023 e bölüyoruz
  sicaklik = (float)DHT11.temperature;
  nem = (float)DHT11.humidity;
  
  
  String veri = "GET https://api.thingspeak.com/update?api_key=KQ62K6LVTX4ZZIKF";   //Thingspeak komutu. Key kısmına kendi api keyimizi yazıyoruz.    //Göndereceğimiz sıcaklık değişkeni
  veri += "&field1=";
  veri += String(sicaklik);
  veri += "&field2=";
  veri += String(nem);                                        //Göndereceğimiz nem değişkeni
  veri += "&field3=";
  veri += String(tnemDeger);
  veri += "&field4=";
  veri += String(ldrDeger);
  veri += "\r\n\r\n"; 

  esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");           //Thingspeak'e bağlanıyoruz.
  if(esp.find("Error")){                                      //Bağlantı hatası kontrolü yapıyoruz.
    Serial.println("AT+CIPSTART Error");
    esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
  }
  
  esp.print("AT+CIPSEND=");                                   //ESP'ye göndereceğimiz veri uzunluğunu veriyoruz.
  esp.println(veri.length()+2);
  delay(100);

  simdikiZaman=millis();
  if((simdikiZaman-eskiZaman)>2000){
  eskiZaman=simdikiZaman;
  
  if(esp.find(">")){                                          //ESP8266 hazır olduğunda içindeki komutlar çalışıyor.
    esp.print(veri);                                          //Veriyi gönderiyoruz.
    //Serial.println(veri);
    Serial.print("Veri gonderildi. ");
    Serial.print("sicaklik:");
    Serial.print(sicaklik);
    Serial.print(" nem:");
    Serial.print(nem);
    Serial.print(" tnem:");
    Serial.print(tnemDeger);
    Serial.print(" ldr:");
    Serial.println(ldrDeger);
    lcd.printNumI(ldrDeger,62, 30);
  lcd.printNumI(tnemDeger,42, 20);
  lcd.printNumI(nem, 60, 0);
  lcd.printNumI(sicaklik, 67, 15);
  
  }
  }
  
}

void SetupEsp() {
  Serial.println("Baslatiliyor");
  esp.println("AT");
  Serial.println("AT gonderildi.");
  while(!esp.find("OK")){
    esp.println("AT");
    Serial.println("Baglanti yeniden kuruluyor");
    }
    Serial.println("ESP ile baglanti kuruldu");
    esp.println("AT+CWMODE=1");
    while(!esp.find("OK")){
      esp.println("AT+CWMODE=1");
      delay(100);
      Serial.println("Client ayari yapilamadi yeniden deneniyor");
     }
     Serial.println("Client olarak ayarlandi");
     esp.println("AT+CWJAP=\"" + agAdi + "\",\"" + agSifresi + "\"");
     while(!esp.find("OK"));
     Serial.println("Aga baglanildi.");
}

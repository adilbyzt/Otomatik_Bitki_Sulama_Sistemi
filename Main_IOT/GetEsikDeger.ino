void GetEsikDeger(){
  
     esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");           //Thingspeak'e bağlanıyoruz.
  while(!esp.find("OK")){                                      //Bağlantı hatası kontrolü yapıyoruz.
    //Serial.println("AT+CIPSTART Error 0");
    esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
    delay(1000);
  }
  esp.println("AT+CIPSEND=87");
  while((!esp.find("OK"))||(esp.find("SEND FAIL"))||(esp.find("ERROR"))){
  //Serial.println("CIPSEND AYARLANAMADI YENIDEN DENENIYOR");
  esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
          while(!esp.find("OK")){                                      //Bağlantı hatası kontrolü yapıyoruz.
          //Serial.println("AT+CIPSTART Error 1");
          esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
          //delay(1000);
          }
  esp.println("AT+CIPSEND=87");
  }
  delay(100);
  //Serial.println("CIPSEND 87 ayarlandi");
  
  
  
  String hostEsikDeger="GET /channels/1688783/fields/field1/last?api_key=FSYK2KYPP0W054QO&results=2";
  hostEsikDeger += "\r\n";
  hostEsikDeger += "Host:api.thingspeak.com";
  hostEsikDeger += "\r\n\r\n\r\n\r\n\r\n";

  responseEsikDeger="";
  esp.print(hostEsikDeger);
  while(!esp.find("SEND OK")){
    //Serial.println("Host gonderilemedi Yeniden deneniyor...");
    esp.println("AT+CIPCLOSE");
    esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80"); 
          while(!esp.find("OK")){                                      //Bağlantı hatası kontrolü yapıyoruz.
          //Serial.println("AT+CIPSTART Error 2");
          esp.println("AT+CIPCLOSE");
          esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
          //delay(1000);
          }
    esp.println("AT+CIPSEND=87");
    while((!esp.find("OK"))||(esp.find("SEND FAIL"))||(esp.find("ERROR"))){
    //Serial.println("CIPSEND AYARLANAMADI YENIDEN DENENIYOR");
    esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
          while(!esp.find("OK")){                                      //Bağlantı hatası kontrolü yapıyoruz.
          //Serial.println("AT+CIPSTART Error 3");
          esp.println("AT+CIPCLOSE");
          esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
          //delay(1000);
          }
          delay(100);
    esp.println("AT+CIPSEND=87");
    }
    delay(100);
    esp.print(hostEsikDeger);
    }
  long int ZamanEsikDeger = millis();

  while ((ZamanEsikDeger + 2000) > millis())
  {
    while (esp.available())
    {

      // esp'nin bağlantısından gelen veri var ise
      char c1 = esp.read(); //her bir karakteri okuyup c ye yazdırıyoruz.
      responseEsikDeger += c1; // her karakteri response değişkeninin sonuna ekliyor ve alacağımız teksti oluşturuyoruz
    }
  }
  int temp1=responseEsikDeger.indexOf("+IPD");  //gelen string veri içerisinden : nın bulunduğu konumu alır ve substring ile sadece istediğimiz veriyi alıyoruz
 esikDeger=responseEsikDeger.substring(temp1+7,temp1+8);
  Serial.print("esikDeger:");
  Serial.println(esikDeger);
  //Serial.print("---------------------------------");
  //Serial.println(responseEsikDeger);
  //Serial.print("---------------------------------");
  }
  

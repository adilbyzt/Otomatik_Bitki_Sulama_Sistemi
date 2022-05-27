void GetManuelSulama(){
  
  
     esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");           //Thingspeak'e bağlanıyoruz.
  if(esp.find("Error")){                                      //Bağlantı hatası kontrolü yapıyoruz.
    //Serial.println("AT+CIPSTART Error 0");
    esp.println("AT+CIPSTART=\"TCP\",\""+ip+"\",80");
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
  
  
  
  String hostManuelSulama="GET /channels/1688830/fields/field1/last?api_key=PBFT0S4TMKQASBHN&results=2";
  hostManuelSulama += "\r\n";
  hostManuelSulama += "Host:api.thingspeak.com";
  hostManuelSulama += "\r\n\r\n\r\n\r\n\r\n";

  responseManuelSulama="";
  esp.print(hostManuelSulama);

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
    esp.print(hostManuelSulama);
    }

  
  long int ZamanManuelSulama = millis();

  while ((ZamanManuelSulama + 2000) > millis())
  {
    while (esp.available())
    {

      // esp'nin bağlantısından gelen veri var ise
      char c2 = esp.read(); //her bir karakteri okuyup c ye yazdırıyoruz.
      responseManuelSulama += c2; // her karakteri response değişkeninin sonuna ekliyor ve alacağımız teksti oluşturuyoruz
    }
  }
  int temp2=responseManuelSulama.indexOf("+IPD");  //gelen string veri içerisinden : nın bulunduğu konumu alır ve substring ile sadece istediğimiz veriyi alıyoruz
 manuelDeger=responseManuelSulama.substring(temp2+7,temp2+8);
  Serial.print("manuelDeger:");
  Serial.println(manuelDeger);
  
  //if(manuelDeger==1){
    
    //}
  
  }
  

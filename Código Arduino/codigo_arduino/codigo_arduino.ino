#include <SoftwareSerial.h>   
#include <dht.h>
#include <Wire.h>
//Bibliotecas


long int data;                                                //dados recebidos via Bluetooth

int buzzer = 10;                                              //buzzer
int LED1 = 9;                                                 //|------
int LED2 = 8;                                                 //| Ferramentas Disponíveis (Ventilação, Rega e Luz)
int LED3 = 7;                                                 //|------

long int password1 = 1111;                                    //código para REFRESH
long int password2 = 2222;                                    //código para desligar AUTO

int code;                                                     //código int igual à "data" para poder ser alterado

boolean automatico = false;                                   //modo AUTO está ON ou OFF

float temp = 0;                                               //Temperatura

int num1=0;                                                   //|------
int num2=0;                                                   //| Numeros recebidos para modo automático
int num3=0;                                                   //|------

int horas = 0;                                                //|------
int minutos = 0;                                              //| Dados para o CLOCK
int seg = 0;                                                  //|------

int humSensor = A7;                                           //Declaração sensor de humidade do solo
int humSensorVal = 0;

int luzSensor = A8;                                           //Sensor de Luz                                
int lux=0;


dht DHT;                                                      //Objeto sensor de Temperatura
#define DHT11_PIN 3                                           //Pin sensor Temperatura


void setup() {  //--------------------------------------- //SETUP// ----------------------------------------

  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(buzzer,OUTPUT);                                     //inicializar tudo como output
  
  digitalWrite(LED1, LOW);
  digitalWrite(LED2, LOW);
  digitalWrite(LED3, LOW);
  Serial.begin(9600);
}



void loop() { //---------------------------------------- //MAIN LOOP// ---------------------------------------

  
  int chk = DHT.read11(DHT11_PIN);                             //|--------------------
  if ((DHT.temperature > -25) && (DHT.humidity > -25)) {       //|Ler sensor temperatura
    temp = constrain (DHT.temperature, -25, 70);               //|
  }                                                            //|--------------------

  humSensorVal = analogRead(humSensor) /8 ;                    //Ler valor da humidade do solo
  lux = analogRead(luzSensor);


//----- DATA RECEBIDA INICIO -----

  if (Serial.available()>0){                                    //|---------------------------------
    data = Serial.parseInt();                                   //|
                                                                //|    Os Dados do sensor
  if(data > 0){                                                 //|    Bluetooth estão a 
                                                                //|    ser lidos constantemente.
    //buzzer                                                    //|    Caso haja dados 
   for(int i=0;i<60;i++)                                        //|    disponíveis, então
   {                                                            //|    o controlador deverá 
    digitalWrite(buzzer,HIGH);                                  //|    dar uma indicação sonora
    delay(1);                                                   //|    e definir uma variável 
    digitalWrite(buzzer,LOW);                                   //|    "data" como valor 
    delay(1);                                                   //|    inteiro resultante.
    }                                                           //|    Esta variável será
    delay(50);                                                  //|    comparada com valores
       for(int i=0;i<60;i++)                                    //|    predefinidos para
   {                                                            //|    ser intrepretada.
    digitalWrite(buzzer,HIGH);                                  //|
    delay(1);                                                   //|    
    digitalWrite(buzzer,LOW);                                   //|
    delay(1);                                                   //|
    }                                                           //|
  }                                                             //|
    //fim buzzer                                                //|---------------------------------

    
  code=data;                                                    //"code" é uma replica de "data" para que possa ser manipulado


    Serial.print(data);
    Serial.print("\n");

  
  if (data==password1){             //REFRESH

    Serial.write("");                                           //|--------
    Serial.print((int)temp);                                    //|
    Serial.write(".");                                          //|
    Serial.print(humSensorVal);                                 //|  Enviar dados para o telemóvel
    Serial.write(".");                                          //|
    Serial.print(lux);                                          //|
    Serial.write(".");                                          //|--------

  }
 
  if (data==password2){             //automatico OFF

    automatico = false;
    seg=0;                                                        //|--------
    minutos=0;                                                    //| Dar reset ao Clock
    horas=0;                                                      //|--------

    digitalWrite(LED1,LOW);
    digitalWrite(LED2,LOW);
    digitalWrite(LED3,LOW);
  }
  
  if (data>3000 && data <4000){     //automatico ON   //  data = 3 a b c --> parametros(num1, num2 ,num3)
    code = code - 3000;
    automatico = true;
    
    if(code>200 && code <400){code = code-200; num1 = 2;}         //|--------------
    if(code>400 && code <600){code = code-400; num1 = 4;}         //|
    if(code>600 && code <800){code = code-600; num1 = 6;}         //|   Valor "code"
    if(code>800 && code <900){code = code-800; num1 = 8;}         //|   é manipulado
                                                                  //|   e são-lhe 
    if(code>20 && code <40){code = code-20; num2 = 2;}            //|   retirados os
    if(code>40 && code <60){code = code-40; num2 = 4;}            //|   paramentros 
    if(code>60 && code <80){code = code-60; num2 = 6;}            //|   para o 
    if(code>80 && code <90){code = code-80; num2 = 8;}            //|   funcionamento
                                                                  //|   do modo AUTO
    if(code==2){num3 = 2;code=0;}                                 //|
    if(code==4){num3 = 4;code=0;}                                 //|
    if(code==6){num3 = 6;code=0;}                                 //|
    if(code==8){num3 = 8;code=0;}                                 //|--------------
  }



  if(code > 4000 && code < 5000){                                 //Ligar componente individualmente
    code =  code - 4000;

    if(code > 99 && code < 200){                                  //| O principio anterior é aplicado, porém
      code=code-100;                                              //| apenas para se descobrir cual o componente e se se pretende ligar ou desligar 
      if(code == 10){
         digitalWrite(LED1,HIGH); 
      }
      else{
        digitalWrite(LED1,LOW);
      }
    }
    if(code > 199 && code < 300){
      code=code-200;
      if(code == 10){
         digitalWrite(LED2,HIGH); 
        delay(2000);
        digitalWrite(LED2,LOW);
      }
    }
    if(code > 299 && code < 400){
      code=code-300;
      if(code == 10){
         digitalWrite(LED3,HIGH); 
      }
      else{
        digitalWrite(LED3,LOW);
      }
    }
    
  }
}

//----- DATA RECEBIDA FIM -----





if(automatico){
       //------------------------------- CLOCK ---------------------
      
      delay(1);                                                    //variavel deverá ser adaptada de acordo com o tempo do clock
       
      if(seg==59){                                                //|----------------------
        seg = 0;                                                  //|
        if(minutos == 59){                                        //|   Clock adiciona um segundo
          minutos = 0;                                            //|   por cada ciclo da placa.
          if(horas == 23){                                        //|   A cada 60 seg adiciona um minuto
            horas = 0;                                            //|   e a cada 60 min uma hora.
          }                                                       //|   O delay faz com que cada ciclo
          else{                                                   //|   tenha exatamente 1 segundo.
                 horas = horas + 1;                               //|
                                                                  //|-----------------------
                   for(int i=0;i<30;i++)
                   {
                    digitalWrite(buzzer,HIGH);
                    delay(1);//wait for 1ms
                    digitalWrite(buzzer,LOW);
                    delay(1);//wait for 1ms
                    }
                
          }
        }
        else{
          minutos = minutos + 1;
          Serial.print(horas);
          Serial.print(":");
          Serial.print(minutos);
          Serial.print("\n");
        }
      }
      else{
        seg = seg + 1;
      }//----------------------------FIM CLOCK ---------------------
}

                                                                  //|-----------------------------------
                                                                  //|
                                                                  //|     Ciclo lê as horas atuais, 
                                                                  //|     caso o modo automático esteja
                                                                  //|     ligado. 
                                                                  //|
                                                                  //|     Este deverá ligar e desligar os 
                                                                  //|     diversos componentes em horas predefinidas, de modo
                                                                  //|     a que as horas desejadas sejam uniformemente
                                                                  //|     distribuidas pelo dia.
                                                                  //|
                                                                  //|     O ciclo começa sempre na hora zero,
                                                                  //|     independentemente da hora do dia.
                                                                  //|   
                                                                  //|----------------------------------- 


if(seg==1 && minutos ==0){

if(automatico && horas==0){   //HORA 0
    digitalWrite(LED1,HIGH);
    digitalWrite(LED3,HIGH);
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
}

if(automatico && horas==1){   //HORA 1
    digitalWrite(LED1,LOW);
    digitalWrite(LED3,LOW);
}

                              //HORA 2 n/a

if(automatico && horas==3){   //HORA 3
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==4){   //HORA 4
  if(num1 == 6){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }
  
  if(num2 == 6){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==5){   //HORA 5
  if(num1 == 6){
    digitalWrite(LED1,LOW);
  }
  if(num2 == 6){
    //digitalWrite(LED2,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==6){   //HORA 6
  if(num1 == 4){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }
  if(num2 == 4){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num3 == 4){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==7){   //HORA 7
  if(num1 == 4){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }
    if(num2 == 4){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }
    if(num3 == 4){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==8){   //HORA 8
  if(num1 == 6){
    digitalWrite(LED1,HIGH);
  }
  if(num2 == 6){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==9){   //HORA 9
  if(num1 == 6){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }

    if(num2 == 6){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }

    if(num3 == 6){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==10){   //HORA 10
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }

  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }

  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

                               //HORA 11 n/a

if(automatico && horas==12){   //HORA 12
  if(num1 == 2){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 4){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 6){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }

  if(num2 == 2){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 4){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 6){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }

  if(num3 == 2){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 4){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 6){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==13){   //HORA 13
  if(num1 == 2){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 4){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 6){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }

    if(num2 == 2){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 4){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 6){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }

    if(num3 == 2){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 4){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

                               //HORA 14 n/a

if(automatico && horas==15){   //HORA 15
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==16){   //HORA 16
  if(num1 == 6){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }
  if(num2 == 6){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==17){   //HORA 17
  if(num1 == 6){
    digitalWrite(LED1,LOW);
  }
  if(num2 == 6){
    //digitalWrite(LED2,LOW);
  }
  if(num3 == 6){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==18){   //HORA 18
  if(num1 == 4){
    digitalWrite(LED1,HIGH);
  }
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }

  if(num2 == 4){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }

  if(num3 == 4){
    digitalWrite(LED3,HIGH);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==19){   //HORA 19
  if(num1 == 4){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }

  if(num2 == 4){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }

  if(num3 == 4){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}

if(automatico && horas==20){   //HORA 20
  if(num1 == 6){
    digitalWrite(LED1,HIGH);
  }

  if(num2 == 6){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }

  if(num3 == 6){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==21){   //HORA 21
  if(num1 == 6){
    digitalWrite(LED1,LOW);
  }
  if(num1 == 8){
    digitalWrite(LED1,HIGH);
  }

  if(num2 == 6){
    //digitalWrite(LED2,LOW);
  }
  if(num2 == 8){
    digitalWrite(LED2,HIGH);
    delay(1000);
    digitalWrite(LED2,LOW);
  }

  if(num3 == 6){
    digitalWrite(LED3,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,HIGH);
  }
}

if(automatico && horas==22){   //HORA 22
  if(num1 == 8){
    digitalWrite(LED1,LOW);
  }
  if(num2 == 8){
    //digitalWrite(LED2,LOW);
  }
  if(num3 == 8){
    digitalWrite(LED3,LOW);
  }
}
}

                               //HORA 23 n/a











}   //----------------------------------------------------------------------------------------- FIM LOOP ----------------

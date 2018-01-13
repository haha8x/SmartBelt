/* Smart Belt by Group 3 - Final Project of IOT Class - NCKU 2017*/

//add library
#include <Adafruit_ADXL345_U.h> //accelerometer sensor

//initialize variable for sensor
Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified(12345);

int x; //x value accelerometer
int y; //y value accelerometer
int z; //z value accelerometer
float ans_algo1 = 0.0;
float ans_algo2 = 0.0;

void setup() {
  Serial.begin(9600); 
  if(!accel.begin()) {while(1);} //check accelerometer sensor on
  accel.setRange(ADXL345_RANGE_16_G); //set accelerometer range to 16G for checking fast movement
  accel.setDataRate(ADXL345_DATARATE_50_HZ); //set accelerometer datarate to 50hz  
}


void loop() {
  //start reading accelerometer value
  sensors_event_t event; 
  accel.getEvent(&event);

  //Serial.print("X");
//  Serial.print(event.acceleration.x); //send x accelerometer value to phone
//  Serial.print(","); 
//  Serial.print(event.acceleration.y); //send y accelerometer value to phone
//  Serial.print(","); 
//  Serial.print(event.acceleration.z); //send z accelerometer value to phone  
  
  /* This is for EMG muscle Analog data. */
  // read the input on analog pin x:
  int EMG1 = analogRead(A0);
  int EMG2 = analogRead(A1);
  // Convert the analog reading (which goes from 0 - 1023) to a voltage (0 - 5V):
//  float vEMG1 = EMG1 * (5.0 / 1023.0);
//  float vEMG2 = EMG2 * (5.0 / 1023.0);
  // print out the value you read:
  
//  Serial.print(",");
  Serial.print(EMG1);
  Serial.print(",");
  Serial.print(EMG2);
//  Serial.print(",vEMG1");
//  Serial.print(vEMG1);
//  Serial.print(",vEMG2");
//  Serial.print(vEMG2);
  
    ans_algo1 = event.acceleration.x - event.acceleration.z;
//  ans_algo2 = abs(log((z-x)*(z-x)));
//
    Serial.print(",");  
    Serial.println(ans_algo1);
//  Serial.print(",ans_algo2");  
//  Serial.println(ans_algo2);

    delay(100);
}


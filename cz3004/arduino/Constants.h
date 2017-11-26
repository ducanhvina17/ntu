#ifndef CONSTANTS_H
#define CONSTANTS_H

#include "Arduino.h"

class Constants {
public:
	// Digital Pins
	// Digital 0   RX
	// Digital 1   TX
	// Digital 2   M1INA	     Motor 1 direction input A
	static const unsigned char INA1 = 2;
	// Digital 3  
	static const unsigned char ML_ENCODER_A = 3;
	// Digital 4   M1INB	     Motor 1 direction input B
	static const unsigned char INB1 = 4;
	// Digital 5   
 static const unsigned char ML_ENCODER_B = 5;
	// Digital 6   M1EN/DIAG     Motor 1 enable input/fault output
	// Digital 7   M2INA    	 Motor 2 direction input A
	static const unsigned char INA2 = 7;
	// Digital 8   M2INB    	 Motor 2 direction input B
	static const unsigned char INB2 = 8;
	// Digital 9   M1PWM	     Motor 1 speed input
	// Digital 10  M2PWM    	 Motor 2 speed input
	// Digital 11  
	// Digital 12  M2EN/DIAG     Motor 1 enable input/fault output
	// Digital 13
	static const unsigned char MR_ENCODER_A = 13;
 static const unsigned char MR_ENCODER_B = 11;
	// Analog Pins for Sensors
	// Analog A0 
	static const unsigned char FR = A0;
	// Analog A1
  static const unsigned char CN = A1;
	// Analog A2
  static const unsigned char FL = A2;
	// Analog A3
  static const unsigned char SL = A3;
	// Analog A4
  static const unsigned char SR = A4;
  // Model Number
  static const unsigned int model = 1080;
  
	static const bool isDebug = false;
};

#endif



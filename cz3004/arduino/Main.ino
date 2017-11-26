#include "Arduino.h"
#include "Constants.h"
#include "PinChangeInt.h"
#include "MotionController.h"
#include "DualVNH5019MotorShield.h"
#include "SharpIR.h"


/*---------------------------------------- Motor ----------------------------------------*/
DualVNH5019MotorShield md;
MotionController* motionController;


/*---------------------------------------- Sensors ----------------------------------------*/
#define model 1080  // Sensor model

// Pin
#define FR A0  // FrontRight IR sensor
#define CN A1  // Center IR sensor
#define FL A2  // FrontLeft IR sensor
#define SL A4  // SideLeft IR sensor
#define SR A3  // SideRight IR sensor

// Obstacle reading directly in front (in cm)
#define TL_OFFSET 12
#define LF_OFFSET 10
#define MF_OFFSET 7
#define RF_OFFSET 10
#define TR_OFFSET 12

SharpIR FrontRight(FR, model);
SharpIR Center(CN, model);
SharpIR FrontLeft(FL, model);
SharpIR SideRight(SR, model);
SharpIR SideLeft(SL, model);

float dFR, dCN, dFL, dSR, dSL;
int rFR, rCN, rFL, rSR, rSL;

const int numreadingsMove = 35;  // number of sensor readings to take for mean distance
const int numreadingsCal = 9;
const int diff = 5;  // the difference between two consecutive measurements to be taken as valid

int TotalDoubleChk = 0;
int TotalTripleChk = 0;

boolean rightflag=false;


/*---------------------------------------- Main loop ----------------------------------------*/
void setup()
{
  Serial.begin(115200);
  PCintPort::attachInterrupt(Constants::ML_ENCODER_A, MotionController::MLCountInc, CHANGE);
  PCintPort::attachInterrupt(Constants::MR_ENCODER_A, MotionController::MRCountInc, CHANGE);
  motionController = new MotionController();

  pinMode(FR, INPUT);
  pinMode(FL, INPUT);
  pinMode(CN, INPUT);
  pinMode(SR, INPUT);
  pinMode(SL, INPUT);
  
  Serial.println("Arduino is ready.");
}

void loop()
{
  waitingCommand();
}


/*---------------------------------------- Communication Functions ----------------------------------------*/
void waitingCommand()
{
  if (Serial.available() > 0)
  {
    char receivedChar = Serial.read();
    executeCommand(receivedChar);
  }
}

void executeCommand(char receivedChar)
{
  if (receivedChar == 'w')  // Forward
  {
    motionController->moveForwardTicks();
    calibrate();
    getAllDist();
  }
  else if (receivedChar == 'a')  // Turn left
  {
    motionController->leftTurn();
    delay(100);
    calibrate();
    getAllDist();
  }
  else if (receivedChar == 'd')  // Turn right
  {
    motionController->rightTurn();
    delay(100);
    calibrate();
    getAllDist();
  }
  else if (receivedChar == 'q')  // Left turn without sending sensor data
  {
    motionController->leftTurn();
    delay(100);
    calibrate();
  }
  else if (receivedChar == 'r') {   // Right turn without sending sensor data
    motionController->rightTurn();
    delay(100);
    calibrate();
  }
  else if (receivedChar == 'l')  // Move robot to corner 10 to 30cm away. Prevent off grid.
  {
    checkFrontProx();
    calibrate();
  }
  else if (receivedChar == 'p')  // Move robot to corner 10 to 60cm away. Prevent off grid. Prepare for shortest path
  {
    checkFrontProx2();
    calibrate();
    calibrate();
    motionController->rightTurn();
    checkFrontProx2();
    calibrate();
    calibrate();
    motionController->rightTurn();
  }
  else if(receivedChar == 'b')
    rightflag=true;
  else if (receivedChar == 'y')  // For shortest path string to goal
  {
    Serial.setTimeout(5000);
    String moveString = Serial.readStringUntil('+');
    Serial.setTimeout(1000);
    readingString(moveString);
  }
  else if (receivedChar == 't')  // For shortest path string back to start after exploration
  {
    Serial.println("z");
    Serial.setTimeout(5000);
    String moveString = Serial.readStringUntil('+');
    Serial.setTimeout(1000);
    readingString(moveString);
  }
  // Debug commands
  else if (receivedChar == 'v')  // Side calibration
  {
    sideCalibrate();
  }
  else if (receivedChar == 'c') {
    calibrate();
    Serial.println("cd");
  }
  else if (receivedChar == 'e')
  {
    getAllDist();
  }
  else if (receivedChar == 's')
  {
    motionController->moveBackwardTicks();
    delay(50);
    calibrate();
    getAllDist();
  }

  receivedChar = 0;
}


/*---------------------------------------- Shortest Path Execution ----------------------------------------*/
void readingString(String moveString)
{
  String intCommand = "";

  for (int i = 0; i < moveString.length(); i++)
  {
    if (isdigit(moveString.charAt(i)))
      intCommand += moveString.charAt(i);
    else if (isalpha(moveString.charAt(i)))
    {
      if (intCommand.length() > 0)
      {
        int num = intCommand.toInt();
        motionController->moveSteps(num);
        intCommand = "";
        orientationCalibrate();   //calibrate orientation of robot first
        forbackwardCalibrate();   //calibrate position  
        delay(268);
      }

      execString(moveString.charAt(i));
    }
  }

  if (intCommand.length() > 0)
  {
    int num = intCommand.toInt();
    motionController->moveSteps(num);
    delay(50);
  }
}

void execString(char command)
{
  if (command == 'a')  // Turn left
  {
    motionController->leftTurn();
    orientationCalibrate();   //calibrate orientation of robot first
    forbackwardCalibrate();   //calibrate position  
    delay(268);
  }
  else if (command == 'd')  // Turn right
  {
    motionController->rightTurn();
    orientationCalibrate();   //calibrate orientation of robot first
    forbackwardCalibrate();   //calibrate position  
    delay(268);
  }
  else if (command == 's')  // Backward
  {
    motionController->moveBackwardTicks();
    delay(50);
  }
}


/*---------------------------------------- Calibration Functions ----------------------------------------*/
void calibrate()
{
  orientationCalibrate();   //calibrate orientation of robot first
  forbackwardCalibrate();   //calibrate position  
  sideCalibrate();          //check left sensor. if too far from wall, turn and calibrate and turn back
}

/* Calibrate orientation. */
void orientationCalibrate()
{
  getDistLessMedian(1, 0, 1, 0, 0);  // FR, FL

  if (abs(dFL - dFR) < 9 && dFL <= 16 && dFR <= 16)
    posCalibration();
}

/* Position and orientation calibration. */
void posCalibration()
{
  int ccounter = 0;
  const int threshold = 13;
  int diffAnalogLR;
  int left = analogRead(FL) - 10;
  int right = analogRead(FR);

  diffAnalogLR = left - right;

  // Repeat the calibration if still out of bound for 10 times
  while (abs(diffAnalogLR) > threshold)
  {
    left = analogRead(FL) - 10;
    right = analogRead(FR);

    diffAnalogLR = left - right;

    if (ccounter == 10)
    {
      Serial.println("break calibrate");
      break;
    }

    if (diffAnalogLR > threshold)  // Right analog value higher, right closer to obstacle. Rotate clockwise
    {
      motionController->calibrateLeftTurn();
      delay(100);
      ccounter++;
    }
    else if (diffAnalogLR < -threshold)  // Left analog value higher, left closer to obstacle. rotate anticlockwise
    {
      motionController->calibrateRightTurn();
      delay(100);
      ccounter++;
    }
  }
}

/* Make robot 10cm from wall. */
void forbackwardCalibrate()
{
  getDistLessMedian(1, 0, 1, 0, 0);  // FR, FL

  if ((dFR > 10.85 && dFR < 14.4) || (dFL > 10.85 && dFL < 14.4))
  {
    while (dFR > 11.4 || dFL > 11.4)
    {
      if (dFR < 11.4 || dFL < 11.4)
        break;

      motionController->calibrateForward();

      delay(50);
      getFront();
    }
  }

  if ((dFR <= 9.7) || (dFL <= 9.7))
  {
    while (dFR < 9.7 || dFL < 9.7)
    {
      motionController->calibrateBackward();
      delay(50);
      getFront();
    }
  }
}

void sideCalibrate()
{
  getDistLessMedian(0, 0, 0, 1, 1);  // SL

  if (dSL < 12.5 || (dSL > 15 && dSL < 17.3))
  {
    delay(100);
    motionController->leftTurn();
    delay(100);
    orientationCalibrate();
    forbackwardCalibrate();
    delay(50);
    motionController->rightTurn();
    return;
  }
  
  else if (rightflag==true && (dSR < 12.5 || (dSR > 15 && dSR < 17.3) ))
  {
    delay(100);
    motionController->rightTurn();
    delay(100);
    orientationCalibrate();
    forbackwardCalibrate();
    delay(50);
    motionController->leftTurn();
  }
  
}

/* If robot 10 to 30cm from wall, move robot to 10cm from wall. */
void checkFrontProx()
{
  getDistLessMedian(0, 1, 0, 0, 0);  // CN

  while (dCN >= 10 && dCN <= 30)
  {
    if (dCN == 10)
      break;

    motionController->calibrateForward();
    delay(100);
    getDistLessMedian(0, 1, 0, 0, 0);  // CN
  }
}

/* If robot 10 to 60cm from wall, move robot to 10cm from wall. */
void checkFrontProx2()
{
  getDistLessMedian(0, 1, 0, 0, 0);  // CN

  while (dCN >= 10 && dCN <= 60)
  {
    if (dCN == 10)
      break;

    motionController->calibrateForward();
    delay(100);
    getDistLessMedian(0, 1, 0, 0, 0);  // CN
  }
}


/*---------------------------------------- Sensor Functions ----------------------------------------*/
int getAnalogReading(int ir)
{
  analogRead(ir);  // Ditch

  int sum = analogRead(ir);
  sum += analogRead(ir);
  sum += analogRead(ir);
  sum += analogRead(ir);

  return (sum / 4);  // Average
}

void getSide()
{
  getDist(0, 0, 0, 1, 1);
}

void getFront()
{
  getDist(1, 1, 1, 0, 0);
}

void getAllDist()
{
  getDist(1, 1, 1, 1, 1);
  sendSensorString();
}

void sendSensorString()
{
  rounding();
  Serial.print("LF");  Serial.print(rFL - 9);  Serial.print("|");
  Serial.print("MF");  Serial.print(rCN - 7);  Serial.print("|");
  Serial.print("RF");  Serial.print(rFR - 10);  Serial.print("|");
  Serial.print("TR");  Serial.print(rSR - 11);  Serial.print("|");
  Serial.print("TL");  Serial.print(rSL - 13);  Serial.println("|");
}

void getDistLessMedian(int irFR, int irCN, int irFL, int irSR, int irSL)
{
  float frArray[numreadingsCal], cnArray[numreadingsCal], flArray[numreadingsCal], srArray[numreadingsCal], slArray[numreadingsCal];

  for (int i = 0; i < numreadingsCal; i++)
  {
    if (irFR == 1)
      frArray[i] = FrontRight.distance();

    if (irCN == 1)
      cnArray[i] = Center.distance();

    if (irFL == 1)
      flArray[i] = FrontLeft.distance();

    if (irSR == 1)
      srArray[i] = SideRight.distance();

    if (irSL == 1)
      slArray[i] = SideLeft.distance();
  }

  if (irFR == 1)
  {
    Quicksort(frArray, 0, numreadingsCal - 1);
    dFR = frArray[numreadingsCal / 2];
  }

  if (irCN == 1)
  {
    Quicksort(cnArray, 0, numreadingsCal - 1);
    dCN = cnArray[numreadingsCal / 2];
  }

  if (irFL == 1)
  {
    Quicksort(flArray, 0, numreadingsCal - 1);
    dFL = flArray[numreadingsCal / 2];
  }

  if (irSR == 1)
  {
    Quicksort(srArray, 0, numreadingsCal - 1);
    dSR = srArray[numreadingsCal / 2];
  }

  if (irSL == 1)
  {
    Quicksort(slArray, 0, numreadingsCal - 1);
    dSL = slArray[numreadingsCal / 2];
  }
}

int getDist(int irFR, int irCN, int irFL, int irSR, int irSL)
{
  const int TWO_READ_DIST = 6;

  getAllMedianReadings(irFR, irCN, irFL, irSR, irSL);

  if (irFR == 1)
  {
    if (abs(dFR - getMedianReadings(1, 0, 0, 0, 0)) <= TWO_READ_DIST)
      return dFR;

    dFR = getMedianReadings(1, 0, 0, 0, 0);

    if (abs(dFR - getMedianReadings(1, 0, 0, 0, 0)) <= TWO_READ_DIST)
      return dFR;
    else
    {
      dFR = getMedianReadings(1, 0, 0, 0, 0);
      return dFR;
    }
  }

  if (irCN == 1)
  {
    if (abs(getMedianReadings(0, 1, 0, 0, 0)) <= TWO_READ_DIST)
      return dCN;

    dCN = getMedianReadings(0, 1, 0, 0, 0);

    if (abs(dCN - getMedianReadings(0, 1, 0, 0, 0)) <= TWO_READ_DIST)
      return dCN;
    else
    {
      dCN = getMedianReadings(0, 1, 0, 0, 0);
      return dCN;
    }
  }

  if (irFL == 1)
  {
    if (abs(dFL - getMedianReadings(0, 0, 1, 0, 0)) <= TWO_READ_DIST)
      return dFL;

    dFL = getMedianReadings(0, 0, 1, 0, 0);

    if (abs(dFL - getMedianReadings(0, 0, 1, 0, 0)) <= TWO_READ_DIST)
      return dFL;

    else
    {
      dFL = getMedianReadings(0, 0, 1, 0, 0);
      return dFL;
    }
  }

  if (irSR == 1)
  {
    if (abs(dSR - getMedianReadings(0, 0, 0, 1, 0)) <= TWO_READ_DIST)
      return dSR;

    dSR = getMedianReadings(0, 0, 0, 1, 0);

    if (abs(dSR - getMedianReadings(0, 0, 0, 1, 0)) <= TWO_READ_DIST)
    {
      return dSR;
    else
    {
      dSR = getMedianReadings(0, 0, 0, 1, 0);
      return dSR;
    }
  }

  if (irSL == 1)
  {
    if (abs(dSL - getMedianReadings(0, 0, 0, 0, 1)) <= TWO_READ_DIST)
      return dSL;

    dSL = getMedianReadings(0, 0, 0, 0, 1);

    if (abs(dSL - getMedianReadings(0, 0, 0, 0, 1)) <= TWO_READ_DIST)
      return dSL;
    else
    {
      dSL = getMedianReadings(0, 0, 0, 0, 1);
      return dSL;
    }
  }

  Serial.println("Error. You fucked up. Invalid pin");
}

float getMedianReadings(int irFR, int irCN, int irFL, int irSR, int irSL)
{
  float sensorArray[numreadingsMove];

  for (int i = 0; i < numreadingsMove; i++)
  {
    if (irFR == 1)
      sensorArray[i] = FrontRight.distance();
    else if (irCN == 1)
      sensorArray[i] = Center.distance();
    else if (irFL == 1)
      sensorArray[i] = FrontLeft.distance();
    else if (irSR == 1)
      sensorArray[i] = SideRight.distance();
    else if (irSL == 1)
      sensorArray[i] = SideLeft.distance();
    else
      return 999;  }

  Quicksort(sensorArray, 0, numreadingsMove - 1);
  return sensorArray[numreadingsMove / 2];
}

void getAllMedianReadings(int irFR, int irCN, int irFL, int irSR, int irSL)
{
  float frArray[numreadingsMove], cnArray[numreadingsMove], flArray[numreadingsMove], srArray[numreadingsMove], slArray[numreadingsMove];

  for (int i = 0; i < numreadingsMove; i++)
  {
    if (irFR == 1)
      frArray[i] = FrontRight.distance();

    if (irCN == 1)
      cnArray[i] = Center.distance();

    if (irFL == 1)
      flArray[i] = FrontLeft.distance();

    if (irSR == 1)
      srArray[i] = SideRight.distance();

    if (irSL == 1)
      slArray[i] = SideLeft.distance();
  }

  if (irFR == 1)
  {
    Quicksort(frArray, 0, numreadingsMove - 1);
    dFR = frArray[numreadingsMove /2];
  }

  if (irCN == 1)
  {
    Quicksort(cnArray, 0, numreadingsMove - 1);
    dCN = cnArray[numreadingsMove / 2];
  }

  if (irFL == 1)
  {
    Quicksort(flArray, 0, numreadingsMove - 1);
    dFL = flArray[numreadingsMove / 2];
  }

  if (irSR == 1)
  {
    Quicksort(srArray, 0, numreadingsMove - 1);
    dSR = srArray[numreadingsMove / 2];
  }

  if (irSL == 1)
  {
    Quicksort(slArray, 0, numreadingsMove - 1);
    dSL = slArray[numreadingsMove / 2];
  }
}

void rounding()
{
  rFL = dFL + 0.5;
  rCN = dCN + 0.5;
  rFR = dFR + 0.5;
  rSR = dSR + 0.5;
  rSL = dSL + 0.5;
}

int Partition(float arr[], int left, int right)
{
  int mid = (left + right) / 2;
  Swap(arr, left, mid);

  int pivot = left;
  int last = left;

  for (int i = left + 1; i <= right; i++)
  {
    if (arr[i] < arr[pivot])
    {
      last++;
      Swap(arr, i, last);
    }
  }

  Swap(arr, pivot, last);

  return last;
}

void Quicksort(float arr[], int left, int right)
{
  if (left >= right)
    return;

  int pivot = Partition(arr, left, right);
  Quicksort(arr, left, pivot - 1);
  Quicksort(arr, pivot + 1, right);
}

void Swap(float a[], int i, int j)
{
  int temp = a[i];
  a[i] = a[j];
  a[j] = temp;
}


#include "MotionController.h"

#define FR A0 //FrontRight IR sensor
#define CN A1 //Center IR sensor
#define FL A2 //FrontLeft IR sensor
#define SL A4 //SideLeft IR sensor
#define SR A3 //SideRight IR sensor

volatile long MotionController::MLCount;
volatile long MotionController::MRCount;
int spinTimes = 0;
int slipCount = 0;

const int FL_ANALOG = 490;
const int CN_ANALOG = 620;
const int FR_ANALOG = 480;

MotionController::MotionController()
{
  MotionController::MLCount = 0;
  MotionController::MRCount = 0;
  this->output = 0;
  this->motorShield.init();
  this->initPid();
}

void MotionController::moveForward()
{
	//  int curMoveSpeed = 0;
	//  while(curMoveSpeed != 200)
	// {
	//    motorShield.setSpeeds((curMoveSpeed +=50) + this->output, 200 - this->output);
	//    updatePid();
	// }
	this->pid->SetTunings(0.5, 3.5, 0); // 1.1=, 1, 0
	motorShield.setSpeeds(325 + (this->output), 325 - (this->output)); //260 282 280
	if (MotionController::MRCount % 450 == 0)
	{
		MotionController::MRCount = MotionController::MRCount + 3;
	}
	updatePid();

	/*Serial.print("Left: ");
	Serial.print(MotionController::MLCount);
	Serial.print(", Right: ");
	Serial.print(MotionController::MRCount);
	Serial.print(", output: ");
	Serial.println(this->output);*/
}

void MotionController::moveBackward()
{
  motorShield.setSpeeds(-300 - this->output, -300 + this->output);
  updatePid();
  //    Serial.print("Left: ");
  //    Serial.print(MotionController::MLCount);
  //    Serial.print(", Right: ");
  //    Serial.print(MotionController::MRCount);
  //    Serial.print(", output: ");
  //    Serial.println(this->output);
}

void MotionController::moveForwardTicks()
{
	stop = false;
	spinTimes = 0;
	resetCounts();
	this->motorShield.setBrakes(392, 383);
	while (!stop)
	{
		moveForward();
		if (MotionController::MRCount > forwardTicks  /*|| analogRead(FL)>FL_ANALOG || analogRead(CN)> CN_ANALOG || analogRead(FR)>FR_ANALOG*/) //540 //610 //600 //583 //585
		{
			this->motorShield.setBrakes(392, 383); //370 350
			spinTimes++;
			resetCounts();
		}

		if (spinTimes == 1)
		{
			stop = true;
		}

		if (analogRead(FL)>FL_ANALOG || analogRead(CN)> CN_ANALOG || analogRead(FR)>FR_ANALOG) {//A2=FL, A1=CN,  AO=FR
			stop = true;
			this->motorShield.setBrakes(390, 370);
			Serial.println("ebrake engaged.");
		}
	}
}

void MotionController::moveBackwardTicks()
{
  stop = false;
  spinTimes = 0;
  delay(250);
  while (!stop)
  {
    moveBackward();
    if (MotionController::MRCount > 540)
    {
      for (int i = 0; i<30000; i++)
      {
        this->motorShield.setBrakes(400, 400);
      }
      spinTimes++;
      resetCounts();
    }
    if (spinTimes == 1)
    {
      stop = true;
    }
  }
}

void MotionController::rightTurn()
{
    stop = false;
    spinTimes = 0;
    resetCounts();
    this->motorShield.setBrakes(350, 360);
    while (!stop)
    {
        this->pid->SetTunings(0.5, 3.2, 0); //1.5
        this->motorShield.setSpeeds(325 + this->output, -325 + this->output); //300
        if(MotionController::MRCount % 400  == 0)
        {
          MotionController::MRCount = MotionController::MRCount + 2;
        }
        updatePid();
        //Serial.print("Left: ");
        //Serial.print(MotionController::MLCount);
        //Serial.print(", Right: ");
        //Serial.print(MotionController::MRCount);
        //Serial.print(", output: ");
        //Serial.println(this->output);
        if (MotionController::MRCount > 729) //780 //788 //792 //790 //794
        {
            this->motorShield.setBrakes(350, 360);
            spinTimes++;
            resetCounts();
        }
        if (spinTimes == 1)
        {
            stop = true;
        }
    }
}
  /*this->motorShield.setSpeeds(300 + this->output, -300 + this->output);
  updatePid();
  Serial.print("Left: ");
  Serial.print(MotionController::MLCount);
  Serial.print(", Right: ");
  Serial.print(MotionController::MRCount);
  Serial.print(", output: ");
  Serial.println(this->output); */


void MotionController::leftTurn()
{
    stop = false;
    spinTimes = 0;
    resetCounts();
    this->motorShield.setBrakes(340, 355); //335 370
    while (!stop)
    {
        this->pid->SetTunings(0.5, 3.2, 0);
        this->motorShield.setSpeeds(-325 - this->output, 325 - this->output);
        updatePid();
        //Serial.print("Left: ");
        //Serial.print(MotionController::MLCount);
        //Serial.print(", Right: ");
        //Serial.print(MotionController::MRCount);
        //Serial.print(", output: ");
        //Serial.println(this->output);
        if (MotionController::MRCount > 724) //780 //785 //792 latest 795
        {
            this->motorShield.setBrakes(340, 355);
            spinTimes++;
            resetCounts();
        }
        if (spinTimes == 1)
        {
            stop = true;
        }
    }
  }
  /*this->motorShield.setSpeeds(300 + this->output, -300 + this->output);
  updatePid();
  Serial.print("Left: ");
  Serial.print(MotionController::MLCount);
  Serial.print(", Right: ");
  Serial.print(MotionController::MRCount);
  Serial.print(", output: ");
  Serial.println(this->output); */

void MotionController::circularTurn()
{
  while (!stop)
  {
    this->motorShield.setSpeeds(300 + this->output, 80 - this->output);
    updatePid();
    if (MotionController::MRCount > 1185) //1000
    {
      for (int i = 0; i<30000; i++)
      {
        this->motorShield.setSpeeds(300, 300);
        updatePid();
        //this->motorShield.setBrakes(400, 400);
      }
      spinTimes++;
      resetCounts();
    }
    if (spinTimes == 1)
    {
      stop = true;
    }
  }
  this->motorShield.setBrakes(400, 400);
}

void MotionController::calibrateLeftTurn()
{
  stop = false;
  spinTimes = 0;
  while (!stop)
  {
    //Serial.println("test");
    this->motorShield.setSpeeds(-300 - this->output, 300 - this->output);
    updatePid();
    if (MotionController::MRCount > 25)
    {
      this->motorShield.setBrakes(400, 400);
      spinTimes++;
      resetCounts();
      delay(80);
    }
    if (spinTimes == 1)
    {
      stop = true;
      this->motorShield.setBrakes(400, 400);
    }
  }
}

void MotionController::calibrateRightTurn()
{
  stop1 = false;
  spinTimes = 0;
  while (!stop1)
  {
    //Serial.println("test");
    this->motorShield.setSpeeds(300 + this->output, -300 + this->output);
    updatePid();
    if (MotionController::MRCount > 30)
    {
      this->motorShield.setBrakes(400, 400);
      spinTimes++;
      resetCounts();
      delay(80);
    }
    if (spinTimes == 1)
    {
      stop1 = true;
    }
  }
}

//void MotionController::SRcalibrateRightTurn()
//{
//  stop1 = false;
//  spinTimes = 0;
//  while (!stop1)
//  {
//    //Serial.println("test");
//    this->motorShield.setSpeeds(300 + this->output, -300 + this->output);
//    updatePid();
//    if (MotionController::MRCount > 30)
//    {
//      this->motorShield.setBrakes(400, 400);
//      spinTimes++;
//      resetCounts();
//      delay(50);
//    }
//    if (spinTimes == 1 || analogRead(SR) <405)
//    {
//      stop1 = true;
//    }
//  }
//}
//
//void MotionController::SRcalibrateLeftTurn()
//{
//  stop = false;
//  spinTimes = 0;
//  while (!stop)
//  {
//    //Serial.println("test");
//    this->motorShield.setSpeeds(-300 - this->output, 300 - this->output);
//    
//    updatePid();
//    if (MotionController::MRCount > 10)
//    {
//      this->motorShield.setBrakes(400, 400);
//      spinTimes++;
//      resetCounts();
//      delay(80);
//    }
//    if (spinTimes == 1 /*|| analogRead(SR) > 405*/ )
//    {
//      stop = true;
//      this->motorShield.setBrakes(400, 400);
//    }
//  }
//}



void MotionController::calibrateBackward()
{
  stop1 = false;
  spinTimes = 0;
  while (!stop1)
  {
    //Serial.println("test");
    this->motorShield.setSpeeds(-200 - this->output, -195 + this->output);
    updatePid();
    if (MotionController::MRCount > 50)
    {
   
        this->motorShield.setBrakes(400, 390);
    
      spinTimes++;
      resetCounts();
    }
    if (spinTimes == 1)
    {
      stop1 = true;
    }
  }
}

void MotionController::calibrateForward()
{
  stop1 = false;
  spinTimes = 0;
  while (!stop1)
  {
    //Serial.println("test");
    this->motorShield.setSpeeds(200 + this->output, 200 - this->output);
    updatePid();
    if (MotionController::MRCount > 35)
    {
      
        this->motorShield.setBrakes(400, 400);
     
      spinTimes++;
      resetCounts();
    }
    if (spinTimes == 1)
    {
      stop1 = true;
    }//spintimes
//    if( analogRead(FL)>FL_ANALOG || analogRead(CN)> CN_ANALOG || analogRead(FR)>FR_ANALOG ){//A2=FL, A1=CN,  AO=FR
//            stop1=true;
//            this->motorShield.setBrakes(388, 390);
//      
//    }//ebrake
  }
}

void MotionController::moveSteps(int s)
{
  stop = false;
  spinTimes = 0;
  resetCounts();
  this->motorShield.setBrakes(400, 380); //400, 380
  while (!stop)
  {
    //moveForward();
    this->pid->SetTunings(0.5, 0, 0); // 0.5, 3.5, 0
    motorShield.setSpeeds(335 + (this->output), 350 - this->output); //0.1, 0
    if(MotionController::MLCount % 250 == 0)
    {
      MotionController::MLCount = MotionController::MLCount + 2;
    }
    updatePid();
    if (MotionController::MRCount > (forwardTicks+62)) //540 //610 //600
    {
      spinTimes++;
      resetCounts();
    }
    if (spinTimes == s)
    {
      stop = true;
      this->motorShield.setBrakes(400, 380);
    }

    if( analogRead(FL)>FL_ANALOG || analogRead(CN)> CN_ANALOG || analogRead(FR)> FR_ANALOG ){//A2=FL, A1=CN,  AO=FR
      stop=true;
      this->motorShield.setSpeeds(-400,-400);
      this->motorShield.setBrakes(400, 400);
      
    }
  }
}

void MotionController::initPid()
{
  this->pid = new PID(&(MotionController::MLCount), &(this->output), &(MotionController::MRCount), MotionController::kp, MotionController::ki, MotionController::kd, DIRECT);
  //this->pidLeft = new PID(&(this->InputLeft), &(this->OutputLeft), &(this->SetpointLeft), MotionController::kp, MotionController::ki, MotionController::kd, DIRECT);
  //this->pidRight = new PID(&(this->InputRight), &(this->OutputRight), &(this->SetpointRight), MotionController::kp, MotionController::ki, MotionController::kd, DIRECT);

  this->pid->SetOutputLimits(-50, 50);

  this->pid->SetMode(AUTOMATIC);
}



void MotionController::MLCountInc()
{
  MotionController::MLCount++;
}

void MotionController::MRCountInc()
{
  MotionController::MRCount++;
}

void MotionController::resetCounts()
{
  MotionController::MLCount = MotionController::MRCount = 0;
}

void MotionController::updatePid()
{
  this->pid->Compute();
}


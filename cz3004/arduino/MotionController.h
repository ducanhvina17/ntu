#ifndef MOTIONCONTROLLER_H
#define MOTIONCONTROLLER_H

#include "Arduino.h"
#include "Constants.h"
#include "PID_v1.h"
#include "DualVNH5019MotorShield.h"


class MotionController {

public:
	MotionController();
	static void MLCountInc();
	static void MRCountInc();
	void resetCounts();
	void moveForward();
	void moveBackward();
	void moveForwardTicks();
  void moveBackwardTicks();
	void rightTurn();
	void leftTurn();
	void circularTurn();
	void calibrateLeftTurn();
  void calibrateRightTurn();
  void calibrateBackward();
  void calibrateForward();
  void SRcalibrateRightTurn();
  void SRcalibrateLeftTurn();
  void moveSteps(int s);
  void continuousLeftTurn();
	DualVNH5019MotorShield motorShield;

private:
	static constexpr double kp = 1.5, ki = 0, kd = 0;
	static volatile long MLCount, MRCount;
	static const int moveSpeed = 200;
	static const int forwardTicks100 = 5950;
	static const int forwardTicks110 = 6700;
	static const int forwardTicks120 = 7150;
	static const int forwardTicks130 = 7830;
	static const int forwardTicks140 = 8580;
	static const int forwardTicks150 = 9150;
  static const int forwardTicks = 532; //570
    bool stop = false;
    bool stop1 = false;
	//unsigned char direction;

	double output;
	//double SetpointRight, InputRight, OutputRight;
	PID* pid;
	//PID* pidRight;
	void initPid();
	void updatePid();
};

#endif

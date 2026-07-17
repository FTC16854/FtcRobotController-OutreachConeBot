/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/**
 * Original FTC opmode header block
 *
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 **/

/** Parent OpMode Class
 * All Teleop and Autonomous OpModes should inherit from (extend) ParentOpMode.
 * Each child/subclass OpMode should have its own unique runOpMode() method that will
 * override the ParentOpMode runOpMode() method.
 **/

@TeleOp(name="Holonomic Example", group="Linear Opmode")
//@Disabled
public class HolonomicExample extends LinearOpMode {

    // Declare OpMode members, hardware variables
    public ElapsedTime runtime = new ElapsedTime();

    private DcMotor rightFront = null;
    private DcMotor leftFront = null;
    private DcMotorSimple rightBack = null;
    private DcMotorSimple leftBack = null;

    private DcMotor lift = null;
    private Servo gripperServo = null;

    private DigitalChannel liftLimitSwitch = null;
    IMU imu = null;

    //Other Global Variables
    //put global variables here...
    //
    double servograbbingCHEESE =0.4;
    double servodroppingCHEESE =0;
    int position0=0;
    int position1=459;
    int position2=2273;
    int position3=3880;
    int position4=5250;
    int positionMAX=5280;
    int targetposition=0;


    public void initialize(){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Driver Station app or Driver Hub).
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        rightFront = hardwareMap.get(DcMotor.class, "fr_drive");
        leftFront = hardwareMap.get(DcMotor.class, "fl_drive");
        rightBack = hardwareMap.get(DcMotorSimple.class, "br_drive");
        leftBack = hardwareMap.get(DcMotorSimple.class, "bl_drive");

        lift = hardwareMap.get(DcMotor.class, "lift");

        gripperServo = hardwareMap.get(Servo.class,"gripper_servo");

        liftLimitSwitch = hardwareMap.get(DigitalChannel.class,"lift_limit_switch");

        //Set Motor  and servo Directions
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);

        lift.setDirection((DcMotor.Direction.REVERSE));

        //Set brake or coast modes. If using combination of SPARK Minis and normal motors (make sure these match)
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //BRAKE or FLOAT (Coast)
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //BRAKE or FLOAT (Coast)


        //Update Driver Station Status Message after init
        telemetry.addData("Status:", "Initialized");
        telemetry.update();
    }


    @Override
    public void runOpMode() {

        initialize();

        // Init loop - optional
        while(opModeInInit()){
            // Code in here will loop continuously until OpMode is started
            liftHoming();
        }

        // Wait for the game to start (driver presses PLAY) - May not be needed if using an init Loop
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

//            tankDrive();
            fieldCentricOmni();
            Grabbingfunction();
            liftcontrol();

            checkEmergencyStop();

            telemetry.addData("Lift at bottom: ",liftAtBottom());
            telemetry.addData("Lift Position: ", getLiftPosition());
            telemetry.addData("Robot Angle: ",gyroAngle());
            telemetry.update();
        }
    }


    /*****************************/
    //Controls should be mapped here to avoid accessing gamepad directly in other functions/methods
    //This makes it simpler to re-map controls as desired
    //CONTROLLER MAP

    // Thumbsticks
    public double left_sticky_x(){
        return gamepad1.left_stick_x;
    }
    public double left_sticky_y() {
        return -gamepad1.left_stick_y;
    }

    public double right_sticky_x(){
        return gamepad1.right_stick_x;
    }
    public double right_sticky_y() {
        return -gamepad1.right_stick_y;
    }



    // Buttons

    public boolean gripperopen(){
        return gamepad1.left_bumper || gamepad2.left_bumper;
    }


    public boolean gripperclosed() {
        return gamepad1.left_trigger_pressed||gamepad2.left_trigger_pressed;
    }

    public boolean position1button(){
        return gamepad2.a;
    }

    public boolean position2button(){
        return gamepad2.x;
    }

    public boolean position3button(){
        return gamepad2.b;
    }

    public boolean position4button(){
        return gamepad2.y;
    }

    public boolean position0button(){
        return gamepad2.dpad_down;
    }

    public boolean positionMAXbutton(){
        return gamepad2.dpad_up;
    }


        public boolean emergencyButtons(){
        // check for combination of buttons to be pressed before returning true
        return (gamepad1.y && gamepad1.b) || (gamepad2.y && gamepad2.b);
    }




    /****************************/
    // Emergency Stop Functions
    public void checkEmergencyStop(){
        if(emergencyButtons()){
            //stop all motors, servos, etc.
            terminateOpModeNow();   // Force exit of OpMode
        }
    }



    /*****************************/
    //Drive Methods

    // Assign left and right drive speed using arguments/parameters rather than hardcoding
    // thumb stick values inside function body. This will allow tank drive to be reused for
    // autonomous programs without additional work
    public void tankDrive(){
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);

        rightFront.setPower(rightPower);
        rightBack.setPower(rightPower);
    }

    public void omniDrive (){
        double speed = Math.hypot(left_sticky_x(), left_sticky_y());
        double rotation = right_sticky_x();

        double DriveAngle = Math.atan2(left_sticky_y(), left_sticky_x());

        double leftFrontSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) + rotation;
        double rightFrontSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) - rotation;
        double leftBackSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) + rotation;
        double rightBackSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) - rotation;

        leftFront.setPower(leftFrontSpeed);
        leftBack.setPower(leftBackSpeed);
        rightFront.setPower(rightFrontSpeed);
        rightBack.setPower(rightBackSpeed);
    }

    public void fieldCentricOmni(){
        double speed = Math.hypot(left_sticky_x(), left_sticky_y());
        double rotation = right_sticky_x();

        double DriveAngle = Math.atan2(left_sticky_y(), left_sticky_x()) - Math.toRadians(gyroAngle());

        double leftFrontSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) + rotation;
        double rightFrontSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) - rotation;
        double leftBackSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) + rotation;
        double rightBackSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) - rotation;

        leftFront.setPower(leftFrontSpeed);
        leftBack.setPower(leftBackSpeed);
        rightFront.setPower(rightFrontSpeed);
        rightBack.setPower(rightBackSpeed);
    }


    /*****************************/
    //More Methods (Functions)

    public void Grabbingfunction(){
        if(gripperopen()){
            gripperServo.setPosition(servograbbingCHEESE);
        }
        if(gripperclosed()){
            gripperServo.setPosition(servodroppingCHEESE);
        }
    }




    //Encoder Functions
    public void liftHoming() {
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (liftAtBottom()) {
            lift.setPower(0);
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            lift.setPower(-0.25);    // Low power/Slow
        }
    }


    public boolean liftAtBottom(){
        return liftLimitSwitch.getState();
    }

    public int getLiftPosition(){
        return lift.getCurrentPosition();
    }


    public void liftcontrol(){
        if (position0button()){
            targetposition= position0;
        }


        if (position1button()){
            targetposition= position1;
        }
        if (position2button()){
            targetposition= position2;
        }
        if (position3button()){
            targetposition= position3;
        }
        if (position4button()){
            targetposition= position4;
        }

        if (positionMAXbutton()){
            targetposition= positionMAX;
        }


        lift.setTargetPosition(targetposition);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(1);   //heymotorwereonnagiveyouatarget
    }




/*
    public void goToSpinnyPosition(int spinnyGoHere){
        spinMotor.setTargetPosition(spinnyGoHere);
        spinMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spinMotor.setPower(0.25);

        telemetry.addData("actual motor position", spinMotor.getCurrentPosition());
        telemetry.addData("motor tick target position", spinnyGoHere);
    }
*/

    /*****************************/
    //Gyro Functions



    public double gyroAngle() {
        YawPitchRollAngles YEEHAWOrientation = imu.getRobotYawPitchRollAngles();

        return YEEHAWOrientation.getYaw(AngleUnit.DEGREES);
    }

}
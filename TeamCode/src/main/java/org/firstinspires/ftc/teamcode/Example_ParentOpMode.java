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

import com.qualcomm.hardware.rev.RevSPARKMini;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@TeleOp(name="Parent Opmode Example", group="Linear Opmode")
@Disabled
public class Example_ParentOpMode extends LinearOpMode {

    // Declare OpMode members, hardware variables
    public ElapsedTime runtime = new ElapsedTime();

    private DcMotor rightFront = null;
    private RevSPARKMini rightBack = null;

    //Global Variables
    //put global variables here...
    //
    double servoGripPosition = 0.4;
    // Lift Positions
    int pos0 = 0; // bottom
    int pos1 = 459;    //Just Above ConÉ on floor
    int pos2 = 2373;
    int pos3 = 3880;
    int pos4 = 5250;  //Top
    int posMax = 5280; // MAX

    public void initialize(){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Driver Station app or Driver Hub).
        rightFront = hardwareMap.get(DcMotor.class, "fr_drive");
        rightBack = hardwareMap.get(RevSPARKMini.class, "br_drive");



        //Set Motor  and servo Directions
        rightFront.setDirection(DcMotor.Direction.REVERSE);


        //Set brake or coast modes. If using combination of SPARK Minis and normal motors (make sure these match)
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //BRAKE or FLOAT (Coast)

//        gyroInitialize();

        //Update Driver Station Status Message after init
        telemetry.addData("Status:", "Initialized");
        telemetry.update();
    }

    /**
     * runOpMode() will be overridden in child OpMode.
     * Basic structure should remain intact (init, wait for start, while(opModeIsActive),
     * Additionally, Emergency conditions should be checked during every cycle
     */
    @Override
    public void runOpMode() {

        initialize();

        // Init loop - optional
        while(opModeInInit()){
            // Code in here will loop continuously until OpMode is started
        }

        // Wait for the game to start (driver presses PLAY) - May not be needed if using an init Loop
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // code here should never actually execute in parent opmode.
            // This function, in child opmode classes, overrides ParentOpMode

            //include emergency stop check in all runOpMode() functions/methods
            //implementation depends on which E-stop function will be used (boolean/void)

            //checkEmergencyStop(); // Stops motors and Terminates if buttons are pressed
            //without additional code in the while(opModeIsActive) loop.

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


    // Buttons
    public boolean emergencyButtons(){
        // check for combination of buttons to be pressed before returning true
        return true;
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
    public void tankdrive(double left, double right){

    }




    /*****************************/
    //More Methods (Functions)

    /*
    public void goToSpinnyPosition(spinnyGoHere){
        spinMotor.setTargetPosition(spinnyGoHere);
        spinMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spinMotor.setPower(0.25);

        telemetry.addData("actual motor position", spinMotor.getCurrentPosition());
        telemetry.addData("motor tick target position", spinnyGoHere);
    }
*/
    /*
    public void spinnyHome() {
        spinMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (SpindexIsHome()) {
            spinMotor.setPower(0);
            spinMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spinMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            spinMotor.setPower(0.1);    // Low power/Slow
        }
    }

    */

    /*
    public void Field_Centric_drive (){
        double speed = Math.hypot(left_sticky_x(), left_sticky_y());
        double rotation = right_sticky_x();

        double DriveAngle = Math.atan2(left_sticky_y(), left_sticky_x()) - Math.toRadians(gyroAngle()) - Math.toRadians(180);

        double leftFrontSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) + rotation;
        double rightFrontSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) - rotation;
        double leftBackSpeed = speed*Math.sin(DriveAngle - (Math.PI/4)) + rotation;
        double rightBackSpeed = speed*Math.cos(DriveAngle - (Math.PI/4)) - rotation;

        leftFront.setPower(leftFrontSpeed);
        leftBack.setPower(leftBackSpeed);
        rightFront.setPower(rightFrontSpeed);
        rightBack.setPower(rightBackSpeed);
    }

    */


    /*****************************/
    //Autonomous Functions

    /*****************************/
    //Encoder Functions
   /*
    public double getLeftVerticalEncoder(){
        return rightFront.getCurrentPosition();
    }
    */

    /*****************************/
    //Gyro Functions

/*
    private void gyroInitialize() {

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection LogoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection USBDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot TotalDirection = new RevHubOrientationOnRobot(LogoDirection, USBDirection);
        IMU.Parameters IMUParameters = new IMU.Parameters(TotalDirection);
        imu.initialize(IMUParameters);

    }

    public double gyroAngle() {
        YawPitchRollAngles YEEHAWOrientation = imu.getRobotYawPitchRollAngles();

        return YEEHAWOrientation.getYaw(AngleUnit.DEGREES);
    }

    public void gyroReset() {
        //gyroInitialize();
        imu.resetYaw();
    }

    public void ManualResetGyro(){
        if( yawResetButton() == true) {
            gyroReset();
        }
    }
*/

}
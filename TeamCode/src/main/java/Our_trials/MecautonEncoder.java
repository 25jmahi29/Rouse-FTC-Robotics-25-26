package Our_trials;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Mecauton Encoder", group = "Starterbot")

public class MecautonEncoder extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive;
    private DcMotor backLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backRightDrive;
    private DcMotor motorCatapult;

    private enum FieldSide {
        BLUE,
        RED,
        SMALLTRI
    }

    private FieldSide SIDE = FieldSide.BLUE; // Defaults to Blue

    private static final double WIND = 1.0;
    // Rubber band tension
    private static final double RELEASE = -1.0;
    static final double COUNTS_PER_MOTOR_REV = 537.7; // Motor Ratio 19.2:1
    static final double DRIVE_GEAR_REDUCTION = 1.0;   // No gear reduction or overdrive
    static final double WHEEL_DIAMETER_INCHES = 4.094;  // To calculate circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV) /
            (WHEEL_DIAMETER_INCHES * 3.1415); // diameter * pi = circumference of wheel
    static final double DRIVE_SPEED = 0.3;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize hardware
        frontLeftDrive = hardwareMap.get(DcMotor.class, "Front Left");
        backLeftDrive = hardwareMap.get(DcMotor.class, "Back Left");
        frontRightDrive = hardwareMap.get(DcMotor.class, "Front Right");
        backRightDrive = hardwareMap.get(DcMotor.class, "Back Right");
        motorCatapult = hardwareMap.get(DcMotor.class,"Catapult");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        motorCatapult.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /* The below portion allows the user to select which side
         * of the field they are starting on.
         */
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.b) {
                SIDE = FieldSide.RED;
            } else if (gamepad1.x) {
                SIDE = FieldSide.BLUE;
            } else if (gamepad1.a) {
                SIDE = FieldSide.SMALLTRI;
            }
            telemetry.addData("Front Left Encoder", frontLeftDrive.getCurrentPosition());
            telemetry.addData("Front Right Encoder", frontRightDrive.getCurrentPosition());
            telemetry.addData("Back Left Encoder", backLeftDrive.getCurrentPosition());
            telemetry.addData("Back Right Encoder", backRightDrive.getCurrentPosition());

            telemetry.addData("Press X/SQUARE", "for BLUE");
            telemetry.addData("Press B/CIRCLE", "for RED");
            telemetry.addData("Press Press A/THE X", "for small triangle");

            telemetry.addData("Selected Side", SIDE);
            telemetry.update();
        }

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Drive backward, set loading side of bot towards the boundary
            encoderDrive(DRIVE_SPEED, 7, 7, 7,7,20.0);
            if (SIDE == FieldSide.BLUE){
                launch();
                launch();
                launch();
                encoderDrive(DRIVE_SPEED, -3, 3, -3, 3,10.0); // Turn left
            }
            else if (SIDE == FieldSide.RED){
                launch();
                launch();
                launch();
                encoderDrive(DRIVE_SPEED, 3, -3, 3,-3,10.0); // Turn right
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left Encoder", frontLeftDrive.getCurrentPosition());
            telemetry.addData("Front Right Encoder", frontRightDrive.getCurrentPosition());
            telemetry.addData("Back Left Encoder", backLeftDrive.getCurrentPosition());
            telemetry.addData("Back Right Encoder", backRightDrive.getCurrentPosition());
            telemetry.addData("Field Side", SIDE);
            telemetry.update();
            break;
        }
    }

    /*
     * Method to perform a relative move, based on encoder counts.
     */
    public void encoderDrive(double speed, double frontLeftInches, double frontRightInches, double backRightInches, double backLeftInches, double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        if (opModeIsActive()) {
            newFrontLeftTarget = frontLeftDrive.getCurrentPosition() + (int) (frontLeftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRightDrive.getCurrentPosition() + (int) (frontRightInches * COUNTS_PER_INCH);
            newBackLeftTarget = backLeftDrive.getCurrentPosition() + (int) (backLeftInches * COUNTS_PER_INCH);
            newBackRightTarget = backRightDrive.getCurrentPosition() + (int) (backRightInches * COUNTS_PER_INCH);

            frontLeftDrive.setTargetPosition(newFrontLeftTarget);
            frontRightDrive.setTargetPosition(newFrontRightTarget);
            backLeftDrive.setTargetPosition(newBackLeftTarget);
            backRightDrive.setTargetPosition(newBackRightTarget);

            frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontLeftDrive.setPower(Math.abs(speed));
            frontRightDrive.setPower(Math.abs(speed));
            backLeftDrive.setPower(Math.abs(speed));
            backRightDrive.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (frontLeftDrive.isBusy() && frontRightDrive.isBusy() && backLeftDrive.isBusy() && backRightDrive.isBusy())) {

                telemetry.addData("Running to", " %7d :%7d :%7d :%7d", newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
                telemetry.addData("Currently at", " %7d :%7d :%7d :%7d",
                        frontLeftDrive.getCurrentPosition(), frontRightDrive.getCurrentPosition()
                        ,backLeftDrive.getCurrentPosition(), backRightDrive.getCurrentPosition());
                telemetry.update();
            }

            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);

            frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(100); // Pause after each move
        }
    }

    public void launch() {

        if (opModeIsActive()) {
            sleep(1000);
            motorCatapult.setPower(RELEASE);
            sleep(1000);
            motorCatapult.setPower(WIND);
            sleep(1000);
            motorCatapult.setPower(RELEASE);
            sleep(100);
            motorCatapult.setPower(0);
        }
    }
}

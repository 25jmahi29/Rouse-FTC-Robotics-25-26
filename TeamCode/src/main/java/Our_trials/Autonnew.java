package Our_trials;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Auto Op", group = "Starterbot")


public class Autonnew extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private DcMotor motorCatapult;

    private enum FieldSide {
        BLUE,
        RED,

        SMALLTRI
    }

    private FieldSide SIDE = FieldSide.BLUE; // Defaults to Blue

    private static final double WIND = 0.9; // Rubber band tension changed because of new light-colored rubber bands
    private static final double RELEASE = -1.0;

    static final double COUNTS_PER_MOTOR_REV = 537.6; // NeveRest 19.2:1
    static final double DRIVE_GEAR_REDUCTION = 0.6;   // 3:5 Gear ratio
    static final double WHEEL_DIAMETER_INCHES = 3.0;  // For calculating circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415); // diameter * pi = circumference of wheel
    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize hardware
        leftDrive = hardwareMap.dcMotor.get("Left Motor");
        rightDrive = hardwareMap.dcMotor.get("Right Motor");
        motorCatapult = hardwareMap.dcMotor.get("Catapult");

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        motorCatapult.setDirection(DcMotorSimple.Direction.REVERSE);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

            telemetry.addData("Press X/SQUARE", "for BLUE");
            telemetry.addData("Press B/CIRCLE", "for RED");
            telemetry.addData("Press Press A/THE X", "for small triangle");

            telemetry.addData("Selected Side", SIDE);
            telemetry.update();
        }

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (SIDE == FieldSide.BLUE){
                encoderDrive(DRIVE_SPEED, -12, -12, 5.0);   //Drive backward
                launch();
                launch();
                launch();
                encoderDrive(TURN_SPEED, 11, -11, 5.0); // Turn left
                encoderDrive(DRIVE_SPEED, 15, 15, 5.0); // Drive forward
            }
            else if (SIDE == FieldSide.RED){
                encoderDrive(DRIVE_SPEED, -12, -12, 5.0);   //Drive Backward
                launch();
                launch();
                launch();
                encoderDrive(TURN_SPEED, -11, 11, 5.0); // Turn right
                encoderDrive(DRIVE_SPEED, 15, 15, 5.0); // Drive forward
            }
            else if (SIDE == FieldSide.SMALLTRI) {
                encoderDrive(DRIVE_SPEED, -12, -12, 5.0); // Drive backward, set loading side of bot towards the boundary
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Left Encoder", leftDrive.getCurrentPosition());
            telemetry.addData("Right Encoder", rightDrive.getCurrentPosition());
            telemetry.addData("Field Side", SIDE);
            telemetry.update();
            break;
        }
    }

    /*
     * Method to perform a relative move, based on encoder counts.
     */
    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {
            newLeftTarget = leftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = rightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);

            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            leftDrive.setPower(Math.abs(speed));
            rightDrive.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftDrive.isBusy() && rightDrive.isBusy())) {

                telemetry.addData("Running to", " %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                telemetry.update();
            }

            leftDrive.setPower(0);
            rightDrive.setPower(0);

            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250); // Pause after each move
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

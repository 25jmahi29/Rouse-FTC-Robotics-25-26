package Our_trials;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Mecaton Time", group="Starterbot")
public class MecatonTime extends LinearOpMode{
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

    private MecatonTime.FieldSide SIDE = MecatonTime.FieldSide.BLUE; // Defaults to Blue

    private static final double WIND = 1.0; // Rubber band tension changed because of new light-colored rubber bands
    private static final double RELEASE = -1.0;
    static final double DRIVE_SPEED = 0.8;


    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
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

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.b) {
                SIDE = MecatonTime.FieldSide.RED;
            } else if (gamepad1.x) {
                SIDE = MecatonTime.FieldSide.BLUE;
            } else if (gamepad1.a) {
                SIDE = MecatonTime.FieldSide.SMALLTRI;
            }

            telemetry.addData("Press X/SQUARE", "for BLUE");
            telemetry.addData("Press B/CIRCLE", "for RED");
            telemetry.addData("Press Press A/THE X", "for small triangle");

            telemetry.addData("Selected Side", SIDE);
            telemetry.update();
        }

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.
        if (SIDE == FieldSide.BLUE){
            frontLeftDrive.setPower(DRIVE_SPEED);
            backLeftDrive.setPower(DRIVE_SPEED);
            frontRightDrive.setPower(DRIVE_SPEED);
            backRightDrive.setPower(DRIVE_SPEED);

            // Step 1:  Drive backward for 0.75 seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.30)) {
                telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            frontLeftDrive.setPower(0);
            backLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backRightDrive.setPower(0);

            //Step 2: Launch 3 balls
            launch();
            launch();
            launch();
            sleep(500);

            // Step 3:  Spin right for 1.0 seconds
            frontLeftDrive.setPower(-DRIVE_SPEED);
            backLeftDrive.setPower(DRIVE_SPEED);
            frontRightDrive.setPower(DRIVE_SPEED);
            backRightDrive.setPower(-DRIVE_SPEED);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.5)) {
                telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            frontLeftDrive.setPower(0);
            backLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backRightDrive.setPower(0);
        }
        else if (SIDE == FieldSide.RED){
            frontLeftDrive.setPower(DRIVE_SPEED);
            backLeftDrive.setPower(DRIVE_SPEED);
            frontRightDrive.setPower(DRIVE_SPEED);
            backRightDrive.setPower(DRIVE_SPEED);

            // Step 1:  Drive backward for 0.75 seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.30)) {
                telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            frontLeftDrive.setPower(0);
            backLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backRightDrive.setPower(0);

            //Step 2: Launch 3 balls
            launch();
            launch();
            launch();
            sleep(500);

            // Step 3:  Spin right for 1.0 seconds
            frontLeftDrive.setPower(DRIVE_SPEED);
            backLeftDrive.setPower(-DRIVE_SPEED);
            frontRightDrive.setPower(-DRIVE_SPEED);
            backRightDrive.setPower(DRIVE_SPEED);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.5)) {
                telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            frontLeftDrive.setPower(0);
            backLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backRightDrive.setPower(0);


        }
        else{
            // Step 1:  Drive backward for 3 seconds
            frontLeftDrive.setPower(DRIVE_SPEED);
            backLeftDrive.setPower(DRIVE_SPEED);
            frontRightDrive.setPower(DRIVE_SPEED);
            backRightDrive.setPower(DRIVE_SPEED);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.3)) {
                telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            frontLeftDrive.setPower(0);
            backLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backRightDrive.setPower(0);

            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
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
            sleep(10);
            motorCatapult.setPower(0);
        }
    }

}


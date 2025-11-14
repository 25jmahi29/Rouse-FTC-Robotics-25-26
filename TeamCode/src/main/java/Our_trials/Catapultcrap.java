package Our_trials;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Basic: Linear OpMode", group="Linear OpMode")
@Config
public class Catapultcrap extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor catapultshoot = null;
    private DcMotor catapultload = null;
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        catapultshoot = hardwareMap.get(DcMotor.class, "Catapult");
        catapultload = hardwareMap.get(DcMotor.class, "Catapult");

        catapultshoot.setDirection(DcMotor.Direction.FORWARD);
        catapultload.setDirection(DcMotor.Direction.REVERSE);
        catapultshoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        catapultload.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double catapultShoot;
            double catapultLoad;

            double shoot = gamepad1.right_trigger;
            double load = -gamepad1.left_trigger;

            catapultShoot = Range.clip(shoot, -1.0, 1.0);
            catapultLoad = Range.clip(load, -1.0, 1.0);
            catapultshoot.setPower(catapultShoot);
            catapultload.setPower(catapultLoad);
        }
    }
}

package frc.robot.commands.autocommands;

// import java.time.Duration;
// import java.time.Instant;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class TurnCommand extends CommandBase {

    private final double m_angle;
    private final ProfiledPIDController m_turnController = new ProfiledPIDController(0.000125, 0.000005,
            0, new TrapezoidProfile.Constraints(720, 720));
    // private Instant m_startTime;

    public TurnCommand(double turnAngle) {
        m_angle = turnAngle;
        addRequirements(DriveSubsystem.get());
    }

    public void initialize() {
      System.out.println("Running Turn Command");
        m_turnController.setGoal(m_angle);
        m_turnController.setTolerance(1);
    }

    public void execute() {
        double measurementAngle = DriveSubsystem.get().getHeading();
        double turnOutput = m_turnController.calculate(measurementAngle);
        DriveSubsystem.get().tankDrive(turnOutput, -turnOutput);
        
    }

    public void end(boolean interupted) {
      DriveSubsystem.get().tankDrive(0, 0);
    }

    public boolean isFinished() {
      return m_turnController.atGoal();

    }
}

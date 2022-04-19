package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArduinoSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CarouselSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.util.ParseJSONAuto;

public class RobotContainer {
	// subsystems
	private final ArduinoSubsystem m_arduinoSubsystem = new ArduinoSubsystem();
	private final ArmSubsystem m_armSubsystem = new ArmSubsystem();
	private final CarouselSubsystem m_carouselSubsystem = new CarouselSubsystem();
	private final ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
	private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	private final FeederSubsystem m_feederSubsystem = new FeederSubsystem();
	private final FlywheelSubsystem m_flywheelSubsystem = new FlywheelSubsystem();
	private final HoodSubsystem m_hoodSubsystem = new HoodSubsystem();
	private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
	private final LimelightSubsystem m_limelightSubsystem = new LimelightSubsystem();
	// controllers
	// auto selector
	private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

	// private final Field2d field = new Field2d();

	public RobotContainer() {

		SmartDashboard.putData(m_autoChooser);
		ParseJSONAuto.parse();
		//add auto option
		m_autoChooser.addOption("DriveSquare", ParseJSONAuto.getAutoCommand("DriveSquare"));
		m_autoChooser.addOption("DriveTriangle", ParseJSONAuto.getAutoCommand("DriveTriangle"));
	
	}

	public Command getAutonomousCommand() {
		return m_autoChooser.getSelected();
	}
}
package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.util.ParseJSONAuto;

public class RobotContainer {
	// subsystems
	private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
	// auto selector
	private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

	private final Field2d field = new Field2d();

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
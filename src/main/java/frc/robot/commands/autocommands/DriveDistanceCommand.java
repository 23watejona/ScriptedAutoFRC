// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autocommands;

// import java.time.Instant;

// import java.time.Duration;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class DriveDistanceCommand extends CommandBase {
  private double m_distance;

  private ProfiledPIDController m_controller;

  /** Creates a new DriveDistanceCommand. */
  public DriveDistanceCommand(double distance) {
    m_distance = distance;
    addRequirements(DriveSubsystem.get());
  }

  public DriveDistanceCommand(double distance, double speed) {
    m_distance = distance;
    addRequirements(DriveSubsystem.get());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Running Distance Drive Command");
    DriveSubsystem.get().resetEncoders();
    double kP = .1;
    double kI = 0.000;
    double kD = 0.00;

    m_controller = new ProfiledPIDController(
        kP, kI, kD,
        new TrapezoidProfile.Constraints(125, 150)); //was 196 35 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double output = m_controller.calculate(DriveSubsystem.get().getAverageEncoderDistance(), m_distance);
    DriveSubsystem.get().tankDrive(output, output);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    DriveSubsystem.get().tankDrive(0, 0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(DriveSubsystem.get().getAverageEncoderDistance()) > Math.abs(m_distance);
  }
}
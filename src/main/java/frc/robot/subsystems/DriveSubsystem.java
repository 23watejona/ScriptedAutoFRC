package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.simulation.AnalogGyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {
        private static DriveSubsystem s_subsystem;

        public static DriveSubsystem get() {
                return s_subsystem;
        }

        DifferentialDrivetrainSim m_driveSim = new DifferentialDrivetrainSim(
                        DCMotor.getNEO(2), // 2 NEO motors on each side of the drivetrain.
                        7.29, // 7.29:1 gearing reduction.
                        7.5, // MOI of 7.5 kg m^2 (from CAD model).
                        60.0, // The mass of the robot is 60 kg.
                        Units.inchesToMeters(3), // The robot uses 3" radius wheels.
                        0.7112, // The track width is 0.7112 meters.

                        // The standard deviations for measurement noise:
                        // x and y: 0.001 m
                        // heading: 0.001 rad
                        // l and r velocity: 0.1 m/s
                        // l and r position: 0.005 m
                        VecBuilder.fill(0, 0, 0, 0, 0, 0, 0));
        private PWMSparkMax m_leftMotor = new PWMSparkMax(0);
        private PWMSparkMax m_rightMotor = new PWMSparkMax(1);

        // These represent our regular encoder objects, which we would
        // create to use on a real robot.
        private Encoder m_leftEncoder = new Encoder(0, 1);
        private Encoder m_rightEncoder = new Encoder(2, 3);

        // These are our EncoderSim objects, which we will only use in
        // simulation. However, you do not need to comment out these
        // declarations when you are deploying code to the roboRIO.
        private EncoderSim m_leftEncoderSim = new EncoderSim(m_leftEncoder);
        private EncoderSim m_rightEncoderSim = new EncoderSim(m_rightEncoder);

        // Create our gyro object like we would on a real robot.
        private AnalogGyro m_gyro = new AnalogGyro(1);

        // Create the simulated gyro object, used for setting the gyro
        // angle. Like EncoderSim, this does not need to be commented out
        // when deploying code to the roboRIO.
        private AnalogGyroSim m_gyroSim = new AnalogGyroSim(m_gyro);


        private Field2d m_field = new Field2d();
        private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(new Rotation2d());

        /**
         * Initializes a new instance of the {@link DriveSubsystem} class.
         */
        public DriveSubsystem() {
                s_subsystem = this;
                SmartDashboard.putData("Field", m_field);

        }

        /**
         * Update odometry
         */
        public void periodic() {
                m_odometry.update(m_gyro.getRotation2d(),
                m_leftEncoder.getDistance(),
                m_rightEncoder.getDistance());
                m_field.setRobotPose(m_odometry.getPoseMeters());
        }

        public void simulationPeriodic() {
                // Set the inputs to the system. Note that we need to convert
                // the [-1, 1] PWM signal to voltage by multiplying it by the
                // robot controller voltage.
                m_driveSim.setInputs(m_leftMotor.get() * RobotController.getInputVoltage(),
                                m_rightMotor.get() * RobotController.getInputVoltage());

                // Advance the model by 20 ms. Note that if you are running this
                // subsystem in a separate thread or have changed the nominal timestep
                // of TimedRobot, this value needs to match it.
                m_driveSim.update(0.02);

                // Update all of our sensors.
                m_leftEncoderSim.setDistance(m_driveSim.getLeftPositionMeters());
                m_leftEncoderSim.setRate(m_driveSim.getLeftVelocityMetersPerSecond());
                m_rightEncoderSim.setDistance(m_driveSim.getRightPositionMeters());
                m_rightEncoderSim.setRate(m_driveSim.getRightVelocityMetersPerSecond());
                m_gyroSim.setAngle(-m_driveSim.getHeading().getDegrees());

                SmartDashboard.putNumber("Average Encoder Distance", getAverageEncoderDistance());
                SmartDashboard.putNumber("Left speed", m_leftMotor.get());
                SmartDashboard.putNumber("Right speed", m_leftMotor.get());
        }

        public void tankDrive(double leftSpeed, double rightSpeed){
                m_leftMotor.set(leftSpeed);
                m_rightMotor.set(rightSpeed);
        }

        public double getHeading(){
                return m_gyroSim.getAngle();
        }

        public double getAverageEncoderDistance(){
                return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance())/2.0;
        }

}
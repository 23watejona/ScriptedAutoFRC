package frc.robot.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.codehaus.groovy.control.CompilationFailedException;

import edu.wpi.first.wpilibj2.command.Command;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ParseJSONAuto {

  private static Hashtable<String,Command> commands;

  @SuppressWarnings("unchecked")
  //Note that this method will give a warning about an illegal access operation
  //according to stack overflow this is normal for newer versions of java
  public static void parse() {
    
    //get the script to generate our auto commands
    File script = new File("src/main/java/frc/robot/scripts/testParse.gvy");
    
    //create variable bindings for the shell so we can get output
    Binding b = new Binding();
    b.setVariable("output", new Hashtable<String, Command>());

    //create the shell to run our script with our bindings
    GroovyShell shell = new GroovyShell(b);

    //run our script, save our output, and catch and print any errors that arise
    try {
      shell.evaluate(script);
      commands = (Hashtable<String, Command>) shell.getVariable("output");
    } catch (CompilationFailedException e) {
      e.printStackTrace();
    } catch(IOException e){
      e.printStackTrace();
    }
  }

  public static Command getAutoCommand(String name){
    return commands.get(name);
  }
}

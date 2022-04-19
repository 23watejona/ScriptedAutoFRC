import groovy.json.JsonSlurper
import java.util.regex.Matcher
import edu.wpi.first.wpilibj2.command.Command

//get a JSON file that contains Command Bindings as well as
//all the imports necessary to build the auto command
//eg. sequentialCommandGroup, any commands you are running, etc
//make sure you use the full name (edu.wpi.first..., frc.robot...)
bindingsAndImportsFile = new File('src/main/java/frc/robot/scripts/bindingsandimports.json')

//Get a file which contains the autos you would like to build
autoFile = new File('src/main/java/frc/robot/scripts/autos.txt')

//parse the JSON file
bindingsAndImports = new JsonSlurper().parseText(bindingsAndImportsFile.text)

//get the text of the text file
autoText = autoFile.text

//arrays to hold the text of the auto command to be run
//and the names of the auto commands
commandText = []
name = []

//keep track of number of tabs
//this is used so we know when to add end parentheses
//and to know how many we need to add
lastTabs = 0;
currInd = -1

//go line by line through the text file
autoText.eachLine{

	//count and save the number of tab characters we currently see
	tabs = it.count("\t")

	//if there are currently no tabs, this is a new Auto Command
	if(tabs == 0){
		//start saving in the next index of the array
		currInd++

		//add the necessary end parentheses for the last command
		if(currInd > 0){
			commandText[currInd - 1] += ")".repeat(lastTabs)
		}
		
		//save the name of this auto
		name[currInd] = it

		//start off the string at this array index
		//otherwise we will get "null" at the beginning of our string
		commandText[currInd] = ""

	} else{

		//if this command is at the same level as the last command,
		//close the last command and continue forward
		//if this command is a level below the last command,
		//don't close the last command, as this command should go inside the last command
		//if this command is at a level above the last command
		//close the last command and the ones for the levels we've now moved to
		if(lastTabs == tabs){
			commandText[currInd] += "), "
		}else if(lastTabs > tabs){
			commandText[currInd] += ")".repeat(lastTabs - tabs + 1) + ", "
		}

		//split the line into the command and the parameter(if applicable)
		ar = it.split(" ")
		command = ar[0].replace("\t", "")

		//search the command bindings for the binding to your current desired command
		commandFromBinding = bindingsAndImports.CommandBindings.find{it.key == command}.value
		
		//if we have a parameter to the command replace the param in the binding to the actual parameter we want
		//otherwise just a the command, minus its end parenthesis
		if(ar.size() > 1){
			commandText[currInd] += commandFromBinding.substring(0,commandFromBinding.length()-1).replace("param", ar[1])
		}else{
			commandText[currInd] += commandFromBinding.substring(0,commandFromBinding.length()-1)
		}

		
	}
	lastTabs = tabs	
}
//add the necessary end parentheses for the last command
commandText[currInd] += ")".repeat(lastTabs)

//build a string containing the imports necessary to create the commands
imports = ""
bindingsAndImports.Imports.each{
	imports += "import " + it + "; "
}

//create a Dictionary to hold our output commands
output = new Hashtable<String,Command>()

ind = 0
for(i in commandText){
	//evaluate the string we built for each auto command
	//to get the actual commands we want
	//and save it in the right place
	output.put(name[ind], Eval.me(imports + i))
	++ind;
}
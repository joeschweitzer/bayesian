package joeschweitzer;

import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.neticaEx.aliases.Node;

/**
 * Driver class to test out Netica bayesian network
 */
public class Netica {

	public static void main(String[] args) throws NeticaException {
		// Setup
		Node.setConstructorClass("norsys.neticaEx.aliases.Node");
		new Environ(null);
		Net net = new Net();
		net.setName("Demo");
		
		// Setup nodes with states
		Node hearBark = new Node("hearBark", "hearBark, quiet", net);
		Node dogOut = new Node("dogOut", "dogOut, dogIn", net);
		Node bowelProblem = new Node("bowelProblem", "bowelProblem, noBowelProblem", net);
		Node familyOut = new Node("familyOut", "familyOut, familyIn", net);
		Node lightOn = new Node("lightOn", "lightOn, lightOff", net);
		
		// Add links child.addLink(parent) where parent usually causes child to happen
		lightOn.addLink(familyOut);
		dogOut.addLink(familyOut);
		dogOut.addLink(bowelProblem);
		hearBark.addLink(dogOut);
		
		// Setup conditional probabilities
		hearBark.setCPTable("dogOut", .70, .30);
		hearBark.setCPTable("dogIn", .01, .99);
		
		dogOut.setCPTable("familyOut", "bowelProblem", .99, .01);
		dogOut.setCPTable("familyOut", "noBowelProblem", .90, .10);
		dogOut.setCPTable("familyIn", "bowelProblem", .97, .03);
		dogOut.setCPTable("familyIn", "noBowelProblem", .30, .70);
		
		lightOn.setCPTable("familyOut", .60, .40);
		lightOn.setCPTable("familyIn", .05, .95);
		
		// Setup 'leaf' probabilities
		familyOut.setCPTable(.15, .85);
		bowelProblem.setCPTable(.01, .99);
		
		net.compile();
		
		// Figure out probability of light being on with no evidence given (just based off of probabilities)
		double belief = lightOn.getBelief("lightOn");
		System.out.println("The probability of the light being on is " + belief);
		
		// Enter evidence, certain things that we observe
		hearBark.finding().enterState("hearBark");
		bowelProblem.finding().enterState("noBowelProblem");
		
		// Recalculate probability of light being on given evidence
		belief = lightOn.getBelief("lightOn");
		System.out.println("The probability of the light being on given a bark was heard "
				+ "and no bowel problem is " + belief);
	}
}

package Global;
import java.util.HashMap;

import GalaxyStateMachine.ProcessGalaxyResponse;

public class Globals {

	//TODO make sure no one else messes with it
		public static HashMap<Integer, ProcessGalaxyResponse> plugins = new HashMap<Integer, ProcessGalaxyResponse>();

		public static String twitchClientId = "yourClientId";
		public static String twitchPassword = "yourPassword";
}

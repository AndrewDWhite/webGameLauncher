package GalaxyStateMachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public enum CurrentState {
	
	get_capabilities {
		@Override
		public CurrentState nextState() {
			return initialize_cache;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "get_capabilities";
		}
	},
	initialize_cache {
		@Override
		public CurrentState nextState() {
			return ping;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "initialize_cache";
		}
		@Override
		public ObjectNode remoteParametersToMake() {
			ObjectMapper mapper = new ObjectMapper();
			
			ObjectNode myObjectNode  = mapper.createObjectNode();
			ObjectNode mySubNode  = mapper.createObjectNode();
			myObjectNode.put("data", mySubNode);
			return myObjectNode;
		}
	},
	ping {
		@Override
		public CurrentState nextState() {
			//Only import once from galaxy side
			//logger.info(String.valueOf(initialized));
			//if (!initialized)
				return import_local_games;
			//else
			//	return ping;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "ping";
		}
	},
	import_local_games {
		@Override
		public CurrentState nextState() {
			return init_authentication;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "import_local_games";
		}
	},
	init_authentication {
		@Override
		public CurrentState nextState() {
			return import_owned_games;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "init_authentication";
		}
		@Override
		public ObjectNode remoteParametersToMake() {
			ObjectMapper mapper = new ObjectMapper();
			
			//ObjectNode mySubNode  = mapper.createObjectNode();
			//mySubNode.put("key","value");
			
			//ArrayNode myArrayNode = mapper.createArrayNode();
			//myArrayNode.add(mySubNode);
			
			ObjectNode  myObjectNode = mapper.createObjectNode();
			
			
			myObjectNode.set("stored_credentials", NullNode.getInstance() );//mySubNode);
			return myObjectNode;
		}
	},
	import_owned_games {
		@Override
		public CurrentState nextState() {
			return start_game_times_import;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "import_owned_games";
		}
	},
	// import_local_games
	start_game_times_import {
		@Override
		public CurrentState nextState() {
			return game_time_import_success;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "start_game_times_import";
		}
		@Override
		public ObjectNode remoteParametersToMake() {
			ObjectMapper mapper = new ObjectMapper();
			
			ArrayNode myObjectArray = mapper.createArrayNode();
			
			
			ObjectNode myObjectNode  = mapper.createObjectNode();
			myObjectNode.set("game_ids",myObjectArray);
			
			return myObjectNode;
		}
		
	},
	// ping
	// start_game_times_import,
	game_time_import_success {
		@Override
		public CurrentState nextState() {
			return game_times_import_finished;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "game_time_import_success";
		}
	},
	game_times_import_finished {
		@Override
		public CurrentState nextState() {
			return local_game_status_changed;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "game_times_import_finished";
		}
	},
	local_game_status_changed {
		@Override
		public CurrentState nextState() {
			//initialized = true;
			//return ping;
			return null;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "local_game_status_changed";
		}
	},
	// start_game_times_import,
	// game_times_import_finished,
	// ping
	;

	public CurrentState nextState() {
		// TODO Auto-generated method stub
		return null;
	}

	public String remoteMethodRequestToMake() {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectNode remoteParametersToMake() {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.createObjectNode();

	}
	
	//public 	Boolean initialized = false;
	static Logger logger = LoggerFactory.getLogger("CurrentState");
}

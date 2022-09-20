package GalaxyStateMachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Web.GoGRPCEmulator;

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
			myObjectNode.set("data", mySubNode);
			return myObjectNode;
		}
	},
	ping {
		@Override
		public CurrentState nextState() {
			//Only import once from galaxy side
			//logger.info(String.valueOf(initialized));
			if (!GoGRPCEmulator.initialized)
				return import_local_games;
			else
				return ping;
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
			return pass_login_credentials;
			//TODO parse capabilities to see if we skip
			//return import_owned_games;
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
	pass_login_credentials {
		@Override
		public CurrentState nextState() {
			return import_owned_games;
		}
		@Override
		public String remoteMethodRequestToMake() {
			return "pass_login_credentials";
		}
		@Override
		public ObjectNode remoteParametersToMake() {
			ObjectMapper mapper = new ObjectMapper();
			
			ObjectNode  myObjectNode = mapper.createObjectNode();
			
			myObjectNode.set("cookies", NullNode.getInstance() );
			
			ObjectNode myCredentialsNode = mapper.createObjectNode();
			//TODO parse from previous message data result user's input
			myCredentialsNode.put("end_uri", "" );
			
			myObjectNode.set("credentials", myCredentialsNode );
			
			myObjectNode.set("step", NullNode.getInstance() );
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
		@Override
		public Boolean remoteMethodRequestToMakeIsNotification() {
			return true;
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
		@Override
		public Boolean remoteMethodRequestToMakeIsNotification() {
			return true;
		}
	},
	local_game_status_changed {
		@Override
		public CurrentState nextState() {
			//TODO prevent others from changing at the same time
			GoGRPCEmulator.initialized = true;
			return ping;
		}

		@Override
		public String remoteMethodRequestToMake() {
			return "local_game_status_changed";
		}
		@Override
		public Boolean remoteMethodRequestToMakeIsNotification() {
			return true;
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
	
	public Boolean remoteMethodRequestToMakeIsNotification() {
		return false;
	}

	public ObjectNode remoteParametersToMake() {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.createObjectNode();

	}
	
	static Logger logger = LoggerFactory.getLogger("CurrentState");
}

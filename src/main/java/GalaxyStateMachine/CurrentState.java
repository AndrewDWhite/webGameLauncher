package GalaxyStateMachine;

public enum CurrentState {
	get_capabilities {
		@Override
		public CurrentState nextState() {
			return initialize_cache;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "get_capabilities";
		}
	},
	initialize_cache {
		@Override
		public CurrentState nextState() {
			return ping;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "initialize_cache";
		}
	},
	ping {
		@Override
		public CurrentState nextState() {
			return import_local_games;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "ping";
		}
	},
	import_local_games {
		@Override
		public CurrentState nextState() {
			return init_authentication;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "import_local_games";
		}
	},
	init_authentication {
		@Override
		public CurrentState nextState() {
			return import_owned_games;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "init_authentication";
		}
	},
	import_owned_games {
		@Override
		public CurrentState nextState() {
			return start_game_times_import;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
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
		public
		String remoteMethodRequestToMake() {
			return "start_game_times_import";
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
		public
		String remoteMethodRequestToMake() {
			return "game_time_import_success";
		}
	},
	game_times_import_finished {
		@Override
		public CurrentState nextState() {
			return local_game_status_changed;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
			return "game_times_import_finished";
		}
	},
	local_game_status_changed {
		@Override
		public CurrentState nextState() {
			return ping;
		}

		@Override
		public
		String remoteMethodRequestToMake() {
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
}

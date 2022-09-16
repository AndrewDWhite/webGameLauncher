
function myOpenGalaxyFunction() {
	window.open('goggalaxy://openGameView/test_' + $('#mytable').DataTable().row({ selected: true }).data()[11], '_self');
}

function myTableUpdatesFunction() {
	//Update table changes of stuff
	//if (controllers[0].axes[0].toFixed(4)==1)//right
	if (controllers != null && controllers[0] != undefined && myStartTime + 175 < Date.now()) {
		if (controllers[0].axes[1].toFixed(4) == 1)//down
		{
			var nextval = $('#mytable').DataTable().row({ selected: true }).next();
			if (null != nextval) {
				nextval.select()
				var myNodeLocation = $('#mytable').DataTable().rows({ order: 'current' }).nodes().indexOf($('#mytable').DataTable().row({ selected: true }).node());
				var myPage = Math.floor(myNodeLocation / $('#mytable').DataTable().page.len());
				$('#mytable').DataTable().page(myPage).draw(false);
				//$('#mytable').DataTable().page( 'next' ).draw( 'page' );
				myStartTime = Date.now();
			}
		}
		if (controllers[0].axes[1].toFixed(4) == -1)//up
		{
			var prevval = $('#mytable').DataTable().row({ selected: true }).prev()
			if (null != prevval) {
				prevval.select()
				var myNodeLocation = $('#mytable').DataTable().rows({ order: 'current' }).nodes().indexOf($('#mytable').DataTable().row({ selected: true }).node());
				var myPage = Math.floor(myNodeLocation / $('#mytable').DataTable().page.len());
				$('#mytable').DataTable().page(myPage).draw(false);
				//$('#mytable').DataTable().page( 'previous' ).draw( 'page' );
				myStartTime = Date.now();
			}
		}
		//Copy to clipboard (x)
		if (controllers[0].buttons[2].touched) {
			navigator.clipboard.writeText($('#mytable').DataTable().row({ selected: true }).data()[17]);
			myStartTime = Date.now();
		}

		//Open in galaxy (a)
		if (controllers[0].buttons[0].touched) {
			//If this doesn't provide the open dialogue you need to click on the page with the mouse again
			myOpenGalaxyFunction();
			myStartTime = Date.now();
		}
		//alert command to open galaxy page (y)
		if (controllers[0].buttons[3].touched) {
			alert('goggalaxy://openGameView/test_' + $('#mytable').DataTable().row({ selected: true }).data()[11]);
			myStartTime = Date.now();
		}

	}
}

var Bridge = {};

Bridge.login = function(state)
{
	var BRIDGE_KEY = "BRIDGE_KEY";
	var message = {method:"login",state: state};
	prompt(READIT_KEY,JSON.stringify(message));	
}

var UserData = function()
{
	userName = "jhansi";
	password = "123456aA";
}

function applyBindings()
{
	ko.applyBindings(new UserData());
}
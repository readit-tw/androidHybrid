var Bridge = {};

Bridge.login = function(userName,password)
{
	var BRIDGE_KEY = "BRIDGE_KEY";
	var loginData = {method:"login",userName: userName,password: password};
	prompt(BRIDGE_KEY,JSON.stringify(loginData));
}

Bridge.loginSuccess = function(data)
{
	alert(data.userId);
}
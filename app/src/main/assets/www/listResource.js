var items = function()
{
	return [];
}

var Item = function(title)
{
	return {title: title};
}

var AppViewModel = function()
{
	var self = this;
	self.items = ko.observableArray(items());
	self.pushItems = function(list)
	{
     for(var i = 0; i < list.length; i++) {
           self.items.push(list[i]);
     }
 	}
}

var appViewModel = new AppViewModel();

var renderList = function()
{
	ko.applyBindings(appViewModel);
}

var onListLoad = function(list)
{
alert(list);
	appViewModel.pushItems(list);
}
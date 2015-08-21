var AddView = function()
{
	var self = this;
	self.ViewModel = function()
	{
		var that = this;
		that.link = ko.observable("");
		that.title = ko.observable("");
		that.addResource = function()
		{
         prompt("ADD_RESOURCE", ko.toJSON(this));
		}
	}

	self.viewModel = new self.ViewModel();
	self.render = function()
	{
       ko.applyBindings(self.viewModel);
	}

	self.renderContent = function(title,link)
	{
    	self.viewModel.link(link);
	    self.viewModel.title(title);
	}
}

var addView = new AddView();
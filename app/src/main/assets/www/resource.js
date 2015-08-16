var ResourceViewModel =  {
	title: ko.observable(""),
	link: ko.observable(""),
	postResource : function() {
        //alert(ko.toJSON(this));
        prompt("ADD_RESOURCE", ko.toJSON(this));
    }
}

function applyBindings() {
	ko.applyBindings(ResourceViewModel);
}
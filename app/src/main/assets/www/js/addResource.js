	ko.validation.init({
	    insertMessages: false
	});

	var AddView = function() {
	    var self = this;
	    self.ViewModel = function() {
	        var that = this;
	        that.link = ko.observable("").extend({
	            required: {
	                message: 'Please enter link.'
	            }
	        }).extend({
	            pattern: {
	                message: 'Please enter a valid link',
                    params: /((?:https?\:\/\/|www\.)(?:[-a-z0-9]+\.)*[-a-z0-9]+.*)/i
	            }
	        });
	        that.title = ko.observable("").extend({
	            required: {
	                message: 'Please enter title.'
	            }
	        });
	        that.errors = ko.validation.group(that);
	        that.isValid = function() {
	            return that.errors().length === 0;
	        }
	        that.submit = function() {
	            if (that.isValid()) {
	               that.tags = $("#tags").materialtags('items');
	               AddResource.onSave(ko.toJSON(this));
	            } else {
	                that.errors.showAllMessages();
	            }
	        }
			that.renderInput = function(title,link)
			{
    			that.link(link);
	    		that.title(title);
			}
            
            that.clear = function(){
            
                that.link("")
                that.title("")
            }
	    }

	    self.viewModel = new self.ViewModel();
	    self.renderContent = function() {
	        ko.applyBindings(self.viewModel);
	    }
	    self.addResource = function()
	    {
	    	self.viewModel.submit();
	    }
        
        self.clearAll = function()
        {
            self.viewModel.clear();
        }
        
	}

	var addView = new AddView();
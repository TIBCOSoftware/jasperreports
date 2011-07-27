jQuery(document).ready(function() {
	
	// disable browser contextual menu when right-clicking
	jQuery(document).bind("contextmenu",function(e){  
        return false;  
    });

	// add event for clickable sortlinks (up/down arrows)
	jQuery('a').live('click', function(event){
		event.preventDefault();
		var currentHref = jQuery(this).attr("href");
		var ctx = getExecutionContext(this, currentHref, null);
		if (ctx) {
			ctx.run();
		}
		
		
	});
	
	jQuery('.hidefilter').live('click', function(event){
		jQuery(this).parent().hide();
	});
	
	jQuery('.sortlink').live('mousedown', function(event) {
	    if (event.which == 3) {
	    	var filterDiv = jQuery('.filterdiv',jQuery(this).parent());
	    	
	    	// hide all other open filters FIXMEJIVE: this will close all visible filters from all reports on the same page
	    	jQuery('.filterdiv').filter(':visible').each(function (index, element) {
	    		jQuery(element).hide();
	    	});
	    	
	    	filterDiv.css({
	    			position: 'absolute',
	    			'z-index': 999998,
	    			left: (event.pageX - jQuery(this).offset().left)  + "px",
	    			top: (event.pageY - jQuery(this).offset().top) + "px"
	    	});
	    	
	    	filterDiv.draggable();
	    	filterDiv.show();
	    	
	    }
	});
	
	jQuery('.submitFilter').live('click', function(event){
		var params = new Object();
		var parentForm = jQuery(this).parent();
		
		// extract form params
		jQuery('.postable', parentForm).each(function(){
			params[this.name] = this.value;
		});
		
		var currentHref = jQuery(parentForm).attr("action");
		var ctx = getExecutionContext(this, currentHref, params);
		if (ctx) {
			ctx.run();
		}		
	});
	
});
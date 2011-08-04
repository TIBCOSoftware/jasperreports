/**
 * Defines 'sort' module in JasperReports namespace
 */
(function(global) {
	if (typeof global.JasperReports.modules.sort !== 'undefined') {
		return;
	}
	
	var js = {};
	
	js.init = function() {
		var gm = global.JasperReports.modules.global;
		
		// disable browser contextual menu when right-clicking
		jQuery(document).bind("contextmenu",function(e){  
	        return false;  
	    });

		// add event for clickable sortlinks (up/down arrows)
		jQuery('a').live('click', function(event){
			event.preventDefault();
			var currentHref = jQuery(this).attr("href"),
				ctx = gm.getExecutionContext(this, currentHref, null);

			if (ctx) {
				ctx.run();
			}
		});
		
		jQuery('.hidefilter').live('click', function(event){
			jQuery(this).parent().hide();
		});
		
		
		jQuery('.filterdiv')
			.draggable()
			.live('keypress', function(event) {
				var target = jQuery(event.target),
					filterDiv = jQuery(this);
				
				// 'Enter' key press for filter value triggers 'contextual' submit
				if (target.is('.filterValue') && event.keyCode == 13) {
					event.preventDefault();
					jQuery('.submitFilter', filterDiv).trigger('click');
				}
			});
		
		/**
		 * Show filter div when right-clicking the table header
		 */
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
	    			left: (event.pageX - jQuery(this).offset().left -1)  + "px",
	    			top: (event.pageY - jQuery(this).offset().top -1) + "px"
		    	});
		    	
		    	filterDiv.show();
		    }
		});
		
		jQuery('.submitFilter').live('click', function(event){
			var params = {},
				parentForm = jQuery(this).parent(),
				currentHref = parentForm.attr("action");

			// extract form params
			jQuery('.postable', parentForm).each(function(){
				params[this.name] = this.value;
			});
			
			var ctx = gm.getExecutionContext(this, currentHref, params);
			
			if (ctx) {
				ctx.run();
			}		
		});
	};
	
	global.JasperReports.modules.sort = js;
} (this));

jQuery(document).ready(function() {
	JasperReports.modules.sort.init();
});
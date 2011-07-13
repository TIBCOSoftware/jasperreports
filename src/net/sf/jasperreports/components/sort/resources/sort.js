$(document).ready(function() {
	
	// disable browser contextual menu when right-clicking
	$(document).bind("contextmenu",function(e){  
        return false;  
    });

	// add event for clickable sortlinks (up/down arrows)
//	$('.sortlink').live('click', function(event){
	$('a').live('click', function(event){
		event.preventDefault();
		var currentHref = $(this).attr("href");
		var ctx = getExecutionContext(this, currentHref, null);
		if (ctx) {
			ctx.run();
		}
		
		
	});
	
	$('.hidefilter').live('click', function(event){
		$(this).parent().hide();
	});
	
	$('.sortlink').live('mousedown', function(event) {
	    if (event.which == 3) {
	    	var filterDiv = $('.filterdiv',$(this).parent());
	    	filterDiv.css({
	    			position: 'absolute',
	    			'z-index': 999998,
	    			left: (event.pageX - $(this).offset().left)  + "px",
	    			top: (event.pageY - $(this).offset().top) + "px"
	    	});
	    	
	    	filterDiv.draggable();
	    	filterDiv.show();
	    	
	    }
	});
	
	$('.submitFilter').live('click', function(event){
		var params = new Object();
		var parentForm = $(this).parent();
		
		// extract form params
		$('.postable', parentForm).each(function(){
			params[this.name] = this.value;
		});
		
		var currentHref = $(parentForm).attr("action");
		var ctx = getExecutionContext(this, currentHref, params);
		if (ctx) {
			ctx.run();
		}		
	});
	
});
/**
 * Defines 'sort' module in JasperReports namespace
 */
(function(global) {
	if (typeof global.JasperReports.modules.sort !== 'undefined') {
		return;
	}
	
	var js = {
				filters: {
					filterContainerId: "jasperreports_filters"
				}
		};
	
	js.createFilterDiv = function (uniqueId, arrFilterDiv) {
		var gm = global.JasperReports.modules.global,
			filterContainerId = js.filters.filterContainerId,
			filterContainerDiv = "<div id='" + filterContainerId + "'></div>",
			fcuid = '#' + filterContainerId,
			uid = '#' + uniqueId;
		
		// if filter container does not exist, create it
		if (jQuery(fcuid).size() == 0) {
			 jQuery('body').append(filterContainerDiv);
		}
		
		// if filter with id of 'uniqueId' does not exist, append it to filter container
		if (jQuery(uid).size() == 0) {
			jQuery(fcuid).append(arrFilterDiv.join(''));
			var filterDiv = jQuery(uid);
			
			// attach filter form events
			jQuery('.hidefilter', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
				jQuery(this).parent().hide();
			});
			
			filterDiv
				.draggable()
				.bind('keypress', function(event) {
					var target = jQuery(event.target),
						thisFilterDiv = jQuery(this);
					
					// 'Enter' key press for filter value triggers 'contextual' submit
					if (target.is('.filterValue') && event.keyCode == 13) {
						event.preventDefault();
						jQuery('.submitFilter', thisFilterDiv).trigger('click');
					}
				});
			
			jQuery('.submitFilter', filterDiv).live(('createTouch' in document) ? 'touchend' : 'click', function(event){
				var params = {},
					parentForm = jQuery(this).parent(),
					currentHref = parentForm.attr("action"),
					parentFilterDiv = jQuery(this).closest('.filterdiv'),
					contextStartPoint = jQuery('.' + parentFilterDiv.attr('data-forsortlink') + ':first');
				
				// extract form params
				jQuery('.postable', parentForm).each(function(){
					params[this.name] = this.value;
				});
				
				var ctx = gm.getExecutionContext(contextStartPoint, currentHref, params);
				
				if (ctx) {
					parentFilterDiv.hide();
					ctx.run();
				}		
			});
		}
		
	};

    js.isLongTouch = function(event) {
        if (!js.touchStartOn) return false;
        var isSameElement = event.target == js.touchStartOn.element;
        var holdTimeStamp = event.timeStamp - js.touchStartOn.timeStamp;
        return isSameElement && (holdTimeStamp > 400) && !event.scrollEvent;
    };

	js.init = function() { 
		var gm = global.JasperReports.modules.global,
			sortEvent = gm.events.SORT_INIT;
		
		// init should be done only once
		if (sortEvent.status === 'default') {
			// disable browser contextual menu when right-clicking
			jQuery(document).bind("contextmenu",function(e){  
		        return false;  
		    });
	
			if ('createTouch' in document) {
				var sortlinks = jQuery('.sortlink');
	
	            jQuery('document').bind("touchmove",function(event){
	                js.touchStartOn = undefined;
	            });
				sortlinks.live('click', function(event){
	                event.preventDefault();
	                event.stopPropagation();
	                return false;
	            });
				/*
				 * Capture long touch on touchstart. Also prevent default.
				 */
	            sortlinks.bind("touchstart",function(event){
	                event.preventDefault();
	
	                !event.isStartData && (js.touchStartOn = {
	                    element: event.target,
	                    timeStamp: event.timeStamp
	                });
	                event.isStartData = true;
	            });
				/*
	             * If long touch do not trigger anchor link.
	             */
	            sortlinks.live('touchend', function(event){
	                event.preventDefault();
	
	                if(js.isLongTouch(event)) {
	                    event.stopPropagation();
	                    return false;
	                }
	
	                var currentHref = jQuery(this).attr("href"),
	                    ctx = gm.getExecutionContext(this, currentHref, null);
	
	                if (ctx) {
	                    ctx.run();
	                }
	            });
				/*
	             * Show filter div on long touch
	             */
	            sortlinks.bind('touchend', function(event) {
	                event.preventDefault();
	                if (js.isLongTouch(event) || event.which == 3) {
	                    var filterDiv = jQuery('#' + jQuery(this).attr('data-filterid'));
	
	                    // hide all other open filters FIXMEJIVE: this will close all visible filters from all reports on the same page
	                    jQuery('.filterdiv').filter(':visible').each(function (index, element) {
	                        jQuery(element).hide();
	                    });
	
	                    var touch = event.changedTouches ? event.changedTouches[0] : event.originalEvent.changedTouches[0];
	                    filterDiv.css({
	                        position: 'absolute',
	                        'z-index': 999998,
	                        left: touch.pageX  + "px",
	                        top: touch.pageY + "px"
	                    });
	
	                    filterDiv.show();
	                }
	            });
			} else {
				// add event for clickable sortlinks (up/down arrows)
//				jQuery('.sortlink').live('click', function(event){
				jQuery('a').live('click', function(event) {
	                event.preventDefault();
	                var currentHref = jQuery(this).attr("href"),
	                    ctx = gm.getExecutionContext(this, currentHref, null);
	
	                if (ctx) {
	                    ctx.run();
	                }
	            });
	            /**
	             * Show filter div when right-clicking the table header
	             */
	            jQuery('.sortlink').live('mousedown', function(event) {
	                if (event.which == 3) {
	                    var filterDiv = jQuery('#' + jQuery(this).attr('data-filterid'));
	
	                    // hide all other open filters FIXMEJIVE: this will close all visible filters from all reports on the same page
	                    jQuery('.filterdiv').filter(':visible').each(function (index, element) {
	                        jQuery(element).hide();
	                    });
	                    
	                    if (filterDiv.size() == 1) {
		                    filterDiv.css({
		                        position: 'absolute',
		                        'z-index': 999998,
		                        left: (event.pageX)  + "px",
		                        top: (event.pageY) + "px"
		                    });
		                    filterDiv.show();
		                }
	                }
	            });
			}
			sortEvent.status = 'finished';
			gm.processEvent(sortEvent.name);
		}
	};
	
	global.JasperReports.modules.sort = js;
} (this));
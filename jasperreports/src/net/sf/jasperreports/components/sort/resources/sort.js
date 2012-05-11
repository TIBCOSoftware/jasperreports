/**
 * Defines 'sort' module in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports.sort !== 'undefined') {
		return;
	}
	
	var js = {
				filters: {
					filterContainerId: "jasperreports_filters"
				}
		};
	
	/**
	 * Creates a unique filter div
	 * 
	 * @param uniqueId
	 * @param arrFilterDiv an array with div's html
	 * @param filtersJsonString a JSON string of a java.util.List<net.sf.jasperreports.components.sort.FieldFilter>
	 */
	js.createFilterDiv = function (uniqueId, arrFilterDiv, filtersJsonString) {
		var gm = global.jasperreports.global,
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
				.draggable();
			
			// 'Enter' key press for filter value triggers 'contextual' submit
			jQuery('.filterValue', filterDiv).bind('keypress', function(event) {
				if (event.keyCode == 13) {
					event.preventDefault();
					if ('createTouch' in document) {	// trigger does not seem to work on safari mobile; doing workaround
						var el = jQuery('.submitFilter', filterDiv).get(0);
						var evt = document.createEvent("MouseEvents");
						evt.initMouseEvent("touchend", true, true);
						el.dispatchEvent(evt);
					} else {
						jQuery('.submitFilter', filterDiv).trigger('click');
					}
				}
			});
			
			jQuery('.submitFilter', filterDiv).live(('createTouch' in document) ? 'touchend' : 'click', function(event){
				var parentForm = jQuery(this).parent(),
					actionBaseData = jive.actionBaseData,
					actionBaseUrl = jive.actionBaseUrl,
					currentHref = parentForm.attr("action"),
					parentFilterDiv = jQuery(this).closest('.filterdiv'),
					contextStartPoint = jQuery('.' + parentFilterDiv.attr('data-forsortlink') + ':first'),
					toolbarId = contextStartPoint.closest('.mainReportDiv').find('.toolbarDiv').attr('id'),
					actionData = {actionName: 'filterica'};
				
		        actionData.filterData = {};
		        
				// extract form params
				jQuery('.postable', parentForm).each(function(){
					// prevent disabled inputs to get posted
					if(!jQuery(this).is(':disabled')) {
						actionData.filterData[this.name] = this.value;
					}
				});
				
				// clear filters
				jQuery('#' + js.filters.filterContainerId).empty();

				jasperreports.reportviewertoolbar.runReport({
	    				actionBaseData: jQuery.parseJSON(actionBaseData),
	    				actionBaseUrl: actionBaseUrl,
	    				toolbarId: toolbarId,
	    				self: contextStartPoint
	    			},
	    			actionData
	    		);
			});
			
			// show the second filter value for options containing 'between'
			jQuery('.filterOperatorTypeValueSelector', filterDiv).live('change', function (event) {
				var self = jQuery(this),
					optionValue = self.val();
				if (optionValue && optionValue.toLowerCase().indexOf('between') != -1) {
					jQuery('.filterValueEnd', filterDiv)
						.removeClass('hidden')
						.removeAttr('disabled');
				} else {
					jQuery('.filterValueEnd', filterDiv)
						.addClass('hidden')
						.attr('disabled', true);
				}
			});
			
			jQuery('.clearFilter', filterDiv).live(('createTouch' in document) ? 'touchend' : 'click', function(event){
				var parentForm = jQuery(this).parent(),
					actionBaseData = jive.actionBaseData,
					actionBaseUrl = jive.actionBaseUrl,
					currentHref = parentForm.attr("action"),
					parentFilterDiv = jQuery(this).closest('.filterdiv'),
					contextStartPoint = jQuery('.' + parentFilterDiv.attr('data-forsortlink') + ':first'),
					toolbarId = contextStartPoint.closest('.mainReportDiv').find('.toolbarDiv').attr('id'),
					actionData = {actionName: 'filterica'};
				
		        actionData.filterData = {};
		        
				// extract form params
				jQuery('.postable', parentForm).each(function(){
					// prevent disabled inputs to get posted
					if(!jQuery(this).is(':disabled')) {
						actionData.filterData[this.name] = this.value;
					}
				});
				
				actionData.filterData.clearFilter = true;
				
				// clear filters
				jQuery('#' + js.filters.filterContainerId).empty();
	
				jasperreports.reportviewertoolbar.runReport({
	    				actionBaseData: jQuery.parseJSON(actionBaseData),
	    				actionBaseUrl: actionBaseUrl,
	    				toolbarId: toolbarId,
	    				self: contextStartPoint
	    			},
	    			actionData
	    		);
			});
		} else {
			// update existing filter with values from filtersJsonString
			var arrFilters = jQuery.parseJSON(filtersJsonString);
			var found = false;
			if (arrFilters) {
				var filterDiv = jQuery(uid),
					currentFilterField = jQuery('.filterField', filterDiv).val();
				
				for (var i=0, ln = arrFilters.length; i < ln; i++) {
					var filter = arrFilters[i];
					if (filter.field === currentFilterField) {
						jQuery('.filterValueStart', filterDiv).val(filter.filterValueStart);
						jQuery('.filterValueEnd', filterDiv).val(filter.filterValueEnd);
						jQuery('.filterOperatorTypeValueSelector', filterDiv).val(filter.filterTypeOperator);
						
						if (filter.filterTypeOperator && filter.filterTypeOperator.toLowerCase().indexOf('between') != -1) {
							jQuery('.filterValueEnd', filterDiv).removeClass('hidden').removeAttr('disabled');
						} else {
							jQuery('.filterValueEnd', filterDiv).addClass('hidden').attr('disabled', true);
						}
						
						// show clear button
						jQuery('.clearFilter', filterDiv).show();
						
						found = true;
						break;
					}
				}
				
				// reset filter controls
				if (!found) {
					jQuery('.filterValueStart', filterDiv).val("");
					jQuery('.filterValueEnd', filterDiv).val("");
					jQuery('.filterOperatorTypeValueSelector :selected', filterDiv).attr('selected', false);
					
					// hide clear button
					jQuery('.clearFilter', filterDiv).hide();
				}
			}
		}
		
	};
	
    js.isLongTouch = function(event) {
        if (!js.touchStartOn) return false;
        var isSameElement = event.target == js.touchStartOn.element;
        var holdTimeStamp = event.timeStamp - js.touchStartOn.timeStamp;
        return isSameElement && (holdTimeStamp > 400) && !event.scrollEvent;
    };

	js.init = function() { 
		var sortEvent = jasperreports.events.registerEvent('jasperreports.sort.init');
		
		// init should be done only once
		if (!sortEvent.hasFinished()) {
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
	                
	                var self = jQuery(this),
		    			actionBaseData = jive.actionBaseData,
						actionBaseUrl = jive.actionBaseUrl,
						toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id'),
						actionData = jQuery.parseJSON(self.attr('data-actionData'));
	                
					jasperreports.reportviewertoolbar.runReport({
		    				actionBaseData: jQuery.parseJSON(actionBaseData),
		    				actionBaseUrl: actionBaseUrl,
		    				toolbarId: toolbarId,
		    				self: self
		    			},
		    			actionData		    			
		    		);
	            });
				/*
	             * Show filter div on long touch
	             */
	            sortlinks.bind('touchend', function(event) {
	                event.preventDefault();
	                if (js.isLongTouch(event) || event.which == 3) {
	                    var filterDiv = jQuery('#' + jQuery(this).attr('data-filterid'));
	
	                    // hide all other open filters; this will close all visible filters from all reports on the same page
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
				jQuery('.sortlink').live('click', function(event){
	                event.preventDefault();
	                
	                var self = jQuery(this),
		    			actionBaseData = jive.actionBaseData,
						actionBaseUrl = jive.actionBaseUrl,
						toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id'),
						actionData = jQuery.parseJSON(self.attr('data-actionData'));
	                
					jasperreports.reportviewertoolbar.runReport({
		    				actionBaseData: jQuery.parseJSON(actionBaseData),
		    				actionBaseUrl: actionBaseUrl,
		    				toolbarId: toolbarId,
		    				self: self
		    			},
		    			actionData		    			
		    		);
	            });
	            /**
	             * Show filter div when right-clicking the table header
	             */
	            jQuery('.sortlink').live('mousedown', function(event) {
	                if (event.which == 3) {
	                    var filterDiv = jQuery('#' + jQuery(this).attr('data-filterid'));
	
	                    // hide all other open filters; this will close all visible filters from all reports on the same page
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
			sortEvent.trigger();
		}
	};
	
	global.jasperreports.sort = js;
} (this));
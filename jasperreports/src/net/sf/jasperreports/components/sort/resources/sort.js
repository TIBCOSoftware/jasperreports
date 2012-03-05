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
	
	/**
	 * Creates a unique filter div
	 * 
	 * @param uniqueId
	 * @param arrFilterDiv an array with div's html
	 * @param filtersJsonString a JSON string of a java.util.List<net.sf.jasperreports.components.sort.FieldFilter>
	 */
	js.createFilterDiv = function (uniqueId, arrFilterDiv, filtersJsonString) {
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
				var params = {},
					parentForm = jQuery(this).parent(),
					currentHref = parentForm.attr("action"),
					parentFilterDiv = jQuery(this).closest('.filterdiv'),
					contextStartPoint = jQuery('.' + parentFilterDiv.attr('data-forsortlink') + ':first');
				
				// extract form params
				jQuery('.postable', parentForm).each(function(){
					// prevent disabled inputs to get posted
					if(!jQuery(this).is(':disabled')) {
						params[this.name] = this.value;
					}
				});
				
				var ctx = gm.getExecutionContext(contextStartPoint, currentHref, params);
				
				if (ctx) {
					parentFilterDiv.hide();
					ctx.run();
				}		
			});
			
			// show the second filter value for options containing 'between'
			jQuery('.filterOperatorTypeValueSelector', filterDiv).live('change', function (event) {
				var optionValue = jQuery(this).val();
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
				var params = {},
					parentForm = jQuery(this).parent(),
					currentHref = parentForm.attr("action"),
					parentFilterDiv = jQuery(this).closest('.filterdiv'),
					contextStartPoint = jQuery('.' + parentFilterDiv.attr('data-forsortlink') + ':first');
				
				// extract form params
				jQuery('.forClear', parentForm).each(function(){
					// prevent disabled inputs to get posted
					if(!jQuery(this).is(':disabled')) {
						params[this.name] = this.value;
					}
				});
				
				var ctx = gm.getExecutionContext(contextStartPoint, currentHref, params);
				
				if (ctx) {
					parentFilterDiv.hide();
					ctx.run();
				}		
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

	                // change sortSymbol hover image
	                var sortImage = jQuery('.sortSymbolImage', jQuery(this));
	                sortImage.attr("src", sortImage.attr('data-hover'));
	                
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
	                    
		                // change sortSymbol hover image
		                var sortImage = jQuery('.sortSymbolImage', jQuery(this));
		                sortImage.attr("src", sortImage.attr('data-src'));

	                }
	            });
			} else {
				// add event for clickable sortlinks (up/down arrows)
//				jQuery('a').live('click', function(event) {
				jQuery('.sortlink').live('click', function(event){
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
	                	
	                	/*
	                	var copy_what = jQuery(this).attr('data-resizecolumn'),
	            		absDiv = "<div id='absDiv' style='position: fixed; top: 50px; right: 50px; width: 200px; height: 400px;'></div>",
	            		jqAbsDiv = jQuery('#absDiv');
	            	
		            	if (jqAbsDiv.size() == 0) {
		            		jQuery('body').append(absDiv);
		            		jqAbsDiv = jQuery('#absDiv');
		            	}
		            	
		            	jQuery('.' + copy_what).removeClass('selected').each(function (index, element) {
		            		jQuery(element).parent().clone().appendTo(jqAbsDiv);
		            	});
		            	*/
	                }
	            });
	            
	            jQuery('.sortlink').live('mouseenter', function(event) {
	            	var target = jQuery('.sortSymbolImage', jQuery(this));
	            	target.attr("src", target.attr('data-hover'));
	            	
	            });

	            jQuery('.sortlink').live('mouseleave', function(event) {
	            	var target = jQuery('.sortSymbolImage', jQuery(this));
	            	target.attr("src", target.attr('data-src'));
	            });
	            
	            /*
	            jQuery('.sortlink').live('mouseover', function(event) {
	            	var resize_what = jQuery(this).attr('data-resizecolumn');
	            	jQuery('.' + resize_what).addClass('selected');
	            });

	            jQuery('.sortlink').live('mouseout', function(event) {
	            	var resize_what = jQuery(this).attr('data-resizecolumn');
	            	jQuery('.' + resize_what).removeClass('selected');
	            });
	            */
			}
			sortEvent.status = 'finished';
			gm.processEvent(sortEvent.name);
		}
	};
	
	global.JasperReports.modules.sort = js;
} (this));
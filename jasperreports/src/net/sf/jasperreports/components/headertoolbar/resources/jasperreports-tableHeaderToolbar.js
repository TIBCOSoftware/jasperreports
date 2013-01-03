/**
 * Defines 'tableheadertoolbar' module in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports.tableheadertoolbar !== 'undefined') {
		return;
	}
	
	var js = {
				filters: {
					filterContainerId: "jasperreports_filters"
				},
				templateData : {
					popupId: 'popupdiv_template'
				},
				drag: {
					dragStarted: false,
					canDrop: false,
					draggedColumnHeaderClass: null,
					dragTableFrameUuid: null,
					cursorInsideMaskPosition: null,
					moveColumnActionData: null,
					dragMaskPosition: null,
					whichTableFrameIndex: null
				},
				allColumns: {},
				selectedColumn: {
					self: null,
					popupId: null,
					toolbarId: null,
					tableUuid: null,
					tableFrameIndex: null,
					columnIndex: null,
					columnHeaderClass: null,
					actionBaseUrl: null,
					actionBaseData: null
				},
				propertiesMenuActions: {
					/* "actionName": {click: "clickHandler", mouseover: "mouseOverHandler", mouseout: "mouseOutHandler"} */
					"format": {click: "showFormatDialog"},
					"hide": {click: "hideColumn"},
					"unhide": {mouseover: "initUnhideMenu"}
				}
	};
	
	/**
	 * Initialization and event registration for non-dynamic JR elements
	 */
	js.init = function() { 
		var headertoolbarEvent = jasperreports.events.registerEvent('jasperreports.tableheadertoolbar.init');
		
		// init should be done only once
		if (!headertoolbarEvent.hasFinished()) {
			// disable browser contextual menu when right-clicking
			jQuery(document).bind("contextmenu",function(e){  
		        return false;  
		    });
			
			jQuery('.jrtableframe').live('click',
					/**
					 * Highlight a column by determining where the click was performed inside the table frame
					 */
					function(event) {
						var target = jQuery(event.target),
							currentTarget = jQuery(this),
							arrHeaderData = currentTarget.data('cachedHeaderData'),
							clickPositionInFrame = event.pageX - currentTarget.offset().left,
							currentTableFrameIndex = currentTarget.closest('.jrPage').find('.jrtableframe').index(currentTarget),
							hd,
							popupDiv,
							headerMask;
						
						if (!arrHeaderData) {
							// find headers inside frame
							var headers = jQuery('.columnHeader', currentTarget),
								header,
								headerName;
							
							arrHeaderData = [];
							for (var i=0, ln = headers.length; i < ln; i++) {
								header = jQuery(headers[i]);
								headerName = /header_(\w+)/.exec(header.attr('class'));
								popupDiv = js.getPopupFromTemplate(header.attr('data-popupId'), currentTableFrameIndex);
								headerMask = jQuery('.headerToolbarMask', popupDiv);
								if(headerName && headerName.length > 1) {
									arrHeaderData.push({
										maxLeft: header.position().left,
										maxRight: header.position().left + header.width(),
										headerClass: '.header_' + headerName[1],
										columnIndex: headerMask.attr('data-columnIndex'),
										toString: function () {return '{' + this.maxRight + ', ' + this.headerClass + '}'}
									});
								}
							}
							currentTarget.data('cachedHeaderData', arrHeaderData);
						}
						
						// determine click position inside frame
						for (var i = 0, ln = arrHeaderData.length; i < ln; i++) {
							hd = arrHeaderData[i];
							if (clickPositionInFrame <= hd.maxRight && clickPositionInFrame >= hd.maxLeft) {
								event.stopPropagation(); // cancel event bubbling here to prevent parent frames to respond to the same event
								currentTarget.find(hd.headerClass).trigger('highlight', [currentTableFrameIndex]);
								break;
							}
						}
					}
			);
			
			jQuery('.jrPage').live('mousemove', function(event) {
				var dragObj = global.jasperreports.tableheadertoolbar.drag;
				
				if (dragObj.dragStarted) {
					var currentDraggedColumnHeader = dragObj.draggedColumnHeaderClass,
						tableFrameElement = jQuery('.jrtableframe[data-uuid=' + dragObj.dragTableFrameUuid + ']').get(dragObj.whichTableFrameIndex), // find tableFrame by uuid
						tableFrame = jQuery(tableFrameElement),
						arrHeaderData = tableFrame.data('cachedHeaderData'),
						centerOfHeaderMaskPos = event.pageX - tableFrame.offset().left + dragObj.cursorInsideMaskPosition,
						hd;
					
					for (var i = 0, ln = arrHeaderData.length; i < ln; i++) {
						hd = arrHeaderData[i];
						if (centerOfHeaderMaskPos <= hd.maxRight) {
							
							if (currentDraggedColumnHeader == hd.headerClass) {
								dragObj.canDrop = false;
							} else {
								dragObj.canDrop = true;
								dragObj.moveColumnActionData = {actionName: 'move',
															moveColumnData: {
															tableUuid: dragObj.dragTableFrameUuid,
															columnToMoveIndex: js.selectedColumn.columnIndex,
															columnToMoveNewIndex: hd.columnIndex,
														}};
							}
							break;
						}
					}
				}
			});

            jQuery('.columnHeader').live('highlight',	// 'columnHeader' hardcoded in TableReport.java
            		function(event, tableFrameIndex) {
            			// hide all other popupdivs
            			jQuery('.popupdiv').fadeOut(100);
            			
		            	var self = jQuery(this),
		            		popupId = self.attr('data-popupId'),
		            		parentFrame = self.closest('.jrtableframe'),
		            		parentFrameUuid = parentFrame.attr('data-uuid'),
		            		columnName = self.attr('data-popupColumn'),
		            		popupDiv = js.getPopupFromTemplate(popupId, tableFrameIndex),
		            		headerToolbar = jQuery('.headerToolbar', popupDiv),
		            		headerToolbarMask = jQuery('.headerToolbarMask', popupDiv),
		            		columnNameSel = '.col_' + columnName, // 'col_' prefix hardcoded in TableReport.java
		            		firstElem = jQuery(columnNameSel + ':first', parentFrame),
		            		lastElem = jQuery(columnNameSel + ':last', parentFrame),
		            		headerNameSel = '.header_' + /header_(\w+)/.exec(self.attr('class'))[1];
		            	
		            	js.selectedColumn = {
		            			self: self,
		            			popupId: popupId,
		            			toolbarId: self.closest('.mainReportDiv').find('.toolbarDiv').attr('id'),
		    					tableUuid: parentFrameUuid,
		    					tableFrameIndex: tableFrameIndex,
		    					columnIndex: parseInt(headerToolbarMask.attr('data-columnIndex')),
		    					columnHeaderClass: headerNameSel,
		    					actionBaseUrl: popupDiv.attr('data-actionBaseUrl'),
		    					actionBaseData: jQuery.parseJSON(popupDiv.attr('data-actionBaseData'))
		    			};
		            	
		            	if (firstElem && lastElem) {
	            			headerToolbar.css({
	            				left: '0px'
	            			});
	            			
	            			/**
	            			 * The popupDiv is in the first 'jrPage'; the column header is in a 'jrtableframe'
	            			 * So, to calculate popupDiv's top and left we need to add each parent's top/left until jrPage is reached
	            			 */
	            			var popupTop = self.position().top,
	            				popupLeft = self.position().left,
	            				closestPage = self.closest('.jrPage'),	            			
	            				selfParents = self.parents();
	            			
	            			for (var i = 0, ln = selfParents.size(); i < ln; i ++) {
	            				var parent = jQuery(selfParents[i]);
	            				if (parent.is(closestPage)) {
	            					break;
	            				}
	            				if (parent.position()) {
	            					popupTop += parent.position().top;
	            					popupLeft += parent.position().left;
	            				}
	            			}
	            			
	            			// the popup div contains headerToolbar(fixed size) and headerToolbarMask divs
	            			
	            			var headerToolbarMaskHeight = self.height();
	            				
	            			if (lastElem.size() == 1) { // if lastElem exists
	            				headerToolbarMaskHeight = lastElem.position().top + lastElem.height() - self.position().top;
	            			}
	            				
	            			headerToolbarMask.css({
		            			position: 'absolute',
		            			'z-index': 1000,
		            			width: self.width() + 'px',
		            			height: headerToolbarMaskHeight + 'px',
		            			left: '0px',
		            			top: '0px'
		            		});
		            	
			            	popupDiv.css({
			                    'z-index': 1000,
			                    width: self.width() + 'px',
			                    left: popupLeft  + 'px',
			                    top: popupTop + 'px'
			                });
			            	
			            	headerToolbarMask.draggable({
			            		start: function(event, ui) {
			            			var dragObj = global.jasperreports.tableheadertoolbar.drag,
			            				self = jQuery(this);
			            			
			            			self.prev().hide();
			            			
			            			dragObj.dragStarted = true;
			            			dragObj.draggedColumnHeaderClass = js.selectedColumn.columnHeaderClass;
			            			dragObj.dragTableFrameUuid = js.selectedColumn.tableUuid;
			            			dragObj.cursorInsideMaskPosition = self.width()/2 - (event.originalEvent.pageX - self.offset().left);	// relative to the middle
			            			dragObj.dragMaskPosition = self.position();
			            			
			            			// which table frame with same uuid
			            			dragObj.whichTableFrameIndex =  js.selectedColumn.tableFrameIndex;
			            		},
			            		drag: function(event, ui) {
			            		},
			            		stop: function(event, ui) {
			            			var	self = jQuery(this),
			            				jvt = global.jasperreports.reportviewertoolbar;
			            				dragObj = global.jasperreports.tableheadertoolbar.drag;
			            			
			            			dragObj.dragStarted = false;
			            			
			            			if (dragObj.canDrop) {
						            	jvt.runReport(js.selectedColumn, 
						            			dragObj.moveColumnActionData, 
						            			js.highlightColumn, 
						            			[js.selectedColumn.popupId, dragObj.whichTableFrameIndex]);
						            	
			            			} else {
			            				// move mask back to its place
			            				self.animate(dragObj.dragMaskPosition, function() {
			            					// show the toolbar
			            					jQuery(this).prev().show();
			            				});
			            			}
			            		}
			            	});
			            	
			            	headerToolbarMask.resizable({
			            		handles: 'w, e',
			                	resize: function(event, ui) {
			                		var self = jQuery(this);
			                		self.prev().css({left: self.css('left')}); // ensures that headerToolbar moves along with the mask
			                	},
			                	stop: function(event, ui) {
			                		var jvt = global.jasperreports.reportviewertoolbar,
			                			self = jQuery(this),
			                			currentLeftPx = self.css('left'),
			                			currentLeft = parseInt(currentLeftPx.substring(0, currentLeftPx.indexOf('px'))),
			                			deltaLeft = ui.originalPosition.left - currentLeft,
			                			deltaWidth = self.width() - ui.originalSize.width,
			                			direction;
			                		
			                	    if (deltaWidth != 0 && deltaLeft == 0) {	// deltaWidth > 0 ? 'resize column right positive' : 'resize column right negative'
		                	    		direction = 'right';
			                	    } else if (deltaLeft != 0) {				// deltaLeft > 0 ? 'resize column left positive' : 'resize column left negative'
			                	    	direction = 'left';
			                	    }
			                	    var actionData = {
			                	    		actionName: 'resize',
			                	    		resizeColumnData: {
			                	    			tableUuid: js.selectedColumn.tableUuid,
			                	    			columnIndex: js.selectedColumn.columnIndex,
			                	    			direction: direction,
			                	    			width: self.width()
			                	    		}
			                	    };
			                	    
			                	    jvt.runReport(js.selectedColumn, actionData);
			                	}
			                });
			            	
			            	popupDiv.fadeIn(100);
	            		}
		         	}
            );
            
			headertoolbarEvent.trigger();
		}
		
	};
	
	js.getColumnByUuid = function (columnUuid, tableUuid) {
	    var tableColumns = this.allColumns[tableUuid],
			colIdx;
		
		for (colIdx in tableColumns) {
			if (tableColumns[colIdx].uuid === columnUuid) {
				return tableColumns[colIdx];
			}
		}
		return null;
	};
	
	js.highlightColumn = function (popupId, tableFrameIndex, toolbarId) {
		var jvt = global.jasperreports.reportviewertoolbar;
		jvt.performAction(toolbarId);
		
		var tableFrame = jQuery('.jrtableframe').get(tableFrameIndex);
		jQuery('.columnHeader[data-popupId=' + popupId + ']', tableFrame).trigger('click').trigger('highlight', [tableFrameIndex]);
	};
	
	js.initTemplate = function (templateId) {
		var gm = global.jasperreports.global,
			jvt = global.jasperreports.reportviewertoolbar,
			templateContainerId = "jive_templates",
			templateContainerDiv = "<div id='" + templateContainerId + "'></div>",
			tcuid = '#' + templateContainerId;
	
		// if filter container does not exist, create it
		if (jQuery(tcuid).size() == 0) {
			 jQuery(gm.reportContainerSelector).append(templateContainerDiv);
		}
		
		var popupDiv = jQuery('#'+templateId);
		if (popupDiv.size() == 1) {
			jQuery(tcuid).append(popupDiv);
		} else{
			// already initialized
			return;
		}
		
		// hide popup on click
		popupDiv.bind('click', function(event) {
			var target = jQuery(event.target);
			if (target.is('.headerToolbar') || target.is('.headerToolbarMask')) {
				jQuery(this).fadeOut(100);
			}
		});

		/**
		 * Handle sort when clicking sort icons
		 */
		jQuery('.sortAscBtn, .sortDescBtn', popupDiv).bind('click', function(event) {
			event.preventDefault();
            jvt.runReport(js.selectedColumn, jQuery(this).attr("data-sortData")); 
		});
		
		/**
         * Show filter div when clicking the filter icon
         */
        jQuery('.filterBtn', popupDiv).bind('click', function(event) {
        	event.stopPropagation();
            var self = jQuery(this),
            	parentPopupDiv = self.closest('.popupdiv'),
            	filterDiv = parentPopupDiv.find('.filterdiv');

            // hide all other open filters; this will close all visible filters from all reports on the same page
            jQuery('.filterdiv').filter(':visible').each(function (index, element) {
                jQuery(element).hide();
            });
            
            if (filterDiv.size() == 1) {
                filterDiv.css({
                    position: 'absolute',
                    'z-index': 1000,
                    left: "30px",	
                    top: "10px"
                });
                filterDiv.show();
            }
        });
        
        /**
         * Button hover
         */
        jQuery('.hoverbtn', popupDiv).bind('mouseenter', function(event) {
        	var self = jQuery(this),
        		hoverClass = self.attr('data-hover');
        	if (hoverClass) {
        		self.addClass(hoverClass);
        	}
        	
        });
        jQuery('.hoverbtn', popupDiv).bind('mouseleave', function(event) {
        	var self = jQuery(this),
	    		hoverClass = self.attr('data-hover');
	    	if (hoverClass) {
	    		self.removeClass(hoverClass);
	    	}
        });
        
        /**
         * COLUMN FILTERING
         */
        var filterDiv = popupDiv.find('.filterdiv');
		
		// attach filter form events
		jQuery('.hidefilter', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
			jQuery(this).parent().hide();
		});
		
		filterDiv.draggable();
		
		// 'Enter' key press for filter value triggers 'contextual' submit
		jQuery('.filterValue', filterDiv).bind('keypress', function(event) {
			if (event.keyCode == 13) {
				event.preventDefault();
				var filterDiv = jQuery(this).closest('.filterdiv');
				if ('createTouch' in document) {	// trigger does not seem to work on safari mobile; doing workaround
					var el = jQuery('.submitFilterBtn', filterDiv).get(0);
					var evt = document.createEvent("MouseEvents");
					evt.initMouseEvent("touchend", true, true);
					el.dispatchEvent(evt);
				} else {
					jQuery('.submitFilterBtn', filterDiv).trigger('click');
				}
			}
		});
		
		jQuery('.submitFilterBtn', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
			var self = jQuery(this),
				parentForm = self.parent(),
				parentFilterDiv = self.closest('.filterdiv'),
				actionData = jQuery.parseJSON(parentFilterDiv.attr('data-filterData')),
				filterData = {};
			
			// extract form params
			jQuery('.postable', parentForm).each(function(){
				// prevent disabled inputs to get posted
				if(!jQuery(this).is(':disabled')) {
					filterData[this.name] = this.value;
				}
			});
			
			actionData.filterData = filterData;
			jvt.runReport(js.selectedColumn, actionData);
		});
		
		jQuery('.filterValue', filterDiv).bind('templateInit', function (event) {
			var isEmpty = global.jasperreports.global.isEmpty,
				self = jQuery(this),
				parentFormDiv = self.parent();
			
			if (!isEmpty(self.val())) {
				self.removeClass('hidden');
				self.removeAttr('disabled');
				parentFormDiv.addClass('filterDivFormExpandedForClear');
				jQuery('.clearFilterBtn', parentFormDiv).show();
			}
		});
		
		// show the second filter value for options containing 'between'
		jQuery('.filterOperatorTypeValueSelector', filterDiv).bind('change', function (event) {
			var self = jQuery(this),
				optionValue = self.val(),
				parentFilterDiv = self.parent();
			
			if (optionValue && optionValue.toLowerCase().indexOf('between') != -1) {
				parentFilterDiv.addClass('filterDivFormExpanded');
				jQuery('[name=fieldValueEnd]', parentFilterDiv)
					.removeClass('hidden')
					.removeAttr('disabled');
			} else {
				parentFilterDiv.removeClass('filterDivFormExpanded');
				jQuery('[name=fieldValueEnd]', parentFilterDiv)
					.addClass('hidden')
					.attr('disabled', true);
			}
		});
		
		jQuery('.clearFilterBtn', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
			var self = jQuery(this),
				parentForm = self.parent(),
				parentFilterDiv = self.closest('.filterdiv'),
				actionData = jQuery.parseJSON(parentFilterDiv.attr('data-clearData')),
				filterData = actionData.filterData;
			
			// extract form params
			jQuery('.forClear', parentForm).each(function(){
				// prevent disabled inputs to get posted
				if(!jQuery(this).is(':disabled')) {
					filterData[this.name] = this.value;
				}
			});
			
			jvt.runReport(js.selectedColumn, actionData);
		});
		
		
		/**
		 * Format dialog
		 */
		jQuery('.jrMenu', popupDiv).on('onShow', function(event) {
			var self = jQuery(this),
				visibleColumnsCount = 0,
				hiddenColumnsCount = 0;
			jQuery.each(js.allColumns[js.selectedColumn.tableUuid], function (i, colData) {
				if (colData.enabled == true) {
					visibleColumnsCount ++;
				} else {
					hiddenColumnsCount ++;
				}
			});
			
			// disable hide if there is only one visible column left
			if (visibleColumnsCount == 1) {
				jQuery('.hide', self).removeClass('jrMenuEnabled').addClass('jrMenuDisabled');
			}
			
			// disable unhide if there are no hidden columns
			if (hiddenColumnsCount == 0) {
				jQuery('.unhide', self).removeClass('jrMenuEnabled').addClass('jrMenuDisabled');
			}
		});
		
        jQuery('.formatDialogBtn', popupDiv).on('mouseenter', function(event) {
        	var self = jQuery(this),
        		popupDiv = self.closest('.popupdiv'),
        		menu = popupDiv.find('.jrMenu');
    	  
			menu.css({
				left: '5px',
				position: 'absolute',
				top: '0px',
				'z-index': 1001,
				'text-align': 'left'
			});
			
			menu.trigger('onShow').show();
		});

        jQuery('.formatTab', popupDiv).bind('click', function() {
        	var self = jQuery(this),
        		parentFDialog = self.closest('.formatDialog');
        	
        	if (!self.is('.formatTabSelected')) {
        		var selectedTab = jQuery('.formatTabSelected', parentFDialog);
        		
        		selectedTab.removeClass('formatTabSelected');
        		jQuery('.' + selectedTab.attr('data-tabBody'), parentFDialog).removeClass('selectedTabBody');
        		
        		self.addClass('formatTabSelected');
        		jQuery('.' + self.attr('data-tabBody'), parentFDialog).addClass('selectedTabBody');
        	}
        });
        
        jQuery('.headingsTabContent .buttonOK', popupDiv).bind('click', function() {
        	var self = jQuery(this),
        		parentTab = self.closest('.headingsTabContent'),
            	actionData = {actionName: 'editColumnHeader'},
        		editColumnHeaderData = {};
        	
        	jQuery('.postable', parentTab).each(function(){
        		var self = jQuery(this);
				// prevent disabled inputs to get posted
				if(!self.is(':disabled')) {
					if (self.is('.customSel')) {
						editColumnHeaderData[this.name] = self.attr('data-postvalue');
					} else {
						editColumnHeaderData[this.name] = this.value;
					}
				}
			});
        	
        	actionData['editColumnHeaderData'] = editColumnHeaderData;
			jvt.runReport(js.selectedColumn, actionData);
        });

        jQuery('.valuesTabContent .buttonOK', popupDiv).bind('click', function() {
        	var self = jQuery(this),
	        	parentTab = self.closest('.valuesTabContent'),
	        	actionData = {actionName: 'editColumnValues'},
	        	editColumnValueData = {};
        	
        	jQuery('.postable', parentTab).each(function(){
        		var self = jQuery(this);
				// prevent disabled inputs to get posted
				if(!self.is(':disabled')) {
					if (self.is('.customSel')) {
						editColumnValueData[this.name] = self.attr('data-postvalue');
					} else {
						editColumnValueData[this.name] = this.value;
					}
				}
			});
        	
        	actionData['editColumnValueData'] = editColumnValueData;
        	jvt.runReport(js.selectedColumn, actionData);
        });
        
        jQuery('.customInput', popupDiv).on('change', function(event) {
        	var self = jQuery(this),
        		isEmpty = global.jasperreports.global.isEmpty,
        		forSelector = jQuery('[name=' + self.attr('data-forselector') + ']', self.parent()),
        		selOption = null;
        	
        	jQuery('option', forSelector).each(function (i, optElem) {
        		var opt = jQuery(optElem);
        		if (opt.text() === self.val()) {
        			selOption = opt;
        			return false; // break
        		}
        	});
        	
        	if (selOption != null) {
        		selOption.attr('selected', 'selected');
        		forSelector.attr('data-postvalue', selOption.val());
        	} else {
        		forSelector.attr('selectedIndex', '-1').children('option:selected').removeAttr('selected');
        		forSelector.attr('data-postvalue', self.val());
        	}
        });

        jQuery('.customSel', popupDiv).on('change', function(event) {
        	var self = jQuery(this),
        		forInput = jQuery('[name=' + self.attr('data-forinput') + ']', self.parent()),
        		selOption = jQuery("option:selected", self);
        	
        	forInput.val(selOption.val());
        	self.attr('data-postvalue', selOption.val());
        });
        
        /*********** Number formatting buttons *****************/
        
        jQuery('.jrToggleBtn', popupDiv).on('click', function (event, boolTriggerToggle) {
        	var self = jQuery(this),
        		toggled,
        		triggerToggle = boolTriggerToggle || true;
        	if (self.is('.jrtoggled')) {
        		self.removeClass('jrtoggled');
        		toggled = false;
        	} else {
        		self.addClass('jrtoggled');
        		toggled = true;
        	}
        	
        	triggerToggle && self.trigger('jrtoggle', [toggled]);
        });
        
        jQuery('.currencyBtn', popupDiv).on('jrtoggle', function(event, boolToggled) {
        	var parent = jQuery(this).closest('.formatting'),
        		formatPatternSelector = parent.find('.formatPatternSelector');
        	
        	jQuery('option', formatPatternSelector).each(function (i, optElem) {
        		var opt = jQuery(optElem);
        		opt.text(js.numberFormat.addRemoveCurrencySymbol(opt.text(), boolToggled));
        		opt.val(js.numberFormat.addRemoveCurrencySymbol(opt.val(), boolToggled));
        	});
        	
        });
        
        jQuery('.percentageBtn', popupDiv).on('jrtoggle', function(event, boolToggled) {
        	var parent = jQuery(this).closest('.formatting'),
    			formatPatternSelector = parent.find('.formatPatternSelector');
    	
        	jQuery('option', formatPatternSelector).each(function (i, optElem) {
        		var opt = jQuery(optElem);
	    		opt.text(js.numberFormat.addRemovePercentageForNumber(opt.text(), boolToggled));
	    		opt.val(js.numberFormat.addRemovePercentage(opt.val(), boolToggled));
        	});
        	
        });
        
        jQuery('.commaSeparatorBtn', popupDiv).on('jrtoggle', function(event, boolToggled) {
        	var parent = jQuery(this).closest('.formatting'),
				formatPatternSelector = parent.find('.formatPatternSelector');
	
        	jQuery('option', formatPatternSelector).each(function (i, optElem) {
	    		var opt = jQuery(optElem);
	    		opt.text(js.numberFormat.addRemoveThousandsSeparator(opt.text(), boolToggled));
	    		opt.val(js.numberFormat.addRemoveThousandsSeparator(opt.val(), boolToggled));
	    	});
        });
        
        jQuery('.increaseDecimalsBtn', popupDiv).on('click', function(event) {
        	var parent = jQuery(this).closest('.formatting'),
				formatPatternSelector = parent.find('.formatPatternSelector');

	    	jQuery('option', formatPatternSelector).each(function (i, optElem) {
	    		var opt = jQuery(optElem);
	    		opt.text(js.numberFormat.addRemoveDecimalPlace(opt.text(), true));
	    		opt.val(js.numberFormat.addRemoveDecimalPlace(opt.val(), true));
	    	});
        });
        
        jQuery('.decreaseDecimalsBtn', popupDiv).on('click', function(event) {
        	var parent = jQuery(this).closest('.formatting'),
				formatPatternSelector = parent.find('.formatPatternSelector');
	
	    	jQuery('option', formatPatternSelector).each(function (i, optElem) {
	    		var opt = jQuery(optElem);
	    		opt.text(js.numberFormat.addRemoveDecimalPlace(opt.text(), false));
	    		opt.val(js.numberFormat.addRemoveDecimalPlace(opt.val(), false));
	    	});
        });
        
        /*********** Style formatting buttons ***************/
        jQuery('.styleButtons button', popupDiv).on('jrtoggle', function (event, boolToggled) {
        	var self = jQuery(this);
        	if (boolToggled) {
        		self.val('true');
        	} else {
        		self.val('false');
        	}
        }).on('templateInit', function (event) {
        	var self = jQuery(this),
        		val = self.val();
        	if (val === 'true') {
        		// .styleButtons button IS A .jrToggleBtn
        		self.trigger('click', [false]);
        	}
        });
        
        jQuery('.alignmentButtons button', popupDiv).on('jrtoggle', function (event, boolToggled) {
        	var self = jQuery(this),
        		parent = self.parent(),
        		postableInput = self.parent().find('input');
        	
        	// untoggle the other toggled button
        	parent.find('.jrtoggled').not(self).removeClass('jrtoggled');
        	
        	if (boolToggled) {
        		postableInput.val(self.val());
        	} else {
        		postableInput.val('');
        	}
        });
        
        jQuery('.alignmentButtons input[name=fontHAlign]', popupDiv).on('templateInit', function (event) {
        	var self = jQuery(this),
        		val = self.val(),
        		parent = self.parent();
        	
        	parent.find("button[value='" + val + "']").trigger('click', [false]);
        });
	};
	
	js.addTemplateData = function (templateId, key, value) {
		js.templateData[templateId + '_' + key] = value;
	};
	
	js.applyTemplateDataToInstance = function (jqTemplateInstance, templateData, templateId) {
		var tData = templateData || {},
			prop;
		
		templateId && jqTemplateInstance.attr('id', templateId);
		
		for (prop in tData) {
			if (tData.hasOwnProperty(prop)) {
				var propValue = tData[prop],
					o2s = Object.prototype.toString.call(propValue);
				switch(o2s) {
					case '[object Array]':
						var target = jQuery('[name=' + prop + ']', jqTemplateInstance);
						if (target.size() != 1) {
							target = jQuery('.'+prop, jqTemplateInstance);
						}
						if (target.is('select')) {
							var option;
							for (var i = 0, ln = propValue.length; i < ln; i++) {
								option = "<option value='" + propValue[i].key + "' " + (propValue[i].sel ? 'selected' : '') + ">" + propValue[i].val + "</option>";
								target.append(option);
							}
						}
						break;
						
					case '[object Object]':
						var target = jQuery('[name=' + prop + ']', jqTemplateInstance);
						if (target.size() != 1) {
							target = jQuery('.'+prop, jqTemplateInstance);
						}
						js.applyTemplateDataToInstance(target, propValue, null);
						break;
						
					default:
						if (prop.indexOf('data-') == 0) {
							jqTemplateInstance.attr(prop, propValue);
							
						} else if (prop.indexOf('attr-') == 0) {
							jqTemplateInstance.attr(/attr-(\w+)/.exec(prop)[1], propValue);
							
						} else if (prop.indexOf('@class') == 0) {
							jqTemplateInstance.addClass(propValue);
							
						} else {
							var target = jQuery('[name=' + prop + ']', jqTemplateInstance);
							if (target.size() != 1) {
								target = jQuery('.'+prop, jqTemplateInstance);
							}
							if (target.is('label')) {
								target.html(propValue);
							} else if (target.is('.customSel')) {
								var forInput = jQuery('[name=' + target.attr('data-forinput') + ']', jqTemplateInstance),
									option = jQuery("option[value='" + propValue + "']", target);
								
								if (option.size() == 1) {
									option.attr('selected', 'selected');
									forInput.val(option.val());
								} else {
									forInput.val(propValue);
								}
								
								target.attr('data-postvalue', propValue);
								
							} else {
								target.val(propValue);
							}
							
							if (target.is('.initable')) {
								target.trigger('templateInit');
							}
						}
						break;
				}
			}
		}
	};
	
	js.getPopupFromTemplate = function (popupId, tableFrameIndex) {
		var gm = global.jasperreports.global,
			templateId = js.templateData.popupId,
			dialogsContainerId = "jive_dialogs",
			dialogsContainerDiv = "<div id='" + dialogsContainerId + "'></div>",
			dcuid = '#' + dialogsContainerId,
			tData = js.templateData[templateId + "_" + popupId],
			uniquePopupId = popupId + '_' + tableFrameIndex;
		
		if (tData) {
			// if dialogs container does not exist, create it
			if (jQuery(dcuid).size() == 0) {
				 jQuery(gm.dialogsContainerSelector).append(dialogsContainerDiv);
			}
			
			if (jQuery('#'+uniquePopupId).size() == 0) {
				var templateInstance = jQuery('#' + templateId).clone(true);
				
				js.applyTemplateDataToInstance(templateInstance, tData, uniquePopupId);
				jQuery(dcuid).append(templateInstance);
				
				js.initMenuActions(templateInstance.find('.jrMenu'));
				
				return templateInstance;
			}
		}
		return jQuery('#'+uniquePopupId);
	};
	
	js.createMenuClickHandler = function (callbackFn, context) {
		return function(event) {
			event.stopPropagation();
			var callback = global.jasperreports.global.extractCallbackFunction(callbackFn, context);
			if (callback) {
				callback.apply(this);
			}
		}
	};
	
	js.initMenuActions = function (jqMenu) {
		var prop,
			actions = js.propertiesMenuActions;
		
		jqMenu.on('mouseleave', function (event) {jQuery(this).hide()});
		
		for(prop in actions) {
			if (actions.hasOwnProperty(prop)) {
				jQuery.each(actions[prop], function (key, val) {
					jqMenu.on(key, '.' +prop, js.createMenuClickHandler(val, js));
				});
			}
		}
	};

	/**
	 * Triggered on first ajax request, ONCE
	 */
	js.setAllColumns = function (allColumns, tableUuid) {
		js.allColumns[tableUuid] = allColumns;
	};
	
	/**
	 * Triggered after each ajax request, MULTIPLE times
	 */
	js.addVisibleColumn = function (colIndex, colLabel, tableUuid) {
		var o = {};
		o[colIndex] = {
				index: colIndex,
				label: colLabel,
				enabled: true
			};
		jQuery.extend(true, js.allColumns[tableUuid], o);
	};
	
	
	/********* MENU ACTIONS ************/
	js.showFormatDialog = function () {
		var self = jQuery(this),
			dialog = self.closest('.popupdiv').find('.formatDialog');
    	
    	dialog.css({
    		left: '10px',
    		position: 'absolute',
    		top: '0',
    		'z-index': 1001,
    		'text-align': 'left'
    	});
    	
    	jQuery('.buttonCancel', dialog).bind('click', function(event) {
    		jQuery(this).closest('.formatDialog').hide();
    	});
    	
    	dialog.show();
	};
	
	js.hideColumn = function () {
		var self = jQuery(this);
		if (!self.is('.jrMenuDisabled')) {
			var	jvt = global.jasperreports.reportviewertoolbar,
				actionData = {
					actionName: 'hideUnhideColumns',
					columnData: {
						hide: true,
						columnIndexes: [js.selectedColumn.columnIndex],
						tableUuid: js.selectedColumn.tableUuid
					}
				};
			
			jvt.runReport(js.selectedColumn, actionData);
		}
	};

	js.initUnhideMenu = function () {
		var self = jQuery(this);
		
		if (!self.data('hasSubMenu') && !self.is('.jrMenuDisabled')) { 
			var	subMenu = jQuery("<ul class='jrMenuLst'></ul>"),
				allCols = js.allColumns[js.selectedColumn.tableUuid],
				liTemplate = ["<li class='jrMenuLstItm jrMenuEnabled' data-colindexes='",,"'><span class='jrMenuText'>",,"</span></li>"],
				hiddenColIndexes=[];
	
			jQuery.each(allCols, function(i, columnData) {
				if (columnData.enabled === false) {
					liTemplate[1] = '[' + columnData.index + ']';
					liTemplate[3] = columnData.label;
					subMenu.append(liTemplate.join(''));
					
					hiddenColIndexes.push(columnData.index);
				}
			});
			
			if (hiddenColIndexes.length > 0) {
				liTemplate[1] = '[' + hiddenColIndexes.join(',') + ']';
				liTemplate[3] = "&lt;ALL&gt;";
				subMenu.prepend(liTemplate.join(''));
				self.append(subMenu);
				
				self.on('click', '.jrMenuEnabled', function (event) {
					var self = jQuery(this),
						jvt = global.jasperreports.reportviewertoolbar,
						actionData = {
							actionName: 'hideUnhideColumns',
							columnData: {
								hide: false,
								columnIndexes: jQuery.parseJSON(self.attr('data-colindexes')),
								tableUuid: js.selectedColumn.tableUuid
							}
						};
					
					jvt.runReport(js.selectedColumn, actionData);
				});
			}
			
			self.data('hasSubMenu', true);
		}
	};
	
	js.numberFormat = {
			symbols: {
				currency: '\u00A4'
			},
			regex: {
				numberPart: /([\d|#]+(?!,))/,
				decimalPart: /(\.[\d|#]+)/,
				numericChar: /[\d|#]/
			},
			addRemoveDecimalPlace: (function () {
				return function (exp, booleanAdd) {
					var pozToken = exp.split(';')[0],
						negToken = exp.split(';')[1];
					
					if (booleanAdd) {
						exp = addDecimalPlaceToToken(pozToken);
						if (negToken) {
							exp = exp + ";" + addDecimalPlaceToToken(negToken);
						}
						return exp;
					} else {
						exp = removeDecimalPlaceFromToken(pozToken);
						if (negToken) {
							exp = exp + ";" + removeDecimalPlaceFromToken(negToken);
						}
						return exp;
					}
				};
				
				function addDecimalPlaceToToken (token) {
					var dotIndex = token.indexOf('.');
					
					if (dotIndex != -1) { // already have decimals
						var decimalPart = js.numberFormat.regex.decimalPart.exec(token)[1];
						
						return token.replace(decimalPart, decimalPart + '0');

					} else { // no decimals
						var numberPart = js.numberFormat.regex.numberPart.exec(token)[1];
						
						return token.replace(numberPart, numberPart + '.0');
					}
				}
				
				function removeDecimalPlaceFromToken (token) {
					var result = token,
						dotIndex = result.indexOf('.');
					
					if (dotIndex != -1) {
						var decimalPart = js.numberFormat.regex.decimalPart.exec(result)[1];
						
						if (decimalPart.length > 2) {	// remove last decimal place
							result = result.replace(decimalPart, decimalPart.substring(0, decimalPart.length - 1));
						} else {	// remove all (dot and decimal place)
							result = result.replace(decimalPart, '');
						}
					}
					
					return result;
				}
			}()),
			
			addRemoveThousandsSeparator: (function () {
				return function (exp, booleanAdd) {
					var indexOfComma = exp.indexOf(','),
						pozToken = exp.split(';')[0],
						negToken = exp.split(';')[1];
						
					if (booleanAdd) {
						if (indexOfComma == -1) {	// add
							exp = addThousandsSeparatorToToken(pozToken);
							if (negToken) {
								exp = exp + ';' + addThousandsSeparatorToToken(negToken);
							}
						}
					} else {
						if (indexOfComma != -1) {	// remove
							exp = removeThousandsSeparatorFromToken(pozToken);
							if (negToken) {
								exp = exp + ';' + removeThousandsSeparatorFromToken(negToken);
							}
						}
					}
					return exp;
				};
				
				function addThousandsSeparatorToToken (token) {
					var indexOfNumericChar = token.indexOf(js.numberFormat.regex.numericChar.exec(token)),
						firstPart = token.substring(0, indexOfNumericChar + 1);
					
					return firstPart + ',' + token.substring(firstPart.length);;
				}
				
				function removeThousandsSeparatorFromToken (token) {
					return token.replace(',','');
				}
			}()),
			
			addRemovePercentage: (function () {
				return function (exp, booleanAdd) {
					var indexOfPercent = exp.indexOf('%'),
						pozToken = exp.split(';')[0],
						negToken = exp.split(';')[1];
					
					if (booleanAdd) {	// add
						if (indexOfPercent == -1) {
							exp = addPercentageToToken(pozToken);
							if (negToken) {
								exp = exp + ";" + addPercentageToToken(negToken);
							}
						}
					} else {	// remove
						if (indexOfPercent != -1) {
							exp = removePercentageFromToken(pozToken);
							if (negToken) {
								exp = exp + ";" + removePercentageFromToken(negToken);
							}
						}
					}
					return exp;
				};
				
				function addPercentageToToken (token) {
					return token + ' %';
				};
				
				function removePercentageFromToken (token) {
					return token.substring(0, token.length - 2);
				};
			}()),
			
			/**
			 * @param negPattern must be in form of: pozSubPattern;negSubPattern
			 */
			applyNegativeNumberPattern: function (negPattern) {
				var pozPatternRegex = new RegExp(negPattern.split(';')[0], 'g'),
					exp = js.numberFormatExpression || '###0',
					pozToken = exp.split(';')[0];
				
				exp = negPattern.replace(pozPatternRegex, pozToken);
				return exp; 
			},
			
			addRemovePercentageForNumber: function (numberExp, booleanAdd) {
				var numberPart = js.numberFormat.regex.numberPart.exec(numberExp)[1];
				
				if (booleanAdd) {
					if (numberExp.indexOf('%') == -1 && numberPart.indexOf('00') == -1) {
						numberExp = numberExp.replace(numberPart, numberPart + "00");
						numberExp = numberExp + ' %';
					}					
				} else {
					if (numberExp.indexOf('%') != -1 && numberPart.indexOf('00') != -1) {
						numberExp = numberExp.replace(numberPart, numberPart.substring(0, numberPart.length - 2));
						numberExp = numberExp.substring(0, numberExp.length - 2);
					}
				}
				
				return numberExp;
			},
			
			addRemoveCurrencySymbol: function(exp, booleanAdd) {
				var cs = js.numberFormat.symbols.currency,
					indexOfCS = exp.indexOf(cs),
					pozToken = exp.split(';')[0],
					negToken = exp.split(';')[1];
				
				if (booleanAdd) {
					if (indexOfCS == -1) {
						exp = cs + " " + pozToken;
						if (negToken) {
							exp = exp + ";" + cs + " " + negToken;
						}
					}
				} else {
					if (indexOfCS != -1) {
						exp = pozToken.substring(2);
						if (negToken) {
							exp = exp + ";" + negToken.substring(2);
						}
					}
				}
				
				return exp;
			}
			
	};
	
	global.jasperreports.tableheadertoolbar = js;
} (this));
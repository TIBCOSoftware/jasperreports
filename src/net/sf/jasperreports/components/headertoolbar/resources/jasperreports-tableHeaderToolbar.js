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
				defaultAllColumns: {},
				allColumns: {},
				selectedColumn: {
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
		var gm = global.jasperreports.global,
			headertoolbarEvent = gm.events.HEADERTOOLBAR_INIT;
		
		// init should be done only once
		if (headertoolbarEvent.status === 'default') {
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
							arrHeaderData = currentTarget.data('cachedHeaderData');
						
						if (!arrHeaderData) {
							// find headers inside frame
							var headers = jQuery('.columnHeader', currentTarget),
								header,
								headerName;
							
							arrHeaderData = [];
							for (var i=0, ln = headers.length; i < ln; i++) {
								header = jQuery(headers[i]);
								headerName = /header_(\w+)/.exec(header.attr('class'));
								if(headerName && headerName.length > 1) {
									arrHeaderData.push({
										maxLeft: header.position().left,
										maxRight: header.position().left + header.width(),
										headerClass: '.header_' + headerName[1],
										toString: function () {return '{' + this.maxRight + ', ' + this.headerClass + '}'}
									});
								}
							}
							currentTarget.data('cachedHeaderData', arrHeaderData);
						}
						
						// determine click position inside frame
						var clickPositionInFrame = event.pageX - currentTarget.offset().left,
							currentTableFrameIndex = currentTarget.closest('.jrPage').find('.jrtableframe').index(currentTarget),
							hd;
						
						for (var i = 0, ln = arrHeaderData.length; i < ln; i++) {
							hd = arrHeaderData[i];
							if (clickPositionInFrame <= hd.maxRight) {
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
						currentHeader = jQuery(currentDraggedColumnHeader, tableFrame),
						currentColPosition = jQuery('.columnHeader', tableFrame).index(currentHeader),
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
															uuid: dragObj.dragTableFrameUuid,
															columnToMoveIndex: currentColPosition,
															columnToMoveNewIndex: i,
														}};
							}
							break;
						}
					}
				}
			});

            jQuery('.columnHeader').live('highlight',	// FIXMEJIVE 'columnHeader' hardcoded in TableReport.java
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
		            		columnNameSel = '.col_' + columnName, // FIXMEJIVE 'col_' prefix hardcoded in TableReport.java
		            		firstElem = jQuery(columnNameSel + ':first', parentFrame),
		            		lastElem = jQuery(columnNameSel + ':last', parentFrame),
		            		headerNameSel = '.header_' + /header_(\w+)/.exec(self.attr('class'))[1];
		            	
		            	js.selectedColumn = {
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
						            	var	params = js.selectedColumn.actionBaseData,
				                	    	toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
						            	
						            	params[jvt.PARAM_ACTION] = gm.toJsonString(dragObj.moveColumnActionData);
						            	
						            	jvt.runReport(self, 
						            			js.selectedColumn.actionBaseUrl,
				                	    		params, 
    	    									js.highlightColumn, 
    	    									[dragObj.moveColumnActionData.moveColumnData.columnToMoveNewIndex, dragObj.whichTableFrameIndex, toolbarId], 
    	    									true);
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
			                	    var uuid = jQuery(headerNameSel+':first').parent('.jrtableframe').attr('data-uuid'),
			                	    	actionData = {	actionName: 'resize',
			                	    					resizeColumnData: {
			                	    						uuid: uuid,
			                	    						columnIndex: js.selectedColumn.columnIndex,
			                	    						direction: direction,
			                	    						width: self.width()
			                	    					}
			                	    	},
			                        	params = js.selectedColumn.actionBaseData,
			                	    	toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
			                	    
			                	    params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
			                	    
			                	    jvt.runReport(jQuery('div.columnHeader:first'), 
			                	    			js.selectedColumn.actionBaseUrl,
			                	    			params, 
			                	    			jvt.performAction, 
			                	    			[toolbarId], 
			                	    			true);
			                	}
			                });
			            	
			            	popupDiv.fadeIn(100);
	            		}
		         	}
            );
            
			headertoolbarEvent.status = 'finished';
			gm.processEvent(headertoolbarEvent.name);
		}
		
	};
	
	js.highlightColumn = function (columnIndex, tableFrameIndex, toolbarId) {
		var jvt = global.jasperreports.reportviewertoolbar;
		jvt.performAction(toolbarId);
		
		var tableFrame = jQuery('.jrtableframe').get(tableFrameIndex);
		jQuery('.columnHeader:eq(' + columnIndex + ')', tableFrame).trigger('click').trigger('highlight', [tableFrameIndex]);
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
            var self = jQuery(this),
            	jvt = global.jasperreports.reportviewertoolbar, 
            	params = js.selectedColumn.actionBaseData,
            	toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');

            params[jvt.PARAM_ACTION] = jQuery(this).attr("data-sortData");
            
            jvt.runReport(jQuery('div.columnHeader:first'), 
            			js.selectedColumn.actionBaseUrl, 
            			params, 
            			jvt.performAction, 
	    				[toolbarId],  
            			true);
		});
		
		/**
         * Show filter div when clicking the filter icon
         */
        jQuery('.filterBtn', popupDiv).bind('click', function(event) {
        	event.stopPropagation();
            var self = jQuery(this),
            	parentPopupDiv = self.closest('.popupdiv'),
            	filterDiv = parentPopupDiv.find('.filterdiv');

            // hide all other open filters FIXMEJIVE: this will close all visible filters from all reports on the same page
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
		
		jQuery('.submitFilterBtn', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
			var self = jQuery(this),
				parentForm = self.parent(),
				params = js.selectedColumn.actionBaseData,
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
			params[jvt.PARAM_ACTION] = gm.toJsonString(actionData)
			
			var toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
			jvt.runReport(jQuery('div.columnHeader:first'), 
						js.selectedColumn.actionBaseUrl,
						params, 
						jvt.performAction, 
						[toolbarId], 
						true);
		});
		
		jQuery('.fieldValueStart, .fieldValueEnd', filterDiv).bind('templateInit', function (event) {
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
				jQuery('.fieldValueEnd', parentFilterDiv)
					.removeClass('hidden')
					.removeAttr('disabled');
			} else {
				parentFilterDiv.removeClass('filterDivFormExpanded');
				jQuery('.fieldValueEnd', parentFilterDiv)
					.addClass('hidden')
					.attr('disabled', true);
			}
		});
		
		jQuery('.clearFilterBtn', filterDiv).bind(('createTouch' in document) ? 'touchend' : 'click', function(event){
			var self = jQuery(this),
				parentForm = self.parent(),
				params = js.selectedColumn.actionBaseData,
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
			
			params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
			
			var toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
			jvt.runReport(jQuery('div.columnHeader:first'), 
						js.selectedColumn.actionBaseUrl, 
						params, 
						jvt.performAction, 
						[toolbarId], 
						true);
		});
		
		
		/**
		 * Format dialog
		 */
		jQuery('.jrMenu', popupDiv).on('onShow', function(event) {
			var self = jQuery(this),
				visibleColumnsCount = 0,
				hiddenColumnsCount = 0;
			jQuery.each(js.allColumns, function (i, colData) {
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
				position: 'relative',
				top: '-15px',
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
        
        jQuery('.headingsTabContent .buttonOK').bind('click', function() {
        	var self = jQuery(this),
        		parentTab = self.closest('.headingsTabContent'),
            	params = js.selectedColumn.actionBaseData,
            	actionData = {actionName: 'editColumnHeader'},
        		editColumnHeaderData = {};
        	
        	jQuery('.postable', parentTab).each(function(){
				// prevent disabled inputs to get posted
				if(!jQuery(this).is(':disabled')) {
					editColumnHeaderData[this.name] = this.value;
				}
			});
        	
        	actionData['editColumnHeaderData'] = editColumnHeaderData;
        	
        	params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
        	
        	var toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
			jvt.runReport(jQuery('div.columnHeader:first'), 
						js.selectedColumn.actionBaseUrl, 
						params, 
						jvt.performAction, 
						[toolbarId], 
						true);
        });

        jQuery('.valuesTabContent .buttonOK').bind('click', function() {
        	var self = jQuery(this),
	        	parentTab = self.closest('.valuesTabContent'),
	        	params = js.selectedColumn.actionBaseData,
	        	actionData = {actionName: 'editColumnValues'},
	        	editColumnValueData = {};
        	
        	jQuery('.postable', parentTab).each(function(){
        		// prevent disabled inputs to get posted
        		if(!jQuery(this).is(':disabled')) {
        			editColumnValueData[this.name] = this.value;
        		}
        	});
        	
        	actionData['editColumnValueData'] = editColumnValueData;
        	
        	params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
        	
        	var toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
        	jvt.runReport(jQuery('div.columnHeader:first'), 
        			js.selectedColumn.actionBaseUrl, 
        			params, 
        			jvt.performAction, 
        			[toolbarId], 
        			true);
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
						target = jQuery('.'+prop, jqTemplateInstance); 
						if (target.is('select')) {
							var i, option;
							for (i = 0, ln = propValue.length; i < ln; i++) {
								option = "<option value='" + propValue[i].key + "' " + (propValue[i].sel ? 'selected' : '') + ">" + propValue[i].val + "</option>";
								target.append(option);
							}
						}
						break;
						
					case '[object Object]':
						target = jQuery('.'+prop, jqTemplateInstance); 
						js.applyTemplateDataToInstance(target, propValue, null);
						break;
						
					default:
						if (prop.indexOf('data-') == 0) {
							jqTemplateInstance.attr(prop, propValue);
							
						} else if (prop.indexOf('@class') == 0) {
							jqTemplateInstance.addClass(propValue);
							
						} else {
							target = jQuery('.'+prop, jqTemplateInstance); 
							if (target.is('label')) {
								target.html(propValue);
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
				callback.call(this);
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
	js.setAllColumns = function (allColumns) {
		js.defaultAllColumns = allColumns;

		js.allColumns = {};
		jQuery.extend(true, js.allColumns, js.defaultAllColumns);
	};
	
	/**
	 * Triggered after each ajax request, MULTIPLE times
	 */
	js.addVisibleColumn = function (colIndex, colLabel) {
		var o = {};
		o[colIndex] = {
				index: colIndex,
				label: colLabel,
				enabled: true
			};
		jQuery.extend(true, js.allColumns, o);
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
		var self = jQuery(this),
			gm = global.jasperreports.global,
			jvt = global.jasperreports.reportviewertoolbar,
			params = js.selectedColumn.actionBaseData,
			actionData = {
				actionName: 'hideUnhideColumns',
				columnData: {
					hide: true,
					columnIndexes: [js.selectedColumn.columnIndex],
					tableUuid: js.selectedColumn.tableUuid
				}
			},
			toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
		
		params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
		
		jvt.runReport(jQuery('div.columnHeader:first'), 
				js.selectedColumn.actionBaseUrl,
				params, 
				jvt.performAction, 
				[toolbarId], 
				true);
	};

	js.initUnhideMenu = function () {
		var self = jQuery(this);
		
		if (!self.data('hasSubMenu') && !self.is('.jrMenuDisabled')) { 
			var	subMenu = jQuery("<ul class='jrMenuLst'></ul>"),
				allCols = js.allColumns,
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
						gm = global.jasperreports.global,
						jvt = global.jasperreports.reportviewertoolbar,
						params = js.selectedColumn.actionBaseData,
						actionData = {
							actionName: 'hideUnhideColumns',
							columnData: {
								hide: false,
								columnIndexes: jQuery.parseJSON(self.attr('data-colindexes')),
								tableUuid: js.selectedColumn.tableUuid
							}
						},
						toolbarId = self.closest('.mainReportDiv').find('.toolbarDiv').attr('id');
					
					params[jvt.PARAM_ACTION] = gm.toJsonString(actionData);
					
					jvt.runReport(jQuery('div.columnHeader:first'), 
							js.selectedColumn.actionBaseUrl,
							params, 
							jvt.performAction, 
							[toolbarId], 
							true);
				});
			}
			
			self.data('hasSubMenu', true);
		}
	};
	
	global.jasperreports.tableheadertoolbar = js;
} (this));
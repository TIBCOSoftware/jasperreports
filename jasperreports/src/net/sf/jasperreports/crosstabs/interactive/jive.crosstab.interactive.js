define(["jquery.ui", "text!jive.crosstab.templates.tmpl", "text!jive.crosstab.templates.styles.css", "text!jive.i18n.tmpl"], function($, templates, templateCss, jivei18nText) {
	var jivei18n = JSON.parse(jivei18nText),
        i18nfn = function (key) {
		if (jivei18n.hasOwnProperty(key)) {
			return jivei18n[key];
		} else {
			return key;
		}
	};

	var ixt = {
		initialized: false,
		selected: null,
        reportInstance: null,
		init: function(report) {
			var ic = this;
			ic.reportInstance = report;

			if (!ic.initialized) {
				$('head').append('<style id="jivext-stylesheet">' + templateCss + '</style>');
			
				$('#jivext_components').length == 0 &&  $('body').append('<div id="jivext_components"></div>');
				$('#jivext_components').empty();
				$('#jivext_components').append(templates);
				
				ic.getReportContainer().on('click touchend', function(){
					ic.hide();
					//TODO 
					//$('body').trigger('jive.inactive');
				});

                ic.setScrollableHeader();//TODO  isDashboard
				
				ic.initialized = true;
			}
				
			$.each(report.components.crosstab, function() {
				ic.initCrosstab(this);
			});
		},
		actions: {
			sortAscending: {icon: 'sortAscIcon', title: i18nfn('column.sortasc.title'), fn:'sort', arg: 'ASCENDING'},
			sortDescnding: {icon: 'sortDescIcon', title: i18nfn('column.sortdesc.title'), fn: 'sort', arg: 'DESCENDING'}
		},
		initCrosstab: function(crosstab) {
            var tblJrPage = $('table.jrPage');

			tblJrPage.on('click touchend', 'td.jrxtcolheader[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var columnHeader = $(this);
					ixt.selectDataColumn(crosstab, columnHeader);
					return false;
				}
			});

            tblJrPage.on('click touchend', 'td.jrxtdatacell[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var dataCell = $(this);
					ixt.selectDataColumn(crosstab, dataCell);
					return false;
				}
			});

            tblJrPage.on('click touchend', 'td.jrxtrowheader[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var rowHeader = $(this);
					ixt.selectRowGroup(crosstab, rowHeader);
					return false;
				}
			});
		},
		selectDataColumn: function(crosstab, cell) {
			var columnIdx = cell.data('jrxtcolidx');
			var fragmentId = cell.data('jrxtid');
			var parentTable = cell.parents('table').filter(':first');
			var firstHeader = $('td.jrxtcolheader[data-jrxtid=\'' + fragmentId + '\'][data-jrxtcolidx=\'' + columnIdx + '\']:first', parentTable);
			var lastCell = $('td.jrxtdatacell[data-jrxtid=\'' + fragmentId + '\'][data-jrxtcolidx=\'' + columnIdx + '\']:last', parentTable);

            var zoomLevel = this.reportInstance.zoom && this.reportInstance.zoom.level ? this.reportInstance.zoom.level : 1;
			var width = lastCell.offset().left + lastCell.outerWidth() - firstHeader.offset().left;
			var height = lastCell.offset().top + lastCell.outerHeight() * zoomLevel - firstHeader.offset().top;
			
			ixt.selected = {crosstab: crosstab, header: firstHeader};
			ixt.overlay.show({w: width, h: height});
			
			var columnIdx = firstHeader.data('jrxtcolidx');
			var sortingEnabled = crosstab.isDataColumnSortable(columnIdx);
			ixt.foobar.show(sortingEnabled);
		},
		selectRowGroup: function(crosstab, cell) {
			var columnIdx = cell.data('jrxtcolidx');
			var fragmentId = cell.data('jrxtid');
			var headers = $('td.jrxtrowheader[data-jrxtid=\'' + fragmentId + '\'][data-jrxtcolidx=\'' + columnIdx + '\']', cell.parents("table:first"));
			var firstHeader = $(headers[0]);
			var lastHeader = $(headers[headers.length - 1]);

            var zoomLevel = this.reportInstance.zoom && this.reportInstance.zoom.level ? this.reportInstance.zoom.level : 1;
			var width = lastHeader.offset().left + lastHeader.outerWidth() - firstHeader.offset().left;
			var height = lastHeader.offset().top + lastHeader.outerHeight() * zoomLevel - firstHeader.offset().top;
				
			ixt.selected = {crosstab: crosstab, header: firstHeader};
			ixt.overlay.show({w: width, h: height});
			ixt.foobar.show(true);
		},
		overlay: {
				jo: null,
				show: function(dim) {
					var isFirstTimeSelection = !this.jo,
                        zoomLevel = ixt.reportInstance.zoom ? ixt.reportInstance.zoom.level : 1;
					isFirstTimeSelection && (this.jo = $('#jivext_overlay'));
					
					this.jo.css({
						width: dim.w * zoomLevel,
						height: dim.h
					});
					this.jo.appendTo(ixt.getReportContainer()).show();
					this.jo.position({of:ixt.selected.header, my: 'left top', at:'left top',collision:'none'});

					isFirstTimeSelection && this.jo.position({of:ixt.selected.header, my: 'left top', at:'left top',collision:'none'});
				}
		},
		foobar: {
			jo: null,
			initialWidth: 0,
			cache: null,
			setElement: function() {
				this.jo = $('#jivext_foobar');
				this.jo.on('mousedown touchstart','button',function(evt){
					var jo = $(this);
					!jo.hasClass('disabled') && jo.addClass('pressed');
					return false;
				});
				this.jo.on('mouseup touchend','button',function(evt){
					var jo = $(this);
					jo.removeClass('pressed');
					var fn = jo.attr('fn');

					if(fn && !jo.hasClass('disabled')){
						ixt[fn](ixt.actions[jo.attr('actionkey')].arg);
					}
					return false;
				});
				this.jo.on('mouseover','button',function(){
					var jo = $(this);
					!jo.hasClass('disabled') && jo.addClass('over');
				});
				this.jo.on('mouseout','button',function(){
					$(this).removeClass('over pressed');
				});
				this.cache = null;
				this.menus = {};
			},
			show:function(enabled){
				var isFirstTimeSelection = !this.jo;
				isFirstTimeSelection && this.setElement();
				this.render(ixt.actions);
				this.jo.find('button').removeClass('over pressed disabled');
				enabled || this.jo.find('button').addClass('disabled');
				this.jo.appendTo(ixt.getReportContainer()).show();
				isFirstTimeSelection && (this.initialWidth = this.jo.width());
				var top = this.jo.outerHeight() - 1;
				this.jo.position({of:ixt.selected.header, my: 'left top-' + top, at:'left top'});
				isFirstTimeSelection && this.jo.position({of:ixt.selected.header, my: 'left top-' + top, at:'left top'});
				this.jo.width() >= this.initialWidth || this.jo.width(this.initialWidth);
			},
			render: function(actionMap){
				var it = this;
				var tmpl = [
					'<button class="jivext_foobar_button" title="',,'" actionkey="',,'" ',
					,'><span class="wrap"><span class="icon ',,'"></span></span></button>'];
				if(!it.cache) {
					it.cache = '';
					var htm;
					$.each(actionMap,function(k,v){
						tmpl[1] = v.title;
						tmpl[3] = k;
						tmpl[5] = v.fn ? 'fn="'+v.fn+'"' : v.actions ? 'menu="'+k+'"' : '';
						tmpl[7] = v.icon;
						it.cache += tmpl.join('');
					});
					
					it.jo.empty();
					it.jo.html(it.cache);
				}
			},
			reset: function() {
				this.cache = null;
				this.menus = {};
			}
		},
		getReportContainer: function() {
			return $('table.jrPage').closest('div.body');
		},
        zoom: function(o) {
            this.hide();
        },
		hide: function() {
			ixt.overlay.jo && ixt.overlay.jo.appendTo('#jivext_components').hide();
			ixt.foobar.jo && ixt.foobar.jo.appendTo('#jivext_components').hide();
		},
		sort: function(order) {
			this.hide();
			if (this.selected.header.hasClass('jrxtcolheader')) {
				var columnIdx = this.selected.header.data('jrxtcolidx');
				var sortOrder = order;
				if (sortOrder == this.selected.crosstab.getColumnOrder(columnIdx)) {
					sortOrder = 'NONE';
				}
				this.selected.crosstab.sortByDataColumn(columnIdx, sortOrder);
			} else if (this.selected.header.hasClass('jrxtrowheader')) {
				var rowGroupIdx = this.selected.header.data('jrxtcolidx');
				var sortOrder = order;
				if (sortOrder == this.selected.crosstab.config.rowGroups[rowGroupIdx].order) {
					sortOrder = 'NONE';
				}
				this.selected.crosstab.sortRowGroup(rowGroupIdx, sortOrder);
			}
		},
        setScrollableHeader: function(isDashboard) {
            var it = this,
                tblJrPage = $('table.jrPage');

            it.scrollData = {
                bColMoved: false,
                reportContainerPositionAtMove: null,
                bRowMoved: false,
                containerLeftAtMove: null
            };

            // manually set z-index to avoid flickering in some browsers
            it.currentZindex = tblJrPage.parents().length;
            tblJrPage.css('z-index', it.currentZindex);
            it.currentZindex += 1;

            it.defaultPageBgColor = tblJrPage.css('background-color');

            // FIXME: Hack to prepare the crosssection in case it doesn't exist
            if (!$('td.jrxtcrossheader').length) {
                var firstColHeader = $('td.jrxtcolfloating').filter(':first'),
                    lastColHeader,
                    firstRow,
                    lastRow,
                    parentTable, parentTableRows, i, j, k,
                    rows = [],
                    bFoundRowHeader = false,
                    firstRowHeaderIdx = -1,
                    remember = [];

                if (firstColHeader.length) {
                    parentTable = firstColHeader.closest('table');
                    lastColHeader = parentTable.find('td.jrxtcolfloating').filter(':last');
                    firstRow = firstColHeader.closest('tr');
                    lastRow = lastColHeader.closest('tr');

                    if (firstRow === lastRow) {
                        rows.push(firstRow);
                    } else {
                        parentTableRows = parentTable.find('tr');
                        i = parentTableRows.index(firstRow);
                        j = parentTableRows.index(lastRow);

                        for (k = i; k <= j; k++) {
                            rows.push(parentTableRows.get(k));
                        }
                    }

                    $.each(rows, function(idx, row) {
                        $(row).find('td').each(function(tdIdx, td) {
                            var $td = $(td);
                            if (!bFoundRowHeader && $td.is('.jrxtrowheader')) {
                                bFoundRowHeader = true;
                                firstRowHeaderIdx = tdIdx;
                            }
                            if ($td.is('.jrxtcolfloating')) {
                                return false; // break each
                            }

                            remember.push({
                                idx: tdIdx,
                                $td: $td
                            });
                        });
                    });

                    bFoundRowHeader && $.each(remember, function(i, v) {
                        if (v.idx >= firstRowHeaderIdx) {
                            v.$td.addClass('jrxtcrossheader');
                            if (!v.$td.children().length) {
                                v.$td.css('background-color', it.defaultPageBgColor);
                            }
                        }
                    });
                }
            }


            if (!isDashboard) {
                $('div#reportViewFrame .body').on('scroll', function() {
                    it.scrollHeaders(isDashboard);
                });
            }

            /** TODO 
             if (it.isIE) { // attach scroll to body for dashboards in IE
                $('body').on('scroll', function() {
                    it.scrollColumnHeader(isDashboard);

                    // reposition jive visual elements
                    it.active && !it.ui.dialog.isVisible && it.showVisualElements(jive.selected.dim);
                });
            }
             $(window).on('resize scroll', function() {
                it.scrollColumnHeader(isDashboard);

                // reposition jive visual elements
                it.active && !it.ui.dialog.isVisible && it.showVisualElements(jive.selected.dim);
            });
             **/
        },
        scrollHeaders: function(isDashboard, forceScroll) {
            var it = this,
                scrollContainer = $('div#reportViewFrame .body'),
                scrolledTop = false,
                scrolledLeft = false;

            // Determine scroll direction
            if (it.scrollData.scrollTop != null) { // check previous scrollTop
                if (scrollContainer.scrollTop() != it.scrollData.scrollTop) {
                    it.scrollData.scrollTop = scrollContainer.scrollTop();
                    scrolledTop = true;
                }
            } else if (scrollContainer.scrollTop() != 0) {
                it.scrollData.scrollTop = scrollContainer.scrollTop();
                scrolledTop = true;
            }

            if (it.scrollData.scrollLeft != null) { // check previous scrollLeft
                if (scrollContainer.scrollLeft() != it.scrollData.scrollLeft) {
                    it.scrollData.scrollLeft = scrollContainer.scrollLeft();
                    scrolledLeft = true;
                }
            } else if (scrollContainer.scrollLeft() != 0) {
                it.scrollData.scrollLeft = scrollContainer.scrollLeft();
                scrolledLeft = true;
            }

            if (!scrolledLeft && !scrolledTop && !forceScroll) {
                return;
            }

            it.scrollColumnHeader(isDashboard, scrolledLeft, scrolledTop);
            it.scrollRowHeader(isDashboard, scrolledLeft, scrolledTop);
            it.scrollCrossSection(isDashboard, scrolledLeft, scrolledTop);
        },
        scrollColumnHeader: function(isDashboard, scrolledLeft, scrolledTop) {
            var it = this,
                $win = $(window);

            var firstHeader = $('td.jrxtcolfloating').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            var floatingTbl = it.getFloatingTable('floatingColumnHeader', 'jrxtcolfloating', 'jrcolGroupHeader'),
                tbl = firstHeader.closest('table'),
                containerTop = isDashboard ? $win.scrollTop() : $('div#reportViewFrame .body').offset().top,
                headerTop = firstHeader.closest('tr').offset().top,
                reportContainerTop = $('#reportContainer').offset().top,
                lastTableCel = $('td.jrxtcolfloating.first').closest('table').find('td.jrxtdatacell:last'),
                diff = lastTableCel.length ? lastTableCel.offset().top - floatingTbl.outerHeight() - containerTop: -1, // if last cell is not visible, hide the floating header
                scrollTop = it.cachedScroll || 0,
                zoom = null;//TODO  jive.reportInstance.zoom;

            it.isIPad && !it.cachedHeaderTop && (it.cachedHeaderTop = headerTop);
            if (!isDashboard && it.isIPad) {
                scrollTop += it.cachedHeaderTop - headerTop;
            }

            if (!it.scrollData.bColMoved && headerTop-containerTop < 0 && diff > 0) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: tbl.offset().left
                    });
                    // do this twice for proper positioning
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: tbl.offset().left
                    });
                } else {
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: tbl.offset().left
                    });
                }

                it.setToolbarPositionWhenFloating(it.active, isDashboard);

                it.scrollData.bColMoved = it.isfloatingColumnHeader = true;
                if (!isDashboard) {
                    if (!it.scrollData.reportContainerPositionAtMove) {
                        it.scrollData.reportContainerPositionAtMove = reportContainerTop;
                    }
                }
            } else if (it.scrollData.bColMoved && headerTop-containerTop < 0 && diff > 0) {
                floatingTbl.show();
                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: tbl.offset().left
                    });
                } else {//if (scrolledLeft) {
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: tbl.offset().left
                    });
                }

                it.setToolbarPositionWhenFloating(it.active, isDashboard);
            } else if (it.scrollData.bColMoved) {
                floatingTbl.hide();
                it.scrollData.bColMoved = it.isfloatingColumnHeader = false;
                it.cachedScroll = 0;
                it.active && it.foobar.setPosition();
            }

            it.isIPad && (it.cachedHeaderTop = headerTop);
            it.isIPad && (it.cachedScroll = scrollTop);
        },
        scrollRowHeader: function(isDashboard, scrolledLeft, scrolledTop) {
            var it = this,
                $win = $(window);

            var firstHeader = $('td.jrxtrowheader').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            var floatingTbl = it.getFloatingTable('floatingRowHeader', 'jrxtrowheader'),
                actualWidth = floatingTbl.data("actualWidth"),
                containerLeft = isDashboard ? $win.scrollLeft() : $('div#reportViewFrame .body').offset().left,
                headerLeft = firstHeader.closest('tr').offset().left,
                lastTableCel = $('td.jrxtrowheader.first').closest('table').find('td.jrxtdatacell:last'),
                diff = lastTableCel.length ? lastTableCel.offset().left - actualWidth - containerLeft: -1, // if last cell is not visible, hide the floating header
                zoom = null;//TODO  jive.reportInstance.zoom;

            if (!it.scrollData.bRowMoved && headerLeft-containerLeft < 0 && diff > 0) {
                floatingTbl.show();

                floatingTbl.offset({
                    top: firstHeader.offset().top,
                    left: containerLeft
                });

//                it.setToolbarPositionWhenFloating(it.active, isDashboard);

                it.scrollData.bRowMoved = it.isfloatingRowHeader = true;

            } else if (it.scrollData.bRowMoved && headerLeft-containerLeft < 0 && diff > 0) {
                floatingTbl.show();

                floatingTbl.offset({
                    top: firstHeader.offset().top,
                    left: containerLeft
                });

//                it.setToolbarPositionWhenFloating(it.active, isDashboard);
            } else if (it.scrollData.bRowMoved) {
                floatingTbl.hide();
                it.scrollData.bRowMoved = it.isfloatingRowHeader = false;
                it.active && it.foobar.setPosition();
            }
        },
        scrollCrossSection: function(isDashboard, scrolledLeft, scrolledTop) {
            var it = this,
                firstHeader;

            firstHeader = $('td.jrxtcrossheader').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            var tblJrPage = firstHeader.closest('table'),
                floatingCrossTbl = it.getFloatingTable('floatingCrossHeader', 'jrxtcrossheader'),
                floatingColumnTbl = it.getFloatingTable('floatingColumnHeader'),
                floatingRowTbl = it.getFloatingTable('floatingRowHeader'),
                zoom = null;//TODO  jive.reportInstance.zoom;

            if (it.scrollData.bColMoved || it.scrollData.bRowMoved) {
                floatingCrossTbl.show();

                floatingCrossTbl.offset({
                    top: it.scrollData.bColMoved ? floatingColumnTbl.offset().top : firstHeader.offset().top,
                    left: it.scrollData.bRowMoved ? floatingRowTbl.offset().left : tblJrPage.offset().left
                });

//                it.setToolbarPositionWhenFloating(it.active, isDashboard);

                it.scrollData.bCrossMoved = it.isfloatingCrossHeader = true;

            } else if (it.scrollData.bCrossMoved) {
                floatingCrossTbl.hide();
                it.scrollData.bCrossMoved = it.isfloatingCrossHeader = false;
                it.active && it.foobar.setPosition();
            }
        },
        getFloatingTable: function(tableClass, elementClass, altElementClass) {
            var it = this,
                tbl = $('table.' + tableClass);

            if (tbl.length == 0) {
                tbl = $("<table class='" + tableClass + "' style='display:none'/>").appendTo('div#reportContainer');

                /** TODO 
                 tbl.on(clickEventName, '.jrcolHeader', function(evt){
                    // keep html links functional
                    if(!$(evt.target).parent().is('a')) {
                        var jo = $(this);
                        var coluuid = jo.data('coluuid');
                        var reportTableCell = tbl.parent().find('table.jrPage td.jrcolHeader[data-coluuid=' + coluuid + ']:first');
                        reportTableCell.length && jive.selectInteractiveElement(reportTableCell);
                        return false;
                    }
                });
                 **/

                var elementSelector = 'td.' + elementClass,
                    firstHeader = $(elementSelector).filter(':first'),
                    parentTable = firstHeader.closest('table'),
                    lastHeader = $(elementSelector, parentTable).filter(':last'),
                    rows = [], clone, cloneWidth = [], visibleWidth = [],
                    row, lastRow, cloneTD, rowTD, rowTDs, i, j, k, ln,
                    tblJrPage, parentTableRows,
                    bFoundFirst;

                if (firstHeader.length > 0) {
                    firstHeader.addClass('first');
                    row = firstHeader.closest('tr');
                    lastRow = lastHeader.closest('tr');
                    tblJrPage = firstHeader.closest('table');

                    if (row === lastRow) {
                        rows.push(row);
                    } else {
                        parentTableRows = parentTable.find('tr');
                        i = parentTableRows.index(row);
                        j = parentTableRows.index(lastRow);

                        for (k = i; k <= j; k++) {
                            rows.push(parentTableRows.get(k));
                        }
                    }

                    $.each(rows, function(idx, row) {
                        rowTDs = $(row).find('td');
                        clone = $("<tr></tr>");
                        cloneWidth[idx] = 0;
                        visibleWidth[idx] = 0;
                        bFoundFirst = false;

                        // set width and height for each cloned TD
                        for (i = 0, ln = rowTDs.length; i < ln; i++) {
                            rowTD = $(rowTDs.get(i));
                            cloneTD = rowTD.clone();
                            cloneWidth[idx] = cloneWidth[idx] + rowTD.outerWidth();
                            cloneTD.width(rowTD.width());
                            cloneTD.height(rowTD.height());

                            // clear the contents and colors of non-header elements
                            if (!(cloneTD.is('.' + elementClass) || (altElementClass && cloneTD.is('.' + altElementClass)))) {
                                cloneTD.html('');
                                cloneTD.css({
                                    'border-color': 'transparent',
                                    'pointer-events': 'none' // allow mouse events to pass through the transparent elements
                                });

                                // set transparent background only after header element was found
                                if (bFoundFirst || elementClass == 'jrxtcolfloating') {
                                    cloneTD.css('background-color', 'transparent');
                                } else {
                                    cloneTD.css('background-color', it.defaultPageBgColor);
                                }
                            } else {
                                visibleWidth[idx] = visibleWidth[idx] + rowTD.outerWidth();
                                bFoundFirst = true;
                            }

                            clone.append(cloneTD);
                        }
                        tbl.append(clone);
                    });

                    tbl.data("actualWidth", Math.max.apply(Math, visibleWidth));

                    tbl.css({
                        position: 'relative', // TODO: set back to 'fixed' and assign z-index to other viewer parts, to avoid flickerings
                        width: Math.max.apply(Math, cloneWidth),
                        'empty-cells': tblJrPage.css('empty-cells'),
                        'border-collapse': tblJrPage.css('border-collapse'),
                        'background-color': 'transparent'
                        ,'pointer-events': 'none' // allow mouse events to pass through the transparent elements
                        ,'z-index': it.currentZindex
                    });
                    tbl.attr('cellpadding', tblJrPage.attr('cellpadding'));
                    tbl.attr('cellspacing', tblJrPage.attr('cellspacing'));
                    tbl.attr('border', tblJrPage.attr('border'));

                    it.currentZindex += 1;
                }
            }

            return tbl;
        },
        setToolbarPositionWhenFloating: function(isDashboard) {
            var it = this, top, firstHeader, toolbarTop, firstHeaderTop;

            if (it.foobar.active) { // handle the toolbar position
                firstHeader = $('td.jrxtFirstColFloating');
                top = isDashboard ? 0 : $('div#reportViewFrame .body').offset().top,
                    toolbarTop = it.foobar.jo.offset().top,
                    firstHeaderTop = firstHeader.offset().top;

                if (!it.foobar.topCalculated) {
                    if (toolbarTop < 0) {
                        if (firstHeaderTop < 0 && toolbarTop > firstHeaderTop) {
                            top += Math.abs(toolbarTop - firstHeaderTop);
                        }
                    } else if (firstHeaderTop < 0) {
                        top += Math.abs(firstHeaderTop) + toolbarTop;
                    } else if (toolbarTop > firstHeaderTop) {
                        top += toolbarTop - firstHeaderTop;
                    }

                    it.foobar.jo.css({
                        position: 'fixed',
                        top: top,
                        left: it.selected.header.offset().left - $(window).scrollLeft()
                    });

                    it.foobar.topCalculated = true;
                } else {
                    it.foobar.jo.css({
                        left: it.selected.header.offset().left - $(window).scrollLeft()
                    });
                }
            }
        }
	};

	return ixt;
});
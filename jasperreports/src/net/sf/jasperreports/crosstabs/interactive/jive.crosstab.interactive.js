/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

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
        active: false,
		selected: null,
        isDashboard: false,
        reportInstance: null,
        isIE: (function() {
            var isIe = /msie/i.test(navigator.userAgent);
            if (!isIe) { // for IE11
                isIe = /trident\/\d+/i.test(navigator.userAgent) && /rv:\d+/i.test(navigator.userAgent);
            }
            return isIe;
        })(),
        isFirefox: /firefox/i.test(navigator.userAgent),
        canFloat: true,
		init: function(report) {
			var ic = this,
                hasFloatingHeaders = true;
			ic.reportInstance = report;
            ic.isDashboard = $('body').is('.dashboardViewFrame');

			if (!ic.initialized) {
                /**
                 * "beforeSearchAdvance" event triggered from the viewer, before highlighting search results
                 * with function "element.scrollIntoView(false)" which seems to be asynchronous;
                 * that's why the setTimeout is used here to delay the release of the canFloat flag
                */
                ic.reportInstance.on("beforeSearchAdvance", function(evt) {
                    ic.canFloat = false;

                    ic.hideFloatingElements();

                    // releasing this flag after 1.5 seconds
                    setTimeout(function() {
                        ic.canFloat = true;
                    }, 1500);
                });

                ic.reportInstance.on("beforeAction", function(evt) {
                    ic.active && ic.hide();
                });

				$('head').append('<style id="jivext-stylesheet">' + templateCss + '</style>');
			
				$('#jivext_components').length == 0 &&  $('body').append('<div id="jivext_components"></div>');
				$('#jivext_components').empty();
				$('#jivext_components').append(templates);
				
				ic.getReportContainer().on('click touchend', function(){
					ic.hide();
					//TODO 
					//$('body').trigger('jive.inactive');
				});

                // if there is more than one crosstab and only one doesn't have floating headers, none won't have
                $.each(report.components.crosstab, function() {
                    if (this.config.hasFloatingHeaders != null && this.config.hasFloatingHeaders === false) {
                        hasFloatingHeaders = false;
                        return false; // break each
                    }
                });

                if (hasFloatingHeaders) {
                    ic.setScrollableHeader(ic.isDashboard);
                }

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
			
			ixt.selected = {crosstab: crosstab, header: firstHeader, jo: cell, isRowGroup: false};
			ixt.overlay.show({w: width, h: height});
			
			var columnIdx = firstHeader.data('jrxtcolidx');
			var sortingEnabled = crosstab.isDataColumnSortable(columnIdx);
			ixt.foobar.show(sortingEnabled);
            ixt.active = true;
            ixt.justHidden = false;
		},
		selectRowGroup: function(crosstab, cell, altCell) {
			var columnIdx = cell.data('jrxtcolidx');
			var fragmentId = cell.data('jrxtid');
			var headers = $('td.jrxtrowheader[data-jrxtid=\'' + fragmentId + '\'][data-jrxtcolidx=\'' + columnIdx + '\']', cell.parents("table:first"));
			var firstHeader = $(headers[0]);
			var lastHeader = $(headers[headers.length - 1]);

            var zoomLevel = this.reportInstance.zoom && this.reportInstance.zoom.level ? this.reportInstance.zoom.level : 1;
			var width = lastHeader.offset().left + lastHeader.outerWidth() - firstHeader.offset().left;
			var height = lastHeader.offset().top + lastHeader.outerHeight() * zoomLevel - firstHeader.offset().top;

			ixt.selected = {crosstab: crosstab, header: firstHeader, jo: altCell ? altCell : cell, isRowGroup: true};
			ixt.overlay.show({w: width, h: height});
			ixt.foobar.show(true);
            ixt.active = true;
            ixt.justHidden = false;
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
            show: function(enabled){
                !this.jo && this.setElement();
                this.render(ixt.actions);
                this.jo.find('button').removeClass('over pressed disabled');
                enabled || this.jo.find('button').addClass('disabled');
                this.jo.appendTo(ixt.getReportContainer()).show();

                this.setToolbarPosition();
            },
            setToolbarPosition: function() {
                var it = this,
                    top = ixt.selected.header.offset().top - it.jo.outerHeight(),
                    containerTop;

                if (it.isDashboard) {
                    containerTop = $(window).scrollTop();
                } else if ($('div#reportViewFrame .body').length > 0) {
                    containerTop = $('div#reportViewFrame .body').offset().top;
                } else {
                    containerTop = 0;
                }

                it.jo.css({position: 'absolute', width: '62px'});
                it.jo.offset({top: top, left: ixt.selected.header.offset().left});
                it.jo.offset({top: top, left: ixt.selected.header.offset().left}); // twice
                it.topCalculated = false;
                if (ixt.isFloatingColumnHeader) {
                    ixt.setToolbarPositionWhenFloating(true, it.isDashboard);
                } else if (containerTop >= top) {
                    this.jo.offset({top: ixt.selected.header.offset().top});
                }
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
            var it = this;
            it.active && it.hide();
            if (it.isFloatingColumnHeader || it.isFloatingRowHeader || it.isFloatingCrossHeader) {
                it.scrollHeaders(it.isDashboard, true);
            }
        },
		hide: function() {
            ixt.active = false;
			ixt.overlay.jo && ixt.overlay.jo.appendTo('#jivext_components').hide();
			ixt.foobar.jo && ixt.foobar.jo.appendTo('#jivext_components').hide();
		},
        justHide: function() {
            ixt.justHidden = true;
            ixt.overlay.jo.hide();
            ixt.foobar.jo.hide();
        },
        reApplySelection: function() {
            var it = this;

            it.justHidden = false;
            if (it.selected.isRowGroup) {
                it.selectRowGroup(it.selected.crosstab, it.selected.jo);
            } else {
                it.selectDataColumn(it.selected.crosstab, it.selected.jo);
            }
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
                tblJrPage = $('table.jrPage'),
                tmr = null,
                scrollDelay = 500;

            it.scrollData = {
                bColMoved: false,
                reportContainerPositionAtMove: null,
                bRowMoved: false,
                containerLeftAtMove: null
            };

            it.defaultPageBgColor = tblJrPage.css('background-color');

            if (!isDashboard) {
                $('div#reportViewFrame .body').on('scroll', function() {
                    if (it.isIE) {
                        it.active && it.justHide();
                        it.hideFloatingElements();
                        if (tmr == null) {
                            tmr = setTimeout(function() {
                                it.scrollHeaders(isDashboard);
                                it.active && it.justHidden && it.reApplySelection();
                            }, scrollDelay);
                        } else {
                            clearTimeout(tmr);
                            tmr = setTimeout(function() {
                                it.scrollHeaders(isDashboard);
                                it.active && it.justHidden && it.reApplySelection();
                            }, scrollDelay);
                        }
                    } else {
                    	it.scrollHeaders(isDashboard);
                    }
                });
            }

             if (it.isIE) { // attach scroll to body for dashboards in IE
                $('body').on('scroll', function() {
                    it.scrollHeaders(isDashboard);

                    // reposition jive visual elements
//                    it.active && !it.ui.dialog.isVisible && it.showVisualElements(jive.selected.dim);
                });
            }
             $(window).on('resize scroll', function() {
                it.scrollHeaders(isDashboard);

                // reposition jive visual elements
//                it.active && !it.ui.dialog.isVisible && it.showVisualElements(jive.selected.dim);
            });
        },
        scrollHeaders: function(isDashboard, forceScroll) {
            if (!this.canFloat) {
                return;
            }

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
        hideFloatingElements: function() {
            var it = this;
            // hide floating parts
            if (it.isFloatingColumnHeader) {
                it.getFloatingTable('floatingColumnHeader').hide();
            }
            if (it.isFloatingRowHeader) {
                it.getFloatingTable('floatingRowHeader').hide();
            }
            if (it.isFloatingCrossHeader) {
                it.getFloatingTable('floatingCrossHeader').hide();
            }
        },
        scrollColumnHeader: function(isDashboard, scrolledLeft, scrolledTop) {
            var it = this,
                $win = $(window);

            var firstHeader = $('td.jrxtcolfloating').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            var floatingTbl = it.getFloatingTable('floatingColumnHeader', 'jrxtcolfloating', 'jrcolGroupHeader'),
                containerTop = isDashboard ? $win.scrollTop() : $('div#reportViewFrame .body').offset().top,
                headerTop = firstHeader.closest('tr').offset().top,
                reportContainerTop = $('#reportContainer').offset().top,
                firstHeaderCel = $('td.jrxtcolfloating.first'),
                lastTableCel = firstHeaderCel.closest('table').find('td.jrxtdatacell:last'),
                diff = lastTableCel.length ? lastTableCel.offset().top - floatingTbl.outerHeight() - containerTop: -1, // if last cell is not visible, hide the floating header
                scrollTop = it.cachedScroll || 0,
                zoom = ixt.reportInstance.zoom;

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
                        left: firstHeaderCel.offset().left
                    });
                } else {
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: firstHeaderCel.offset().left
                    });
                }

                it.setToolbarPositionWhenFloating(it.active, isDashboard);

                it.scrollData.bColMoved = it.isFloatingColumnHeader = true;
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
                        left: firstHeaderCel.offset().left
                    });
                } else /*if (scrolledLeft)*/ {
                    floatingTbl.offset({
                        top: isDashboard ? (it.isIPad ? scrollTop : 0) : (it.isIPad ? scrollTop : containerTop),
                        left: firstHeaderCel.offset().left
                    });
                }

                it.setToolbarPositionWhenFloating(it.active, isDashboard);
            } else if (it.scrollData.bColMoved) {
                floatingTbl.hide();
                it.scrollData.bColMoved = it.isFloatingColumnHeader = false;
                it.cachedScroll = 0;
                it.active && it.foobar.setToolbarPosition();
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
                containerLeft = isDashboard ? $win.scrollLeft() : $('div#reportViewFrame .body').offset().left,
                headerLeft = firstHeader.offset().left,
                lastTableCel = $('td.jrxtrowheader.first').closest('table').find('td.jrxtdatacell:last'),
                diff = lastTableCel.length ? lastTableCel.offset().left - floatingTbl.width() - containerLeft: -1, // if last cell is not visible, hide the floating header
                zoom = it.reportInstance.zoom,
                zoomLevel = (zoom && it.reportInstance.zoom.level) ? it.reportInstance.zoom.level : 1,
                floatingTblRight;

            if (!it.scrollData.bRowMoved && headerLeft-containerLeft < 0 && diff > 0) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                    floatingTbl.offset({
                        top: firstHeader.offset().top,
                        left: containerLeft
                    });
                } else {
                    floatingTbl.offset({
                        top: firstHeader.offset().top,
                        left: containerLeft
                    });
                }

                floatingTblRight = floatingTbl.offset().left + floatingTbl.width() * zoomLevel;

                if (it.active && it.overlay.jo.offset().left < (floatingTblRight)) {
                    it.justHide();
                }

                it.scrollData.bRowMoved = it.isFloatingRowHeader = true;

            } else if (it.scrollData.bRowMoved && headerLeft-containerLeft < 0 && diff > 0) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                    floatingTbl.offset({
                        top: firstHeader.offset().top,
                        left: containerLeft
                    });
                } else /*if (scrolledTop)*/ {
                    floatingTbl.offset({
                        top: firstHeader.offset().top,
                        left: containerLeft
                    });
                }

                if (it.active) {
                    floatingTblRight = floatingTbl.offset().left + floatingTbl.width() * zoomLevel;

                    if (!it.justHidden && it.selected.header.offset().left < floatingTblRight) {
                        it.justHide();
                    } else if (it.justHidden && ixt.selected.header.offset().left >= floatingTblRight) {
                        it.reApplySelection();
                    }
                }

            } else if (it.scrollData.bRowMoved) {
                floatingTbl.hide();
                if (it.active && it.justHidden) {
                    it.reApplySelection();
                }
                it.scrollData.bRowMoved = it.isFloatingRowHeader = false;
            }
        },
        scrollCrossSection: function(isDashboard, scrolledLeft, scrolledTop) {
            var it = this,
                firstHeader;

            if (!$('table.floatingCrossHeader').length) {
                it.markCrossHeaderElements();
            }

            firstHeader = $('td.jrxtcrossheader').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            var floatingCrossTbl = it.getFloatingTable('floatingCrossHeader', 'jrxtcrossheader'),
                floatingColumnTbl = it.getFloatingTable('floatingColumnHeader'),
                floatingRowTbl = it.getFloatingTable('floatingRowHeader'),
                zoom = ixt.reportInstance.zoom;

            if (it.scrollData.bColMoved || it.scrollData.bRowMoved) {
                floatingCrossTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingCrossTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                }

                floatingCrossTbl.offset({
                    top: it.scrollData.bColMoved ? floatingColumnTbl.offset().top : firstHeader.offset().top,
                    left: it.scrollData.bRowMoved ? floatingRowTbl.offset().left : firstHeader.offset().left
                });

//                it.setToolbarPositionWhenFloating(it.active, isDashboard);

                it.scrollData.bCrossMoved = it.isFloatingCrossHeader = true;

            } else if (it.scrollData.bCrossMoved) {
                floatingCrossTbl.hide();
                it.scrollData.bCrossMoved = it.isFloatingCrossHeader = false;
            }
        },
        getFloatingTable: function(tableClass, elementClass, altElementClass) {
            var it = this,
                tbl = $('table.' + tableClass);

            if (tbl.length == 0) {
                tbl = $("<table class='" + tableClass + "' style='display:none'/>").appendTo('div#reportContainer');

                if (elementClass == 'jrxtcolfloating') {
                    tbl.on('click', '.jrxtcolfloating', function(evt){
                        // keep html links functional
                        if(!$(evt.target).parent().is('a')) {
                            var jo = $(this),
                                crosstabId = jo.attr('data-jrxtid'),
                                colIdx = jo.attr('data-jrxtcolidx'),
                                crosstabFloatingHeader = tbl.parent()
                                    .find("table.jrPage td.jrxtcolheader[data-jrxtid='" + crosstabId + "']")
                                    .filter("td[data-jrxtcolidx='" + colIdx + "']"),
                                crosstab = null;

                            $.each(ixt.reportInstance.components.crosstab, function(i, xtab) {
                                if (crosstabId == xtab.getFragmentId()) {
                                    crosstab = xtab;
                                    return false; // break each
                                }
                            });

                            crosstab && crosstabFloatingHeader.length && ixt.selectDataColumn(crosstab, crosstabFloatingHeader);
                            return false;
                        }
                    });
                } else if (elementClass == 'jrxtrowheader') {
                    tbl.on('click', '.jrxtrowheader', function(evt){
                        // keep html links functional
                        if(!$(evt.target).parent().is('a')) {
                            var jo = $(this),
                                crosstabId = jo.attr('data-jrxtid'),
                                colIdx = jo.attr('data-jrxtcolidx'),
                                altJo = tbl.parent()
                                    .find("table.jrPage td.jrxtrowheader[data-jrxtid='" + crosstabId + "']")
                                    .filter("td[data-jrxtcolidx='" + colIdx + "']"),
                                crosstab = null;

                            $.each(ixt.reportInstance.components.crosstab, function(i, xtab) {
                                if (crosstabId == xtab.getFragmentId()) {
                                    crosstab = xtab;
                                    return false; // break each
                                }
                            });

                            crosstab && ixt.selectRowGroup(crosstab, jo, altJo);
                            return false;
                        }
                    });
                } else if (elementClass == 'jrxtcrossheader') {
                    tbl.on('click', '.jrxtcrossheader', function(evt){
                        // keep html links functional
                        if(!$(evt.target).parent().is('a')) {
                            var jo = $(this),
                                crosstabId = jo.attr('data-jrxtid'),
                                colIdx = jo.attr('data-jrxtcolidx'),
                                crosstab = null,
                                altJo;

                            if (it.isFloatingRowHeader) {
                                altJo = it.getFloatingTable('floatingRowHeader')
                                    .find("td.jrxtrowheader[data-jrxtid='" + crosstabId + "']")
                                    .filter("td[data-jrxtcolidx='" + colIdx + "']");
                                jo = altJo.eq(0);
                            } else {
                                altJo = tbl.parent()
                                    .find("table.jrPage td.jrxtrowheader[data-jrxtid='" + crosstabId + "']")
                                    .filter("td[data-jrxtcolidx='" + colIdx + "']");
                                jo = altJo.eq(0);
                            }

                            $.each(ixt.reportInstance.components.crosstab, function(i, xtab) {
                                if (crosstabId == xtab.getFragmentId()) {
                                    crosstab = xtab;
                                    return false; // break each
                                }
                            });

                            crosstab && ixt.selectRowGroup(crosstab, jo, altJo);
                            return false;
                        }
                    });
                }

                var elementSelector = 'td.' + elementClass,
                    firstHeader = $(elementSelector).filter(':first'),
                    parentTable = firstHeader.closest('table'),
                    lastHeader = $(elementSelector, parentTable).filter(':last'),
                    rows = [], clone, cloneWidth = [],
                    row, $row, lastRow, cloneTD, rowTD, rowTDs, i, j, k,
                    tblJrPage, parentTableRows,
                    bFoundFirst, cssHeight,
                    // It seems to be necessary to adjust the height of the <td>s when cloning
                    bAdjust = it.isIE || it.isFirefox,
                    adjustAmount = 0.35;

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
                        $row = $(row);
                        rowTDs = $row.find('td');
                        clone = $("<tr></tr>");
                        cloneWidth[idx] = 0;
                        clone.attr('style', $row.attr('style'));
                        clone.attr('valign', $row.attr('valign'));
                        bFoundFirst = false;

                        // add only the tds with elementClass class
                        for (i = 0; i < rowTDs.length; i++) {
                            rowTD = $(rowTDs.get(i));
                            if (rowTD.is('.' + elementClass) || (altElementClass && rowTD.is('.' + altElementClass))) {
                                // put empty TDs for the rowheader to prevent rowspans from interfering
                                if (elementClass == 'jrxtrowheader' && rowTD.is('.jrxtinteractive')) {
                                    cloneTD = $("<td class='jrxtrowheader jrxtinteractive'></td>");
                                    cloneTD.attr("data-jrxtid", rowTD.attr("data-jrxtid"));
                                    cloneTD.attr("data-jrxtcolidx", rowTD.attr("data-jrxtcolidx"));
                                } else {
                                    cloneTD = rowTD.clone();

                                    // Fix for bug #41786 - set width/height with css method to take box-sizing into account
                                    cloneTD.css("width", rowTD.css("width"));
                                    cssHeight = rowTD.css("height");
                                    cloneTD.css("height", parseInt(cssHeight.substring(0, cssHeight.indexOf("px"))) - (bAdjust ? adjustAmount : 0) + "px");

                                    cloneWidth[idx] = cloneWidth[idx] + rowTD.outerWidth();
                                }
                                clone.append(cloneTD);

                                if (elementClass == 'jrxtrowheader') {
                                    bAdjust && cloneTD.addClass('__adj');
                                }
                            }
                        }
                        tbl.append(clone);
                    });

                    tbl.css({
//                        position: 'fixed',
                        position: 'absolute',
                        width: Math.max.apply(Math, cloneWidth),
                        'empty-cells': tblJrPage.css('empty-cells'),
                        'border-collapse': tblJrPage.css('border-collapse'),
                        'background-color': tblJrPage.css('background-color')
                    });
                    tbl.attr('cellpadding', tblJrPage.attr('cellpadding'));
                    tbl.attr('cellspacing', tblJrPage.attr('cellspacing'));
                    tbl.attr('border', tblJrPage.attr('border'));
                }
            }

            return tbl;
        },
        setToolbarPositionWhenFloating: function(isActive, isDashboard) {
            var it = this, top, firstHeader, toolbarTop, firstHeaderTop;

            if (isActive) { // handle the toolbar position
                firstHeader = $('td.jrxtcolfloating.first');
                top = isDashboard ? 0 : $('div#reportViewFrame .body').offset().top;
                toolbarTop = it.foobar.jo.offset().top;
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
        },
        markCrossHeaderElements: function() {
            // Prepare the crosssection in case it doesn't exist
            if (!$('td.jrxtcrossheader').length) {
                var firstColHeader = $('td.jrxtcolfloating').filter(':first'),
                    lastColHeader,
                    firstRow,
                    lastRow,
                    lastRowGroupRow,
                    parentTable, parentTableRows, i, j, k, l,
                    rows = [],
                    bFoundRowHeader = false,
                    firstRowHeaderIdx = -1,
                    remember = [];

                if (firstColHeader.length) {
                    parentTable = firstColHeader.closest('table');
                    firstRow = firstColHeader.closest('tr');
                    lastColHeader = parentTable.find('td.jrxtcolfloating').filter(':last');
                    lastRow = lastColHeader.closest('tr');

                    if (firstRow === lastRow) {
                        rows.push(firstRow);
                    } else {
                        lastRowGroupRow = parentTable.find('td.jrxtrowheader.jrxtinteractive').filter(':last').closest('tr');
                        parentTableRows = parentTable.find('tr');
                        i = parentTableRows.index(firstRow);
                        j = parentTableRows.index(lastRow);

                        for (k = i; k <= j; k++) {
                            rows.push(parentTableRows.get(k));
                        }

                        l = parentTableRows.index(lastRowGroupRow);
                        if (l > j) {
                            for (k = j; k <=l; k++) {
                                rows.push(parentTableRows.get(k))
                            }
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
//                            if (!v.$td.children().length) {
//                                v.$td.css('background-color', ixt.defaultPageBgColor);
//                            }
                        }
                    });
                }
            }
        },
        applyScaleTransform: function($container, zoom, origin) {
            var scale = 'scale(' + zoom + ")",
                origin = origin || '50% 0',
                transform =  {
                    '-webkit-transform': scale,
                    '-webkit-transform-origin': origin,
                    '-moz-transform':    scale,
                    '-moz-transform-origin': origin,
                    '-ms-transform':     scale,
                    '-ms-transform-origin': origin,
                    '-o-transform':      scale,
                    '-o-transform-origin': origin,
                    'transform':         scale,
                    'transform-origin': origin
                };

            $container.css(transform);
        }
	};

	return ixt;
});
/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
        canFloat: null,
		init: function(report) {
			var ic = this,
                hasFloatingHeaders = true;
			ic.reportInstance = report;
            ic.isDashboard = $('body').is('.dashboardViewFrame');
            ic.canFloat = true;

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
                    // prevent floating
                    ic.canFloat = false;

                    // prevent caching floating headers
                    ic.floatingColumnHeader && ic.floatingColumnHeader.remove();
                    ic.floatingRowHeader && ic.floatingRowHeader.remove();
                    ic.floatingCrossHeader && ic.floatingCrossHeader.remove();

                    ic.floatingColumnHeader = null;
                    ic.floatingRowHeader = null;
                    ic.floatingCrossHeader = null;

                    ic.isFloatingColumnHeader = ic.isFloatingRowHeader = ic.isFloatingCrossHeader = null;

                    // hide column selection if visible
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
                    ic.$scrollContainer = $('div#reportViewFrame .body').first();
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
				if(!$(evt.target).parent().is('._jrHyperLink')) {
					var columnHeader = $(this);
					ixt.selectDataColumn(crosstab, columnHeader);
					return false;
				}
			});

            tblJrPage.on('click touchend', 'td.jrxtdatacell[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('._jrHyperLink')) {
					var dataCell = $(this);
					ixt.selectDataColumn(crosstab, dataCell);
					return false;
				}
			});

            tblJrPage.on('click touchend', 'td.jrxtrowheader[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('._jrHyperLink')) {
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
			var headers = $("td.jrxtrowheader[data-jrxtid='" + fragmentId + "'][data-jrxtcolidx='" + columnIdx + "']", cell.parents("table").first());

            if (!headers.length) {
                // nothing to select
                return;
            }

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

            it.scrollColumnHeader();
            it.scrollRowHeader();
            it.scrollCrossSection();
        },
        hideFloatingElements: function() {
            this.isFloatingColumnHeader && this.floatingColumnHeader.hide();
            this.isFloatingRowHeader && this.floatingRowHeader.hide();
            this.isFloatingCrossHeader && this.floatingCrossHeader.hide();
        },
        scrollColumnHeader: function() {
            var it = this,
                scrollContainer = this.$scrollContainer,
                crosstabId,
                isDashboard = false;

            var firstHeader = $('td.jrxtcolfloating').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            crosstabId = firstHeader.data("jrxtid");

            if (!this.floatingColumnHeader) {
                this.floatingColumnHeader = this.getColumnHeaderFloatingTable(firstHeader, crosstabId);
            }

            var floatingTbl = this.floatingColumnHeader,
                containerTop = scrollContainer.offset().top,
                headerTop = firstHeader.closest('tr').offset().top,
                firstRowHeaderCel = scrollContainer.find('td.jrxtrowheader').first(),
                lastTableCel = firstHeader.closest("table").find("td.jrxtdatacell[data-jrxtid='" + crosstabId + "']").last(),
                diff = lastTableCel.length ? lastTableCel.offset().top - floatingTbl.outerHeight() - containerTop: -1, // if last cell is not visible, hide the floating header
                zoom = ixt.reportInstance.zoom,
                floatCondition = headerTop - containerTop < 0 && diff > 0;

            if ((!it.scrollData.bColMoved && floatCondition) || (it.scrollData.bColMoved && floatCondition)) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                }
                floatingTbl.offset({
                    top: containerTop,
                    left: firstRowHeaderCel.offset().left
                });

                it.setToolbarPositionWhenFloating(it.active, isDashboard);
                it.scrollData.bColMoved = it.isFloatingColumnHeader = true;
            } else if (it.scrollData.bColMoved) {
                floatingTbl.hide();
                it.scrollData.bColMoved = it.isFloatingColumnHeader = false;
                it.active && it.foobar.setToolbarPosition();
            }
        },
        scrollRowHeader: function() {
            var it = this,
                scrollContainer = this.$scrollContainer,
                crosstabId;

            var firstHeader = $('td.jrxtrowheader').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            crosstabId = firstHeader.data("jrxtid");

            if (!this.floatingRowHeader) {
                this.floatingRowHeader = this.getFloatingTable(firstHeader, crosstabId, "jr_floating_row_header", "jrxtrowfloating");
            }

            var floatingTbl = this.floatingRowHeader,
                containerLeft = scrollContainer.offset().left,
                headerLeft = firstHeader.offset().left,
                lastTableCel = firstHeader.closest('table').find('td.jrxtdatacell').last(),
                diff = lastTableCel.length ? lastTableCel.offset().left - floatingTbl.width() - containerLeft: -1, // if last cell is not visible, hide the floating header
                zoom = it.reportInstance.zoom,
                zoomLevel = (zoom && zoom.level) ? zoom.level : 1,
                floatingTblRight,
                floatCondition = headerLeft-containerLeft < 0 && diff > 0,
                showCondition = 0.8 * scrollContainer.outerWidth() > floatingTbl.outerWidth() * zoomLevel;

            if (!it.scrollData.bRowMoved && floatCondition && showCondition) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                }
                floatingTbl.offset({
                    top: firstHeader.offset().top,
                    left: containerLeft
                });

                floatingTblRight = floatingTbl.offset().left + floatingTbl.width() * zoomLevel;

                if (it.active && it.overlay.jo.offset().left < (floatingTblRight)) {
                    it.justHide();
                }

                it.scrollData.bRowMoved = it.isFloatingRowHeader = true;

            } else if (it.scrollData.bRowMoved && floatCondition && showCondition) {
                floatingTbl.show();

                if (zoom) {
                    it.applyScaleTransform(floatingTbl, zoom.level, zoom.overflow ? '0 0' : '50% 0');
                }
                floatingTbl.offset({
                    top: firstHeader.offset().top,
                    left: containerLeft
                });

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
        scrollCrossSection: function() {
            var scrollContainer = this.$scrollContainer,
                scrollData = this.scrollData,
                firstHeader,
                firstColHeader = scrollContainer.find("td.jrxtcolfloating").first(),
                crosstabId = firstColHeader.data("jrxtid");

            if (!$('table.floatingCrossHeader').length) {
                this.markCrossHeaderElements(crosstabId);
            }

            firstHeader = $('td.jrxtcrossheader').filter(':first');
            if (!firstHeader.length > 0) {
                return;
            }

            if (!this.floatingCrossHeader) {
                this.floatingCrossHeader = this.getFloatingTable(firstHeader, crosstabId, "jr_floating_cross_header", "jrxtcrossheader");
            }

            var floatingCrossTbl = this.floatingCrossHeader,
                floatingColumnTbl = this.floatingColumnHeader || this.getColumnHeaderFloatingTable(firstHeader, crosstabId),
                floatingRowTbl = this.floatingRowHeader || this.getFloatingTable(firstHeader, crosstabId, "jr_floating_row_header"),
                zoom = ixt.reportInstance.zoom,
                zoomLevel = (zoom && zoom.level) ? zoom.level : 1,
                showCondition = 0.8 * scrollContainer.outerWidth() > floatingRowTbl.outerWidth() * zoomLevel;

            if ((scrollData.bColMoved || scrollData.bRowMoved) && showCondition) {
                floatingCrossTbl.show();

                if (zoom) {
                    this.applyScaleTransform(floatingCrossTbl, zoomLevel, zoom.overflow ? '0 0' : '50% 0');
                }

                floatingCrossTbl.offset({
                    top: scrollData.bColMoved ? floatingColumnTbl.offset().top : firstHeader.offset().top,
                    left: scrollData.bRowMoved ? floatingRowTbl.offset().left : firstHeader.offset().left
                });

                scrollData.bCrossMoved = this.isFloatingCrossHeader = true;

            } else if (scrollData.bCrossMoved) {
                floatingCrossTbl.hide();
                scrollData.bCrossMoved = this.isFloatingCrossHeader = false;
            }
        },
        getFloatingTable: function(firstHeader, crosstabId, tableClass, elementClass, altElementClass) {
            var it = this,
                tbl = this.$scrollContainer.find("table." + tableClass + "[data-jrxtid='" + crosstabId + "']");

            if (firstHeader && tbl.length === 0) {
                tbl = $("<table class='" + tableClass + "' data-jrxtid='" + crosstabId + "' style='display:none'/>").appendTo(this.$scrollContainer);

                if (elementClass == "jrxtrowfloating") {
                    tbl.on("click", ".jrxtrowfloating", function(evt){
                        // keep links functional
                        if(!$(evt.target).parent().is("._jrHyperLink")) {
                            var jo = $(this),
                                crosstabId = jo.attr("data-jrxtid"),
                                colIdx = jo.attr("data-jrxtcolidx"),
                                altJo = tbl.parent()
                                    .find("table.jrPage td.jrxtrowfloating[data-jrxtid='" + crosstabId + "']")
                                    .filter("td[data-jrxtcolidx='" + colIdx + "']").eq(0),
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
                } else if (elementClass == "jrxtcrossheader") {
                    tbl.on("click", ".jrxtcrossheader", function(evt){
                        // keep links functional
                        if(!$(evt.target).parent().is('._jrHyperLink')) {
                            var jo = $(this),
                                crosstabId = jo.attr('data-jrxtid'),
                                colIdx = jo.attr('data-jrxtcolidx'),
                                crosstab = null,
                                altJo;

                            if (it.isFloatingRowHeader) {
                                altJo = it.floatingRowHeader
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

                var parentTable = firstHeader.closest("table"),
                    lastHeader = parentTable.find("td." + elementClass + "[data-jrxtid='" + crosstabId + "']").last(),
                    rows = [], clone, cloneWidth = [],
                    row, $row, lastRow, cloneTD, rowTD, rowTDs, i, j, k,
                    tblJrPage, parentTableRows,
                    startIndex = 0, colSpanLength = 0, finishedCalculatedStartIndex = false,
                    appendedCloneTdCount;

                if (firstHeader.length > 0) {
                    row = firstHeader.closest("tr");
                    lastRow = lastHeader.closest("tr");
                    tblJrPage = firstHeader.closest("table");
                    parentTableRows = parentTable.find("tr");

                    if (row.get(0) === lastRow.get(0)) {    // need to compare the actual dom nodes
                        rows.push(row);
                    } else {
                        i = parentTableRows.index(row);
                        j = parentTableRows.index(lastRow);

                        for (k = i; k <= j; k++) {
                            rows.push(parentTableRows.get(k));
                        }
                    }

                    /*
                     * Need to compensate the cross section with rows due to rowspan variations
                     */
                    if (elementClass === "jrxtcrossheader") {
                        lastRow = $(rows[rows.length - 1]);

                        var lastRowIndex = parentTableRows.index(lastRow),
                            allRowSpans = $.map(lastRow.find("td.jrxtcrossheader"), function(td) {
                                return $(td).prop("rowspan");
                            }),
                            maxSpan = Math.max.apply(Math, allRowSpans);

                        k = 1;
                        if (maxSpan > 1) {
                            for (k; k < maxSpan; k++) {
                                rows.push(parentTableRows.get(lastRowIndex + k));
                            }
                        }
                    }

                    $.each(rows, function(idx, row) {
                        $row = $(row);
                        rowTDs = $row.find("td");
                        clone = $("<tr></tr>");
                        cloneWidth[idx] = 0;
                        clone.attr("style", $row.attr("style"));
                        clone.attr("valign", $row.attr("valign"));
                        clone.css("height", $row.css("height"));
                        clone.css("vertical-align", $row.css("vertical-align"));

                        appendedCloneTdCount = 0;

                        // add only the tds with elementClass class
                        for (i = 0; i < rowTDs.length; i++) {
                            rowTD = $(rowTDs.get(i));
                            !finishedCalculatedStartIndex && (startIndex += rowTD.prop("colspan"));
                            if (rowTD.data("jrxtid") === crosstabId && (rowTD.is("." + elementClass) || (altElementClass && rowTD.is("." + altElementClass)))) {
                                if (elementClass !== "jrxtrowfloating") {
                                    if (idx === 0 && !finishedCalculatedStartIndex) {
                                        startIndex -= rowTD.prop("colspan");
                                        finishedCalculatedStartIndex = true;
                                    }
                                    colSpanLength += rowTD.prop("colspan");
                                }

                                // put empty TDs for the rowheader to prevent rowspans from interfering
                                if (elementClass == "jrxtrowfloating" && rowTD.is(".jrxtinteractive")) {
                                    cloneTD = $("<td class='jrxtrowfloating jrxtinteractive'></td>");
                                    cloneTD.attr("data-jrxtid", rowTD.attr("data-jrxtid"));
                                    cloneTD.attr("data-jrxtcolidx", rowTD.attr("data-jrxtcolidx"));
                                } else {
                                    cloneTD = rowTD.clone();

                                    // Fix for bug #41786 - set width/height with css method to take box-sizing into account
                                    cloneTD.css("width", rowTD.css("width"));
                                    cloneTD.css("height", rowTD.css("height"));

                                    cloneTD.css("vertical-align", rowTD.css("vertical-align"));

                                    cloneWidth[idx] = cloneWidth[idx] + rowTD.outerWidth();
                                }
                                clone.append(cloneTD);
                                appendedCloneTdCount++;
                            }
                        }

                        /* First row of table.jrPage contains all the columns(with colspan = 1) with their respective
                         * size, so we must copy all the columns from it, across which the floating column header
                         * expands so that future columns with colspan will expand properly
                         */
                        if (idx === 0 && startIndex !== undefined && elementClass !== "jrxtrowfloating") {
                            var firstRow = tblJrPage.find("tr").first(),
                                firstRowTDs = firstRow.find("td"),
                                firstRowClone = $("<tr></tr>"),
                                j = startIndex;

                            for (j; j < startIndex + colSpanLength; j ++) {
                                firstRowClone.append($(firstRowTDs.get(j)).clone());
                            }

                            tbl.append(firstRowClone);
                        }

                        // append only the rows that actually contain TDs
                        appendedCloneTdCount > 0 && tbl.append(clone);
                    });

                    tbl.css({
                        position: "relative",
                        width: Math.max.apply(Math, cloneWidth),
                        "empty-cells": tblJrPage.css("empty-cells"),
                        "border-collapse": tblJrPage.css("border-collapse"),
                        "background-color": tblJrPage.css("background-color"),
                        "vertical-align": tblJrPage.css("vertical-align")
                    });
                    tbl.attr("cellpadding", tblJrPage.attr("cellpadding"));
                    tbl.attr("cellspacing", tblJrPage.attr("cellspacing"));
                    tbl.attr("border", tblJrPage.attr("border"));
                }
            }

            return tbl;
        },
        getColumnHeaderFloatingTable: function(firstHeader, crosstabId) {
            var tableClass = "jr_floating_column_header",
                elementClass = "jrxtcolfloating",
                tbl = this.$scrollContainer.find("table." + tableClass + "[data-jrxtid='" + crosstabId + "']");

            if (firstHeader && tbl.length === 0) {
                tbl = $("<table class='" + tableClass + "' data-jrxtid='" + crosstabId + "' style='display:none'/>").appendTo(this.$scrollContainer);

                tbl.on("click", ".jrxtcolfloating", function(evt){
                    // keep links functional
                    if(!$(evt.target).parent().is("._jrHyperLink")) {
                        var jo = $(this),
                            crosstabId = jo.attr("data-jrxtid"),
                            colIdx = jo.attr("data-jrxtcolidx"),
                            crosstabFloatingHeader = tbl.parent()
                                .find("table.jrPage td.jrxtcolheader[data-jrxtid='" + crosstabId + "']")
                                .filter("td[data-jrxtcolidx='" + colIdx + "']").eq(0);

                        crosstabFloatingHeader.length && crosstabFloatingHeader.trigger("click");
                        return false;
                    }
                });

                var parentTable = firstHeader.closest("table"),
                    lastHeader = parentTable.find("td." + elementClass + "[data-jrxtid='" + crosstabId + "']").last(),
                    lastInteractiveRowHeader = parentTable.find("td.jrxtrowheader.jrxtinteractive[data-jrxtid='" + crosstabId + "']").last(),
                    rows = [], clone, cloneWidth = [],
                    row, $row, lastRow, lastInteractiveRowHeaderRow, cloneTD, rowTD, rowTDs, i, j, k,
                    tblJrPage, parentTableRows,
                    startIndex = 0, colSpanLength = 0, finishedCalculatedStartIndex = false,
                    appendedCloneTdCount;

                if (firstHeader.length > 0) {
                    row = firstHeader.closest("tr");
                    lastRow = lastHeader.closest("tr");
                    lastInteractiveRowHeaderRow = lastInteractiveRowHeader.closest("tr");
                    tblJrPage = parentTable;
                    parentTableRows = parentTable.find("tr");

                    if (lastInteractiveRowHeaderRow.length && parentTableRows.index(lastInteractiveRowHeaderRow) > parentTableRows.index(lastRow)) {
                        lastRow = lastInteractiveRowHeaderRow;
                    }

                    if (row.get(0) === lastRow.get(0)) {    // need to compare the actual dom nodes
                        rows.push(row);
                    } else {
                        i = parentTableRows.index(row);
                        j = parentTableRows.index(lastRow);

                        for (k = i; k <= j; k++) {
                            rows.push(parentTableRows.get(k));
                        }
                    }

                    $.each(rows, function(idx, row) {
                        $row = $(row);
                        rowTDs = $row.find("td");
                        clone = $("<tr></tr>");
                        cloneWidth[idx] = 0;

                        clone.attr("style", $row.attr("style"));
                        clone.attr("valign", $row.attr("valign"));
                        clone.css("height", $row.css("height"));
                        clone.css("vertical-align", $row.css("vertical-align"));

                        appendedCloneTdCount = 0;

                        for (i = 0; i < rowTDs.length; i++) {
                            rowTD = $(rowTDs.get(i));

                            !finishedCalculatedStartIndex && (startIndex += rowTD.prop("colspan"));

                            if (rowTD.data("jrxtid") === crosstabId) {
                                if (idx === 0 && !finishedCalculatedStartIndex) {
                                    startIndex -= rowTD.prop("colspan");
                                    finishedCalculatedStartIndex = true;
                                }

                                colSpanLength += rowTD.prop("colspan");
                                cloneTD = rowTD.clone();

                                // Fix for bug #41786 - set width/height with css method to take box-sizing into account
                                cloneTD.css("width", rowTD.css("width"));
                                cloneTD.css("height", rowTD.css("height"));

                                cloneTD.css("vertical-align", rowTD.css("vertical-align"));

                                cloneWidth[idx] = cloneWidth[idx] + rowTD.outerWidth();

                                clone.append(cloneTD);
                                appendedCloneTdCount++;
                            }
                        }

                        /* First row of table.jrPage contains all the columns(with colspan = 1) with their respective
                         * size, so we must copy all the columns from it, across which the floating column header
                         * expands so that future columns with colspan will expand properly
                         */
                        if (idx === 0 && startIndex !== undefined) {
                            var firstRow = tblJrPage.find("tr").first(),
                                firstRowTDs = firstRow.find("td"),
                                firstRowClone = $("<tr></tr>"),
                                j = startIndex;

                            for (j; j < startIndex + colSpanLength; j ++) {
                                firstRowClone.append($(firstRowTDs.get(j)).clone());
                            }

                            tbl.append(firstRowClone);
                        }

                        // append only the rows that actually contain TDs
                        appendedCloneTdCount > 0 && tbl.append(clone);
                    });

                    tbl.css({
                        position: "relative",
                        width: Math.max.apply(Math, cloneWidth),
                        "empty-cells": tblJrPage.css("empty-cells"),
                        "border-collapse": tblJrPage.css("border-collapse"),
                        "background-color": tblJrPage.css("background-color"),
                        "vertical-align": tblJrPage.css("vertical-align")
                    });
                    tbl.attr("cellpadding", tblJrPage.attr("cellpadding"));
                    tbl.attr("cellspacing", tblJrPage.attr("cellspacing"));
                    tbl.attr("border", tblJrPage.attr("border"));
                }
            }

            return tbl;
        },
        setToolbarPositionWhenFloating: function(isActive, isDashboard) {
            var it = this, top, firstHeader, toolbarTop, firstHeaderTop;

            if (isActive) { // handle the toolbar position
                firstHeader = $('td.jrxtcolfloating').first();
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
        markCrossHeaderElements: function(crosstabId) {
            var scrollContainer = this.$scrollContainer;
            // Prepare the crosssection in case it doesn't exist
            if (!scrollContainer.find("td.jrxtcrossheader[data-jrxtid='" + crosstabId + "']").length) {
                var firstColHeader = scrollContainer.find("td.jrxtcolfloating[data-jrxtid='" + crosstabId + "']").first(),
                    firstRow, lastRow, parentTable, parentTableRows, i,
                    firstRowIndex, lastRowIndex, rows = [],
                    maxSpan = 1, row, currentRowMaxSpan, currentRowSpans,
                    colSpanLength, colSpanStop, bFoundColHeader;

                if (firstColHeader.length) {
                    parentTable = firstColHeader.closest("table");
                    firstRow = firstColHeader.closest("tr");
                    parentTableRows = parentTable.find("tr");

                    lastRow = parentTable.find("td.jrxtrowheader.jrxtinteractive[data-jrxtid='" + crosstabId + "']").last().closest("tr");

                    if (!lastRow.length) {
                        lastRow = parentTable.find("td.jrxtcolfloating[data-jrxtid='" + crosstabId + "']").last().closest("tr");
                    }

                    firstRowIndex = parentTableRows.index(firstRow);
                    lastRowIndex = parentTableRows.index(lastRow);

                    for (i = firstRowIndex; i <= lastRowIndex; i++) {
                        rows.push(parentTableRows.get(i));

                        row = $(parentTableRows.get(i));
                        currentRowSpans = $.map(row.find("td"), function(td) {
                            return $(td).prop("rowspan");
                        });
                        currentRowMaxSpan = Math.max.apply(Math, currentRowSpans);

                        if (currentRowMaxSpan > maxSpan) {
                            maxSpan = currentRowMaxSpan;
                        }

                        maxSpan--;
                    }

                    i = 1;
                    if (maxSpan > 1) {
                        for (i; i < maxSpan; i++) {
                            rows.push(parentTableRows.get(lastRowIndex + i));
                        }
                    }

                    $.each(rows, function(idx, row) {
                        colSpanLength = 0;

                        $(row).find("td").each(function(tdIdx, td) {
                            var $td = $(td);

                            colSpanLength += $td.prop("colspan");

                            if ($td.data("jrxtid") === crosstabId) {
                                if (idx === 0 && !bFoundColHeader && $td.is(".jrxtcolfloating")) {
                                    bFoundColHeader = true;
                                    colSpanStop = colSpanLength - $td.prop("colspan");
                                }

                                if (!bFoundColHeader || (colSpanLength <= colSpanStop && !$td.is(".jrxtcolfloating"))) {
                                    $td.addClass("jrxtcrossheader");
                                }
                            }
                        });
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
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

define(["jquery.ui", "jive"], function($, jive) {
    var EventManager = null;

    var DURATION_PATTERN = "[h]:mm:ss";

    jive.interactive.column = {
        genericPropertiesInitialized: false,
        uuid: null,
        allColumns: {},
        allColumnGroups: {},
        allColumnsMap: {},
        columnsData: {},
        count: 0,
        ids: {},
        fontSizes:null,
        fonts: {
            extension: null,
            system: null
        },
        patterns: null,
        operators: null,
        filterPatterns: null,
        calendarPatterns: null,
        actions: {
            format: {icon: 'formatIcon', title: jive.i18n.get('column.format.title'), actions:{
                formatHeaderAndValues: {label: jive.i18n.get('column.format.formatmenu.label'), fn:'formatHeader'},
                hideColumn: {label: jive.i18n.get('column.format.hidecolumn.label'), fn:'hide', arg:'{"hide":true}'},
                showColumns: {label: jive.i18n.get('column.format.showcolumns.label'), actions:{
                    showAll: {label: jive.i18n.get('column.format.showcolumns.all.label'), fn: 'hide', arg:'{"hide":false,"column":"all"}'}
                }}
            }},
            filter: {icon: 'filterIcon', title: jive.i18n.get('column.filter.title'), fn: 'filter'},
            sortAscending: {icon: 'sortAscIcon', title: jive.i18n.get('column.sortasc.title'), fn:'sort', arg:['Asc']},
            sortDescnding: {icon: 'sortDescIcon', title: jive.i18n.get('column.sortdesc.title'), fn: 'sort', arg:['Desc']}
        },
        dropColumns: {},
        dropPoints: {},
        visibleColumnsMoveData: {},
        ldi: null,
        rdi: null,
        delta: null,
        reportInstance: null,

        init: function(report) {
            var it = this;

            it.reportInstance = report;
            EventManager = report.eventManager;

            jive.init(report);

            $.each(report.components.table, function() {
                // dynamic properties (fonts, fontsizes) come only for the first table
                if (!it.genericPropertiesInitialized && this.config.genericProperties) {
                	it.setGenericProperties(this.config.genericProperties);
                    it.genericPropertiesInitialized = true;
                }
                if(!it.genericPropertiesInitialized) {
                    alert('Error: generic properties not set for interactive column.');
                } else {
                    it.initColumns(this, report.config.container);
                }
            });
        },

        initColumns: function(Table, jqReportContainer){
            var c,i,it = this, lt, tableCols = [], colData, prop;
            var tableUuid = Table.config.id;
            var allColumns = Table.config.allColumnsData;

            it.allColumns[tableUuid] = allColumns;
            it.allColumnGroups[tableUuid] = Table.config.allColumnGroupsData;
            it.allColumnsMap[tableUuid] = {};

            /*
             * Load dynamic form data
             */
            it.basicFormatForm.elements[1][0][0].groups = [{name: jive.i18n.get('column.dialog.extfonts'),values:[]}];
            $.each(it.fonts.extension,function(i,v) {
                it.basicFormatForm.elements[1][0][0].groups[0].values.push([v,v]);
            });
            it.basicFormatForm.elements[1][0][0].groups.push({name: jive.i18n.get('column.dialog.sysfonts'),values:[]});
            $.each(it.fonts.system,function(i,v) {
                it.basicFormatForm.elements[1][0][0].groups[1].values.push([v,v]);
            });

            it.basicFormatForm.elements[1][0][1].values = [];
            $.each(it.fontSizes,function(i,v) {
                it.basicFormatForm.elements[1][0][1].values.push([v,v]);
            });

            /*
             * Compute drop boundaries (x-axis only) for DnD visual feedback.
             */
            it.dropPoints[tableUuid] = [];
            it.visibleColumnsMoveData[tableUuid] = [];
            it.dropColumns[tableUuid] = [];

            var firstColumnHeader = jqReportContainer.find("table.jrPage").find('td.jrcolHeader[data-tableuuid=' + tableUuid + ']:first');
            var parentContainer;
            firstColumnHeader.parents('table').each(function(i, v) {
                parentContainer = $(v);
                tableCols = parentContainer.find('td.jrcolHeader[data-tableuuid=' + tableUuid + ']');
                if (tableCols.length > 0) {
                    return false; //break each
                }
            });

            var colsData = it.columnsData[tableUuid] = {};

            for (i = 0; i < tableCols.length; i++) {
                var colUuid = $(tableCols.get(i)).data('coluuid'),
                    cols, firstCol, lastCol, realWidth, firstLeft;
                if (colsData[colUuid]) continue;

                cols = parentContainer.find('td.jrcolHeader[data-coluuid=' + colUuid + ']');
                firstCol = cols.eq(0);
                if (cols.size() > 0) {
                    lastCol = cols.eq(cols.size()-1);
                } else {
                    lastCol = firstCol;
                }

                realWidth = firstCol.outerWidth();
                firstLeft = firstCol.position().left;

                cols.each(function(i, v) {
                    var it = $(v);
                    if (it.position().left < firstLeft) {//should not happen but let's be safe
                        realWidth += firstLeft - it.position().left;
                        firstLeft = it.position().left;
                    }
                    if (it.position().left + it.outerWidth() > firstLeft + realWidth) {
                        realWidth = it.position().left + it.outerWidth() - firstLeft;
                    }
                });

                colsData[colUuid] = {
                    jo: $(firstCol),
                    width: realWidth,
                    height: lastCol.position().top - firstCol.position().top + lastCol.height(),
                    colidx: lastCol.data('colidx')
                };
            }

            tableCols = [];
            for (prop in colsData) {	// convert object to array
                if (colsData.hasOwnProperty(prop)) {
                    tableCols.push(colsData[prop]);
                }
            }
            tableCols.sort(function(col1,col2) {
                return col1.jo.data('colidx')-col2.jo.data('colidx');
            });

            for (i = 0; i < tableCols.length; i++) {
                c = tableCols[i];
                lt = c.jo.offset().left;
                colData = it.getColumnByUuid(c.jo.data('coluuid'), tableUuid);
                if (colData != null) {
                    colData.visible = true;	// enable column
                }
                it.dropColumns[tableUuid].push('cel_'+c.jo.data('cellid'));
                it.dropPoints[tableUuid].push(lt);
                it.visibleColumnsMoveData[tableUuid].push({
                    left: lt,
                    right: lt + c.width,
                    width: c.width,
                    index: colData != null ? colData.index : null,
                    uuid: c.jo.data('coluuid')
                });

                if (i == tableCols.length - 1) {
                    it.dropPoints[tableUuid].push(lt + c.width);
                }
            }

            var markers = [];
            for(i=0;i<it.dropColumns[tableUuid].length;i++) {
                markers.push(it.dropPoints[tableUuid][i]);
                markers.push(it.dropPoints[tableUuid][i] + (it.dropPoints[tableUuid][i+1] - it.dropPoints[tableUuid][i]) / 2);
            }
            markers.push(it.dropPoints[tableUuid][i]);
            it.dropPoints[tableUuid] = markers;

            /*
             * Create show column menu
             */
            var menu = it.actions.format.actions.showColumns;
            for(i in allColumns) {
                c = allColumns[i];
                if (c.interactive) {
                    menu.actions[c.uuid] = {label: jive.decodeHTML(c.label), fn:'hide', arg:'{"hide":false, "columnUuid": "' + c.uuid + '"}'};
                }
                it.allColumnsMap[tableUuid][c.uuid] = c;
            }
            it.count = it.dropColumns[tableUuid].length;

            jive.ui.foobar.reset();
            jive.ui.forms.add(jive.interactive.column.columnFilterForm);
            jive.ui.forms.add(jive.interactive.column.columnConditionalFormattingForm);
            jive.ui.forms.add(jive.interactive.column.basicFormatForm);

            EventManager.registerEvent('jive.interactive.column.init').trigger();

            $.each(Table.columns, function(k, thisColumn){
                thisColumn && jive.initInteractiveElement(thisColumn, jqReportContainer);
            });
        },
        getJoForCurrentSelection: function(jo) {
            var result = null;
            jo.parents('table').each(function(i, v) {
                result = $(v).find('td.jrcolHeader[data-cellid="' + jo.data('cellid') + '"]:first');
                if (result.length > 0) {
                    return false; //break each
                }
            });
            return result;
        },
        getColumnByUuid: function(columnUuid, tableUuid) {
            var tableColumns = this.allColumns[tableUuid],
                colIdx;

            for (colIdx in tableColumns) {
                if (tableColumns[colIdx].uuid === columnUuid) {
                    return tableColumns[colIdx];
                }
            }
            return null;
        },
        getInteractiveElementFromProxy: function(cellJo){
            var classes = cellJo.attr('class').split(' '),
                headerSel = 'td[data-cellid="' + classes[1].substring(4) + '"]:first',
                headerJo = null,
                cellJoOffsetTop = cellJo.offset().top;

            cellJo.parents().each(function(i, v) {
                headerJo = $(headerSel, v);
                if (headerJo && headerJo.length > 0
                    && headerJo.offset().top < cellJoOffsetTop) {	// the header must be above the cell
                    return false;	// break each
                }
                headerJo = null;
            });

            return headerJo;
        },
        zoom: function(o) {
            jive.zoom(o);
        },
        getElementSize: function(){
            var it = this,
                jo = jive.selected.jo,
                cid = jo.data('cellid'),
                h = null;

            cid = ('' + cid).replace(/\./g,'\\.');

            jo.parents().each(function(i, v) {
                var lastCell = $('td.cel_' + cid + ':last', v);
                if(lastCell && lastCell.length > 0) {
                    var lastElemTop = lastCell.offset().top;
                    var lastElemHeight = lastCell.outerHeight() * (it.reportInstance.zoom && it.reportInstance.zoom.level ? it.reportInstance.zoom.level : 1);
                    h = lastElemTop + lastElemHeight - jo.offset().top;
                    return false; // break each
                }
            });

            if (h === null) {
                h = jive.selected.realHeight;
            }

            return {w:jive.selected.realWidth, h:h};
        },
        onSelect: function(){
            var it = this,
                allOption = [],
                pmenu = jive.ui.foobar.menus.column.showColumns.jo,
                tableUuid = jive.selected.jo.data('tableuuid'),
                allColumns = it.allColumns[tableUuid];

            if(it.count == 1) {
                jive.ui.foobar.menus.column.format.jo.find('li').eq(1).hide();
                jive.ui.overlay.jo.draggable('option','disabled',true);
            }else {
                jive.ui.foobar.menus.column.format.jo.find('li').eq(1).show();
                jive.ui.overlay.jo.draggable('option','disabled',false);
            }

            pmenu.children().each(function(i,el){
                if (i > 0) {
                    var menuItmArgs = $(el).data('args');
                    var col = it.getColumnByUuid(menuItmArgs.columnUuid, tableUuid);
                    if (col != null) {
                        menuItmArgs.column = [col.index];
                        $(el).data('args', menuItmArgs);
                    }
                    if (col && col.visible === true) {
                        el.style.display = 'none';
                    } else if (col){
                        allOption.push(col.index);
                        el.style.display = 'block';
                    } else {
                        el.style.display = 'none';
                    }
                }
            });

            if(allOption.length > 0){
                jive.ui.foobar.menus.column.format.jo.find('li').eq(2).show();
                pmenu.children().eq(0).data('args',{hide:false,column:allOption});
            } else {
                jive.ui.foobar.menus.column.format.jo.find('li').eq(2).hide();
            }

            // disable sort/filter if not sortable/filterable
            var sortButtons = $('button:eq(2), button:eq(3)', jive.ui.foobar.jo),
                filterButton = $('button:eq(1)', jive.ui.foobar.jo);
            !jive.selected.ie.config.canSort ? sortButtons.addClass('disabled') : sortButtons.removeClass('disabled');
            !jive.selected.ie.config.canFilter ? filterButton.addClass('disabled') : filterButton.removeClass('disabled');
        },
        onDragStart: function(){
            var prop, i;
            this.uuid = jive.selected.jo.data('tableuuid');
            this.currentColumnsMoveData = this.visibleColumnsMoveData[this.uuid];
            this.currentColMoveData = null;

            for (i = 0; i < this.currentColumnsMoveData.length; i++) {
                if (this.currentColumnsMoveData[i].uuid === jive.selected.jo.data('coluuid')) {
                    this.currentColMoveData = this.currentColumnsMoveData[i];
                    break;
                }
            }

            var c = 'cel_'+ jive.selected.jo.data('cellid');
            var ci = $.inArray(c,this.dropColumns[this.uuid]) * 2;
            this.ldi = ci == 0 ? 0 : ci - 1;
            this.rdi =  ci + 3 == this.dropPoints[this.uuid].length ? ci + 2 : ci + 3;

            this.delta = jive.ui.marker.position.left - this.dropPoints[this.uuid][ci+2];
            this.dropColumnIndex = ci;

            this.colToMoveToIndex = this.currentColMoveData.index;
        },
        onDrag: function(evt,ui){
            var ev = evt.originalEvent.originalEvent || evt;
            var markers = this.dropPoints[this.uuid];
            var i = 0, ln = this.currentColumnsMoveData.length, colMoveData, refColIndex, refColMiddle, isLeftToRight;

            if(evt.pageX < markers[this.ldi]) {
                if(this.ldi > 0){
                    this.dropColumnIndex = this.ldi % 2 == 1 ? this.ldi - 1 : this.ldi;
                    jive.ui.marker.jo.css('left', markers[this.dropColumnIndex] + this.delta +'px').show();
                    this.rdi = this.ldi;
                    this.ldi--;
                }
            }
            if(evt.pageX > markers[this.rdi]) {
                if(this.rdi < (markers.length-1)) {
                    this.dropColumnIndex = this.rdi % 2 == 1 ? this.rdi + 1 : this.rdi;
                    jive.ui.marker.jo.css('left', markers[this.dropColumnIndex] + this.delta + 'px').show();
                    this.ldi = this.rdi;
                    this.rdi++;
                }
            }

            // determine move direction
            if (evt.pageX > this.currentColMoveData.right) {
                isLeftToRight = true;
            } else if (evt.pageX < this.currentColMoveData.left){
                isLeftToRight = false;
            }

            // find column based on event.pageX
            for (; i < ln; i++) {
                colMoveData = this.currentColumnsMoveData[i];
                if (evt.pageX <= colMoveData.right) {
                    refColIndex = parseInt(colMoveData.index);
                    refColMiddle = colMoveData.left + colMoveData.width / 2;

                    if (evt.pageX <= refColMiddle) { // move left, relative to column middle
                        if (refColIndex > 0) {
                            if (isLeftToRight === true) {
                                this.colToMoveToIndex = refColIndex - 1;
                            } else if (isLeftToRight === false) {
                                this.colToMoveToIndex = refColIndex;
                            }
                        } else {
                            this.colToMoveToIndex = 0;
                        }
                    } else { // move right, relative to column middle
                        if (isLeftToRight === true) {
                            this.colToMoveToIndex = refColIndex;
                        } else if (isLeftToRight === false) {
                            this.colToMoveToIndex = refColIndex + 1;
                        }
                    }
                    break;
                }
            }

            if (isLeftToRight && this.colToMoveToIndex == null) {	// for maximum drag to right, move to index of last visible column
                this.colToMoveToIndex = parseInt(this.currentColumnsMoveData[ln-1].index);
            }
        },
        onDragStop: function(ev,ui){
            if(this.colToMoveToIndex != null && this.colToMoveToIndex != jive.selected.ie.config.columnIndex) {
                jive.selected.ie.move({index: this.colToMoveToIndex});
            }
        },
        resize: function(width){
            var w = width < 8 ? 8 : Math.floor(width);
            jive.hide();
            jive.selected.ie.resize({width: w});
        },
        sort: function(argv){
            jive.hide();
            jive.selected.ie.sort({order: argv[0]});
        },
        filter: function(){
            var label = jive.selected.ie.config.columnLabel;
            !label.length && (label = "#" + (jive.selected.ie.config.columnIndex + 1));

            jive.ui.dialog.show(jive.i18n.get('column.filter.dialog.title') + ': ' + label, ['columnfilter']);
        },
        formatHeader: function(){
            var label = jive.selected.ie.config.columnLabel;
            !label.length && (label = "#" + (jive.selected.ie.config.columnIndex + 1));

            jive.ui.dialog.show(jive.i18n.get('column.format.dialog.title') + ': ' + label, ['basicFormat', 'columnConditionalFormatting']);
        },
        hide: function(args){
            jive.hide();
            if(args.hide) {
                jive.selected.ie.hide();
            } else {
                var columnIndices, allColumnsMap;
                if(args.columnUuid) {
                    if (!jive.elements[args.columnUuid]) {
                        allColumnsMap = this.allColumnsMap[jive.selected.ie.config.parentId];
                    } else {
                        allColumnsMap = this.allColumnsMap[jive.elements[args.columnUuid].config.parentId];
                    }
                    columnIndices = [allColumnsMap[args.columnUuid].index];
                } else {
                    columnIndices = args.column;
                }
                jive.selected.ie.unhide(columnIndices);
            }
        },
        setGenericProperties: function (obj) {
            jive.interactive.column.fontSizes = obj.fontSizes;
            jive.interactive.column.fonts = obj.fonts;
            jive.interactive.column.patterns = obj.patterns;
            jive.interactive.column.operators = obj.operators;
            jive.interactive.column.filterPatterns = obj.filterPatterns;
            jive.interactive.column.calendarPatterns = obj.calendarPatterns;
        },
        showCurrencyDropDown: function(){
            jive.selected.form.inputs['currencyBtn1'].showOptions();
        },
        applyCurrencyFormat: function(args) {
            var it = this,
                input = jive.selected.form.inputs['currencyBtn1'],
                cSymbol = $('#formatPattern').data('cSymbol');

            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);

                if (DURATION_PATTERN !== opt.val()) {

                    // attempt to remove current symbol, if present
                    if (cSymbol && cSymbol.length > 0) {
                        opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), false, cSymbol));
                        opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), false, cSymbol));
                    }

                    // apply new symbol
                    if (args.val && args.val.length > 0) {
                        opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), true, args.val));
                        opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), true, args.val));
                        $('#formatPattern').data('cSymbol', args.val);
                    }
                }
            });

            input.hideOptions();
        },
        togglePercentageFormat: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);

                if (DURATION_PATTERN !== opt.val()) {
                    opt.text(it.numberFormat.addRemovePercentageForNumber(opt.text(), jive.selected.form.inputs['percentageBtn'].value));
                    opt.val(it.numberFormat.addRemovePercentage(opt.val(), jive.selected.form.inputs['percentageBtn'].value));
                }
            });
        },
        toggleCommaFormat: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);

                if (DURATION_PATTERN !== opt.val()) {
                    opt.text(it.numberFormat.addRemoveThousandsSeparator(opt.text(), jive.selected.form.inputs['commaBtn'].value));
                    opt.val(it.numberFormat.addRemoveThousandsSeparator(opt.val(), jive.selected.form.inputs['commaBtn'].value));
                }
            });
        },
        addDecimal: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);
                if (DURATION_PATTERN !== opt.val()) {
                    opt.text(it.numberFormat.addRemoveDecimalPlace(opt.text(), true));
                    opt.val(it.numberFormat.addRemoveDecimalPlace(opt.val(), true));
                }
            });
        },
        remDecimal: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);

                if (DURATION_PATTERN !== opt.val()) {
                    opt.text(it.numberFormat.addRemoveDecimalPlace(opt.text(), false));
                    opt.val(it.numberFormat.addRemoveDecimalPlace(opt.val(), false));
                }
            });
        },
        numberFormat: {
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
                        var decimalPart = jive.interactive.column.numberFormat.regex.decimalPart.exec(token)[1];

                        return token.replace(decimalPart, decimalPart + '0');

                    } else { // no decimals
                        var numberPart = jive.interactive.column.numberFormat.regex.numberPart.exec(token)[1];

                        return token.replace(numberPart, numberPart + '.0');
                    }
                }

                function removeDecimalPlaceFromToken (token) {
                    var result = token,
                        dotIndex = result.indexOf('.');

                    if (dotIndex != -1) {
                        var decimalPart = jive.interactive.column.numberFormat.regex.decimalPart.exec(result)[1];

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
                    var indexOfNumericChar = token.indexOf(jive.interactive.column.numberFormat.regex.numericChar.exec(token)),
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
                    exp = jive.interactive.column.numberFormatExpression || '###0',
                    pozToken = exp.split(';')[0];

                exp = negPattern.replace(pozPatternRegex, pozToken);
                return exp;
            },

            addRemovePercentageForNumber: function (numberExp, booleanAdd) {
                var numberPart = jive.interactive.column.numberFormat.regex.numberPart.exec(numberExp)[1];

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

            addRemoveCurrencySymbol: function(exp, booleanAdd, currencySymbol) {
                var cs = currencySymbol || jive.interactive.column.numberFormat.symbols.currency,
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
        }
    }

    jive.interactive.column.columnFilterForm = {
        name: 'columnfilter',
        method: 'get',
        jc: {},
        elements: [
            [[{type:'radio',id:'clearFilter',label: jive.i18n.get('column.filterform.clearfilter.true.label'), value:'true'}]],
            [
                [{type:'radio',id:'clearFilter',label: jive.i18n.get('column.filterform.clearfilter.false.label'), value:'false',colspan:4}],
                [
                    {type:'list', id:'filterTypeOperator', values:[]},
                    {type:'text', id:"fieldValueStart", value:''},
                    {type:'label', value:'and'},
                    {type:'text', id:"fieldValueEnd", value:''}
                ]
            ]
        ],
        onCreate:function(jo){
            /*
             *  This method is called when form is created. Can be used to initiate behavior and cache form elements.
             */
            var it = this;
            it.jc.filterStart = $('#fieldValueStart');
            it.jc.filterEnd = $('#fieldValueEnd').prop('disabled',true);
            it.jc.filterType = $('#filterTypeOperator').change(function(){
                if($(this).val().indexOf('BETWEEN') >= 0){
                    it.jc.filterEnd.parent().parent().prev().show();
                    it.jc.filterEnd.parent().parent().show();
                    it.jc.filterEnd.prop('disabled',false);
                } else {
                    it.jc.filterEnd.parent().parent().prev().hide();
                    it.jc.filterEnd.parent().parent().hide();
                    it.jc.filterEnd.prop('disabled',true);
                }
            });
            $('input[name="clearFilter"]').change(function(){
                var filtertype = jive.selected.ie.config.filtering.filterData.filterType.toLowerCase();

                for(p in it.jc) (it.jc.hasOwnProperty(p) && $(this).val() == 'true') ? it.jc[p].prop('disabled',true) : it.jc[p].prop('disabled',false);
                it.jc.filterEnd.prop('disabled', (it.jc.filterType.val().indexOf('BETWEEN') >= 0 && $(this).val() == 'false') ? false : true);

                if (filtertype == 'boolean' && $(this).val() == 'false') {
                    it.jc.filterStart.prop('disabled', true);
                    it.jc.filterStart.closest('td').hide();
                }
            });
        },
        onShow:function(){
            // hide applyTo and prev/next col
            var dialog = jive.ui.dialog.jo;
            dialog.find('.applytoWrapper').hide();
            dialog.find('#colprev').hide();
            dialog.find('#colnext').hide();

            var it = this,
                metadata = jive.selected.ie.config.filtering.filterData,
                filtertype = metadata.filterType.toLowerCase(),
                options = jive.interactive.column.operators[filtertype],
                calendarPattern = jive.interactive.column.calendarPatterns["date"],
                calendarTimePattern = jive.interactive.column.calendarPatterns["time"],
                p;

            it.jc.filterType.empty();

            $.each(options, function(i, option) {
                it.jc.filterType.append($("<option/>", {
                    value: option.key,
                    text: option.val
                }));
            });

            metadata.filterTypeOperator && it.jc.filterType.val(metadata.filterTypeOperator);
            if (filtertype === 'text') {
                it.jc.filterStart.val(jive.decodeHTML(metadata.fieldValueStart));
            } else {
                it.jc.filterStart.val(metadata.fieldValueStart);
            }

            var filterOff = metadata.filterTypeOperator == null ? true : false;
            $('input[name="clearFilter"][value="'+filterOff+'"]').prop("checked",true);
            for(p in it.jc) (it.jc.hasOwnProperty(p) && filterOff) ? it.jc[p].prop('disabled',true) : it.jc[p].prop('disabled',false);

            if(!filterOff && metadata.filterTypeOperator && metadata.filterTypeOperator.indexOf('BETWEEN') >= 0) {
                it.jc.filterEnd.parent().parent().prev().show();
                it.jc.filterEnd.parent().parent().show();
                it.jc.filterEnd.val(metadata.fieldValueEnd).prop('disabled', false);
            } else {
                it.jc.filterEnd.parent().parent().prev().hide();
                it.jc.filterEnd.parent().parent().hide();
                it.jc.filterEnd.val(metadata.fieldValueEnd).prop('disabled', true);
            }

            if (filtertype === 'boolean') {
                it.jc.filterStart.prop('disabled',true);
                it.jc.filterStart.closest('td').hide();
            } else {
                it.jc.filterStart.closest('td').show();
            }
            if(filtertype === 'date') {
                var pickerOptions = {
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: calendarPattern,
                    timeFormat: calendarTimePattern,
                    showSecond: true
                }
                it.jc.filterStart.timepicker('destroy');
                it.jc.filterEnd.timepicker('destroy');
                it.jc.filterStart.datetimepicker(pickerOptions);
                it.jc.filterEnd.datetimepicker(pickerOptions);
            } else if (filtertype === 'time') {
                var timePickerOptions = {
                    timeFormat: calendarTimePattern,
                    showSecond:true,
                    constrainInput:false
                }
                it.jc.filterStart.datetimepicker('destroy');
                it.jc.filterEnd.datetimepicker('destroy');
                it.jc.filterStart.timepicker(timePickerOptions);
                it.jc.filterEnd.timepicker(timePickerOptions);
            } else {
                it.jc.filterStart.datetimepicker('destroy');
                it.jc.filterStart.timepicker('destroy');
                it.jc.filterEnd.datetimepicker('destroy');
                it.jc.filterEnd.timepicker('destroy');
            }
        },
        submit: function() {
            var filterData = {
                    tableUuid: jive.selected.ie.config.parentId,
                    filterPattern: jive.selected.ie.config.filtering.filterData.filterPattern,
                    fieldValueStart: jive.selected.form.jo.find('input[name="fieldValueStart"]').val(),
                    filterTypeOperator: jive.selected.form.jo.find('select[name="filterTypeOperator"]').val(),
                    clearFilter: jive.selected.form.jo.find('input[name="clearFilter"]:checked').val()
                };

            if(!jive.selected.form.jo.find('input[name="filterValueEnd"]').is(':hidden')){
                filterData.fieldValueEnd = jive.selected.form.jo.find('input[name="fieldValueEnd"]').val();
            }

            jive.hide();
            jive.selected.ie.filter(filterData);
        }
    };

    jive.interactive.column.basicFormatForm = {
        actionDataCache: {},
        title: jive.i18n.get('column.basicFormatForm.title'),
        name: 'basicFormat',
        method: 'get',
        prevApplyTo: null,
        elements: [
            [
                [
                    {type: 'text', id: 'headingName', label: jive.i18n.get('column.formatHeaderForm.headingName.label'), value: '', colspan: 4}
                ]
            ],
            // common format
            [
                [
                    {type: 'list', id: 'fontName', label: jive.i18n.get('column.formatforms.fontName.label'), values: [], freeText: true, size: 6, rowspan: 2},
                    {type: 'list', id: 'fontSize', label: jive.i18n.get('column.formatforms.fontSize.label'), values: [], freeText: true, size: 6, rowspan: 2},
                    {
                        type: 'buttons',
                        label: jive.i18n.get('column.formatforms.styleButtons.label'),
                        items: [
                            {type: 'checkbox', id: 'fontBold', value: 'bold', bIcon: 'boldIcon'},
                            {type: 'checkbox', id: 'fontItalic', value: 'italic', bIcon: 'italicIcon'},
                            {type: 'checkbox', id: 'fontUnderline', value: 'underline', bIcon: 'underlineIcon'}
                        ]
                    },
                    {
                        type: 'buttons',
                        label: jive.i18n.get('column.formatforms.color.label'),
                        items: [
                            {type: 'backcolor', id: 'fontBackColor', bIcon: 'backgroundColorIcon', title: jive.i18n.get('column.formatforms.fontBackColor.title'), drop: true, showTransparent: true, styleClass: 'wide'},
                            {type: 'color', id: 'fontColor', bIcon: 'fontColorIcon', title: jive.i18n.get('column.formatforms.fontColor.title'), drop: true}
                        ]
                    }
                ],
                [
                    {
                        type: 'buttons',
                        label: jive.i18n.get('column.formatforms.alignment.label'),
                        items: [
                            {type: 'radio', id: 'fontAlign', value: 'Left', bIcon: 'leftIcon'},
                            {type: 'radio', id: 'fontAlign', value: 'Center', bIcon: 'centerIcon'},
                            {type: 'radio', id: 'fontAlign', value: 'Right', bIcon: 'rightIcon'}
                        ]
                    }
                ]
            ],
            // date/number format
            [
                [
                    {type: 'list', id: 'formatPattern', label: jive.i18n.get('column.formatCellsForm.formatPattern.label'), freeText: true, values: [], colspan: 2, size: 4, rowspan: 2},
                    {
                        type: 'buttons',
                        id: 'numberFormatButtons',
                        label: jive.i18n.get('column.formatforms.numberformat.label'),
                        items: [
                            //                  {type:'checkbox',id:'currencyBtn',fn:'toggleCurrencyFormat',value:'',bIcon:'currencyIcon'},
                            {type: 'checkbox', id: 'percentageBtn', fn: 'togglePercentageFormat', value: '', bIcon: 'percentageIcon'},
                            {type: 'checkbox', id: 'commaBtn', fn: 'toggleCommaFormat', value: '', bIcon: 'commaIcon'},
                            {type: 'action', id: 'increaseDecimalsBtn', fn: 'addDecimal', value: '', bIcon: 'increaseDecimalsIcon'},
                            {type: 'action', id: 'decreaseDecimalsBtn', fn: 'remDecimal', value: '', bIcon: 'decreaseDecimalsIcon'}
                        ]
                    }
                ],
                [
                    {
                        type: 'buttons',
                        label: jive.i18n.get('column.formatforms.currency.label'),
                        items: [
                            {
                                type: 'dropdown',
                                drop: true,
                                id: 'currencyBtn1',
                                fn: 'showCurrencyDropDown',
                                bIcon: 'currencyIcon',
                                options: {
                                    'none': {label: jive.i18n.get('column.formatforms.currency.none.label'), value: '', fn: 'applyCurrencyFormat'},
                                    'locale_specific': {label: jive.i18n.get('column.formatCellsForm.numberFormatButtons.localespecific.label'), value: '\u00A4', fn: 'applyCurrencyFormat'},
                                    'dollar': {label: '\u0024 - USD', value: '\u0024', fn: 'applyCurrencyFormat'},
                                    'euro': {label: '\u20AC - EUR', value: '\u20AC', fn: 'applyCurrencyFormat'},
                                    'pound': {label: '\u00A3 - GBP', value: '\u00A3', fn: 'applyCurrencyFormat'},
                                    'yen': {label: '\u00A5 - YEN', value: '\u00A5', fn: 'applyCurrencyFormat'}
                                }
                            }
                        ]
                    }
                ]
            ]
        ],
        onCreate: function(jo) {
            var it = this,
                selector = $('#applyTo');

            // events for applyTo selector
            selector.on('change', function(evt) {
                jive.selected.form.onBlur(jive.selected.form.prevApplyTo);
                jive.selected.form.applyToChanged(this.value);
                jive.selected.form.prevApplyTo = this.value;
            });

            // events for column advance buttons
            $("#colnext, #colprev").on(jive.clickEventName, function(evt) {
                if(this.className.indexOf('disabled') < 0){
                    var colIdx = jive.selected.ie.config.columnIndex;

                    jive.selected.form.onBlur();

                    if(this.id == 'colnext') {
                        jive.selected.ie = jive.elements[it.getNextVisibleColUuid(colIdx)];
                    } else {
                        jive.selected.ie = jive.elements[it.getPrevVisibleColUuid(colIdx)];
                    }
                    jive.selected.form.columnChanged();
                }
            });

            $("#formatPatternText").on("change", function(evt) {
                var jo, dataType, applyToVal = selector.val();

                if (applyToVal === "detailrows") {
                    dataType = jive.selected.ie.config.dataType;
                } else {
                    dataType = it.getGroupMetadata(applyToVal).dataType;
                }

                if ("Numeric" === dataType) {
                    jo = jive.selected.form.jo.find('table:eq(2)');
                    if (DURATION_PATTERN === this.value) {
                        $(this).attr("disabled", true);
                        jo.find('tr:eq(0)').children('td:last').css('visibility','hidden');
                        jo.find('tr:eq(1)').children('td:last').css('visibility','hidden');
                    } else {
                        $(this).attr("disabled", false);
                        jo.find('tr:eq(0)').children('td:last').css('visibility','visible');
                        jo.find('tr:eq(1)').children('td:last').css('visibility','visible');
                    }
                } else {
                    $(this).attr("disabled", false);
                }
            });
        },
        updateApplyToSelector: function(hideHeadings, isConditionalFormatting) {
            // populate applyTo selector
            var it = this,
                options = [],
                finalOptions,
                selector = $('#applyTo'),
                colGroups = jive.interactive.column.allColumnGroups[jive.selected.jo.data('tableuuid')],
                suffix,
                currentColIdx = jive.selected.ie.config.columnIndex,
                groupName,
                groupHeadingOptions = [],
                groupSubTotalsOptions = [],
                totalsOptions = [],
                option;

            if (!hideHeadings && jive.selected.ie.config.headingsTabContent) {
                options.push({value: 'heading', text: jive.i18n.get('column.basicFormatForm.applyto.option.headings')});
            }

            $('option', selector).remove();

            $.each(colGroups, function(i, group) {
                // check if group is for current column
                if ($.inArray(currentColIdx, group.forColumns) != -1) {
                    groupName = group.groupName ? group.groupName: group.id;
                    if (group.groupType === 'groupheading') {
                        option = {
                            value: group.id,
                            text: groupName + ' ' + jive.i18n.get('column.basicFormatForm.groupheading.prefix')
                        };

                        if (isConditionalFormatting) {
                            group.conditionalFormattingData && groupHeadingOptions.push(option);
                        } else {
                            groupHeadingOptions.push(option);
                        }
                    } else if (group.groupType === 'groupsubtotal') {
                        option = {
                            value: group.id,
                            text: groupName + ' ' + jive.i18n.get('column.basicFormatForm.groupsubtotal.prefix')
                        };

                        if (isConditionalFormatting) {
                            group.conditionalFormattingData && groupSubTotalsOptions.push(option);
                        } else {
                            groupSubTotalsOptions.push(option);
                        }
                    } else if (group.groupType === 'tabletotal') {
                        option = {
                            value: group.id,
                            text: jive.i18n.get('column.basicFormatForm.applyto.option.tabletotal')
                        };

                        if (isConditionalFormatting) {
                            group.conditionalFormattingData && totalsOptions.push(option);
                        } else {
                            totalsOptions.push(option);
                        }
                    }
                }
            });

            var extraOptions = [];

            if (!isConditionalFormatting || (isConditionalFormatting && jive.selected.ie.config.conditionalFormattingData)) {
                extraOptions.push({
                    value: 'detailrows',
                    text: jive.i18n.get('column.basicFormatForm.applyto.option.detailrows')
                });
            }

            finalOptions = options.concat(groupHeadingOptions, extraOptions, groupSubTotalsOptions, totalsOptions);

            $.each(finalOptions, function(i, v) {
                selector.append($("<option/>", v));
            });

            if (finalOptions.length) {
                selector.attr("disabled", false);
            } else {
                selector.attr("disabled", true);
            }
        },
        onShow: function() {
            // show applyTo and prev/next col
            var dialog = jive.ui.dialog.jo,
                availableApplyToOptions,
                hasDetailRowsOption;

            dialog.find('.applytoWrapper').show();
            dialog.find('#colprev').show();
            dialog.find('#colnext').show();

            jive.selected.form.jo.parent().css({'overflow-y': 'hidden'});

            this.updateColNavButtons();
            this.updateApplyToSelector();

            availableApplyToOptions = $.map($("#applyTo option") ,function(option) {
                return option.value;
            });
            hasDetailRowsOption = $.grep(availableApplyToOptions, function(optionValue) {
                return optionValue === "detailrows";
            }).length > 0;

            this.prevApplyTo = availableApplyToOptions[0] === "heading" ? "heading" : hasDetailRowsOption ? "detailrows" : availableApplyToOptions[0];

            $("#applyTo").val(this.prevApplyTo);
            this.applyToChanged(this.prevApplyTo);

        },
        columnChanged: function() {
            var existingApplyToText = $('#applyTo :selected').text(),
                newOption,
                availableApplyToOptions,
                hasDetailRowsOption,
                altOption,
                label = jive.selected.ie.config.columnLabel;

            !label.length && (label = "#" + (jive.selected.ie.config.columnIndex + 1));

            // update dialog column name
            jive.ui.dialog.title.html(jive.i18n.get('column.format.dialog.title') + ': ' + label);
            this.updateColNavButtons();
            this.updateApplyToSelector();

            newOption = $('#applyTo').find('option:contains(' + existingApplyToText + ')');
            if (newOption.length > 0) {
                newOption.attr('selected', true);
                this.applyToChanged(newOption.val());
            } else {
                availableApplyToOptions = $.map($("#applyTo option") ,function(option) {
                    return option.value;
                });

                hasDetailRowsOption = $.grep(availableApplyToOptions, function(optionValue) {
                    return optionValue === "detailrows";
                }).length > 0;

                altOption = availableApplyToOptions[0] === "heading" ? "heading" : hasDetailRowsOption ? "detailrows" : availableApplyToOptions[0];

                $("#applyTo").val(altOption);
                this.applyToChanged(altOption);
            }
        },
        applyToChanged: function(val) {
            var it = this,
                tables = jive.selected.form.jo.find('table'),
                value = val.substring(0, val.indexOf('_') != -1 ? val.indexOf("_"): val.length);
            switch(value) {
                case "heading":
                    tables.eq(2).hide();
                    tables.eq(0).show();
                    break;
                case "groupheading":
                    tables.eq(2).hide();
                    tables.eq(0).hide();
                    break;
                case "detailrows":
                    tables.eq(2).show();
                    tables.eq(0).hide();
                    break;
                case "tabletotal":
                case "groupsubtotal":
                    tables.eq(2).show();
                    tables.eq(0).hide();
                    break;
            }
            it.onGenericShow(val);
        },
        updateColNavButtons: function() {
            var colIdx = jive.selected.ie.config.columnIndex,
                btnColNext = $("#colnext"),
                btnColPrev = $("#colprev");

            if (this.getNextVisibleColUuid(colIdx)) {
                btnColNext.removeClass('disabled');
            } else {
                btnColNext.addClass('disabled');
            }

            if (this.getPrevVisibleColUuid(colIdx)) {
                btnColPrev.removeClass('disabled');
            } else {
                btnColPrev.addClass('disabled');
            }
        },
        getNextVisibleColUuid: function(colIdx) {
            var allColumns = jive.interactive.column.allColumns[jive.selected.ie.config.parentId], i;
            for (i = colIdx + 1; allColumns[i]; i++) {
                if (allColumns[i].visible) {
                    return allColumns[i].uuid;
                }
            }
            return false;
        },
        getPrevVisibleColUuid: function(colIdx) {
            var allColumns = jive.interactive.column.allColumns[jive.selected.ie.config.parentId], i;
            for (i = colIdx -1; i >= 0; i--) {
                if (allColumns[i].visible) {
                    return allColumns[i].uuid;
                }
            }
            return false;
        },
        getCacheKey: function(prevApplyTo) {
            var applyTo = prevApplyTo || $('#applyTo').val();

            if (applyTo === 'heading' || applyTo === 'detailrows') {
                return this.name + "_" + jive.selected.ie.config.columnIndex + "_" + applyTo;
            } else {
                return this.name + "_" + applyTo;
            }
        },
        onGenericShow: function(applyToVal) {
            var metadata,
                inputs = jive.selected.form.inputs,
                isFromCache = false,
                jo,
                dataType,
                formatPattern,
                htm = [];

            if (this.actionDataCache[this.getCacheKey()]) {
                metadata = this.actionDataCache[this.getCacheKey()].editTextElementData;
                isFromCache = true;
            } else {
                if (applyToVal === 'heading') {
                    metadata = jive.selected.ie.config.headingsTabContent;
                } else if (applyToVal === 'detailrows') {
                    metadata = jive.selected.ie.config.valuesTabContent;
                } else {
                    metadata = this.getGroupMetadata(applyToVal);
                }
            }

            inputs['fontBold'].set(metadata.fontBold);
            inputs['fontItalic'].set(metadata.fontItalic);
            inputs['fontUnderline'].set(metadata.fontUnderline);

            inputs['fontAlign'].set(metadata.fontHAlign);
            if (!isFromCache) {
                if (applyToVal === 'heading') {
                    inputs['headingName'].set(jive.decodeHTML(metadata.headingName));
                }
                inputs['fontName'].set(jive.decodeHTML(metadata.fontName));
            } else {
                if (applyToVal === 'heading') {
                    inputs['headingName'].set(metadata.headingName);
                }
                inputs['fontName'].set(metadata.fontName);
            }
            inputs['fontSize'].set(metadata.fontSize);
            inputs['fontColor'].set(metadata.fontColor);
            inputs['fontBackColor'].set(metadata.fontBackColor, metadata.mode);

            if (applyToVal !== 'heading') {
                jo = jive.selected.form.jo.find('table:eq(2)');

                if (applyToVal === 'detailrows') {
                    dataType = jive.selected.ie.config.dataType.toLowerCase();
                } else if (metadata.dataType) {
                    dataType = metadata.dataType.toLowerCase();
                }

                if(dataType && (dataType == 'numeric' || dataType == 'date' || dataType == 'time')) {
                    $.each(jive.interactive.column.patterns[dataType],function(i,o){
                        o && htm.push('<option value="'+o.key+'">'+o.val+'</option>');
                    });
                    $('#formatPattern').html(htm.join(''));

                    if (!isFromCache) {
                        formatPattern = jive.decodeHTML(metadata.formatPattern);
                    } else {
                        formatPattern = metadata.formatPattern;
                    }

                    inputs['formatPattern'].set(formatPattern);

                    jo.find('tr').show();
                    if (dataType == 'numeric') {
                        if (DURATION_PATTERN === formatPattern) {
                            jo.find('tr:eq(0)').children('td:last').css('visibility','hidden');
                            jo.find('tr:eq(1)').children('td:last').css('visibility','hidden');
                        } else {
                            jo.find('tr:eq(0)').children('td:last').css('visibility','visible');
                            jo.find('tr:eq(1)').children('td:last').css('visibility','visible');
                            inputs['percentageBtn'].set(false);
                            inputs['commaBtn'].set(false);
                        }
                    } else {
                        jo.find('tr:eq(0)').children('td:last').css('visibility','hidden');
                        jo.find('tr:eq(1)').children('td:last').css('visibility','hidden');
                    }
                } else {
                    jo.find('tr').hide();
                }
            }
        },
        getGroupMetadata: function(groupId) {
            var groupData = {};
            $.each(jive.interactive.column.allColumnGroups[jive.selected.ie.config.parentId], function(i, group) {
                if (group.id === groupId) {
                    $.extend(groupData, group.groupData);
                    group.groupName && (groupData.groupName = group.groupName);
                    group.dataType && (groupData.dataType = group.dataType);
                    return false; // break each
                }
            });

            return groupData;
        },
        submit:function(){
            var actions = [],
                prop;
            this.actionDataCache[this.getCacheKey()] = this.getActionData();

            for (prop in this.actionDataCache) {
                if (this.actionDataCache.hasOwnProperty(prop)) {
                    actions.push(this.actionDataCache[prop]);
                }
            }
            jive.hide();
            jive.selected.ie.format(actions);
        },
        onBlur: function(prevApplyTo) {
            this.actionDataCache[this.getCacheKey(prevApplyTo)] = this.getActionData(prevApplyTo);
        },
        onHide: function() {
            this.actionDataCache = {};
        },
        getActionData: function(prevApplyTo) {
            var inputs = jive.selected.form.inputs,
                currentApplyTo = prevApplyTo || $('#applyTo').val(),
                val = currentApplyTo.substring(0, currentApplyTo.indexOf('_') != -1 ? currentApplyTo.indexOf("_"): currentApplyTo.length),
                result,
                metadata = this.getGroupMetadata(currentApplyTo);

            result = {
                actionName: 'editTextElement',
                editTextElementData:{
                    applyTo: val,
                    tableUuid: jive.selected.ie.config.parentId,
                    columnIndex: jive.selected.ie.config.columnIndex,
                    fontName: jive.escapeFontName(inputs['fontName'].get()),
                    fontSize: inputs['fontSize'].get(),
                    fontBold: inputs['fontBold'].get(),
                    fontItalic: inputs['fontItalic'].get(),
                    fontUnderline: inputs['fontUnderline'].get(),
                    fontHAlign: inputs['fontAlign'].get(),
                    fontColor: inputs['fontColor'].get(),
                    fontBackColor: inputs['fontBackColor'].getBackColor(),
                    mode: inputs['fontBackColor'].getModeValue(),
                    dataType: metadata && metadata.dataType
                }
            };

            switch(val) {
                case 'heading':
                    result.editTextElementData.headingName = inputs['headingName'].get();
                    break;
                case 'tabletotal':
                case 'detailrows':
                    result.editTextElementData.formatPattern = inputs['formatPattern'].get();
                    break;
                case 'groupheading':
                    result.editTextElementData.groupName = metadata.groupName || null;
                    break;
                case 'groupsubtotal':
                    result.editTextElementData.formatPattern = inputs['formatPattern'].get();
                    result.editTextElementData.groupName = metadata.groupName || null;
                    break;
            }

            return result;
        }
    };

    jive.interactive.column.columnConditionalFormattingForm = {
        actionDataCache: {},
        name: 'columnConditionalFormatting',
        title: jive.i18n.get('column.conditionalFormatting.title'),
        method: 'get',
        options: null,
        conditionType: null,
        prevApplyTo: null,
        templateElements: [
            {type:'label', value:''},
            {type:'list', id:'conditionTypeOperator', values:[]},
            {type:'text', id:'conditionStart', value:'', wrapClass: 't_wrap'},
            {type:'label', value:'and', wrapClass: 't_wrap', tdClass: 'condition_between_start'},
            {type:'text', id:'conditionEnd', value:'', wrapClass: 't_wrap'},
            {
                type: 'buttons',
                items: [
                    {type:'checkbox',id:'conditionFontBold',value:'bold',bIcon:'boldIcon', isTripleState: true},
                    {type:'checkbox',id:'conditionFontItalic',value:'italic',bIcon:'italicIcon', isTripleState: true},
                    {type:'checkbox',id:'conditionFontUnderline',value:'underline',bIcon:'underlineIcon', isTripleState: true},
                    {type:'backcolor',id:'conditionFontBackColor',bIcon:'backgroundColorIcon',title:jive.i18n.get('column.formatforms.fontBackColor.title'), drop: true, showTransparent: true, showReset: true, styleClass: 'wide'},
                    {type:'color',id:'conditionFontColor',bIcon:'fontColorIcon',title:jive.i18n.get('column.formatforms.fontColor.title'), drop: true, showReset: true}
                ]
            },
            {
                type: 'buttons',
                items: [
                    {type:'action', id:'conditionMoveUp', fn: 'conditionMoveUp', bIcon: 'moveUpIcon', btnClass: 'plain'},
                    {type:'action', id:'conditionMoveDown', fn: 'conditionMoveDown', bIcon: 'moveDownIcon', btnClass: 'plain'}
                ]
            },
            {type:'button', id:'conditionRemove', bIcon:'deleteIcon', fn: 'removeFormatCondition', btnClass: 'plain', tdClass: 'last'}
        ],
        elements: [
            [
                [
                    {type:'label', value: jive.i18n.get('column.conditionalFormatting.conditions.list.title'), align: 'left', wrapClass: 'wrapper label'}
                ]
            ],
            [
                [
                    {type:'label', value:'#', isHeader: true, tdWidth: '3%', align: 'center'},
                    {type:'label', value: jive.i18n.get('column.conditionalFormatting.condition.operator'), isHeader: true, tdWidth: '26%', align: 'center'},
                    {type:'label', value: jive.i18n.get('column.conditionalFormatting.condition.condition'), colspan: 3, isHeader: true, tdWidth: '30%', align: 'center'},
                    {type:'label', value: jive.i18n.get('column.conditionalFormatting.condition.format'), isHeader: true, tdWidth: '26%', align: 'center'},
                    {type:'label', isHeader: true, tdWidth: '10%', align: 'center'},
                    {type:'label', isHeader: true, tdClass: 'last', tdWidth: '5%', align: 'center'}
                ],
                [
                    {type:'label'},
                    {type:'button', id:'conditionAdd', bLabel: jive.i18n.get('column.conditionalFormatting.condition.add'), fn: 'addFormatCondition', btnClass: 'plain', nowrap: true},
                    {type:'label', nowrap: true, colspan: 3},
                    {type:'label', nowrap: true},
                    {type:'label', nowrap: true},
                    {type:'label', nowrap: true, tdClass: 'last'}
                ]
            ]
        ],
        onCreate: function(jo){
            var it = this,
                form = jo;

            form.on('change', 'select[name=conditionTypeOperator]', function(evt) {
                var self = $(this),
                    row = self.closest('tr'),
                    conditionEnd = row.find('input[name^=conditionEnd]'),
                    conditionStart = row.find('input[name^=conditionStart]');

                if (self.val().indexOf('BETWEEN') >= 0) {
                    conditionEnd.closest('td').addClass('condition_between_end').show().prev().show();
                    conditionEnd.prop('disabled', false);
                    conditionStart.closest('td').attr('colspan', 1).addClass('condition_between_start');
                } else {
                    conditionEnd.closest('td').removeClass('condition_between_end').hide().prev().hide();
                    conditionEnd.prop('disabled', true);
                    conditionStart.closest('td').attr('colspan', 3).removeClass('condition_between_start');
                }
            });

            form.on('rowchange', 'table', function(evt) {
                $(this).find('tr.jive_condition').each(function(i, v) {
                    $(this).find('.jive_textLabel:first div:first').text(i+1);
                });
            });

            form.find('table:eq(1)').addClass('conditionList').find('tr:last').addClass('add');
        },
        columnChanged: function() {
            var existingApplyToText = $('#applyTo :selected').text(),
                newOption,
                availableApplyToOptions,
                hasDetailRowsOption,
                altOption,
                label = jive.selected.ie.config.columnLabel;

            !label.length && (label = "#" + (jive.selected.ie.config.columnIndex + 1));

            // update dialog column name
            jive.ui.dialog.title.html(jive.i18n.get('column.format.dialog.title') + ': ' + label);
            jive.interactive.column.basicFormatForm.updateColNavButtons();
            jive.interactive.column.basicFormatForm.updateApplyToSelector(true, true);

            newOption = $('#applyTo').find('option:contains(' + existingApplyToText + ')');
            if (newOption.length > 0) {
                newOption.attr('selected', true);
                this.applyToChanged(newOption.val());
            } else {
                availableApplyToOptions = $.map($("#applyTo option") ,function(option) {
                    return option.value;
                });

                hasDetailRowsOption = $.grep(availableApplyToOptions, function(optionValue) {
                        return optionValue === "detailrows";
                    }).length > 0;

                altOption = hasDetailRowsOption ? "detailrows" : availableApplyToOptions[0];

                $("#applyTo").val(altOption || "");
                this.applyToChanged(altOption || "");
            }
        },
        applyToChanged: function(val) {
            this.onGenericShow(val);
        },
        onShow: function() {
            jive.selected.form.jo.parent().css({'overflow-y': 'hidden'});

            jive.interactive.column.basicFormatForm.updateColNavButtons();
            jive.interactive.column.basicFormatForm.updateApplyToSelector(true, true);
            this.prevApplyTo = 'detailrows';
            $('#applyTo').val('detailrows');
            this.onGenericShow('detailrows');
        },
        getGroupMetadata: function(groupId) {
            var groupData = {};
            $.each(jive.interactive.column.allColumnGroups[jive.selected.ie.config.parentId], function(i, group) {
                if (group.id === groupId) {
                    $.extend(groupData, group.conditionalFormattingData);
                    group.groupName && (groupData.groupName = group.groupName);
                    return false; // break each
                }
            });

            return groupData;
        },
        onGenericShow:function(applyToVal){
            var it = this,
                table = jive.selected.form.jo.find('table:eq(1)'),
                isFromCache = false,
                addButton = jive.selected.form.jo.find('div.jive_inputbutton[bname=conditionAdd]'),
                metadata;

            jive.selected.form.jo.parent().css({'overflow-y': 'auto'});

            if (this.actionDataCache[this.getCacheKey()]) {
                metadata = this.actionDataCache[this.getCacheKey()].conditionalFormattingData;
                isFromCache = true;
            } else {
                if (applyToVal.indexOf('group') != -1 || applyToVal.indexOf('tabletotal') != -1) {
                    metadata = this.getGroupMetadata(applyToVal);
                } else if (applyToVal == 'detailrows') {
                    metadata = jive.selected.ie.config.conditionalFormattingData;
                }
            }

            if (metadata) {
                it.conditionType = metadata.conditionType.toLowerCase();
                it.options = jive.interactive.column.operators[it.conditionType];

                // enable Add button
                addButton.removeClass('disabled');
                /**
                 * for boolean fields, hide the condition column
                 */
                if (it.conditionType === 'boolean') {
                    table.find('th:eq(2), tr.add td:eq(2)').hide();
                } else {
                    table.find('th:eq(2), tr.add td:eq(2)').show();
                }

                $.each(metadata.conditions, function(i,v) {
                    it.addFormatCondition(jive.selected.form.jo, v, isFromCache);
                });

            } else {
                // clear conditions table, disable Add button
                jive.selected.form.jo.find('table:eq(1) tr.jive_condition').each(function() {it.removeRow($(this));});
                addButton.addClass('disabled');

            }
        },
        onBlur: function(prevApplyTo) {
            var it = this,
                currentApplyTo = prevApplyTo || $('#applyTo').val(),
                cacheKey;
            // do not cache actionData for headings
            if (currentApplyTo !== 'heading') {
                cacheKey = this.getCacheKey(currentApplyTo);
                if (cacheKey) {
                    this.actionDataCache[cacheKey] = this.getActionData(currentApplyTo);
                }
            }
            jive.selected.form.jo.find('table:eq(1) tr.jive_condition').each(function() {it.removeRow($(this));});
        },
        onHide: function() {
            var it = this;
            this.actionDataCache = {};
            jive.selected.form.jo.find('table:eq(1) tr.jive_condition').each(function() {it.removeRow($(this));});
        },
        submit:function(){
            var actions = [],
                prop;
            this.actionDataCache[this.name] = this.getActionData();

            for (prop in this.actionDataCache) {
                if (this.actionDataCache.hasOwnProperty(prop)) {
                    actions.push(this.actionDataCache[prop]);
                }
            }

            jive.hide();

            jive.selected.ie.format(actions);
        },
        addFormatCondition: function(jo, conditionData, isFromCache) {
            if (!jo.is('.disabled')) {
                var it = this,
                    conditionType = it.conditionType,
                    calendarPattern = jive.interactive.column.calendarPatterns["date"],
                    calendarTimePattern = jive.interactive.column.calendarPatterns["time"],
                    form = jo.closest('form'),
                    table = form.find('table:eq(1)'),
                    tr = [],
                    row,
                    inputs = jive.selected.form.inputs,
                    htm = [];

                tr.push('<tr class="jive_condition">');
                $.each(this.templateElements, function(i,e) {
                    jive.ui.forms.createTemplateElement(e, it, form, tr);
                });
                tr.push('</tr>');
                row = $(tr.join(''));
                row.insertBefore(table.find('tr:last'));

                $.each(it.options, function(k,v) {
                    v && htm.push('<option value="'+v.key+'">'+v.val+'</option>');
                });

                row.find('select[name=conditionTypeOperator]').append(htm.join('')).trigger('change');

                if(conditionType === 'date') {
                    var pickerOptions = {
                        changeMonth: true,
                        changeYear: true,
                        dateFormat: calendarPattern,
                        timeFormat: calendarTimePattern,
                        showSecond: true
                    }
                    row.find('input[name=conditionStart]').datetimepicker(pickerOptions);
                    row.find('input[name=conditionEnd]').datetimepicker(pickerOptions);
                } else if (conditionType === 'time') {
                    var timePickerOptions = {
                        timeFormat: calendarTimePattern,
                        showSecond:true,
                        constrainInput:false
                    }
                    row.find('input[name=conditionStart]').timepicker(timePickerOptions);
                    row.find('input[name=conditionEnd]').timepicker(timePickerOptions);
                } else if (conditionType === 'boolean') {
                    row.find('input[name=conditionStart]').prop('disabled', true);
                    row.find('input[name=conditionStart]').closest('td').hide();
                }

                if (conditionData) {
                    row.find('select[name=conditionTypeOperator]').val(conditionData.conditionTypeOperator).trigger('change');
                    inputs[row.find('input[name=conditionEnd]').attr('id')].set(conditionData.conditionEnd);

                    if (conditionType === 'text' && isFromCache) {
                        inputs[row.find('input[name=conditionStart]').attr('id')].set(conditionData.conditionStart);
                    } else {
                        inputs[row.find('input[name=conditionStart]').attr('id')].set(jive.decodeHTML(conditionData.conditionStart));
                    }

                    inputs[row.find('.jive_inputbutton[bname^=conditionFontBold]').attr('bname')].set(conditionData.conditionFontBold);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontItalic]').attr('bname')].set(conditionData.conditionFontItalic);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontUnderline]').attr('bname')].set(conditionData.conditionFontUnderline);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontColor]').attr('bname')].set(conditionData.conditionFontColor);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontBackColor]').attr('bname')].set(conditionData.conditionFontBackColor, conditionData.conditionMode);
                } else {
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontBold]').attr('bname')].set(null);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontItalic]').attr('bname')].set(null);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontUnderline]').attr('bname')].set(null);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontColor]').attr('bname')].set(null);
                    inputs[row.find('.jive_inputbutton[bname^=conditionFontBackColor]').attr('bname')].set(null, null);
                }

                table.trigger('rowchange');
            }
        },
        removeFormatCondition: function(jo) {
            var row = jo.closest('tr'),
                table = row.closest('table');

            this.removeRow(row);

            table.trigger('rowchange');
        },
        removeRow: function(row) {
            var inputs = jive.selected.form.inputs;
            row.find('[name^=condition]').each(function() {delete inputs[$(this).attr('id')];});
            row.find('.jive_inputbutton[bname^=conditionFont]').each(function() {delete inputs[$(this).attr('bname')];});
            row.remove();
        },
        conditionMoveUp: function(jo) {
            var row = jo.closest('tr'),
                prev = row.prev(),
                table = row.closest('table');

            if (table.find('tr').index(row) > 1) {
                row.insertBefore(prev);
                table.trigger('rowchange');
            }
        },
        conditionMoveDown: function(jo) {
            var row = jo.closest('tr'),
                next = row.next(),
                table = row.closest('table'),
                rows = table.find('tr');

            if (next.size() > 0 && rows.index(row) < (rows.size() - 2)) {
                row.insertAfter(next);
                table.trigger('rowchange');
            }

        },
        getCacheKey: function(prevApplyTo) {
            var applyTo = prevApplyTo || $('#applyTo').val();

            if (applyTo === 'heading') {
                return null;
            }

            if (applyTo === 'detailrows' && jive.selected.ie.config.conditionalFormattingData) {
                return this.name + "_" + jive.selected.ie.config.columnIndex + "_" + applyTo;
            } else if (this.getGroupMetadata(applyTo) && this.getGroupMetadata(applyTo).conditionalFormattingData) {
                return this.name + "_" + applyTo;
            }

            return null;
        },
        getActionData: function(prevApplyTo) {
            var currentApplyTo = prevApplyTo || $('#applyTo').val(),
                val = currentApplyTo.substring(0, currentApplyTo.indexOf('_') != -1 ? currentApplyTo.indexOf("_"): currentApplyTo.length),
                inputs = jive.selected.form.inputs,
                metadata, actionData;

            if (currentApplyTo.indexOf('group') != -1) {
                metadata = this.getGroupMetadata(currentApplyTo);
                actionData = {
                    actionName: 'conditionalFormatting',
                    conditionalFormattingData: {
                        applyTo: val,
                        tableUuid: jive.selected.ie.config.parentId,
                        columnIndex: jive.selected.ie.config.columnIndex,
                        conditionPattern: metadata.conditionPattern,
                        conditionType: metadata.conditionType,
                        conditions: [],
                        groupName: metadata.groupName
                    }
                };
            } else if (currentApplyTo == 'detailrows' || val === 'tabletotal') {
                if (val === 'tabletotal') {
                    metadata = this.getGroupMetadata(currentApplyTo);
                } else {
                    metadata = jive.selected.ie.config.conditionalFormattingData;
                }
                actionData = {
                    actionName: 'conditionalFormatting',
                    conditionalFormattingData: {
                        applyTo: val,
                        tableUuid: jive.selected.ie.config.parentId,
                        columnIndex: jive.selected.ie.config.columnIndex,
                        conditionPattern: metadata.conditionPattern,
                        conditionType: metadata.conditionType,
                        conditions: []
                    }
                };
            }

            jive.selected.form.jo.find('table:eq(1) tr.jive_condition').each(function(i, v) {
                var row = $(this);
                actionData.conditionalFormattingData.conditions.push({
                    conditionStart: inputs[row.find('input[name=conditionStart]').attr('id')].get(),
                    conditionEnd: inputs[row.find('input[name=conditionEnd]').attr('id')].get(),
                    conditionTypeOperator: inputs[row.find('select[name=conditionTypeOperator]').attr('id')].get(),
                    conditionFontBold: inputs[row.find('.jive_inputbutton[bname^=conditionFontBold]').attr('bname')].get(),
                    conditionFontItalic: inputs[row.find('.jive_inputbutton[bname^=conditionFontItalic]').attr('bname')].get(),
                    conditionFontUnderline: inputs[row.find('.jive_inputbutton[bname^=conditionFontUnderline]').attr('bname')].get(),
                    conditionFontColor: inputs[row.find('.jive_inputbutton[bname^=conditionFontColor]').attr('bname')].get(),
                    conditionFontBackColor: inputs[row.find('.jive_inputbutton[bname^=conditionFontBackColor]').attr('bname')].getBackColor(),
                    conditionMode: inputs[row.find('.jive_inputbutton[bname^=conditionFontBackColor]').attr('bname')].getModeValue()
                });
            });

            return actionData;
        }
    };

    return jive.interactive.column;
});

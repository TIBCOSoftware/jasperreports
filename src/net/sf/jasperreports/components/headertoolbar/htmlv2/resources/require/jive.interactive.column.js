define(["jquery.ui-1.10.3", "jive"], function($, jive) {
    var EventManager = null;

    jive.interactive.column = {
        uuid: null,
        allColumns: {},
        allColumnsMap: {},
        columnsData: {},
        count: 0,
        ids: {},
        fontSizes:null,
        fonts: {
            extension: null,
            system: null
        },
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

        init: function(report) {
            var it = this;

            EventManager = report.eventManager;

            jive.init(report);

            it.setDynamicProperties();

            $.each(report.components.table, function() {
                it.initColumns(this);
            });
        },

        initColumns: function(Table){
            var c,i,it = this, tableCols = [], colData, prop;
            var tableUuid = Table.config.id;
            var allColumns = Table.config.allColumnsData;

            it.allColumns[tableUuid] = allColumns;
            it.allColumnsMap[tableUuid] = {};

            /*
             * Load dynamic form data
             */
            it.formatHeaderForm.elements[0][1][0].groups = [{name: jive.i18n.get('column.dialog.extfonts'),values:[]}];
            it.formatCellsForm.elements[0][0][0].groups = [{name: jive.i18n.get('column.dialog.extfonts'),values:[]}];
            $.each(it.fonts.extension,function(i,v) {
                it.formatHeaderForm.elements[0][1][0].groups[0].values.push([v,v]);
                it.formatCellsForm.elements[0][0][0].groups[0].values.push([v,v]);
            });
            it.formatHeaderForm.elements[0][1][0].groups.push({name: jive.i18n.get('column.dialog.sysfonts'),values:[]});
            it.formatCellsForm.elements[0][0][0].groups.push({name: jive.i18n.get('column.dialog.sysfonts'),values:[]});
            $.each(it.fonts.system,function(i,v) {
                it.formatHeaderForm.elements[0][1][0].groups[1].values.push([v,v]);
                it.formatCellsForm.elements[0][0][0].groups[1].values.push([v,v]);
            });

            it.formatHeaderForm.elements[0][1][1].values = [];
            it.formatCellsForm.elements[0][0][1].values = [];
            $.each(it.fontSizes,function(i,v) {
                it.formatHeaderForm.elements[0][1][1].values.push([v,v]);
                it.formatCellsForm.elements[0][0][1].values.push([v,v]);
            });

            /*
             * Compute drop boundaries (x-axis only) for DnD visual feedback.
             */
            it.dropPoints[tableUuid] = [];
            it.visibleColumnsMoveData[tableUuid] = [];
            it.dropColumns[tableUuid] = [];

            var firstColumnHeader = $("table.jrPage").find('td.jrcolHeader[data-tableuuid=' + tableUuid + ']:first');
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
                    cols, firstCol, lastCol;
                if (colsData[colUuid]) continue;

                cols = parentContainer.find('td.jrcolHeader[data-coluuid=' + colUuid + ']');
                firstCol = cols.eq(0);
                if (cols.size() > 0) {
                    lastCol = cols.eq(cols.size()-1);
                } else {
                    lastCol = firstCol;
                }

                colsData[colUuid] = {
                    jo: $(firstCol),
                    width: lastCol.position().left - firstCol.position().left + lastCol.width(),
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
                    menu.actions[c.uuid] = {label: c.label, fn:'hide', arg:'{"hide":false, "columnUuid": "' + c.uuid + '"}'};
                }
                it.allColumnsMap[tableUuid][c.uuid] = c;
            }
            it.count = it.dropColumns[tableUuid].length;

            jive.ui.foobar.reset();
            jive.ui.forms.add(jive.interactive.column.columnFilterForm);
            jive.ui.forms.add(jive.interactive.column.formatHeaderForm);
            jive.ui.forms.add(jive.interactive.column.formatCellsForm);
            jive.ui.forms.add(jive.interactive.column.columnConditionalFormattingForm);

            EventManager.registerEvent('jive.interactive.column.init').trigger();

            $.each(Table.columns, function(k, thisColumn){
                thisColumn && jive.initInteractiveElement(thisColumn);
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
        getElementSize: function(){
            var jo = jive.selected.jo,
                cid = jo.data('cellid'),
                h = null;

            cid = ('' + cid).replace(/\./g,'\\.');

            jo.parents().each(function(i, v) {
                var lastCell = $('td.cel_' + cid + ':last', v);
                if(lastCell && lastCell.length > 0) {
                    var lastElemTop = lastCell.offset().top;
                    var lastElemHeight = lastCell.outerHeight();
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
            jive.ui.dialog.show(jive.i18n.get('column.filter.dialog.title') + ': ' + jive.selected.ie.config.columnLabel, ['columnfilter']);
        },
        formatHeader: function(){
            jive.ui.dialog.show(jive.i18n.get('column.format.dialog.title') + ': ' + jive.selected.ie.config.columnLabel, ['formatHeader', 'formatCells', 'columnConditionalFormatting']);
        },
        hide: function(args){
            jive.hide();
            if(args.hide) {
                jive.selected.ie.hide();
            } else {
                var columnIndices;
                if(args.columnUuid) {
                    var allColumnsMap = this.allColumnsMap[jive.elements[args.columnUuid].config.tableId];
                    columnIndices = [allColumnsMap[args.columnUuid].index];
                } else {
                    columnIndices = args.column;
                }
                jive.selected.ie.unhide(columnIndices);
            }
        },
        setDynamicProperties: function (obj) {
            var obj = {
                fontSizes: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],
                fonts: {
                    extension: [
                        "DejaVu Sans" ,
                        "DejaVu Sans Mono" ,
                        "DejaVu Serif" ,
                        "Monospaced" ,
                        "SansSerif" ,
                        "Serif"
                    ],
                    system: [
                        "Bitstream Charter" ,
                        "Bitstream Vera Sans" ,
                        "Bitstream Vera Sans Mono" ,
                        "Bitstream Vera Serif" ,
                        "Century Schoolbook L" ,
                        "Courier" ,
                        "Courier 10 Pitch" ,
                        "Cursor" ,
                        "DejaVu LGC Sans" ,
                        "DejaVu LGC Sans Condensed" ,
                        "DejaVu LGC Sans Light" ,
                        "DejaVu LGC Sans Mono" ,
                        "DejaVu LGC Serif" ,
                        "DejaVu LGC Serif Condensed" ,
                        "Dialog" ,
                        "DialogInput" ,
                        "Dingbats" ,
                        "Hershey" ,
                        "Liberation Mono" ,
                        "Liberation Sans" ,
                        "Liberation Serif" ,
                        "Lucida Bright" ,
                        "Lucida Sans" ,
                        "Lucida Sans Typewriter" ,
                        "Luxi Mono" ,
                        "Luxi Sans" ,
                        "Luxi Serif" ,
                        "Nimbus Mono L" ,
                        "Nimbus Roman No9 L" ,
                        "Nimbus Sans L" ,
                        "Nimbus Sans L Condensed" ,
                        "Standard Symbols L" ,
                        "URW Bookman L" ,
                        "URW Chancery L" ,
                        "URW Gothic L" ,
                        "URW Palladio L" ,
                        "Utopia"
                    ]
                }
            }
            jive.interactive.column.fontSizes = obj.fontSizes;
            jive.interactive.column.fonts = obj.fonts;
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
            });

            input.hideOptions();
        },
        togglePercentageFormat: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);
                opt.text(it.numberFormat.addRemovePercentageForNumber(opt.text(), jive.selected.form.inputs['percentageBtn'].value));
                opt.val(it.numberFormat.addRemovePercentage(opt.val(), jive.selected.form.inputs['percentageBtn'].value));
            });
        },
        toggleCommaFormat: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);
                opt.text(it.numberFormat.addRemoveThousandsSeparator(opt.text(), jive.selected.form.inputs['commaBtn'].value));
                opt.val(it.numberFormat.addRemoveThousandsSeparator(opt.val(), jive.selected.form.inputs['commaBtn'].value));
            });
        },
        addDecimal: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);
                opt.text(it.numberFormat.addRemoveDecimalPlace(opt.text(), true));
                opt.val(it.numberFormat.addRemoveDecimalPlace(opt.val(), true));
            });
        },
        remDecimal: function(){
            var it = this;
            $('#formatPattern').children().each(function (i, optElem) {
                var opt = $(optElem);
                opt.text(it.numberFormat.addRemoveDecimalPlace(opt.text(), false));
                opt.val(it.numberFormat.addRemoveDecimalPlace(opt.val(), false));
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
        datePickerFormats: {
            "yyyy-MM-dd": {dateFormat: "yy-mm-dd"},
            "dd/MM/yyyy": {dateFormat: "dd/mm/yy"},
            "MM/dd/yyyy": {dateFormat: "mm/dd/yy"},
            "yyyy/MM/dd": {dateFormat: "yy/mm/dd"},
            "EEEEE dd MMMMM yyyy": {dateFormat: "DD dd MM yy"},
            "MMMMM dd. yyyy": {dateFormat: "MM dd. yy"},
            "dd/MM": {dateFormat: "dd/mm"},
            "dd/MM/yy": {dateFormat: "dd/mm/y"},
            "dd-MMM": {dateFormat: "dd-M"},
            "dd-MMM-yy": {dateFormat: "dd-M-y"},
            "MMM-yy": {dateFormat: "M-y"},
            "MMMMM-yy": {dateFormat: "MM-y"},
            "dd/MM/yyyy h.mm a": {dateFormat: "dd/mm/yy", timeFormat: "h.mm TT", ampm: true},
            "dd/MM/yyyy HH.mm.ss": {dateFormat: "dd/mm/yy", timeFormat: "hh.mm.ss"},
            "MMM": {dateFormat: "M"},
            "d/M/yyyy": {dateFormat: "d/m/yy"},
            "dd-MMM-yyyy": {dateFormat: "dd-M-yy"},
            "yyyy.MM.dd G 'at' HH:mm:ss z": {dateFormat: "yy.mm.dd", timeFormat:"hh:mm:ss", separator: 'at'},
            "EEE. MMM d. ''yy": {dateFormat: "D. M d. 'y"},
            "yyyy.MMMMM.dd GGG hh:mm aaa": {dateFormat: "yy.MM.dd AD", timeFormat:"hh:mm TT", ampm: true},
            "EEE. d MMM yyyy HH:mm:ss Z": {dateFormat: "D. d M yy", timeFormat:"hh:mm:ss z"},
            "yyMMddHHmmssZ": {dateFormat: "ymmdd", timeFormat:"hhmmssz"}
        },
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
            var it = this;
            var metadata = jive.selected.ie.config.filtering.filterData;
            var filtertype = metadata.filterType.toLowerCase();
            var options = jive.selected.ie.config.filtering.filterOperatorTypeValueSelector || {
                text : [
                    {key:'EQUALS',val:'Equals'},
                    {key:'IS_NOT_EQUAL_TO',val:'Is not equal to'},
                    {key:'CONTAINS',val:'Contains'},
                    {key:'DOES_NOT_CONTAIN',val:'Does not contain'},
                    {key:'STARTS_WITH',val:'Starts with'},
                    {key:'DOES_NOT_START_WITH',val:'Does not start with'},
                    {key:'ENDS_WITH',val:'Ends with'},
                    {key:'DOES_NOT_END_WITH',val:'Does not end with'}
                ],
                date: [
                    {key:'EQUALS',val:'Equals'},
                    {key:'IS_NOT_EQUAL_TO',val:'Is not equal to'},
                    {key:'IS_BETWEEN',val:'Is between'},
                    {key:'IS_NOT_BETWEEN',val:'Is not between'},
                    {key:'IS_ON_OR_BEFORE',val:'Is on or before'},
                    {key:'IS_BEFORE',val:'Is before'},
                    {key:'IS_ON_OR_AFTER',val:'Is on or after'},
                    {key:'IS_AFTER',val:'Is after'}
                ],
                numeric: [
                    {key:'EQUALS',val:'Equals'},
                    {key:'DOES_NOT_EQUAL',val:'Does not equal'},
                    {key:'GREATER_THAN',val:'Greater than'},
                    {key:'GREATER_THAN_EQUAL_TO',val:'Greater than or equal to'},
                    {key:'LESS_THAN',val:'Less than'},
                    {key:'LESS_THAN_EQUAL_TO',val:'Less than or equal to'},
                    {key:'IS_BETWEEN',val:'Is between'},
                    {key:'IS_NOT_BETWEEN',val:'Is not between'}
                ],
                boolean: [
                    {key:'IS_TRUE', val:'Is true'},
                    {key:'IS_NOT_TRUE', val:'Is not true'},
                    {key:'IS_FALSE', val:'Is false'},
                    {key:'IS_NOT_FALSE', val:'Is not false'}
                ]
            }[filtertype];

            it.jc.filterType.empty();
            var htm = [];
            $.each(options,function(k,v){
                v && htm.push('<option value="'+v.key+'">'+v.val+'</option>');
            });
            it.jc.filterType.append(htm.join(''));
            it.jc.filterType.val(metadata.filterTypeOperator);
            if (filtertype === 'text') {
                it.jc.filterStart.val(jive.decodeHTML(metadata.fieldValueStart));
            } else {
                it.jc.filterStart.val(metadata.fieldValueStart);
            }

            var filterOff = metadata.filterTypeOperator == '' ? true : false;
            $('input[name="clearFilter"][value="'+filterOff+'"]').prop("checked",true);
            for(p in it.jc) (it.jc.hasOwnProperty(p) && filterOff) ? it.jc[p].prop('disabled',true) : it.jc[p].prop('disabled',false);

            if(!filterOff && metadata.filterTypeOperator.indexOf('BETWEEN') >= 0) {
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
                    dateFormat: metadata.calendarPattern,
                    timeFormat: metadata.calendarTimePattern,
                    showSecond: true
                }
                it.jc.filterStart.timepicker('destroy');
                it.jc.filterEnd.timepicker('destroy');
                it.jc.filterStart.datetimepicker(pickerOptions);
                it.jc.filterEnd.datetimepicker(pickerOptions);
            } else if (filtertype === 'time') {
                var timePickerOptions = {
                    timeFormat: metadata.calendarTimePattern,
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
        submit:function(){
            var filterData = {
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

    jive.interactive.column.formatHeaderForm = {
        actionDataCache: {},
        title: jive.i18n.get('column.formatHeaderForm.title'),
        name: 'formatHeader',
        method: 'get',
        elements: [[
            [{type:'text', id:'headingName', label: jive.i18n.get('column.formatHeaderForm.headingName.label'), value:'',colspan:4}],
            [
                {type:'list', id:'headerFontName', label: jive.i18n.get('column.formatforms.fontName.label'), values:[], freeText: true, size:6, rowspan:2},
                {type:'list', id:'headerFontSize', label: jive.i18n.get('column.formatforms.fontSize.label'), values:[], freeText: true, size:6, rowspan:2, restriction: 'numeric'},
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.styleButtons.label'),
                    items: [
                        {type:'checkbox',id:'headerFontBold',value:'bold',bIcon:'boldIcon'},
                        {type:'checkbox',id:'headerFontItalic',value:'italic',bIcon:'italicIcon'},
                        {type:'checkbox',id:'headerFontUnderline',value:'underline',bIcon:'underlineIcon'}
                    ]
                },
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.color.label'),
                    items: [
                        {type:'backcolor',id:'headerFontBackColor',bIcon:'backgroundColorIcon',title:jive.i18n.get('column.formatforms.fontBackColor.title'), drop: true, showTransparent: true, styleClass: 'wide'},
                        {type:'color',id:'headerFontColor',bIcon:'fontColorIcon',title:jive.i18n.get('column.formatforms.fontColor.title'), drop: true}
                    ]
                }
            ],
            [
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.alignment.label'),
                    items: [
                        {type:'radio',id:'headerFontAlign',value:'Left',bIcon:'leftIcon'},
                        {type:'radio',id:'headerFontAlign',value:'Center',bIcon:'centerIcon'},
                        {type:'radio',id:'headerFontAlign',value:'Right',bIcon:'rightIcon'}
                    ]
                }
            ]
        ]],
        onCreate:function(jo){

        },
        onShow:function(){
            var metadata,
                inputs = jive.selected.form.inputs,
                isFromCache = false;

            jive.selected.form.jo.parent().css({'overflow-y': 'hidden'});

            if (this.actionDataCache[this.name]) {
                metadata = this.actionDataCache[this.name].editColumnHeaderData;
                isFromCache = true;
            } else {
                metadata = jive.selected.ie.config.headingsTabContent;
            }

            inputs['headerFontBold'].set(metadata.fontBold);
            inputs['headerFontItalic'].set(metadata.fontItalic);
            inputs['headerFontUnderline'].set(metadata.fontUnderline);

            inputs['headerFontAlign'].set(metadata.fontHAlign);
            if (!isFromCache) {
                inputs['headingName'].set(jive.decodeHTML(metadata.headingName));
                inputs['headerFontName'].set(jive.decodeHTML(metadata.fontName));
            } else {
                inputs['headingName'].set(metadata.headingName);
                inputs['headerFontName'].set(metadata.fontName);
            }
            inputs['headerFontSize'].set(metadata.fontSize);
            inputs['headerFontColor'].set(metadata.fontColor);
            inputs['headerFontBackColor'].set(metadata.fontBackColor, metadata.mode);

            // disable conditional formatting tab
            var conditionalFormattingTab = $('#columnConditionalFormattingTab');
            !jive.selected.ie.config.canFormatConditionally ? conditionalFormattingTab.addClass('disabled') : conditionalFormattingTab.removeClass('disabled');
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
        onBlur: function() {
            this.actionDataCache[this.name] = this.getActionData();
        },
        onHide: function() {
            this.actionDataCache = {};
        },
        getActionData: function() {
            var inputs = jive.selected.form.inputs;
            return {
                actionName: 'editColumnHeader',
                editColumnHeaderData: {
                    tableUuid: jive.selected.jo.data('tableuuid'),
                    columnIndex: jive.selected.ie.config.columnIndex,
                    headingName: inputs['headingName'].get(),
                    fontName: jive.escapeFontName(inputs['headerFontName'].get()),
                    fontSize: inputs['headerFontSize'].get(),
                    fontBold: inputs['headerFontBold'].get(),
                    fontItalic: inputs['headerFontItalic'].get(),
                    fontUnderline: inputs['headerFontUnderline'].get(),
                    fontHAlign: inputs['headerFontAlign'].get(),
                    fontColor: inputs['headerFontColor'].get(),
                    fontBackColor: inputs['headerFontBackColor'].getBackColor(),
                    mode: inputs['headerFontBackColor'].getModeValue()
                }
            }
        }
    };

    jive.interactive.column.formatCellsForm = {
        actionDataCache: {},
        title: jive.i18n.get('column.formatCellsForm.title'),
        name: 'formatCells',
        method: 'get',
        elements: [[
            [
                {type:'list', id:'cellsFontName', label: jive.i18n.get('column.formatforms.fontName.label'), values:[], freeText: true, size:6, rowspan:2},
                {type:'list', id:'cellsFontSize', label: jive.i18n.get('column.formatforms.fontSize.label'), values:[], freeText: true, size:6, rowspan:2, restriction: 'numeric'},
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.styleButtons.label'),
                    items: [
                        {type:'checkbox',id:'cellsFontBold',value:'bold',bIcon:'boldIcon'},
                        {type:'checkbox',id:'cellsFontItalic',value:'italic',bIcon:'italicIcon'},
                        {type:'checkbox',id:'cellsFontUnderline',value:'underline',bIcon:'underlineIcon'}
                    ]
                },
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.color.label'),
                    items: [
                        {type:'backcolor',id:'cellsFontBackColor',bIcon:'backgroundColorIcon',title:jive.i18n.get('column.formatforms.fontBackColor.title'), drop: true, showTransparent: true, styleClass: 'wide'},
                        {type:'color',id:'cellsFontColor',bIcon:'fontColorIcon',title:jive.i18n.get('column.formatforms.fontColor.title'), drop: true}
                    ]
                }
            ],
            [
                {
                    type: 'buttons',
                    label: jive.i18n.get('column.formatforms.alignment.label'),
                    items: [
                        {type:'radio',id:'cellsFontAlign',value:'Left',bIcon:'leftIcon'},
                        {type:'radio',id:'cellsFontAlign',value:'Center',bIcon:'centerIcon'},
                        {type:'radio',id:'cellsFontAlign',value:'Right',bIcon:'rightIcon'}
                    ]
                }
            ],
            [
                {type:'list',id:'formatPattern',label: jive.i18n.get('column.formatCellsForm.formatPattern.label'),freeText:true,values:[],colspan:2, size:4, rowspan:2},
                {
                    type:'buttons',
                    id: 'numberFormatButtons',
                    label: jive.i18n.get('column.formatforms.numberformat.label'),
                    items: [
                        //                  {type:'checkbox',id:'currencyBtn',fn:'toggleCurrencyFormat',value:'',bIcon:'currencyIcon'},
                        {type:'checkbox',id:'percentageBtn',fn:'togglePercentageFormat',value:'',bIcon:'percentageIcon'},
                        {type:'checkbox',id:'commaBtn',fn:'toggleCommaFormat',value:'',bIcon:'commaIcon'},
                        {type:'action',id:'increaseDecimalsBtn',fn:'addDecimal',value:'',bIcon:'increaseDecimalsIcon'},
                        {type:'action',id:'decreaseDecimalsBtn',fn:'remDecimal',value:'',bIcon:'decreaseDecimalsIcon'}
                    ]
                }
            ],
            [
                {
                    type:'buttons',
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
        ]],
        onCreate:function(jo){

        },
        onShow:function(){
            var metadata,
                inputs = jive.selected.form.inputs,
                htm = [],
                ie = jive.selected.ie.config,
                jo = jive.selected.form.jo
            isFromCache = false;

            jive.selected.form.jo.parent().css({'overflow-y': 'hidden'});

            if (this.actionDataCache[this.name]) {
                metadata = this.actionDataCache[this.name].editColumnValueData;
                isFromCache = true;
            } else {
                metadata = jive.selected.ie.config.valuesTabContent;
            }

            inputs['cellsFontBold'].set(metadata.fontBold);
            inputs['cellsFontItalic'].set(metadata.fontItalic);
            inputs['cellsFontUnderline'].set(metadata.fontUnderline);

            inputs['cellsFontAlign'].set(metadata.fontHAlign);
            if (!isFromCache) {
                inputs['cellsFontName'].set(jive.decodeHTML(metadata.fontName));
            } else {
                inputs['cellsFontName'].set(metadata.fontName);
            }
            inputs['cellsFontSize'].set(metadata.fontSize);
            inputs['cellsFontColor'].set(metadata.fontColor);
            inputs['cellsFontBackColor'].set(metadata.fontBackColor, metadata.mode);

            if(typeof ie.formatPatternLabel === 'string') {
                $.each(ie.formatPatternSelector,function(i,o){
                    o && htm.push('<option value="'+o.key+'">'+o.val+'</option>');
                })
                $('#formatPattern').html(htm.join(''));
                if (!isFromCache) {
                    inputs['formatPattern'].set(jive.decodeHTML(metadata.formatPattern));
                } else {
                    inputs['formatPattern'].set(metadata.formatPattern);
                }
                jo.find('tr:gt(1)').show();
                if (ie.formatPatternLabel.indexOf('Number') >= 0) {
                    jo.find('tr:eq(2)').children('td:last').css('visibility','visible');
                    jo.find('tr:eq(3)').children('td:last').css('visibility','visible');
                    inputs['percentageBtn'].set(false);
                    inputs['commaBtn'].set(false);
                } else {
                    jo.find('tr:eq(2)').children('td:last').css('visibility','hidden');
                    jo.find('tr:eq(3)').children('td:last').css('visibility','hidden');
                }
            } else {
                jo.find('tr:gt(1)').hide();
            }
        },
        submit: function(){
            var actions = [],
                prop;
            this.actionDataCache[this.name] = this.getActionData();

            for (prop in this.actionDataCache) {
                if (this.actionDataCache.hasOwnProperty(prop)) {
                    actions.push(this.actionDataCache[prop]);
                }
            }

            jive.hide();
            jive.runAction({actionData: actions,
                defaultAction: true});
            this.actionDataCache = {};
        },
        onBlur: function() {
            this.actionDataCache[this.name] = this.getActionData();
        },
        onHide: function() {
            this.actionDataCache = {};
        },
        getActionData: function(isForCache) {
            var inputs = jive.selected.form.inputs;
            return {
                actionName: 'editColumnValues',
                editColumnValueData:{
                    tableUuid: jive.selected.jo.data('tableuuid'),
                    columnIndex: jive.selected.ie.config.columnIndex,
                    fontName: jive.escapeFontName(inputs['cellsFontName'].get()),
                    fontSize: inputs['cellsFontSize'].get(),
                    fontBold: inputs['cellsFontBold'].get(),
                    fontItalic: inputs['cellsFontItalic'].get(),
                    fontUnderline: inputs['cellsFontUnderline'].get(),
                    fontHAlign: inputs['cellsFontAlign'].get(),
                    fontColor: inputs['cellsFontColor'].get(),
                    fontBackColor: inputs['cellsFontBackColor'].getBackColor(),
                    mode: inputs['cellsFontBackColor'].getModeValue(),
                    formatPattern: inputs['formatPattern'].get()
                }
            };
        }
    };

    jive.interactive.column.columnConditionalFormattingForm = {
        actionDataCache: {},
        name: 'columnConditionalFormatting',
        title: jive.i18n.get('column.conditionalFormatting.title'),
        method: 'get',
        options: null,
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
        onShow:function(){
            var it = this,
                conditionType =  jive.selected.ie.config.conditionalFormatting.conditionType.toLowerCase(),
                table = jive.selected.form.jo.find('table:eq(1)'),
                isFromCache = false;

            jive.selected.form.jo.parent().css({'overflow-y': 'auto'});
            it.options = jive.selected.ie.config.filtering.filterOperatorTypeValueSelector;

            if (this.actionDataCache[this.name]) {
                metadata = this.actionDataCache[this.name].conditionalFormattingData.conditions;
                isFromCache = true;
            } else {
                metadata = jive.selected.ie.config.conditionalFormatting.conditions;
            }

            /**
             * for boolean fields, hide the condition column
             */
            if (conditionType === 'boolean') {
                table.find('th:eq(2), tr.add td:eq(2)').hide();
            } else {
                table.find('th:eq(2), tr.add td:eq(2)').show();
            }

            $.each(metadata, function(i,v) {
                it.addFormatCondition(jive.selected.form.jo, v, isFromCache);
            });
        },
        onBlur: function() {
            var it = this;
            this.actionDataCache[this.name] = this.getActionData();
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

            console.info(this.actionDataCache);

            for (prop in this.actionDataCache) {
                if (this.actionDataCache.hasOwnProperty(prop)) {
                    actions.push(this.actionDataCache[prop]);
                }
            }

            jive.hide();
            console.info(actions);
            jive.selected.ie.format(actions);
        },
        addFormatCondition: function(jo, conditionData, isFromCache) {
            var conditionType =  jive.selected.ie.config.conditionalFormatting.conditionType.toLowerCase(),
                calendarPattern = jive.selected.ie.config.conditionalFormatting.calendarPattern,
                calendarTimePattern = jive.selected.ie.config.conditionalFormatting.calendarTimePattern,
                form = jo.closest('form'),
                table = form.find('table:eq(1)'),
                tr = [],
                it = this,
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
        getActionData: function(isForCache) {
            var metadata = jive.selected.ie.config.conditionalFormatting,
                inputs = jive.selected.form.inputs,
                actionData = {
                    actionName: 'conditionalFormatting',
                    conditionalFormattingData: {
                        tableUuid: metadata.tableUuid,
                        columnIndex: jive.selected.ie.config.columnIndex,
                        conditionPattern: metadata.conditionPattern,
                        conditionType: metadata.conditionType,
                        columnType: metadata.columnType,
                        fieldOrVariableName: metadata.fieldOrVariableName,
                        conditions: []
                    }
                };

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

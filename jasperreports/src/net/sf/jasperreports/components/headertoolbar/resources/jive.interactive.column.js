jive.interactive.column = jive.interactive.column || {
    uuid: null,
    allColumns: {},
    count: 0,
    ids: {},
    fontSizes:null,
    fonts: {
    	extension: null,
    	system: null
    },
    actions: {
        'Format': {icon: 'formatIcon', title:'Column options', actions:{
            'Formatting...': {fn:'formatHeader'},
            'Hide column': {fn:'hide', arg:'{"hide":true}'},
            'Show columns': {label: 'Show columns &#x25ba;', actions:{
                'All': {label: '&lt;All&gt;', fn: 'hide', arg:'{"hide":false,"column":"all"}'}
            }}
        }},
        'Filter': {icon: 'filterIcon', title:'Column filters', fn: 'filter'},
        'Sort ascending':{icon: 'sortAscIcon', title:'Sort ascending', fn:'sort', arg:['Asc']},
        'Sort descending': {icon: 'sortDescIcon', title:'Sort descending', fn: 'sort', arg:['Desc']}
    },
    dropColumns: {},
    dropPoints: {},
    ldi: null,
    rdi: null,
    delta: null,

    init: function(allColumns, tableUuid){
        var t,c,lt,i,j,it = this;
        it.allColumns[tableUuid] = allColumns;
        /*
         * Load dynamic form data
         */
         it.formatHeaderForm.elements[0][1][0].groups = [{name:'Extension Fonts',values:[]}];
         it.formatCellsForm.elements[0][0][0].groups = [{name:'Extension Fonts',values:[]}];
         jQuery.each(it.fonts.extension,function(i,v) {
        	 it.formatHeaderForm.elements[0][1][0].groups[0].values.push([v,v]);
             it.formatCellsForm.elements[0][0][0].groups[0].values.push([v,v]);
         });
         it.formatHeaderForm.elements[0][1][0].groups.push({name:'System Fonts',values:[]});
         it.formatCellsForm.elements[0][0][0].groups.push({name:'System Fonts',values:[]});
         jQuery.each(it.fonts.system,function(i,v) {
        	 it.formatHeaderForm.elements[0][1][0].groups[1].values.push([v,v]);
        	 it.formatCellsForm.elements[0][0][0].groups[1].values.push([v,v]);
         });

        it.formatHeaderForm.elements[0][1][1].values = [];
        it.formatCellsForm.elements[0][0][1].values = [];
        jQuery.each(it.fontSizes,function(i,v) {
            it.formatHeaderForm.elements[0][1][1].values.push([v,v]);
            it.formatCellsForm.elements[0][0][1].values.push([v,v]);
        });
        /*
         * Compute drop boundaries (x-axis only) for DnD visual feedback.
         */
        it.dropPoints[tableUuid] = [];
        it.dropColumns[tableUuid] = [];

        t = jQuery('.jrtableframe[data-uuid=' + tableUuid + ']').eq(0);
        t.find('.columnHeader').each(function(i){
            c = jQuery(this);
            lt = c.offset().left;
            it.enableColumn(c.data('popupid'), tableUuid)
            it.dropColumns[tableUuid].push('col_'+c.data('popupcolumn'));
            it.dropPoints[tableUuid].push(lt);
        });
        it.dropPoints[tableUuid].push(lt + c.width());
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
        var menu = it.actions.Format.actions['Show columns'];
        for(i in allColumns) {
            c = allColumns[i];
           	menu.actions[c.uuid] = {label: c.label, fn:'hide', arg:'{"hide":false,"column":['+c.index+'], "columnUuid": "' + c.uuid + '"}'};
        }
        it.count = it.dropColumns[tableUuid].length;
    },
    enableColumn: function(columnUuid, tableUuid) {
    	var col = this.getColumnByUuid(columnUuid, tableUuid);
    	if (col != null) {
    		col.enabled = true;
    	}
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
    getInteractiveElementFromProxy: function(cell){
        var clss = cell.attr('class').split(' ');
        return cell.parent().find('div[data-popupcolumn="' + clss[1].substring(4) + '"]');
    },
    getElementSize: function(){
        var jo = jive.selected.jo;
        var h;
        var cid = jo.data('popupColumn') || jo.data('popupcolumn');
        cid = ('' + cid).replace(/\./g,'\\.');
        var lastCell = jQuery('.col_' + cid + ':last', jo.closest('.jrtableframe'));
        if(lastCell && lastCell.length > 0) {
            var lastElemTop = lastCell.position().top;
            var lastElemHeight = lastCell.height();
            h = lastElemTop + lastElemHeight - jo.position().top;
        } else {
        	h = jo.height();
        }
        return {w:jo.width(),h:h};
    },
    onSelect: function(){
        var it = this,
        	allOption = [],
        	pmenu = jive.ui.foobar.menus.column['Format'].jo.find('ul[label="Show columns"]').eq(0),
        	tableUuid = jive.selected.jo.closest('.jrtableframe').data('uuid'),
        	allColumns = it.allColumns[tableUuid],
        	menuItmArgs,
        	col;

        if(it.count == 1) {
            jive.ui.foobar.menus.column['Format'].jo.find('li').eq(2).hide();
            jive.ui.overlay.jo.draggable('option','disabled',true);
        }else {
            jive.ui.foobar.menus.column['Format'].jo.find('li').eq(2).show();
            jive.ui.overlay.jo.draggable('option','disabled',false);
        }

        pmenu.children().each(function(i,el){
        	if (i > 0) {
        		menuItmArgs = jQuery(el).data('args');
        		col = it.getColumnByUuid(menuItmArgs.columnUuid, tableUuid);
        		if (col && col.enabled === true) {
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
        	pmenu.parent().show();
            pmenu.children().eq(0).data('args',{hide:false,column:allOption});
        } else {
            pmenu.parent().hide();
        }
    },
    onDragStart: function(){
        var parent = jive.selected.jo.parent();
        this.uuid = parent.data('uuid');
        var c = 'col_'+ jive.selected.jo.data('popupcolumn');
        var ci = jQuery.inArray(c,this.dropColumns[this.uuid]) * 2;
        this.ldi = ci == 0 ? 0 : ci - 1;
        this.rdi =  ci + 3 == this.dropPoints[this.uuid].length ? ci + 2 : ci + 3;

        this.delta = jive.ui.marker.position.left - this.dropPoints[this.uuid][ci+2];
    },
    onDrag: function(evt,ui){
        var ev = evt.originalEvent.originalEvent || evt;
        var markers = this.dropPoints[this.uuid];

        if(ev.pageX < markers[this.ldi]) {
            if(this.ldi > 0){
                this.dropColumnIndex = this.ldi % 2 == 1 ? this.ldi - 1 : this.ldi;
                jive.ui.marker.jo.css('left', markers[this.dropColumnIndex] + this.delta +'px').show();
                this.rdi = this.ldi;
                this.ldi--;
            }
        }
        if(ev.pageX > markers[this.rdi]) {
            if(this.rdi < (markers.length-1)) {
                this.dropColumnIndex = this.rdi % 2 == 1 ? this.rdi + 1 : this.rdi;
                jive.ui.marker.jo.css('left', markers[this.dropColumnIndex] + this.delta + 'px').show();
                this.ldi = this.rdi;
                this.rdi++;
            }
        }

    },
    onDragStop: function(ev,ui){
        var dropIndex = this.dropColumnIndex / 2;
        dropIndex == this.count && dropIndex--;

        if(dropIndex != jive.selected.ie.columnIndex) {
            jive.runAction({
                actionName: 'move',
                moveColumnData: {
                    tableUuid: this.uuid,
                    columnToMoveIndex: jive.selected.ie.columnIndex,
                    columnToMoveNewIndex: dropIndex
                }
            });
        }
    },
    resize: function(width){
        jive.hide();
        jive.runAction({
            actionName: 'resize',
            resizeColumnData: {
                tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid'),
                columnIndex: jive.selected.ie.columnIndex,
                direction: 'right',
                width: Math.floor(width)
            }
        });
    },
    sort: function(argv){
        jive.hide();
        jive.runAction(jive.selected.ie.headerToolbar['sort'+argv[0]+'Btn']['data-sortData']);
    },
    filter: function(){
        jive.ui.dialog.show('Filter Column',['columnfilter']);
    },
    formatHeader: function(){
        jive.ui.dialog.show('Format Column',['formatHeader', 'formatCells']);
    },
    hide: function(args){
        jive.hide();
        jive.runAction({
            actionName: 'hideUnhideColumns',
            columnData: {
                hide: args.hide,
                columnIndexes: args.column instanceof Array ? args.column : [jive.selected.ie.columnIndex],
                tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid')
            }
        });
    },
    setDynamicProperties: function (obj) {
    	jive.interactive.column.fontSizes = obj.fontSizes;
    	jive.interactive.column.fonts = obj.fonts;
    },
    showCurrencyDropDown: function(){
    	jive.selected.form.inputs['currencyBtn1'].showOptions();
    },
    applyCurrencyFormat: function(args) {
    	var it = this,
    		input = jive.selected.form.inputs['currencyBtn1'],
    		cSymbol = jQuery('#formatPattern').data('cSymbol');
    	
		jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
            
            // attempt to remove current symbol, if present
            if (cSymbol && cSymbol.length > 0) {
            	opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), false, cSymbol));
            	opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), false, cSymbol));
            } 

            // apply new symbol
            if (args.val && args.val.length > 0) {
            	opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), true, args.val));
            	opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), true, args.val));
            	jQuery('#formatPattern').data('cSymbol', args.val);
            }
        });
    	
    	input.hideOptions();
    },
//    toggleCurrencyFormat: function(){
//        var it = this;
//        jQuery('#formatPattern').children().each(function (i, optElem) {
//            var opt = jQuery(optElem);
//            opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), jive.selected.form.inputs['currencyBtn'].selected));
//            opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), jive.selected.form.inputs['currencyBtn'].selected));
//        });
//    },
    togglePercentageFormat: function(){
        var it = this;
        jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
            opt.text(it.numberFormat.addRemovePercentageForNumber(opt.text(), jive.selected.form.inputs['percentageBtn'].selected));
            opt.val(it.numberFormat.addRemovePercentage(opt.val(), jive.selected.form.inputs['percentageBtn'].selected));
        });
    },
    toggleCommaFormat: function(){
        var it = this;
        jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
            opt.text(it.numberFormat.addRemoveThousandsSeparator(opt.text(), jive.selected.form.inputs['commaBtn'].selected));
            opt.val(it.numberFormat.addRemoveThousandsSeparator(opt.val(), jive.selected.form.inputs['commaBtn'].selected));
        });
    },
    addDecimal: function(){
        var it = this;
        jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
            opt.text(it.numberFormat.addRemoveDecimalPlace(opt.text(), true));
            opt.val(it.numberFormat.addRemoveDecimalPlace(opt.val(), true));
        });
    },
    remDecimal: function(){
        var it = this;
        jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
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
    elements: [
        [[{type:'radio',id:'clearFilter',label:'Show all rows',value:'true'}]],
        [
            [{type:'radio',id:'clearFilter',label:'Show only rows where',value:'false',colspan:4}],
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
        it.jc.filterStart = jQuery('#fieldValueStart');
        it.jc.filterEnd = jQuery('#fieldValueEnd').prop('disabled',true);
        it.jc.filterType = jQuery('#filterTypeOperator').change(function(){
            jQuery(this).val().indexOf('BETWEEN') >= 0 ? it.jc.filterEnd.prop('disabled',false) : it.jc.filterEnd.prop('disabled',true);
        });
        jQuery('input[name="clearFilter"]').change(function(){
        	var filtertype = jive.selected.ie.filterdiv.filterDivForm.filterType.toLowerCase();
            
        	for(p in it.jc) (it.jc.hasOwnProperty(p) && jQuery(this).val() == 'true') ? it.jc[p].prop('disabled',true) : it.jc[p].prop('disabled',false);
            it.jc.filterEnd.prop('disabled', (it.jc.filterType.val().indexOf('BETWEEN') >= 0 && jQuery(this).val() == 'false') ? false : true);

            if (filtertype == 'boolean' && jQuery(this).val() == 'false') {
           		it.jc.filterStart.prop('disabled', true);
            }
        });
    },
    onShow:function(){
        var it = this;
        var metadata = jive.selected.ie.filterdiv.filterDivForm;
        var filtertype = metadata.filterType.toLowerCase();
        var options = {
            text : [
                ['EQUALS','Equals'],
                ['IS_NOT_EQUAL_TO','Is not equal to'],
                ['CONTAINS','Contains'],
                ['DOES_NOT_CONTAIN','Does not contain'],
                ['STARTS_WITH','Starts with'],
                ['DOES_NOT_START_WITH','Does not start with'],
                ['ENDS_WITH','Ends with'],
                ['DOES_NOT_END_WITH','Does not end with']
            ],
            date: [
                ['EQUALS','Equals'],
                ['IS_NOT_EQUAL_TO','Is not equal to'],
                ['IS_BETWEEN','Is between'],
                ['IS_NOT_BETWEEN','Is not between'],
                ['IS_ON_OR_BEFORE','Is on or before'],
                ['IS_BEFORE','Is before'],
                ['IS_ON_OR_AFTER','Is on or after'],
                ['IS_AFTER','Is after']
            ],
            numeric: [
                ['EQUALS','Equals'],
                ['DOES_NOT_EQUAL','Does not equal'],
                ['GREATER_THAN','Greater than'],
                ['GREATER_THAN_EQUAL_TO','Greater than or equal to'],
                ['LESS_THAN','Less than'],
                ['LESS_THAN_EQUAL_TO','Less than or equal to'],
                ['IS_BETWEEN','Is between'],
                ['IS_NOT_BETWEEN','Is not between']
            ],
            boolean: [
                ['IS_TRUE', 'Is true'],
                ['IS_NOT_TRUE', 'Is not true'],
                ['IS_FALSE', 'Is false'],
                ['IS_NOT_FALSE', 'Is not false']
            ]
        }
        it.jc.filterType.empty();
        var htm = [];
        jQuery.each(options[filtertype],function(i,v){
            htm.push('<option value="'+v[0]+'">'+v[1]+'</option>');
        });
        it.jc.filterType.append(htm.join(''));
        it.jc.filterType.val(metadata.filterTypeOperator);
        it.jc.filterStart.val(metadata.fieldValueStart);

        var filterOff = metadata.filterTypeOperator == '' ? true : false;
        jQuery('input[name="clearFilter"][value="'+filterOff+'"]').prop("checked",true);
        for(p in it.jc) (it.jc.hasOwnProperty(p) && filterOff) ? it.jc[p].prop('disabled',true) : it.jc[p].prop('disabled',false);

        it.jc.filterEnd.val(metadata.fieldValueEnd).prop('disabled', (!filterOff && metadata.filterTypeOperator.indexOf('BETWEEN') >= 0) ? false : true);
        
        if (filtertype === 'boolean' && !filterOff) {
        	it.jc.filterStart.prop('disabled',true);
        }
    },
    submit:function(){
        var metadata = jive.selected.ie.filterdiv.filterDivForm,
            actionData = {actionName: 'filter'};

        actionData.filterData = {
            tableUuid: metadata.tableUuid,
            fieldName: metadata.fieldName,
            filterType: metadata.filterType,
            filterPattern: metadata.filterPattern,
            fieldValueStart: jive.selected.form.jo.find('input[name="fieldValueStart"]').val(),
            filterTypeOperator: jive.selected.form.jo.find('select[name="filterTypeOperator"]').val(),
            clearFilter: jive.selected.form.jo.find('input[name="clearFilter"]:checked').val()
        };

        if(!jive.selected.form.jo.find('input[name="filterValueEnd"]').is(':hidden')){
            actionData.filterData.fieldValueEnd = jive.selected.form.jo.find('input[name="fieldValueEnd"]').val();
        }

        if(jive.selected.ie.filterPattern){
            actionData.filterData.filterPattern = metadata.filterPattern;
        }

        jive.hide();
        jive.runAction(actionData);
    }
};
jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.columnFilterForm]);

jive.interactive.column.formatHeaderForm = {
	actionDataCache: {},
    title: 'Headings',
    name: 'formatHeader',
    method: 'get',
    elements: [[
        [{type:'text', id:'headingName', label:'Header text', value:'',colspan:3}],
        [
            {type:'list', id:'headerFontName', label:'Font', values:[], freeText: true, size:6},
            {type:'list', id:'headerFontSize', label:'Size', values:[], freeText: true, size:6},
            {
                type: 'buttons',
                label:'Style',
                items: [
                    {type:'color',id:'headerFontColor',bIcon:'fontColorIcon',title:'Pick a font color'},
                    {type:'checkbox',id:'headerFontBold',value:'bold',bIcon:'boldIcon'},
                    {type:'checkbox',id:'headerFontItalic',value:'italic',bIcon:'italicIcon'},
                    {type:'checkbox',id:'headerFontUnderline',value:'underline',bIcon:'underlineIcon'},
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
        	inputs = jive.selected.form.inputs;
        
        if (this.actionDataCache[this.name]) {
        	metadata = this.actionDataCache[this.name].editColumnHeaderData;
        } else {
        	metadata = jive.selected.ie.headingsTabContent;
        }

        metadata.fontBold ? inputs['headerFontBold'].set() : inputs['headerFontBold'].unset();
        metadata.fontItalic ?  inputs['headerFontItalic'].set() : inputs['headerFontItalic'].unset();
        metadata.fontUnderline ?  inputs['headerFontUnderline'].set() : inputs['headerFontUnderline'].unset();

        inputs['headerFontAlign'].set(metadata.fontHAlign);
        inputs['headingName'].set(metadata.headingName);
        inputs['headerFontName'].set(metadata.fontName);
        inputs['headerFontSize'].set(metadata.fontSize);
        inputs['headerFontColor'].set(metadata.fontColor);
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
        jive.runAction(actions);
        this.actionDataCache = {};
    },
    onHide: function() {
   		this.actionDataCache[this.name] = this.getActionData();
    },
    getActionData: function() {
    	var inputs = jive.selected.form.inputs;
    	return {
            actionName: 'editColumnHeader',
            editColumnHeaderData: {
                tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid'),
                columnIndex: jive.selected.ie.columnIndex,
                headingName: inputs['headingName'].get(),
                fontName: inputs['headerFontName'].get(),
                fontSize: inputs['headerFontSize'].get(),
                fontBold: inputs['headerFontBold'].get(),
                fontItalic: inputs['headerFontItalic'].get(),
                fontUnderline: inputs['headerFontUnderline'].get(),
                fontHAlign: inputs['headerFontAlign'].get(),
                fontColor: inputs['headerFontColor'].get()
            }
    	};
    }
};
jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.formatHeaderForm]);

jive.interactive.column.formatCellsForm = {
	actionDataCache: {},
    title: 'Values',
    name: 'formatCells',
    method: 'get',
    elements: [[
        [
            {type:'list', id:'cellsFontName', label:'Font', values:[], freeText: true, size:6},
            {type:'list', id:'cellsFontSize', label:'Size', values:[], freeText: true, size:6},
            {
                type: 'buttons',
                label:'Style',
                items: [
                    {type:'color',id:'cellsFontColor',bIcon:'fontColorIcon',title:'Pick a font color'},
                    {type:'checkbox',id:'cellsFontBold',value:'bold',bIcon:'boldIcon'},
                    {type:'checkbox',id:'cellsFontItalic',value:'italic',bIcon:'italicIcon'},
                    {type:'checkbox',id:'cellsFontUnderline',value:'underline',bIcon:'underlineIcon'},
                    {type:'radio',id:'cellsFontAlign',value:'Left',bIcon:'leftIcon'},
                    {type:'radio',id:'cellsFontAlign',value:'Center',bIcon:'centerIcon'},
                    {type:'radio',id:'cellsFontAlign',value:'Right',bIcon:'rightIcon'}
                ]
            }
        ],
        [
            {type:'list',id:'formatPattern',label:'Format Pattern',freeText:true,values:[],colspan:2, size:4},
            {
                type:'buttons',
                id: 'numberFormatButtons',
                items: [
                    {	
                    	type: 'dropdown',
                    	id: 'currencyBtn1',
                    	fn: 'showCurrencyDropDown',
                    	bIcon: 'currencyIcon',
                    	options: {
                    		'none': {label: '&lt;None&gt;', value: '', fn: 'applyCurrencyFormat'},
                    		'locale_specific': {label: '\u00A4 - Locale specific', value: '\u00A4', fn: 'applyCurrencyFormat'},
                    		'dollar': {label: '\u0024 - USD', value: '\u0024', fn: 'applyCurrencyFormat'},
                    		'euro': {label: '\u20AC - EUR', value: '\u20AC', fn: 'applyCurrencyFormat'},
                    		'pound': {label: '\u00A3 - GBP', value: '\u00A3', fn: 'applyCurrencyFormat'},
                    		'yen': {label: '\u00A5 - YEN', value: '\u00A5', fn: 'applyCurrencyFormat'}
                    	}
                    },
//                  {type:'checkbox',id:'currencyBtn',fn:'toggleCurrencyFormat',value:'',bIcon:'currencyIcon'},
                    {type:'checkbox',id:'percentageBtn',fn:'togglePercentageFormat',value:'',bIcon:'percentageIcon'},
                    {type:'checkbox',id:'commaBtn',fn:'toggleCommaFormat',value:'',bIcon:'commaIcon'},
                    {type:'action',id:'increaseDecimalsBtn',fn:'addDecimal',value:'',bIcon:'increaseDecimalsIcon'},
                    {type:'action',id:'decreaseDecimalsBtn',fn:'remDecimal',value:'',bIcon:'decreaseDecimalsIcon'}
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
            ie = jive.selected.ie,
            jo = jive.selected.form.jo;
        
        if (this.actionDataCache[this.name]) {
        	metadata = this.actionDataCache[this.name].editColumnValueData;
        } else {
        	metadata = jive.selected.ie.valuesTabContent;
        }

        metadata.fontBold ? inputs['cellsFontBold'].set() : inputs['cellsFontBold'].unset();
        metadata.fontItalic ?  inputs['cellsFontItalic'].set() : inputs['cellsFontItalic'].unset();
        metadata.fontUnderline ?  inputs['cellsFontUnderline'].set() : inputs['cellsFontUnderline'].unset();

        inputs['cellsFontAlign'].set(metadata.fontHAlign);
        inputs['cellsFontName'].set(metadata.fontName);
        inputs['cellsFontSize'].set(metadata.fontSize);
        inputs['cellsFontColor'].set(metadata.fontColor);

        if(typeof ie.formatPatternLabel === 'string') {
            jQuery.each(ie.formatPatternSelector,function(i,o){
                o && htm.push('<option value="'+o.key+'">'+o.val+'</option>');
            })
            jQuery('#formatPattern').html(htm.join(''));
            inputs['formatPattern'].set(metadata.formatPattern);
            jo.find('tr').eq(1).show();
            if (ie.formatPatternLabel.indexOf('Number') >= 0) {
                jo.find('tr').eq(1).children().eq(1).children().css('visibility','visible');
            	inputs['percentageBtn'].unset();
            	inputs['commaBtn'].unset();
            } else {
                jo.find('tr').eq(1).children().eq(1).children().css('visibility','hidden');
            }
        } else {
            jo.find('tr').eq(1).hide();
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
	    jive.runAction(actions);
	    this.actionDataCache = {};
    },
    onHide: function() {
    	this.actionDataCache[this.name] = this.getActionData();
    },
    getActionData: function() {
    	var inputs = jive.selected.form.inputs;
		return {
            actionName: 'editColumnValues',
            editColumnValueData:{
                tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid'),
                columnIndex: jive.selected.ie.columnIndex,
                fontName: inputs['cellsFontName'].get(),
                fontSize: inputs['cellsFontSize'].get(),
                fontBold: inputs['cellsFontBold'].get(),
                fontItalic: inputs['cellsFontItalic'].get(),
                fontUnderline: inputs['cellsFontUnderline'].get(),
                fontHAlign: inputs['cellsFontAlign'].get(),
                fontColor: inputs['cellsFontColor'].get(),
                formatPattern: inputs['formatPattern'].get()
            }
		};
    }
};
jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.formatCellsForm]);

jasperreports.global.events.JIVE_COLUMN_INIT.status = 'finished';
jasperreports.global.processEvent(jasperreports.global.events.JIVE_COLUMN_INIT.name);

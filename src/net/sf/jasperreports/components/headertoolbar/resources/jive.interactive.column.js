jive.interactive.column = jive.interactive.column || {
    uuid: null,
    allColumns: null,
    columnLabels: {},
    count: 0,
    ids: {},
    fontSizes:null,
    fonts: {
    	extension: null,
    	system: null
    },
    actions: {
        'Format': {icon: 'formatIcon', actions:{
            'Format header': {fn:'formatHeader'},
            'Format cells': {fn:'formatCells'},
            'Hide column': {fn:'hide', arg:'{"hide":true}'},
            'Show column': {actions:{
                'All': {fn: 'hide', arg:'{"hide":false,"column":"all"}'}
            }}
        }},
        'Sort ascending':{icon: 'sortAscIcon', fn:'sort', arg:['Asc']},
        'Sort descending': {icon: 'sortDescIcon', fn: 'sort', arg:['Desc']},
        'Filter': {icon: 'filterIcon', fn: 'filter'}
    },
    dropPoints: {},
    dropColumnIndex: null,
    dropMarks: {},
    dropColumnsRW: {},
    dropColumnsFF: {},
    dropColumnsIndex: {},
    dropLeft: null,
    dropRight: null,
    delta: null,

    init: function(allColumns){
        var t,c,i,j,tid,
            dropPoints = [],
            it = this;
        it.allColumns = allColumns;

        /*
         * Load dynamic form data
         */
         it.formatHeaderForm.elements[1].values = [];
         it.formatCellsForm.elements[0].values = [];
         jQuery.each(it.fonts.extension,function(i,v) {
        	 it.formatHeaderForm.elements[1].values.push([v,v, 'Extension Fonts']);
             it.formatCellsForm.elements[0].values.push([v,v, 'Extension Fonts']);
         });

         jQuery.each(it.fonts.system,function(i,v) {
        	 it.formatHeaderForm.elements[1].values.push([v,v, 'System Fonts']);
        	 it.formatCellsForm.elements[0].values.push([v,v, 'System Fonts']);
         });

        it.formatHeaderForm.elements[2].values = [];
        it.formatCellsForm.elements[1].values = [];
        jQuery.each(it.fontSizes,function(i,v) {
            it.formatHeaderForm.elements[2].values.push([v,v]);
            it.formatCellsForm.elements[1].values.push([v,v]);
        });
        /*
         * Compute drop boundaries (x-axis only) for DnD visual feedback.
         */
        it.columnLabels = {};
        it.dropColumnsIndex = {};
        jQuery('.jrtableframe').each(function(i){
            t = jQuery(this);

            uuid = t.data('uuid');
            if(!it.dropColumnsIndex[uuid]){
                it.dropPoints[uuid] = [];
                it.dropMarks[uuid] = [];
                it.dropColumnsRW[uuid] = [];
                it.dropColumnsFF[uuid] = [];
                it.dropColumnsIndex[uuid] = {};
                dropColumns = [];
                t.find('.columnHeader').each(function(i){
                    c = jQuery(this);
                    dropColumns.push('col_'+c.data('popupcolumn'));

                    it.columnLabels[jQuery('div > span > span > span',c).html()] = 1;

                    var off = c.offset();
                    it.dropPoints[uuid].push(off.left);
                    it.dropPoints[uuid].push(off.left + c.width());
                });

                for(i=0;i<dropColumns.length;i++) {
                    j = i * 2;
                    it.dropMarks[uuid].push(it.dropPoints[uuid][j]);
                    it.dropMarks[uuid].push(it.dropPoints[uuid][j] + (it.dropPoints[uuid][j+1] - it.dropPoints[uuid][j]) / 2);

                    it.dropColumnsRW[uuid].push(j);
                    it.dropColumnsRW[uuid].push(j);

                    it.dropColumnsFF[uuid].push(j);
                    it.dropColumnsFF[uuid].push(j+1);

                    it.dropColumnsIndex[uuid][dropColumns[i]] = j;
                }
                it.dropMarks[uuid].push(it.dropPoints[uuid][j+1]);
                it.dropColumnsRW[uuid].push(j+1);
                it.dropColumnsFF[uuid].push(j+1);
            }
        });
        /*
         * Create show column menu
         */
        var menu = it.actions.Format.actions['Show column'];
        for(i in allColumns) {
            c = allColumns[i];
            menu.actions[c.label] = {fn:'hide',arg:'{"hide":false,"column":'+c.index+'}'};
        }
        it.count = dropColumns.length;
    },
    getInteractiveElementFromProxy: function(cell){
        var clss = cell.attr('class').split(' ');
        return cell.parent().find('div[data-popupcolumn="' + clss[1].substring(4) + '"]');
    },
    getElementSize: function(){
        var jo = jive.selected.jo;
        var h = jo.height();
        var lastCell = jQuery('.col_' + jo.attr('data-popupColumn') + ':last', jo.closest('.jrtableframe'));
        if(lastCell && lastCell.length > 0) {
            var lastElemTop = lastCell.position().top;
            var lastElemHeight = lastCell.height();
            h = lastElemTop + lastElemHeight - jo.position().top;
        }
        return {w:jo.width(),h:h};
    },
    onToolbarShow: function(){
        var it = this;
        var on = false;
        it.count == 1 ?
            jive.ui.foobar.menus.column['Format'].jo.find('li').eq(2).hide() :
            jive.ui.foobar.menus.column['Format'].jo.find('li').eq(2).show();

        var allOption = [];
        var pmenu = jive.ui.foobar.menus.column['Format'].jo.find('ul[label="Show column"]').eq(0);
        pmenu.children().each(function(i,el){
            if(it.columnLabels[el.innerHTML]) {
                el.style.display = 'none';
            } else {
                if(el.innerHTML != 'All') {
                    on = true;
                    allOption.push(i-1);
                }
                el.style.display = 'block';
            }
        });
        if(on){
            pmenu.children().eq(0).data('args',{hide:false,column:allOption});
            pmenu.parent().show();
        } else {
            pmenu.parent().hide();
        }
    },
    onDragStart: function(){
        var parent = jive.selected.jo.parent();
        this.uuid = parent.data('uuid');
        var c = 'col_'+ jive.selected.jo.data('popupcolumn');
        this.dropLeft = this.dropColumnsIndex[this.uuid][c] == 0 ? 0 : this.dropColumnsIndex[this.uuid][c] - 1;
        this.dropRight =  this.dropColumnsIndex[uuid][c] + 3 == this.dropMarks.length ? this.dropColumnsIndex[uuid][c] + 2 : this.dropColumnsIndex[uuid][c] + 3;
        this.delta = jive.ui.marker.position.left - this.dropMarks[this.uuid][(jive.selected.ie.columnIndex+1)*2];
    },
    onDrag: function(evt,ui){
        var ev = evt.originalEvent.originalEvent || evt;
        if(ev.pageX < this.dropMarks[this.uuid][this.dropLeft]) {
            if(this.dropLeft > 0){
                this.dropColumnIndex = this.dropColumnsRW[this.uuid][this.dropLeft];
                jive.ui.marker.jo.css('left', this.dropPoints[this.uuid][this.dropColumnIndex] + this.delta +'px').show();
                this.dropLeft = this.dropLeft - 1;
                this.dropRight = this.dropLeft + 1;
            }
        }
        if(ev.pageX > this.dropMarks[this.uuid][this.dropRight]) {
            if(this.dropRight < (this.dropMarks[this.uuid].length-1)) {
                this.dropColumnIndex = this.dropColumnsFF[this.uuid][this.dropRight];
                jive.ui.marker.jo.css('left', this.dropPoints[this.uuid][this.dropColumnIndex] + this.delta + 'px').show();
                this.dropRight = this.dropRight + 1;
                this.dropLeft = this.dropRight - 1;
            }
        }
    },
    onDragStop: function(ev,ui){
        var dropIndex = this.dropColumnIndex % 2 == 1 ? (this.dropColumnIndex + 1) / 2 - 1 : this.dropColumnIndex / 2;
        if(dropIndex >= 0 && (dropIndex != jive.selected.ie.columnIndex || dropIndex != jive.selected.ie.columnIndex + 1)) {
            jive.runAction({
                actionName: 'move',
                moveColumnData: {
                    uuid: this.uuid,
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
                uuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid'),
                columnIndex: jive.selected.ie.columnIndex,
                direction: 'right',
                width: width
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
    formatCells: function(){
        jive.ui.dialog.show('Format Column',['formatHeader', 'formatCells'], 1);
    },
    hide: function(args){
        var cdata ={
            hide: args.hide,
            columnIndexes: args.column instanceof Array ? args.column : [jive.selected.ie.columnIndex],
            tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid')
        }
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
    toggleCurrencyFormat: function(){
        var it = this;
        jQuery('#formatPattern').children().each(function (i, optElem) {
            var opt = jQuery(optElem);
            opt.text(it.numberFormat.addRemoveCurrencySymbol(opt.text(), jive.selected.form.inputs['currencyBtn'].selected));
            opt.val(it.numberFormat.addRemoveCurrencySymbol(opt.val(), jive.selected.form.inputs['currencyBtn'].selected));
        });
    },
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

        addRemoveCurrencySymbol: function(exp, booleanAdd) {
            var cs = jive.interactive.column.numberFormat.symbols.currency,
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
    elements: [
        {type:'list', id:'filterTypeOperator', label:'Filter type', values:[]},
        {type:'text', id:"fieldValueStart", label:'Filter value', value:''},
        {type:'text', id:"fieldValueEnd", value:''}
    ],
    buttons: [],
    onCreate:function(jo){
        /*
         *  This method is called when form is created. Can be used to initiate behavior and cache form elements.
         */
        jQuery('.colfilter',jo).change(function(){
            var v = jQuery('select',this).val().toLowerCase();
            v.indexOf('between') >= 0 ? jive.selected.form.jo.find('#fieldValueEnd').show() : jive.selected.form.jo.find('#fieldValueEnd').hide();
        });
    },
    onShow:function(){
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
            ]
        }
        var se = jive.selected.form.jo.find('select[name="filterTypeOperator"]');
        se.empty();
        var htm = [];
        jQuery.each(options[filtertype],function(i,v){
            htm.push('<option value="'+v[0]+'">'+v[1]+'</option>');
        });
        se.append(htm.join(''));

        metadata.filterTypeOperator && se.val(metadata.filterTypeOperator);
        metadata.fieldValueStart ?
            jive.selected.form.jo.find('input[name="fieldValueStart"]').val(metadata.fieldValueStart) :
            jive.selected.form.jo.find('input[name="fieldValueStart"]').val('');
        metadata.fieldValueEnd ?
            jive.selected.form.jo.find('input[name="fieldValueEnd"]').val(metadata.fieldValueEnd).show() :
            jive.selected.form.jo.find('input[name="fieldValueEnd"]').hide();
    },
    submit:function(){
        var metadata = jive.selected.ie.filterdiv.filterDivForm,
            actionData = {actionName: 'filter'};
        	
        actionData.filterData = {
            uuid: metadata.uuid,
            fieldName: metadata.fieldName,
            filterType: metadata.filterType,
            fieldValueStart: jive.selected.form.jo.find('input[name="fieldValueStart"]').val(),
            filterTypeOperator: jive.selected.form.jo.find('select[name="filterTypeOperator"]').val()
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
    title: 'Header',
    name: 'formatHeader',
    method: 'get',
    elements: [
        {type:'text', id:'headingName', label:'Header text', value:''},
        {type:'grouplist', id:'headerFontName', label:'Font', values:[]},
        {type:'list', id:'headerFontSize', label:'Font size', values:[]},
        {type:'color', id:'headerFontColor', label:'Header Font Color'},
        {
            type: 'buttons',
            label:'Text format',
            items: [
                {type:'checkbox',id:'headerFontBold',value:'bold',bIcon:'boldIcon'},
                {type:'checkbox',id:'headerFontItalic',value:'italic',bIcon:'italicIcon'},
                {type:'checkbox',id:'headerFontUnderline',value:'underline',bIcon:'underlineIcon'}
            ]
        },
        {
            type: 'buttons',
            label:'Text alignment',
            items: [
                {type:'radio',id:'headerFontAlign',value:'Left',bIcon:'leftIcon'},
                {type:'radio',id:'headerFontAlign',value:'Center',bIcon:'centerIcon'},
                {type:'radio',id:'headerFontAlign',value:'Right',bIcon:'rightIcon'}
            ]
        }
    ],
    onCreate:function(jo){

    },
    onShow:function(){
        var metadata = jive.selected.ie.headingsTabContent,
        	inputs = jive.selected.form.inputs;

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
        var inputs = jive.selected.form.inputs,
            actionData = {
                actionName: 'editColumnHeader',
                editColumnHeaderData:{
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

        jive.hide();
        jive.runAction(actionData)
    }
};
jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.formatHeaderForm]);

jive.interactive.column.formatCellsForm = {
    title: 'Cells',
    name: 'formatCells',
    method: 'get',
    elements: [
        {type:'grouplist', id:'cellsFontName', label:'Font', values:[]},
        {type:'list', id:'cellsFontSize', label:'Font size', values:[], freeText: true},
        {type:'color', id:'cellsFontColor', label:'Cell Font Color'},
        {
            type: 'buttons',
            label:'Text format',
            items: [
                {type:'checkbox',id:'cellsFontBold',value:'bold',bIcon:'boldIcon'},
                {type:'checkbox',id:'cellsFontItalic',value:'italic',bIcon:'italicIcon'},
                {type:'checkbox',id:'cellsFontUnderline',value:'underline',bIcon:'underlineIcon'}
            ]
        },
        {
            type: 'buttons',
            label:'Text alignment',
            items: [
                {type:'radio',id:'cellsFontAlign',value:'Left',bIcon:'leftIcon'},
                {type:'radio',id:'cellsFontAlign',value:'Center',bIcon:'centerIcon'},
                {type:'radio',id:'cellsFontAlign',value:'Right',bIcon:'rightIcon'}
            ]
        },
        /*
         formatPatternLabel,
         formatPatternSelector,
         */
        {type:'list', id:'formatPattern', label:'Format Pattern', values:[]},
        {
            type:'buttons',
            items: [
                {type:'checkbox',id:'currencyBtn',fn:'toggleCurrencyFormat',value:'',bIcon:'currencyIcon'},
                {type:'checkbox',id:'percentageBtn',fn:'togglePercentageFormat',value:'',bIcon:'percentageIcon'},
                {type:'checkbox',id:'commaBtn',fn:'toggleCommaFormat',value:'',bIcon:'commaIcon'},
                {type:'action',id:'increaseDecimalsBtn',fn:'addDecimal',value:'',bIcon:'increaseDecimalsIcon'},
                {type:'action',id:'decreaseDecimalsBtn',fn:'remDecimal',value:'',bIcon:'decreaseDecimalsIcon'}
            ]
        }
    ],
    onCreate:function(jo){

    },
    onShow:function(){
        var metadata = jive.selected.ie.valuesTabContent,
            inputs = jive.selected.form.inputs,
            htm = [],
            ie = jive.selected.ie,
            jo = jive.selected.form.jo;

        metadata.fontBold ? inputs['cellsFontBold'].set() : inputs['cellsFontBold'].unset();
        metadata.fontItalic ?  inputs['cellsFontItalic'].set() : inputs['cellsFontItalic'].unset();
        metadata.fontUnderline ?  inputs['cellsFontUnderline'].set() : inputs['cellsFontUnderline'].unset();

        inputs['cellsFontAlign'].set(metadata.fontHAlign);
        inputs['cellsFontName'].set(metadata.fontName);
        inputs['cellsFontSize'].set(metadata.fontSize);
        inputs['cellsFontColor'].set(metadata.fontColor);

        if(typeof ie.formatPatternLabel === 'string') {
            jQuery.each(ie.formatPatternSelector,function(i,o){
                htm.push('<option value="'+o.key+'">'+o.val+'</option>');
            })
            jQuery('#formatPattern').html(htm.join(''));
            inputs['formatPattern'].set(metadata.formatPattern);
            jo.find('tr').eq(5).show();
            if (ie.formatPatternLabel.indexOf('Number') >= 0) {
            	jo.find('tr').eq(6).show();
            	inputs['currencyBtn'].unset();
            	inputs['percentageBtn'].unset();
            	inputs['commaBtn'].unset();
            } else {
            	jo.find('tr').eq(6).hide();
            }
        } else {
            jo.find('tr').eq(5).hide();
            jo.find('tr').eq(6).hide();
        }
    },
    submit: function(){
    	var inputs = jive.selected.form.inputs,
    		actionData = {
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

    	jive.hide();
    	jive.runAction(actionData)
    }
};
jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.formatCellsForm]);

jasperreports.global.events.JIVE_COLUMN_INIT.status = 'finished';
jasperreports.global.processEvent(jasperreports.global.events.JIVE_COLUMN_INIT.name);

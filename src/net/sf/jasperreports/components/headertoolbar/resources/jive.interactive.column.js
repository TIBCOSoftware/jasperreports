jive.interactive.column = jive.interactive.column || {
    uuid: null,
    allColumns: null,
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
    init: function(){
        var t,c,i,j,tid,
            dropPoints = [],
            m = this.actions.Format.actions['Show column'].actions,
            it = this;
        /*
         * Update menu for show column
         */
        for(i in this.allColumns) {
            c = this.allColumns[i];
            m[c.label] = {fn:'hide',arg:'{"hide":false,"column":'+c.index+'}'};
        }
        /*
         * Load dynamic form data
         */
         jQuery.each(it.fonts.extension,function(i,v) {
            it.formatHeaderForm.elements[1].values.push([v,v]);
         });
        jQuery.each(it.fontSizes,function(i,v) {
            it.formatHeaderForm.elements[2].values.push([v,v]);
        });
        /*
         * Compute drop boundaries (x-axis only) for DnD visual feedback.
         */
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
        var jvt = jasperreports.reportviewertoolbar;
        var selectedColumn = {
            actionBaseData: jQuery.parseJSON(jive.interactive.column.actionBaseData)
        };
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
        jive.hide();
        jive.runAction({
            actionName: 'hideUnhideColumns',
            columnData: {
                hide: args.hide,
                columnIndexes: !isNaN(args.column) ? [args.column] : [jive.selected.ie.columnIndex],
                tableUuid: jive.selected.jo.parent('.jrtableframe').attr('data-uuid')
            }
        });
    },
    setAllColumns: function (allColumns) {
    	this.allColumns = allColumns;
    	this.init();
    },
    setupInteractiveColumn: function (obj) {
    	jive.actionBaseUrl = obj.actionBaseUrl;
    	jive.actionBaseData = obj.actionBaseData;
    	
    	jive.interactive.column.fontSizes = obj.fontSizes;
    	jive.interactive.column.fonts = obj.fonts;
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
        {type:'list', id:'headerFonttName', label:'Font', values:[]},
        {type:'list', id:'headerFontSize', label:'Font size', values:[]},
        {type:'color', id:'headerFontColor', label:'Header Font Color'},
        {
            type:'checkbox',
            ids:['headerFontBold','headerFontItalic','headerFontUnderline'],
            label:'Text format',
            values:['bold','italic','underline'],
            bIcons:['boldIcon','italicIcon','underlineIcon']
        },
        {type:'radio', id:'headerFontAlign', label:'Text alignment', values:['Left','Center','Right'], bIcons:['leftIcon','centerIcon','rightIcon']}
    ],
    onCreate:function(jo){

    },
    onShow:function(){
        var metadata = jive.selected.ie.headingsTabContent;
        //console.info(metadata);
        metadata.fontBold ? jive.selected.form.inputs['headerFontBold'].set() : jive.selected.form.inputs['headerFontBold'].unset();
        metadata.fontItalic ?  jive.selected.form.inputs['headerFontItalic'].set() : jive.selected.form.inputs['headerFontItalic'].unset();
        metadata.fontUnderline ?  jive.selected.form.inputs['headerFontUnderline'].set() : jive.selected.form.inputs['headerFontUnderline'].unset();
        jive.selected.form.inputs['headerFontAlign'].set(metadata.fontHAlign);
        jive.selected.form.inputs['headingName'].set(metadata.headingName);
    },
    submit:function(){
        var table = jive.selected.jo.parent('.jrtableframe'),
            uuid = table.attr('data-uuid'),
            actionData = {
                actionName: 'editColumnHeader',
                editColumnHeaderData:{
                    tableUuid: uuid,
                    columnIndex: jive.selected.ie.columnIndex,
                    headingName: jive.selected.form.inputs['headingName'].get(),
                    fontHAlign: jive.selected.form.inputs['headerFontAlign'].get(),
                    fontBold: jive.selected.form.inputs['headerFontBold'].get(),
                    fontItalic: jive.selected.form.inputs['headerFontItalic'].get(),
                    fontColor: jive.selected.form.inputs['headerFontColor'].get(),
                    fontName: "DejaVu Sans",
                    fontSize: 8
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
        {type:'list', id:'cellFontName', label:'Font', values:[]},
        {type:'list', id:'cellFontSize', label:'Font size', values:[]},
        {type:'color', id:'cellFontColor', label:'Cell Font Color'},
        {
            type:'checkbox',
            ids:['cellFontBold','cellFontItalic','cellFontUnderline'],
            label:'Text format',
            values:['bold','italic','underline'],
            bIcons:['boldIcon','italicIcon','underlineIcon']
        },
        {type:'radio', id:'cellFontAlign', label:'Text alignment', values:['left','center','right'], bIcons:['leftIcon','centerIcon','rightIcon']}
    ],
    onCreate:function(jo){

    },
    onShow:function(){

    },
    submit:function(){

    }
};

jasperreports.global.subscribeToEvent('jive_init', 'jive.ui.forms.add', [jive.interactive.column.formatCellsForm]);

jasperreports.global.events.JIVE_COLUMN_INIT.status = 'finished';
jasperreports.global.processEvent(jasperreports.global.events.JIVE_COLUMN_INIT.name);

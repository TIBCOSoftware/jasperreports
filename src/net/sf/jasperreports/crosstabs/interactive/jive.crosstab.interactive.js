define(["jquery.ui-1.10.3", "text!jive.crosstab.templates.tmpl", "text!jive.crosstab.templates.styles.css", "jive.i18n"], function($, templates, templateCss, jivei18n) {
	var i18nfn = function (key) {
		if (jivei18n.hasOwnProperty(key)) {
			return jivei18n[key];
		} else {
			return key;
		}
	};

	var ixt = {
		initialized: false,
		selected: null,
		init: function(report) {
			var ic = this;
			
			if (!ic.initialized) {
				$('head').append('<style id="jivext-stylesheet">' + templateCss + '</style>');
			
				$('#jivext_components').length == 0 &&  $('body').append('<div id="jivext_components"></div>');
				$('#jivext_components').empty();
				$('#jivext_components').append(templates);
				
				ic.getReportContainer().on('click touchend', function(){
					ic.hide();
					//TODO lucianc
					//$('body').trigger('jive.inactive');
				});
				
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
			$('table.jrPage').on('click touchend', 'td.jrxtcolheader[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var columnHeader = $(this);
					ixt.selectDataColumn(crosstab, columnHeader);
					return false;
				}
			});
			
			$('table.jrPage').on('click touchend', 'td.jrxtdatacell[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var dataCell = $(this);
					ixt.selectDataColumn(crosstab, dataCell);
					return false;
				}
			});
			
			$('table.jrPage').on('click touchend', 'td.jrxtrowheader[data-jrxtid=\'' + crosstab.getFragmentId() + '\']', function(evt){
				if(!$(evt.target).parent().is('a')) {
					var rowHeader = $(this);
					ixt.selectRowGroup(crosstab, rowHeader);
					return false;
				}
			});
		},
		selectDataColumn: function(crosstab, cell) {
			var columnIdx = cell.data('jrxtcolidx');
			var parentTable = cell.parents("table:first");
			var firstHeader = $('td.jrxtcolheader[data-jrxtcolidx=\'' + columnIdx + '\']:first', parentTable);
			var lastCell = $('td.jrxtdatacell[data-jrxtcolidx=\'' + columnIdx + '\']:last', parentTable);

			var width = lastCell.offset().left + lastCell.outerWidth() - firstHeader.offset().left;
			var height = lastCell.offset().top + lastCell.outerHeight() - firstHeader.offset().top;
			
			ixt.selected = {crosstab: crosstab, header: firstHeader};
			ixt.overlay.show({w: width, h: height});
			
			var columnIdx = firstHeader.data('jrxtcolidx');
			var sortingEnabled = crosstab.isDataColumnSortable(columnIdx);
			ixt.foobar.show(sortingEnabled);
		},
		selectRowGroup: function(crosstab, cell) {
			var columnIdx = cell.data('jrxtcolidx');
			var headers = $('td.jrxtrowheader[data-jrxtcolidx=\'' + columnIdx + '\']', cell.parents("table:first"));
			var firstHeader = $(headers[0]);
			var lastHeader = $(headers[headers.length - 1]);

			var width = lastHeader.offset().left + lastHeader.outerWidth() - firstHeader.offset().left;
			var height = lastHeader.offset().top + lastHeader.outerHeight() - firstHeader.offset().top;
				
			ixt.selected = {crosstab: crosstab, header: firstHeader};
			ixt.overlay.show({w: width, h: height});
			ixt.foobar.show(true);
		},
		overlay: {
				jo: null,
				show: function(dim) {
					var isFirstTimeSelection = !this.jo;
					isFirstTimeSelection && (this.jo = $('#jivext_overlay'));
					
					this.jo.css({
						width: dim.w,
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
		}
	};

	return ixt;
});
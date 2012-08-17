jQuery.extend(jive, {
    init:function(settings){
        var jiveInitEvent = jasperreports.events.registerEvent('jive.init');

        jQuery.extend(jive,settings);
        jQuery('div.jrPage').parent().on('click touchend',function(){
            jive.hide();
            jQuery.event.trigger('jive_inactive');
        });

        jQuery('#jive_components').length == 0 &&  jQuery('body').append('<div id="jive_components"></div>');
        jQuery('#jive_components').empty();
        jive.ui.marker.jo = null;
        jive.ui.overlay.jo = null;
        jive.ui.foobar.jo = null;
        jive.ui.dialog.jo = null;
        jive.ui.colorpicker.jo = null;

        jQuery('#jive_overlay').appendTo('#jive_components');
        jQuery('#jive_marker').appendTo('#jive_components');
        jQuery('div.jive_drag_label').appendTo('#jive_components');
        jQuery('#jive_foobar').appendTo('#jive_components');
        jQuery('#jive_menus').appendTo('#jive_components');
        jQuery('#jive_dropdown').appendTo('#jive_components');
        jQuery('#jive_forms').appendTo('#jive_components');
        jQuery('#jive_dialog').appendTo('#jive_components');
        jQuery('#jive_colorpicker').appendTo('#jive_components');

        /*
         * Init event handlers for viewer. One time event?
         */
        var jmenuitem;
        jQuery('div#jive_menus, div#jive_dropdown').on({
            'mouseup touchend': function(evt){
                jmenuitem = jQuery(this);
                var args = jmenuitem.data('args');

                jmenuitem.attr('fn') && jive.interactive[jive.selected.ie.type][jmenuitem.attr('fn')](args);

                if(evt.type == 'touchend') {
                    var submenu = jmenuitem.children('ul');
                    if(submenu.length){
                        submenu.show().position({
                            of: jmenuitem,
                            my: 'left top',
                            at: 'right top'
                        });
                    } else {
                        jive.hide();
                    }
                }
                evt.preventDefault();
            },
            'mouseover': function(evt){
                jmenuitem = jQuery(this);
                jmenuitem.addClass('hover');
                if(jmenuitem.width() < (jmenuitem.parent().width() - 24)) {
                    jmenuitem.width(jmenuitem.parent().width()-24); // IE7 fix
                }
                jmenuitem.parent().prevAll().hide();
                var key = jmenuitem.attr('key');
                if(jive.ui.foobar.menus[jive.selected.ie.type][key]){
                    jive.ui.foobar.menus[jive.selected.ie.type][key].jo.show().position({
                        of: jmenuitem,
                        my: 'left top',
                        at: 'right top'
                    });
                }
            },
            'mouseout': function(evt){
                jmenuitem = jQuery(this);
                jmenuitem.removeClass('hover');
            }
        }, '.pmenuitem');

        jQuery('div#jive_dropdown').on({
            'mouseup touchend': function(evt){
                var args = jQuery(this).data('args');
                jQuery(this).attr('fn') && jive.interactive[jive.selected.ie.type][jQuery(this).attr('fn')](args);
                evt.preventDefault();
            }
        }, '.pmenuitem');

        jQuery('div#jive_dropdown').on('mouseleave', '.pmenu', function(evt) {
            jQuery(this).hide();
        });

        jiveInitEvent.trigger();
    },
    initInteractiveElement: function(o){
        if(jive.elements[o.id]) {
            /*
             * Check if report has been reloaded, if so we need to setup event
             * listeners.
             */
            if(typeof jQuery('div.jrPage').data('events') === 'undefined') {
                jive.selectors = {};
            }
        }
        jive.elements[o.id] = jQuery.extend({},o);
        /*
         * Check if event listener has for selector already set.
         */
        if(!jive.selectors[o.selector]){
            jive.selectors[o.selector] = o.type;
            jQuery('div.jrPage').on('click touchend',o.selector,function(evt){
            	// keep html links functional
            	if(jQuery(evt.target).closest('a').size() == 0) {
            		var jo = jQuery(this);
            		jive.selectInteractiveElement(jo);
            		return false;
            	}
            })
        }
        if(o.proxySelector && !jive.selectors[o.proxySelector]) {
            jive.selectors[o.proxySelector] = o.type;
            jQuery('div.jrPage').on('click touchend',o.proxySelector,function(evt){
            	// keep html links functional
            	if(jQuery(evt.target).closest('a').size() == 0) {
            		var jo = jive.interactive[o.type].getInteractiveElementFromProxy(jQuery(this));
            		jive.selectInteractiveElement(jo);
            		return false;
            	}
            })
        }
    },
    selectInteractiveElement: function(jo){
    	if (jo.is('.interactiveElement')) {
	        jive.ui.dialog.isVisible && jive.ui.dialog.hide();
	        jive.selected = {
	            ie: jive.elements[jo.data('popupid')],
	            jo: jo
	        };
	        var dim = jive.interactive[jive.selected.ie.type].getElementSize();
	
	        jive.ui.overlay.show(dim);
	        jive.ui.marker.show(dim);
	        jive.ui.foobar.show(dim);
	        jive.ui.foobar.dropMenu && jive.ui.foobar.dropMenu.jo.hide();
	
	        jive.active = true;
	        jive.started = true;
	        jive.interactive[jive.selected.ie.type].onSelect();
    	}
    },
    hide: function(items, keepDialogOpen){
        if(!items){
        	if (keepDialogOpen) {
        		jive.active = jive.ui.dialog.isVisible;
        	} else {
        		jive.active = false;
        		jive.ui.dialog.isVisible && jive.ui.dialog.hide();
        	}
            jive.ui.marker.jo && jive.ui.marker.jo.appendTo('#jive_components').hide();
            jive.ui.overlay.jo && jive.ui.overlay.jo.appendTo('#jive_components').hide();
            jive.ui.foobar.jo && jive.ui.foobar.jo.appendTo('#jive_components').hide();
            jQuery('.pmenu').hide();
            jive.ui.colorpicker.jo && jive.ui.colorpicker.jo.hide();
        } else {
            jQuery.each(items,function(i,v){
                jive.ui[v].jo && jive.ui[v].jo.hide();
            });
        }
    }
});

jive.ui.marker = {
    jo: null,
    position: null,
    setElement: function(selector){
        this.jo = jQuery(selector);
        this.jo.draggable({
            axis: "x",
            start: function(ev,ui) {
                jive.ui.overlay.left = jive.ui.overlay.jo.position().left;
            },
            drag: function(ev,ui){
                jive.ui.overlay.jo.width(ui.position.left - jive.ui.overlay.left);
            },
            stop:function(ev,ui) {
                jive.interactive[jive.selected.ie.type].resize((ui.position.left - jive.ui.overlay.left) / jive.ui.scaleFactor);
            }
        });
        //this.jo.appendTo('div.jrPage');
    },
    show: function(dim){
        !this.jo && this.setElement('#jive_marker');
        this.jo.css({
            height: dim.h+'px'
        });
        this.jo.appendTo('div.jrPage').show();
        this.jo.position({of:jive.ui.overlay.jo, my: 'left top', at:'right top',collision:'none'});

        var de = this.jo.get(0);
        var left = this.jo.get(0).style.left;
        var top = this.jo.get(0).style.top;
        var i = left.indexOf('px');
        if(i > 0) left = left.substring(0,i);
        i = top.indexOf('px');
        if(i > 0) top = top.substring(0,i);
        this.position = {
            left: left,
            top: top
        }
    }
}

jive.ui.overlay = {
    jo: null,
    left: null,
    setElement: function(selector){
        this.jo = jQuery(selector);
        this.jo.draggable({
            cursorAt: { top: 40, left: -30 },
            start: function(ev,ui) {
                jive.hide(['foobar','marker']);
                jive.interactive[jive.selected.ie.type].onDragStart(ev,ui);
            },
            drag: function(ev,ui){
                jive.interactive[jive.selected.ie.type].onDrag(ev,ui);
            },
            stop:function(ev,ui) {
                jive.interactive[jive.selected.ie.type].onDragStop(ev,ui);
                jive.hide();
            }
        });
    },
    show: function(dim){
        !this.jo && this.setElement('#jive_overlay');
        this.jo.css({
            width: dim.w * jive.ui.scaleFactor,
            height: dim.h
        }).draggable('option','helper', function(event) {
                return jQuery('div.jive_drag_label').clone().appendTo('#jive_components').html(jive.i18n.get('column.move.helper')).show();
            });
        this.jo.appendTo('div.jrPage').show();
        this.jo.position({of:jive.selected.jo, my: 'left top', at:'left top',collision:'none'});
    }
};

jive.ui.foobar = {
    jo: null,
    current: null,
    dropMenu: null,
    cache: {},
    menus: {},
    setElement: function(selector){
        this.jo = jQuery(selector);
        this.jo.on('mousedown touchstart','button',function(evt){
            var jo = jQuery(this);
            !jo.hasClass('disabled') && jo.addClass('pressed');
            return false;
        });
        this.jo.on('mouseup touchend','button',function(evt){
            var jo = jQuery(this);
            jo.removeClass('pressed');
            var type = jive.selected.ie.type;
            var fn = jo.attr('fn');

            if(fn && !jo.hasClass('disabled')){
                jive.interactive[type][fn](jive.interactive[type].actions[jo.attr('actionkey')].arg);
            } else {
                if(jo.attr('menu')){
                    var menu = jive.ui.foobar.menus[type][jo.attr('menu')];
                    menu.jo.show().position({
                        of: jQuery(this),
                        my: 'left top',
                        at: 'left bottom',
                        collision: 'none',
                        offset: '0 2px'
                    });
                    jive.ui.foobar.dropMenu = menu;
                }
            }
            return false;
        });
        this.jo.on('mouseover','button',function(){
            jive.ui.foobar.dropMenu && jive.ui.foobar.dropMenu.jo.hide();
            var jo = jQuery(this);
            !jo.hasClass('disabled') && jo.addClass('over');
            if(jo.attr('menu')){
                var menu = jive.ui.foobar.menus[jive.selected.ie.type][jo.attr('menu')];
                menu.jo.show().position({
                    of: jQuery(this),
                    my: 'left top',
                    at: 'left bottom',
                    collision: 'none',
                    offset: '0 2px'
                });
                jive.ui.foobar.dropMenu = menu;
            }
        });
        this.jo.on('mouseout','button',function(){
            jQuery(this).removeClass('over pressed');
        });
        this.cache = {};
        this.menus = {};
        this.current = null;
    },
    show:function(dim){
        !this.jo && this.setElement('#jive_foobar');
        this.render(jive.interactive[jive.selected.ie.type].actions);
        this.jo.find('button').removeClass('over pressed');
        this.jo.appendTo('div.jrPage').show();
        var top = this.jo.outerHeight() - 1;
        this.jo.position({of:jive.selected.jo, my: 'left top', at:'left top', offset:'0 -' + top});
    },
    render: function(actionMap){
        var it = this;
        var tmpl = [
            '<button class="jive_foobar_button" title="',,'" actionkey="',,'" ',
            ,'><span class="wrap"><span class="icon ',,'"></span></span></button>'];

        if(!it.cache[jive.selected.ie.type]){
            it.cache[jive.selected.ie.type] = '';
            var htm;
            jQuery.each(actionMap,function(k,v){
                if(v.actions) {
                    it.menus[jive.selected.ie.type] = it.menus[jive.selected.ie.type] || {};
                    it.createMenu(k, v.label, v.actions);
                }
                tmpl[1] = v.title;
                tmpl[3] = k;
                tmpl[5] = v.fn ? 'fn="'+v.fn+'"' : v.actions ? 'menu="'+k+'"' : '';
                tmpl[7] = v.icon;
                it.cache[jive.selected.ie.type] += tmpl.join('');
            });
        }

        if(it.current != jive.selected.ie.type){
            it.jo.empty();
            it.jo.html(it.cache[jive.selected.ie.type]);
            it.current = jive.selected.ie.type;
        }
    },
    createMenu: function(key, label, items){
        var it = this,
            htm = '<ul class="pmenu" label="'+key+'">';
        jQuery.each(items,function(k,v){
            if(!v.disabled) {
                var attr = v.fn ? 'fn="'+v.fn+'"' : '',
                    label = v.label || k;
                attr += v.arg ? " data-args='"+v.arg+"'" : "";
                htm += '<li class="pmenuitem" key="'+k+'" '+attr+'>'+label+'</li>';
                v.actions && it.createMenu(k, v.label, v.actions);
            }
        });
        htm += '</ul>';
        it.menus[jive.selected.ie.type][key] ={jo:jQuery(htm).appendTo('#jive_menus')};
    },
    reset: function() {
        this.cache = {};
        this.menus = {};
    }
}
jasperreports.events.registerEvent('jive.ui.foobar').trigger();

jive.ui.dialog = {
    jo: null,
    body: null,
    isVisible: false,
    setElement: function(selector){
        var it = this;
        var jo, input;

        it.jo = jQuery(selector);
        it.jo.draggable({handle: 'div.dialogHeader'});
        /*
         * Cache jquery objects
         */
        it.body = jQuery('#jive_dialog div.dialogBody');
        it.title = jQuery('#jive_dialog span.dialogTitle');
        it.tabs = jQuery('#jive_dialog div.tabContainer');

        if(typeof isIE === 'function') {
            isIE() && it.jo.prepend('<div class="msshadow" />');
        }
        /*
         * Set behaviors for form elements
         */
        it.tabs.on('click touchend', '.tab',function(e){
            var jo = jQuery(this),
                activeTabActionCache;

            it.tabs.find('.tab').removeClass('active');
            jo.addClass('active');
            jive.selected.form.onBlur();
            jive.selected.form.jo.hide();
            activeTabActionCache = jive.selected.form.actionDataCache;
            jive.selected.form.actionCache = {};

            jive.selected.form = jive.ui.forms[jo.data('form')];
            jQuery.extend(jive.selected.form.actionDataCache, activeTabActionCache);

            jive.selected.form.onShow();
            jive.selected.form.jo.show();
        });
        it.body.on('click touchend','input, select',function(e){
            var jo = jQuery(this);
            jo.focus();
            if(jo.attr('type') == 'radio') jo.trigger('change').prop('checked',true);
        });
        it.body.on('change','select.wFreeText',function(e){
            var jo = jQuery(this);
            jo.prev().val(jo.val());
        });
        it.body.on('click touchend','.jive_inputbutton',function(){
            jo = jQuery(this);
            input = jive.selected.form.inputs[jo.attr('bname')];
            switch(jo.attr('type')) {
                case "radio":
                    input.set(jo.attr('value'));
                    break;
                case "checkbox":
                    input.toggle();
            }
            input.onClick && input.onClick(jo);
        });
        it.body.on('click touchend','.jive_freeTextButton',function(){
            jo = jQuery(this);
            jo.parent().next().find('input, select').toggle();
        });
        jQuery('#dialogOk, #dialogCancel').bind('click touchend',function(e){
            if(this.className.indexOf('disabled') < 0){
                if(this.id == 'dialogCancel'){
                    jive.active = false;
                    jQuery.event.trigger('jive_inactive');
                } else {
                    jive.selected.form.submit();
                }
                jive.ui.dialog.hide();
            }
        });
    },
    show: function(title, forms, formIndex){
        !this.jo && this.setElement('#jive_dialog');

        if(forms.length > 1) {
            var htm = '';
            var form;
            var fi = formIndex || 0;
            var active;

            jQuery.each(forms,function(i,v){
                form = jive.ui.forms[v];
                active = i == fi ? 'active' : '';
                htm += '<div id="'+form.name+'Tab" data-form="'+form.name+'" class="tab dialog '+active+'"><span>'+form.title+'</span></div>';
                !form.jo && jive.ui.forms.render(form);
                jive.ui.dialog.body.append(form.jo);
            });
            this.tabs.html(htm).show();

            jive.selected.form = jive.ui.forms[forms[fi]];
        } else {
            this.tabs.hide();
            jive.selected.form = jive.ui.forms[forms[0]];
            !jive.selected.form.jo && jive.ui.forms.render(jive.selected.form);
            this.body.append(jive.selected.form.jo);
        }
        jive.selected.form.onShow();
        this.title.html(title);
        jive.ui.pageOverlay && jive.ui.pageOverlay.show();
        jive.selected.form.jo.show();
        this.jo.show().position({of:jQuery(window), at:'center top', my:'center top', offset: '0 ' + (128 + jQuery('div.jrPage').offset().top), collision: 'none'});
        this.isVisible = true;
        jive.hide(null, true);
    },
    hide:function(){
        var it = this;
        var ids = [];
        jive.ui.dialog.jo.hide();
        jive.ui.pageOverlay && jive.ui.pageOverlay.hide();
        it.body.children().each(function(){
            ids.push(this.id.substring(10));
            jQuery(this).appendTo('#jive_forms').hide();
        });

        jQuery('#jive_dropdown .pmenu').hide();

        jQuery.each(ids,function(i,v){
            jive.ui.forms[v].onHide && jive.ui.forms[v].onHide();
        });
    },
    toggleButtons: function() {
        jQuery('#dialogOk, #dialogCancel').toggleClass('disabled');
    }
}

jive.ui.forms = {
    add:function(parms){
        jive.ui.forms[parms.name] = parms;
    },
    render:function(parms){
        var it = jive.ui.forms;
        var style = 'display:none;width:700px;';
        var form = jQuery('<form id="jive_form_'+parms.name+'" action="" method="'+parms.method+'" class="jive_form" style="'+style+'"/>').appendTo('#jive_forms');

        var tb = [];
        var label,pw,colspan,rowspan;
        parms.inputs = {};

        jQuery.each(parms.elements,function(i,table){
            tb.push('<table width="100%">');
            jQuery.each(table,function(i,row){
                tb.push('<tr>');
                jQuery.each(row,function(i,e){
                    label = e.label || '';
                    colspan = e.colspan ? 'colspan="'+e.colspan+'"' : '';
                    rowspan = e.rowspan ? 'rowspan="'+e.rowspan+'"' : '';
                    if(e.type == 'label') {
                        tb.push('<td class="jive_textLabel"><div class="wrapper">'+e.value+'</div></td>');
                    }
                    if(e.type == 'text') {
                        tb.push('<td style="" '+colspan+' '+rowspan+'>');
                        e.label && tb.push('<div class="wrapper">'+e.label+':</div>');
                        tb.push('<div class="wrapper"><input id="'+e.id+'" type="text" name="'+e.id+'" value="'+e.value+'"/></div></td>');
                        parms.inputs[e.id] = {
                            set:function(val) {
                                jQuery('#'+e.id).val(val);
                            },
                            get:function(){
                                return jQuery('#'+e.id).val();
                            }
                        }
                    }
                    if(e.type == 'radio') {
                        tb.push('<td style="" '+colspan+' '+rowspan+'><div class="thick wrapper"><input type="radio" id="'+e.id+e.value+'" name="'+e.id+'" value="'+e.value+'"/><label for="'+e.id+e.value+'" class="jive_inputLabel">'+label+'</label></div></td>');
                        parms.inputs[e.id] = {
                            set:function(val) {
                                jQuery('input[name="'+e.id+'"]').val(val);
                            },
                            get:function(){
                                return jQuery('input[name="'+e.id+'"]').val();
                            }
                        }
                    }
                    if(e.type == 'list') {
                        var size = e.size ? e.size : 1;
                        var isTouch = 'ontouchstart' in document.documentElement ? 'Touch' : '';
                        var showList = ('ontouchstart' in document.documentElement || size == 1) ? '' : 'showList';
                        var wFreeText = e.freeText ? 'wFreeText' : '';

                        var select = ['<select id="'+e.id+'" name="'+e.id+'" class="'+showList+' '+wFreeText+'" size="'+size+'">'];
                        jQuery.each(e.values,function(i,options){
                            select.push('<option value="'+options[0]+'">'+options[1]+'</option>');
                        });
                        if(e.groups){
                            jQuery.each(e.groups,function(i,group){
                                select.push('<optgroup label="' + group.name + '">');
                                jQuery.each(group.values,function(i,options){
                                    select.push('<option value="'+options[0]+'">'+options[1]+'</option>');
                                });
                                select.push('</optgroup>');
                            });
                        }
                        select.push('</select>');
                        tb.push('<td style="" '+colspan+' '+rowspan+'>');
                        e.label && tb.push('<div class="wrapper">' + e.label + ':</div>');
                        tb.push('<div class="wrapper">');
                        e.freeText && tb.push('<input id="'+e.id+'Text" type="text" class="jive_listTextInput'+isTouch+'" name="'+e.id+'Text" value="" />');
                        tb.push(select.join(''));
                        tb.push('</div></td>');

                        parms.inputs[e.id] = {
                            set:function(val) {
                                jQuery('#'+e.id).val(val);
                                e.freeText && jQuery('#'+e.id+'Text').val(val);
                            },
                            get:function(){
                                return e.freeText ? jQuery('#'+e.id+'Text').val() : jQuery('#'+e.id).val();
                            }
                        }
                    }
                    if(e.type == 'buttons') {
                        tb.push('<td style="" '+colspan+' '+rowspan+'><div class="wrapper">'+label+':</div><div class="wrapper"><div class="buttonbar">');
                        jQuery.each(e.items,function(i,v){
                            !parms.inputs[v.id] && form.append('<input type="hidden" name="'+v.id+'" value="" />');

                            tb.push('<div class="jive_inputbutton '+((i==e.items.length-1)?'last':(i==0?'first':''))+' '+ (v.drop ? 'drop' : '') +'" bname="'+v.id+'" value="'+v.value+'" type="'+v.type+'">');
                            v.type == 'color' && tb.push('<div class="colorpick"></div>');
                            v.bIcon && tb.push('<span class="jive_bIcon '+v.bIcon+'"></span>');
                            v.bLabel && tb.push('<span class="jive_bLabel">'+v.bLabel+'</span>');
                            tb.push('</div>');

                            if(v.type === 'dropdown') {
                                parms.inputs[v.id] = {
                                    _idd: v.id,
                                    _options: v.options,
                                    onClick: function() {
                                        jive.interactive[jive.selected.ie.type][v.fn]();
                                    },
                                    showOptions: function() {
                                        var dd = jQuery('#jive_dropdown');
                                        dd.empty();

                                        var htm = '<ul class="pmenu">',
                                            args;
                                        jQuery.each(this._options, function(k, option) {
                                            htm += "<li class='pmenuitem' data-args='{\"val\":\"" + option.value + "\"}' fn='" + option.fn + "'>" + option.label + "</li>";
                                        });
                                        htm += '</ul>';

                                        dd.append(htm);
                                        dd.find('.pmenu').show();
                                        dd.css({width: '120px', height: '100px'});
                                        dd.position({my: 'left top', at: 'left bottom', of: jQuery('div.jive_inputbutton[bname="'+this._idd+'"]'), collision: 'none', offset: '0 -10px'});
                                    },
                                    hideOptions: function () {
                                        jQuery('#jive_dropdown .pmenu').hide();
                                    }
                                }
                            }
                            if(v.type == 'checkbox') {
                                parms.inputs[v.id] = {
                                    selected: false,
                                    set:function() {
                                        jQuery('input[name="'+v.id+'"]').val('true');
                                        jQuery('div.jive_inputbutton[bname="'+v.id+'"]').addClass('selected');
                                        this.selected = true;
                                    },
                                    unset:function() {
                                        jQuery('input[name="'+v.id+'"]').val('false');
                                        jQuery('div.jive_inputbutton[bname="'+v.id+'"]').removeClass('selected');
                                        this.selected = false;
                                    },
                                    toggle:function(){
                                        this.selected ? this.unset() : this.set();
                                    },
                                    get:function(){
                                        return jQuery('input[name="'+v.id+'"]').val() === "true";
                                    },
                                    onClick: function(){
                                        v.fn && jive.interactive[jive.selected.ie.type][v.fn]();
                                    }
                                }
                            }
                            if(v.type == 'radio' && !parms.inputs[v.id]) {
                                parms.inputs[v.id] = {
                                    set:function(val) {
                                        jQuery('input[name="'+v.id+'"]').val(val);
                                        jQuery('div.jive_inputbutton[bname="'+v.id+'"]').removeClass('selected');
                                        jQuery('div.jive_inputbutton[bname="'+v.id+'"][value="'+val+'"]').addClass('selected');
                                    },
                                    get:function(){
                                        return jQuery('input[name="'+v.id+'"]').val();
                                    },
                                    onClick: function(){
                                        v.fn && jive.interactive[jive.selected.ie.type][v.fn]();
                                    }
                                }
                            }
                            if(v.type == 'action'){
                                parms.inputs[v.id] = {
                                    onClick: function(){
                                        jive.interactive[jive.selected.ie.type][v.fn]();
                                    }
                                }
                            }
                            if(v.type == 'color') {
                                parms.inputs[v.id] = {
                                    set:function(val) {
                                        jQuery('input[name="'+v.id+'"]').val(val);
                                        jQuery('div.jive_inputbutton[bname="'+v.id+'"]').find('div.colorpick').css('background-color','#'+val);
                                    },
                                    get:function(){
                                        return jQuery('input[name="'+v.id+'"]').val();
                                    },
                                    onClick:function(jo){
                                        jive.ui.colorpicker.show({
                                            title: v.title,
                                            inputId: v.id,
                                            anchor: jo
                                        });
                                    }
                                }
                            }
                        });
                        tb.push('</div></div></td>');
                    }
                });
                tb.push('</tr>');
            });
            tb.push('</table>');
        });
        form.append(tb.join(''));
        it[parms.name].jo = form;
        it[parms.name].onCreate(form);
    }
}

jive.ui.colorpicker = {
    jo: null,
    selected: null,
    title: '',
    inputId: null,
    setElement: function(){
        var it = this;
        var jo;
        it.jo = jQuery('#jive_colorpicker');
        it.jo.draggable({handle: 'div.dialogHeader'});
        it.jo.on('click touchend','div.colorpick',function(evt){
            //it.selected && it.selected.toggleClass('selected');
            it.selected = jQuery(this).parent().toggleClass('selected');
            jive.selected.form.inputs[it.inputId].set(it.extractHexColor(it.selected.children().eq(0).attr('title')));
            jive.ui.colorpicker.jo.hide();
            jive.ui.modal.hide();
            evt.preventDefault();
        });
    },
    show: function(options) {
        this.title = options.title || 'Pick a color';
        this.inputId = options.inputId;
        !this.jo && this.setElement();
        jive.ui.modal.show(this.jo);
        this.jo.find('h2').html(this.title);
        this.jo.show().position({of:options.anchor, at:'left bottom', my:'left top', offset: '0 0', collision:'none'});
    },
    extractHexColor: function(rgbString) {
        var out = "";
        if (rgbString && rgbString.toLowerCase().indexOf('rgb') !== -1) {
            var tokens = rgbString.split(','),
                i,
                number,
                conv;

            for (i = 0; i < tokens.length; i++) {
                number = parseInt(/\d+/.exec(tokens[i])[0], 10);
                conv = number.toString(16);
                out += (conv.length === 1) ? ('0' + conv) : conv;
            }
        }
        return out;
    }
}

jive.ui.modal = {
    whf: null,
    show: function(whoHasFocus) {
        this.whf = whoHasFocus;
        if(jQuery('#jive_modal').length == 0) {
            jQuery('div.jrPage').parent().append('<div id="jive_modal" style="display:none;position:absolute;z-index:9999;top:0;bottom:0;left:0;right:0;"></div>');
            jQuery('#jive_modal').on('click touchend',function(evt){
                jive.ui.modal.hide();
                evt.preventDefault();
            })
        }
        jive.ui.dialog.toggleButtons();
        jQuery('#jive_modal').show();
    },
    hide: function() {
        jive.ui.modal.whf.hide();
        jive.ui.dialog.toggleButtons();
        jQuery('#jive_modal').hide();
    }
}

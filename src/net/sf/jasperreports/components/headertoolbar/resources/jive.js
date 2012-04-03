jQuery.extend(jive, {
    init:function(settings){
        jQuery.extend(jive,settings);
        /*
         * Init event handlers for viewer. One time event?
         */
        if(!jive.viewerReady){
            jQuery('div#jive_menus').on({
                'click': function(evt){
                    var args = jQuery(this).data('args');
                    jQuery(this).attr('fn') && jive.interactive[jive.selected.ie.type][jQuery(this).attr('fn')](args);
                },
                'mouseover': function(evt){
                    var jo = jQuery(this);
                    var submenu = jo.children('ul');
                    if(submenu.length){
                        submenu.show().position({
                            of: jQuery(this),
                            my: 'left top',
                            at: 'right top'
                        });
                    }
                },
                'mouseout': function(evt){
                    var jo = jQuery(this);
                    jo.children().hide();
                }
            }, '.pmenuitem');
            jive.viewerReady = true;
        }
        jQuery('#jive_overlay').appendTo('body');
        jQuery('#jive_marker').appendTo('body');
        jQuery('#jive_foobar').appendTo('body');
        jQuery('#jive_menus').appendTo('body');
        jQuery('#jive_forms').appendTo('body');
        jQuery('#jive_dialog').appendTo('body');
        jQuery('#jive_colorpicker').appendTo('body');

        jasperreports.global.events.JIVE_INIT.status = 'finished';
        jasperreports.global.processEvent(jasperreports.global.events.JIVE_INIT.name);
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
                var jo = jQuery(this);
                jive.selectInteractiveElement(jo);
            })
        }
        if(o.proxySelector && !jive.selectors[o.proxySelector]) {
            jive.selectors[o.proxySelector] = o.type;
            jQuery('div.jrPage').on('click touchend',o.proxySelector,function(evt){
                var jo = jive.interactive[o.type].getInteractiveElementFromProxy(jQuery(this));
                jive.selectInteractiveElement(jo);
            })
        }
    },
    selectInteractiveElement: function(jo){
        jive.selected = {
            //ie: jive.elements[jo.data('popupId')],
            ie: jive.elements[jo.data('popupid')],
            jo: jo
        };
        var dim = jive.interactive[jive.selected.ie.type].getElementSize();
        jive.ui.overlay.show(dim);
        jive.ui.marker.show(dim);
        jive.ui.foobar.show(dim);
        jive.ui.foobar.dropMenu && jive.ui.foobar.dropMenu.jo.hide();
    },
    hide: function(items){
        if(!items){
            jive.ui.marker.jo && jive.ui.marker.jo.hide();
            jive.ui.overlay.jo && jive.ui.overlay.jo.hide();
            jive.ui.foobar.jo && jive.ui.foobar.jo.hide();
            jive.ui.foobar.dropMenu && jive.ui.foobar.dropMenu.jo.hide();
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
                jive.interactive[jive.selected.ie.type].resize(ui.position.left - jive.ui.overlay.left);
            }
        });
    },
    show: function(dim){
        !this.jo && this.setElement('#jive_marker');
        this.jo.css({
            height: dim.h+'px'
        });
        this.jo.show();
        this.jo.position({of:jive.selected.jo, my: 'left top', at:'right top',collision:'none'});

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
            helper: function(event) {
                return jQuery( "<div class='' style='background:#eee;border:solid 1px #555;padding:8px;'>Drag to new column position.</div>" );
            },
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
            width: dim.w,
            height: dim.h
        });
        this.jo.show();
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
        this.jo.on('click','button',function(evt){
            var jo = jQuery(this);
            var type = jive.selected.ie.type;
            var fn = jo.attr('fn');
            if(fn){
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
        });

    },
    show:function(dim){
        !this.jo && this.setElement('#jive_foobar');
        this.render(jive.interactive[jive.selected.ie.type].actions);
        jive.interactive[jive.selected.ie.type].onToolbarShow();
        this.jo.show();

        var wdiff = dim.w - this.jo.width();
        wdiff > 32 ?
            this.jo.position({of:jive.selected.jo, my: 'left top', at:'left top', offset:'0 -' + this.jo.outerHeight()}) :
            this.jo.position({of:jive.selected.jo, my: 'right top', at:'left top', collision: 'none'});
    },
    render: function(actionMap){
        var it = this;
        var tmpl = [
            '<button class="jive_foobar_button" actionkey="',,'" ',
            ,'><span class="wrap"><span class="icon ',,'"></span></span></button>'];

        if(!it.cache[jive.selected.ie.type]){
            it.cache[jive.selected.ie.type] = '';
            var htm;
            jQuery.each(actionMap,function(k,v){
                if(v.actions) {
                    it.menus[jive.selected.ie.type] = it.menus[jive.selected.ie.type] || {};
                    htm = it.createMenu(k, v.label, v.actions);
                    it.menus[jive.selected.ie.type][k] ={jo:jQuery(htm).appendTo('#jive_menus')};
                }
                tmpl[1] = k;
                tmpl[3] = v.fn ? 'fn="'+v.fn+'"' : v.actions ? 'menu="'+k+'"' : '';
                tmpl[5] = v.icon;
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
        	lbl = label || key,
        	htm = '<ul class="pmenu" label="'+lbl+'">';
        jQuery.each(items,function(k,v){
            if(!v.disabled) {
                var attr = v.fn ? 'fn="'+v.fn+'"' : '',
                	label = v.label || k;
                attr += v.arg ? " data-args='"+v.arg+"'" : "";
                htm += '<li class="pmenuitem" '+attr+'>'+label;
                if(v.actions) {
                    htm += it.createMenu(k, v.label, v.actions);
                }
                htm += '</li>';
            }
        });
        htm += '</ul>';

        return htm;
    }
}

jive.ui.dialog = {
    jo: null,
    body: null,
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
        /*
         * Set behaviors for form elements
         */
        it.tabs.on('click touchend', '.tab',function(e){
            var jo = jQuery(this);
            it.tabs.find('.tab').removeClass('active');
            jo.addClass('active');
            jive.selected.form.jo.hide();
            jive.selected.form = jive.ui.forms[jo.data('form')];
            jive.selected.form.onShow();
            jive.selected.form.jo.show();
        });
        it.body.on('click touchend','input, select',function(e){
            jQuery(this).focus();
        });
        it.body.on('click touchend','.jive_inputbutton',function(){
            jo = jQuery(this);
            input = jive.selected.form.inputs[jo.attr('name')];
            switch(jo.attr('type')) {
                case "radio":
                    input.set(jo.attr('value'));
                    break;
                case "checkbox":
                    input.toggle();
            }
            input.onClick();
        });
        it.body.on('click touchend','.jive_freeTextButton',function(){
            jo = jQuery(this);
            jo.parent().next().find('input, select').toggle();
        });
        it.body.on('click touchend','.colorbar',function(){
            it.jo.hide();
            jive.ui.colorpicker.show(jQuery(this).attr('title'),this.id);
        });
        jQuery('#dialogOk').bind('click touchend',function(e){
            jive.ui.dialog.jo.hide();
            it.body.children().each(function(){
                jQuery(this).appendTo('#jive_forms').hide();
            });
            jive.selected.form.submit();
        });
        jQuery('#dialogCancel').bind('click touchend',function(e){
            jive.ui.dialog.jo.hide();
            it.body.children().each(function(){
                jQuery(this).appendTo('#jive_forms').hide();
            });
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
                !form.jo && jive.ui.forms.create(form);
                jive.ui.dialog.body.append(form.jo);
            });
            this.tabs.html(htm).show();

            jive.selected.form = jive.ui.forms[forms[fi]];
        } else {
            this.tabs.hide();
            jive.selected.form = jive.ui.forms[forms[0]];
            !jive.selected.form.jo && jive.ui.forms.create(jive.selected.form);
            this.body.append(jive.selected.form.jo);
        }
        jive.selected.form.onShow();
        this.title.html(title);
        jive.selected.form.jo.show();
        this.jo.show().position({of:'div.jrPage', at:'center top', my:'center top', offset: '0 128'});
        jive.hide();
    }
}

jive.ui.forms = {
    add:function(parms){
        jive.ui.forms[parms.name] = parms;
    },
    create:function(parms){
        var it = jive.ui.forms;
        var style = 'display:none;width:480px;';
        var form = jQuery('<form id="jive_form_'+parms.name+'" action="" method="'+parms.method+'" class="jive_form" style="'+style+'"/>').appendTo('#jive_forms');
        /*
         * Create input elements from form definition
         */
        var tb = ['<table width="100%">'];
        var label,pw, inputName;
        parms.inputs = {};
        jQuery.each(parms.elements,function(i,e){
            tb.push('<tr>');
            label = e.label || '';
            if(e.type == 'text') {
                tb.push('<td><div class="wrapper">'+label+'</div></td><td><div class="wrapper"><input id="'+e.id+'" type="text" name="'+e.id+'" value="'+e.value+'"/></div></td>');
                parms.inputs[e.id] = {
                    set:function(v) {
                        jQuery('input[name="'+e.id+'"]').val(v);
                    },
                    get:function(){
                        return jQuery('input[name="'+e.id+'"]').val();
                    }
                }
            }
            if(e.type == 'list') {
                var penIcon = '';
                var freeTextInput = '';
                if(e.freeText) {
                    penIcon = '<div class="jive_freeTextButton"><span class="jive_bIcon editIcon"></span></div>';
                    freeTextInput = '<input id="" type="text" name="" value="" style="display:none;" />';
                }
                tb.push('<td>'+penIcon+'<div class="wrapper">'+label+'</div></td><td><div class="wrapper"><select id="'+e.id+'" name="'+e.id+ '">');
                jQuery.each(e.values,function(i,options){
                    tb.push('<option value="'+options[0]+'">'+options[1]+'</option>');
                })
                tb.push('</select>'+freeTextInput+'</div></td>');
                parms.inputs[e.id] = {
                    set:function(v) {
                        jQuery('select[name="'+e.id+'"]').val(v);
                    },
                    get:function(){
                        return jQuery('select[name="'+e.id+'"]').val();
                    }
                }
            }
            if(e.type == 'grouplist') {
            	var penIcon = '',
            		freeTextInput = '',
            		group,
            		groups = {},
            		groupsNo = 0;

            	if(e.freeText) {
            		penIcon = '<div class="jive_freeTextButton"><span class="jive_bIcon editIcon"></span></div>';
            		freeTextInput = '<input id="" type="text" name="" value="" style="display:none;" />';
            	}
            	tb.push('<td>'+penIcon+'<div class="wrapper">'+label+'</div></td><td><div class="wrapper"><select id="'+e.id+'" name="'+e.id+ '">');
            	
            	jQuery.each(e.values,function(i,options){
            		options.length === 3 ? group = options[2] : group = 'Other';
            		if (!groups[group]) {
            			if (groupsNo > 0) {
            				tb.push('</optgroup>');
            			}
            			tb.push('<optgroup label="' + group + '">');
            			groups[group] = 1;
            			groupsNo ++;
            		}
            		tb.push('<option value="'+options[0]+'">'+options[1]+'</option>');
            		if (groupsNo > 0 && i === e.values.length -1) {
            			tb.push('</optgroup>');
            		}
            	})
            	
            	tb.push('</select>'+freeTextInput+'</div></td>');
            	parms.inputs[e.id] = {
            			set:function(v) {
            				jQuery('select[name="'+e.id+'"]').val(v);
            			},
            			get:function(){
            				return jQuery('select[name="'+e.id+'"]').val();
            			}
            	}
            }
            if(e.type == 'buttons') {
                tb.push('<td><div class="wrapper">'+label+'</div></td><td><div class="wrapper"><div class="buttonbar">');
                pw = 100 / e.items.length;
                jQuery.each(e.items,function(i,v){
                    !parms.inputs[v.id] && form.append('<input type="hidden" name="'+v.id+'" value="" />');

                    tb.push('<div class="jive_inputbutton" name="'+v.id+'" value="'+v.value+'" type="'+v.type+'" style="width:'+pw+'%;"><div class="jive_inputbutton_wrapper '+(i==0?'first':'')+'">');
                    v.bIcon && tb.push('<span class="jive_bIcon '+v.bIcon+'"></span>');
                    v.bLabel && tb.push('<span class="jive_bLabel">'+v.bLabel+'</span>');
                    tb.push('</div></div>');

                    if(v.type == 'checkbox') {
                        parms.inputs[v.id] = {
                            selected: false,
                            set:function() {
                                jQuery('input[name="'+v.id+'"]').val('true');
                                jQuery('div.jive_inputbutton[name="'+v.id+'"]').addClass('selected');
                                this.selected = true;
                            },
                            unset:function() {
                                jQuery('input[name="'+v.id+'"]').val('false');
                                jQuery('div.jive_inputbutton[name="'+v.id+'"]').removeClass('selected');
                                this.selected = false;
                            },
                            toggle:function(){
                                this.selected ? this.unset() : this.set();
                            },
                            get:function(){
                                return jQuery('input[name="'+v.id+'"]').val();
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
                                jQuery('div.jive_inputbutton[name="'+v.id+'"]').removeClass('selected');
                                jQuery('div.jive_inputbutton[name="'+v.id+'"][value="'+val+'"]').addClass('selected');
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
                })
                tb.push('</div></div></td>');
            }
            if(e.type == 'color') {
                form.append('<input type="hidden" name="'+e.id+'" value="" />');
                tb.push('<td><div class="wrapper">'+label+'</div></td><td><div class="wrapper"><div id="'+e.id+'" class="colorbar" style="background:#000;" title="'+label+'">&nbsp;</div></div></td>');
                parms.inputs[e.id] = {
                    set:function(v) {
                        jQuery('input[name="'+e.id+'"]').val(v);
                        jQuery('#'+e.id).css('background','#'+v);
                    },
                    get:function(){
                        return jQuery('input[name="'+e.id+'"]').val();
                    }
                }
            }
            if(e.type == 'hidden') {
                form.append('<input type="hidden" name="'+e.id+'" value="'+e.value+'"/>');
            }
            tb.push('</tr>');
        });
        tb.push('</table>');
        form.append(tb.join(''));
        it[parms.name].jo = form;
        it[parms.name].onCreate(form);
    }
}

jive.ui.colorpicker = {
    jo: null,
    selected: null,
    title: '',
    input: null,
    setElement: function(){
        var it = this;
        var jo;
        it.jo = jQuery('#jive_colorpicker');
        it.jo.draggable({handle: 'div.dialogHeader'});
        it.jo.on('click touchend','div.colorpick',function(){
            it.selected && it.selected.toggleClass('selected');
            it.selected = jQuery(this).parent().toggleClass('selected');
        });
        it.jo.on('click touchend','button',function(){
            if(this.innerHTML.indexOf('Select') >= 0) {
//            	jive.selected.form.inputs[it.input].set(it.selected.children().eq(0).attr('hexcolor'));
                jive.selected.form.inputs[it.input].set(it.extractHexColor(it.selected.children().eq(0).attr('title')));
                jive.ui.colorpicker.jo.hide();
                jive.ui.dialog.jo.show();
            }
            if(this.innerHTML.indexOf('Cancel') >= 0) jive.ui.colorpicker.jo.hide() && jive.ui.dialog.jo.show();
        });
    },
    show: function(title,input) {
        this.title = title || 'Pick a color';
        this.input = input;
        !this.jo && this.setElement();
        this.jo.find('h2').html(this.title);
        this.jo.show().position({of:'div.jrPage', at:'center top', my:'center top', offset: '0 128', collision:'none'});
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
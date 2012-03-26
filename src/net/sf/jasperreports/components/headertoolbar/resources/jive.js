var jive = {
    selectors: {},
    elements: {},
    interactive:{},
    ui: {},
    selected: {
        ie: null,  // selected interactive element
        jo: null,  // selected jquery object tied to interactive element
        form: null // selected form defined by interactive element
    },
    selectedform: null,
    viewerReady: false
};

jQuery.extend(jive, {
    init:function(){
        /*
         * Init event handlers on new page
         */
        for(s in jive.selectors) {
            jQuery('div.jrPage').on('click',s,function(evt){
                var jo = jQuery(this);
                jive.selectInteractiveElement(jo);
            })
        }
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
        /*
         * Run init sequence for interactive components if available
         */
        for(ic in jive.interactive){
            if(jive.interactive.hasOwnProperty(ic)) {
                jive.interactive[ic].init && jive.interactive[ic].init();
            }
        }
        /*
         * Create forms requested by interactive components
         */
        jQuery.each(jive.ui.forms.queue,function(i,v){jive.ui.forms.create(v);});
        jive.ui.forms.queue = [];
    },
    initInteractiveElement: function(o){
        jive.elements[o.id] = jQuery.extend({},o);
        if(!jive.selectors[o.selector]) {
            jQuery('div.jrPage').on('click',o.selector,function(evt){
                var jo = jQuery(this);
                jive.selectInteractiveElement(jo);
            })
            jive.selectors[o.selector] = o.type;
        }
    },
    selectInteractiveElement: function(jo){
        jive.selected = {
            ie: jive.elements[jo.attr('data-popupId')],
            jo: jo
        };
        var dim = jive.interactive[jive.selected.ie.type].getElementSize();
        jive.ui.overlay.show(dim);
        jive.ui.marker.show(dim);
        jive.ui.foobar.show(dim);
        jive.ui.foobar.showing && jive.ui.foobar.showing.hide();
    },
    hide: function(items){
        if(!items){
            jive.ui.marker.jo && jive.ui.marker.jo.hide();
            jive.ui.overlay.jo && jive.ui.overlay.jo.hide();
            jive.ui.foobar.jo && jive.ui.foobar.jo.hide();
            jive.ui.foobar.showing && jive.ui.foobar.showing.hide();
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
                return jQuery( "<div class='' style='background:#eee;border:solid 1px #555;padding:8px;'>I'm a custom helper</div>" );
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
    showing: null,
    cache: {},
    menus: {},
    icons: {
        sortasc: {
            url: '/jasperserver-pro/j/column_toolbar_buttons_sprite.png',
            pos:'0 -24px'
        },
        sortdesc: {
            url: '/jasperserver-pro/j/column_toolbar_buttons_sprite.png',
            pos: '0 -48px'
        },
        filter: {
            url: '/jasperserver-pro/j/column_toolbar_buttons_sprite.png',
            pos: '0 -72px'
        },
        format_all: {
            url: '/jasperserver-pro/j/column_toolbar_buttons_sprite.png',
            pos: '0 0'
        },
        format_font: {},
        format_cell: {},
        format_values: {},
        format_border: {}
    },
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
                    menu.show().position({
                        of: jQuery(this),
                        my: 'left top',
                        at: 'left bottom'
                    });
                    jive.ui.foobar.showing = menu;
                }
            }
        });

    },
    show:function(dim){
        !this.jo && this.setElement('#jive_foobar');
        this.render(jive.interactive[jive.selected.ie.type].actions);
        this.jo.show();

        var wdiff = dim.w - this.jo.width();
        wdiff > 32 ?
            this.jo.position({of:jive.selected.jo, my: 'left top', at:'left top', offset:'0 -' + this.jo.outerHeight()}) :
            this.jo.position({of:jive.selected.jo, my: 'right top', at:'left top', collision: 'none'});
    },
    render: function(actionMap){
        var it = this;
        var tmpl = [
            '<button class="button capsule up" actionkey="',,'" ',
            ,'><span class="wrap"><span class="icon ',,'" style="background:url(',,');background-position:',
            ,';"></span></span></button>'];

        if(!it.cache[jive.selected.ie.type]){
            it.cache[jive.selected.ie.type] = '';
            var htm;
            jQuery.each(actionMap,function(k,v){
                if(v.actions) {
                    it.menus[jive.selected.ie.type] = it.menus[jive.selected.ie.type] || {};
                    htm = it.createMenu(k,v.actions);
                    it.menus[jive.selected.ie.type][k] = jQuery(htm).appendTo('#jive_menus');
                }
                tmpl[1] = k;
                tmpl[3] = v.fn ? 'fn="'+v.fn+'"' : v.actions ? 'menu="'+k+'"' : '';
                tmpl[5] = v.icon;
                tmpl[7] = it.icons[v.icon].url;
                tmpl[9] = it.icons[v.icon].pos;
                it.cache[jive.selected.ie.type] += tmpl.join('');
            });
        }

        if(it.current != jive.selected.ie.type){
            it.jo.empty();
            it.jo.html(it.cache[jive.selected.ie.type]);
            it.current = jive.selected.ie.type;
        }
    },
    createMenu: function(name, items){
        var it = this;
        var htm = '<ul class="pmenu" style="display:none;">';
        jQuery.each(items,function(k,v){
            var attr = v.fn ? 'fn="'+v.fn+'"' : '';
            attr += v.arg ? " data-args='"+v.arg+"'" : "";
            htm += '<li class="pmenuitem" '+attr+'>'+k;
            if(v.actions) {
                htm += it.createMenu(k,v.actions);
            }
            htm += '</li>';
        });
        htm += '</ul>';

        return htm;
    }
}

jive.ui.dialog = {
    jo: null,
    setElement: function(selector){
        this.jo = jQuery(selector);
        this.jo.resizable();
        this.jo.draggable();
        jQuery('#dialogOk').bind('click touchend',function(e){
            jive.ui.dialog.jo.hide();
            jive.selected.form.submit();
        });
        jQuery('#dialogCancel').bind('click touchend',function(e){
            jive.ui.dialog.jo.hide();
        });

        jQuery('#dialogBody').on('click touchend','input, select',function(e){
            jQuery(this).focus();
        });
    },
    show: function(formName){
        !this.jo && this.setElement('#jive_dialog');
        jive.selected.form = jive.ui.forms[formName];
        this.jo.find('div.body.oneColumn').append(jive.selected.form.jo.show());
        jive.selected.form.onShow();
        jQuery('h2.dialog_title',this.jo).html(jive.selected.form.title);
        this.jo.show().position({of:'div.jrPage', at:'center top', my:'center top', offset: '0 128'});
        jive.hide();
    },
    setForms: function(forms){

    }
}

jive.ui.forms = {
    queue: [],
    /*
     Builtins
     */
    singleLineText: {},
    singleSelectList: {},
    multiSelectList: {},
    fontFormat: {},
    cellFormat: {},
    numericValueFormat: {},
    colorPicker: {},
    /*
     Functions
     */
    add: function(parms){
        this.queue.push(parms.name);
        this[parms.name] = parms;
    },
    create:function(name){
        var parms = this[name];
        var form = jQuery('<form id="jive_form_'+parms.name+'" action="" method="'+parms.method+'" style="display:none;"/>').appendTo('body');

        jQuery.each(parms.elements,function(i,v){
            if(v.type == 'text') {
                form.append('<div id="'+v.id+'" style="margin-top:12px;text-align:left;">'+v.label+'<br><input type="'+v.type+'" name="'+v.name+'" value="'+v.value+'"/></div>');
            }
            if(v.type == 'hidden') {
                form.append('<input type="'+v.type+'" name="'+v.name+'" value="'+v.value+'"/>');
            }
            if(v.type == 'list') {
                var select = ['<div id="' + v.id + '" class="' + v.cssClass + '" style="text-align:left;position:relative;z-index:9999;">'+ v.label + '<br><select name="'+ v.name + '">'];
                jQuery.each(v.values,function(i,v){
                    select.push('<option value="'+v[0]+'">'+v[1]+'</option>');
                })
                select.push('</select></div>');
                form.append(select.join(''));
            }
        });

        this[name].jo = form;
        this[name].onCreate(form);
    }
}
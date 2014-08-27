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

define(["jquery.ui", "text!jive.sort.vm.css", "text!jive.filterDialog.tmpl"], function($, css, filterDialogTemplate) {

    var InteractiveSort = {
        initialized: false,
        selected: null,
        sortComponents: null,
        operators: null,
        fdc: {}, // cache the filter dialog's controls
        init: function(report) {
            var it = this,
                component, i, ln, sortlinks;

            it.sortComponents = report.components.sort;
            if (!it.initialized) {
                // Setup CSS
                $('head').append('<style id="jive-sort-stylesheet">'+css+'</style>');

                it.setupFilterDialog();
                report.on('beforeAction', function() {
                   it.fdc.filterDialog.hide();
                });

                // disable browser contextual menu when right-clicking
                $(document).bind("contextmenu", function() {
                    return false;
                });

                if (it.sortComponents) {
                    for (i = 0, ln = it.sortComponents.length; i < ln; i++) {
                        if (it.sortComponents[i].config.genericProperties) {
                            it.operators = it.sortComponents[i].config.genericProperties.operators;
                            break;
                        }
                    }
                }

                it.initialized = true;
            }

            sortlinks = $('div.sortlink');

            // iPad events
            if ('createTouch' in document) {
                $('document').bind("touchmove",function(evt){
                    it.touchStartOn = undefined;
                });
                sortlinks.on('click', function(evt){
                    evt.preventDefault();
                    evt.stopPropagation();
                    return false;
                });
                /*
                 * Capture long touch on touchstart. Also prevent default.
                 */
                sortlinks.on("touchstart",function(evt){
                    evt.preventDefault();

                    !evt.isStartData && (it.touchStartOn = {
                        element: evt.target,
                        timeStamp: evt.timeStamp
                    });
                    evt.isStartData = true;
                });
                /*
                 * If long touch do not trigger anchor link.
                 */
                sortlinks.on('touchend', function(evt){
                    evt.preventDefault();

                    if(it.isLongTouch(evt)) {
                        evt.stopPropagation();
                        return false;
                    }
                    component = it.getComponent($(this).data('uuid'));
                    component && component.sort();
                });
                /*
                 * Show filter div on long touch
                 */
                sortlinks.on('touchend', function(evt) {
                    evt.preventDefault();
                    if (it.isLongTouch(evt)) {
                        var touchEvent = evt.changedTouches ? evt.changedTouches[0] : evt.originalEvent.changedTouches[0];
                        it.showFilterDialogForComponent($(this).data('uuid'), touchEvent);
                    }
                });
            } else {
                sortlinks.on('click', function(evt){
                    evt.preventDefault();
                    component = it.getComponent($(this).data('uuid'));
                    component && component.sort();
                });

                // Show filter div when right-clicking the table header
                sortlinks.on('mousedown', function(evt) {
                    if (evt.which == 3) {
                        it.showFilterDialogForComponent($(this).data('uuid'), evt);
                    }
                });
            }
        },
        isLongTouch: function(evt) {
            var it = this;
            if (!it.touchStartOn) return false;
            var isSameElement = evt.target == it.touchStartOn.element;
            var holdTimeStamp = evt.timeStamp - it.touchStartOn.timeStamp;
            return isSameElement && (holdTimeStamp > 400) && !evt.scrollEvent;
        },
        getComponent: function(uuid) {
            var sortComponents = this.sortComponents,
                sortComponent = null, i;
            if (uuid && sortComponents && sortComponents.length>0) {
                for (i = 0; i < sortComponents.length; i ++) {
                    if (uuid === sortComponents[i].config.id) {
                        sortComponent = sortComponents[i];
                        break;
                    }
                }
            }
            return sortComponent;
        },
        setupFilterDialog: function() {
            var it = this,
                container =  $('#jive_sort_component');
            container.length == 0 &&  (container = $('<div id="jive_sort_component"></div>').appendTo('body'));
            container.empty();
            container.append(filterDialogTemplate);

            container.on('click', '.hidefilter', function(){
                $(this).parent().hide();
            });

            it.fdc.filterDialog = container.find('#jrSortComponentFilterDialog');
            it.fdc.operatorTypeSelector = it.fdc.filterDialog.find('select.filterOperatorTypeValueSelector');
            it.fdc.filterStart = it.fdc.filterDialog.find('input.filterValueStart');
            it.fdc.filterEnd = it.fdc.filterDialog.find('input.filterValueEnd');
            it.fdc.filterSubmit = it.fdc.filterDialog.find('input.submitFilter');
            it.fdc.filterClear = it.fdc.filterDialog.find('input.clearFilter');

            it.fdc.operatorTypeSelector.on('change', function() {
                if($(this).val().indexOf('BETWEEN') >= 0){
                    it.fdc.filterEnd.show();
                } else {
                    it.fdc.filterEnd.hide();
                }
            });

            it.fdc.filterSubmit.on('click', function() {
                it.selected.filter(it.getFilterData());
            });
            it.fdc.filterClear.on('click', function() {
                var params = it.getFilterData();
                params.clearFilter = true;
                it.selected.filter(params);
            });

            it.fdc.filterDialog.draggable();
        },
        showFilterDialogForComponent: function(uuid, evt) {
            var it = this,
                filterDialog = it.fdc.filterDialog,
                selector = it.fdc.operatorTypeSelector,
                filterStart = it.fdc.filterStart,
                filterEnd = it.fdc.filterEnd,
                component = it.getComponent(uuid),
                filterType, operators;

            if (component && component.config.isFilterable) {
                it.selected = component;
                filterType = component.config.filterData.filterType.toLocaleLowerCase();
                operators = it.operators[filterType];
                selector.empty();

                // populate selector
                $.each(operators, function(i, option) {
                    selector.append($("<option/>", {
                        value: option.key,
                        text: option.val
                    }));
                });

                // for operators that contain 'BETWEEN' show both inputs
                if (component.config.filterData.filterTypeOperator.indexOf('BETWEEN') >= 0) {
                    filterEnd.show();
                } else {
                    filterEnd.hide();
                }

                // show clear button if filter is applied
                if (component.config.filterData.fieldValueStart.length > 0 || component.config.filterData.fieldValueEnd.length > 0) {
                    it.fdc.filterClear.show();
                } else {
                    it.fdc.filterClear.hide();
                }

                // populate with existing values
                filterStart.val(component.config.filterData.fieldValueStart);
                filterEnd.val(component.config.filterData.fieldValueEnd);
                component.config.filterData.filterTypeOperator && selector.val(component.config.filterData.filterTypeOperator);

                // position and show filter
                filterDialog.css({
                    position: 'absolute',
                    'z-index': 999998,
                    left: (evt.pageX)  + "px",
                    top: (evt.pageY) + "px"
                });
                filterDialog.show();
            } else {
                it.selected = null;
            }
        },
        getFilterData: function() {
            var it = this;
            return {
                tableUuid: it.selected.config.datasetUuid,
                fieldValueStart: it.fdc.filterStart.val(),
                fieldValueEnd: it.fdc.filterEnd.val(),
                filterTypeOperator: it.fdc.operatorTypeSelector.val()
            }
        }
    };

    return InteractiveSort;
});
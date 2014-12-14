/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
define('cv-component',["require"], function(require){
    var cvComponent = function(config) {

        this.config = config;
        this.parent = null;
        this.loader = null;
        this._init();
    };

    cvComponent.prototype = {
        // public API

        // internal API
        _init: function() {

            var it = this;

            // Cleanup the DIV...
            // This is due to a bug in the interactive viewer which
            // inovkes the component twice.
            var element = document.getElementById(it.config.id);
            if (element)
            {
                var currentSvgTags = element.getElementsByTagName("svg");
                if (currentSvgTags.length > 0) { element.removeChild(currentSvgTags[0]); };
            }


            require([it.config.renderer], function(renderer) {  // it.config.renderer
                        renderer(it.config.instanceData);
            });

        }
    }

    return cvComponent;
});

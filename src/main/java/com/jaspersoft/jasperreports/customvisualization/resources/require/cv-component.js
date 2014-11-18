define(["require","jquery"], function(require, $){
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
            
            var requiredModuleConfigs = window["requireJSComponent" + it.config.id]();
            
            // Cleanup the DIV...
            $("#" + it.config.id).empty();
            
            this._init2(requiredModuleConfigs,0,it);
        },  
        
        
        /**
         * Load the next required javascript, if required.
         * At the end invokes the render component...
         * 
         * @param {type} requiredModuleConfigs
         * @param {type} indexToLoad
         * @returns {undefined}
         */
        _init2: function(requiredModuleConfigs, indexToLoad, it)
        {
            if (requiredModuleConfigs.length <= indexToLoad)
            {
                if (window["renderComponent" + it.config.id] != 'undefined')
                {
                    window["renderComponent" + it.config.id](it.config.instanceData);
                }
            }
            else
            {
                    var loadModules = [];
                    var bComponent = this;
            
                    var mConf = requiredModuleConfigs[indexToLoad];
                    if (mConf.name != "")
                    {
                        loadModules.push( mConf.name );
                    }
                    else if (mConf.path != "")
                    {
                        loadModules.push( mConf.path );
                    }
                    
                    require(loadModules,  function () {
                            
                          if (mConf.export != "")
                          {
                                window[mConf.export] = require( (mConf.name != "") ? mConf.name : mConf.path);
                                this[mConf.export] = window[mConf.export];
                          }
                          
                          bComponent._init2(requiredModuleConfigs, indexToLoad+1, it);
                    });  
            }
        }
    
    }
    
    return cvComponent;
});

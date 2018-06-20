({
    optimize: 'none', // Uncomment this property to disable the uglify of the libraries
    baseUrl: '',
    paths: {
			'd3': '../../libraries/d3/d3-5.5.0.min' , 		
			'sparkline': 'sparkline' 		
	}, 
		
	wrap: {
        start: "(function(root){\n\nvar define = root.define;\n\n",
        end: "\n\n}(typeof __visualize__ != 'undefined' ? __visualize__ : (typeof __jrio__ != 'undefined' ? __jrio__ : window)));"
    },
    
    name: "sparkline",
    out: "sparkline.min.js"
})

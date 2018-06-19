({
    optimize: 'none', // Uncomment this property to disable the uglify of the libraries
    baseUrl: '',
    paths: {
            'leaflet': 'leaflet' ,
            'leaflet-providers': 'leaflet-providers',
            'leaflet-omnivore': 'leaflet-omnivore.min',
            'icon': 'icon',
            'leaflet-ajax': 'leaflet-ajax',
			'leafletmarkers': 'leafletmarkers' 		
	}, 
		
	wrap: {
        start: "(function(root){\n\nvar define = root.define;\n\n",
        end: "\n\n}(typeof __visualize__ != 'undefined' ? __visualize__ : (typeof __jrio__ != 'undefined' ? __jrio__ : window)));"
    },
    
    name: "leafletmarkers",
    out: "leafletmarkers.min.js"
})

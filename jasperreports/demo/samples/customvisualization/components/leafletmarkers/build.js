({
    optimize: 'none', // Uncomment this property to disable the uglify of the libraries
    baseUrl: '',
    paths: {
            'leaflet': '../../libraries/leaflet/leaflet' ,
            'leaflet-providers': '../../libraries/leaflet/leaflet-providers',
            'leaflet-omnivore': '../../libraries/leaflet/leaflet-omnivore.min',
            'leaflet-ajax': '../../libraries/leaflet/leaflet-ajax',
            'icon': 'icon',
			'leafletmarkers': 'leafletmarkers' 		
	}, 
		
	wrap: {
        start: "(function(root){\n\nvar define = root.define;\n\n",
        end: "\n\n}(typeof __visualize__ != 'undefined' ? __visualize__ : (typeof __jrio__ != 'undefined' ? __jrio__ : window)));"
    },
    
    name: "leafletmarkers",
    out: "leafletmarkers.min.js"
})

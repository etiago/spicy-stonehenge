//
// halfviz.js
//
// instantiates all the helper classes, sets up the particle system + renderer
// and maintains the canvas/editor splitview
//
(function(){
  
  trace = arbor.etc.trace
  objmerge = arbor.etc.objmerge
  objcopy = arbor.etc.objcopy
  var parse = Parseur().parse

  var HalfViz = function(elt){
    var dom = $(elt)

    sys = arbor.ParticleSystem(2600, 512, 0.5)
    sys.renderer = Renderer("#viewport") // our newly created renderer will have its .init() method called shortly by sys...
    sys.screenPadding(20)
    
    var _canvas = dom.find('#viewport').get(0)
    
    var _updateTimeout = null
    var _current = null // will be the id of the doc if it's been saved before
    var _editing = false // whether to undim the Save menu and prevent navigating away
    var _failures = null
    
    var _text = "";
    
    var that = {
      dashboard:Dashboard("#dashboard", sys),
      io:IO("#editor .io"),
      init:function(){
        $(window).resize(that.resize)
        that.resize()
        that.updateLayout(Math.max(1, $(window).width()-340))

        $(that.io).bind('get', that.getDoc)
        $(that.io).bind('clear', that.newDoc)
        return that
      },
      
      getDoc:function(e){
	  alert(e)
        $.getJSON(e, function(doc){
            
          // modify the graph in the particle system
          $.each(doc.data, function(index, pair){
        	  var ratio = pair.CNT / doc.stats.totalCalls
        	  var red = (ratio * 255).toString(16).substr(0,2)
        	  var green = ((1-ratio) * 255).toString(16).substr(0,2)
        	  _text += "-> {weight: "+pair.CNT+", color: #"+red+green+"00}\n"
        	  _text += pair.CONSUMER_METHOD + "->" + pair.SERVICE_METHOD +"\n"
	          _text += pair.CONSUMER + "--" pair.CONSUMER_METHOD+"\n"
	          _text += pair.SERVICE + "--" pair.SERVICE_METHOD+"\n"
          }) 
	      alert("la")
          sys.parameters({repulsion:0})
          that.updateGraph()
          that.resize()
          _editing = false
        })
        
      },

      newDoc:function(){
        var lorem = "; some example nodes\nhello {color:red, label:HELLO}\nworld {color:orange}\n\n; some edges\nhello -> world {color:yellow}\nfoo -> bar {weight:5}\nbar -> baz {weight:2}"
        	
        	
        $.address.value("")
        that.updateGraph()
        that.resize()
        _editing = false
      },

      updateGraph:function(e){
    	  
        var src_txt = _text
        var network = parse(src_txt)
        $.each(network.nodes, function(nname, ndata){
          if (ndata.label===undefined) ndata.label = nname
        })
        sys.merge(network)
        _updateTimeout = null
      },
      
      resize:function(){        
        var w = $(window).width() - 40
        var x = w
        that.updateLayout(x)
        sys.renderer.redraw()
      },
      
      updateLayout:function(split){
        
      },
      
      
    }
    
    return that.init()    
  }


  $(document).ready(function(){
    var mcp = HalfViz("#halfviz")
    mcp.getDoc("http://localhost:8080/logdump/logdump?timestart=1327928767000")
    //mcp.getDoc("http://atlantis.st.ewi.tudelft.nl/test.json?callback=?")
  })

  
})()
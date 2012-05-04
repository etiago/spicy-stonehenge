 var tl;
 
 function getJSONTimelineData(){
 	/*var data = $.getJSON('timeline.json', function(data) {
							  $.each(data, function(key,val) {
							  	var date = new Date(key);
							  	
							  })
							});
	*/
	return "bo";
 }
 
 var eventSource;
 var bandInfos;
 
 function loadTimeline(jsonData) {
 	   eventSource =  new Timeline.DefaultEventSource();
   bandInfos = [
     Timeline.createBandInfo({
     	eventSource:	eventSource,
         width:          "70%", 
     intervalUnit:   Timeline.DateTime.MONTH, 
     intervalPixels: 200
 	}),
 	Timeline.createBandInfo({
	    eventSource:	eventSource,
	     width:          "30%", 
	         intervalUnit:   Timeline.DateTime.YEAR, 
	         intervalPixels: 200
	     })
	   ];
	   bandInfos[1].syncWith = 0;
	   bandInfos[1].highlight = true;
	   
	   Timeline._Band.prototype.addDecorator = function(decorator) { 
	        this._decorators.push(decorator); 
	        decorator.initialize(this,this._timeline); 
	        decorator.paint(); 
		}; 
		
	   tl = Timeline.create(document.getElementById("slider"), bandInfos);
	   
	      Timeline.loadJSON("temp.json", function(json, url) { eventSource.loadJSON(json, document.location.href); });
	      
	      

 }

 /*var resizeTimerID = null;
 function resizeTimeline() {
     if (resizeTimerID == null) {
         resizeTimerID = window.setTimeout(function() {
             resizeTimerID = null;
             tl.layout();
         }, 500);
     }
 }*/
$(document).ready(function() {
	var inputs = document.getElementById('inputs');
	var elements = inputs.getElementsByTagName("input");
	
	var abbreviation = new Array();
	var number = new Array();
	for (var inputIndex = 0; inputIndex < elements.length; inputIndex++){
		var input = elements[inputIndex];
		var allValues = input.value;
		abbreviation[inputIndex] = allValues.split(";")[0];
		number[inputIndex] = parseInt(allValues.split(";")[1]);
	}
});

$(function () {
    var chart;
    $(document).ready(function() {
    	var inputs = document.getElementById('inputs');
    	var elements = inputs.getElementsByTagName("input");
    	
    	var abbreviation = new Array();
    	var number = new Array();
    	var nameV = new Array();
    	for (var inputIndex = 0; inputIndex < elements.length; inputIndex++){
    		var input = elements[inputIndex];
    		var allValues = input.value;
    		abbreviation[inputIndex] = allValues.split(";")[0];
    		number[inputIndex] = parseFloat(allValues.split(";")[1]);
    		nameV[inputIndex] = allValues.split(";")[2] + ";" + allValues.split(";")[0];
    	}
    	
    	var dataV = new Array();
    	for (var inputIndex = 0; inputIndex < elements.length; inputIndex++){
    		dataV[inputIndex] = [nameV[inputIndex], number[inputIndex]];
    	}

        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: "Parcours d'approfondissement"
            },
            tooltip: {
            	formatter: function() {
                    return '<b>' + this.point.name.split(";")[0] + '</b><br />Elèves : <b>' + this.point.y + '</b><br />' + 'Proportion : <b>' + Math.round(this.point.percentage*100)/100 + '</b>%<br />';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return '<b>'+ this.point.name.split(";")[1] +'</b>: '+ Math.round(this.percentage*100)/100 +' %';
                        }
                    }
                }
            },
        	
            series: [{
                type: 'pie',
                name: 'Percentage',
                data: dataV
            }]
        });
    });
    
});
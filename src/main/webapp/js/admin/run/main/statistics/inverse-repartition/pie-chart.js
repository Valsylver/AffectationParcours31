$(function () {
    $(document).ready(function() {
    	generatePieChart();
    });
    
});

function generatePieChart() {
	var chart;
	var inputs;
	var render;
	var title = 'Choix de ';
	var type = document.getElementById('type').value;
	if (type == '2'){
		title += "parcours";
	}
	else{
		title += "filières";
	}
	title += ' des élèves ayant choisi ';
	title += document.getElementById('spec').value;
	title += ' en choix 1';
	
	var results = document.getElementById('results');
	
	var elements = results.getElementsByTagName("div");
	
	var dataV = new Array();
	for (var inputIndex = 0; inputIndex < elements.length; inputIndex++){
		var inputs = elements[inputIndex].getElementsByTagName("input");
		var value = inputs[0].value + ";";
		for (var inputIndex2 = 1; inputIndex2 < inputs.length; inputIndex2++){
			value += inputs[inputIndex2].value;
			if (inputIndex2 != inputs.length-1){
				value += ";";
			}
		}
		dataV[inputIndex] = [value, inputs.length-1];
	}
	
	Highcharts.setOptions({
		 colors: ['#4571a7', '#aa4543', '#89a54d',  '#717589', '#3c96ae', '#bc9a73', '#92a8cd', '#a47d7b', '#b5ca92', '#5b2c12', '#80699b', '#db843c']
		});

    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: title
        },
        tooltip: {
        	formatter: function() {
        		var splitList = this.point.name.split(";");
        		var studentNames = "";
        		for (var i=2; i<splitList.length; i++){
        			studentNames +="<b>";
        			studentNames += splitList[i];
        			studentNames +="</b>";
        			studentNames += "<br />";
        		}
                return '<b>' + this.point.name.split(";")[0] + "</b><br />Nombre d'élèves : <b>" + this.point.y + '</b><br />' + 'Proportion : <b>' + Math.round(this.point.percentage*100)/100 + '</b>%<br />' + 'Elèves : <br/>' + studentNames;
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
                        return '<b>'+ this.point.name.split(";")[1] +'</b>: '+ this.point.y;
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
}
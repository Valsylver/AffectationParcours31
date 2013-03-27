$(function () {
    $(document).ready(function() {
    	generatePieChart('Ic');
    	generatePieChart('Js');
    });
    
});

function generatePieChart(type) {
	var chart;
	var inputs;
	var render;
	var title;
	if (type == 'Ic'){
		inputs = document.getElementById('inputsIc');
		render = 'containerPieChartIc';
		title = "Parcours d'approfondissement";
	}
	else{
		inputs = document.getElementById('inputsJs');
		render = 'containerPieChartJs';
		title = "Filières métier";
	}
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
	
	Highcharts.setOptions({
		 colors: ['#4571a7', '#aa4543', '#89a54d',  '#717589', '#3c96ae', '#bc9a73', '#92a8cd', '#c4847a', '#b5ca92', '#5b2c12', '#80699b', '#db843c', '#a47d7b', '#cccccc', '#004812', '#161616']
		});

    chart = new Highcharts.Chart({
        chart: {
            renderTo: render,
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: title
        },
        tooltip: {
        	formatter: function() {
                return '<b>' + this.point.name.split(";")[0] + "</b><br />Nombre d'élèves : <b>" + this.point.y + '</b><br />' + 'Proportion : <b>' + Math.round(this.point.percentage*100)/100 + '</b>%<br />';
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
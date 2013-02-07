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
	
	//Highcharts.setOptions({
	//	 colors: ['#f49d55', '#9982b4', '#c35f5b', '#5d8bc0', '#a2be67', '#55afc7', '#cee3ab', '#bd9695', '#FF9655', '#FFF263', '#6AF9C4']
	//	});

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
}
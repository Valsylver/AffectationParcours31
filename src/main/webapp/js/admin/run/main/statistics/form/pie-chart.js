$(function () {
    $(document).ready(function() {
    	generatePieChart();
    });
    
});

function generatePieChart() {	
	var noSubmission = parseInt(document.getElementById('noSubmission').value);
	var partialSubmission = parseInt(document.getElementById('partialSubmission').value);
	var totalSubmission = parseInt(document.getElementById('totalSubmission').value);
	
	var dataV = [['Dossiers vides', noSubmission], ['Dossiers incomplets', partialSubmission], ['Dossiers complets', totalSubmission]];
	
	Highcharts.setOptions({
		 colors: ['#3c96ae', '#717589', '#bc9a73']
		});
    
    var chart;
    $(document).ready(function() {
    	chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: 'Avancement des dossiers'
            },
            tooltip: {
            	formatter: function() {
                    return '<b>' + this.point.name + "</b><br />Nombre d'élèves : <b>" + this.point.y + '</b><br />' + 'Proportion : <b>' + Math.round(this.point.percentage*100)/100 + '</b>%<br />';
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
                            return '<b>'+ this.point.name +'</b>: '+ Math.round(this.percentage*100)/100 +' %';
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
}
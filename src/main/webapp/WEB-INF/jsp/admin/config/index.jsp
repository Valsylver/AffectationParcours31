<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">

<link href="/css/dot-luv/jquery-ui-1.9.2.custom.css" rel="stylesheet">
<link href="/css/date-time-picker/jquery-ui-timepicker-addon.css" rel="stylesheet">
<script>
	$(function() {
		$.datepicker.setDefaults($.extend($.datepicker.regional["fr"]));
		$("#datepicker").datepicker();
	});
</script>
<script src="/js/jquery/jquery-1.8.3.js"></script>
<script src="/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<script src="/js/date-time-picker/jquery-ui-timepicker-addon.js"></script>
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=false%>" />


		<form:form action="/admin/config/process-config-saving" method="POST" commandName="when">
			<center>
				<button class="btn btn-success btn-large" name="commit" type="submit">
					<i class="icon-white icon-play"></i><br /> Lancer le processus
				</button>
				<br />
			</center>
			<hr />

			<div class="row">
				<div class="span4">
					<h2>
						Parcours <a href="/admin/config/new/improvement-course" class="btn btn-info"><i class="icon-white icon-plus"></i></a>
					</h2>
					<br />

					<c:forEach var="pa" items="${paAvailable}">
					${pa.stringForForm}
					<br />
						<a href="/admin/common/edit/ic/${pa.abbreviation}" class="btn btn-primary btn-small pull-right">Modfier</a>
						<br />
						<br />
					</c:forEach>
				</div>
				<div class="span4">
					<h2>
						Filières <a href="/admin/config/new/job-sector" class="btn btn-info"><i class="icon-white icon-plus"></i></a>
					</h2>
					<br />

					<c:forEach var="fm" items="${fmAvailable}">
					${fm.stringForForm}
					<br />
						<a href="/admin/common/edit/js/${fm.abbreviation}" class="btn btn-primary btn-small pull-right">Modfier</a>
						<br />
						<br />
					</c:forEach>
				</div>
				<div class="span4">
					<h2>Dates</h2>
					<c:if test="${not empty alertMessage}">
						<div class="alert alert-error">${alertMessage}</div>
					</c:if>
					<label for="firstEmail">Envoi du premier mail de rappel <form:errors path="firstEmail">
							<div class="alert alert-error">La date n'a pas le bon format.</div>
						</form:errors>
					</label>
					<div class="input-append">
						<form:input id="firstEmail" path="firstEmail" class="span2" value="${now}" />
						<span class="add-on">dd/MM/yyyy HH:mm</span>
					</div>
					<br /> <label for="secondEmail">Envoi du second mail de rappel <form:errors path="secondEmail">
							<div class="alert alert-error">La date n'a pas le bon format.</div>
						</form:errors>
					</label>
					<div class="input-append">
						<form:input id="secondEmail" path="secondEmail" class="span2" value="${now}" />
						<span class="add-on">dd/MM/yyyy HH:mm</span>
					</div>
					<br /> <label for="endSubmission">Fin des soumissions <form:errors path="endSubmission">
							<div class="alert alert-error">La date n'a pas le bon format.</div>
						</form:errors>
					</label>
					<div class="input-append">
						<form:input id="endSubmission" path="endSubmission" class="span2" value="${now}" />
						<span class="add-on">dd/MM/yyyy HH:mm</span>
					</div>

					<br /> <label for="endValidation">Fin de la validation par les responsables <form:errors path="endValidation">
							<div class="alert alert-error">La date n'a pas le bon format.</div>
						</form:errors>
					</label>
					<div class="input-append">
						<form:input id="endValidation" path="endValidation" class="span2" value="${now}" />
						<span class="add-on">dd/MM/yyyy HH:mm</span>
					</div>
					<br /> <br />
				</div>
			</div>
		</form:form>
	</div>

</body>
<script src="js/jquery-1.8.3.js"></script>
<script src="js/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript">
	$(function() {
		$('#firstEmail, #secondEmail, #endSubmission, #endValidation')
				.datetimepicker(
						{
							timeFormat : 'HH:mm',
							closeText : 'Fermer',
							prevText : 'Précédent',
							nextText : 'Suivant',
							currentText : 'Aujourd\'hui',
							monthNames : [ 'Janvier', 'Février', 'Mars',
									'Avril', 'Mai', 'Juin', 'Juillet', 'Août',
									'Septembre', 'Octobre', 'Novembre',
									'Décembre' ],
							monthNamesShort : [ 'Janv.', 'Févr.', 'Mars',
									'Avril', 'Mai', 'Juin', 'Juil.', 'Août',
									'Sept.', 'Oct.', 'Nov.', 'Déc.' ],
							dayNames : [ 'Dimanche', 'Lundi', 'Mardi',
									'Mercredi', 'Jeudi', 'Vendredi', 'Samedi' ],
							dayNamesShort : [ 'Dim.', 'Lun.', 'Mar.', 'Mer.',
									'Jeu.', 'Ven.', 'Sam.' ],
							dayNamesMin : [ 'D', 'L', 'M', 'M', 'J', 'V', 'S' ],
							weekHeader : 'Sem.',
							dateFormat : 'dd/mm/yy',
							firstDay : 1,
							isRTL : false,
							showMonthAfterYear : false,
							yearSuffix : '',
							minDate : new Date(),
						});
	});
</script>
</html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/dot-luv/jquery-ui-1.9.2.custom.css" rel="stylesheet">
<link href="/css/date-time-picker/jquery-ui-timepicker-addon.css"
	rel="stylesheet">
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
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="/admin/run/settings/students">Elèves</a></li>
					<li><a href="/admin/run/settings/export">Export</a></li>
					<li class="active"><a href="/admin/run/settings/process">Processus</a></li>
					<li><a href="/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span10">
				<c:if test="${not empty successMessage}">
					<div class="alert alert success">
						<h4>Modifications</h4>
						${successMessage}
					</div>
				</c:if>
				<c:choose>
					<c:when test="${modifyEndValidation}">
						<form:form action="/admin/run/settings/edit-process" method="post"
							commandName="when">
							<c:if test="${not empty alertMessage}">
								<div class="alert alert-block">
									<h4>Dates</h4>
									${alertMessage}
								</div>
							</c:if>
							<c:choose>
								<c:when test="${modifyFirstEmail}">
									<form:input path="number" class="span2" value="4"
										style="display:none" />
									<label for="firstEmail">Envoi du premier mail de rappel
										<form:errors path="firstEmail">
											<br />
											<a style="color: red">La date n'a pas le bon format</a>
										</form:errors>
									</label>
									<div class="input-append">
										<form:input id="firstEmail" path="firstEmail" class="span2"
											value="${firstEmail}" />
										<span class="add-on">dd/MM/yyyy HH:mm</span>
									</div>
									<br />
									<label for="secondEmail">Envoi du second mail de rappel
										<form:errors path="secondEmail">
											<br />
											<a style="color: red">La date n'a pas le bon format</a>
										</form:errors>
									</label>
									<div class="input-append">
										<form:input id="secondEmail" path="secondEmail" class="span2"
											value="${secondEmail}" />
										<span class="add-on">dd/MM/yyyy HH:mm</span>
									</div>
									<br />
									<label for="endSubmission">Envoi du premier mail de
										rappel <form:errors path="endSubmission">
											<br />
											<a style="color: red">La date n'a pas le bon format</a>
										</form:errors>
									</label>
									<div class="input-append">
										<form:input id="endSubmission" path="endSubmission"
											class="span2" value="${endSubmission}" />
										<span class="add-on">dd/MM/yyyy HH:mm</span>
									</div>
									<br />
									<label for="endValidation">Fin de la validation par les
										responsables <form:errors path="endValidation">
											<br />
											<a style="color: red">La date n'a pas le bon format</a>
										</form:errors>
									</label>
									<div class="input-append">
										<form:input id="endValidation" path="endValidation"
											class="span2" value="${endValidation}" />
										<span class="add-on">dd/MM/yyyy HH:mm</span>
									</div>
									<br />
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${modifySecondEmail}">
											<form:input path="number" class="span2" value="3"
												style="display:none" />
											<div class="alert alert-block">
												<h4>Envoi du premier mail de rappel</h4>
												Impossible de modifier ce champ. L'évènement est
												déjà passé ou est trop proche.
											</div>
											<label for="secondEmail">Envoi du second mail de
												rappel <form:errors path="secondEmail">
													<br />
													<a style="color: red">La date n'a pas le bon format</a>
												</form:errors>
											</label>
											<div class="input-append">
												<form:input id="secondEmail" path="secondEmail"
													class="span2" value="${secondEmail}" />
												<span class="add-on">dd/MM/yyyy HH:mm</span>
											</div>
											<br />
											<label for="endSubmission">Fin des soumissions <form:errors path="endSubmission">
													<br />
													<a style="color: red">La date n'a pas le bon format</a>
												</form:errors>
											</label>
											<div class="input-append">
												<form:input id="endSubmission" path="endSubmission"
													class="span2" value="${endSubmission}" />
												<span class="add-on">dd/MM/yyyy HH:mm</span>
											</div>
											<br />
											<label for="endValidation">Fin de la validation par
												les responsables <form:errors path="endValidation">
													<br />
													<a style="color: red">La date n'a pas le bon format</a>
												</form:errors>
											</label>
											<div class="input-append">
												<form:input id="endValidation" path="endValidation"
													class="span2" value="${endValidation}" />
												<span class="add-on">dd/MM/yyyy HH:mm</span>
											</div>
											<br />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${modifyEndSubmission}">
													<form:input path="number" class="span2" value="2"
														style="display:none" />
													<div class="alert alert-block">
														<h4>Envoi du premier mail de rappel</h4>
														Impossible de modifier ce champ. L'évènement est
														déjà passé ou est trop proche.
													</div>
													<div class="alert alert-block">
														<h4>Envoi du second mail de rappel</h4>
														Impossible de modifier ce champ. L'évènement est
														déjà passé ou est trop proche.
													</div>
													<label for="endSubmission">Fin des soumissions <form:errors
															path="endSubmission">
															<br />
															<a style="color: red">La date n'a pas le bon format</a>
														</form:errors>
													</label>
													<div class="input-append">
														<form:input id="endSubmission" path="endSubmission"
															class="span2" value="${endSubmission}" />
														<span class="add-on">dd/MM/yyyy HH:mm</span>
													</div>
													<br />
													<label for="endValidation">Fin de la validation par
														les responsables <form:errors path="endValidation">
															<br />
															<a style="color: red">La date n'a pas le bon format</a>
														</form:errors>
													</label>
													<div class="input-append">
														<form:input id="endValidation" path="endValidation"
															class="span2" value="${endValidation}" />
														<span class="add-on">dd/MM/yyyy HH:mm</span>
													</div>
													<br />
												</c:when>
												<c:otherwise>
													<form:input path="number" class="span2" value="1"
														style="display:none" />
													<div class="alert alert-block">
														<h4>Envoi du premier mail de rappel</h4>
														Impossible de modifier ce champ. L'évènement est
														déjà passé ou est trop proche.
													</div>
													<div class="alert alert-block">
														<h4>Envoi du second mail de rappel</h4>
														Impossible de modifier ce champ. L'évènement est
														déjà passé ou est trop proche.
													</div>
													<div class="alert alert-block">
														<h4>Fin des soumissions</h4>
														Impossible de modifier ce champ. L'évènement est
														déjà passé ou est trop proche.
													</div>
													<label for="endValidation">Fin de la validation par
														les responsables <form:errors path="endValidation">
															<br />
															<a style="color: red">La date n'a pas le bon format</a>
														</form:errors>
													</label>
													<div class="input-append">
														<form:input id="endValidation" path="endValidation"
															class="span2" value="${endValidation}" />
														<span class="add-on">dd/MM/yyyy HH:mm</span>
													</div>
													<br />
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<button class="btn btn-primary" name="commit" type="submit">
								<i class="icon-white icon-ok"></i> Sauvegarder les modifications
							</button>
						</form:form>
					</c:when>
					<c:otherwise>
						<div class="alert alert-block">
							<h4>Modification des dates</h4>
							Il est actuellement impossible de modifier les dates
							relatives au processus.
						</div>
					</c:otherwise>
				</c:choose>

				<a href="/admin/run/settings/stop-process" class="btn btn-danger"><i
					class="icon-white icon-stop"></i> Arrêter le processus en cours</a> <br />
			</div>
		</div>
	</div>

</body>
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
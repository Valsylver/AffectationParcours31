<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/admins">Administrateurs</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/students">Elèves</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/export">Export</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail1">Mail 1 <c:choose><c:when test="${mail1Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/mail2">Mail 2 <c:choose><c:when test="${mail2Activated}">[actif]</c:when><c:otherwise>[non actif]</c:otherwise></c:choose></a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/spontaneous-mail">Mail Spontané</a></li>
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/settings/process">Processus</a></li>
					<li><a href="${pageContext.request.contextPath}/admin/run/settings/specializations">Spécialisations</a></li>
				</ul>
			</div>

			<div class="span10">
				<c:if test="${not empty successMessage}">
					<div class="alert alert-error">
						${successMessage}
					</div>
				</c:if>
				<c:choose>
					<c:when test="${modifyEndValidation}">
						<form:form action="${pageContext.request.contextPath}/admin/run/settings/edit-process" method="post"
							commandName="when">
							<c:if test="${not empty alertMessage}">
								<div class="alert alert-error">
									${alertMessage}
								</div>
							</c:if>
							<c:choose>
								<c:when test="${modifyFirstEmail}">
									<form:input path="number" class="span2" value="4"
										style="display:none" />
									<label for="firstEmail">Envoi du premier mail de rappel
										<form:errors path="firstEmail">
											<div class="alert alert-error">La date n'a pas le bon format.</div>
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
											<div class="alert alert-error">La date n'a pas le bon format.</div>
										</form:errors>
									</label>
									<div class="input-append">
										<form:input id="secondEmail" path="secondEmail" class="span2"
											value="${secondEmail}" />
										<span class="add-on">dd/MM/yyyy HH:mm</span>
									</div>
									<br />
									<label for="endSubmission">Fin des soumissions <form:errors path="endSubmission">
											<div class="alert alert-error">La date n'a pas le bon format.</div>
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
											<div class="alert alert-error">La date n'a pas le bon format.</div>
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
													<div class="alert alert-error">La date n'a pas le bon format.</div>
												</form:errors>
											</label>
											<div class="input-append">
												<form:input id="secondEmail" path="secondEmail"
													class="span2" value="${secondEmail}" />
												<span class="add-on">dd/MM/yyyy HH:mm</span>
											</div>
											<br />
											<label for="endSubmission">Fin des soumissions <form:errors path="endSubmission">
													<div class="alert alert-error">La date n'a pas le bon format.</div>
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
													<div class="alert alert-error">La date n'a pas le bon format.</div>
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
															<div class="alert alert-error">La date n'a pas le bon format.</div>
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
															<div class="alert alert-error">La date n'a pas le bon format.</div>
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
															<div class="alert alert-error">La date n'a pas le bon format.</div>
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

				<a href="${pageContext.request.contextPath}/admin/run/settings/stop-process" class="btn btn-danger"><i
					class="icon-white icon-stop"></i> Arrêter le processus en cours</a> <br />
			</div>
		</div>
	</div>
</body>
</html>
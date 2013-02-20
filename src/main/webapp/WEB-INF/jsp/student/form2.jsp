<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filières 3ème année Centrale Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/student/bootstrap-select.css" rel="stylesheet">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/js/student/bootstrap-select.js" type="text/javascript"></script>
<script src="/js/student/dynamic_select.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-inverse"></nav>

		<div class="row">
			<div class="span8 offset2">
				<form:form action="/student/processForm" method="POST" commandName="fullChoice" class="well form-horizontal" enctype="multipart/form-data">
					<legend>
						<h3>Parcours d'approfondissement</h3>
					</legend>
					<c:choose>
						<c:when test="${choiceIc == null}">
							<%
								for (int i = 1; i < 6; i++) {
												String strChPa = "improvementCourseChoice.choice" + i;
												String strAttrPa = "Choix " + i;
												String strIdPa = "selectPa" + i;
							%>
							<label path="<%=strChPa%>"><%=strAttrPa%></label>
							<div class="row-fluid"><form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="selectpicker span10">
								<option></option>
								<c:forEach var="pa" items="${paAvailable}">
									<option>${pa.stringForForm}</option>
								</c:forEach>
							</form:select></div>
							<br />
							<%
								}
							%>
						</c:when>
						<c:otherwise>
							<c:set var="abb1" value="${choiceIc.choice1}" />
							<c:set var="abb2" value="${choiceIc.choice2}" />
							<c:set var="abb3" value="${choiceIc.choice3}" />
							<c:set var="abb4" value="${choiceIc.choice4}" />
							<c:set var="abb5" value="${choiceIc.choice5}" />
							<%
								for (int i = 1; i < 6; i++) {
												String strChPa = "improvementCourseChoice.choice" + i;
												String strAttrPa = "Choix " + i;
												String strIdPa = "selectPa" + i;
							%>
							<c:set var="i" value="<%=i%>" />
							<c:choose>
								<c:when test="${i == 1}">
									<c:set var="abb" value="${abb1}" />
								</c:when>
								<c:when test="${i == 2}">
									<c:set var="abb" value="${abb2}" />
								</c:when>
								<c:when test="${i == 3}">
									<c:set var="abb" value="${abb3}" />
								</c:when>
								<c:when test="${i == 4}">
									<c:set var="abb" value="${abb4}" />
								</c:when>
								<c:when test="${i == 5}">
									<c:set var="abb" value="${abb5}" />
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${abb == null}">
									<label path="<%=strChPa%>"><%=strAttrPa%></label>
									<div class="row-fluid"><form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="selectpicker span10">
										<option></option>
										<c:forEach var="pa" items="${paAvailable}">
											<option disabled>${pa.stringForForm}</option>
										</c:forEach>
									</form:select></div>
									<br />
								</c:when>
								<c:otherwise>
									<label path="<%=strChPa%>"><%=strAttrPa%></label>
									<div class="row-fluid"><form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="selectpicker span10">
										<option></option>
										<c:forEach var="pa" items="${paAvailable}">
											<c:choose>
												<c:when test="${pa.abbreviation == abb}">
													<option selected>${pa.stringForForm}</option>
												</c:when>
												<c:otherwise>
													<option>${pa.stringForForm}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select></div>
									<br />
								</c:otherwise>
							</c:choose>
							<%
								}
							%>
						</c:otherwise>
					</c:choose>
					<br />

					<legend>
						<h3>Filières métier</h3>
					</legend>
					<c:choose>
						<c:when test="${choiceJs == null}">
							<%
								for (int i = 1; i < 6; i++) {
												String strChFm = "jobSectorChoice.choice" + i;
												String strAttrFm = "Choix " + i;
												String strIdFm = "selectFm" + i;
							%>
							<label path="<%=strChFm%>"><%=strAttrFm%></label>
							<div class="row-fluid"><form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="selectpicker span10">
								<option></option>
								<c:forEach var="fm" items="${fmAvailable}">
									<option>${fm.stringForForm}</option>
								</c:forEach>
							</form:select></div>
							<br />
							<%
								}
							%>
						</c:when>
						<c:otherwise>
							<%
								for (int i = 1; i < 6; i++) {
												String strChFm = "jobSectorChoice.choice" + i;
												String strAttrFm = "Choix " + i;
												String strIdFm = "selectFm" + i;
							%>
							<c:set var="i" value="<%=i%>" />
							<c:choose>
								<c:when test="${i == 1}">
									<c:set var="abb" value="${choiceJs.choice1}" />
								</c:when>
								<c:when test="${i == 2}">
									<c:set var="abb" value="${choiceJs.choice2}" />
								</c:when>
								<c:when test="${i == 3}">
									<c:set var="abb" value="${choiceJs.choice3}" />
								</c:when>
								<c:when test="${i == 4}">
									<c:set var="abb" value="${choiceJs.choice4}" />
								</c:when>
								<c:when test="${i == 5}">
									<c:set var="abb" value="${choiceJs.choice5}" />
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${abb == null}">
									<label path="<%=strChFm%>"><%=strAttrFm%></label>
									<div class="row-fluid"><form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="selectpicker span10">
										<option></option>
										<c:forEach var="fm" items="${fmAvailable}">
											<option>${fm.stringForForm}</option>
										</c:forEach>
									</form:select></div>
									<br />
									<br />
								</c:when>
								<c:otherwise>
									<label path="<%=strChFm%>"><%=strAttrFm%></label>
									<div class="row-fluid">
										<form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="selectpicker span10">
											<option></option>
											<c:forEach var="fm" items="${fmAvailable}">
												<c:choose>
													<c:when test="${fm.abbreviation == abb}">
														<option selected>${fm.stringForForm}</option>
													</c:when>
													<c:otherwise>
														<option>${fm.stringForForm}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</form:select>
									</div>
									<br />
								</c:otherwise>
							</c:choose>
							<%
								}
							%>
						</c:otherwise>
					</c:choose>
					<br />

					<legend>
						<h3>Documents</h3>
					</legend>

					<c:choose>
						<c:when test="${!hasFilledResume}">
							<span id="resume2"> <label for="resume"><h4>CV</h4></label> <input id="resume" name="resume" type="file" style="display: none">
								<div class="input-append">
									<input id="resumeInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=resume]').click();">Parcourir...</a>
								</div> <br /> <br />
							</span>
						</c:when>
						<c:otherwise>
							<span id="resume2"> <label><h4>CV</h4></label> Vous avez déjà déposé votre CV, voulez vous supprimer ce fichier pour en ajouter un nouveau ? <a
								class="btn" onclick="changeResume();">Oui</a>
								<div style="display: none">
									<input id="resume" name="resume" type="file" style="display: none">
									<div class="input-append">
										<input id="resumeInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=resume]').click();">Parcourir...</a>
									</div>
								</div>
							</span>
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${!hasFilledLetterIc}">
							<span id="letterIc2"> <label for="letterIc"><h4>Lettre de motivation parcours d'approfondissement</h4></label> <input id="letterIc"
								name="letterIc" type="file" style="display: none">
								<div class="input-append">
									<input id="letterIcInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterIc]').click();">Parcourir...</a>
								</div> <br /> <br />
							</span>
						</c:when>
						<c:otherwise>
							<span id="letterIc2"> <label><h4>Lettre de motivation parcours d'approfondissement</h4></label> Vous avez déjà déposé votre lettre de motivation
								pour le parcours d'approfondissement, voulez vous supprimer ce fichier pour en ajouter un nouveau ? <a class="btn" onclick="changeLetterIc();">Oui</a>
								<div style="display: none">
									<input id="letterIc" name="letterIc" type="file" style="display: none">
									<div class="input-append">
										<input id="letterIcInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterIc]').click();">Parcourir...</a>
									</div>
								</div>
							</span>
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${!hasFilledLetterJs}">
							<span id="letterJs2"> <label for="letterJs"><h4>Lettre de motivation filière métier</h4></label> <input id="letterJs" name="letterJs" type="file"
								style="display: none">
								<div class="input-append">
									<input id="letterJsInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterJs]').click();">Parcourir...</a>
								</div> <br /> <br />
							</span>
						</c:when>
						<c:otherwise>
							<span id="letterJs2"> <label><h4>Lettre de motivation filière métier</h4></label> Vous avez déjà déposé votre lettre de motivation pour la filière
								métier, voulez vous supprimer ce fichier pour en ajouter un nouveau ? <a class="btn" onclick="changeLetterJs();">Oui</a>
								<div style="display: none">
									<input id="letterJs" name="letterJs" type="file" style="display: none">
									<div class="input-append">
										<input id="letterJsInput" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterJs]').click();">Parcourir...</a>
									</div>
								</div>
							</span>
						</c:otherwise>
					</c:choose>

					<br />
					<br />

					<button class="btn btn-primary pull-right" name="commit" type="submit">
						<i class="icon-white icon-ok"></i> Valider
					</button>
					<br />

				</form:form>

				<br /> <span id="blocResultat"> </span>
			</div>

			<div class="span2">
				<div class="pull right">
					<a href="/logout" class="btn btn-danger"><i class="icon-white icon-off"></i> Deconnexion</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filières 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/select3.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-inverse"></nav>
		<div id="path" style="display:none">${pageContext.request.contextPath}</div>

		<div class="row">
			<div class="span8 offset2">
				<form:form action="${pageContext.request.contextPath}/student/processForm" method="POST" commandName="fullChoice" class="well form-horizontal" enctype="multipart/form-data">
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
							<form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="combobox" style="min-width:550px">
								<option>------------------------------------- Pas de choix -------------------------------------</option>
								<c:forEach var="superPa" items="${paAvailable}">
									<optgroup label="${superPa[0].superIc}">
										<c:forEach var="pa" items="${superPa}">
											<option>${pa.stringForForm}</option>
										</c:forEach>
									</optgroup>
								</c:forEach>
							</form:select>
							<br />
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
									<form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="combobox" style="min-width:550px">
										<option>------------------------------------- Pas de choix -------------------------------------</option>
										<c:forEach var="superPa" items="${paAvailable}">
											<optgroup label="${superPa[0].superIc}">
												<c:forEach var="pa" items="${superPa}">
													<option>${pa.stringForForm}</option>
												</c:forEach>
											</optgroup>
										</c:forEach>
									</form:select>
									<br />
									<br />
								</c:when>
								<c:otherwise>
									<label path="<%=strChPa%>"><%=strAttrPa%></label>
									<form:select id="<%=strIdPa%>" path="<%=strChPa%>" onchange="updatePaSelect(this.value, this.id);" class="combobox" style="min-width:550px">
										<option>------------------------------------- Pas de choix -------------------------------------</option>
										<c:forEach var="superPa" items="${paAvailable}">
											<optgroup label="${superPa[0].superIc}">
												<c:forEach var="pa" items="${superPa}">
													<c:choose>
														<c:when test="${pa.abbreviation == abb}">
															<option selected>${pa.stringForForm}</option>
														</c:when>
														<c:otherwise>
															<option>${pa.stringForForm}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</optgroup>
										</c:forEach>
									</form:select>
									<br />
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
							<form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="combobox" style="min-width:550px">
								<option>------------------------------------- Pas de choix -------------------------------------</option>
								<c:forEach var="fm" items="${fmAvailable}">
									<option>${fm.stringForForm}</option>
								</c:forEach>
							</form:select>
							<br />
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
									<form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="combobox" style="min-width:550px">
										<option>------------------------------------- Pas de choix -------------------------------------</option>
										<c:forEach var="fm" items="${fmAvailable}">
											<option>${fm.stringForForm}</option>
										</c:forEach>
									</form:select>
									<br />
									<br />
								</c:when>
								<c:otherwise>
									<label path="<%=strChFm%>"><%=strAttrFm%></label>
									<form:select id="<%=strIdFm%>" path="<%=strChFm%>" onchange="updateFmSelect(this.value, this.id);" class="combobox" style="min-width:550px">
										<option>------------------------------------- Pas de choix -------------------------------------</option>
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
									<br />
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
						<h3>Documents (fichiers pdf)</h3>
					</legend>

					<label for="resume"><h4>CV</h4></label>
					<c:if test="${not empty resumeError}">
						<div class="alert alert-error">${resumeError}</div>
					</c:if>
					<div id="alertResume"></div>
					<div id="divResume">
						<c:choose>
							<c:when test="${!hasFilledResume}">
								<input id="resume" name="resume" type="file" style="display: none">
								<div class="input-append">
									<input id="resumeInput" onclick="$('input[id=resume]').click();" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=resume]').click();">Parcourir...</a>
								</div>
								<br />
							</c:when>
							<c:otherwise>
							Vous avez déjà déposé ce document. Vous aurez la possibilité d'en ajouter un nouveau si vous le supprimez. 
							<br />
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/filestudent/cv.pdf"><i class="icon-white icon-download-alt"></i> Voir le fichier</a>
								<a class="btn btn-danger" onclick="removeResume()"><i class="icon-white icon-remove"></i> Supprimer</a>
								<input id="resume" name="resume" type="file" style="display: none">
								<br />
							</c:otherwise>
						</c:choose>
					</div>

					<label for="letterIc"><h4>Lettre de motivation parcours</h4></label>
					<c:if test="${not empty letterIcError}">
						<div class="alert alert-error">${letterIcError}</div>
					</c:if>
					<div id="alertLetterIc"></div>
					<div id="divLetterIc">
						<c:choose>
							<c:when test="${!hasFilledLetterIc}">
								<input id="letterIc" name="letterIc" type="file" style="display: none">
								<div class="input-append">
									<input id="letterIcInput" onclick="$('input[id=letterIc]').click();" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterIc]').click();">Parcourir...</a>
								</div>
								<br />
							</c:when>
							<c:otherwise>
							Vous avez déjà déposé ce document. Vous aurez la possibilité d'en ajouter un nouveau si vous le supprimez. 
							<br />
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/filestudent/lettre-parcours.pdf"><i class="icon-white icon-download-alt"></i> Voir le fichier</a>
								<a class="btn btn-danger" onclick="removeLetterIc()"><i class="icon-white icon-remove"></i> Supprimer</a>
								<input id="letterIc" name="letterIc" type="file" style="display: none">
								<br />
							</c:otherwise>
						</c:choose>
					</div>

					<label for="letterJs"><h4>Lettre de motivation filière métier</h4></label>
					<c:if test="${not empty letterJsError}">
						<div class="alert alert-error">${letterJsError}</div>
					</c:if>
					<div id="alertLetterJs"></div>
					<div id="divLetterJs">
						<c:choose>
							<c:when test="${!hasFilledLetterJs}">
								<input id="letterJs" name="letterJs" type="file" style="display: none">
								<div class="input-append">
									<input id="letterJsInput" onclick="$('input[id=letterJs]').click();" type="text" style="min-width: 300px"> <a class="btn" onclick="$('input[id=letterJs]').click();">Parcourir...</a>
								</div>
								<br />
							</c:when>
							<c:otherwise>
							Vous avez déjà déposé ce document. Vous aurez la possibilité d'en ajouter un nouveau si vous le supprimez. 
							<br />
								<a class="btn btn-primary" href="${pageContext.request.contextPath}/filestudent/lettre-filiere.pdf"><i class="icon-white icon-download-alt"></i> Voir le fichier</a>
								<a class="btn btn-danger" onclick="removeLetterJs()"><i class="icon-white icon-remove"></i> Supprimer</a>
								<input id="letterJs" name="letterJs" type="file" style="display: none">
								<br />
							</c:otherwise>
						</c:choose>
					</div>
					<br />

					<button class="btn btn-primary pull-right" name="commit" type="submit">
						<i class="icon-white icon-ok"></i> Valider
					</button>
					<br />

				</form:form>

				<br />
			</div>

			<div class="span2">
				<div class="pull right">
					<a href="${pageContext.request.contextPath}/logout" class="btn btn-danger"><i class="icon-white icon-off"></i> Deconnexion</a>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="${pageContext.request.contextPath}/js/jquery-final/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-typeahead.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/select2.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/common/dynamic_select.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/student/document.js" type="text/javascript"></script>
</html>
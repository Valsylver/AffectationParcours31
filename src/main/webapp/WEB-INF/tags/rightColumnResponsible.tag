<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="specialization" required="true" type="fr.affectation.domain.specialization.Specialization"%>

<ul class="nav nav-list">
	<li class="nav-header">Documents (zip)</li>
	<c:choose>
		<c:when test="${specialization.type == specialization.JOB_SECTOR }">
			<li><a href="${pageContext.request.contextPath}/files/responsible/js/documents-eleves-${specialization.abbreviation}.zip">Tous les fichiers</a></li>
			<li><a href="${pageContext.request.contextPath}/files/responsible/js/documents-eleves-${specialization.abbreviation}-cv.zip">CV</a></li>
			<li><a href="${pageContext.request.contextPath}/files/responsible/js/documents-eleves-${specialization.abbreviation}-lettres.zip">Lettres</a></li>
		</c:when>
		<c:otherwise>
			<li><a href="${pageContext.request.contextPath}/files/responsible/ic/documents-eleves-${specialization.abbreviation}.zip">Tous les fichiers</a></li>
			<li><a href="${pageContext.request.contextPath}/files/responsible/ic/documents-eleves-${specialization.abbreviation}-cv.zip">CV</a></li>
			<li><a href="${pageContext.request.contextPath}/files/responsible/ic/documents-eleves-${specialization.abbreviation}-lettres.zip">Lettres</a></li>
		</c:otherwise>
	</c:choose>
</ul>
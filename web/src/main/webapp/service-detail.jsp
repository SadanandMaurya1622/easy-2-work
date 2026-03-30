<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.easy2work.catalog.ServiceCatalog" %>
<%@ page import="com.easy2work.catalog.ServiceDetail" %>
<%@ page import="java.util.Optional" %>
<%!
  private static String h(String s) {
    if (s == null) {
      return "";
    }
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
  }
%>
<%
  final String c = request.getContextPath();
  ServiceDetail svc = (ServiceDetail) request.getAttribute("svcDetail");
  if (svc == null) {
    String rawId = request.getParameter("id");
    if (rawId == null) {
      rawId = "";
    }
    Optional<ServiceDetail> opt = ServiceCatalog.find(rawId);
    if (opt.isEmpty()) {
      response.sendRedirect(c + "/#services");
      return;
    }
    svc = opt.get();
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= h(svc.getTitle()) %> – Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%= c %>/css/style.css" rel="stylesheet">
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jspf/navbar.jspf" %>

  <main class="service-detail-page container pb-5">
    <p class="mb-3"><a href="<%= c %>/#services" class="text-success text-decoration-none fw-semibold">&larr; Back to services</a></p>

    <div class="service-detail-hero">
      <img src="<%= h(svc.getImageUrl()) %>" alt="" class="service-detail-hero-img" width="1200" height="420" loading="eager">
    </div>

    <header class="service-detail-header mb-4">
      <h1 class="service-detail-title"><%= h(svc.getTitle()) %></h1>
      <p class="service-detail-intro lead mb-3"><%= h(svc.getSummary()) %></p>
      <div class="service-detail-price" role="region" aria-label="Indicative pricing">
        <span class="service-detail-price-label"><%= h(svc.getPriceLabel()) %></span>
        <p class="service-detail-price-detail mb-0"><%= h(svc.getPriceDetail()) %></p>
      </div>
    </header>

    <div class="row g-4 mb-4">
      <div class="col-lg-6">
        <div class="service-detail-card service-detail-card--we h-100">
          <div class="service-detail-card-icon" aria-hidden="true">✓</div>
          <h2 class="service-detail-card-title">What we’ll do for you</h2>
          <p class="service-detail-card-sub">Is service mein hum yeh deliver karte hain</p>
          <ul class="service-detail-list">
<% for (String line : svc.getWeProvide()) { %>
            <li><%= h(line) %></li>
<% } %>
          </ul>
        </div>
      </div>
      <div class="col-lg-6">
        <div class="service-detail-card service-detail-card--you h-100">
          <div class="service-detail-card-icon service-detail-card-icon--you" aria-hidden="true">→</div>
          <h2 class="service-detail-card-title">What we need from your side</h2>
          <p class="service-detail-card-sub">Aapki taraf se yeh tayar / clear hona helpful hai</p>
          <ul class="service-detail-list">
<% for (String line : svc.getFromYou()) { %>
            <li><%= h(line) %></li>
<% } %>
          </ul>
        </div>
      </div>
    </div>

    <div class="service-detail-card service-detail-card--note mb-4">
      <h2 class="service-detail-card-title h5 mb-2">Usually not included</h2>
      <p class="service-detail-card-sub mb-3">Samanya taur par in cheezon mein alag visit, parts ya specialist lag sakta hai</p>
      <ul class="service-detail-list service-detail-list--compact mb-0">
<% for (String line : svc.getNotIncluded()) { %>
        <li><%= h(line) %></li>
<% } %>
      </ul>
    </div>

    <div class="service-detail-flow mb-5">
      <h2 class="service-detail-flow-title">After you book — what happens</h2>
      <p class="service-detail-card-sub text-center mb-4">Booking ke baad flow (step-by-step)</p>
      <ol class="service-detail-steps list-unstyled mb-0">
<% int step = 0; for (String line : svc.getVisitSteps()) { step++; %>
        <li class="service-detail-step">
          <span class="service-detail-step-num"><%= step %></span>
          <span class="service-detail-step-text"><%= h(line) %></span>
        </li>
<% } %>
      </ol>
    </div>

    <div class="service-detail-actions">
      <a href="<%= c %>/book.jsp?serviceType=<%= h(svc.getCode()) %>" class="btn btn-success btn-lg btn-book">Book this service</a>
      <a href="<%= c %>/#faqs" class="btn btn-outline-secondary btn-lg">Questions? See FAQs</a>
    </div>
  </main>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>All Services - Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="<%= c %>/css/style.css?v=20260408-2" rel="stylesheet">
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

<section class="services-section py-5">
  <div class="container">
    <h1 class="section-title text-center">All Services</h1>
    <p class="section-subtitle text-center">Choose from all available services and book instantly.</p>
    <div class="row g-4" id="allServicesGrid">
      <div class="col-12 text-center">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading services...</span>
        </div>
      </div>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

<script>
  (function () {
    function escapeHtml(text) {
      var div = document.createElement('div');
      div.textContent = text == null ? '' : String(text);
      return div.innerHTML;
    }

    function loadAllServices() {
      fetch('<%= c %>/api/services')
        .then(function (r) { return r.json(); })
        .then(function (data) {
          var container = document.getElementById('allServicesGrid');
          if (!container) return;

          if (!data || !data.ok || !Array.isArray(data.services) || data.services.length === 0) {
            container.innerHTML = '<div class="col-12 text-center text-muted">No services available right now.</div>';
            return;
          }

          var html = '';
          data.services.forEach(function (s) {
            var title = escapeHtml(s.title || '');
            var summary = escapeHtml(s.summary || '');
            var priceLabel = escapeHtml(s.priceLabel || '');
            var imageUrl = escapeHtml(s.imageUrl || '');
            var code = encodeURIComponent(s.code || '');

            html += '<div class="col-12 col-md-6 col-lg-4">' +
              '<a href="<%= c %>/service?id=' + code + '" class="service-card service-card-with-img service-card-link h-100 d-block">' +
              '<div class="service-card-img" style="background-image: url(\'' + imageUrl + '\')"></div>' +
              '<div class="service-card-body">' +
              '<h5>' + title + '</h5>' +
              '<p class="mb-1 fw-semibold text-primary small">' + priceLabel + '</p>' +
              '<p class="text-muted mb-0 small">' + summary + '</p>' +
              '</div>' +
              '</a>' +
              '</div>';
          });

          container.innerHTML = html;
        })
        .catch(function () {
          var container = document.getElementById('allServicesGrid');
          if (container) {
            container.innerHTML = '<div class="col-12 text-center text-muted">Unable to load services at the moment.</div>';
          }
        });
    }

    document.addEventListener('DOMContentLoaded', loadAllServices);
  })();
</script>
</body>
</html>

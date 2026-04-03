<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin – Bookings</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <div class="container-fluid py-4 px-3 px-md-4">
    <h1 class="h3 mb-1">Bookings (admin)</h1>
    <c:if test="${not empty adminDemoKeyHint}">
      <div class="alert alert-info py-2 small mb-3" role="status">
        <strong>Local demo:</strong> admin key is <code><c:out value="${adminDemoKeyHint}"/></code>.
        In one browser, place a booking via <a href="<%= c %>/book.jsp" class="alert-link">book.jsp</a>,
        then click <strong>Refresh</strong> on this page — both use the same in-memory server on localhost.
      </div>
    </c:if>
    <p class="text-muted small mb-3">
      Data source: <strong><c:out value="${adminSource}"/></strong>.
      “Where” = <strong>service address</strong> the customer typed on the form (pincode / area). This app does not store GPS or visitor IP on the booking row.
    </p>
    <c:choose>
      <c:when test="${empty adminBookings}">
        <div class="alert alert-secondary">No bookings yet.</div>
      </c:when>
      <c:otherwise>
        <p class="small text-muted mb-2">Showing <strong><c:out value="${adminBookingCount}"/></strong> newest (DB capped at 500).</p>
        <div class="table-responsive shadow-sm bg-white rounded">
          <table class="table table-sm table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th>Ref</th>
                <th>Booked at</th>
                <th>Customer</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Service</th>
                <th>Status</th>
                <th>Address (where)</th>
                <th>Notes</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="b" items="${adminBookings}">
                <tr>
                  <td class="text-nowrap fw-semibold">#<c:out value="${b.id}"/></td>
                  <td class="text-nowrap small"><c:out value="${b.bookedAtDisplay}"/></td>
                  <td><c:out value="${b.customerName}"/></td>
                  <td class="text-nowrap"><c:out value="${b.phone}"/></td>
                  <td class="small"><c:out value="${b.email}"/></td>
                  <td class="small">
                    <span class="fw-semibold"><c:out value="${b.serviceTitle}"/></span>
                    <br><span class="text-muted">(<c:out value="${b.serviceType}"/>)</span>
                  </td>
                  <td class="small text-nowrap"><c:out value="${b.status}"/></td>
                  <td class="small" style="max-width: 14rem;"><c:out value="${b.address}"/></td>
                  <td class="small" style="max-width: 12rem;"><c:out value="${b.description}"/></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:otherwise>
    </c:choose>
    <p class="mt-3 small text-muted mb-0">
      <a href="<%= c %>/" class="text-decoration-none">← Site home</a>
      · <a href="<%= c %>/admin/dashboard.jsp" class="text-decoration-none">Admin dashboard</a>
    </p>
  </div>
</body>
</html>

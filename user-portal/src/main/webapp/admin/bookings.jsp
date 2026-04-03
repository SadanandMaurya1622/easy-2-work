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
  <style>
    .status-badge {
      font-size: 0.75rem;
      padding: 0.25rem 0.5rem;
    }
    .status-PENDING { background-color: #ffc107; color: #000; }
    .status-CONFIRMED { background-color: #0dcaf0; color: #000; }
    .status-IN_PROGRESS { background-color: #0d6efd; color: #fff; }
    .status-COMPLETED { background-color: #198754; color: #fff; }
    .status-CANCELLED { background-color: #dc3545; color: #fff; }
  </style>
</head>
<body class="bg-light">
  <div class="container-fluid py-4 px-3 px-md-4">
    <h1 class="h3 mb-1">Bookings (admin)</h1>

    <c:if test="${param.updated != null}">
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        <strong>Success!</strong> Booking #<c:out value="${param.updated}"/> status updated successfully.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </c:if>
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
                <th>Update Status</th>
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
                  <td class="small text-nowrap">
                    <span class="badge status-badge status-<c:out value="${b.status}"/>">
                      <c:out value="${b.status}"/>
                    </span>
                  </td>
                  <td class="text-nowrap">
                    <form method="post" action="<%= c %>/admin/update-status" class="d-inline" onsubmit="return confirm('Update status for booking #<c:out value="${b.id}"/>?');">
                      <input type="hidden" name="bookingId" value="<c:out value="${b.id}"/>"/>
                      <input type="hidden" name="key" value="<c:out value="${param.key}"/>"/>
                      <select name="status" class="form-select form-select-sm" style="width: auto; display: inline-block; min-width: 120px;" onchange="this.form.submit()">
                        <option value="">-- Update --</option>
                        <option value="PENDING" <c:if test="${b.status == 'PENDING'}">selected</c:if>>PENDING</option>
                        <option value="CONFIRMED" <c:if test="${b.status == 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
                        <option value="IN_PROGRESS" <c:if test="${b.status == 'IN_PROGRESS'}">selected</c:if>>IN_PROGRESS</option>
                        <option value="COMPLETED" <c:if test="${b.status == 'COMPLETED'}">selected</c:if>>COMPLETED</option>
                        <option value="CANCELLED" <c:if test="${b.status == 'CANCELLED'}">selected</c:if>>CANCELLED</option>
                      </select>
                    </form>
                  </td>
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
      · <a href="<%= c %>/admin/bookings?key=<c:out value="${param.key}"/>" class="text-decoration-none">🔄 Refresh</a>
    </p>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

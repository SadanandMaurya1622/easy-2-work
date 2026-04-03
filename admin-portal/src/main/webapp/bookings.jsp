<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin – Bookings Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .navbar-admin {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .status-badge {
      font-size: 0.75rem;
      padding: 0.25rem 0.5rem;
    }
    .status-PENDING { background-color: #ffc107; color: #000; }
    .status-CONFIRMED { background-color: #0dcaf0; color: #000; }
    .status-IN_PROGRESS { background-color: #0d6efd; color: #fff; }
    .status-COMPLETED { background-color: #198754; color: #fff; }
    .status-CANCELLED { background-color: #dc3545; color: #fff; }
    .table-container {
      background: white;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body class="bg-light">
  <!-- Navbar -->
  <nav class="navbar navbar-admin navbar-dark">
    <div class="container-fluid">
      <span class="navbar-brand mb-0 h1">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-shield-check me-2" viewBox="0 0 16 16">
          <path d="M5.338 1.59a61.44 61.44 0 0 0-2.837.856.481.481 0 0 0-.328.39c-.554 4.157.726 7.19 2.253 9.188a10.725 10.725 0 0 0 2.287 2.233c.346.244.652.42.893.533.12.057.218.095.293.118a.55.55 0 0 0 .101.025.615.615 0 0 0 .1-.025c.076-.023.174-.061.294-.118.24-.113.547-.29.893-.533a10.726 10.726 0 0 0 2.287-2.233c1.527-1.997 2.807-5.031 2.253-9.188a.48.48 0 0 0-.328-.39c-.651-.213-1.75-.56-2.837-.855C9.552 1.29 8.531 1.067 8 1.067c-.53 0-1.552.223-2.662.524zM5.072.56C6.157.265 7.31 0 8 0s1.843.265 2.928.56c1.11.3 2.229.655 2.887.87a1.54 1.54 0 0 1 1.044 1.262c.596 4.477-.787 7.795-2.465 9.99a11.775 11.775 0 0 1-2.517 2.453 7.159 7.159 0 0 1-1.048.625c-.28.132-.581.24-.829.24s-.548-.108-.829-.24a7.158 7.158 0 0 1-1.048-.625 11.777 11.777 0 0 1-2.517-2.453C1.928 10.487.545 7.169 1.141 2.692A1.54 1.54 0 0 1 2.185 1.43 62.456 62.456 0 0 1 5.072.56z"/>
          <path d="M10.854 5.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 7.793l2.646-2.647a.5.5 0 0 1 .708 0z"/>
        </svg>
        Easy 2 Work Admin Portal
      </span>
      <span class="navbar-text text-white">
        Port: 8081
      </span>
    </div>
  </nav>

  <div class="container-fluid py-4 px-3 px-md-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1 class="h3 mb-0">Bookings Management</h1>
      <div>
        <a href="<%= c %>/bookings?key=<c:out value="${param.key}"/>" class="btn btn-sm btn-outline-primary me-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z"/>
            <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z"/>
          </svg>
          Refresh
        </a>
        <a href="<%= c %>/" class="btn btn-sm btn-secondary">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house" viewBox="0 0 16 16">
            <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.707 1.5ZM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5 5 5Z"/>
          </svg>
          Dashboard
        </a>
      </div>
    </div>

    <c:if test="${param.updated != null}">
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        <strong>Success!</strong> Booking #<c:out value="${param.updated}"/> status updated successfully.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
    </c:if>

    <c:if test="${not empty adminDemoKeyHint}">
      <div class="alert alert-info py-2 small mb-3" role="status">
        <strong>Local Demo Mode:</strong> Admin key is <code><c:out value="${adminDemoKeyHint}"/></code>.
        Test by placing a booking on <a href="http://localhost:8080/book.jsp" target="_blank" class="alert-link">the main site (port 8080)</a>,
        then refresh this page.
      </div>
    </c:if>

    <div class="row mb-3">
      <div class="col-md-12">
        <div class="card">
          <div class="card-body">
            <p class="card-text small text-muted mb-0">
              <strong>Data source:</strong> <c:out value="${adminSource}"/>
              | <strong>Total bookings:</strong> <c:out value="${adminBookingCount}"/>
            </p>
          </div>
        </div>
      </div>
    </div>

    <c:choose>
      <c:when test="${empty adminBookings}">
        <div class="alert alert-secondary">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-inbox me-2" viewBox="0 0 16 16">
            <path d="M4.98 4a.5.5 0 0 0-.39.188L1.54 8H6a.5.5 0 0 1 .5.5 1.5 1.5 0 1 0 3 0A.5.5 0 0 1 10 8h4.46l-3.05-3.812A.5.5 0 0 0 11.02 4H4.98zm9.954 5H10.45a2.5 2.5 0 0 1-4.9 0H1.066l.32 2.562a.5.5 0 0 0 .497.438h12.234a.5.5 0 0 0 .496-.438L14.933 9zM3.809 3.563A1.5 1.5 0 0 1 4.981 3h6.038a1.5 1.5 0 0 1 1.172.563l3.7 4.625a.5.5 0 0 1 .105.374l-.39 3.124A1.5 1.5 0 0 1 14.117 13H1.883a1.5 1.5 0 0 1-1.489-1.314l-.39-3.124a.5.5 0 0 1 .106-.374l3.7-4.625z"/>
          </svg>
          No bookings yet. Bookings will appear here when customers place orders.
        </div>
      </c:when>
      <c:otherwise>
        <div class="table-container">
          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead class="table-light">
                <tr>
                  <th>Ref</th>
                  <th>Date & Time</th>
                  <th>Customer</th>
                  <th>Phone</th>
                  <th>Email</th>
                  <th>Service</th>
                  <th>Status</th>
                  <th>Update Status</th>
                  <th>Address</th>
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
                    <td class="text-nowrap">
                      <span class="badge status-badge status-<c:out value="${b.status}"/>">
                        <c:out value="${b.status}"/>
                      </span>
                    </td>
                    <td class="text-nowrap">
                      <form method="post" action="<%= c %>/update-status" class="d-inline" onsubmit="return confirm('Update status for booking #<c:out value="${b.id}"/>?');">
                        <input type="hidden" name="bookingId" value="<c:out value="${b.id}"/>"/>
                        <input type="hidden" name="key" value="<c:out value="${param.key}"/>"/>
                        <select name="status" class="form-select form-select-sm" style="width: auto; display: inline-block; min-width: 140px;" onchange="this.form.submit()">
                          <option value="">-- Change Status --</option>
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
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

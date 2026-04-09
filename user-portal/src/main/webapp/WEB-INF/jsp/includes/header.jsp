  <nav class="navbar navbar-expand-lg navbar-pronto sticky-top">
    <div class="navbar-pronto-outer">
      <a class="navbar-pronto-mobile-brand d-lg-none" href="<%= c %>/">Easy 2 Work</a>
      <button class="navbar-toggler d-lg-none" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu" aria-label="Menu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse navbar-pronto-pill-wrap" id="navMenu">
        <div class="navbar-pronto-pill">
          <a class="nav-link" href="<%= c %>/#why-us" data-section="why-us">Why us</a>
          <a class="nav-link" href="<%= c %>/services.jsp" data-page="services">Services</a>
          <a class="navbar-brand navbar-pronto-brand" href="<%= c %>/">Easy 2 Work</a>
          <a class="nav-link" href="<%= c %>/reviews.jsp" data-page="reviews">Reviews</a>
          <a class="nav-link nav-link-my-booking" href="<%= c %>/my-bookings" data-page="my-bookings">My Booking</a>
          <a class="nav-link" href="<%= c %>/#how" data-section="how">How it works</a>
        </div>
      </div>
    </div>
  </nav>
  <script>
    // Highlight active navigation link
    document.addEventListener('DOMContentLoaded', function() {
      var currentPath = window.location.pathname;
      var currentHash = window.location.hash;
      var navLinks = document.querySelectorAll('.navbar-pronto-pill .nav-link');

      navLinks.forEach(function(link) {
        // Remove active class from all links first
        link.classList.remove('active');

        // Check if link matches current page
        var linkPath = new URL(link.href).pathname;
        var linkHash = new URL(link.href).hash;

        // For My Booking page
        if (link.dataset.page === 'my-bookings' && currentPath.includes('my-bookings')) {
          link.classList.add('active');
        }
        // For Services page
        else if (link.dataset.page === 'services' && currentPath.includes('services.jsp')) {
          link.classList.add('active');
        }
        // For Reviews page
        else if (link.dataset.page === 'reviews' && currentPath.includes('reviews')) {
          link.classList.add('active');
        }
        // For hash sections on homepage
        else if (linkHash && currentHash === linkHash) {
          link.classList.add('active');
        }
        // For homepage without hash
        else if (!currentHash && linkPath === currentPath && link.dataset.section === 'why-us') {
          link.classList.add('active');
        }
      });

      // Add click handler to set active on click
      navLinks.forEach(function(link) {
        link.addEventListener('click', function() {
          navLinks.forEach(function(l) { l.classList.remove('active'); });
          this.classList.add('active');
        });
      });
    });
  </script>

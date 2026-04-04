<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Easy 2 Work – On-Demand Home Service</title>
  <base href="<%= c %>/">
  <link rel="icon" href="<%= c %>/images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" rel="stylesheet" crossorigin="">
  <link href="<%= c %>/css/style.css" rel="stylesheet">
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

  <!-- Booking Success Modal -->
  <c:if test="${param.booked == '1'}">
    <div class="modal fade" id="bookingSuccessModal" tabindex="-1" aria-labelledby="bookingSuccessModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
          <div class="modal-body text-center p-5">
            <div class="mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="#198754" viewBox="0 0 16 16">
                <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
              </svg>
            </div>
            <h3 class="mb-3 fw-bold text-success">Booking Successful!</h3>
            <p class="mb-2 text-muted">Thank you! Your booking request was received.</p>
            <c:if test="${not empty param.ref}">
              <div class="alert alert-light border mb-4">
                <small class="text-muted d-block">Reference Number</small>
                <h5 class="mb-0 fw-bold text-dark">#<c:out value="${param.ref}"/></h5>
              </div>
            </c:if>
            <p class="small text-muted mb-4">We'll contact you soon to confirm the timing.</p>
            <div class="progress mb-2" style="height: 4px;">
              <div class="progress-bar bg-success" role="progressbar" id="autoCloseProgress" style="width: 100%"></div>
            </div>
            <small class="text-muted">This will close automatically in <span id="countdownTimer">5</span> seconds</small>
          </div>
        </div>
      </div>
    </div>
    <script>
      // Auto-show and auto-hide booking success modal
      document.addEventListener('DOMContentLoaded', function() {
        var modal = new bootstrap.Modal(document.getElementById('bookingSuccessModal'));
        modal.show();

        var countdown = 5;
        var timerElement = document.getElementById('countdownTimer');
        var progressBar = document.getElementById('autoCloseProgress');

        var interval = setInterval(function() {
          countdown--;
          timerElement.textContent = countdown;
          progressBar.style.width = (countdown / 5 * 100) + '%';

          if (countdown <= 0) {
            clearInterval(interval);
            modal.hide();
            // Remove query params from URL
            setTimeout(function() {
              window.history.replaceState({}, document.title, window.location.pathname);
            }, 300);
          }
        }, 1000);
      });
    </script>
  </c:if>

  <section class="hero hero-pronto text-center" style="background-image: url('<%= c %>/images/logo.png');">
    <div class="container hero-pronto-inner position-relative">
      <h1 class="hero-headline hero-headline-pronto animate-fade-in-up">India's quick<br>Home Service App</h1>
      <p class="hero-cities hero-cities-pronto animate-fade-in-up animate-delay-1">Now live in Varanasi & expanding to more cities.</p>
      <div class="hero-play-store animate-fade-in-up animate-delay-2 d-flex flex-wrap justify-content-center gap-3 align-items-center">
        <a href="<%= c %>/book.jsp" class="book-cta-btn">Book a service</a>
        <div class="play-store-btn play-store-btn-disabled" style="cursor: not-allowed; opacity: 0.7;">
          <span class="play-store-icon">
            <svg viewBox="0 0 24 24" width="28" height="28" aria-hidden="true"><path fill="currentColor" d="M3.609 1.814L13.792 12 3.61 22.186a.996.996 0 0 1-.61-.92V2.734a1 1 0 0 1 .609-.92zm10.89 10.893l2.302 2.302-10.937 6.333 8.635-8.635zm3.199-3.198l2.807 1.626a1 1 0 0 1 0 1.73l-2.808 1.626L15.206 12l2.492-2.491zM5.864 2.658L16.8 8.99l-2.302 2.302-8.634-8.634z"/></svg>
          </span>
          <span class="play-store-text">
            <span class="play-store-label">LAUNCHING SOON</span>
            <span class="play-store-name">Mobile App</span>
          </span>
        </div>
      </div>
      <p class="hero-subline hero-subline-pronto animate-fade-in-up animate-delay-3">Your home, professionally cleaned — exactly when you need it.</p>
    </div>
    <div class="hero-phones">
      <div class="phone-mockup phone-mockup-left">
        <div class="phone-frame">
          <div class="phone-notch"></div>
          <div class="phone-screen phone-screen-1">
            <div class="app-header">Easy 2 Work</div>
            <div class="app-label">All services</div>
            <div class="app-tile"><span>⚡ Electrical</span></div>
            <div class="app-tile"><span>❄️ AC</span></div>
            <div class="app-tile"><span>🧺 Laundry</span></div>
            <div class="app-tile"><span>🪟 Window</span></div>
          </div>
        </div>
      </div>
      <div class="phone-mockup phone-mockup-center">
        <div class="phone-frame">
          <div class="phone-notch"></div>
          <div class="phone-screen phone-screen-2">
            <div class="app-header">Book service</div>
            <div class="app-card">
              <strong>Electrical Repair</strong>
              <p>Add to cart</p>
            </div>
            <div class="app-card">
              <strong>AC Servicing</strong>
              <p>Add to cart</p>
            </div>
            <div class="app-cta">Proceed</div>
          </div>
        </div>
      </div>
      <div class="phone-mockup phone-mockup-right">
        <div class="phone-frame">
          <div class="phone-notch"></div>
          <div class="phone-screen phone-screen-3">
            <div class="app-header">Track</div>
            <div class="app-status">Engineer on the way</div>
            <div class="app-map">📍 Your address</div>
            <div class="app-time">~15 mins</div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section id="why-us" class="stats-section">
    <div class="container">
      <h2 class="section-title text-center">On-demand professional home service</h2>
      <p class="section-subtitle text-center">No more planning around your help. Our team of verified professionals are always on time.</p>
      <div class="row g-4 justify-content-center text-center stats-row">
        <div class="col-6 col-md-4">
          <div class="stat-item animate-fade-in-up animate-delay-1">
            <span class="stat-number" id="stat-homes" data-count="1200">0</span><span class="stat-plus">+</span>
            <p class="stat-label">Homes serviced</p>
          </div>
        </div>
        <div class="col-6 col-md-4">
          <div class="stat-item animate-fade-in-up animate-delay-2">
            <span class="stat-number" id="stat-hours" data-count="850">0</span><span class="stat-plus">+</span>
            <p class="stat-label">Hours saved</p>
          </div>
        </div>
        <div class="col-6 col-md-4">
          <div class="stat-item animate-fade-in-up animate-delay-3">
            <span class="stat-number" id="stat-pros" data-count="50">0</span><span class="stat-plus">+</span>
            <p class="stat-label">Verified professionals</p>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section id="services" class="services-section">
    <div class="container">
      <h2 class="section-title text-center">Book trusted cleaning & repair help</h2>
      <p class="section-subtitle text-center">From deep cleans to repairs, we've got you covered.</p>
    </div>
    <div class="services-scroll-fullwidth">
      <div class="services-marquee" id="servicesMarquee">
      <div class="services-scroll">
        <a href="<%= c %>/service?id=ELECTRICAL" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-1">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1621905252507-b35492cc74b4?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Electrical Repair</h5>
            <p class="text-muted mb-0 small">Wiring, fuse, switch and electrical fault resolution.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=AC" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-2">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1631545914464-f152c32b1b2c?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>AC Servicing</h5>
            <p class="text-muted mb-0 small">AC installation, repair and maintenance.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=COOLER" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-3">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Cooler Repair</h5>
            <p class="text-muted mb-0 small">Cooler repair and servicing at home.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=LAUNDRY" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-4">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Laundry</h5>
            <p class="text-muted mb-0 small">Washing, ironing and laundry help at home.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=WINDOW" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-5">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Window cleaning</h5>
            <p class="text-muted mb-0 small">Inside and outside window cleaning.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=UTENSILS" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-6">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Utensils</h5>
            <p class="text-muted mb-0 small">Utensil washing and kitchen cleanup.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=BALCONY" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-7">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Balcony cleaning</h5>
            <p class="text-muted mb-0 small">Balcony sweep, mop and upkeep.</p>
          </div>
        </a>
        <a href="<%= c %>/service?id=BATHROOM" class="service-card service-card-with-img service-card-link animate-fade-in-up animate-delay-8">
          <div class="service-card-img" style="background-image: url('https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=800&q=85')"></div>
          <div class="service-card-body">
            <h5>Bathroom cleaning</h5>
            <p class="text-muted mb-0 small">Bathroom deep clean and sanitisation.</p>
          </div>
        </a>
      </div>
      </div>
    </div>
  </section>

  <section id="how" class="steps-section steps-section-pronto steps-section-one-img">
    <div class="container">
      <p class="steps-section-label text-center">How it works</p>
      <h2 class="section-title text-center">Simple steps to a cleaner home</h2>
      <p class="section-subtitle text-center">Follow these simple steps to get lightning-fast household help.</p>
      <div class="steps-section-img-wrap">
        <img src="<%= c %>/images/steps-section.png" alt="How it works – Step 1 Pick services, Step 2 Add to cart, Step 3 Pay and done" class="steps-section-img">
      </div>
    </div>
  </section>

  <section class="testimonials-section">
    <div class="container">
      <h2 class="section-title text-center">User reviews and feedback</h2>
      <p class="section-subtitle text-center">See how Easy 2 Work has transformed users' experiences through their own words.</p>
      <div class="row g-4">
        <div class="col-md-4">
          <div class="testimonial-card animate-fade-in-up animate-delay-1">
            <p class="testimonial-text">"Quick and professional. The engineer fixed my AC in no time. Very satisfied."</p>
            <div class="testimonial-author">
              <strong>Rahul</strong>
              <span class="text-muted">Varanasi</span>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="testimonial-card animate-fade-in-up animate-delay-2">
            <p class="testimonial-text">"Simple process from booking to completion. No hassle. Would recommend."</p>
            <div class="testimonial-author">
              <strong>Priya</strong>
              <span class="text-muted">Varanasi</span>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="testimonial-card animate-fade-in-up animate-delay-3">
            <p class="testimonial-text">"Reliable and transparent. Got my wiring issue resolved the same day. Great service."</p>
            <div class="testimonial-author">
              <strong>Vikram</strong>
              <span class="text-muted">Varanasi</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section id="faqs" class="faq-section">
    <div class="container">
      <h2 class="section-title section-title-faq text-center">FAQs</h2>
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="accordion accordion-faq" id="accordionFAQ">
            <div class="accordion-item">
              <h3 class="accordion-header">
                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#faq1" aria-expanded="true">What is Easy 2 Work?</button>
              </h3>
              <div id="faq1" class="accordion-collapse collapse show" data-bs-parent="#accordionFAQ">
                <div class="accordion-body">Easy 2 Work is an on-demand home service platform. You can book electrical repair, AC servicing, cooler repair, and appliance repair. A verified engineer accepts your request and gets the job done.</div>
              </div>
            </div>
            <div class="accordion-item">
              <h3 class="accordion-header">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">How do I book a service?</button>
              </h3>
              <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#accordionFAQ">
                <div class="accordion-body">Open <strong>Book a service</strong>, fill in your details, choose the service type, describe what you need, and add your address. Submit the form — you'll get a reference number. We'll contact you by phone or WhatsApp to confirm timing; you can also view your bookings under <strong>My Booking</strong>.</div>
              </div>
            </div>
            <div class="accordion-item">
              <h3 class="accordion-header">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">How can I trust the engineers?</button>
              </h3>
              <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#accordionFAQ">
                <div class="accordion-body">Our engineers are registered and verified by the platform. You can see the assigned engineer's name and track the job status in real time from your dashboard.</div>
              </div>
            </div>
            <div class="accordion-item">
              <h3 class="accordion-header">
                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq4">Where is Easy 2 Work available?</button>
              </h3>
              <div id="faq4" class="accordion-collapse collapse" data-bs-parent="#accordionFAQ">
                <div class="accordion-body">We are currently serving Varanasi and expanding to more cities. Check the website for updates on new areas.</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section class="varanasi-map-section" id="varanasi">
    <div class="varanasi-map-wrap">
      <p class="varanasi-map-text">Easy 2 Work is providing on-demand home service in Varanasi.</p>
      <div id="varanasiMap" class="varanasi-map-api" aria-label="Map of Varanasi"></div>
    </div>
  </section>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

  <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" crossorigin=""></script>
  <script src="<%= c %>/js/easy2work-api.js"></script>
  <script src="<%= c %>/js/site.js"></script>
</body>
</html>

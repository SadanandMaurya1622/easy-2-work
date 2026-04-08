<%@ page contentType="text/html; charset=UTF-8" %>
<% final String c = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Customer Reviews - Easy 2 Work</title>
  <base href="<%= c %>/">
  <link rel="icon" href="images/logo.png" type="image/png">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="css/style.css" rel="stylesheet">
  <style>
    .review-card {
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 1.5rem;
      margin-bottom: 1.5rem;
      background: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }
    .stars {
      color: #FFB700;
      font-size: 1.2rem;
      margin-bottom: 0.5rem;
    }
    .review-meta {
      color: #666;
      font-size: 0.9rem;
      margin-bottom: 1rem;
    }
    .review-comment {
      color: #333;
      line-height: 1.6;
      overflow: hidden;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      line-clamp: 3;
      word-break: break-word;
    }
    .submit-review-btn {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      padding: 0.8rem 2rem;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s;
    }
    .submit-review-btn:hover {
      transform: translateY(-2px);
    }
    .star-rating {
      font-size: 2rem;
      cursor: pointer;
      color: #ddd;
    }
    .star-rating .star.active {
      color: #FFB700;
    }
    .average-rating {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 2rem;
      border-radius: 12px;
      text-align: center;
      margin-bottom: 2rem;
    }
    .average-rating h2 {
      font-size: 3rem;
      margin: 0;
    }
  </style>
</head>
<body data-ctx="<%= c %>">
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>

  <div class="container my-5">
    <div class="row">
      <div class="col-lg-8 mx-auto">
        <h1 class="text-center mb-4">Customer Reviews</h1>

        <div id="averageRating" class="average-rating">
          <div>Average Rating</div>
          <h2 id="avgRatingValue">--</h2>
          <div id="avgStars" class="stars"></div>
          <div id="totalReviews">Loading...</div>
        </div>

        <div class="text-center mb-4">
          <button class="submit-review-btn" onclick="showReviewForm()">
            ✍️ Write a Review
          </button>
        </div>

        <!-- Review Submission Form (Hidden by default) -->
        <div id="reviewFormContainer" class="card mb-4" style="display: none;">
          <div class="card-body">
            <h4 class="mb-3">Share Your Experience</h4>
            <form id="reviewForm" onsubmit="submitReview(event)">
              <div class="mb-3">
                <label class="form-label">Your Name *</label>
                <input type="text" id="customerName" class="form-control" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Service Type *</label>
                <select id="serviceType" class="form-control" required>
                  <option value="">Select Service</option>
                  <option value="AC">AC Servicing</option>
                  <option value="ELECTRICAL">Electrical Repair</option>
                  <option value="PLUMBING">Plumbing</option>
                  <option value="CLEANING">Deep Cleaning</option>
                  <option value="LAUNDRY">Laundry</option>
                  <option value="CARPENTER">Carpenter</option>
                  <option value="PAINTER">Painter</option>
                  <option value="MOVING">Moving & Packing</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Rating *</label>
                <div class="star-rating" id="starRating">
                  <span class="star" data-rating="1">☆</span>
                  <span class="star" data-rating="2">☆</span>
                  <span class="star" data-rating="3">☆</span>
                  <span class="star" data-rating="4">☆</span>
                  <span class="star" data-rating="5">☆</span>
                </div>
                <input type="hidden" id="rating" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Your Review *</label>
                <textarea id="comment" class="form-control" rows="4" required
                  placeholder="Share your experience with our service..."></textarea>
              </div>
              <button type="submit" class="btn btn-primary">Submit Review</button>
              <button type="button" class="btn btn-secondary" onclick="hideReviewForm()">Cancel</button>
            </form>
          </div>
        </div>

        <!-- Reviews List -->
        <div id="reviewsList">
          <div class="text-center">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <footer class="footer-pronto">
    <div class="container">
      <p class="m-0">© 2025 Easy 2 Work. All rights reserved.</p>
    </div>
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    let selectedRating = 0;
    const apiBase = document.body.dataset.ctx || '';

    // Star rating interaction
    document.querySelectorAll('.star').forEach(star => {
      star.addEventListener('click', function() {
        selectedRating = parseInt(this.dataset.rating);
        document.getElementById('rating').value = selectedRating;
        updateStars();
      });
    });

    function updateStars() {
      document.querySelectorAll('.star').forEach((star, index) => {
        if (index < selectedRating) {
          star.classList.add('active');
          star.textContent = '★';
        } else {
          star.classList.remove('active');
          star.textContent = '☆';
        }
      });
    }

    function showReviewForm() {
      document.getElementById('reviewFormContainer').style.display = 'block';
      document.getElementById('reviewFormContainer').scrollIntoView({ behavior: 'smooth' });
    }

    function hideReviewForm() {
      document.getElementById('reviewFormContainer').style.display = 'none';
      document.getElementById('reviewForm').reset();
      selectedRating = 0;
      updateStars();
    }

    async function submitReview(e) {
      e.preventDefault();

      const data = {
        bookingId: 0,
        customerName: document.getElementById('customerName').value,
        serviceType: document.getElementById('serviceType').value,
        rating: parseInt(document.getElementById('rating').value),
        comment: document.getElementById('comment').value
      };

      try {
        const response = await fetch(apiBase + '/api/reviews', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok && result.ok) {
          alert('Thank you for your review!');
          hideReviewForm();
          loadReviews();
        } else {
          alert('Error: ' + (result.error || 'Could not submit review'));
        }
      } catch (error) {
        alert('Error submitting review. Please try again.');
        console.error(error);
      }
    }

    async function loadReviews() {
      try {
        const response = await fetch(apiBase + '/api/reviews?limit=50');
        const data = await response.json();

        if (data.ok) {
          displayReviews(data.reviews);
          displayAverage(data.averageRating, data.count);
        }
      } catch (error) {
        document.getElementById('reviewsList').innerHTML =
          '<div class="alert alert-danger">Failed to load reviews</div>';
      }
    }

    function displayAverage(avg, count) {
      document.getElementById('avgRatingValue').textContent = avg.toFixed(1);
      const reviewWord = count === 1 ? 'review' : 'reviews';
      document.getElementById('totalReviews').textContent =
        'Based on ' + count + ' ' + reviewWord;

      const stars = Math.round(avg);
      let starsHtml = '';
      for (let i = 0; i < 5; i++) {
        starsHtml += i < stars ? '★' : '☆';
      }
      document.getElementById('avgStars').textContent = starsHtml;
    }

    function displayReviews(reviews) {
      const container = document.getElementById('reviewsList');

      if (reviews.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No reviews yet. Be the first to share your experience!</div>';
        return;
      }

      let html = '';
      reviews.forEach(review => {
        const date = new Date(review.createdAtMillis).toLocaleDateString('en-IN');
        html += '<div class="review-card">' +
          '<div class="stars">' + review.stars + '</div>' +
          '<div class="review-meta">' +
          '<strong>' + review.customerName + '</strong> • ' + review.serviceType + ' • ' + date +
          '</div>' +
          '<div class="review-comment">' + review.comment + '</div>' +
          '</div>';
      });

      container.innerHTML = html;
    }

    // Load reviews on page load
    loadReviews();
  </script>
</body>
</html>

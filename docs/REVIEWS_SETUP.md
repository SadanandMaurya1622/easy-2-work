# Reviews & Feedback Setup Guide

## What's Changed

The reviews/feedback section now displays **real reviews from the API** instead of hardcoded content!

### Homepage Reviews Section
- **Before**: Showed 3 hardcoded reviews (Rahul, Priya, Vikram)
- **After**: Fetches and displays the latest 3 reviews from the API dynamically

### How It Works

1. **Homepage (/)**: Shows the latest 3 reviews from the database
2. **Reviews Page (/reviews.jsp)**: Shows all reviews with ability to add new ones

## Database Setup

### Create the Reviews Table

Run the SQL script: `docs/database/reviews_table.sql`

```sql
CREATE TABLE IF NOT EXISTS service_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    service_type VARCHAR(100) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_service_type (service_type),
    INDEX idx_created_at (created_at DESC)
);
```

## Testing the System

### 1. Add a New Review

Go to http://localhost:8080/reviews.jsp and:
1. Click "Write a Review" button
2. Fill in the form:
   - Your Name
   - Service Type (AC, Electrical, Plumbing, etc.)
   - Rating (1-5 stars)
   - Your Review comment
3. Submit the form

### 2. See Your Review on Homepage

1. Go to homepage: http://localhost:8080/
2. Scroll to "User reviews and feedback" section
3. Your newly added review will appear (latest 3 reviews are shown)

### 3. View All Reviews

- Click "View All Reviews" button on homepage
- Or go directly to http://localhost:8080/reviews.jsp
- See all reviews with average rating

## API Endpoints

The system uses these API endpoints:

- **GET** `/api/reviews?limit=3` - Get latest 3 reviews (for homepage)
- **GET** `/api/reviews` - Get all reviews (for reviews page)
- **POST** `/api/reviews` - Submit a new review

## Features

### Homepage Testimonials Section
- Automatically loads latest 3 reviews from API
- Shows star ratings with each review
- Displays customer name and service type
- Smooth animations
- Shows loading spinner while fetching
- Graceful error handling

### Reviews Page
- View all reviews with average rating
- Filter by service type (optional)
- Add new reviews with star rating selector
- Real-time form validation
- Shows creation date for each review

## Without Database

If you haven't configured a database:
- The app uses **in-memory storage** automatically
- Reviews are stored in server memory
- Reviews will be lost when server restarts
- No SQL setup needed for testing

## With Database

When database is configured:
- Reviews are stored **permanently** in MySQL
- Reviews persist across server restarts
- Better performance for large number of reviews

## Sample Review Data (For Testing)

If you want to add some sample reviews for testing, you can use the API:

```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "bookingId": 0,
    "customerName": "Rahul Kumar",
    "serviceType": "AC",
    "rating": 5,
    "comment": "Quick and professional. The engineer fixed my AC in no time. Very satisfied."
  }'
```

Or use the web form at http://localhost:8080/reviews.jsp

## Troubleshooting

**Reviews not showing on homepage?**
- Check browser console for errors (F12)
- Verify API is responding: http://localhost:8080/api/reviews?limit=3
- Make sure JavaScript is enabled

**Can't submit new reviews?**
- Check all required fields are filled
- Rating must be selected (1-5 stars)
- Comment cannot be empty

**Reviews disappear after server restart?**
- This means database is not configured
- Reviews are stored in-memory only
- Set up MySQL database to persist reviews

## Next Steps

1. Set up MySQL database (if not already done)
2. Run the reviews_table.sql script
3. Test adding a review through the web form
4. Verify it appears on both homepage and reviews page
5. Share the link with users to collect real feedback!

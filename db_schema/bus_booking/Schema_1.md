# Bus Booking Service

## Requirements

**Create a schema for postgres database bus booking system with the requirements below**

1. User registration
2. Bus registration
3. User can search for buses for a particular route for a particular day/time.
4. User can select multiple seats and book.
5. One seat can be booked multiple times in a route based on its availability. For example If a user books a seat from stop 1 to 2, then another user can book it from 2 onwards, and so on.
6. User will get seat map of bus on selecting a bus while choosing seat.

## Solution: DB Schema

### 1. User

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Bus Type

```sql
CREATE TABLE buse_type (
    bus_type_id SERIAL PRIMARY KEY,
    name TEXT,
    seat_count INTEGER NOT NULL,
    layout TEXT,
    air_condition BOOLEAN,
    electric BOOLEAN,
    sleeper BOOLEAN,
    semi_sleeper BOOLEAN
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Seat Details (Based on bus type)

```sql
CREATE TABLE seats (
    seat_id SERIAL PRIMARY KEY,
    bus_type_id INTEGER REFERENCES buse_type(bus_type_id),
    seat_number TEXT NOT NULL,
    row_number INTEGER,
    column_number INTEGER,
    level_number INTEGER,
    seat_type TEXT, -- e.g., Window, Aisle, Middle
    UNIQUE(bus_id, seat_number)
);
```

### 4. Buses

```sql
CREATE TABLE buses (
    bus_id SERIAL PRIMARY KEY,
    registration_number TEXT UNIQUE NOT NULL,
    name TEXT,
    seat_count INTEGER NOT NULL,
    bus_type_id INTEGER REFERENCES bus_type(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5. Routes

```sql
CREATE TABLE routes (
    route_id SERIAL PRIMARY KEY,
    name TEXT
);
```

### 6. Stops

```sql
CREATE TABLE stops (
    stop_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
```

### 7. Route Stop Mapping

```sql
CREATE TABLE route_stops (
    route_stop_id SERIAL PRIMARY KEY,
    route_id INTEGER REFERENCES routes(route_id) ON DELETE CASCADE,
    stop_id INTEGER REFERENCES stops(stop_id),
    stop_order INTEGER NOT NULL,
    arrival_time TIME,
    departure_time TIME
);
```

### 8. Trip

```sql
CREATE TABLE trips (
    trip_id SERIAL PRIMARY KEY,
    bus_id INTEGER REFERENCES buses(bus_id),
    route_id INTEGER REFERENCES routes(route_id),
    travel_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 9. Bookings

```sql
CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    trip_id INTEGER REFERENCES trips(trip_id),
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price NUMERIC
);
```

### 10. Seat Booking

```sql
CREATE TABLE booking_seats (
    booking_seat_id SERIAL PRIMARY KEY,
    booking_id INTEGER REFERENCES bookings(booking_id) ON DELETE CASCADE,
    seat_id INTEGER REFERENCES seats(seat_id),
    from_stop_id INTEGER REFERENCES stops(stop_id),
    to_stop_id INTEGER REFERENCES stops(stop_id),
    CONSTRAINT valid_segment CHECK (from_stop_id <> to_stop_id)
);
```

#### Query to check seat availability

```sql
SELECT s.seat_id
FROM seats s
JOIN trips bs ON bs.bus_id = s.bus_id
WHERE bs.trip_id = :trip_id
AND NOT EXISTS (
    SELECT 1 FROM booking_seats bs2
    JOIN bookings b ON bs2.booking_id = b.booking_id
    WHERE bs2.seat_id = s.seat_id
    AND b.trip_id = :trip_id
    AND (
        (:from_order < (SELECT stop_order FROM route_stops WHERE stop_id = bs2.to_stop_id AND route_id = bs.route_id)) AND
        (:to_order > (SELECT stop_order FROM route_stops WHERE stop_id = bs2.from_stop_id AND route_id = bs.route_id))
    )
);
```

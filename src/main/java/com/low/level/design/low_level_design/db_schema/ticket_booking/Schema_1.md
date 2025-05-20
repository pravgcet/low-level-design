# Ticket Booking Service

## Design database schema(sql) of a ticket booking system with the following requirements

### Requirements

1. User should be able to book one or more tickets.
2. User can search by show with city filter
3. User can search by show_name.(with filters)
4. Show the show details by id.
5. Show all the shows in a theater.
6. A theater can have multiple screens.

## API

- GET: /shows?city={cityName}
- GET: /shows?showName={showName}&city={cityName} // get default cityName from user location
- GET: /shows/{showId}
- POST: /booking
  {
  showId: showId
  seats: SeatMap[]
  }

## Schema

### 1. Users

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Theater

```sql
CREATE TABLE theatres (
    theater_id,
    name TEXT,
    city,
    locality,
    address
);
```

### 3. Screens

```sql
CREATE TABLE screens (
    screen_id,
    theater_id,
    floor,
    name,
    seat_map_id
);
```

### 4. Shows

```sql
CREATE TABLE shows (
   show_id,
   details_id,
   screen_id,
   start_time,
   end_time
);
```

### 5. Show_Details

```sql
CREATE TABLE show_details(
    details_id,
    name,
    languages,
    descripton,
);
```

### 6. Seat Map

```sql
CREATE TABLE seat_map (
    seat_map_id,
    seat_number,
    row,
    col
);
```

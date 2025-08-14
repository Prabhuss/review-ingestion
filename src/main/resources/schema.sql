-- Drop old tables to start fresh (discard existing data)
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS hotel_provider_grades CASCADE;
DROP TABLE IF EXISTS hotel_provider_overall CASCADE;
DROP TABLE IF EXISTS reviewers CASCADE;
DROP TABLE IF EXISTS providers CASCADE;
DROP TABLE IF EXISTS hotels CASCADE;

-- Base tables
CREATE TABLE hotels (
  hotel_id bigint PRIMARY KEY,
  hotel_name text NOT NULL
);

CREATE TABLE providers (
  provider_id integer PRIMARY KEY,
  provider_name text NOT NULL
);

CREATE TABLE reviewers (
  reviewer_id bigserial PRIMARY KEY,
  display_member_name text,
  country_id integer,
  country_name text,
  flag_name text,
  review_group_id integer,
  review_group_name text,
  room_type_id integer,
  room_type_name text,
  length_of_stay integer,
  reviewer_reviewed_count integer,
  is_expert_reviewer boolean,
  is_show_global_icon boolean,
  is_show_reviewed_count boolean
);

-- Reviews table
CREATE TABLE reviews (
  hotel_review_id bigint NOT NULL,
  provider_id integer NOT NULL REFERENCES providers(provider_id),
  hotel_id bigint NOT NULL REFERENCES hotels(hotel_id),
  PRIMARY KEY (hotel_review_id, provider_id),
  reviewer_id bigint NOT NULL REFERENCES reviewers(reviewer_id),
  rating numeric(3,1),
  rating_text text,
  check_in_month_year text,
  review_date timestamptz,
  review_title text,
  review_comments text,
  review_negatives text,
  review_positives text,
  encrypted_review_data text,
  responder_name text,
  response_date_text text,
  response_translate_source text,
  response_text text,
  translate_source text,
  translate_target text,
  is_show_review_response boolean,
  original_title text,
  original_comment text,
  formatted_response_date text,
  review_provider_text text
);

-- Aggregations per hotel/provider
CREATE TABLE hotel_provider_overall (
  id bigserial PRIMARY KEY,
  hotel_id bigint NOT NULL REFERENCES hotels(hotel_id),
  provider_id integer NOT NULL REFERENCES providers(provider_id),
  overall_score numeric(3,1),
  review_count integer
);

CREATE TABLE hotel_provider_grades (
  id bigserial PRIMARY KEY,
  hotel_id bigint NOT NULL REFERENCES hotels(hotel_id),
  provider_id integer NOT NULL REFERENCES providers(provider_id),
  category_name text NOT NULL,
  score numeric(3,1)
);

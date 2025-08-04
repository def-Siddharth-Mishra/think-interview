export interface Customer {
  id: number;
  first_name: string;
  last_name: string;
  email: string;
  age: number;
  gender: string;
  state: string;
  street_address: string;
  postal_code: string;
  city: string;
  country: string;
  latitude: number;
  longitude: number;
  traffic_source: string;
  created_at: string;
  order_count: number;
}

export interface Order {
  order_id: number;
  user_id: number;
  status: string;
  gender: string;
  created_at: string;
  returned_at: string | null;
  shipped_at: string | null;
  delivered_at: string | null;
  num_of_item: number;
  customer_name: string;
  customer_email: string;
}

export interface PagedResponse<T> {
  content: T[];
  page_number: number;
  page_size: number;
  total_elements: number;
  total_pages: number;
  is_first: boolean;
  is_last: boolean;
}

export interface ApiError {
  error: string;
  message: string;
  status: number;
  path: string;
  timestamp: string;
}
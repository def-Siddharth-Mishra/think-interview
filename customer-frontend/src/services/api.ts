import axios from 'axios';
import { Customer, Order, PagedResponse, ApiError } from '../types/api';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add response interceptor for better error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    if (error.response?.data) {
      throw error.response.data as ApiError;
    }
    throw new Error('Network error occurred - please check if the backend is running');
  }
);

export interface GetCustomersParams {
  page?: number;
  size?: number;
  search?: string;
  country?: string;
}

export interface GetOrdersParams {
  page?: number;
  size?: number;
}

export class CustomerAPI {
  static async getCustomers(params: GetCustomersParams = {}): Promise<PagedResponse<Customer>> {
    try {
      const response = await api.get<PagedResponse<Customer>>('/customers', {
        params: {
          page: params.page || 0,
          size: params.size || 20,
          ...(params.search && { search: params.search }),
          ...(params.country && { country: params.country }),
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching customers:', error);
      throw error;
    }
  }

  static async getCustomerById(id: number): Promise<Customer> {
    try {
      const response = await api.get<Customer>(`/customers/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching customer ${id}:`, error);
      throw error;
    }
  }

  static async getCustomerOrders(
    customerId: number,
    params: GetOrdersParams = {}
  ): Promise<PagedResponse<Order>> {
    try {
      const response = await api.get<PagedResponse<Order>>(
        `/customers/${customerId}/orders`,
        {
          params: {
            page: params.page || 0,
            size: params.size || 10,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(`Error fetching orders for customer ${customerId}:`, error);
      throw error;
    }
  }

  static async getOrderById(orderId: number): Promise<Order> {
    try {
      const response = await api.get<Order>(`/orders/${orderId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching order ${orderId}:`, error);
      throw error;
    }
  }

  static async getTotalCustomerCount(): Promise<number> {
    try {
      const response = await api.get<number>('/customers/count');
      return response.data;
    } catch (error) {
      console.error('Error fetching customer count:', error);
      throw error;
    }
  }

  static async customerExists(id: number): Promise<boolean> {
    try {
      const response = await api.get<boolean>(`/customers/${id}/exists`);
      return response.data;
    } catch (error) {
      console.error(`Error checking if customer ${id} exists:`, error);
      throw error;
    }
  }
}

export default CustomerAPI;

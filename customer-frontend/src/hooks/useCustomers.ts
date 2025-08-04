import { useState, useEffect, useCallback } from 'react';
import { Customer, PagedResponse, ApiError } from '../types/api';
import { CustomerAPI, GetCustomersParams } from '../services/api';

interface UseCustomersState {
  customers: Customer[];
  loading: boolean;
  error: string | null;
  pagination: {
    currentPage: number;
    totalPages: number;
    totalElements: number;
    pageSize: number;
    isFirst: boolean;
    isLast: boolean;
  };
}

interface UseCustomersActions {
  fetchCustomers: (params?: GetCustomersParams) => Promise<void>;
  searchCustomers: (searchTerm: string) => Promise<void>;
  filterByCountry: (country: string) => Promise<void>;
  goToPage: (page: number) => Promise<void>;
  clearError: () => void;
  refresh: () => Promise<void>;
}

export const useCustomers = (initialPageSize: number = 20): UseCustomersState & UseCustomersActions => {
  const [state, setState] = useState<UseCustomersState>({
    customers: [],
    loading: true,
    error: null,
    pagination: {
      currentPage: 0,
      totalPages: 0,
      totalElements: 0,
      pageSize: initialPageSize,
      isFirst: true,
      isLast: true,
    },
  });

  const [currentParams, setCurrentParams] = useState<GetCustomersParams>({
    page: 0,
    size: initialPageSize,
  });

  const fetchCustomers = useCallback(async (params: GetCustomersParams = {}) => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      const mergedParams = { ...currentParams, ...params };
      const response: PagedResponse<Customer> = await CustomerAPI.getCustomers(mergedParams);
      
      setState(prev => ({
        ...prev,
        customers: response.content,
        loading: false,
        pagination: {
          currentPage: response.page_number,
          totalPages: response.total_pages,
          totalElements: response.total_elements,
          pageSize: response.page_size,
          isFirst: response.is_first,
          isLast: response.is_last,
        },
      }));
      
      setCurrentParams(mergedParams);
    } catch (error) {
      const errorMessage = error instanceof Error 
        ? error.message 
        : (error as ApiError).message || 'Failed to fetch customers';
      
      setState(prev => ({
        ...prev,
        loading: false,
        error: errorMessage,
      }));
    }
  }, [currentParams]);

  const searchCustomers = useCallback(async (searchTerm: string) => {
    const params: GetCustomersParams = {
      page: 0,
      size: currentParams.size,
      search: searchTerm || undefined,
    };
    await fetchCustomers(params);
  }, [fetchCustomers, currentParams.size]);

  const filterByCountry = useCallback(async (country: string) => {
    const params: GetCustomersParams = {
      page: 0,
      size: currentParams.size,
      country: country || undefined,
    };
    await fetchCustomers(params);
  }, [fetchCustomers, currentParams.size]);

  const goToPage = useCallback(async (page: number) => {
    const params: GetCustomersParams = {
      ...currentParams,
      page,
    };
    await fetchCustomers(params);
  }, [fetchCustomers, currentParams]);

  const clearError = useCallback(() => {
    setState(prev => ({ ...prev, error: null }));
  }, []);

  const refresh = useCallback(async () => {
    await fetchCustomers(currentParams);
  }, [fetchCustomers, currentParams]);

  // Initial fetch
  useEffect(() => {
    fetchCustomers();
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return {
    ...state,
    fetchCustomers,
    searchCustomers,
    filterByCountry,
    goToPage,
    clearError,
    refresh,
  };
};
import React from 'react';
import { Customer } from '../types/api';

interface CustomerCardProps {
  customer: Customer;
  onClick?: (customer: Customer) => void;
  className?: string;
}

const CustomerCard: React.FC<CustomerCardProps> = ({ customer, onClick, className = '' }) => {
  const handleClick = () => {
    if (onClick) {
      onClick(customer);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const getOrderCountBadgeColor = (count: number) => {
    if (count === 0) return 'bg-gray-100 text-gray-800';
    if (count <= 2) return 'bg-yellow-100 text-yellow-800';
    if (count <= 5) return 'bg-green-100 text-green-800';
    return 'bg-blue-100 text-blue-800';
  };

  return (
    <div
      className={`bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow duration-200 ${
        onClick ? 'cursor-pointer hover:border-blue-300' : ''
      } ${className}`}
      onClick={handleClick}
    >
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <div className="flex items-center space-x-3">
            <div className="flex-shrink-0">
              <div className="h-10 w-10 rounded-full bg-blue-500 flex items-center justify-center">
                <span className="text-sm font-medium text-white">
                  {customer.first_name.charAt(0)}{customer.last_name.charAt(0)}
                </span>
              </div>
            </div>
            <div className="flex-1 min-w-0">
              <h3 className="text-lg font-medium text-gray-900 truncate">
                {customer.first_name} {customer.last_name}
              </h3>
              <p className="text-sm text-gray-500 truncate">{customer.email}</p>
            </div>
          </div>
          
          <div className="mt-4 grid grid-cols-2 gap-4 text-sm">
            <div>
              <span className="text-gray-500">Location:</span>
              <p className="text-gray-900 truncate">
                {customer.city}, {customer.country}
              </p>
            </div>
            <div>
              <span className="text-gray-500">Age:</span>
              <p className="text-gray-900">{customer.age}</p>
            </div>
            <div>
              <span className="text-gray-500">Traffic Source:</span>
              <p className="text-gray-900">{customer.traffic_source}</p>
            </div>
            <div>
              <span className="text-gray-500">Joined:</span>
              <p className="text-gray-900">{formatDate(customer.created_at)}</p>
            </div>
          </div>
        </div>
        
        <div className="flex-shrink-0 ml-4">
          <span
            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getOrderCountBadgeColor(
              customer.order_count
            )}`}
          >
            {customer.order_count} {customer.order_count === 1 ? 'order' : 'orders'}
          </span>
        </div>
      </div>
      
      {onClick && (
        <div className="mt-4 pt-4 border-t border-gray-100">
          <div className="flex items-center text-sm text-blue-600">
            <span>View details</span>
            <svg className="ml-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
            </svg>
          </div>
        </div>
      )}
    </div>
  );
};

export default CustomerCard;
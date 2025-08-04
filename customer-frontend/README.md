# Customer Management Frontend

A modern React TypeScript application for managing customer data with a clean, responsive UI built with Tailwind CSS.

## 🚀 Features

- **Customer List View** - Browse all customers in grid or table format
- **Real-time Search** - Search customers by name or email with debounced input
- **Pagination** - Navigate through large datasets efficiently
- **Customer Details** - View detailed customer information and recent orders
- **Responsive Design** - Works seamlessly on desktop and mobile devices
- **Error Handling** - Comprehensive error states and user feedback
- **Loading States** - Smooth loading indicators for better UX
- **TypeScript** - Full type safety and better developer experience

## 🛠️ Tech Stack

- **React 19** with TypeScript
- **Tailwind CSS** for styling
- **Axios** for API communication
- **Custom Hooks** for state management
- **Responsive Design** with mobile-first approach

## 📋 Prerequisites

- Node.js 16+ 
- npm or yarn
- Customer API running on `http://localhost:8080`

## 🚀 Getting Started

### 1. Install Dependencies
```bash
npm install
```

### 2. Environment Setup
Create a `.env` file in the root directory:
```env
REACT_APP_API_URL=http://localhost:8080/api
```

### 3. Start Development Server
```bash
npm start
```

The application will open at `http://localhost:3000`

### 4. Build for Production
```bash
npm run build
```

## 🏗️ Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── CustomerCard.tsx
│   ├── CustomerList.tsx
│   ├── CustomerDetailModal.tsx
│   ├── SearchInput.tsx
│   ├── Pagination.tsx
│   ├── LoadingSpinner.tsx
│   └── ErrorAlert.tsx
├── hooks/              # Custom React hooks
│   └── useCustomers.ts
├── services/           # API service layer
│   └── api.ts
├── types/              # TypeScript type definitions
│   └── api.ts
├── App.tsx             # Main application component
└── index.tsx           # Application entry point
```

## 🎨 UI Components

### CustomerList
- **Grid View**: Card-based layout showing customer summaries
- **Table View**: Compact tabular format for data-heavy viewing
- **Search**: Real-time search with 300ms debounce
- **Pagination**: Navigate through customer pages

### CustomerCard
- **Customer Avatar**: Initials-based avatar with color coding
- **Order Count Badge**: Visual indicator of customer activity
- **Location & Details**: Key customer information at a glance
- **Click to View**: Expandable detail view

### CustomerDetailModal
- **Full Customer Profile**: Complete customer information
- **Recent Orders**: Last 5 orders with status indicators
- **Responsive Layout**: Adapts to different screen sizes

## 🔧 API Integration

The frontend integrates with the Customer API through a typed service layer:

```typescript
// Get paginated customers
CustomerAPI.getCustomers({ page: 0, size: 20, search: 'john' })

// Get customer details
CustomerAPI.getCustomerById(123)

// Get customer orders
CustomerAPI.getCustomerOrders(123, { page: 0, size: 5 })
```

## 📱 Responsive Design

- **Mobile First**: Optimized for mobile devices
- **Breakpoints**: 
  - `sm`: 640px+
  - `md`: 768px+
  - `lg`: 1024px+
  - `xl`: 1280px+

## 🎯 Key Features

### Search Functionality
- Debounced search input (300ms delay)
- Searches across customer name and email
- Real-time results with loading states

### Pagination
- Server-side pagination for performance
- Configurable page sizes
- Smart page number display with ellipsis

### Error Handling
- Network error recovery
- User-friendly error messages
- Retry mechanisms

### Loading States
- Skeleton loading for better perceived performance
- Spinner indicators for actions
- Disabled states during operations

## 🧪 Testing

```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage
```

## 🚀 Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Static Hosting
The `build` folder can be deployed to any static hosting service:
- Netlify
- Vercel
- AWS S3 + CloudFront
- GitHub Pages

### Environment Variables
Set the following environment variables for production:
```env
REACT_APP_API_URL=https://your-api-domain.com/api
```

## 🔍 Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 📈 Performance

- **Code Splitting**: Automatic code splitting with React.lazy
- **Optimized Images**: Responsive image loading
- **Debounced Search**: Reduces API calls
- **Pagination**: Handles large datasets efficiently

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is part of the Think41 interview process.

---

**Ready to manage customers with style! 🎉**